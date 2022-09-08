package com.smart.controller;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.smart.entities.Emil;
import com.smart.entities.User;
import com.smart.helper.Message;
import com.smart.repositeries.UserRepository;

@Controller
public class HomeController {
	
	@Autowired
	JavaMailSender javaMailSender;
	
	@Autowired
	BCryptPasswordEncoder passwordEncoder;
	
	@Autowired
	UserRepository userRepository;

	@RequestMapping("/test")
	@ResponseBody
	public String test() {
		User user = new User();
		user.setName("Manish");
		user.setEmail("ermaanish@gmail.com");
		
		userRepository.save(user);
		
		return "test controller";
	}
	@RequestMapping("/")
	public String home() {
		
		return "home";
	}
	@RequestMapping("/about")
	public String about() {
		
		return "about";
	}
	
	//show contact us page
	@RequestMapping("/contactus")
	public String contactus(Model model) {
		model.addAttribute("email",new Emil());
		return "contactus";
	}
	//process contact us page
	
	@PostMapping("/process_contactus")
	public String contactus(@ModelAttribute Emil emil,HttpSession session) {
		
		//setting default value to emil object
		emil.setTo("manishkumarsingh1997@gmail.com");
		
		
		try {
		SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
		simpleMailMessage.setSubject(emil.getSubject());
		simpleMailMessage.setTo(emil.getTo());
		simpleMailMessage.setText("Email : "+emil.getEmail()+"\n"+
									"Name : "+emil.getName()+"\n"+emil.getContent());
		simpleMailMessage.setFrom("ermaanish@gmail.com");
		
		javaMailSender.send(simpleMailMessage);
		session.setAttribute("message", new Message("Thanks !! for contacting us, we will reply you soon","success"));
		}catch(Exception e) {
			session.setAttribute("message", new Message("Sorry !! Something went wrong, pleases try again later","danger"));
		}
				
		System.out.println("sent successfull");
		
		
		return "redirect:/contactus/";
	}
	
	
	@RequestMapping("/signup")
	public String signup(Model model) {
		model.addAttribute("user", new User());
		
		return "signup";
	}
	
	@RequestMapping("/signin")
	public String signin() {
		
		return "signin";
	}
	
	
	@PostMapping("/process_signup")
	public String processSignup(@Valid @ModelAttribute("user") User user, BindingResult result,@RequestParam(value="agreement",defaultValue="false") boolean agreement,Model model) {
		String msg = null;
		if(result.hasErrors()) {
			//System.out.println(result);
			model.addAttribute("user", user);
			return "signup";
		}
		if(!agreement) {
			msg ="Please accept the terms and condition";
			model.addAttribute("msg",msg );
			return "signup";
		}
		user.setRole("ROLE_USER");
		user.setPassword(passwordEncoder.encode(user.getPassword()));
		userRepository.save(user);
		
		
		return "home";
	}
}
