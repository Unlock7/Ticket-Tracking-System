package net.hackyourfuture.tickettrackingsystem.controller;

import net.hackyourfuture.tickettrackingsystem.dto.project.ProjectResponse;
import net.hackyourfuture.tickettrackingsystem.service.ProjectService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class ProjectControllerTest {

    @Mock
    private ProjectService projectService;

    @InjectMocks
    private ProjectController controller;

    @Test
    void getAllProjects_shouldReturnList() throws Exception {
        MockMvc mvc = MockMvcBuilders.standaloneSetup(controller).build();

        List<ProjectResponse> projects = List.of(
                new ProjectResponse(1L, "Project A", new ProjectResponse.TicketsSummary(3, 0, 0)),
                new ProjectResponse(2L, "Project B", new ProjectResponse.TicketsSummary(5, 1, 0))
        );

        when(projectService.getAllProjectsWithTicketCounts())
                .thenReturn(projects);

        mvc.perform(get("/projects")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(2))
                .andExpect(jsonPath("$[0].name").value("Project A"))
                .andExpect(jsonPath("$[0].tickets.open").value(3))
                .andExpect(jsonPath("$[1].name").value("Project B"))
                .andExpect(jsonPath("$[1].tickets.open").value(5));
    }
}
