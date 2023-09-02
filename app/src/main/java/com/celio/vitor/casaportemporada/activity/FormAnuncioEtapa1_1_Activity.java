package com.celio.vitor.casaportemporada.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.celio.vitor.casaportemporada.R;

public class FormAnuncioEtapa1_1_Activity extends AppCompatActivity {

    private TextView btnVoltar;
    private Button btnAvancar;
    private Button btnSair;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_anuncio_etapa11);

        iniciaComponentes();
        cliquesConfig();
    }

    private void iniciaComponentes() {
        ImageView img = findViewById(R.id.imageView3);
        Glide.with(this).load(R.drawable.img3).into(img);
        btnVoltar = findViewById(R.id.botao_voltar);
        btnAvancar = findViewById(R.id.btn_avancar);
        btnSair = findViewById(R.id.btn_sair);
        btnSair.setText("Sair");
    }

    private void cliquesConfig() {
        btnVoltar.setOnClickListener(v -> {
            finish();
            startActivity(new Intent(this, FormAnuncioActivity.class));
        });

        btnAvancar.setOnClickListener(v -> {
            finish();
            startActivity(new Intent(this, FormAnuncioEtapa1_2_Activity.class));
        });

        btnSair.setOnClickListener(v -> {
            finish();
        });
    }
}