package net.hackyourfuture.tickettrackingsystem.service;

import net.hackyourfuture.tickettrackingsystem.dto.ticket.TicketRequest;
import net.hackyourfuture.tickettrackingsystem.dto.ticket.TicketResponse;
import net.hackyourfuture.tickettrackingsystem.exception.NotFoundException;
import net.hackyourfuture.tickettrackingsystem.model.Ticket;
import net.hackyourfuture.tickettrackingsystem.model.User;
import net.hackyourfuture.tickettrackingsystem.repository.TicketAssigneeRepository;
import net.hackyourfuture.tickettrackingsystem.repository.TicketRepository;
import net.hackyourfuture.tickettrackingsystem.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TicketServiceTest {

    @Mock
    private TicketRepository tickets;

    @Mock
    private TicketAssigneeRepository assignees;

    @Mock
    private UserRepository users;

    @Mock
    private EmailNotificationService email;

    @InjectMocks
    private TicketService service;

    // ---------------------------------------------------------
    // CREATE
    // ---------------------------------------------------------
    @Test
    void create_shouldCreateTicket() {
        TicketRequest req = new TicketRequest("Bug", "Fix login", 1L, "OPEN");
        Ticket saved = new Ticket(1L, "Bug", "Fix login", "OPEN", 1L, Instant.now());

        when(tickets.create(any(Ticket.class))).thenReturn(saved);

        TicketResponse result = service.create(req);

        assertEquals("Bug", result.getTitle());
        assertEquals("Fix login", result.getDescription());
        verify(tickets).create(any(Ticket.class));
    }

    // ---------------------------------------------------------
    // GET ALL
    // ---------------------------------------------------------
    @Test
    void getAll_shouldReturnList() {
        List<Ticket> list = List.of(
                new Ticket(1L, "A", "desc", "OPEN", 1L, Instant.now()),
                new Ticket(2L, "B", "desc", "OPEN", 1L, Instant.now())
        );

        when(tickets.findAll()).thenReturn(list);

        List<TicketResponse> result = service.getAll();

        assertEquals(2, result.size());
        verify(tickets).findAll();
    }

    // ---------------------------------------------------------
    // GET BY ID
    // ---------------------------------------------------------
    @Test
    void getById_shouldReturnTicket() {
        Ticket t = new Ticket(1L, "Bug", "Fix", "OPEN", 1L, Instant.now());
        when(tickets.findById(1L)).thenReturn(Optional.of(t));

        TicketResponse result = service.getById(1L);

        assertEquals("Bug", result.getTitle());
        verify(tickets).findById(1L);
    }

    @Test
    void getById_shouldThrowWhenNotFound() {
        when(tickets.findById(1L)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> service.getById(1L));
    }

    // ---------------------------------------------------------
    // UPDATE
    // ---------------------------------------------------------
    @Test
    void update_shouldUpdateTicketAndSendEmail() {
        Ticket existing = new Ticket(1L, "Old", "Old desc", "OPEN", 1L, Instant.now());
        TicketRequest req = new TicketRequest("New", "New desc", 2L, "CLOSED");

        when(tickets.findById(1L)).thenReturn(Optional.of(existing));
        when(assignees.findUserIds(1L)).thenReturn(List.of(10L, 20L));
        when(users.findByIds(List.of(10L, 20L)))
                .thenReturn(List.of(
                        new User(10L, "A", "a@test.com"),
                        new User(20L, "B", "b@test.com")
                ));

        TicketResponse result = service.update(1L, req);

        assertEquals("New", result.getTitle());
        verify(tickets).update(existing);
        verify(email).sendTicketUpdated(eq(existing), anyList());
    }

    @Test
    void update_shouldThrowWhenNotFound() {
        when(tickets.findById(1L)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class,
                () -> service.update(1L, new TicketRequest()));
    }

    // ---------------------------------------------------------
    // ASSIGN
    // ---------------------------------------------------------
    @Test
    void assign_shouldAssignUserAndSendEmail() {
        Ticket t = new Ticket(1L, "Bug", "Fix", "OPEN", 1L, Instant.now());
        User u = new User(7L, "John", "john@test.com");

        when(tickets.findById(1L)).thenReturn(Optional.of(t));
        when(users.findById(7L)).thenReturn(Optional.of(u));
        when(assignees.exists(1L, 7L)).thenReturn(false);
        when(assignees.findUserIds(1L)).thenReturn(List.of(7L));
        when(users.findByIds(List.of(7L))).thenReturn(List.of(u));

        service.assign(1L, 7L);

        verify(assignees).assign(1L, 7L);
        verify(email).sendUserAssigned(eq(t), anyList());
    }

    @Test
    void assign_shouldThrowWhenTicketNotFound() {
        when(tickets.findById(1L)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class,
                () -> service.assign(1L, 7L));
    }

    @Test
    void assign_shouldThrowWhenUserNotFound() {
        Ticket t = new Ticket(1L, "Bug", "Fix", "OPEN", 1L, Instant.now());
        when(tickets.findById(1L)).thenReturn(Optional.of(t));
        when(users.findById(7L)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class,
                () -> service.assign(1L, 7L));
    }

    // ---------------------------------------------------------
    // REMOVE
    // ---------------------------------------------------------
    @Test
    void remove_shouldRemoveAssignee() {
        service.remove(1L, 7L);
        verify(assignees).remove(1L, 7L);
    }
}

