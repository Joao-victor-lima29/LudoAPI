package br.com.ludoteca;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class JogoServiceTest {

    @Mock
    private JogoRepository repository;

    @InjectMocks
    private JogoService service;

    @Test
    void cadastraJogoComFaixaValida() {
        // Arrange: monta a entrada e simula o retorno do salvamento.
        var dados = new JogoDados("  Trilha das Ilhas  ", "Estratégia", 2, 4, 45);
        when(repository.save(any(Jogo.class))).thenAnswer(chamada -> chamada.getArgument(0));

        // Act: executa o Service real, isolado do banco.
        Jogo jogo = service.cadastrar(dados);

        // Assert: confere o resultado e a chamada ao Repository.
        assertEquals("Trilha das Ilhas", jogo.getTitulo());
        assertEquals(4, jogo.getMaximoJogadores());
        verify(repository).save(jogo);
    }

    @Test
    void buscaJogoExistente() {
        var jogo = new Jogo(new JogoDados("Trilha das Ilhas", "Estratégia", 2, 4, 45));
        when(repository.findById(7L)).thenReturn(Optional.of(jogo));

        assertSame(jogo, service.buscar(7L));
        verify(repository).findById(7L);
    }

    @Test
    void recusaFaixaInvertidaSemAcessarRepositorio() {
        var dados = new JogoDados("Trilha das Ilhas", "Estratégia", 5, 2, 45);

        assertThrows(FaixaJogadoresInvalidaException.class, () -> service.cadastrar(dados));
        verifyNoInteractions(repository);
    }

    @Test
    void sinalizaJogoAusente() {
        when(repository.findById(7L)).thenReturn(Optional.empty());

        assertThrows(JogoNaoEncontradoException.class, () -> service.buscar(7L));
        verify(repository).findById(7L);
    }
}
