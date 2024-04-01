package com.ACC.MobilePlanPrice.controller;
 
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
 
import com.ACC.MobilePlanPrice.model.Index;
import com.ACC.MobilePlanPrice.model.MobilePlan;
import com.ACC.MobilePlanPrice.model.PageRanking;
import com.ACC.MobilePlanPrice.service.impl.BellMobilePlanServiceImpl;
import com.ACC.MobilePlanPrice.service.impl.DataValidationServiceImp;
import com.ACC.MobilePlanPrice.service.impl.RogersMobilePlanServiceImpl;
import com.ACC.MobilePlanPrice.service.impl.WebCrawlerServiceImp;
import com.ACC.MobilePlanPrice.service.impl.WordCompletionImp;
import com.ACC.MobilePlanPrice.service.impl.searchFrequencyImp;
import com.ACC.MobilePlanPrice.service.impl.searchFrequencyImp.TreeNode;
import com.ACC.MobilePlanPrice.service.impl.spellCheckImp;
import com.ACC.MobilePlanPrice.service.impl.FreedomMobilePlanServiceImpl;
import com.ACC.MobilePlanPrice.service.impl.FrequencyCountImpl;
import com.ACC.MobilePlanPrice.service.impl.InvertedIndexImp;
import com.ACC.MobilePlanPrice.service.impl.PageRankingServiceImpl;
 
@RestController
@RequestMapping("/mobile-plans")
@CrossOrigin(origins = "http://localhost:5173/")

