package com.celio.vitor.casaportemporada.model.enums;

public enum TipoEspaco {

    CASA("Casa"),
    APARTAMENTO("Apartamento"),
    CELEIRO("Celeiro"),
    BARCO("Barco"),
    CABANA("Cabana"),
    CASTELO("Castelo"),
    HOTEL("Hotel"),
    CASAS_BARCO("Casas-Barco"),
    TORRE("Torre"),
    POUSADA("Pousada");

    private String descricao;

    TipoEspaco(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return this.descricao;
    }

    }
