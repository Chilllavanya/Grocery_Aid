package com.GroceryAid.GroceryAid.controllers;

import com.GroceryAid.GroceryAid.dtos.CartDto;
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
	
	@PostMapping("/add")
	public String addToCart(@RequestBody CartDto cartDto)	{
		cartService.addToCart(cartDto);
		return "";
	}
	
	@PostMapping("/get-cart")
	public ResponseEntity<CartDto> getCart(@RequestBody UserDto userDto) {
		CartDto cartDto = cartService.getCartDetails(userDto.getUsername());
		return new ResponseEntity<>(cartDto, HttpStatus.OK);
	}
	
	@DeleteMapping("/clear")
	public void clearCart(@RequestBody UserDto user){
		cartService.clearCart(user.getUsername());
	}
	
}
