package net.hackyourfuture.tickettrackingsystem.dto.ticket;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TicketRequest {

    @NotBlank
    @Size(min = 3)
    private String title;

    @NotBlank
    private String description;

    @NotNull
    private Long projectId;

    @NotBlank
    private String status;   // open, in progress, closed
}
