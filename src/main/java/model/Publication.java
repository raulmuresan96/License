package model;

import javax.persistence.*;
import java.util.Collection;

/**
 * Created by Raul on 10/04/2018.
 */
@Entity
@Table(name="publication")
public class Publication {
    @Id
    private String publicationId;

    @ManyToMany(mappedBy = "publications")
    private Collection<Author> authors;

    @ManyToOne
    private Journal journal;


    @ManyToMany
    @JoinTable(name = "citations",
            joinColumns = { @JoinColumn(name = "cited") },
            inverseJoinColumns = { @JoinColumn(name = "cited_by") })
    private Collection<Publication> citedBy;


    public Journal getJournal() {
        return journal;
    }

    public void setJournal(Journal journal) {
        this.journal = journal;
    }

    public String getPublicationId() {
        return publicationId;
    }

    public void setPublicationId(String publicationId) {
        this.publicationId = publicationId;
    }

    public Collection<Author> getAuthors() {
        return authors;
    }

    public void setAuthors(Collection<Author> authors) {
        this.authors = authors;
    }
}
