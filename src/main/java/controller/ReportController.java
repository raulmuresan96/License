package controller;

import model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import service.ReportService;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.function.Predicate;

/**
 * Created by Ciprian on 11/25/2017.
 */
@RestController
public class ReportController {

//    @Autowired
//    private ReportService service;
//
//    @RequestMapping(value = "/API/user/day", method = RequestMethod.POST)
//    public Report checkIfDayStarted(@RequestBody User user){
//        return service.checkIfDayStarted(user);
//    }
//
//    @RequestMapping(value = "/API/user/day/isFinished", method = RequestMethod.POST)
//    public Report checkIfDayFinished(@RequestBody User user){
//        return service.checkIfDayFinished(user);
//    }
//
//    @RequestMapping(value = "/API/user/day/start",  method = RequestMethod.POST)
//    public Report startDay(@RequestBody Report report){
//        return service.startDay(report);
//    }
//
//    @RequestMapping(value = "/API/user/day/finish", method = RequestMethod.POST)
//    public Report finishDay(@RequestBody User user){
//        return service.finishDay(user);
//    }
//
//    @RequestMapping(value = "/API/report", method = RequestMethod.POST)
//    public Report updateReport(@RequestBody Report report){
//        return service.updateReport(report);
//    }
//
//    @RequestMapping(value = "/API/report/update", method = RequestMethod.POST)
//    public Report updateReportSwift(@RequestBody Report report){
//        double workedHours = report.getHours();
//        report = service.findByReportId(report.getRaportId());
//        report.setHours(workedHours);
//        return service.updateReportSwift(report, workedHours);
//    }
//
//    @RequestMapping(value = "/API/report/{idUser}", method = RequestMethod.GET)
//    public Iterable<Report> getAllReportsByUser(@PathVariable(value="idUser") int idUser){
//        return service.findAllByUserId(idUser);
//    }
//
//    @RequestMapping(value = "/API/report/statistic", method = RequestMethod.POST)
//    public List<Report> generateStatistics(@RequestBody ReportsStatistics reportsStatistics){
//        return service.generateStatistics(reportsStatistics);
//    }
}
