package com.rodkrtz.paysync.domain.exception;

public class ContaNaoEncontradaException extends EntidadeNaoEncontradaException {

    public ContaNaoEncontradaException(String message) {
        super(message);
    }

    public ContaNaoEncontradaException(Long contaId) {
        this(String.format("Não existe um cadastro de conta com id %d", contaId));
    }
}
