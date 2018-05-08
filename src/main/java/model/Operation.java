package model;

/**
 * Created by Raul on 12/03/2018.
 */


public class Operation {
    private String type;
    private Author author;
    public Operation(String type, Author author){
        this.type = type;
        this.author = author;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Author getAuthor() {
        return author;
    }

    public void setAuthor(Author author) {
        this.author = author;
    }
}
