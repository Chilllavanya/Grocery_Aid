package com.GroceryAid.GroceryAid.services;

import com.GroceryAid.GroceryAid.dtos.DeleteItemDto;
import com.GroceryAid.GroceryAid.dtos.GroceryListDto;
import com.GroceryAid.GroceryAid.dtos.UserDto;
import com.GroceryAid.GroceryAid.entities.GroceryList;
import com.GroceryAid.GroceryAid.entities.Item;
import com.GroceryAid.GroceryAid.entities.User;
import com.GroceryAid.GroceryAid.repositories.GroceryListRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class GroceryListServiceImpl implements GroceryListService {
	@Autowired
	GroceryListRepository gLRepo;
	
	@Override
	public void addItem(GroceryListDto gListDto) {
	
	}
	
	@Override
	public void deleteItem(GroceryListDto gListDto, Item item) {
	
	}
	
	@Override
	public GroceryListDto createList(String name, User user) {
		GroceryList gList = new GroceryList(name, new ArrayList<>(), user);

		System.out.println(gList.getName());
		System.out.println(gList.getGroceryID());

		gLRepo.save(gList);

		System.out.println(user.getGroceryListSet().size());
		System.out.println(gLRepo.findById(gList.getGroceryID()).get().getUser().getUserName());

		return new GroceryListDto(gList);
	}
	
	@Override
	public void deleteList(GroceryListDto gListDto) {
		var gList_op = gLRepo.findById(gListDto.getListID());
		if (gList_op.isEmpty())
			return;

		var gList = gList_op.get();
		var delItemDto = new DeleteItemDto();
//		delItemDto.setItemIds(gList.getItemsList());
		
		
		gLRepo.delete(gList);
	}
}
