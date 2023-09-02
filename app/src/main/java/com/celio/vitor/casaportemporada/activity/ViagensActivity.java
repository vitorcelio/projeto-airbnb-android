package com.celio.vitor.casaportemporada.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import com.celio.vitor.casaportemporada.R;
import com.celio.vitor.casaportemporada.Util.UtilAirbnb;
import com.celio.vitor.casaportemporada.activity.authentication.LoginActivity;
import com.celio.vitor.casaportemporada.helper.FirebaseHelper;

public class ViagensActivity extends AppCompatActivity {

    private Button btnExplorar;
    private Button btnFavoritos;
    private Button btnViagens;
    private Button btnMensagens;
    private Button btnConta;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_viagens);

        iniciaComponentes();
        configCliques();
    }

    private void iniciaComponentes() {
        btnExplorar = findViewById(R.id.btn_explorar);
        btnFavoritos = findViewById(R.id.btn_favoritos);
        btnViagens = findViewById(R.id.btn_viagens);
        UtilAirbnb.mudarCorTextoDrawableDoBotao(btnViagens, this, R.color.cor_principal);
        btnMensagens = findViewById(R.id.btn_mensagens);
        btnConta = findViewById(R.id.btn_conta);
        if (FirebaseHelper.getAutenticado()) {
            btnConta.setText("Perfil");
        } else {
            btnConta.setText("Entrar");
        }
    }

    private void configCliques() {

        btnExplorar.setOnClickListener(v -> finish());

        btnFavoritos.setOnClickListener(v -> {
            finish();
            startActivity(new Intent(this, FavoritosActivity.class));
        });

        btnMensagens.setOnClickListener(v -> {
            finish();
            startActivity(new Intent(this, MensagensActivity.class));
        });

        btnConta.setOnClickListener(v -> {
            finish();

            if (FirebaseHelper.getAutenticado()) {
                startActivity(new Intent(this, ContaActivity.class));
            } else {
                startActivity(new Intent(this, LoginActivity.class));
            }
        });
    }
}