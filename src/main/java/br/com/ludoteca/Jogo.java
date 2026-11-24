package br.com.ludoteca;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "jogos")
public class Jogo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 120)
    private String titulo;

    @Column(nullable = false, length = 80)
    private String categoria;

    @Column(nullable = false)
    private int minimoJogadores;

    @Column(nullable = false)
    private int maximoJogadores;

    @Column(nullable = false)
    private int duracaoMinutos;

    protected Jogo() {}

    Jogo(JogoDados dados) {
        atualizar(dados);
    }

    void atualizar(JogoDados dados) {
        titulo = dados.titulo().strip();
        categoria = dados.categoria().strip();
        minimoJogadores = dados.minimoJogadores();
        maximoJogadores = dados.maximoJogadores();
        duracaoMinutos = dados.duracaoMinutos();
    }

    public Long getId() {
        return id;
    }

    public String getTitulo() {
        return titulo;
    }

    public String getCategoria() {
        return categoria;
    }

    public int getMinimoJogadores() {
        return minimoJogadores;
    }

    public int getMaximoJogadores() {
        return maximoJogadores;
    }

    public int getDuracaoMinutos() {
        return duracaoMinutos;
    }
}
