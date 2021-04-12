package br.com.henrique.formulario.exceptions;

import org.springframework.web.bind.annotation.ResponseStatus;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

@ResponseStatus(value = BAD_REQUEST)
public class ValidacaoException extends RuntimeException {
    public ValidacaoException(final String mensagem) {
        super(mensagem);
    }
}
