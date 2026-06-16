package net.hackyourfuture.tickettrackingsystem.service;

import net.hackyourfuture.tickettrackingsystem.model.Ticket;
import net.hackyourfuture.tickettrackingsystem.model.User;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmailNotificationService {

    public void sendTicketUpdated(Ticket ticket, List<User> assignees) {
        System.out.println("EMAIL: Ticket updated → " + ticket.getId());
    }

    public void sendUserAssigned(Ticket ticket, List<User> assignees) {
        System.out.println("EMAIL: User assigned → Ticket " + ticket.getId());
    }
}
