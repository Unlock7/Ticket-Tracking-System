package net.hackyourfuture.tickettrackingsystem.service;

import net.hackyourfuture.tickettrackingsystem.dto.project.ProjectResponse;
import net.hackyourfuture.tickettrackingsystem.repository.ProjectRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProjectServiceTest {

    @Mock
    private ProjectRepository projectRepository;

    @InjectMocks
    private ProjectService projectService;

    @Test
    void getAllProjectsWithTicketCounts_shouldReturnProjects() {
        List<ProjectResponse> projects = List.of(
                new ProjectResponse(1L, "Project A", 3),
                new ProjectResponse(2L, "Project B", 5)
        );

        when(projectRepository.findAllWithTicketCounts())
                .thenReturn(projects);

        List<ProjectResponse> result =
                projectService.getAllProjectsWithTicketCounts();

        assertEquals(2, result.size());
        assertEquals("Project A", result.getFirst().getName());
        assertEquals(3, result.getFirst().getTicketCount());

        verify(projectRepository).findAllWithTicketCounts();
    }
}