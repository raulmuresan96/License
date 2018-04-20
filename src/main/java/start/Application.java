package start;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/**
 * Created by Raul on 23/11/2017.
 */

@EnableJpaRepositories(basePackages={"repo"})
@EntityScan(basePackages={"model"})
@SpringBootApplication
@ComponentScan(basePackages={"controller","service","configuration","repo"})
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