public class ControllerClass {
	private final searchFrequencyImp.TreeNode root;
	private final searchFrequencyImp searchFrequency;
	private String dir="MobileWebCrawlDir";
 
 
    @Autowired
    public ControllerClass(searchFrequencyImp.TreeNode root, searchFrequencyImp searchFrequency) {
        this.root = root;
        this.searchFrequency = searchFrequency;
    }
	@Autowired
	private RogersMobilePlanServiceImpl rogersService;
	@Autowired
	private BellMobilePlanServiceImpl bellService;
	@Autowired
	private FreedomMobilePlanServiceImpl freedomService;
	@Autowired
	private WebCrawlerServiceImp webCrawler;
	@Autowired
	private FrequencyCountImpl frequencyCounter;
	@Autowired
	private WordCompletionImp wordCompletion;
	@Autowired
	private spellCheckImp spellCheck;
	@Autowired
	private InvertedIndexImp invertedIndexImplService;
	@Autowired
	private PageRankingServiceImpl pageRankingService;
	

	
	@GetMapping("/spellCheck/{userInput}")
	public ResponseEntity<Object> spellCheck(@PathVariable String userInput) {
		try {
			// Validate user input format
            
			if (!DataValidationServiceImp.isValidSearch(userInput)) {
            	return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid word format.");      
            }
	        List<String> suggestions = spellCheckImp.spellTheWord(userInput.toLowerCase());
	        List<String> topTwoSuggestions = suggestions.subList(0, Math.min(suggestions.size(), 3));
	        return ResponseEntity.ok().body(topTwoSuggestions);
	    } catch (Exception e) {
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred: " + e.getMessage());
	    }
	}
 
		
	@GetMapping("/searchFrequency")
	public ResponseEntity<Object> searchFrequency() {
		try {
			if (root == null) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Search frequency tree is not initialized.");
            }
			
	        List<Map<String, Object>> searchFrequencyList = new ArrayList<>();
	        collectWordFrequencies(root, searchFrequencyList);
	        Map<String, List<Map<String, Object>>> response = new HashMap<>();
	        response.put("searchFrequency", searchFrequencyList);
	        return new ResponseEntity<>(response, HttpStatus.OK);
	    } catch (Exception e) {
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred: " + e.getMessage());
	    }
	}
 
	private void collectWordFrequencies(TreeNode root, List<Map<String, Object>> searchFrequencyList) {
	    if (root != null) {
	        // Traverse left subtree
	        collectWordFrequencies(root.left, searchFrequencyList);
	        
	        // Add word-frequency pair to the list
	        if (DataValidationServiceImp.isValidWord(root.word)) {
	        Map<String, Object> wordFrequencyMap = new HashMap<>();
		        wordFrequencyMap.put("word", root.word);
		        wordFrequencyMap.put("frequency", root.frequency);
		        searchFrequencyList.add(wordFrequencyMap);   
	        }
	        else {
	        	ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Invalid word");
	            
	        }
	        // Traverse right subtree
	        collectWordFrequencies(root.right, searchFrequencyList);
	    }
	}
	@GetMapping("/wordcompletion/{userInput}")
	public ResponseEntity<Object> wordCompletion(@PathVariable String userInput) {
	    try {
	    	// Validate user input format
            if (!DataValidationServiceImp.isValidSearch(userInput)) {
            	return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid word format.");      
            }
	        // Get the word completions for the user input
	        List<String> wordCompletions = WordCompletionImp.spellSuggestions(userInput);
	        // Check if the list of word completions is empty
	        if (wordCompletions.isEmpty()) {
	            // Return a generic response indicating no word completions found
	            return ResponseEntity.ok().body("No word completions found for the input: " + userInput);
	        }
	        // Retrieve the last word from the list directly
	        String lastWord = wordCompletions.get(wordCompletions.size() - 1);
	        // Return the last word as JSON response
	        Map<String, String> response = new HashMap<>();
	        response.put("Last Word", lastWord);
	        return ResponseEntity.ok().body(response);
	    } 
	    catch (Exception e) {
	        // Handle other errors
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred: " + e.getMessage());
	    }
	}
 
 
	
	@GetMapping("/crawl")
	public ResponseEntity<Object> crawl(@RequestParam String startingUrl) {
	    try {
	    	if (startingUrl == null || startingUrl.isEmpty()) {
	            return ResponseEntity.badRequest().body("Starting URL is empty.");
	        }
	    	// Validate URL format
            if (!DataValidationServiceImp.isValidUrl(startingUrl)) {
                return ResponseEntity.badRequest().body("Invalid URL format.");
            }
	    	webCrawler.clear();
 
	        Set<String> visitedUrls = webCrawler.crawl(startingUrl);
	        // Create the response object with visited URLs and message
	        Map<String, Object> response = new HashMap<>();
	        response.put("visited_urls", visitedUrls);
	        response.put("message", "Website is crawled!");
	        // Return the response as JSON
	        return new ResponseEntity<>(response, HttpStatus.OK);
	    } catch (IOException e) {
	        // Log the exception or handle it as needed
	        return new ResponseEntity<>("Error crawling website!", HttpStatus.INTERNAL_SERVER_ERROR);
	    }
	}

	@GetMapping("/invertedIndex/{userInput}")
	public ResponseEntity<Object> invertedIndex(@PathVariable String userInput) {
		 try {
			 if (userInput == null || userInput.isEmpty()) {
	                return ResponseEntity.badRequest().body("Input keyword is empty.");
			 }
			// Validate user input format
	            if (!DataValidationServiceImp.isValidKeyword(userInput)) {
	                return ResponseEntity.badRequest().body("Invalid keyword format.");
	            }
		    //InvertedIndexImpl invertedIndex = new InvertedIndexImpl();
			 invertedIndexImplService.buildIndex(dir); 
		    List<Index> occurrences = invertedIndexImplService.searchKeyword(userInput);
		    if (occurrences.isEmpty()) {
		        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
		    } else {
		        return new ResponseEntity<>(occurrences, HttpStatus.OK);
		    }
		 }
		 catch (Exception e) {
		        // Log the exception or handle it as needed
		        return new ResponseEntity<>("Error in Inverted Indexing!", HttpStatus.INTERNAL_SERVER_ERROR);
		    }
	}
	 @GetMapping("/frequencyCount/{userInput}")
	    public ResponseEntity<Object> frequencyCount(@PathVariable String userInput) {
	        try {
	        	if (userInput == null || userInput.isEmpty()) {
	                return ResponseEntity.badRequest().body("Input word list is empty.");
	        	}
	        	// Validate user input format
	            if (!DataValidationServiceImp.isValidWord(userInput)) {
	                return ResponseEntity.badRequest().body("Invalid word list format.");
	            }
	        	// Split userInput into individual words
	            String[] words = userInput.split(",");
	            // Insert each word into the root
	            for (String word : words) {
	                root.insert(word.trim().toLowerCase());
	            }
	            Map<String, Integer> wordFrequency = frequencyCounter.countFrequency(userInput);
	            return new ResponseEntity<>(wordFrequency, HttpStatus.OK);
	        } catch (IllegalArgumentException e) {
	            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid input: " + e.getMessage());
	        } catch (Exception e) {
	            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred: " + e.getMessage());
	        }
	    }    

	 @GetMapping("/pageranking")
	    public ResponseEntity<Object> getPageRanking(@RequestParam(required = false) String keyword) {
	        // Check if the keyword parameter is provided
	        if (keyword == null || keyword.isEmpty()) {
	            // Return bad request response if keyword is missing
	        	return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Entered keyword is missing.");
	        }

	        if (!DataValidationServiceImp.isValidKeyword(keyword)) {
                return ResponseEntity.badRequest().body("Invalid keyword format.");
            }
	        // Call the service to get the page ranking
	        List<PageRanking> rankings = pageRankingService.getPageRanking(dir,keyword);
 
	        // Check if the ranking list is empty
	        if (rankings.isEmpty()) {
	            // Return not found response if no results are found for the given keyword
	        	return new ResponseEntity<>("No result found",HttpStatus.NOT_FOUND);
	        }
 
	        // Return the list of page rankings
	        return new ResponseEntity<>(rankings, HttpStatus.OK);
	     }

	@GetMapping("/rogers")
    public ResponseEntity<Object> getRogersMobilePlan() {
		try {
            List<MobilePlan> rogersMobilePlan = rogersService.getMobilePlan();
 
            if (rogersMobilePlan != null) {
            	 return new ResponseEntity<>(rogersMobilePlan, HttpStatus.OK);
               } 
            else {
                return new ResponseEntity<>("Rogers mobile plan not found",HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            // Log the exception or handle it as needed
            return new ResponseEntity<>("An error occurred while fetching the Rogers mobile plan",HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

	@GetMapping("/bell")
    public ResponseEntity<Object> getBellMobilePlan() {
		try {
            List<MobilePlan> bellMobilePlan = bellService.getMobilePlan();
 
            if (bellMobilePlan != null) {
            	 return new ResponseEntity<>(bellMobilePlan, HttpStatus.OK);
               } 
            else {
                return new ResponseEntity<>("Bell mobile plan not found",HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return new ResponseEntity<>("An error occurred while fetching the Bell mobile plan",HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

	@GetMapping("/freedom")
    public ResponseEntity<Object> getVirginPlusMobilePlan() {
		try {
            List<MobilePlan> freedomMobilePlan = freedomService.getMobilePlan();
 
            if (freedomMobilePlan != null) {
            	 return new ResponseEntity<>(freedomMobilePlan, HttpStatus.OK);
               } 
            else {
                return new ResponseEntity<>("Freedom mobile plan not found",HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            // Log the exception or handle it as needed
            return new ResponseEntity<>("An error occurred while fetching the Freedom mobile plan",HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
	@GetMapping("/compare-plans")
    public ResponseEntity<Object> compareAllMobilePlans() {
        try {
            List<List<MobilePlan>> plans = new ArrayList<>();
            List<MobilePlan> rogersPlans=rogersService.getMobilePlan();
            List<MobilePlan> bellPlans=bellService.getMobilePlan();
            List<MobilePlan> virginPlusPlans=freedomService.getMobilePlan();
            plans.add(rogersPlans);
            plans.add(bellPlans);
            plans.add(virginPlusPlans);         
 
            return new ResponseEntity<>(plans, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>( "An error occurred while fetching mobile plans",HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
	

	@GetMapping("/best-plan/{criteria}")
    public ResponseEntity<Object> getBestMobilePlans(@PathVariable String criteria) {
        try {
        	List<MobilePlan> allPlans = new ArrayList<>();
            allPlans.addAll(rogersService.getMobilePlan());
            allPlans.addAll(bellService.getMobilePlan());
            allPlans.addAll(freedomService.getMobilePlan());

            List<MobilePlan> filteredPlans = new ArrayList<>();
            if (criteria.equals("price")) {
                MobilePlan lowestPricePlan = allPlans.stream()
                                                .min(Comparator.comparing(MobilePlan::getMonthlyCost))
                                                .orElse(null);
                if (lowestPricePlan != null) {
                    filteredPlans.add(lowestPricePlan);
                }
            } else if (criteria.equals("data")) {
                filteredPlans = allPlans.stream()
                                    .filter(plan -> plan.getDataAllowance().equals("Unlimited data"))
                                    .collect(Collectors.toList());
            }

            if (filteredPlans.isEmpty()) {
                return new ResponseEntity<>("Please provide criteria as price or data.", HttpStatus.NOT_FOUND);
            }

            return new ResponseEntity<>(filteredPlans, HttpStatus.OK);

        } catch (Exception e) {
            return new ResponseEntity<>("An error occurred while fetching mobile plans", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
	
}
 