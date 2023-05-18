package com.GroceryAid.GroceryAid.dtos;

import com.GroceryAid.GroceryAid.entities.GroceryList;
import com.GroceryAid.GroceryAid.entities.Item;
import com.GroceryAid.GroceryAid.entities.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GroceryListDto implements Serializable {
    private Long listID;
    private String name;
    Collection<ItemDto> itemsList;
    private float totalPrice = 0;
    private User user;
    
    public GroceryListDto(GroceryList gList)
    {
        this.listID = gList.getGroceryID();
        this.name = gList.getName();
        this.itemsList = new ArrayList<>();
        for (var item : gList.getItemsList())
        {
            var itemDTO = new ItemDto(item);
            itemsList.add(itemDTO);
            totalPrice += itemDTO.getItemAmount();
        }
        this.user = gList.getUser();
    }
}
