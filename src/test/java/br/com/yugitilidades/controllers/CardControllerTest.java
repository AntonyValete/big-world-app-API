package br.com.yugitilidades.controllers;

import br.com.yugitilidades.domain.mappers.CardMapper;
import br.com.yugitilidades.services.CardService;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Import({CardController.class, CardControllerTest.MockConfig.class})
@WithMockUser
class CardControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private CardService cardService;
    @Autowired
    private CardMapper cardMapper;

    @Value("${yugitilidades.jwt.secret}")
    private String jwtSecret;

    private String jwtToken;

    @Configuration
    static class MockConfig {
        @Bean
        public CardService cardService() {
            return Mockito.mock(CardService.class);
        }
        @Bean
        public CardMapper cardMapper() {
            return Mockito.mock(CardMapper.class);
        }
    }

    @BeforeEach
    void setUp() {
        Mockito.reset(cardService, cardMapper);
        jwtToken = JWT.create()
                .withSubject("user")
                .withClaim("role", "CARDS")
                .sign(Algorithm.HMAC256(jwtSecret));
    }

    @Test
    void getCardByName_returnsCardResponse() throws Exception {
        mockMvc.perform(get("/cards/Dark Magician")
                .header("Authorization", "Bearer " + jwtToken)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
}
