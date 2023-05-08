package com.GroceryAid.GroceryAid.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DeleteItemDto {
	
	private String userName;
	private List<Long> itemIDs = new ArrayList<>(); // user will click on delete button so no need to go by name
	// try going by List<Item> itself instead of name (if possible)
}
