package br.com.ludoteca;

class FaixaJogadoresInvalidaException extends RuntimeException {

    FaixaJogadoresInvalidaException() {
        super("O máximo de jogadores deve ser maior ou igual ao mínimo.");
    }
}
