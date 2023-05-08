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
    private User user;
    
    public GroceryListDto(GroceryList gList)
    {
        this.listID = gList.getGroceryID();
        this.name = gList.getName();
        this.itemsList = new ArrayList<>();
        for (var item : gList.getItemsList())
        {
            itemsList.add(new ItemDto(item));
        }
        this.user = gList.getUser();
    }
}
