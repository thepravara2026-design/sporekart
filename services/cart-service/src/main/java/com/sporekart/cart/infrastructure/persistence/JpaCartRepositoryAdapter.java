package com.sporekart.cart.infrastructure.persistence;

import com.sporekart.cart.domain.model.Cart;
import com.sporekart.cart.domain.repository.CartRepositoryPort;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Primary
@Repository
@Transactional
public class JpaCartRepositoryAdapter implements CartRepositoryPort {

    private final CartJpaRepository cartJpaRepository;

    public JpaCartRepositoryAdapter(CartJpaRepository cartJpaRepository) {
        this.cartJpaRepository = cartJpaRepository;
    }

    @Override
    public Cart save(Cart cart) {
        CartEntity entity = CartEntity.fromDomain(cart);
        CartEntity saved = cartJpaRepository.save(entity);
        return saved.toDomain();
    }

    @Override
    public Optional<Cart> findById(String id) {
        return cartJpaRepository.findById(id).map(CartEntity::toDomain);
    }

    @Override
    public List<Cart> findByCustomerId(String customerId) {
        return cartJpaRepository.findByCustomerId(customerId).stream()
                .map(CartEntity::toDomain)
                .toList();
    }

    @Override
    public void deleteById(String id) {
        cartJpaRepository.deleteById(id);
    }
}
