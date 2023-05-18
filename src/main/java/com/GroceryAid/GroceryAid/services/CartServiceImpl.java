package com.GroceryAid.GroceryAid.services;

import com.GroceryAid.GroceryAid.dtos.*;
import com.GroceryAid.GroceryAid.entities.Cart;
import com.GroceryAid.GroceryAid.entities.GroceryList;
import com.GroceryAid.GroceryAid.entities.Item;
import com.GroceryAid.GroceryAid.entities.User;
import com.GroceryAid.GroceryAid.repositories.CartRepository;
import com.GroceryAid.GroceryAid.repositories.GroceryListRepository;
import com.GroceryAid.GroceryAid.repositories.ItemRepository;
import com.GroceryAid.GroceryAid.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class CartServiceImpl implements CartService {
	
	@Autowired
	private CartRepository cartRepository;
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private GroceryListRepository groceryListRepository;
	
	@Autowired
	private ItemRepository itemRepository;
	
	@Override
	public boolean addToCart(CartDto cartDto) {
		Cart cart = new Cart(cartDto);
		
		if (cartDto.getUser() == null)
			return false;

		User user = new User(cartDto.getUser());
		
		Optional<Cart> cartDBop = cartRepository.findByUser(user);
		if (cartDBop.isPresent()) {
			updateCart(cartDto, cartDBop.get());
		} else {
			// there should always be 1 cart for a user, create a new cart when creating a new user
			cart.setUser(user);
			GroceryList groceryList = new GroceryList(cartDto.getGroceries());
			
			List<Item> items = itemRepository.saveAll(groceryList.getItemsList());
			groceryList.setItemsList(items);
			groceryList = groceryListRepository.save(groceryList);
			
			cart.setGroceries(groceryList);
			
			cartRepository.save(cart);
		}
		return true;
	}
	
	@Override
	public CartDto getCartDetails(String userName) {
		Optional<User> userOp = userRepository.findByUserName(userName);
		if (userOp.isEmpty())
			return null;
		User user = userOp.get();
		Optional<Cart> cartOp = cartRepository.findByUser(user);
		
		if (cartOp.isEmpty())
			return null;
		
		return new CartDto(cartOp.get());
	}
	
	public boolean clearCart(String username){ // should not be able to delete carts only gLists and items
		Optional<User> userOp = userRepository.findByUserName(username);
		if (userOp.isEmpty())
			return false;
		
		User user = userOp.get();
		Optional<Cart> cartOp = cartRepository.findByUser(user);
		
		if (cartOp.isEmpty())
			return false;
		
		Cart cart = cartOp.get();
		cart.setGroceries(null);
		
		cartRepository.save(cart);
		return true;
	}
	
	protected void updateCart(CartDto cartDto, Cart cartDB) {
		
		GroceryList groceryList = cartDB.getGroceries();
		
		Collection<Item> itemsDB = groceryList.getItemsList();
		
		Set<Item> finalItemList = new HashSet<>();
		
		for (ItemDto i : cartDto.getGroceries().getItemsList()) {
			Item item = itemsDB.stream().filter(x -> x.getWalmartSKU() == i.getWalmartSKU()).findAny().orElse(null);
			
			if (item != null) {
				item.setItemQuantity(i.getItemQuantity() + item.getItemQuantity());
				
				finalItemList.add(itemRepository.save(item));
			} else {
				
				finalItemList.add(itemRepository.save(new Item(i)));
			}
		}
		
		//itemsDB = (Set<Item>) itemRepository.saveAll(itemsDB);
		groceryList.setItemsList(finalItemList);
		groceryList = groceryListRepository.save(groceryList);
		cartDB.setGroceries(groceryList);
		cartRepository.save(cartDB);
		
	}
}
