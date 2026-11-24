package br.com.ludoteca;

import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class JogoService {

    private final JogoRepository repository;

    public JogoService(JogoRepository repository) {
        this.repository = repository;
    }

    @Transactional
    public Jogo cadastrar(JogoDados dados) {
        validarFaixaJogadores(dados);
        return repository.save(new Jogo(dados));
    }

    public List<Jogo> listar() {
        return repository.findAll(Sort.by("id"));
    }

    public Jogo buscar(long id) {
        return repository.findById(id)
                .orElseThrow(() -> new JogoNaoEncontradoException(id));
    }

    @Transactional
    public Jogo atualizar(long id, JogoDados dados) {
        Jogo jogo = buscar(id);
        validarFaixaJogadores(dados);
        jogo.atualizar(dados);
        return jogo;
    }

    @Transactional
    public void excluir(long id) {
        repository.delete(buscar(id));
    }

    private void validarFaixaJogadores(JogoDados dados) {
        if (dados.minimoJogadores() > dados.maximoJogadores()) {
            throw new FaixaJogadoresInvalidaException();
        }
    }
}
