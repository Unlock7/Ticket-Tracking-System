package net.hackyourfuture.tickettrackingsystem.dto.ticket;

import lombok.*;

import java.time.Instant;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TicketResponse {
    private Long id;
    private String title;
    private String description;
    private String status;
    private Long projectId;
    private Instant createdAt;
}
