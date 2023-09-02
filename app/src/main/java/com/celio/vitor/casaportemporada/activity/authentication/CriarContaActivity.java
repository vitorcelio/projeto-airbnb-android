package com.celio.vitor.casaportemporada.activity.authentication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.celio.vitor.casaportemporada.R;
import com.celio.vitor.casaportemporada.activity.MainActivity;
import com.celio.vitor.casaportemporada.helper.FirebaseHelper;
import com.celio.vitor.casaportemporada.model.Usuario;

import java.util.Date;

public class CriarContaActivity extends AppCompatActivity {

    private EditText editNome;
    private EditText editEmail;
    private EditText editTelefone;
    private EditText editSenha;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_criar_conta);

        iniciaComponentes();
        configCliques();
    }

    public void criarConta(View view) {
        String nome = editNome.getText().toString();
        String email = editEmail.getText().toString();
        String telefone = editTelefone.getText().toString().replace(" ", "").replace("(", "").replace(")", "").replace("-", "").trim();
        String senha = editSenha.getText().toString();

        if (nome.isEmpty()) {
            editNome.requestFocus();
            editNome.setError("Informe seu nome.");
            return;
        } else {
            confirmarValidacao(editNome);
        }

        if (email.isEmpty()) {
            editEmail.requestFocus();
            editEmail.setError("Informe seu email.");
            return;
        } else {
            confirmarValidacao(editEmail);
        }

        if (telefone.isEmpty()) {
            editTelefone.requestFocus();
            editTelefone.setError("Informe seu telefone.");
            return;
        }

        if (telefone.length() < 11) {
            editTelefone.requestFocus();
            editTelefone.setError("Informe um número válido.");
            return;
        } else {
            confirmarValidacao(editTelefone);
        }


        if (senha.isEmpty()) {
            editSenha.requestFocus();
            editSenha.setError("Informe sua senha.");
            return;
        } else {
            confirmarValidacao(editSenha);
        }

        progressBar.setVisibility(View.VISIBLE);

        Usuario usuario = new Usuario();
        usuario.setNome(nome);
        usuario.setEmail(email);
        usuario.setTelefone(telefone);
        usuario.setSenha(senha);
        usuario.setData(new Date());

        cadastrarUsuario(usuario);
    }

    private void confirmarValidacao(EditText editText) {
        Drawable ic = getDrawable(R.drawable.ic_check);
        editText.setCompoundDrawablesWithIntrinsicBounds(null, null, ic, null);
    }

    private void cadastrarUsuario(Usuario usuario) {
        FirebaseHelper.getAuth().createUserWithEmailAndPassword(usuario.getEmail(), usuario.getSenha())
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        String idUsuario = task.getResult().getUser().getUid();
                        usuario.setId(idUsuario);
                        usuario.salvar();

                        finish();
                        startActivity(new Intent(this, MainActivity.class));
                    } else {
                        progressBar.setVisibility(View.GONE);
                        String erro = task.getException().getMessage();
                        Toast.makeText(this, erro, Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void iniciaComponentes() {
        TextView titulo = findViewById(R.id.titulo_pagina);
        titulo.setText("Cadastrar");
        progressBar = findViewById(R.id.progress_bar);
        editNome = findViewById(R.id.edit_nome);
        editEmail = findViewById(R.id.edit_email);
        editTelefone = findViewById(R.id.edit_telefone);
        editSenha = findViewById(R.id.edit_senha);

    }

    private void configCliques() {
        findViewById(R.id.btn_voltar).setOnClickListener(v -> finish());
    }
}