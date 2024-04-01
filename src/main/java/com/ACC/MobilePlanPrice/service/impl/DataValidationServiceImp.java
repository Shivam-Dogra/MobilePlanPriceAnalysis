package com.ACC.MobilePlanPrice.service.impl;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.io.File;
 

		public class DataValidationServiceImp {
			
		    // Validate word format
			public static boolean isValidWord(String word) {
				return word.matches("(?i)(?:[a-z]+|5G?|4G?)");
			}

		    
			public static boolean isValidSearch(String word) {
			    return word.matches("(?i)(?:[a-z$0-9+ ]+|5G\\+?)");
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
		    	return keyword.matches("(?i)(?:[a-zA-Z]+|5g\\+?)");

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
			/*
			//changes
			// Validate word format
		    public static boolean isValidWord(String word) {
		        // Word should contain only alphabetic characters
		        //return word.matches("[a-zA-Z]+");
		    	List<String> wordInDict= new ArrayList<>();
		    	loadDictionary(wordInDict, "dictionary.txt");
		    	if(wordInDict.contains(word)) {
		    		return word.matches("(?i)(?:[a-z]+|5g\\+?|3g|4g|[a-z]+(?:-[a-z]+)*)");
		    	}
		    	else
		    		return false;
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
		    private static void loadDictionary(List<String> wordList, String fileName) {
		        try {
		        	Scanner scanner = new Scanner(new File(fileName));
		            while (scanner.hasNext()) {
		                wordList.add(scanner.next().toLowerCase());
		            }
		            scanner.close();
		        } catch (FileNotFoundException e) {
		            e.printStackTrace();
		        }
		    }
		}
*/
	 
	 
