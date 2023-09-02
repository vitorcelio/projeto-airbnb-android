package com.celio.vitor.casaportemporada.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.celio.vitor.casaportemporada.R;
import com.celio.vitor.casaportemporada.model.Imovel;

public class FormAnuncioEtapa3_1_Activity extends AppCompatActivity {

    private TextView btnVoltar;
    private Button btnAvancar;
    private Button btnSair;
    private ProgressBar progressBar1;
    private ProgressBar progressBar2;
    private Imovel imovel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_anuncio_etapa31);

        iniciaComponenetes();
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            imovel = (Imovel) bundle.getSerializable("imovel");
        }

        configCliques();
    }

    private void iniciaComponenetes() {
        ImageView img = findViewById(R.id.imageView3);
        Glide.with(this).load(R.drawable.img5).into(img);
        btnVoltar = findViewById(R.id.botao_voltar);
        btnAvancar = findViewById(R.id.btn_avancar);
        btnSair = findViewById(R.id.btn_sair);
        progressBar1 = findViewById(R.id.progressBar);
        progressBar1.incrementProgressBy(100);
        progressBar2 = findViewById(R.id.progressBar2);
        progressBar2.incrementProgressBy(100);
    }

    private void configCliques() {
        btnVoltar.setOnClickListener(v -> {
            finish();
            Intent intent = new Intent(this, FormAnuncioEtapa2_4_Activity.class);
            intent.putExtra("imovel", this.imovel);
            startActivity(intent);
        });

        btnAvancar.setOnClickListener(v -> {
            finish();
            Intent intent = new Intent(this, FormAnuncioEtapa3_2_Activity.class);
            intent.putExtra("imovel", this.imovel);
            startActivity(intent);

        });

        btnSair.setOnClickListener(v -> {
            imovel.salvar();
            finish();
        });
    }
}