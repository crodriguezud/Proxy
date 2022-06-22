package com.mercadolibre.proxy.core.documents;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;

@Document(collection="parameter")
public class Parameter implements Serializable {

    @Transient
    public static final String SEQUENCE_NAME = "parameter_sequence";

    @Id
    private Long id;

    private String name;

    private String value;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
