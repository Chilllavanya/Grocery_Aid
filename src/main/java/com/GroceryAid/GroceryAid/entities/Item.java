package com.GroceryAid.GroceryAid.entities;

import com.GroceryAid.GroceryAid.dtos.ItemDto;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "Item")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Item {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "item_id")
	private Long itemID;
	@Column(name = "walmart_sku")
	private Long walmartSKU;
	@Column(name = "item_name")
	private String itemName;
	@Column(name = "item_price")
	private float itemPrice;
	@Column(name = "item_quantity")
	private int itemQuantity;
	
	public Item(ItemDto itemDto)
	{
		this.itemID =itemDto.getItemID();
		this.walmartSKU=itemDto.getWalmartSKU();
		this.itemName=itemDto.getItemName();
		this.itemPrice=itemDto.getItemPrice();
		this.itemQuantity=itemDto.getItemQuantity();
	}
}
