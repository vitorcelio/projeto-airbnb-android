package com.celio.vitor.casaportemporada.model;

import java.io.Serializable;

public class Filtro implements Serializable {

    private String cidade;
    private int precoMinimo;
    private int precoMaximo;
    private String tipoLugar;
    private String qtdQuartos;
    private String qtdCamas;
    private String qtdBanheiros;
    private String qtdGaragens;

    public String getCidade() {
        return cidade;
    }

    public void setCidade(String cidade) {
        this.cidade = cidade;
    }

    public int getPrecoMinimo() {
        return precoMinimo;
    }

    public void setPrecoMinimo(int precoMinimo) {
        this.precoMinimo = precoMinimo;
    }

    public int getPrecoMaximo() {
        return precoMaximo;
    }

    public void setPrecoMaximo(int precoMaximo) {
        this.precoMaximo = precoMaximo;
    }

    public String getTipoLugar() {
        return tipoLugar;
    }

    public void setTipoLugar(String tipoLugar) {
        this.tipoLugar = tipoLugar;
    }

    public String getQtdQuartos() {
        return qtdQuartos;
    }

    public void setQtdQuartos(String qtdQuartos) {
        this.qtdQuartos = qtdQuartos;
    }

    public String getQtdCamas() {
        return qtdCamas;
    }

    public void setQtdCamas(String qtdCamas) {
        this.qtdCamas = qtdCamas;
    }

    public String getQtdBanheiros() {
        return qtdBanheiros;
    }

    public void setQtdBanheiros(String qtdBanheiros) {
        this.qtdBanheiros = qtdBanheiros;
    }

    public String getQtdGaragens() {
        return qtdGaragens;
    }

    public void setQtdGaragens(String qtdGaragens) {
        this.qtdGaragens = qtdGaragens;
    }
}
