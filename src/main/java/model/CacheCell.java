package model;

import java.util.Set;

/**
 * Created by Raul on 12/03/2018.
 */
public class CacheCell {
    private int lastUpdate;
    private Set<String> authors;
    private long lastTimeQueried;
    public CacheCell(int lastUpdate, Set<String> authors, long lastTimeQueried){
        this.lastUpdate = lastUpdate;
        this.authors = authors;
        this.lastTimeQueried = lastTimeQueried;
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

    public long getLastTimeQueried() {
        return lastTimeQueried;
    }

    public void setLastTimeQueried(long lastTimeQueried) {
        this.lastTimeQueried = lastTimeQueried;
    }

    @Override
    public String toString() {
        return "CacheCell{" +
                "lastUpdate=" + lastUpdate +
                ", users=" + authors +
                '}';
    }
}
