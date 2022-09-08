package com.smart.controller;

import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.smart.entities.Contact;
import com.smart.entities.User;
import com.smart.repositeries.ContactRepository;
import com.smart.repositeries.UserRepository;

@RestController
public class SearchController {
	
	@Autowired
	ContactRepository contactRepository;
	
	@Autowired
	UserRepository userRepository;
	
	@GetMapping("/contact/search/{query}")
	public ResponseEntity<?> contactSearchByName(@PathVariable("query") String query,Principal principal){
		//System.out.println("query = "+query);
		User user = userRepository.getUserByUserName(principal.getName());
		List<Contact> contacts = contactRepository.getContactsByQuery(user.getId(), query);
				//contactRepository.findByNameContaining(query);
		//contacts.forEach(contact->System.out.println(contact.getName()));
		//if(contacts.isEmpty()) return null;
		
		return ResponseEntity.ok(contacts);
	}
	

	
	
}
