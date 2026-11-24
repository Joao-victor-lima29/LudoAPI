package br.com.ludoteca;

import java.net.URI;
import java.util.List;

import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@RestController
@RequestMapping("/jogos")
public class JogoController {

    private static final Logger log = LoggerFactory.getLogger(JogoController.class);
    private final JogoService service;

    public JogoController(JogoService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<Jogo> cadastrar(@Valid @RequestBody JogoDados dados) {
        Jogo jogo = service.cadastrar(dados);
        URI localizacao = ServletUriComponentsBuilder.fromCurrentRequestUri()
                .path("/{id}").buildAndExpand(jogo.getId()).toUri();
        log.info("Jogo cadastrado. id={}", jogo.getId());
        return ResponseEntity.created(localizacao).body(jogo);
    }

    @GetMapping
    public List<Jogo> listar() {
        return service.listar();
    }

    @GetMapping("/{id}")
    public Jogo buscar(@PathVariable long id) {
        return service.buscar(id);
    }

    @PutMapping("/{id}")
    public Jogo atualizar(@PathVariable long id, @Valid @RequestBody JogoDados dados) {
        Jogo jogo = service.atualizar(id, dados);
        log.info("Jogo atualizado. id={}", jogo.getId());
        return jogo;
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable long id) {
        service.excluir(id);
        log.info("Jogo excluído. id={}", id);
        return ResponseEntity.noContent().build();
    }
}
