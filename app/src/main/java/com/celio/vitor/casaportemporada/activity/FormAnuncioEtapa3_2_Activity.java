package com.celio.vitor.casaportemporada.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.celio.vitor.casaportemporada.R;
import com.celio.vitor.casaportemporada.model.Imovel;

public class FormAnuncioEtapa3_2_Activity extends AppCompatActivity {

    private TextView btnVoltar;
    private Button btnAvancar;
    private Button btnSair;
    private ProgressBar progressBar1;
    private ProgressBar progressBar2;
    private ProgressBar progressBar3;
    private Imovel imovel;
    private EditText editPreco;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_anuncio_etapa32);

        iniciaComponenetes();
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            imovel = (Imovel) bundle.getSerializable("imovel");
            if(imovel.getPreco() != 0) {
                editPreco.setText(String.valueOf(imovel.getPreco()));
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
        progressBar2.incrementProgressBy(100);
        progressBar3 = findViewById(R.id.progressBar3);
        progressBar3.incrementProgressBy(50);
        editPreco = findViewById(R.id.edit_preco);
    }

    private boolean validaPreco() {
        String preco = editPreco.getText().toString();

        if (preco.isEmpty()) {
            editPreco.requestFocus();
            editPreco.setError("Informe o preço.");
            return false;
        }

        int precoInt = Integer.parseInt(preco);

        if (precoInt < 53 || precoInt > 52644) {
            editPreco.requestFocus();
            editPreco.setError("Insira um preço básico entre R$53 e R$52.644");
            return false;
        }

        imovel.setPreco(Integer.parseInt(preco));
        return true;
    }

    private void configCliques() {
        btnVoltar.setOnClickListener(v -> {
            finish();
            Intent intent = new Intent(this, FormAnuncioEtapa3_1_Activity.class);
            intent.putExtra("imovel", this.imovel);
            startActivity(intent);
        });

        btnAvancar.setOnClickListener(v -> {
            if (validaPreco()) {
                finish();
                Intent intent = new Intent(this, FormAnuncioEtapa3_3_Activity.class);
                intent.putExtra("imovel", this.imovel);
                startActivity(intent);
            }
        });

        btnSair.setOnClickListener(v -> {
            imovel.salvar();
            finish();
        });
    }

    public void aumentarPreco(View view) {
        String edit = editPreco.getText().toString();
        int preco = edit != null ? Integer.parseInt(edit) : 0;
        if (preco >= 0) {
            preco += 10;
            imovel.setPreco(preco);
        }

        editPreco.setText(String.valueOf(preco));
    }

    public void diminuirPreco(View view) {
        String edit = editPreco.getText().toString();
        int preco = edit != null ? Integer.parseInt(edit) : 0;
        if (preco >= 10) {
            preco -= 10;
            imovel.setPreco(preco);
        }

        editPreco.setText(String.valueOf(preco));
    }
}