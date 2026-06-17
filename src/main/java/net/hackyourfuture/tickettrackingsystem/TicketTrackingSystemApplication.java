package net.hackyourfuture.tickettrackingsystem;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import io.github.cdimascio.dotenv.Dotenv;

import java.util.Objects;

@SpringBootApplication
public class TicketTrackingSystemApplication {

	public static void main(String[] args) {

		Dotenv dotenv = Dotenv.load();

		System.setProperty("DB_URL", dotenv.get("DB_URL"));
		System.setProperty("DB_USERNAME", dotenv.get("DB_USERNAME"));
		System.setProperty("DB_PASSWORD", dotenv.get("DB_PASSWORD"));
		System.setProperty("EMAIL_API_KEY", dotenv.get("EMAIL_API_KEY"));
		System.setProperty("EMAIL_FROM", dotenv.get("EMAIL_FROM"));


		SpringApplication.run(TicketTrackingSystemApplication.class, args);
	}

}
