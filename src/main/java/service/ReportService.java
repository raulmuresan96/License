package service;
import org.springframework.stereotype.Service;

@Service
public class ReportService {

//    @Autowired
//    private ReportRepository reportRepository;
//    @Autowired
//    private UserRepository userRepository;
//    @Autowired
//    private LocationRepository locationRepository;
//
//    public Report findByReportId(int reportId){
//        return reportRepository.findAllByRaportId(reportId);
//    }
//
//
//
//    public Report checkIfDayStarted(User user){
//        Date date = getDateNow();
//        return findByUserAndStartDate(user, date);
//    }
//
//    public Report checkIfDayFinished(User user){
//        Date date = getDateNow();
//        return findByUserAndEndDate(user, date);
//    }
//
//    public Report startDay(Report report){
//        report.setStartDate(getDateNow());
//        return reportRepository.save(report);
//    }
//
//    public Report finishDay(User user){
//        Date date = getDateNow();
//        Report report = findByUserAndStartDate(user, date);
//
//        if (report != null && report.getRaportId() != -1) {
//            report.setEndDate(date);
//            report.setHours( 1.0 * (date.getTime() -report.getStartDate().getTime()) / 1000 / 3600);
//            return reportRepository.save(report);
//        } else {
//            //TODO: Remove this exception and add proper exceptions
//            throw new RuntimeException("Report not found");
//        }
//    }
//
//    public Report updateReport(Report report){
//        report.setHours( 1.0 * (report.getEndDate().getTime() -report.getStartDate().getTime()) / 1000 / 3600);
//        return reportRepository.save(report);
//    }
//
//    public Report updateReportSwift(Report report, double workedHours){
//        long workedHoursInMilliSeconds = (long) (workedHours * 3600 * 1000);
//        report.setEndDate(new Date(report.getStartDate().getTime() + workedHoursInMilliSeconds));
//        //report.setHours( 1.0 * (report.getEndDate().getTime() -report.getStartDate().getTime()) / 1000 / 3600);
//        return reportRepository.save(report);
//    }
//
//
//    public List<Report> generateStatistics(ReportsStatistics reportsStatistics){
//        List<Report> reports = new ArrayList<>();
//        //checks if a Report is in the requested interval
//        Predicate<Report> predicate = (entity) -> (entity.getStartDate().compareTo(reportsStatistics.getStartDate()) > 0) &&
//                (entity.getEndDate().compareTo(reportsStatistics.getEndDate()) < 0);
//        reportRepository.findAllByUserUserId(reportsStatistics.getIdUser()).forEach(key ->{
//            if(predicate.test(key)){
//                reports.add(key);
//            }
//        });
//        return reports;
//    }
//
//    public Iterable<Report> findAllByUserId(int userId){
//        return reportRepository.findAllByUserUserId(userId);
//    }
//
//    public Date getDateNow() {
//        return new Date();
//    }
//
//    private Report findByUserAndStartDate(User user, Date date){
//        for(Report report: reportRepository.findAllByUserUserId(user.getUserId())){
//            if (compareReportDateAndDate(report.getStartDate(), date)) {
//                return report;
//            }
//        }
//        return new Report(-1);
//    }
//
//    private Report findByUserAndEndDate(User user, Date date){
//        for(Report report: reportRepository.findAllByUserUserId(user.getUserId())){
//            if (report.getEndDate() != null && compareReportDateAndDate(report.getEndDate(), date)) {
//                return report;
//            }
//        }
//        return new Report(-1);
//    }
//
//
//    private boolean compareReportDateAndDate(Date report, Date date) {
//        Calendar reportCalendar = toCalendar(report);
//        Calendar dateCalendar = toCalendar(date);
//
//        return reportCalendar.get(Calendar.DAY_OF_MONTH) == dateCalendar.get(Calendar.DAY_OF_MONTH)
//                && reportCalendar.get(Calendar.MONTH) == dateCalendar.get(Calendar.MONTH)
//                && reportCalendar.get(Calendar.YEAR) == dateCalendar.get(Calendar.YEAR);
//    }
//
//    private Calendar toCalendar(Date date){
//        Calendar cal = Calendar.getInstance();
//        cal.setTime(date);
//        return cal;
//    }

}
