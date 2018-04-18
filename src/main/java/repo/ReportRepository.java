package repo;
import model.Report;
import org.springframework.data.repository.CrudRepository;

/**
 * Created by Ciprina on 11/25/2017.
 */
public interface ReportRepository extends CrudRepository<Report,Integer>  {
    Iterable<Report> findAllByUserUserId(int idUser);
    Iterable<Report> findByUser(int userId);
    Report findAllByRaportId(int raportId);
}
