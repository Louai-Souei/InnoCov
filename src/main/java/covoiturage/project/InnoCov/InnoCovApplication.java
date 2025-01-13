package covoiturage.project.InnoCov;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class InnoCovApplication {

	public static void main(String[] args) {
		SpringApplication.run(InnoCovApplication.class, args);
	}

}
