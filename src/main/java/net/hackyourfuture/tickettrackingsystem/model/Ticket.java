package net.hackyourfuture.tickettrackingsystem.model;

import lombok.*;

import java.time.Instant;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Ticket {
    private Long id;
    private String title;
    private String description;
    private String status;     // open, in progress, closed
    private Long projectId;
    private Instant createdAt;
}

