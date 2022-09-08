package com.smart.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class EmailController {
	
	@Autowired
	private JavaMailSender javaMailSender;
	
	@GetMapping("/mail/")
	public ResponseEntity<?> sendEmail(){
		String to="manishkumarsingh1997@gmail.com";
		String from = "ermaanish@gmail.com";
		
		try {
		SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
		simpleMailMessage.setTo(to);
		simpleMailMessage.setFrom(from);
		simpleMailMessage.setSubject("email testing SCM");
		simpleMailMessage.setText("this is testing text email");
		javaMailSender.send(simpleMailMessage);
		}catch(Exception e) {
			e.printStackTrace();
		}
		String r ="email sent ...";
		return ResponseEntity.ok(r);
	}

}
