package com.GroceryAid.GroceryAid.dtos;

import com.GroceryAid.GroceryAid.entities.Cart;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CartDto implements Serializable {
    private Long cartID;
    //private Set<GroceryListDto> groceryListDtoSet = new HashSet<>();
    
    private GroceryListDto groceries = new GroceryListDto();
    
//    private String userName;

    private UserDto user;

    public CartDto(Cart cart) {
        this.cartID = cart.getCartID();
        this.groceries = new GroceryListDto(cart.getGroceries());
        this.user = new UserDto(cart.getUser());
    }
}