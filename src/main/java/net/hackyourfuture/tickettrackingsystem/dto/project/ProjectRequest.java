package net.hackyourfuture.tickettrackingsystem.dto.project;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProjectRequest {

    @NotBlank
    @Size(min = 3, max = 255)
    private String name;
}
