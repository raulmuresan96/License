package model;

import java.util.Set;

/**
 * Created by Raul on 12/03/2018.
 */
public class CacheCell {
    private int lastUpdate;
    private Set<String> authors;
    public CacheCell(int lastUpdate, Set<String> authors){
        this.lastUpdate = lastUpdate;
        this.authors = authors;
    }


    public int getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate(int lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    public Set<String> getUsers() {
        return authors;
    }

    public void setUsers(Set<String> users) {
        this.authors = authors;
    }

    @Override
    public String toString() {
        return "CacheCell{" +
                "lastUpdate=" + lastUpdate +
                ", users=" + authors +
                '}';
    }
}
