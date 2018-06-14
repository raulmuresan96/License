package model;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * Created by Raul on 11/03/2018.
 */
public class TrieNode {
    public ConcurrentMap<Integer, TrieNode> children;
    public ConcurrentMap<String, Boolean> authors;

    public TrieNode(){
        children = new ConcurrentHashMap<>();
        authors = new ConcurrentHashMap<>();
    }
}

