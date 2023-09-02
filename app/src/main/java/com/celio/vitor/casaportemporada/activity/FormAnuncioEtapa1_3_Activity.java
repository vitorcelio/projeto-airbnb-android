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

public class FormAnuncioEtapa1_3_Activity extends AppCompatActivity {

    private TextView btnVoltar;
    private Button btnAvancar;
    private Button btnSair;
    private ProgressBar progressBar1;
    private EditText editRua;
    private EditText editApto;
    private EditText editCidade;
    private EditText editEstado;
    private EditText editCep;
    private EditText editPais;
    private Imovel imovel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_anuncio_etapa13);

        iniciaComponenetes();

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            imovel = (Imovel) bundle.getSerializable("imovel");

            if(imovel.getRua() != null) {
                preencherCampos();
            }
        }

        configCliques();
    }

    private void preencherCampos() {
        editRua.setText(imovel.getRua());
        editApto.setText(imovel.getApto());
        editCidade.setText(imovel.getCidade());
        editEstado.setText(imovel.getEstado());
        editCep.setText(imovel.getCep());
        editPais.setText(imovel.getPais());
    }

    private void iniciaComponenetes() {
        btnVoltar = findViewById(R.id.botao_voltar);
        btnAvancar = findViewById(R.id.btn_avancar);
        btnSair = findViewById(R.id.btn_sair);
        progressBar1 = findViewById(R.id.progressBar);
        progressBar1.incrementProgressBy(70);
        editRua = findViewById(R.id.edit_rua);
        editApto = findViewById(R.id.edit_apto);
        editCidade = findViewById(R.id.edit_cidade);
        editEstado = findViewById(R.id.edit_estado);
        editCep = findViewById(R.id.edit_cep);
        editPais = findViewById(R.id.edit_pais_regiao);
    }

    private boolean validarEndereco() {
        String rua = editRua.getText().toString();
        String apto = editApto.getText().toString();
        String cidade = editCidade.getText().toString();
        String estado = editEstado.getText().toString();
        String cep = editCep.getText().toString();
        String pais = editPais.getText().toString();

        if (rua.isEmpty()) {
            editRua.requestFocus();
            editRua.setError("Informe a rua.");
            return false;
        }

        if (cidade.isEmpty()) {
            editCidade.requestFocus();
            editCidade.setError("Informe a cidade.");
            return false;
        }

        if (estado.isEmpty()) {
            editEstado.requestFocus();
            editEstado.setError("Informe o estado.");
            return false;
        }

        if (cep.isEmpty()) {
            editCep.requestFocus();
            editCep.setError("Informe o cep.");
            return false;
        }

        if (pais.isEmpty()) {
            editPais.requestFocus();
            editPais.setError("Informe o país.");
            return false;
        }

        imovel.setRua(rua);
        imovel.setApto(apto);
        imovel.setCidade(cidade);
        imovel.setEstado(estado);
        imovel.setCep(cep);
        imovel.setPais(pais);
        return true;
    }

    private void configCliques() {
        btnVoltar.setOnClickListener(v -> {
            finish();
            Intent intent = new Intent(this, FormAnuncioEtapa1_2_2_Activity.class);
            intent.putExtra("imovel", this.imovel);
            startActivity(intent);
        });

        btnAvancar.setOnClickListener(v -> {
            if (validarEndereco()) {
                finish();
                Intent intent = new Intent(this, FormAnuncioEtapa1_4_Activity.class);
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