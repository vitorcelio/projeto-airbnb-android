package com.celio.vitor.casaportemporada.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.celio.vitor.casaportemporada.R;
import com.celio.vitor.casaportemporada.helper.FirebaseHelper;
import com.celio.vitor.casaportemporada.model.Usuario;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class PerfilActivity extends AppCompatActivity {

    private ImageButton btnVoltar;
    private TextView textNome;
    private TextView textAno;
    private ImageView imgPerfil;
    private LinearLayout llSobre;
    private TextView textDescricao;
    private TextView textOndeVive;
    private TextView textTrabalho;
    private TextView btnEditar;
    private Usuario usuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil);
        iniciaComponentes();
        dadosUsuario();
        configCliques();
    }

    @Override
    protected void onResume() {
        super.onResume();
        dadosUsuario();
    }

    private void dadosUsuario() {
        DatabaseReference reference = FirebaseHelper.getDatabaseReference()
                .child("usuarios")
                .child(FirebaseHelper.getIdFirebase());

        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    usuario = snapshot.getValue(Usuario.class);
                    if (usuario != null) {
                        carregarDados(usuario);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private String dataCadastrado(Date data) {
        return new SimpleDateFormat("MMMM 'de' yyyy", new Locale("pt", "BR")).format(data);
    }

    private void carregarDados(Usuario usuario) {
        textNome.setText("Oi, meu nome é " +  usuario.getNome().split(" ")[0]);
        textAno.setText(textAno.getText().toString().replace(":ano", dataCadastrado(usuario.getData())));

        boolean txSb, txSb2, txSb3;

        if (usuario.getDescricao() != null && !usuario.getDescricao().trim().isEmpty()) {
            textDescricao.setText(usuario.getDescricao());
            txSb = false;
        } else {
            textDescricao.setVisibility(View.GONE);
            txSb = true;
        }

        if (usuario.getLocalizacao() != null && !usuario.getLocalizacao().trim().isEmpty()) {
            textOndeVive.setText("Vive em " + usuario.getLocalizacao());
            txSb2 = false;
        } else {
            textOndeVive.setVisibility(View.GONE);
            findViewById(R.id.img_local).setVisibility(View.GONE);
            txSb2 = true;
        }

        if (usuario.getTrabalho() != null && !usuario.getTrabalho().trim().isEmpty()) {
            textTrabalho.setText("Trabalho: " + usuario.getTrabalho());
            txSb3 = false;
        } else {
            textTrabalho.setVisibility(View.GONE);
            findViewById(R.id.img_trabalho).setVisibility(View.GONE);
            txSb3 = true;
        }

        if (txSb && txSb2 && txSb3) llSobre.setVisibility(View.GONE);

        if (usuario.getImagem() != null) {
            Picasso.get().load(usuario.getImagem()).into(imgPerfil);
        } else {
            imgPerfil.setImageDrawable(getDrawable(R.drawable.perfil_null));
        }
    }

    private void iniciaComponentes() {
        btnVoltar = findViewById(R.id.btn_voltar);
        btnVoltar.setImageTintList(ColorStateList.valueOf(ContextCompat.getColor(this, R.color.black)));
        textNome = findViewById(R.id.text_nome);
        textAno = findViewById(R.id.text_ano);
        imgPerfil = findViewById(R.id.img_perfil);
        textDescricao = findViewById(R.id.text_descricao);
        textOndeVive = findViewById(R.id.text_onde_vive);
        textTrabalho = findViewById(R.id.text_trabalho);
        llSobre = findViewById(R.id.ll_sobre);
        btnEditar = findViewById(R.id.botao_pagina);
    }

    private void configCliques() {
        btnVoltar.setOnClickListener(v -> finish());

        btnEditar.setOnClickListener(v -> {
            Intent intent = new Intent(this, EditarPerfilActivity.class);
            if(usuario != null) {
                intent.putExtra("usuario", usuario);
            }

            startActivity(intent);
        });
    }
}