package net.hackyourfuture.tickettrackingsystem.controller;

import net.hackyourfuture.tickettrackingsystem.dto.ticket.TicketRequest;
import net.hackyourfuture.tickettrackingsystem.dto.ticket.TicketResponse;
import net.hackyourfuture.tickettrackingsystem.model.Ticket;
import net.hackyourfuture.tickettrackingsystem.service.TicketService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/tickets")
public class TicketController {

    private final TicketService service;

    public TicketController(TicketService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<TicketResponse> create(@RequestBody TicketRequest req) {
        return ResponseEntity.ok(service.create(req));
    }
    @GetMapping
    public ResponseEntity<List<TicketResponse>> getAll() {
        return ResponseEntity.ok(service.getAll());
    }

    @GetMapping("/search")
    public ResponseEntity<List<Ticket>> search(
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String text
    ) {
        return ResponseEntity.ok(service.search(status, text));
    }

    @GetMapping("/{id}")
    public ResponseEntity<TicketResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(service.getById(id));
    }


    @PutMapping("/{id}")
    public ResponseEntity<TicketResponse> update(
            @PathVariable Long id,
            @RequestBody TicketRequest req
    ) {
        return ResponseEntity.ok(service.update(id, req));
    }

    @PostMapping("/{ticketId}/assignees")
    public ResponseEntity<?> assign(
            @PathVariable Long ticketId,
            @RequestBody AssignUserRequest req
    ) {
        service.assign(ticketId, req.getUserId());
        return ResponseEntity.ok("{\"message\":\"User assigned successfully\"}");
    }

    @DeleteMapping("/{ticketId}/assignees/{userId}")
    public ResponseEntity<?> remove(
            @PathVariable Long ticketId,
            @PathVariable Long userId
    ) {
        service.remove(ticketId, userId);
        return ResponseEntity.ok(
                "{\"message\":\"User " + userId + " removed from ticket " + ticketId + "\"}"
        );
    }

    // inner DTO
    public static class AssignUserRequest {
        private Long userId;

        public Long getUserId() { return userId; }
        public void setUserId(Long userId) { this.userId = userId; }
    }
}
