package com.ACC.MobilePlanPrice.service.impl;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
	 
		public class DataValidationServiceImp {
	 
		    // Validate word format
		    public static boolean isValidWord(String word) {
		        // Word should contain only alphabetic characters
		        return word.matches("[a-zA-Z]+");
		    }
		    
		    public static boolean isValidSearch(String word) {
		        // Word should contain only alphabetic characters and/or numbers
		        if (word.equals("5G") || word.equals("5G+") || word.equals("4G")) {
		            return true;
		        } else {
		            return word.matches("[a-zA-Z$0-9]+");
		        }
		    }
	 
		    // Validate URL format
		    public static boolean isValidUrl(String url) {
		        try {
		            new URL(url); // Try creating URL object
		            return true; // If successful, URL format is valid
		        } catch (MalformedURLException e) {
		            return false; // If exception, URL format is invalid
		        }
		    }
	 
		    // Validate keyword format
		    public static boolean isValidKeyword(String keyword) {
		        // Keyword should contain only alphabetic characters, digits, and underscores
		        return keyword.matches("[a-zA-Z0-9_]+");
		    }
	 
		    // Validate word list format
		    public static boolean isValidWordList(String wordList) {
		        // Word list should contain comma-separated words, each in valid word format
		        String[] words = wordList.split(",");
		        for (String word : words) {
		            if (!isValidWord(word.trim())) {
		                return false; // If any word is invalid, return false
		            }
		        }
		        return true; // If all words are valid, return true
		    }
		}
	 
	 
