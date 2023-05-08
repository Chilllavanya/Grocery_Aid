package com.GroceryAid.GroceryAid.repositories;

import com.GroceryAid.GroceryAid.entities.Cart;
import com.GroceryAid.GroceryAid.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository
public interface CartRepository extends JpaRepository<Cart, Long> {
    Optional<Cart> findByUser(User userID);

}
