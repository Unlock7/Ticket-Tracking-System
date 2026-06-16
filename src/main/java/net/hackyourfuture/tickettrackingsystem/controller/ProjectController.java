package net.hackyourfuture.tickettrackingsystem.controller;

import net.hackyourfuture.tickettrackingsystem.dto.project.ProjectResponse;
import net.hackyourfuture.tickettrackingsystem.service.ProjectService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/projects")
public class ProjectController {

    private final ProjectService projectService;

    public ProjectController(ProjectService projectService) {
        this.projectService = projectService;
    }

    @GetMapping
    public ResponseEntity<List<ProjectResponse>> getAllProjects() {
        List<ProjectResponse> projects = projectService.getAllProjectsWithTicketCounts();
        return ResponseEntity.ok(projects);
    }
}

