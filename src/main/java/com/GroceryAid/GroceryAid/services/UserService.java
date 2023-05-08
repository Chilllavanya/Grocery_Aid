package com.GroceryAid.GroceryAid.services;

import com.GroceryAid.GroceryAid.dtos.GroceryListDto;
import com.GroceryAid.GroceryAid.dtos.UserDto;
import com.GroceryAid.GroceryAid.entities.GroceryList;
import jakarta.servlet.http.HttpSession;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface UserService {
    UserDto getLoggedInUser(HttpSession session);

    @Transactional
    String addUser(UserDto userDto);

    UserDto userLogin(UserDto userDto);
}
