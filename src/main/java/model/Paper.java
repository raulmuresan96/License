package model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Created by Raul on 10/04/2018.
 */
@Entity
@Table(name="papers")
public class Paper {
    @Id
    private String paperId;
}
