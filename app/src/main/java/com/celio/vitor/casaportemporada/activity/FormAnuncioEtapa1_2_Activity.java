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
import com.celio.vitor.casaportemporada.helper.FirebaseHelper;
import com.celio.vitor.casaportemporada.model.Imovel;
import com.celio.vitor.casaportemporada.model.enums.TipoEspaco;

import java.util.ArrayList;
import java.util.List;

public class FormAnuncioEtapa1_2_Activity extends AppCompatActivity {

    private TextView btnVoltar;
    private Button btnAvancar;
    private Button btnSair;
    private ProgressBar progressBar1;
    private LinearLayout btnCasa;
    private LinearLayout btnApartamento;
    private LinearLayout btnCeleiro;
    private LinearLayout btnBarco;
    private LinearLayout btnCabana;
    private LinearLayout btnCastelo;
    private LinearLayout btnHotel;
    private LinearLayout btnCasasBarco;
    private LinearLayout btnTorre;
    private LinearLayout btnPousada;
    private List<LinearLayout> btnsEspacos;
    private Imovel imovel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_anuncio_etapa12);

        iniciaComponentes();
        addButtonsNaList();

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            imovel = (Imovel) bundle.getSerializable("imovel");
            verificaBackground(imovel.getTipoEspaco());
        }

        if (imovel == null) {
            imovel = new Imovel();
            imovel.setTipoEspaco("Casa");
            imovel.setIdUsuario(FirebaseHelper.getIdFirebase());
        }

        cliquesConfig();
    }

    private void iniciaComponentes() {
        btnVoltar = findViewById(R.id.botao_voltar);
        btnAvancar = findViewById(R.id.btn_avancar);
        btnSair = findViewById(R.id.btn_sair);
        progressBar1 = findViewById(R.id.progressBar);
        progressBar1.incrementProgressBy(25);
        btnCasa = findViewById(R.id.btn_casa);
        btnApartamento = findViewById(R.id.btn_apartamento);
        btnCeleiro = findViewById(R.id.btn_celeiro);
        btnBarco = findViewById(R.id.btn_barco);
        btnCabana = findViewById(R.id.btn_cabana);
        btnCastelo = findViewById(R.id.btn_castelo);
        btnHotel = findViewById(R.id.btn_hotel);
        btnCasasBarco = findViewById(R.id.btn_casas_barco);
        btnTorre = findViewById(R.id.btn_torre);
        btnPousada = findViewById(R.id.btn_pousada);
    }

    private void addButtonsNaList() {
        btnsEspacos = new ArrayList<>();
        btnsEspacos.add(btnCasa);
        btnsEspacos.add(btnApartamento);
        btnsEspacos.add(btnCeleiro);
        btnsEspacos.add(btnBarco);
        btnsEspacos.add(btnCabana);
        btnsEspacos.add(btnCastelo);
        btnsEspacos.add(btnHotel);
        btnsEspacos.add(btnCasasBarco);
        btnsEspacos.add(btnTorre);
        btnsEspacos.add(btnPousada);
    }

    private void verificaBackground(String btn) {
        if (btn.equals(TipoEspaco.CASA.getDescricao())) {
            removerLinearLaySelecionado(btnCasa);
        } else if (btn.equals(TipoEspaco.APARTAMENTO.getDescricao())) {
            removerLinearLaySelecionado(btnApartamento);
        } else if (btn.equals(TipoEspaco.CELEIRO.getDescricao())) {
            removerLinearLaySelecionado(btnCeleiro);
        } else if (btn.equals(TipoEspaco.BARCO.getDescricao())) {
            removerLinearLaySelecionado(btnBarco);
        } else if (btn.equals(TipoEspaco.CABANA.getDescricao())) {
            removerLinearLaySelecionado(btnCabana);
        } else if (btn.equals(TipoEspaco.CASTELO.getDescricao())) {
            removerLinearLaySelecionado(btnCastelo);
        } else if (btn.equals(TipoEspaco.HOTEL.getDescricao())) {
            removerLinearLaySelecionado(btnHotel);
        } else if (btn.equals(TipoEspaco.CASAS_BARCO.getDescricao())) {
            removerLinearLaySelecionado(btnCasasBarco);
        } else if (btn.equals(TipoEspaco.TORRE.getDescricao())) {
            removerLinearLaySelecionado(btnTorre);
        } else {
            removerLinearLaySelecionado(btnPousada);
        }
    }

    private void removerLinearLaySelecionado(LinearLayout btnEspaco) {
        for (LinearLayout espaco : btnsEspacos) {
            if (espaco.getId() != btnEspaco.getId()) {
                Drawable drawable = getDrawable(R.drawable.bg_button_espaco);
                espaco.setBackground(drawable);
            }
        }

        Drawable drawable = getDrawable(R.drawable.bg_button_espaco_selecionado);
        btnEspaco.setBackground(drawable);
    }

    private void cliquesConfig() {
        btnVoltar.setOnClickListener(v -> {
            finish();
            startActivity(new Intent(this, FormAnuncioEtapa1_1_Activity.class));
        });

        btnAvancar.setOnClickListener(v -> {
            finish();
            Intent intent = new Intent(this, FormAnuncioEtapa1_2_2_Activity.class);
            intent.putExtra("imovel", this.imovel);
            startActivity(intent);
        });

        btnSair.setOnClickListener(v -> {
            imovel.salvar();
            finish();
        });

        btnCasa.setOnClickListener(v -> {
            imovel.setTipoEspaco(TipoEspaco.CASA.getDescricao());
            removerLinearLaySelecionado(btnCasa);
        });

        btnApartamento.setOnClickListener(v -> {
            imovel.setTipoEspaco(TipoEspaco.APARTAMENTO.getDescricao());
            removerLinearLaySelecionado(btnApartamento);
        });

        btnCeleiro.setOnClickListener(v -> {
            imovel.setTipoEspaco(TipoEspaco.CELEIRO.getDescricao());
            removerLinearLaySelecionado(btnCeleiro);
        });

        btnBarco.setOnClickListener(v -> {
            imovel.setTipoEspaco(TipoEspaco.BARCO.getDescricao());
            removerLinearLaySelecionado(btnBarco);
        });

        btnCabana.setOnClickListener(v -> {
            imovel.setTipoEspaco(TipoEspaco.CABANA.getDescricao());
            removerLinearLaySelecionado(btnCabana);
        });

        btnCastelo.setOnClickListener(v -> {
            imovel.setTipoEspaco(TipoEspaco.CASTELO.getDescricao());
            removerLinearLaySelecionado(btnCastelo);
        });

        btnHotel.setOnClickListener(v -> {
            imovel.setTipoEspaco(TipoEspaco.HOTEL.getDescricao());
            removerLinearLaySelecionado(btnHotel);
        });

        btnCasasBarco.setOnClickListener(v -> {
            imovel.setTipoEspaco(TipoEspaco.CASAS_BARCO.getDescricao());
            removerLinearLaySelecionado(btnCasasBarco);
        });

        btnTorre.setOnClickListener(v -> {
            imovel.setTipoEspaco(TipoEspaco.TORRE.getDescricao());
            removerLinearLaySelecionado(btnTorre);
        });

        btnPousada.setOnClickListener(v -> {
            imovel.setTipoEspaco(TipoEspaco.POUSADA.getDescricao());
            removerLinearLaySelecionado(btnPousada);
        });
    }
}