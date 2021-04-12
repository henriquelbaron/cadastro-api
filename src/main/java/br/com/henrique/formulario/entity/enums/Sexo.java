package br.com.henrique.formulario.entity.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import java.util.stream.Stream;

public enum Sexo {
    MASCULINO(0, "Masculino"),
    FEMININO(1, "Feminino"),
    OUTRO(2, "Outro");

    private final int id;
    private final String descricao;

    Sexo(int id, String descricao) {
        this.id = id;
        this.descricao = descricao;
    }

    @JsonCreator
    public static Sexo decode(final int id) {
        return Stream.of(Sexo.values()).filter(targetEnum -> targetEnum.id == id).findFirst().orElse(null);
    }

    @JsonValue
    public int getId() {
        return id;
    }
}
