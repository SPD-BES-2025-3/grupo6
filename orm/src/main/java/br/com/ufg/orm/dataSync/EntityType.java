package br.com.ufg.orm.dataSync;

import lombok.Getter;

@Getter
public enum EntityType {

    USUARIO("usuario"),
    LIVRO("livro"),
    EXEMPLAR("exemplar"),
    RESERVA("reserva"),
    EMPRESTIMO("emprestimo");

    EntityType(String value) {
        this.value = value;
    }

    private final String value;
}
