package com.GroceryAid.GroceryAid.controllers;

import com.GroceryAid.GroceryAid.dtos.GroceryListDto;
import com.GroceryAid.GroceryAid.dtos.ItemDto;
import com.GroceryAid.GroceryAid.dtos.UserDto;
import com.GroceryAid.GroceryAid.entities.GroceryList;
import com.GroceryAid.GroceryAid.entities.Item;
import com.GroceryAid.GroceryAid.repositories.GroceryListRepository;
import com.GroceryAid.GroceryAid.repositories.ItemRepository;
import com.GroceryAid.GroceryAid.repositories.UserRepository;
import com.GroceryAid.GroceryAid.services.GroceryListService;
import com.GroceryAid.GroceryAid.services.WalmartSignatureGenerator;
import com.GroceryAid.GroceryAid.services.WalmartSignatureGenerator.WalmartResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpSession;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Objects;

@RestController
@RequestMapping("/api/v1/glist")
public class GroceryListController {
	@Autowired
	private GroceryListService groceryListService;
	@Autowired
	private GroceryListRepository groceryListRepository;

	@Autowired
	private ItemRepository itemRepository;
	
	@Autowired private UserRepository userRepository;
	
	@Autowired private WalmartSignatureGenerator walmartSignatureGenerator;
	
	public WalmartSignatureGenerator.WalmartResponse getWalmartItem(String itemName) {
		String url = "https://developer.api.walmart.com/api-proxy/service/affil/product/v2/search?query="
				+itemName+"&numItems=1";
		System.out.println(url);
		
		WalmartSignatureGenerator.WalmartSignature signature = walmartSignatureGenerator.generate();

		HttpHeaders headers = new HttpHeaders();
		headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
		headers.setContentType(MediaType.APPLICATION_JSON);
		
		headers.set("WM_SEC.KEY_VERSION", signature.getKeyVersion());
		headers.set("WM_CONSUMER.ID", signature.getConsumerID());
		headers.set("WM_CONSUMER.INTIMESTAMP", signature.getTimestamp().toString());
		headers.set("WM_SEC.AUTH_SIGNATURE", signature.getAuthSignature());
		
		System.out.println(signature.getTimestamp());
		
		// Get Walmart Response
		RestTemplate template = new RestTemplate();
		String walmartResponse_str = template.exchange(url,
				HttpMethod.GET,
				new HttpEntity<String>("parameters", headers),
				String.class
		).getBody();
		
		System.out.println(walmartResponse_str);
		
		WalmartResponse walmartResponse;
		//Parse JSON
		try {
			walmartResponse = (new ObjectMapper())
					.readValue(
							walmartResponse_str,
							WalmartSignatureGenerator.WalmartResponseMapper.class
					).getItems().get(0); // there should only be 1 object, so taking [0] should work
		} catch (JsonProcessingException e) {
			throw new RuntimeException(e);
		}
		
		return walmartResponse;
	}

	@PostMapping(value = "/get-user-glists")
	public Collection<GroceryListDto> getUserGroceryLists(HttpSession session) {
		UserDto s_userDTO = (UserDto) session.getAttribute("user");
		var user_op = userRepository.findById(s_userDTO.getUserID());
		if (user_op.isEmpty())
			return null;

		var g_lists = user_op.get().getGroceryListSet();
		Collection<GroceryListDto> g_list_dtos = new ArrayList<>();

		for (var list : g_lists) {
			g_list_dtos.add(new GroceryListDto(list));
		}

		return g_list_dtos;
	}
	
	@PostMapping("/get_glist")
	public GroceryListDto getGroceryList(@RequestBody Long id, HttpSession session) {
		var gList = groceryListRepository.findById(id);
		UserDto userDto = (UserDto) session.getAttribute("user");
		if (userDto == null)
			return null;
		if (gList.isEmpty() || !Objects.equals(gList.get().getUser().getUserID(), userDto.getUserID()))
			return null;
		
		return new GroceryListDto(gList.get());
	}

	@PostMapping("/create")
	public GroceryListDto createGroceryList(@RequestBody String gListName, HttpSession session) {
		var user_op = userRepository.findById(
			((UserDto) session.getAttribute("user")).getUserID()
		);
		
		return user_op.map(user -> groceryListService.createList(gListName, user)).orElse(null);
		
	}
	@PostMapping("/delete")
	public boolean deleteGroceryList(@RequestBody GroceryListDto gListDto, HttpSession session) {
		UserDto userDto = (UserDto) session.getAttribute("user");
		if (userDto == null)
			return false;

		var user_OP = userRepository.findById(userDto.getUserID());
		if (user_OP.isEmpty())
			return false;
		var user = user_OP.get();

		var gList_OP = groceryListRepository.findByName(gListDto.getName());
		if (gList_OP.isEmpty())
			return false;
		var gList = gList_OP.get();

		if (gList.getUser() != user)
			return false;

		for (var item : gList.getItemsList()) {
			itemRepository.delete(item);
		}

		groceryListRepository.delete(gList);
		session.setAttribute("user", new UserDto(user_OP.get()));

		return true;
	}

	@PostMapping("/add-item/{gListID}")
	public GroceryListDto addItemToGroceryList(
			@RequestBody ItemDto itemDto,
			@PathVariable("gListID") Long gListID, HttpSession session
	) {
		var gList_OP = groceryListRepository.findById(gListID);
		UserDto userDto = (UserDto) session.getAttribute("user");
		if (userDto == null)
			return null;
		if  (
				gList_OP.isEmpty() ||
				!Objects.equals(gList_OP.get().getUser().getUserID(), userDto.getUserID())
			)
		{
			return null;
		}
		
		GroceryList gList = gList_OP.get();
		
		var item = new Item(itemDto);
		WalmartResponse walmartResponse = getWalmartItem(item.getItemName());
		item.setItemName(walmartResponse.getName());
		item.setItemPrice(walmartResponse.getSalePrice());
		item.setWalmartSKU(walmartResponse.getItemId());
		
		gList.getItemsList().add(item);

		itemRepository.save(item);
		groceryListRepository.save(gList);

		return new GroceryListDto(gList);
	}
	@PostMapping("/delete-item/{gListID}")
	public GroceryListDto deleteItemToGroceryList(
			@RequestBody ItemDto itemDto,
			@PathVariable("gListID") Long gListID, HttpSession session
	) {
		var gList_OP = groceryListRepository.findById(gListID);
		if  (
				gList_OP.isEmpty() ||
				!Objects.equals(gList_OP.get().getUser().getUserID(),
						((UserDto) session.getAttribute("user")).getUserID())
			)
		{
			return null;
		}

		System.out.println(itemDto.getItemID());

		GroceryList gList = gList_OP.get();
		gList.getItemsList().removeIf(x -> Objects.equals(x.getItemID(), itemDto.getItemID()));

		itemRepository.deleteById(itemDto.getItemID());
		groceryListRepository.save(gList);

		return new GroceryListDto(gList);
	}
}
