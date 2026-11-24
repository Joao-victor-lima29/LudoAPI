package br.com.ludoteca;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record JogoDados(
        @NotBlank @Size(max = 120) String titulo,
        @NotBlank @Size(max = 80) String categoria,
        @NotNull @Min(1) Integer minimoJogadores,
        @NotNull @Min(1) Integer maximoJogadores,
        @NotNull @Min(1) Integer duracaoMinutos
) {}
