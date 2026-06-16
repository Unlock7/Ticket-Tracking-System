package net.hackyourfuture.tickettrackingsystem.dto.project;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProjectResponse {

    private Long id;
    private String name;
    private TicketsSummary tickets;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class TicketsSummary {
        private long open;
        private long inProgress;
        private long closed;
    }
}

