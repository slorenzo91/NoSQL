package com.tallernoSQL.domicilios.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/nosql")
public class MainController {
	
	@GetMapping
	public String getAddress() {
		return "Api rest responde ok :)";
	}
	
	@PostMapping
	public String createUser() {
		return "Usuario creado OK";
	}
}
	