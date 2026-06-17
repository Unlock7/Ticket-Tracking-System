package net.hackyourfuture.tickettrackingsystem.controller;

import net.hackyourfuture.tickettrackingsystem.dto.ticket.TicketRequest;
import net.hackyourfuture.tickettrackingsystem.dto.ticket.TicketResponse;
import net.hackyourfuture.tickettrackingsystem.service.TicketService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.Instant;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class TicketControllerTest {

    @Mock
    private TicketService service;

    @InjectMocks
    private TicketController controller;

    private MockMvc mvc() {
        return MockMvcBuilders.standaloneSetup(controller).build();
    }

    // ---------------------------------------------------------
    // CREATE
    // ---------------------------------------------------------
    @Test
    void create_shouldReturnCreatedTicket() throws Exception {
        TicketResponse response = new TicketResponse(
                1L, "Bug", "Fix login", "OPEN", 1L, Instant.now()
        );

        when(service.create(any(TicketRequest.class))).thenReturn(response);

        mvc().perform(post("/tickets")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "title": "Bug",
                                  "description": "Fix login",
                                  "status": "OPEN",
                                  "projectId": 1
                                }
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Bug"))
                .andExpect(jsonPath("$.status").value("OPEN"));

        verify(service).create(any(TicketRequest.class));
    }

    // ---------------------------------------------------------
    // GET ALL
    // ---------------------------------------------------------
    @Test
    void getAll_shouldReturnList() throws Exception {
        List<TicketResponse> list = List.of(
                new TicketResponse(1L, "A", "desc", "OPEN", 1L, Instant.now()),
                new TicketResponse(2L, "B", "desc", "OPEN", 1L, Instant.now())
        );

        when(service.getAll()).thenReturn(list);

        mvc().perform(get("/tickets"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(2))
                .andExpect(jsonPath("$[0].title").value("A"));

        verify(service).getAll();
    }

    // ---------------------------------------------------------
    // GET BY ID
    // ---------------------------------------------------------
    @Test
    void getById_shouldReturnTicket() throws Exception {
        TicketResponse response = new TicketResponse(
                1L, "Bug", "Fix", "OPEN", 1L, Instant.now()
        );

        when(service.getById(1L)).thenReturn(response);

        mvc().perform(get("/tickets/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Bug"));

        verify(service).getById(1L);
    }

    // ---------------------------------------------------------
    // UPDATE
    // ---------------------------------------------------------
    @Test
    void update_shouldReturnUpdatedTicket() throws Exception {
        TicketResponse response = new TicketResponse(
                1L, "Updated", "New desc", "IN_PROGRESS", 2L, Instant.now()
        );

        when(service.update(eq(1L), any(TicketRequest.class))).thenReturn(response);

        mvc().perform(put("/tickets/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "title": "Updated",
                                  "description": "New desc",
                                  "status": "IN_PROGRESS",
                                  "projectId": 2
                                }
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Updated"))
                .andExpect(jsonPath("$.status").value("IN_PROGRESS"));

        verify(service).update(eq(1L), any(TicketRequest.class));
    }

    // ---------------------------------------------------------
    // ASSIGN USER
    // ---------------------------------------------------------
    @Test
    void assign_shouldCallService() throws Exception {
        mvc().perform(post("/tickets/1/assignees")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                { "userId": 7 }
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("User assigned successfully"));

        verify(service).assign(1L, 7L);
    }

    // ---------------------------------------------------------
    // REMOVE USER
    // ---------------------------------------------------------
    @Test
    void remove_shouldCallService() throws Exception {
        mvc().perform(delete("/tickets/1/assignees/7"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("User 7 removed from ticket 1"));

        verify(service).remove(1L, 7L);
    }
}

