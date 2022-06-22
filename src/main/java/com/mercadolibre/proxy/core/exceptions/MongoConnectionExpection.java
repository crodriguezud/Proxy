package com.mercadolibre.proxy.core.exceptions;

import java.io.Serial;

public class MongoConnectionExpection extends RuntimeException{


    @Serial
    private static final long serialVersionUID = 4880658540379607886L;

    public MongoConnectionExpection(String message){
        super(message);
    }
}
