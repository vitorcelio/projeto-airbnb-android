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

public class FormAnuncioEtapa2_3_Activity extends AppCompatActivity {

    private TextView textView;
    private TextView btnVoltar;
    private Button btnAvancar;
    private Button btnSair;
    private ProgressBar progressBar1;
    private ProgressBar progressBar2;
    private Imovel imovel;
    private EditText editTitulo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_anuncio_etapa23);

        iniciaComponenetes();
        Bundle bundle = getIntent().getExtras();
        if(bundle != null) {
            imovel = (Imovel) bundle.getSerializable("imovel");
            textView.setText(textView.getText().toString().replace("tipoAcomodacao", imovel.getTipoEspaco()));
            if(imovel.getTitulo() != null) {
                editTitulo.setText(imovel.getTitulo());
            }
        }

        configCliques();
    }

    private void iniciaComponenetes() {
        textView = findViewById(R.id.textView);
        btnVoltar = findViewById(R.id.botao_voltar);
        btnAvancar = findViewById(R.id.btn_avancar);
        btnSair = findViewById(R.id.btn_sair);
        progressBar1 = findViewById(R.id.progressBar);
        progressBar1.incrementProgressBy(100);
        progressBar2 = findViewById(R.id.progressBar2);
        progressBar2.incrementProgressBy(50);
        editTitulo = findViewById(R.id.edit_titulo);
    }

    private boolean validaTitulo() {
        String titulo = editTitulo.getText().toString();

        if(titulo.isEmpty()) {
            editTitulo.requestFocus();
            editTitulo.setError("Informe o título.");
            return false;
        }

        imovel.setTitulo(titulo);
        return true;
    }

    private void configCliques() {
        btnVoltar.setOnClickListener(v -> {
            finish();
            Intent intent = new Intent(this, FormAnuncioEtapa2_2_Activity.class);
            intent.putExtra("imovel", this.imovel);
            startActivity(intent);
        });

        btnAvancar.setOnClickListener(v -> {
            if(validaTitulo()) {
                finish();
                Intent intent = new Intent(this, FormAnuncioEtapa2_4_Activity.class);
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