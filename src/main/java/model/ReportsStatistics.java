package model;

import java.util.Date;

/**
 * Created by Raul on 19/12/2017.
 */
public class ReportsStatistics {
    private int idUser;
    private Date startDate;
    private Date endDate;

    public ReportsStatistics(){

    }

    public ReportsStatistics(int idUser, Date startDate, Date endDate) {
        this.idUser = idUser;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public int getIdUser() {
        return idUser;
    }

    public void setIdUser(int idUser) {
        this.idUser = idUser;
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
}
