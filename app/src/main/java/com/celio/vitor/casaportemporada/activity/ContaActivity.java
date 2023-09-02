package com.celio.vitor.casaportemporada.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.celio.vitor.casaportemporada.R;
import com.celio.vitor.casaportemporada.Util.UtilAirbnb;
import com.celio.vitor.casaportemporada.helper.FirebaseHelper;
import com.celio.vitor.casaportemporada.model.Usuario;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class ContaActivity extends AppCompatActivity {

    private LinearLayout btnAnuncios;
    private LinearLayout layoutPerfil;
    private LinearLayout layoutInfoPessoais;
    private TextView textUsuarioNome;
    private ImageView imgPerfil;
    private Button btnSair;
    private Button btnExplorar;
    private Button btnFavoritos;
    private Button btnViagens;
    private Button btnMensagens;
    private Button btnConta;
    private LinearLayout btnCriarAnuncio;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conta);

        iniciaComponentes();
        configCliques();
        instanciarUsuario();
    }

    private void instanciarUsuario() {
        DatabaseReference reference = FirebaseHelper.getDatabaseReference()
                .child("usuarios")
                .child(FirebaseHelper.getIdFirebase());

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Usuario usuario = snapshot.getValue(Usuario.class);
                textUsuarioNome.setText(usuario.getNome());
                if(usuario.getImagem() != null) {
                    Picasso.get().load(usuario.getImagem()).into(imgPerfil);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void iniciaComponentes() {
        btnCriarAnuncio = findViewById(R.id.btn_criar_anuncio);
        layoutPerfil = findViewById(R.id.layout_perfil);
        layoutInfoPessoais = findViewById(R.id.layoutInfoPessoais);
        textUsuarioNome = findViewById(R.id.text_usuario_nome);
        imgPerfil = findViewById(R.id.img_perfil);
        btnAnuncios = findViewById(R.id.btn_anuncios);
        btnSair = findViewById(R.id.btn_sair);
        btnExplorar = findViewById(R.id.btn_explorar);
        btnFavoritos = findViewById(R.id.btn_favoritos);
        btnViagens = findViewById(R.id.btn_viagens);
        btnMensagens = findViewById(R.id.btn_mensagens);
        btnConta = findViewById(R.id.btn_conta);
        UtilAirbnb.mudarCorTextoDrawableDoBotao(btnConta, this, R.color.cor_principal);
    }

    private void configCliques() {
        btnAnuncios.setOnClickListener(v -> startActivity(new Intent(this, MeusAnunciosActivity.class)));

        layoutPerfil.setOnClickListener(v -> startActivity(new Intent(this, PerfilActivity.class)));

        layoutInfoPessoais.setOnClickListener(v -> startActivity(new Intent(this, InformacoesPerfilActivity.class)));

        btnSair.setOnClickListener(v -> {
            FirebaseHelper.getAuth().signOut();
            finish();
        });

        btnExplorar.setOnClickListener(v -> {
            finish();
        });

        btnFavoritos.setOnClickListener(v -> {
            finish();
            startActivity(new Intent(this, FavoritosActivity.class));
        });

        btnViagens.setOnClickListener(v -> {
            finish();
            startActivity(new Intent(this, ViagensActivity.class));
        });

        btnMensagens.setOnClickListener(v -> {
            finish();
            startActivity(new Intent(this, MensagensActivity.class));
        });

        btnCriarAnuncio.setOnClickListener(v -> {
            startActivity(new Intent(this, FormAnuncioActivity.class));
        });

    }
}