package com.celio.vitor.casaportemporada.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.celio.vitor.casaportemporada.R;
import com.celio.vitor.casaportemporada.Util.UtilAirbnb;
import com.celio.vitor.casaportemporada.activity.authentication.LoginActivity;
import com.celio.vitor.casaportemporada.adapter.AdapterImovel;
import com.celio.vitor.casaportemporada.helper.FirebaseHelper;
import com.celio.vitor.casaportemporada.model.Filtro;
import com.celio.vitor.casaportemporada.model.Imovel;
import com.celio.vitor.casaportemporada.model.enums.AcomodacaoEspaco;
import com.celio.vitor.casaportemporada.model.enums.TipoEspaco;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MainActivity extends AppCompatActivity implements AdapterImovel.OnCLick {

    private final int REQUEST_FILTRO = 100;
    private LinearLayout layoutCarregandoAnuncio;
    private LinearLayout layoutSemAnuncios;
    private Button btnExplorar;
    private Button btnFavoritos;
    private Button btnViagens;
    private Button btnMensagens;
    private Button btnConta;
    private ImageButton btnFiltro;
    private RecyclerView rvAnuncios;
    private List<Imovel> imovelList = new ArrayList<>();
    private AdapterImovel adapterImovel;
    private Filtro filtro;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        filtro = (Filtro) getIntent().getSerializableExtra("filtro");

        iniciaComponenetes();
        configCliques();
        configRecyclerView();
        recuperarAnuncios(null);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (FirebaseHelper.getAutenticado()) {
            btnConta.setText("Perfil");
        } else {
            btnConta.setText("Entrar");
        }

        List<Integer> ids = idsBotoes();
        for (Integer id : ids) {
            mudarLinearlayoutDefault(id);
        }

        configRecyclerView();
        if(filtro == null) recuperarAnuncios(null);
    }

    private void verificarAnuncios() {
        if (imovelList.size() >= 1) {
            layoutSemAnuncios.setVisibility(View.GONE);
        } else {
            layoutSemAnuncios.setVisibility(View.VISIBLE);
        }
    }

    private void iniciaComponenetes() {
        layoutCarregandoAnuncio = findViewById(R.id.layout_carregando_anuncios);
        layoutSemAnuncios = findViewById(R.id.layout_sem_anuncio);
        btnFiltro = findViewById(R.id.btn_filtro);
        btnExplorar = findViewById(R.id.btn_explorar);
        UtilAirbnb.mudarCorTextoDrawableDoBotao(btnExplorar, this, R.color.cor_principal);
        btnFavoritos = findViewById(R.id.btn_favoritos);
        btnViagens = findViewById(R.id.btn_viagens);
        btnMensagens = findViewById(R.id.btn_mensagens);
        btnConta = findViewById(R.id.btn_conta);
        if (FirebaseHelper.getAutenticado()) {
            btnConta.setText("Perfil");
        } else {
            btnConta.setText("Entrar");
        }
        rvAnuncios = findViewById(R.id.rv_anuncios);
    }

    private void configCliques() {

        btnFavoritos.setOnClickListener(v -> {
            startActivity(new Intent(this, FavoritosActivity.class));
        });

        btnViagens.setOnClickListener(v -> {
            startActivity(new Intent(this, ViagensActivity.class));
        });

        btnMensagens.setOnClickListener(v -> {
            startActivity(new Intent(this, MensagensActivity.class));
        });

        btnConta.setOnClickListener(v -> {
            if (FirebaseHelper.getAutenticado()) {
                startActivity(new Intent(this, ContaActivity.class));
            } else {
                startActivity(new Intent(this, LoginActivity.class));
            }
        });

        btnFiltro.setOnClickListener(v -> {
            Intent intent = new Intent(this, FiltroAnuncioActivity.class);
            if(filtro != null) {
                intent.putExtra("filtro", filtro);
            }

            startActivityForResult(intent, REQUEST_FILTRO);
        });
    }

    private void mudarLinearlayout(int id) {
        LinearLayout layout = findViewById(id);

        ImageView imgBtn = (ImageView) layout.getChildAt(0);
        imgBtn.setImageTintList(ColorStateList.valueOf(ContextCompat.getColor(this, R.color.black)));

        TextView textBtn = (TextView) layout.getChildAt(1);
        textBtn.setTextColor(ColorStateList.valueOf(ContextCompat.getColor(this, R.color.black)));

        ImageView selectBtn = (ImageView) layout.getChildAt(2);
        selectBtn.setVisibility(View.VISIBLE);
    }

    private void mudarLinearlayoutDefault(int id) {
        LinearLayout layout = findViewById(id);

        ImageView imgBtn = (ImageView) layout.getChildAt(0);
        imgBtn.setImageTintList(ColorStateList.valueOf(ContextCompat.getColor(this, R.color.cor_menu_tipo)));

        TextView textBtn = (TextView) layout.getChildAt(1);
        textBtn.setTextColor(ColorStateList.valueOf(ContextCompat.getColor(this, R.color.cor_menu_tipo)));

        ImageView selectBtn = (ImageView) layout.getChildAt(2);
        selectBtn.setVisibility(View.GONE);
    }

    public void cliqueTipoEspaco(View view) {
        List<Integer> ids = idsBotoes();
        int idClicado = view.getId();

        switch (idClicado) {
            case R.id.btn_casa:
                recuperarAnuncios(TipoEspaco.CASA.getDescricao());
                break;
            case R.id.btn_apartamento:
                recuperarAnuncios(TipoEspaco.APARTAMENTO.getDescricao());
                break;
            case R.id.btn_celeiro:
                recuperarAnuncios(TipoEspaco.CELEIRO.getDescricao());
                break;
            case R.id.btn_barco:
                recuperarAnuncios(TipoEspaco.BARCO.getDescricao());
                break;
            case R.id.btn_cabana:
                recuperarAnuncios(TipoEspaco.CABANA.getDescricao());
                break;
            case R.id.btn_castelo:
                recuperarAnuncios(TipoEspaco.CASTELO.getDescricao());
                break;
            case R.id.btn_hotel:
                recuperarAnuncios(TipoEspaco.HOTEL.getDescricao());
                break;
            case R.id.btn_casas_barco:
                recuperarAnuncios(TipoEspaco.CASAS_BARCO.getDescricao());
                break;
            case R.id.btn_torre:
                recuperarAnuncios(TipoEspaco.TORRE.getDescricao());
                break;
            case R.id.btn_pousada:
                recuperarAnuncios(TipoEspaco.POUSADA.getDescricao());
                break;

        }
        mudarLinearlayout(idClicado);

        for (Integer id : ids) {
            if (id != idClicado) {
                mudarLinearlayoutDefault(id);
            }
        }
    }

    private List<Integer> idsBotoes() {
        List<Integer> ids = new ArrayList<>();
        ids.add(R.id.btn_casa);
        ids.add(R.id.btn_apartamento);
        ids.add(R.id.btn_celeiro);
        ids.add(R.id.btn_barco);
        ids.add(R.id.btn_cabana);
        ids.add(R.id.btn_castelo);
        ids.add(R.id.btn_hotel);
        ids.add(R.id.btn_casas_barco);
        ids.add(R.id.btn_torre);
        ids.add(R.id.btn_pousada);

        return ids;
    }

    private void configRecyclerView() {
        rvAnuncios.setLayoutManager(new LinearLayoutManager(this));
        rvAnuncios.setHasFixedSize(true);

        adapterImovel = new AdapterImovel(R.layout.item_anuncio, imovelList, this);
        rvAnuncios.setAdapter(adapterImovel);
    }

    private void recuperarAnuncios(String tipoEspaco) {
        DatabaseReference reference = FirebaseHelper.getDatabaseReference()
                .child("anuncios_publicos");


        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    imovelList.clear();

                    for (DataSnapshot snap : snapshot.getChildren()) {
                        Imovel imovel = snap.getValue(Imovel.class);

                        if (imovel.isStatus()) {
                            if (tipoEspaco != null) {
                                if (imovel.getTipoEspaco().equals(tipoEspaco)) {
                                    imovelList.add(imovel);
                                }
                            } else {
                                imovelList.add(imovel);
                            }
                        }
                    }
                }

                verificarAnuncios();
                layoutCarregandoAnuncio.setVisibility(View.GONE);
                Collections.reverse(imovelList);
                adapterImovel.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(MainActivity.this, "Erro ao carregar anúncios. Tente novamente mais tarde!", Toast.LENGTH_SHORT).show();
                layoutSemAnuncios.setVisibility(View.VISIBLE);
                layoutCarregandoAnuncio.setVisibility(View.GONE);
            }
        });

    }

    private void recuperarAnunciosFiltros(String tipoEspaco) {
        DatabaseReference reference = FirebaseHelper.getDatabaseReference()
                .child("anuncios_publicos");


        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    imovelList.clear();

                    for (DataSnapshot snap : snapshot.getChildren()) {
                        Imovel imovel = snap.getValue(Imovel.class);

                        if (imovel.isStatus()) {
                            if (tipoEspaco != null) {
                                if (imovel.getTipoEspaco().equals(tipoEspaco)) {

                                    if ((filtro.getQtdQuartos() == null || imovel.getQuarto() >= Integer.parseInt(filtro.getQtdQuartos())) &&
                                            (filtro.getQtdBanheiros() == null || imovel.getBanheiro() >= Integer.parseInt(filtro.getQtdBanheiros())) &&
                                            (filtro.getQtdCamas() == null || imovel.getCama() >= Integer.parseInt(filtro.getQtdCamas())) &&
                                            (filtro.getQtdGaragens() == null || imovel.getGaragem() >= Integer.parseInt(filtro.getQtdGaragens())) &&
                                            (filtro.getTipoLugar() == null || filtro.getTipoLugar().contains(imovel.getAcomodacaoDoEspaco())) &&
                                            (filtro.getPrecoMinimo() == 0 || imovel.getPreco() >= filtro.getPrecoMinimo()) &&
                                            (filtro.getPrecoMaximo() == 0 || imovel.getPreco() <= filtro.getPrecoMaximo())) {
                                        imovelList.add(imovel);
                                    }

                                }
                            } else {
                                if ((filtro.getQtdQuartos() == null || imovel.getQuarto() >= Integer.parseInt(filtro.getQtdQuartos())) &&
                                        (filtro.getQtdBanheiros() == null || imovel.getBanheiro() >= Integer.parseInt(filtro.getQtdBanheiros())) &&
                                        (filtro.getQtdCamas() == null || imovel.getCama() >= Integer.parseInt(filtro.getQtdCamas())) &&
                                        (filtro.getQtdGaragens() == null || imovel.getGaragem() >= Integer.parseInt(filtro.getQtdGaragens())) &&
                                        (filtro.getTipoLugar() == null || filtro.getTipoLugar().contains(imovel.getAcomodacaoDoEspaco())) &&
                                        (filtro.getPrecoMinimo() == 0 || imovel.getPreco() >= filtro.getPrecoMinimo()) &&
                                        (filtro.getPrecoMaximo() == 0 || imovel.getPreco() <= filtro.getPrecoMaximo())) {
                                    imovelList.add(imovel);
                                }
                            }
                        }
                    }
                }

                verificarAnuncios();
                layoutCarregandoAnuncio.setVisibility(View.GONE);
                Collections.reverse(imovelList);
                adapterImovel.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(MainActivity.this, "Erro ao carregar anúncios. Tente novamente mais tarde!", Toast.LENGTH_SHORT).show();
                layoutSemAnuncios.setVisibility(View.VISIBLE);
                layoutCarregandoAnuncio.setVisibility(View.GONE);
            }
        });

    }

    @Override
    public void onClickListener(Imovel imovel) {
        Intent intent = new Intent(this, ImovelDetalheActivity.class);
        intent.putExtra("imovel", imovel);
        startActivity(intent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        layoutSemAnuncios.setVisibility(View.GONE);
        layoutCarregandoAnuncio.setVisibility(View.VISIBLE);

        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_FILTRO) {
                filtro = (Filtro) data.getSerializableExtra("filtro");
                if (filtro != null) {
                    recuperarAnunciosFiltros(null);
                }
            }
        } else if (resultCode == RESULT_CANCELED){
            recuperarAnuncios(null);
        }
    }
}