package com.celio.vitor.casaportemporada.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.celio.vitor.casaportemporada.R;
import com.celio.vitor.casaportemporada.model.Imovel;

public class FormAnuncioEtapa2_4_Activity extends AppCompatActivity {

    private TextView btnVoltar;
    private Button btnAvancar;
    private Button btnSair;
    private ProgressBar progressBar1;
    private ProgressBar progressBar2;
    private Imovel imovel;
    private EditText editDescricao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_anuncio_etapa24);

        iniciaComponenetes();
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            imovel = (Imovel) bundle.getSerializable("imovel");
            if (imovel.getDescricao() != null) {
                editDescricao.setText(imovel.getDescricao());
            }
        }
        configCliques();
    }

    private void iniciaComponenetes() {
        btnVoltar = findViewById(R.id.botao_voltar);
        btnAvancar = findViewById(R.id.btn_avancar);
        btnSair = findViewById(R.id.btn_sair);
        progressBar1 = findViewById(R.id.progressBar);
        progressBar1.incrementProgressBy(100);
        progressBar2 = findViewById(R.id.progressBar2);
        progressBar2.incrementProgressBy(75);
        editDescricao = findViewById(R.id.edit_descricao);
    }

    private boolean validaDescricao() {
        String descricao = editDescricao.getText().toString();

        if (descricao.isEmpty()) {
            editDescricao.requestFocus();
            editDescricao.setError("Informe uma descrição.");
            return false;
        }

        imovel.setDescricao(descricao);
        return true;
    }

    private void configCliques() {
        btnVoltar.setOnClickListener(v -> {
            finish();
            Intent intent = new Intent(this, FormAnuncioEtapa2_3_Activity.class);
            intent.putExtra("imovel", this.imovel);
            startActivity(intent);
        });

        btnAvancar.setOnClickListener(v -> {
            if (validaDescricao()) {
                finish();
                Intent intent = new Intent(this, FormAnuncioEtapa3_1_Activity.class);
                intent.putExtra("imovel", this.imovel);
                startActivity(intent);
            }
        });

        btnSair.setOnClickListener(v -> {
            imovel.salvar();
            finish();
        });
    }
}