package com.GroceryAid.GroceryAid.controllers;

import com.GroceryAid.GroceryAid.dtos.CartDto;
import com.GroceryAid.GroceryAid.dtos.DeleteItemDto;
import com.GroceryAid.GroceryAid.dtos.UserDto;
import com.GroceryAid.GroceryAid.services.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/cart")
public class CartController {
	
	
	@Autowired
	private CartService cartService;
	
	@PostMapping("/add") // how do you call other api endpoints (such as /gList) in this method?
	public String addToCart(@RequestBody CartDto cartDto)	{
		cartService.addToCart(cartDto);
		
		return "";
	}
	
	@GetMapping("/getcartdetails")
	public ResponseEntity<CartDto> getCart(@RequestBody UserDto userDto) throws Exception {
		CartDto cartDto = cartService.getCartDetails(userDto.getUsername());
		if (cartDto.getCartID() == null) {
			throw new  Exception("Cart is empty");
		}
		
		return new ResponseEntity<>(cartDto, HttpStatus.OK);
	}
	
	@DeleteMapping("/items")
	public void deleteItems(@RequestBody DeleteItemDto deleteItemDto) {
		cartService.deleteItems(deleteItemDto);
	}
	
	@DeleteMapping("/clear")
	public void clearCart (@RequestBody UserDto user){
		cartService.clearCart(user.getUsername());
	}
	
}
