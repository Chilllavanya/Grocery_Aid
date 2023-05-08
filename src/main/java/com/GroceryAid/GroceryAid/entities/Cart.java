package com.GroceryAid.GroceryAid.entities;

import com.GroceryAid.GroceryAid.dtos.CartDto;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "Cart")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Cart {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "cart_id")
    private Long cartID;

    @OneToOne
    private GroceryList groceries = new GroceryList();

   // @OneToOne(targetEntity = User.class, cascade = CascadeType.ALL)
    /*@JoinTable(
        name = "Bridge",
        joinColumns = @JoinColumn(name = "cartId"),
        inverseJoinColumns = @JoinColumn(name = "userId")
    )*/
   @OneToOne
   @JoinColumn(name = "user_id")
    private User user;

   public Cart(CartDto cartDto)
   {
       this.cartID = cartDto.getCartID();
       this.groceries = new GroceryList(cartDto.getGroceries());
       this.user = new User(cartDto.getUser());
   }
}
