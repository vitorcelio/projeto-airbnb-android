package com.celio.vitor.casaportemporada.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.res.ColorStateList;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.celio.vitor.casaportemporada.R;
import com.celio.vitor.casaportemporada.helper.FirebaseHelper;
import com.celio.vitor.casaportemporada.model.Usuario;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

public class InformacoesPerfilActivity extends AppCompatActivity {

    private ImageButton btnVoltar;
    private TextView btnEditar;
    private LinearLayout layoutInfo;
    private TextView textNome;
    private TextView textTelefone;
    private TextView textEmail;
    private LinearLayout layoutCadastroInfo;
    private EditText editNome;
    private EditText editTelefone;
    private TextView textCancelar;
    private Button btnSalvar;
    private Usuario usuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_informacoes_perfil);

        iniciaComponentes();
        buscarUsuario();
        configCliques();
    }

    private void iniciaComponentes() {
        btnVoltar = findViewById(R.id.btn_voltar);
        btnVoltar.setImageTintList(ColorStateList.valueOf(ContextCompat.getColor(this, R.color.black)));
        btnEditar = findViewById(R.id.botao_pagina);
        layoutInfo = findViewById(R.id.ll_info);
        textNome = findViewById(R.id.text_nome);
        textTelefone = findViewById(R.id.text_telefone);
        textEmail = findViewById(R.id.text_email);
        layoutCadastroInfo = findViewById(R.id.ll_cadastro_info);
        editNome = findViewById(R.id.edit_nome);
        editTelefone = findViewById(R.id.edit_numero);
        textCancelar = findViewById(R.id.text_cancelar);
        btnSalvar = findViewById(R.id.btn_salvar);
    }

    private void configCliques() {
        btnVoltar.setOnClickListener(v -> finish());

        btnEditar.setOnClickListener(v -> {
            layoutCadastroInfo.setVisibility(View.VISIBLE);
            layoutInfo.setVisibility(View.GONE);
            btnEditar.setVisibility(View.GONE);
        });

        textCancelar.setOnClickListener(v -> {
            layoutCadastroInfo.setVisibility(View.GONE);
            layoutInfo.setVisibility(View.VISIBLE);
            btnEditar.setVisibility(View.VISIBLE);
        });

        btnSalvar.setOnClickListener(v -> {
            if (verificarEdicao()) {
                layoutCadastroInfo.setVisibility(View.GONE);
                layoutInfo.setVisibility(View.VISIBLE);
                btnEditar.setVisibility(View.VISIBLE);
            }
        });

    }

    private void buscarUsuario() {
        DatabaseReference reference = FirebaseHelper.getDatabaseReference()
                .child("usuarios")
                .child(FirebaseHelper.getIdFirebase());

        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    usuario = snapshot.getValue(Usuario.class);
                    if (usuario != null) {
                        carregarUsuario(usuario);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void carregarUsuario(Usuario usuario) {
        textNome.setText(usuario.getNome());
        textTelefone.setText(usuario.getTelefone());
        textEmail.setText(usuario.getEmail());

        editNome.setText(usuario.getNome());
        editTelefone.setText(usuario.getTelefone());
    }

    private boolean verificarEdicao() {
        String nome = editNome.getText().toString();
        String telefone = editTelefone.getText().toString().replace(" ", "").replace("(", "").replace(")", "").replace("-", "").trim();

        if (nome.isEmpty()) {
            editNome.requestFocus();
            editNome.setError("Informe seu nome.");
            return false;
        }

        if (telefone.isEmpty()) {
            editTelefone.requestFocus();
            editTelefone.setError("Informe seu telefone.");
            return false;
        }

        if (telefone.length() < 11) {
            editTelefone.requestFocus();
            editTelefone.setError("Informe um telefone válido.");
            return false;
        }

        usuario.setNome(nome);
        usuario.setTelefone(telefone);
        usuario.salvar();
        carregarUsuario(usuario);
        return true;
    }
}