package br.com.ludoteca;

class JogoNaoEncontradoException extends RuntimeException {

    JogoNaoEncontradoException(long id) {
        super("Jogo de ID " + id + " não encontrado.");
    }
}
