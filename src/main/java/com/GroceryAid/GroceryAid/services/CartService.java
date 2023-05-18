package com.GroceryAid.GroceryAid.services;

import com.GroceryAid.GroceryAid.dtos.CartDto;
import com.GroceryAid.GroceryAid.dtos.DeleteItemDto;
import com.GroceryAid.GroceryAid.dtos.ItemDto;

import java.util.List;

public interface CartService {
//add grocerylisttocart
//        remove grocerylist from cart
	
	
	boolean addToCart(CartDto cartDto);
	CartDto getCartDetails(String userName);
	
	boolean clearCart(String userName);
}
