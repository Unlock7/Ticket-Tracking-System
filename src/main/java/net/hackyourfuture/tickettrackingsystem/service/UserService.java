package net.hackyourfuture.tickettrackingsystem.service;

import net.hackyourfuture.tickettrackingsystem.dto.user.UserRequest;
import net.hackyourfuture.tickettrackingsystem.dto.user.UserResponse;
import net.hackyourfuture.tickettrackingsystem.exception.NotFoundException;
import net.hackyourfuture.tickettrackingsystem.exception.ValidationException;
import net.hackyourfuture.tickettrackingsystem.model.User;
import net.hackyourfuture.tickettrackingsystem.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    private final UserRepository repo;

    public UserService(UserRepository repo) {
        this.repo = repo;
    }

    public List<UserResponse> getAll() {
        return repo.findAll().stream()
                .map(this::toResponse)
                .toList();
    }

    public UserResponse getById(Long id) {
        User user = repo.findById(id)
                .orElseThrow(() -> new NotFoundException("User not found"));
        return toResponse(user);
    }

    public UserResponse create(UserRequest request) {

        repo.findByEmail(request.getEmail()).ifPresent(u -> {
            throw new ValidationException("Email already exists", "email");
        });

        User user = new User(null, request.getName(), request.getEmail());
        User saved = repo.create(user);

        return toResponse(saved);
    }

    public UserResponse update(Long id, UserRequest request) {
        User existing = repo.findById(id)
                .orElseThrow(() -> new NotFoundException("User not found"));

        repo.findByEmail(request.getEmail()).ifPresent(u -> {
            if (!u.getId().equals(id)) {
                throw new ValidationException("Email already exists", "email");
            }
        });

        existing.setName(request.getName());
        existing.setEmail(request.getEmail());

        repo.update(id, existing);

        return toResponse(existing);
    }

    public void delete(Long id) {
        repo.findById(id)
                .orElseThrow(() -> new NotFoundException("User not found"));

        repo.delete(id);
    }

    private UserResponse toResponse(User user) {
        return new UserResponse(
                user.getId(),
                user.getName(),
                user.getEmail()
        );
    }
}
