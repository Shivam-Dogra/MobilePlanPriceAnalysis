package com.ACC.MobilePlanPrice.service.impl;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Service
public class searchFrequencyImp {
	@Component
	public static class TreeNode {
	    public String word;
	    public int frequency;
	    public TreeNode left;
	    public TreeNode right;

	    public TreeNode() {
	        this.word = null;
	        this.frequency = 0;
	        this.left = this.right = null;
	    }

	    public void insert(String newWord) {
	        if (word == null) {
	            // If the node is empty, set the word and initialize frequency
	            this.word = newWord;
	            this.frequency = 1;
	        } else {
	            int compareResult = newWord.compareTo(this.word);
	            if (compareResult < 0) {
	                if (this.left == null) {
	                    this.left = new TreeNode();
	                }
	                this.left.insert(newWord);
	            } else if (compareResult > 0) {
	                if (this.right == null) {
	                    this.right = new TreeNode();
	                }
	                this.right.insert(newWord);
	            } else {
	                // Word already exists in the tree, increase frequency
	                this.frequency++;
	            }
	        }
	    }

	    // Check if word exists in dictionary file
	    private static boolean wordExistsInDictionary(String word) throws IOException {
	        BufferedReader reader = new BufferedReader(new FileReader("dictionary.txt"));
	        String line;
	        while ((line = reader.readLine()) != null) {
	            if (line.trim().equalsIgnoreCase(word.trim())) {
	                reader.close();
	                return true;
	            }
	        }
	        reader.close();
	        return false;
	    }

	    public void insertIfWordExistsInDictionary(String newWord) throws IOException {
	        if (wordExistsInDictionary(newWord)) {
	            insert(newWord);
	        } else {
	            System.out.println("Word '" + newWord + "' does not exist in the dictionary. Skipping insertion.");
	        }
	    }
	}
}

	