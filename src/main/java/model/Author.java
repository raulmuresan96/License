package model;

import org.springframework.context.annotation.Lazy;
import org.springframework.data.repository.cdi.Eager;

import javax.persistence.*;
import java.util.Collection;

/**
 * Created by Raul on 16/04/2018.
 */

@Entity
public class Author {
    @Id
    private String authorId;
    private String surname;
    private String firstname;


    @ManyToMany
    @JoinTable(name = "author_publication",
            joinColumns = { @JoinColumn(name = "author_id") },
            inverseJoinColumns = { @JoinColumn(name = "publication_id") })
    private Collection<Publication> publications;


    public Author(){

    }

    public Author(String authorId, String surname) {
        this.authorId = authorId;
        this.surname = surname;
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


    public Collection<Publication> getPublications() {
        return publications;
    }

    public void setPublications(Collection<Publication> publications) {
        this.publications = publications;
    }

    @Override
    public String toString() {
        return "Author{" +
                "authorId='" + authorId + '\'' +
                ", surname='" + surname + '\'' +
                ", firstname='" + firstname + '\'' +
                '}';
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Author author = (Author) o;

        return authorId != null ? authorId.equals(author.authorId) : author.authorId == null;
    }

    @Override
    public int hashCode() {
        return authorId != null ? authorId.hashCode() : 0;
    }
}
