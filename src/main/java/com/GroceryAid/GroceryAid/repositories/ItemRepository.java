package com.GroceryAid.GroceryAid.repositories;

import com.GroceryAid.GroceryAid.entities.Item;
import com.sun.source.tree.OpensTree;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ItemRepository extends JpaRepository<Item, Long> {
	Optional<Item> findByWalmartSKU(Long sku);
}
