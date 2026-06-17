package net.hackyourfuture.tickettrackingsystem.service;

import net.hackyourfuture.tickettrackingsystem.dto.user.UserRequest;
import net.hackyourfuture.tickettrackingsystem.dto.user.UserResponse;
import net.hackyourfuture.tickettrackingsystem.exception.NotFoundException;
import net.hackyourfuture.tickettrackingsystem.exception.ValidationException;
import net.hackyourfuture.tickettrackingsystem.model.User;
import net.hackyourfuture.tickettrackingsystem.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository repo;

    @InjectMocks
    private UserService service;

    @Test
    void getById_shouldReturnUser() {
        User user = new User(1L, "John", "john@test.com");

        when(repo.findById(1L)).thenReturn(Optional.of(user));

        UserResponse result = service.getById(1L);

        assertEquals(1L, result.getId());
        assertEquals("John", result.getName());
        assertEquals("john@test.com", result.getEmail());
    }

    @Test
    void getById_shouldThrowWhenUserNotFound() {
        when(repo.findById(1L)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class,
                () -> service.getById(1L));
    }

    @Test
    void create_shouldCreateUser() {
        UserRequest request = new UserRequest("John", "john@test.com");
        User saved = new User(1L, "John", "john@test.com");

        when(repo.findByEmail(request.getEmail()))
                .thenReturn(Optional.empty());

        when(repo.create(any(User.class)))
                .thenReturn(saved);

        UserResponse result = service.create(request);

        assertEquals("John", result.getName());
        assertEquals("john@test.com", result.getEmail());

        verify(repo).create(any(User.class));
    }

    @Test
    void create_shouldThrowWhenEmailExists() {
        User existing = new User(1L, "John", "john@test.com");
        UserRequest request = new UserRequest("Jane", "john@test.com");

        when(repo.findByEmail(request.getEmail()))
                .thenReturn(Optional.of(existing));

        assertThrows(ValidationException.class,
                () -> service.create(request));

        verify(repo, never()).create(any());
    }

    @Test
    void delete_shouldDeleteUser() {
        User user = new User(1L, "John", "john@test.com");

        when(repo.findById(1L)).thenReturn(Optional.of(user));

        service.delete(1L);

        verify(repo).delete(1L);
    }
}
