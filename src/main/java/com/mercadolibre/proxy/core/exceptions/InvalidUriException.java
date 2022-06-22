package com.mercadolibre.proxy.core.exceptions;

import java.io.Serial;

public class InvalidUriException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = -4103742306093254558L;

    public InvalidUriException(String message){
        super(message);
    }
}
