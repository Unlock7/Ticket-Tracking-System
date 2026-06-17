package net.hackyourfuture.tickettrackingsystem.service;

import net.hackyourfuture.tickettrackingsystem.dto.ticket.TicketRequest;
import net.hackyourfuture.tickettrackingsystem.dto.ticket.TicketResponse;
import net.hackyourfuture.tickettrackingsystem.exception.NotFoundException;
import net.hackyourfuture.tickettrackingsystem.model.Ticket;
import net.hackyourfuture.tickettrackingsystem.model.User;
import net.hackyourfuture.tickettrackingsystem.repository.TicketAssigneeRepository;
import net.hackyourfuture.tickettrackingsystem.repository.TicketRepository;
import net.hackyourfuture.tickettrackingsystem.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TicketService {

    private final TicketRepository tickets;
    private final TicketAssigneeRepository assignees;
    private final UserRepository users;
    private final EmailNotificationService email;

    public TicketService(
            TicketRepository tickets,
            TicketAssigneeRepository assignees,
            UserRepository users,
            EmailNotificationService email
    ) {
        this.tickets = tickets;
        this.assignees = assignees;
        this.users = users;
        this.email = email;
    }
    public List<Ticket> search(String status, String text) {
        return tickets.search(status, text);
    }
    public TicketResponse create(TicketRequest req) {
        Ticket t = new Ticket(
                null,
                req.getTitle(),
                req.getDescription(),
                req.getStatus(),
                req.getProjectId(),
                null
        );

        Ticket saved = tickets.create(t);
        return toResponse(saved);
    }
    public List<TicketResponse> getAll() {
        return tickets.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    public TicketResponse getById(Long id) {
        Ticket t = tickets.findById(id)
                .orElseThrow(() -> new NotFoundException("Ticket not found"));
        return toResponse(t);
    }


    public TicketResponse update(Long id, TicketRequest req) {
        Ticket t = tickets.findById(id)
                .orElseThrow(() -> new NotFoundException("Ticket not found"));

        t.setTitle(req.getTitle());
        t.setDescription(req.getDescription());
        t.setStatus(req.getStatus());
        t.setProjectId(req.getProjectId());

        tickets.update(t);

        List<User> assignedUsers = users.findByIds(assignees.findUserIds(id));

        email.sendTicketUpdated(t, assignedUsers);

        return toResponse(t);
    }

    public void assign(Long ticketId, Long userId) {
        Ticket t = tickets.findById(ticketId)
                .orElseThrow(() -> new NotFoundException("Ticket not found"));

        User u = users.findById(userId)
                .orElseThrow(() -> new NotFoundException("User not found"));

        if (!assignees.exists(ticketId, userId)) {
            assignees.assign(ticketId, userId);
        }

        List<User> assignedUsers = users.findByIds(assignees.findUserIds(ticketId));

        email.sendUserAssigned(t, assignedUsers);
    }

    public void remove(Long ticketId, Long userId) {
        assignees.remove(ticketId, userId);
    }

    private TicketResponse toResponse(Ticket t) {
        return new TicketResponse(
                t.getId(),
                t.getTitle(),
                t.getDescription(),
                t.getStatus(),
                t.getProjectId(),
                t.getCreatedAt()
        );
    }
}
