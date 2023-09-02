package com.celio.vitor.casaportemporada.adapter;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.celio.vitor.casaportemporada.R;
import com.celio.vitor.casaportemporada.Util.UtilAirbnb;
import com.celio.vitor.casaportemporada.activity.authentication.LoginActivity;
import com.celio.vitor.casaportemporada.helper.FirebaseHelper;
import com.celio.vitor.casaportemporada.model.Imovel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class AdapterImovel extends RecyclerView.Adapter<AdapterImovel.ImovelViewHolder> {

    private List<Imovel> imovelList;
    private int layout;
    private OnCLick onCLick;

    public AdapterImovel(int layout, List<Imovel> imovelList, OnCLick onCLick) {
        this.layout = layout;
        this.imovelList = imovelList;
        this.onCLick = onCLick;
    }

    @NonNull
    @Override
    public ImovelViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(layout, parent, false);
        return new ImovelViewHolder(view, layout);
    }

    @Override
    public void onBindViewHolder(@NonNull ImovelViewHolder holder, int position) {
        Imovel imovel = imovelList.get(position);

        Picasso.get().load(imovel.getImagem()).into(holder.imgAnuncio);
        holder.textTitulo.setText(imovel.getTitulo());
        holder.textPreco.setText(NumberFormat.getCurrencyInstance(new Locale("pt", "BR")).format(imovel.getPreco()));

        if (holder.btnFavorito != null) {
            if (FirebaseHelper.getAutenticado()) {
                verificarFavoritos(imovel, holder.btnFavorito);
            }

            holder.textDescricao.setText(imovel.getDescricao());
            holder.btnFavorito.setOnClickListener(v -> {
                if (FirebaseHelper.getAutenticado()) {

                    String drawable = holder.itemView.getResources().getResourceName(R.drawable.ic_fav);
                    String drawable2 = holder.itemView.getResources().getResourceName(Integer.parseInt(holder.btnFavorito.getTag().toString()));

                    boolean isFav = drawable.equals(drawable2);

                    if (isFav) {
                        imovel.desfavoritar();
                    } else {
                        imovel.favoritar();
                    }

                    verificarFavoritos(imovel, holder.btnFavorito);
                } else {
                    UtilAirbnb.showDialogLogin(holder.itemView.getContext(), null, null, "Não", "Sim", "Criar conta", true);
                }
            });

        } else {
            if (imovel.isStatus()) {
                Drawable drawable = ContextCompat.getDrawable(holder.imgStatus.getContext(), R.drawable.ic_check);
                holder.imgStatus.setImageDrawable(drawable);
            } else {
                Drawable drawable = ContextCompat.getDrawable(holder.imgStatus.getContext(), R.drawable.ic_status_false);
                holder.imgStatus.setImageDrawable(drawable);
            }

        }

        holder.itemView.setOnClickListener(v -> onCLick.onClickListener(imovel));
    }

    @Override
    public int getItemCount() {
        return imovelList.size();
    }

    private void verificarFavoritos(Imovel imovel, ImageButton btnFavorito) {

        try {
            FirebaseHelper.getDatabaseReference()
                    .child("favoritos")
                    .child(FirebaseHelper.getIdFirebase())
                    .child(imovel.getId()).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            Drawable drawable = null;
                            if (snapshot.exists()) {
                                drawable = ContextCompat.getDrawable(btnFavorito.getContext(), R.drawable.ic_fav);
                                btnFavorito.setTag(R.drawable.ic_fav);

                            } else {
                                drawable = ContextCompat.getDrawable(btnFavorito.getContext(), R.drawable.ic_nao_fav);
                                btnFavorito.setTag(R.drawable.ic_nao_fav);
                            }

                            btnFavorito.setImageDrawable(drawable);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });

        } catch (NullPointerException e) {
            Log.i("Erro", "Usuário não logado");
        }
    }

    public interface OnCLick {
        public void onClickListener(Imovel imovel);
    }

    static class ImovelViewHolder extends RecyclerView.ViewHolder {
        ImageButton btnFavorito;
        ImageView imgAnuncio;
        ImageView imgStatus;
        TextView textTitulo;
        TextView textPreco;
        TextView textDescricao;

        public ImovelViewHolder(@NonNull View v, int layout) {
            super(v);

            if (layout == R.layout.item_anuncio) {
                btnFavorito = v.findViewById(R.id.btn_favorito);
                textDescricao = v.findViewById(R.id.text_descricao);
            } else {
                imgStatus = v.findViewById(R.id.img_status);
            }

            imgAnuncio = v.findViewById(R.id.img_anuncio);
            textTitulo = v.findViewById(R.id.text_titulo);
            textPreco = v.findViewById(R.id.text_preco_anuncio);
        }
    }
}
