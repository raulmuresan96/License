package model;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by Raul on 11/03/2018.
 */
public class TrieNode {
    public TrieNode[] children;
    public Map<String, Boolean> authors;

    public TrieNode(){
        children = new TrieNode[26];
        authors = new ConcurrentHashMap<>();
    }
}
