package model;

import java.util.Collection;
import java.util.HashSet;

/**
 * Created by Raul on 11/03/2018.
 */
public class Trie {
    private TrieNode root;

    public Trie(){
        root = new TrieNode();
    }

    public void insert(String word, int position, String wordId){
        insert(root, word, position, wordId);
    }

    private void insert(TrieNode node, String word, int position, String wordId){
        node.authors.put(wordId, true);
        if(position == word.length()){
            //node.users.put(wordId, true);
            return;
        }

        int currentLetter = word.charAt(position) - 'a';
        if(node.children[currentLetter] == null){
            node.children[currentLetter] = new TrieNode();
        }
        insert(node.children[currentLetter], word, position + 1, wordId);
    }

    public Collection<String> search(String word){
        return search(root, word, 0);
    }

    public Collection<String> search(TrieNode node, String word, int position){
        if(position == word.length()){
            return node.authors.keySet();
        }
        int currentLetter = word.charAt(position) - 'a';
        if(node.children[currentLetter] == null)
            return new HashSet<>();
        return search(node.children[currentLetter], word, position + 1);
    }

    public TrieNode getNodeForString(String username){
        return getNodeForString(root, username, 0);
    }

    private TrieNode getNodeForString(TrieNode node, String word, int position){
        if(position == word.length()){
            return node;
        }
        int currentLetter = word.charAt(position) - 'a';
        if(node.children[currentLetter] == null)
            return null;
        return getNodeForString(node.children[currentLetter], word, position + 1);

    }
}
