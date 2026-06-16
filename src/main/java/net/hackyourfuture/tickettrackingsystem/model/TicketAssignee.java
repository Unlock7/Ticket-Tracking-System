package net.hackyourfuture.tickettrackingsystem.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TicketAssignee {
    private Long ticketId;
    private Long userId;
}

