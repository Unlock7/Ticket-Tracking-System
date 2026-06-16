package net.hackyourfuture.tickettrackingsystem.dto.project;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProjectRequest {

    @NotBlank
    @Size(min = 3, max = 255)
    private String name;
}
