package net.hackyourfuture.tickettrackingsystem.service;

import net.hackyourfuture.tickettrackingsystem.model.Ticket;
import net.hackyourfuture.tickettrackingsystem.model.User;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class EmailNotificationService {

    private final String apiKey = System.getProperty("EMAIL_API_KEY");
    private final String from = System.getProperty("EMAIL_FROM");

    private final RestTemplate rest = new RestTemplate();
    private final String RESEND_URL = "https://api.resend.com/emails";

    // -----------------------------
    // 1. Ticket Updated Email
    // -----------------------------
    public void sendTicketUpdated(Ticket ticket, List<User> assignees) {
        for (User user : assignees) {
            sendEmail(
                    user.getEmail(),
                    "Ticket Updated: " + ticket.getTitle(),
                    "Hello " + user.getName() + ",\n\n" +
                            "The ticket you are assigned to has been updated.\n" +
                            "Ticket ID: " + ticket.getId() + "\n" +
                            "Title: " + ticket.getTitle() + "\n\n" +
                            "Regards,\nTicket Tracking System"
            );
        }
    }

    // -----------------------------
    // 2. User Assigned Email
    // -----------------------------
    public void sendUserAssigned(Ticket ticket, List<User> assignees) {
        for (User user : assignees) {
            sendEmail(
                    user.getEmail(),
                    "You Have Been Assigned to a Ticket",
                    "Hello " + user.getName() + ",\n\n" +
                            "You have been assigned to a new ticket.\n" +
                            "Ticket ID: " + ticket.getId() + "\n" +
                            "Title: " + ticket.getTitle() + "\n\n" +
                            "Regards,\nTicket Tracking System"
            );
        }
    }

    // -----------------------------
    // 3. Core Email Sending Method
    // -----------------------------
    private void sendEmail(String to, String subject, String text) {

        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.setBearerAuth(apiKey);

            Map<String, Object> body = new HashMap<>();
            body.put("from", from);
            body.put("to", to);
            body.put("subject", subject);
            body.put("text", text);

            HttpEntity<Map<String, Object>> request = new HttpEntity<>(body, headers);

            rest.postForEntity(RESEND_URL, request, String.class);

            System.out.println("EMAIL SENT → " + to + " | " + subject);

        } catch (Exception e) {
            System.out.println("EMAIL FAILED → " + to + " | " + e.getMessage());
        }
    }
}
