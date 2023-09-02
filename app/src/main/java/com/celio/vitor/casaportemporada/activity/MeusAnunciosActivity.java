package com.celio.vitor.casaportemporada.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.celio.vitor.casaportemporada.R;
import com.celio.vitor.casaportemporada.adapter.AdapterImovel;
import com.celio.vitor.casaportemporada.helper.FirebaseHelper;
import com.celio.vitor.casaportemporada.model.Imovel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.tsuryo.swipeablerv.SwipeLeftRightCallback;
import com.tsuryo.swipeablerv.SwipeableRecyclerView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MeusAnunciosActivity extends AppCompatActivity implements AdapterImovel.OnCLick {

    private List<Imovel> imovelList = new ArrayList<>();
    private LinearLayout layoutCarregandoAnuncio;
    private LinearLayout layoutSemAnuncios;
    private TextView textAnunciosQtd;
    private SwipeableRecyclerView rvAnuncios;
    private AdapterImovel adapterImovel;
    private ImageButton btnClose;
    private SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meus_anuncios);

        preferences = getSharedPreferences("dialog-verifica", 0);

        if(preferences.getString("visto", null) == null) {
            showDialogInformacao();
        }

        iniciaComponentes();

        configRecyclerView();

        recuperaAnuncios();

        configCliques();
    }

    private void configRecyclerView() {
        rvAnuncios.setLayoutManager(new LinearLayoutManager(this));
        rvAnuncios.setHasFixedSize(true);

        adapterImovel = new AdapterImovel(R.layout.item_meus_anuncio, imovelList, this);

        rvAnuncios.setAdapter(adapterImovel);

        rvAnuncios.setListener(new SwipeLeftRightCallback.Listener() {
            @Override
            public void onSwipedLeft(int position) {
                Imovel imovel = imovelList.get(position);
                imovelList.remove(imovel);
                imovel.remover();

                adapterImovel.notifyItemRemoved(position);
                verificaQtdAnuncios();
            }

            @Override
            public void onSwipedRight(int position) {
                Imovel imovel = imovelList.get(position);
                imovel.setStatus(false);
                imovel.salvar();

                adapterImovel.notifyItemChanged(position);
            }
        });
    }

    private void recuperaAnuncios() {
        DatabaseReference reference = FirebaseHelper.getDatabaseReference()
                .child("anuncios")
                .child(FirebaseHelper.getIdFirebase());

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    imovelList.clear();
                    for (DataSnapshot snap : snapshot.getChildren()) {
                        Imovel imovel = snap.getValue(Imovel.class);
                        imovelList.add(imovel);
                    }
                } else {
                    layoutSemAnuncios.setVisibility(View.VISIBLE);
                }

                verificaQtdAnuncios();
                layoutCarregandoAnuncio.setVisibility(View.GONE);
                Collections.reverse(imovelList);
                adapterImovel.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(MeusAnunciosActivity.this, "Erro ao carregar anúncios. Tente novamente mais tarde!", Toast.LENGTH_SHORT).show();
                layoutSemAnuncios.setVisibility(View.VISIBLE);
                layoutCarregandoAnuncio.setVisibility(View.GONE);
            }
        });
    }

    private void iniciaComponentes() {
        layoutCarregandoAnuncio = findViewById(R.id.layout_carregando_anuncios);
        layoutSemAnuncios = findViewById(R.id.layout_sem_anuncio);
        textAnunciosQtd = findViewById(R.id.text_anuncios_qtd);
        rvAnuncios = findViewById(R.id.rv_anuncios);
        btnClose = findViewById(R.id.btn_close);
    }

    private void configCliques() {
        btnClose.setOnClickListener(v -> finish());
    }

    @Override
    public void onClickListener(Imovel imovel) {
        Intent intent = new Intent(this, FormAnuncioEtapa1_2_Activity.class);
        intent.putExtra("imovel", imovel);
        startActivity(intent);
    }

    private void verificaQtdAnuncios() {
        textAnunciosQtd.setText(String.valueOf(imovelList.size()) + (imovelList.size() == 1 ? " anúncio" : " anúncios"));
    }

    private void showDialogInformacao() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Informações");
        builder.setMessage("Clique no anúncio para editá-lo. Para excluí-lo arraste para a esquerda e para invativá-lo arraste para a direita.");
        builder.setCancelable(false);
        builder.setNeutralButton("Não mostrar novamente", (dialogInterface, i) -> {
            SharedPreferences.Editor editor = preferences.edit();
            editor.putString("visto", "v");
            editor.apply();
        });
        builder.setPositiveButton("Ok", (dialogInterface, i) -> dialogInterface.dismiss());

        AlertDialog dialog = builder.create();
        dialog.show();
    }

}