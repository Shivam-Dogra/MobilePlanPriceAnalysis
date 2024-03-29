package com.ACC.MobilePlanPrice.service.impl;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import org.springframework.stereotype.Service;

@Service
public class WordCompletionImp {
    private static ArrayList<String> wordList = new ArrayList<String>();

    public static void setWordList(String word) {
        WordCompletionImp.wordList.add(word);
    }

    public static ArrayList<String> spellSuggestions(String word) {
        word = word.toLowerCase().trim();
        Trie trie = new Trie();
        trie.insertFromFile("./wordsfile/mobilewords.txt");
        trie.wordCompletion(word);
        return WordCompletionImp.wordList;
    }

    static class TrieNode {
        Map<Character, TrieNode> children;
        boolean isEndOfWord;

        public TrieNode() {
            this.children = new HashMap<>();
            this.isEndOfWord = false;
        }
    }

    static class Trie {
        TrieNode root;

        public Trie() {
            this.root = new TrieNode();
        }

        public void insert(String word) {
            TrieNode current = root;

            for (char ch : word.toCharArray()) {
                current.children.putIfAbsent(ch, new TrieNode());
                current = current.children.get(ch);
            }

            current.isEndOfWord = true;
        }

        public void insertFromFile(String filePath) {
            try {
                File file = new File(filePath);
                Scanner scanner = new Scanner(file);

                while (scanner.hasNext()) {
                    String word = scanner.next().toLowerCase(); // Convert to lowercase for case-insensitive search
                    insert(word);
                }

                scanner.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }

        public boolean search(String word) {
            TrieNode current = root;

            for (char ch : word.toCharArray()) {
                if (!current.children.containsKey(ch)) {
                    return false;
                }
                current = current.children.get(ch);
            }

            return current.isEndOfWord;
        }

        public void wordCompletion(String prefix) {
            TrieNode current = root;

            // Traverse to the node corresponding to the prefix
            for (char ch : prefix.toCharArray()) {
                if (!current.children.containsKey(ch)) {
                    System.out.println("No words with the given prefix.");
                    return;
                }
                current = current.children.get(ch);
            }

            wordCompletionDFS(prefix, current);
        }

        private void wordCompletionDFS(String prefix, TrieNode node) {
            if (node.isEndOfWord) {
                WordCompletionImp.setWordList(prefix);
            }

            for (char ch : node.children.keySet()) {
                wordCompletionDFS(prefix + ch, node.children.get(ch));
            }
        }
    }
}
