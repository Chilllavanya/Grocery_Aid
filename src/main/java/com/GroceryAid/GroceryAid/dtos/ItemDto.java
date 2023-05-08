package com.GroceryAid.GroceryAid.dtos;

import com.GroceryAid.GroceryAid.entities.Item;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ItemDto {
	private Long itemID;
	private Long walmartSKU;
	private String itemName;
	private float itemPrice;
	private int itemQuantity;
	
	public ItemDto(Item item)
	{
		this.itemID =item.getItemID();
		this.walmartSKU=item.getWalmartSKU();
		this.itemName=item.getItemName();
		this.itemPrice=item.getItemPrice();
		this.itemQuantity=item.getItemQuantity();
	}
}
