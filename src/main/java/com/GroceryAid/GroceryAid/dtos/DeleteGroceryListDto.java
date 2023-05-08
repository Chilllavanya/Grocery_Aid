package com.GroceryAid.GroceryAid.dtos;

import com.GroceryAid.GroceryAid.entities.GroceryList;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DeleteGroceryListDto {
	
	private String userName;
	private List<GroceryList> groceryLists = new ArrayList<>();
	
}
