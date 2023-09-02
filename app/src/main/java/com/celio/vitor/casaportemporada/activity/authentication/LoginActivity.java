package com.celio.vitor.casaportemporada.activity.authentication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.celio.vitor.casaportemporada.R;
import com.celio.vitor.casaportemporada.activity.MainActivity;
import com.celio.vitor.casaportemporada.helper.FirebaseHelper;

public class LoginActivity extends AppCompatActivity {

    private TextView criarConta;
    private TextView recuperarConta;
    private EditText editEmail;
    private EditText editSenha;
    private ProgressBar progressBar;
    private ImageButton btnVoltar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        verificarLogin();
        iniciarComponentes();
        configCliques();
    }

    private void verificarLogin() {
        if (FirebaseHelper.getAutenticado()) {
            finish();
            startActivity(new Intent(this, MainActivity.class));
        }
    }

    public void login(View view) {
        String email = editEmail.getText().toString();
        String senha = editSenha.getText().toString();

        if (email.isEmpty()) {
            editEmail.requestFocus();
            editEmail.setError("Informe seu email.");
            return;
        }

        if (senha.isEmpty()) {
            editSenha.requestFocus();
            editSenha.setError("Informe sua senha.");
            return;
        }

        progressBar.setVisibility(View.VISIBLE);
        logar(email, senha);
    }

    public void logar(String email, String senha) {
        FirebaseHelper.getAuth().signInWithEmailAndPassword(email, senha)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        finish();
                    } else {
                        progressBar.setVisibility(View.GONE);
                        String erro = task.getException().getMessage();
                        Toast.makeText(this, erro, Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void iniciarComponentes() {
        TextView titulo = findViewById(R.id.titulo_pagina);
        titulo.setText("Entrar");
        criarConta = findViewById(R.id.criar_conta);
        recuperarConta = findViewById(R.id.recuperar_conta);
        editEmail = findViewById(R.id.edit_email);
        editSenha = findViewById(R.id.edit_senha);
        progressBar = findViewById(R.id.progress_bar);
        btnVoltar = findViewById(R.id.btn_voltar);
    }

    private void configCliques() {
        criarConta.setOnClickListener(v -> startActivity(new Intent(this, CriarContaActivity.class)));
        recuperarConta.setOnClickListener(v -> startActivity(new Intent(this, RecuperarSenhaActivity.class)));
        btnVoltar.setOnClickListener(v -> finish());
    }

}