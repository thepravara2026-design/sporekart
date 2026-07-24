package com.sporekart.cart.infrastructure.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CartItemJpaRepository extends JpaRepository<CartItemEntity, String> {
    List<CartItemEntity> findByCartId(String cartId);
    void deleteByCartId(String cartId);
}
