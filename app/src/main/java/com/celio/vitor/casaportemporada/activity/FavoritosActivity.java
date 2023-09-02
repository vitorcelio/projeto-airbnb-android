package com.celio.vitor.casaportemporada.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.celio.vitor.casaportemporada.R;
import com.celio.vitor.casaportemporada.Util.UtilAirbnb;
import com.celio.vitor.casaportemporada.activity.authentication.LoginActivity;
import com.celio.vitor.casaportemporada.adapter.AdapterImovel;
import com.celio.vitor.casaportemporada.adapter.AdapterImovelFavoritos;
import com.celio.vitor.casaportemporada.helper.FirebaseHelper;
import com.celio.vitor.casaportemporada.model.Imovel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class FavoritosActivity extends AppCompatActivity implements AdapterImovelFavoritos.OnCLick {

    private Button btnExplorar;
    private Button btnFavoritos;
    private Button btnViagens;
    private Button btnMensagens;
    private Button btnConta;
    private LinearLayout layoutCarregando;
    private LinearLayout layoutSemAnuncio;
    private LinearLayout layoutSemLogin;
    private RecyclerView rvImoveis;
    private AdapterImovelFavoritos adapterImovel;
    private Button btnEntrar;
    private List<Imovel> imovelList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favoritos);

        iniciaComponentes();
        configCliques();
        configRecyclerView();

        if (FirebaseHelper.getAutenticado())
            recuperarAnuncio();
    }

    private void iniciaComponentes() {
        btnEntrar = findViewById(R.id.btn_entrar);
        rvImoveis = findViewById(R.id.rv_imoveis);
        btnExplorar = findViewById(R.id.btn_explorar);
        btnFavoritos = findViewById(R.id.btn_favoritos);
        UtilAirbnb.mudarCorTextoDrawableDoBotao(btnFavoritos, this, R.color.cor_principal);
        btnViagens = findViewById(R.id.btn_viagens);
        btnMensagens = findViewById(R.id.btn_mensagens);
        btnConta = findViewById(R.id.btn_conta);
        if (FirebaseHelper.getAutenticado()) {
            btnConta.setText("Perfil");
        } else {
            btnConta.setText("Entrar");
        }

        layoutCarregando = findViewById(R.id.layout_carregando_anuncios);
        layoutSemAnuncio = findViewById(R.id.layout_sem_anuncio);
        layoutSemLogin = findViewById(R.id.layout_sem_login);

        if (!FirebaseHelper.getAutenticado()) {
            layoutCarregando.setVisibility(View.GONE);
            layoutSemLogin.setVisibility(View.VISIBLE);
        }
    }

    private void configRecyclerView() {
        rvImoveis.setLayoutManager(new LinearLayoutManager(this));
        rvImoveis.setHasFixedSize(true);

        adapterImovel = new AdapterImovelFavoritos(R.layout.item_anuncio, imovelList, this);
        rvImoveis.setAdapter(adapterImovel);
    }

    private void recuperarAnuncio() {
        imovelList.clear();

        DatabaseReference reference = FirebaseHelper.getDatabaseReference()
                .child("favoritos")
                .child(FirebaseHelper.getIdFirebase());

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot snap : snapshot.getChildren()) {
                        Imovel imovel = snap.getValue(Imovel.class);
                        imovelList.add(imovel);
                    }
                } else {
                    layoutSemAnuncio.setVisibility(View.VISIBLE);
                }

                verificarAnuncios();
                layoutCarregando.setVisibility(View.GONE);
                adapterImovel.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void verificarAnuncios() {
        if (imovelList.size() >= 1) {
            layoutSemAnuncio.setVisibility(View.GONE);
        } else {
            layoutSemAnuncio.setVisibility(View.VISIBLE);
        }
    }

    private void configCliques() {
        btnExplorar.setOnClickListener(v -> finish());

        btnViagens.setOnClickListener(v -> {
            finish();
            startActivity(new Intent(this, ViagensActivity.class));
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

        btnEntrar.setOnClickListener(v -> {
            finish();
            startActivity(new Intent(this, LoginActivity.class));
        });
    }

    @Override
    public void onClickListener(Imovel imovel) {
        Intent intent = new Intent(this, ImovelDetalheActivity.class);
        intent.putExtra("imovel", imovel);
        startActivity(intent);
    }

    @Override
    public void onClickFavorito(Imovel imovel) {
        try {
            imovelList.remove(imovel);
            imovel.desfavoritar();
            adapterImovel.notifyItemRemoved(imovelList.indexOf(imovel));
            imovelList.clear();
        } catch (Exception e) {
            Toast.makeText(this, "Ocorreu um erro ao tentar remover item dos favoritos", Toast.LENGTH_SHORT).show();
        }
    }
}