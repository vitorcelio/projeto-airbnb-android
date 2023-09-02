package com.celio.vitor.casaportemporada.model;

import android.graphics.Bitmap;

import com.celio.vitor.casaportemporada.helper.FirebaseHelper;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Exclude;
import com.google.firebase.storage.StorageReference;

import java.io.Serializable;

public class Imovel implements Serializable {

    private String id;
    private String idUsuario;
    private boolean status = false;
    private String titulo;
    private String descricao;
    private int preco;
    private String tipoEspaco;
    private String acomodacaoDoEspaco;
    private int quarto;
    private int banheiro;
    private int garagem;
    private String rua;
    private String apto;
    private String cidade;
    private String estado;
    private String cep;
    private String pais;
    private int hospede;
    private int cama;
    private String imagem;
    private String uriImagem;

    public Imovel() {
        DatabaseReference reference = FirebaseHelper.getDatabaseReference();
        this.setId(reference.push().getKey());
    }

    public void salvar() {
        DatabaseReference reference = FirebaseHelper.getDatabaseReference()
                .child("anuncios")
                .child(FirebaseHelper.getIdFirebase())
                .child(this.getId());

        reference.setValue(this);

        DatabaseReference imovelPublico = FirebaseHelper.getDatabaseReference()
                .child("anuncios_publicos")
                .child(this.getId());

        imovelPublico.setValue(this);
    }

    public void remover() {
        DatabaseReference reference = FirebaseHelper.getDatabaseReference()
                .child("anuncios")
                .child(FirebaseHelper.getIdFirebase())
                .child(this.getId());

        reference.removeValue().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                StorageReference storageReference = FirebaseHelper.getStorageReference()
                        .child("imagens")
                        .child("anuncios")
                        .child(this.getId() + ".jpeg");

                storageReference.delete();
            }
        });

        DatabaseReference imovelPublico = FirebaseHelper.getDatabaseReference()
                .child("anuncios_publicos")
                .child(this.getId());

        imovelPublico.removeValue();
    }

    public void favoritar() {
        DatabaseReference reference = FirebaseHelper.getDatabaseReference()
                .child("favoritos")
                .child(FirebaseHelper.getIdFirebase())
                .child(this.getId());

        reference.setValue(this);
    }

    public void desfavoritar() {
        DatabaseReference reference = FirebaseHelper.getDatabaseReference()
                .child("favoritos")
                .child(FirebaseHelper.getIdFirebase())
                .child(this.getId());

        reference.removeValue();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(String idUsuario) {
        this.idUsuario = idUsuario;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public int getPreco() {
        return preco;
    }

    public void setPreco(int preco) {
        this.preco = preco;
    }

    public String getTipoEspaco() {
        return tipoEspaco;
    }

    public void setTipoEspaco(String tipoEspaco) {
        this.tipoEspaco = tipoEspaco;
    }

    public String getAcomodacaoDoEspaco() {
        return acomodacaoDoEspaco;
    }

    public void setAcomodacaoDoEspaco(String acomodacaoDoEspaco) {
        this.acomodacaoDoEspaco = acomodacaoDoEspaco;
    }

    public int getQuarto() {
        return quarto;
    }

    public void setQuarto(int quarto) {
        this.quarto = quarto;
    }

    public int getBanheiro() {
        return banheiro;
    }

    public void setBanheiro(int banheiro) {
        this.banheiro = banheiro;
    }

    public int getGaragem() {
        return garagem;
    }

    public void setGaragem(int garagem) {
        this.garagem = garagem;
    }

    public String getRua() {
        return rua;
    }

    public void setRua(String rua) {
        this.rua = rua;
    }

    public String getApto() {
        return apto;
    }

    public void setApto(String apto) {
        this.apto = apto;
    }

    public String getCidade() {
        return cidade;
    }

    public void setCidade(String cidade) {
        this.cidade = cidade;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getCep() {
        return cep;
    }

    public void setCep(String cep) {
        this.cep = cep;
    }

    public String getPais() {
        return pais;
    }

    public void setPais(String pais) {
        this.pais = pais;
    }

    public int getHospede() {
        return hospede;
    }

    public void setHospede(int hospede) {
        this.hospede = hospede;
    }

    public int getCama() {
        return cama;
    }

    public void setCama(int cama) {
        this.cama = cama;
    }

    public String getImagem() {
        return imagem;
    }

    public void setImagem(String imagem) {
        this.imagem = imagem;
    }

    @Exclude
    public String getUriImagem() {
        return uriImagem;
    }

    public void setUriImagem(String uriImagem) {
        this.uriImagem = uriImagem;
    }
}
