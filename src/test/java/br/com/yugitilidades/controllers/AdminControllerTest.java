package br.com.yugitilidades.controllers;

import br.com.yugitilidades.services.CardService;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Import(AdminController.class)
@WithMockUser(roles = "ADMIN")
class AdminControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private CardService cardService;

    @Value("${yugitilidades.jwt.secret}")
    private String jwtSecret;

    private String jwtToken;

    @Configuration
    static class MockConfig {
        @Bean
        public CardService cardService() {
            return Mockito.mock(CardService.class);
        }
    }

    @BeforeEach
    void setUp() {
        Mockito.reset(cardService);
        jwtToken = JWT.create()
                .withSubject("admin")
                .withClaim("role", "ADMIN")
                .sign(Algorithm.HMAC256(jwtSecret));
    }

    @Test
    void importAllCards_returnsAccepted() throws Exception {
        doNothing().when(cardService).importAllCardsFromYGOProDeck();
        mockMvc.perform(post("/admin/importCards")
                .with(csrf())
                .header("Authorization", "Bearer " + jwtToken))
                .andExpect(status().isAccepted())
                .andExpect(content().string("Card import started (check logs for progress)."));
    }

    @Test
    void getImportStatus_importSucceeded_returnsOkWithCount() throws Exception {
        when(cardService.countCards()).thenReturn(42L);
        mockMvc.perform(get("/admin/importStatus")
                .header("Authorization", "Bearer " + jwtToken))
                .andExpect(status().isOk())
                .andExpect(content().string("Import succeeded. Total cards in DB: 42"));
    }

    @Test
    void getImportStatus_importFailed_returnsOkWithNoCards() throws Exception {
        when(cardService.countCards()).thenReturn(0L);
        mockMvc.perform(get("/admin/importStatus")
                .header("Authorization", "Bearer " + jwtToken))
                .andExpect(status().isOk())
                .andExpect(content().string("Import not completed or failed. No cards in DB."));
    }
}
