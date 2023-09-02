package com.celio.vitor.casaportemporada.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.celio.vitor.casaportemporada.R;
import com.celio.vitor.casaportemporada.model.Imovel;

public class FormAnuncioEtapa1_4_Activity extends AppCompatActivity {

    private TextView btnVoltar;
    private Button btnAvancar;
    private Button btnSair;
    private ProgressBar progressBar1;
    private TextView textHospedes;
    private TextView textQuartos;
    private TextView textCamas;
    private TextView textBanheiros;
    private TextView textGaragem;
    private Imovel imovel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_anuncio_etapa14);

        iniciaComponenetes();

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            imovel = (Imovel) bundle.getSerializable("imovel");
            if (imovel.getHospede() >= 1) {
                validaInformacoes(true);
            } else {
                validaInformacoes(false);
            }
        }

        configCliques();
    }

    private void validaInformacoes(boolean notNull) {
        if (notNull) {
            textHospedes.setText(String.valueOf(imovel.getHospede()));
            textQuartos.setText(String.valueOf(imovel.getQuarto()));
            textCamas.setText(String.valueOf(imovel.getCama()));
            textBanheiros.setText(String.valueOf(imovel.getBanheiro()));
            textGaragem.setText(String.valueOf(imovel.getGaragem()));
        } else {
            textHospedes.setText("1");
            imovel.setHospede(1);
            textQuartos.setText("0");
            imovel.setQuarto(0);
            textCamas.setText("1");
            imovel.setCama(1);
            textBanheiros.setText("0");
            imovel.setBanheiro(0);
            textGaragem.setText("0");
            imovel.setGaragem(0);
        }
    }

    private void iniciaComponenetes() {
        btnVoltar = findViewById(R.id.botao_voltar);
        btnAvancar = findViewById(R.id.btn_avancar);
        btnSair = findViewById(R.id.btn_sair);
        progressBar1 = findViewById(R.id.progressBar);
        progressBar1.incrementProgressBy(90);
        textHospedes = findViewById(R.id.text_hospedes);
        textQuartos = findViewById(R.id.text_quartos);
        textCamas = findViewById(R.id.text_camas);
        textBanheiros = findViewById(R.id.text_banheiros);
        textGaragem = findViewById(R.id.text_garagem);
    }

    public void aumentarHospedes(View view) {
        String hospedes = textHospedes.getText().toString();
        int qtd = Integer.parseInt(hospedes);
        if (qtd <= 15) {
            qtd += 1;
        }

        imovel.setHospede(qtd);
        textHospedes.setText(String.valueOf(qtd));
    }

    public void diminuirHospedes(View view) {
        String hospedes = textHospedes.getText().toString().replace("+", "");
        int qtd = Integer.parseInt(hospedes);
        if (qtd >= 2) {
            qtd -= 1;
        }

        imovel.setHospede(qtd);
        textHospedes.setText(String.valueOf(qtd));
    }

    public void aumentarQuartos(View view) {
        String quartos = textQuartos.getText().toString();
        int qtd = Integer.parseInt(quartos);
        if (qtd <= 49) {
            qtd += 1;
        }

        imovel.setQuarto(qtd);
        textQuartos.setText(String.valueOf(qtd));
    }

    public void diminuirQuartos(View view) {
        String quartos = textQuartos.getText().toString();
        int qtd = Integer.parseInt(quartos);
        if (qtd >= 1) {
            qtd -= 1;
        }

        imovel.setQuarto(qtd);
        textQuartos.setText(String.valueOf(qtd));
    }

    public void aumentarCamas(View view) {
        String camas = textCamas.getText().toString();
        int qtd = Integer.parseInt(camas);
        if (qtd <= 49) {
            qtd += 1;
        }

        imovel.setCama(qtd);
        textCamas.setText(String.valueOf(qtd));
    }

    public void diminuirCamas(View view) {
        String camas = textCamas.getText().toString();
        int qtd = Integer.parseInt(camas);
        if (qtd >= 2) {
            qtd -= 1;
        }

        imovel.setCama(qtd);
        textCamas.setText(String.valueOf(qtd));
    }

    public void aumentarBanheiros(View view) {
        String banheiros = textBanheiros.getText().toString();
        int qtd = Integer.parseInt(banheiros);
        if (qtd <= 49) {
            qtd += 1;
        }

        imovel.setBanheiro(qtd);
        textBanheiros.setText(String.valueOf(qtd));
    }

    public void diminuirBanheiros(View view) {
        String banheiros = textBanheiros.getText().toString();
        int qtd = Integer.parseInt(banheiros);
        if (qtd >= 1) {
            qtd -= 1;
        }

        imovel.setBanheiro(qtd);
        textBanheiros.setText(String.valueOf(qtd));
    }

    public void aumentarGaragem(View view) {
        String garagem = textGaragem.getText().toString();
        int qtd = Integer.parseInt(garagem);
        if (qtd <= 49) {
            qtd += 1;
        }

        imovel.setGaragem(qtd);
        textGaragem.setText(String.valueOf(qtd));
    }

    public void diminuirGaragem(View view) {
        String garagem = textGaragem.getText().toString();
        int qtd = Integer.parseInt(garagem);
        if (qtd >= 1) {
            qtd -= 1;
        }

        imovel.setGaragem(qtd);
        textGaragem.setText(String.valueOf(qtd));
    }

    private void configCliques() {
        btnVoltar.setOnClickListener(v -> {
            finish();
            Intent intent = new Intent(this, FormAnuncioEtapa1_3_Activity.class);
            intent.putExtra("imovel", this.imovel);
            startActivity(intent);
        });

        btnAvancar.setOnClickListener(v -> {
            finish();
            Intent intent = new Intent(this, FormAnuncioEtapa2_1_Activity.class);
            intent.putExtra("imovel", this.imovel);
            startActivity(intent);
        });

        btnSair.setOnClickListener(v -> {
            imovel.salvar();
            finish();
        });
    }


}