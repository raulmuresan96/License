package model;

import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * Created by Raul on 16/04/2018.
 */

@Entity
public class Journal {

    @Id
    private String issn;
    private String title;
    private String category;

    public Journal(String issn, String category, String name) {
        this.issn = issn;
        this.category = category;
        this.title = name;
    }

    public Journal(String issn, String category) {
        this.issn = issn;
        this.category = category;
    }

    public Journal(){

    }

    public String getIssn() {
        return issn;
    }

    public void setIssn(String issn) {
        this.issn = issn;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getTitle() {
        return title;
    }

    public void setName(String name) {
        this.title = name;
    }

    @Override
    public String toString() {
        return "Journal{" +
                "issn='" + issn + '\'' +
                ", title='" + title + '\'' +
                ", category='" + category + '\'' +
                '}';
    }
}
