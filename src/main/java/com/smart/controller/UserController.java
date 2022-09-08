package com.smart.controller;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.Principal;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.smart.entities.Contact;
import com.smart.entities.User;
import com.smart.helper.Message;
import com.smart.repositeries.ContactRepository;
import com.smart.repositeries.UserRepository;

@Controller
@RequestMapping("/user")
public class UserController {
	
	@Autowired
	UserRepository userRepository;
	
	@Autowired
	ContactRepository contactRepository;
	
	
	@RequestMapping("/test")
	@ResponseBody
	public String test() {
		System.out.println("this is test controller inside user");
		return "this is test page user/test";
	}
	
	@ModelAttribute
	public void addCommonData(Model model,Principal principal) {
		String name = principal.getName();
		//System.out.println(name);
		User user = userRepository.getUserByUserName(name);
		model.addAttribute("user", user);
		//System.out.println(user);
		
	}
	
//	@RequestMapping("/")
//	public String index() {
//		System.out.println("this is index controller inside user");
//		return "users/index";
//	}
	@RequestMapping("/")
	public String userDashboard() {
		
		
		return "users/dashboard";
	}
	
	@GetMapping("/add_contact")
	public String openAddContactForm(Model model) {
		
		model.addAttribute("contact",new Contact());
		model.addAttribute("title","Add Contact | Smart Contact Manager");
		
		return "users/add_contact";
	}
	
	
	
	//add new contact or update contact end point
	@PostMapping("/process_add_contact")
	public String processAddContactForm(@ModelAttribute Contact contact,
			@RequestParam("profileImage") MultipartFile file, 
			Principal principal,
			HttpSession session) {
		
		//System.out.println("contact id is = "+contact.getId());
		
		//System.out.println(principal.getName());
		User user = userRepository.getUserByUserName(principal.getName());
		contact.setUser(user);
		try {
			
			
			
			if(file.isEmpty()) {
				//file is empty
			}else {
				//contact with file---> saving file name
				String originalFilename = file.getOriginalFilename();
				contact.setProfile(originalFilename);
				//saving file
				File file2 = new ClassPathResource("static/img").getFile();
				String absolutePath = file2.getAbsolutePath();
				Path path = Paths.get(absolutePath+File.separator+originalFilename);
				
				Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
				//userRepository.save(user);	
				
			}
			//user.getContacts().add(contact);
			contact.setUser(user);
			contactRepository.save(contact);
		
		}catch(Exception e) {
			e.printStackTrace();
			//System.out.println("something went wrong");
			session.setAttribute("message", new Message("Something went wrong please try again !!!","danger"));
		}

		session.setAttribute("message", new Message("Contact is successfully added, add more contact","success"));
		//System.out.println("successfully added contact");
		
		return "users/add_contact";
	}
	
	@GetMapping("/view_contacts_row_no/{crowNo}")
	@ResponseBody
	public void viewContactsRowNo(@PathVariable("crowNo") int crowNo,HttpSession session) {
		System.out.println(crowNo);
		session.setAttribute("crowNo", crowNo);
		System.out.println("session updated with : "+crowNo);
		
	}
	
	@GetMapping(value = {"/view_contacts/{page}","/view_contacts"})
	public String viewContacts(
			@PathVariable(name = "page",required = false) Integer page,
			Principal principal,
			Model model,
			HttpSession session) {
		
		if(page==null)page=0;
		
		User user = userRepository.getUserByUserName(principal.getName());
		int uid = user.getId();
		int number=5;
		if(session.getAttribute("crowNo") != null) {
			number=(Integer)session.getAttribute("crowNo");
			System.out.println("row no updated"+number);
		}

		
		Pageable pageRequest = PageRequest.of(page, number, Sort.unsorted());
		
		Page<Contact> pageContacts = contactRepository.getContactsByUserId(uid,pageRequest);
		//pageContacts.forEach(e->System.out.println(e.getNumber()));
		model.addAttribute("contacts",pageContacts);
		model.addAttribute("page",pageContacts.getNumber());
		model.addAttribute("totalPages",pageContacts.getTotalPages());
		

		return "users/view_contacts";
	}
	
	@GetMapping("/contact/delete/{cid}")
	public String deleteContact(@PathVariable("cid") int cid,Principal principal,Model model,HttpSession session) {
		String uemail = principal.getName();
		Contact contact = contactRepository.getById(cid);
		
		
		if(contact.getUser().getEmail().equals(uemail)) {
			try {
				String profile = contact.getProfile();
				File file = new ClassPathResource("static/img").getFile();
				String absolutePath = file.getAbsolutePath();
				Path path = Paths.get(absolutePath+File.separator+profile);
			Files.delete(path);
			}catch(Exception e) {
				e.printStackTrace();
			}
			contactRepository.deleteById(cid);
			
			
			
			//model.addAttribute("message",new Message("contact has been deleted successfully","success") );
			session.setAttribute("message", new Message("contact has been deleted successfully","success") );
			
		}else {
			session.setAttribute("message", new Message("Something went wrong please try agin later !!!","danger"));
		}
		
	return "redirect:/user/view_contacts";	
	}
	
	@GetMapping("/single_contact/{cid}")
	public String viewSingleContact(@PathVariable("cid") int cid,Principal principal,Model model) {
		try {
		Contact contact = contactRepository.getById(cid);
		String contactid = contact.getUser().getEmail();
		String userId = principal.getName();
		System.out.println("contact id = "+ contactid);
		System.out.println("user id = "+userId);
		if(contactid.equals(userId)) {
			System.out.println("inside if contact"+contact);
			model.addAttribute("contact", contact);
		}
		}catch(Exception e) {
			
		}
		return "users/single_contact";
	}
	
	
	//showing update contact form
	@PostMapping("/contact/update/{cid}")
	public String updateContact(@PathVariable("cid") int cid
			,Model m) {
		Contact contact = contactRepository.getById(cid);
		m.addAttribute("contact", contact);
		return "users/update_contact";
	}
	
	//show user profile 
	@GetMapping("/user_profile")
	public String showUserProfile(Model model,Principal principal) {
		User user = userRepository.getUserByUserName(principal.getName());
		model.addAttribute("user", user);
		
		return "users/user_profile";
	}
	

	
	
}
