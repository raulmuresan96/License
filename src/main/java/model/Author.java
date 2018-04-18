package model;

import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * Created by Raul on 16/04/2018.
 */

@Entity
public class Author {
    @Id
    private String authorId;
    private String surname;
    private String firstname;


    public Author(){

    }

    public Author(String authorId, String surname, String firstname) {
        this.authorId = authorId;
        this.surname = surname;
        this.firstname = firstname;
    }

    public String getAuthorId() {
        return authorId;
    }

    public void setAuthorId(String authorId) {
        this.authorId = authorId;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    @Override
    public String toString() {
        return "Author{" +
                "authorId='" + authorId + '\'' +
                ", surname='" + surname + '\'' +
                ", firstname='" + firstname + '\'' +
                '}';
    }
}
