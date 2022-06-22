package com.mercadolibre.proxy.core.exceptions;

import java.io.Serial;

public class UnlimitedException extends RuntimeException{


    @Serial
    private static final long serialVersionUID = 2494904270205832600L;

    public UnlimitedException(String message){
        super(message);
    }
}
