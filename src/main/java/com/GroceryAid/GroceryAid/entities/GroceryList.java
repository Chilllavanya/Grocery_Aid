package com.GroceryAid.GroceryAid.entities;

import com.GroceryAid.GroceryAid.dtos.GroceryListDto;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.persistence.CascadeType;

import java.util.ArrayList;
import java.util.Collection;

@Entity
//@Table(name = "Groceries")
//@Table(name = "grocery_items")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class GroceryList {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "grocery_id")
	private Long groceryID;

	@Column(name="name", unique = true)
	private String name;
	
	@OneToMany(cascade = CascadeType.ALL)
	@Column(name = "item_id")
	Collection<Item> itemsList;
	
	@ManyToOne
	@JoinColumn(name = "user_id")
	@JsonBackReference
	private User user; // gList should have access to user
	
	public GroceryList(GroceryListDto groceryListDto) {
		this.groceryID = groceryListDto.getListID();
		this.name = groceryListDto.getName();
		this.itemsList = new ArrayList<>();
		for (var item : groceryListDto.getItemsList())
		{
			this.itemsList.add(new Item(item));
		}
		this.user = groceryListDto.getUser();
	}

	public GroceryList(String name, Collection<Item> itemsList, User user) {
		this.name = name;
		this.itemsList = itemsList;
		this.user = user;
	}
	
	public void setGroceryID(Long groceryID) {
		this.groceryID = groceryID;
	}
	
	public void setItemsList(Collection<Item> itemsList) {
		this.itemsList = itemsList;
	}
	
	public Collection<Item> getItemsList() {
		return itemsList;
	}
}
