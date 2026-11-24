package br.com.ludoteca;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
class ApiExceptionHandler {

    @ExceptionHandler(JogoNaoEncontradoException.class)
    ProblemDetail tratarJogoNaoEncontrado(JogoNaoEncontradoException erro) {
        return ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, erro.getMessage());
    }

    @ExceptionHandler(FaixaJogadoresInvalidaException.class)
    ProblemDetail tratarFaixaInvalida(FaixaJogadoresInvalidaException erro) {
        return ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, erro.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    ProblemDetail tratarCamposInvalidos(MethodArgumentNotValidException erro) {
        ProblemDetail problema = ProblemDetail.forStatusAndDetail(
                HttpStatus.BAD_REQUEST, "Revise os campos indicados.");
        problema.setProperty("campos", erro.getBindingResult().getFieldErrors().stream()
                .map(campo -> campo.getField())
                .distinct()
                .sorted()
                .toList());
        return problema;
    }
}
