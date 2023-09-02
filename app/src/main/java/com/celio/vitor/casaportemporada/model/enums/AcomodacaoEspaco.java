package com.celio.vitor.casaportemporada.model.enums;

public enum AcomodacaoEspaco {

    ESPACO_INTEIRO("Espaço inteiro"),
    QUARTO_INTEIRO("Um quarto inteiro"),
    QUARTO_COMPARTILHADO("Um quarto compartilhado");

    private String descricao;

    AcomodacaoEspaco(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }
}
