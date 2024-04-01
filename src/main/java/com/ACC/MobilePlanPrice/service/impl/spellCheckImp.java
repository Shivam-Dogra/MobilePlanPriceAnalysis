package com.ACC.MobilePlanPrice.service.impl;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

import org.springframework.stereotype.Service;

@Service
public class spellCheckImp {
    static class BKTreeNode {
        String word;
        Map<Integer, BKTreeNode> children;

        public BKTreeNode(String word) {
            this.word = word;
            this.children = new HashMap<>();
        }
    }

    static class BKTree {
        BKTreeNode root;

        public BKTree() {
            this.root = null;
        }

        public void insert(String word) {
            if (root == null) {
                root = new BKTreeNode(word);
                return;
            }

            int distance = calculateDistance(root.word, word);
            BKTreeNode current = root;
            while (current.children.containsKey(distance)) {
                current = current.children.get(distance);
                distance = calculateDistance(current.word, word);
            }
            current.children.put(distance, new BKTreeNode(word));
        }

        public List<String> suggestCorrections(String query, int tolerance) {
            List<String> suggestions = new ArrayList<>();
            suggestCorrectionsDFS(root, query, tolerance, suggestions);
            return suggestions;
        }

        
        private void suggestCorrectionsDFS(BKTreeNode node, String query, int tolerance, List<String> suggestions) {
            if (node == null) {
                return;
            }

            int distance = calculateDistance(node.word, query);
            System.out.println("Word: " + node.word + ", Query: " + query + ", Distance: " + distance);

            if (distance <= tolerance && !suggestions.contains(node.word)) /*&&matchingLetters(node.word, query) >= 3)*/ {
            	 //System.out.println("Word added: " + node.word);
           // if (distance <= tolerance && !suggestions.contains(node.word) && node.word.length()>=3) {
                suggestions.add(node.word);
            }

            for (int i = distance - tolerance; i <= distance + tolerance; i++) {
                if (node.children.containsKey(i)) {
                    suggestCorrectionsDFS(node.children.get(i), query, tolerance, suggestions);
                }
            }
        }

        private int calculateDistance(String word1, String word2) {
            int[][] dp = new int[word1.length() + 1][word2.length() + 1];

            for (int i = 0; i <= word1.length(); i++) {
                dp[i][0] = i;
            }
            for (int j = 0; j <= word2.length(); j++) {
                dp[0][j] = j;
            }

            for (int i = 1; i <= word1.length(); i++) {
                for (int j = 1; j <= word2.length(); j++) {
                    int cost = (word1.charAt(i - 1) == word2.charAt(j - 1)) ? 0 : 1;
                    dp[i][j] = Math.min(dp[i - 1][j] + 1, Math.min(dp[i][j - 1] + 1, dp[i - 1][j - 1] + cost));
                }
            }

            return dp[word1.length()][word2.length()];
        }
    }
    
    

    public static List<String> spellTheWord(String spellingToCheck) {
        BKTree bkTree = new BKTree();
        loadDictionary(bkTree, "dictionary.txt");
        
        
        int tolerance = 2; 
        String word = spellingToCheck;
        List<String> suggestions = bkTree.suggestCorrections(word, tolerance);
        
        int count = 0;
        for (String suggestion : suggestions) {
            System.out.println(suggestion);
            count++;
            if (count >= 2) {
                break; 
            }
        }
		return suggestions;
        
    }


    private static void loadDictionary(BKTree bkTree, String fileName) {
        try {
            Scanner scanner = new Scanner(new File(fileName));
            while (scanner.hasNext()) {
                //bkTree.insert(scanner.next());
            	bkTree.insert(scanner.nextLine().toLowerCase());
            }
            scanner.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
    /*
    public static int matchingLetters(String word1, String word2) {
        int count = 0;
        for (int i = 0; i < Math.min(word1.length(), word2.length()); i++) {
            if (word1.charAt(i) == word2.charAt(i)) {
                count++;
            }
        }
        return count;
    }*/


}
