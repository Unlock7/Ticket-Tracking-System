package net.hackyourfuture.tickettrackingsystem.dto.user;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Data
public class UserRequest {

    @NotBlank
    @Size(min = 3)
    private String name;

    @NotBlank
    @Email
    private String email;
}
