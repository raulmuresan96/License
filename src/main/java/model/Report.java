package model;

import javax.persistence.*;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.Date;

/**
 * Created by Cosmin on 11/25/2017.
 */
@Entity
@Table(name="reports")
public class Report {
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private int raportId;
    @ManyToOne
    @JoinColumn(name = "userId", referencedColumnName = "userId")
    private User user;
    private Date startDate;
    private Date endDate;
    private double hours;
    @ManyToOne
    @JoinColumn(name = "locationId", referencedColumnName = "locationId")
    private Location location;

    public Report() {
    }

    public Report(int raportId) {
        this.raportId = raportId;
    }

    public Report(User user, Date startDate, Date endDate, double hours, Location location) {
        this.user = user;
        this.startDate = startDate;
        this.endDate = endDate;
        this.hours = hours;
        this.location = location;
    }

    public Report(User user, Date startDate, Location location){
        this.user = user;
        this.startDate = startDate;
        this.location = location;
    }

    public int getRaportId() {
        return raportId;
    }

    public void setRaportId(int raportId) {
        this.raportId = raportId;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public double getHours() {
        return hours;
    }

    public void setHours(double hours) {
        this.hours = hours;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }
}
