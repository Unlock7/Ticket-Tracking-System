package net.hackyourfuture.tickettrackingsystem.dto.project;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProjectResponse {

    private Long id;
    private String name;
    private TicketsSummary tickets;

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class TicketsSummary {
        private long open;
        private long inProgress;
        private long closed;
    }
}

