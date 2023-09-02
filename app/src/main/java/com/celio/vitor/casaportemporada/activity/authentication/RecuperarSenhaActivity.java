package com.celio.vitor.casaportemporada.activity.authentication;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.celio.vitor.casaportemporada.R;
import com.celio.vitor.casaportemporada.helper.FirebaseHelper;
import com.google.firebase.auth.FirebaseAuth;

public class RecuperarSenhaActivity extends AppCompatActivity {

    private EditText editEmail;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recuperar_senha);

        iniciaComponentes();
        configCliques();
    }

    public void recuperarSenha(View view) {
        String email = editEmail.getText().toString();

        if(email.isEmpty()) {
            editEmail.requestFocus();
            editEmail.setError("Informe seu email.");
            return;
        }

        progressBar.setVisibility(View.VISIBLE);
        enviarEmailRecuperacao(email);
    }

    private void enviarEmailRecuperacao(String email) {
        FirebaseHelper.getAuth().sendPasswordResetEmail(email)
                .addOnCompleteListener(task -> {
                    if(task.isSuccessful()) {
                        finish();
                        Toast.makeText(this, "Email enviado com sucesso", Toast.LENGTH_SHORT).show();
                    } else {
                        progressBar.setVisibility(View.GONE);
                        String erro = task.getException().getMessage();
                        Toast.makeText(this, erro, Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void iniciaComponentes() {
        TextView titulo = findViewById(R.id.titulo_pagina);
        titulo.setText("Recuperar senha");
        editEmail = findViewById(R.id.edit_email);
        progressBar = findViewById(R.id.progress_bar);
    }

    private void configCliques() {
        findViewById(R.id.btn_voltar).setOnClickListener(v -> finish());
    }
}