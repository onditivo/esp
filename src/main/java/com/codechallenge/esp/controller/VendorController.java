package com.codechallenge.esp.controller;

import java.util.HashMap;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class VendorController {
    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("message", "Welcome To The Index Page!");
        return "Welcome!";
    }
    
	@GetMapping(path = "/unauthenticated")
	public HashMap<String, String> homePage() {
		return new HashMap<String, String>() {
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			{
				put("message", "Free for all to see");
				put("Name", "Anonymous");
			}
		};
	}
	
	@GetMapping(path = "/authenticated")
	public HashMap<String, String> authenticatedPage() {
		// get a successful user login
		OAuth2User user = ((OAuth2User) SecurityContextHolder.getContext().getAuthentication().getPrincipal());
		return new HashMap<String, String>() {
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			{
				put("message", "Looks like you have been authenticated");
				put("name", user.getAttribute("name"));
				put("grantedAuthorities", user.getAttribute("email"));
			}
		};
	}
	
	@GetMapping(path = "/vendors")
	public HashMap<String, String> vendorsPage() {
		OAuth2User user = ((OAuth2User) SecurityContextHolder.getContext().getAuthentication().getPrincipal());
		return new HashMap<String, String>() {
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			{
				put("message", "Welcome to the vendor group!");
				put("name", user.getAttribute("name"));
				put("grantedAuthorities", user.getAttribute("email"));
			}
		};
	}
	
	@GetMapping(path = "/vendors/{vid}")
	public HashMap<String, String> vendorsWithIdPage(@PathVariable String vid) {
		OAuth2User user = ((OAuth2User) SecurityContextHolder.getContext().getAuthentication().getPrincipal());
		return new HashMap<String, String>() {
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			{
				put("message", "Looks like you’re a specific vendor!");
				put("vid", vid);
				put("name", user.getAttribute("name"));
				put("grantedAuthorities", user.getAttribute("email"));
			}
		};
	}
}
