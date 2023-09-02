package com.celio.vitor.casaportemporada.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.celio.vitor.casaportemporada.R;
import com.celio.vitor.casaportemporada.model.Imovel;
import com.squareup.picasso.Picasso;

import java.text.NumberFormat;
import java.util.Locale;

public class FormAnuncioEtapa3_3_Activity extends AppCompatActivity {

    private TextView btnVoltar;
    private Button btnAvancar;
    private Button btnSair;
    private ProgressBar progressBar1;
    private ProgressBar progressBar2;
    private ProgressBar progressBar3;
    private Imovel imovel;
    private ImageView imagePublicar;
    private TextView textNomeLugar;
    private TextView textPreco;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_anuncio_etapa33);

        iniciaComponenetes();
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            imovel = (Imovel) bundle.getSerializable("imovel");
            Picasso.get().load(imovel.getImagem()).into(imagePublicar);
            textNomeLugar.setText(imovel.getTitulo());
            textPreco.setText(NumberFormat.getCurrencyInstance(new Locale("pt", "BR")).format(imovel.getPreco()));
        }
        configCliques();
    }

    private void iniciaComponenetes() {
        btnVoltar = findViewById(R.id.botao_voltar);
        btnAvancar = findViewById(R.id.btn_avancar);
        btnAvancar.setText("Publicar");
        Drawable drawable = getDrawable(R.drawable.bg_button);
        btnAvancar.setBackground(drawable);
        btnSair = findViewById(R.id.btn_sair);
        progressBar1 = findViewById(R.id.progressBar);
        progressBar1.incrementProgressBy(100);
        progressBar2 = findViewById(R.id.progressBar2);
        progressBar2.incrementProgressBy(100);
        progressBar3 = findViewById(R.id.progressBar3);
        progressBar3.incrementProgressBy(80);
        imagePublicar = findViewById(R.id.imageView4);
        textNomeLugar = findViewById(R.id.text_nome_lugar);
        textPreco = findViewById(R.id.text_preco);
    }

    private void configCliques() {
        btnVoltar.setOnClickListener(v -> {
            finish();
            Intent intent = new Intent(this, FormAnuncioEtapa3_2_Activity.class);
            intent.putExtra("imovel", this.imovel);
            startActivity(intent);
        });

        btnAvancar.setOnClickListener(v -> {
            pegarImgSharedPreferences(null);
            imovel.setStatus(true);
            imovel.salvar();
            finish();
        });

        btnSair.setOnClickListener(v -> {
            imovel.salvar();
            finish();
        });
    }

    private void pegarImgSharedPreferences(String url) {
        SharedPreferences preferences = getSharedPreferences("img-anuncio", 0);
        SharedPreferences.Editor ed = preferences.edit();
        ed.putString("urlImg", url);
        ed.apply();
    }
}