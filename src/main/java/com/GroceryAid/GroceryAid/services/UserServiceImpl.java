package com.GroceryAid.GroceryAid.services;

import com.GroceryAid.GroceryAid.dtos.UserDto;
import com.GroceryAid.GroceryAid.entities.User;
import com.GroceryAid.GroceryAid.repositories.UserRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public UserDto getLoggedInUser(HttpSession session) {
        UserDto user =  (UserDto) session.getAttribute("user");

        var user_op = userRepository.findById(user.getUserID());
        if (user_op.isEmpty())
            return null;

        session.setAttribute("user", new UserDto(user_op.get()));

        user =  (UserDto) session.getAttribute("user");
        return user;
    }

    @Override
    public String addUser(UserDto userDto) {
        String encodedPass = passwordEncoder.encode(userDto.getPassword());
        System.out.println(userDto.getPassword());
        System.out.println(encodedPass);
        userDto.setPassword(encodedPass);
        userRepository.save(new User(userDto));

        return "login";
    }

    @Override
    public UserDto userLogin(UserDto userDto) {
        Optional<User> user = userRepository.findByUserName(userDto.getUsername());
        if (user.isEmpty())
            return null;

        if (passwordEncoder.matches(userDto.getPassword(), user.get().getPassword()))
        {
            System.out.println(user.get().getUserID());
            return new UserDto(user.get());
        } else {
            return null;
        }
    }
}
