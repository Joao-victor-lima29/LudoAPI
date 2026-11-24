package br.com.ludoteca;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class JogoControllerTest {

    @Mock
    private JogoService service;

    private MockMvc mvc;

    @BeforeEach
    void configurarMvc() {
        mvc = MockMvcBuilders.standaloneSetup(new JogoController(service))
                .setControllerAdvice(new ApiExceptionHandler())
                .build();
    }

    @Test
    void devolveCriadoComLocalizacaoEJson() throws Exception {
        var dados = new JogoDados("Trilha das Ilhas", "Estratégia", 2, 4, 45);
        var jogo = spy(new Jogo(dados));
        when(jogo.getId()).thenReturn(7L);
        when(service.cadastrar(dados)).thenReturn(jogo);

        mvc.perform(post("/jogos").contentType(MediaType.APPLICATION_JSON).content("""
                {"titulo":"Trilha das Ilhas","categoria":"Estratégia",
                 "minimoJogadores":2,"maximoJogadores":4,"duracaoMinutos":45}
                """))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "http://localhost/jogos/7"))
                .andExpect(jsonPath("$.id").value(7))
                .andExpect(jsonPath("$.titulo").value("Trilha das Ilhas"));
    }

    @Test
    void converteJogoAusenteEm404() throws Exception {
        when(service.buscar(7L)).thenThrow(new JogoNaoEncontradoException(7L));

        mvc.perform(get("/jogos/7"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.detail").value("Jogo de ID 7 não encontrado."));
    }

    @Test
    void validaCamposAntesDeChamarService() throws Exception {
        mvc.perform(post("/jogos").contentType(MediaType.APPLICATION_JSON).content("""
                {"titulo":" ","categoria":"Estratégia",
                 "minimoJogadores":2,"maximoJogadores":4,"duracaoMinutos":45}
                """))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.campos[0]").value("titulo"));
        verifyNoInteractions(service);
    }

    @Test
    void devolveSemConteudoAposExclusao() throws Exception {
        mvc.perform(delete("/jogos/7"))
                .andExpect(status().isNoContent())
                .andExpect(content().string(""));
        verify(service).excluir(7L);
    }
}
