package com.sporekart.customer.copilot.service;

import com.sporekart.copilot.PageContext;
import com.sporekart.copilot.UserContext;
import com.sporekart.customer.copilot.domain.ConversationMessage;
import com.sporekart.customer.copilot.domain.CustomerOrder;
import com.sporekart.customer.copilot.domain.CustomerProfile;
import com.sporekart.customer.copilot.domain.ShoppingCartItem;
import com.sporekart.customer.copilot.dto.ContextResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

@Service
public class CustomerContextService {

    private static final Logger log = LoggerFactory.getLogger(CustomerContextService.class);

    private final Map<String, CustomerProfile> profileStore = new ConcurrentHashMap<>();
    private final Map<String, List<ShoppingCartItem>> cartStore = new ConcurrentHashMap<>();
    private final Map<String, List<CustomerOrder>> orderStore = new ConcurrentHashMap<>();
    private final Map<String, List<String>> viewedProductsStore = new ConcurrentHashMap<>();
    private final Map<String, List<ConversationMessage>> conversationStore = new ConcurrentHashMap<>();

    public CustomerProfile getOrCreateProfile(UserContext userContext) {
        return profileStore.computeIfAbsent(userContext.userId(), id -> {
            log.info("Creating new customer profile for user: {}", id);
            return new CustomerProfile(
                userContext.userId(),
                userContext.userName(),
                userContext.email(),
                userContext.language() != null ? userContext.language() : "en",
                userContext.featureFlags(),
                OffsetDateTime.now()
            );
        });
    }

    public CustomerProfile updateProfile(CustomerProfile profile) {
        profileStore.put(profile.userId(), profile);
        log.debug("Updated profile for user: {}", profile.userId());
        return profile;
    }

    public Optional<CustomerProfile> findProfile(String userId) {
        return Optional.ofNullable(profileStore.get(userId));
    }

    public List<ShoppingCartItem> getCart(String userId) {
        return cartStore.getOrDefault(userId, List.of());
    }

    public void updateCart(String userId, List<ShoppingCartItem> items) {
        cartStore.put(userId, new ArrayList<>(items));
        log.debug("Updated cart for user: {} ({} items)", userId, items.size());
    }

    public void addToCart(String userId, ShoppingCartItem item) {
        cartStore.computeIfAbsent(userId, k -> new CopyOnWriteArrayList<>()).add(item);
        log.debug("Added item to cart for user: {}", userId);
    }

    public List<CustomerOrder> getOrders(String customerId) {
        return orderStore.getOrDefault(customerId, List.of());
    }

    public void recordOrder(CustomerOrder order) {
        orderStore.computeIfAbsent(order.customerId(), k -> new CopyOnWriteArrayList<>()).add(order);
        log.debug("Recorded order {} for customer: {}", order.orderId(), order.customerId());
    }

    public void recordViewedProduct(String userId, String productId) {
        viewedProductsStore.computeIfAbsent(userId, k -> new CopyOnWriteArrayList<>()).add(productId);
        log.debug("Recorded product view: user={}, product={}", userId, productId);
    }

    public List<String> getViewedProducts(String userId) {
        return viewedProductsStore.getOrDefault(userId, List.of());
    }

    public void addConversationMessage(String sessionId, ConversationMessage message) {
        conversationStore.computeIfAbsent(sessionId, k -> new CopyOnWriteArrayList<>()).add(message);
        log.debug("Added conversation message to session: {}", sessionId);
    }

    public List<ConversationMessage> getConversationHistory(String sessionId) {
        return conversationStore.getOrDefault(sessionId, List.of());
    }

    public String determineJourneyStage(String userId) {
        if (!profileStore.containsKey(userId)) {
            return "new";
        }
        var orders = orderStore.getOrDefault(userId, List.of());
        if (orders.isEmpty()) {
            return "browsing";
        }
        var activeOrders = orders.stream().filter(o -> "PENDING".equals(o.status()) || "SHIPPED".equals(o.status())).toList();
        if (!activeOrders.isEmpty()) {
            return "checkout";
        }
        return "returning";
    }

    public ContextResponse assembleContext(String sessionId, PageContext pageContext, UserContext userContext) {
        log.debug("Assembling context for session: {}", sessionId);

        var profile = getOrCreateProfile(userContext);
        var cart = getCart(userContext.userId());
        var orders = getOrders(userContext.userId());
        var viewedProducts = getViewedProducts(userContext.userId());
        var journeyStage = determineJourneyStage(userContext.userId());

        var cartInfo = new HashMap<String, Object>();
        cartInfo.put("items", cart);
        cartInfo.put("itemCount", cart.size());
        cartInfo.put("totalPrice", cart.stream().mapToDouble(ShoppingCartItem::totalPrice).sum());

        var userInfo = new HashMap<String, Object>();
        userInfo.put("userId", profile.userId());
        userInfo.put("userName", profile.userName());
        userInfo.put("email", profile.email());
        userInfo.put("preferredLanguage", profile.preferredLanguage());
        userInfo.put("journeyStage", journeyStage);

        var recentActivity = new HashMap<String, Object>();
        recentActivity.put("recentOrders", orders.stream().limit(5).toList());
        recentActivity.put("viewedProducts", viewedProducts.stream().limit(10).toList());
        recentActivity.put("conversationLength", getConversationHistory(sessionId).size());

        return new ContextResponse(
            sessionId,
            pageContext != null ? pageContext.pageUrl() : "",
            userInfo,
            cartInfo,
            recentActivity
        );
    }
}
