package supun.senanayake.librarysystem;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class LibrarySystemApplication {

    public static void main(String[] args) {
//        SpringApplication.run(LibrarySystemApplication.class, args);
        SpringApplication app = new SpringApplication(LibrarySystemApplication.class);
        app.setAdditionalProfiles("dev");
        app.run(args);
    }

}
