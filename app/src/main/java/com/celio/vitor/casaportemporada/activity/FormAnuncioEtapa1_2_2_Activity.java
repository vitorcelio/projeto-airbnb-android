package com.celio.vitor.casaportemporada.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.celio.vitor.casaportemporada.R;
import com.celio.vitor.casaportemporada.model.Imovel;
import com.celio.vitor.casaportemporada.model.enums.AcomodacaoEspaco;

import java.util.ArrayList;
import java.util.List;

public class FormAnuncioEtapa1_2_2_Activity extends AppCompatActivity {

    private TextView btnVoltar;
    private Button btnAvancar;
    private Button btnSair;
    private ProgressBar progressBar1;
    private LinearLayout btnEspacoInteiro;
    private LinearLayout btnUmQuarto;
    private LinearLayout btnQuartoCompartilhado;
    private List<LinearLayout> btnsTipos;
    private Imovel imovel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_anuncio_etapa112);

        iniciaComponentes();
        addButtonsNaList();
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            imovel = (Imovel) bundle.getSerializable("imovel");
            verificaBackground(imovel.getAcomodacaoDoEspaco());
        }

        cliquesConfig();
    }

    private void iniciaComponentes() {
        btnVoltar = findViewById(R.id.botao_voltar);
        btnAvancar = findViewById(R.id.btn_avancar);
        btnSair = findViewById(R.id.btn_sair);
        progressBar1 = findViewById(R.id.progressBar);
        progressBar1.incrementProgressBy(50);
        btnEspacoInteiro = findViewById(R.id.btn_espaco_inteiro);
        btnUmQuarto = findViewById(R.id.btn_um_quarto);
        btnQuartoCompartilhado = findViewById(R.id.btn_quarto_compartilhado);
    }

    private void addButtonsNaList() {
        btnsTipos = new ArrayList<>();
        btnsTipos.add(btnEspacoInteiro);
        btnsTipos.add(btnUmQuarto);
        btnsTipos.add(btnQuartoCompartilhado);
    }

    private void verificaBackground(String btn) {
        if (this.imovel.getAcomodacaoDoEspaco() != null) {
            if (btn.equals(AcomodacaoEspaco.ESPACO_INTEIRO.getDescricao())) {
                removerLinearLaySelecionado(btnEspacoInteiro);
            } else if (btn.equals(AcomodacaoEspaco.QUARTO_INTEIRO.getDescricao())) {
                removerLinearLaySelecionado(btnUmQuarto);
            } else {
                removerLinearLaySelecionado(btnQuartoCompartilhado);
            }
        } else {
            imovel.setAcomodacaoDoEspaco("Espaço inteiro");
        }
    }

    private void removerLinearLaySelecionado(LinearLayout btnTipo) {
        for (LinearLayout tipos : btnsTipos) {
            if (tipos.getId() != btnTipo.getId()) {
                Drawable drawable = getDrawable(R.drawable.bg_button_espaco);
                tipos.setBackground(drawable);
            }
        }

        Drawable drawable = getDrawable(R.drawable.bg_button_espaco_selecionado);
        btnTipo.setBackground(drawable);
    }

    private void cliquesConfig() {
        btnVoltar.setOnClickListener(v -> {
            finish();
            Intent intent = new Intent(this, FormAnuncioEtapa1_2_Activity.class);
            intent.putExtra("imovel", this.imovel);
            startActivity(intent);
        });

        btnAvancar.setOnClickListener(v -> {
            finish();
            Intent intent = new Intent(this, FormAnuncioEtapa1_3_Activity.class);
            intent.putExtra("imovel", this.imovel);
            startActivity(intent);
        });

        btnSair.setOnClickListener(v -> {
            imovel.salvar();
            finish();
        });

        btnEspacoInteiro.setOnClickListener(v -> {
            imovel.setAcomodacaoDoEspaco(AcomodacaoEspaco.ESPACO_INTEIRO.getDescricao());
            removerLinearLaySelecionado(btnEspacoInteiro);
        });

        btnUmQuarto.setOnClickListener(v -> {
            imovel.setAcomodacaoDoEspaco(AcomodacaoEspaco.QUARTO_INTEIRO.getDescricao());
            removerLinearLaySelecionado(btnUmQuarto);
        });

        btnQuartoCompartilhado.setOnClickListener(v -> {
            imovel.setAcomodacaoDoEspaco(AcomodacaoEspaco.QUARTO_COMPARTILHADO.getDescricao());
            removerLinearLaySelecionado(btnQuartoCompartilhado);
        });
    }
}