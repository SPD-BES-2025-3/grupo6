package br.com.ufg.odm.dataSync;

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

    public static EntityType fromValue(String value) {
        for (EntityType type : values()) {
            if (type.value.equals(value)) {
                return type;
            }
        }
        throw new IllegalArgumentException("Tipo de entidade inválido: " + value);
    }
}
