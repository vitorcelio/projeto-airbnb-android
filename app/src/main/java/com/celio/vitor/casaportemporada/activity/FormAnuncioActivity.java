package com.celio.vitor.casaportemporada.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;

import com.celio.vitor.casaportemporada.R;

public class FormAnuncioActivity extends AppCompatActivity {

    private Button btnProximo;
    private ImageButton btnVoltar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_anuncio);

        iniciaComponentes();
        cliquesConfig();
    }

    private void iniciaComponentes() {
        btnProximo = findViewById(R.id.btn_proximo);
        btnVoltar = findViewById(R.id.btn_voltar);
    }

    private void cliquesConfig() {
        btnProximo.setOnClickListener(v -> {
            finish();
            startActivity(new Intent(this, FormAnuncioEtapa1_1_Activity.class));
        });

        btnVoltar.setOnClickListener(v -> {
            finish();
        });
    }
}