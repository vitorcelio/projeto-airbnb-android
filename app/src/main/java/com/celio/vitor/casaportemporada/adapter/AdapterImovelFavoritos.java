package com.celio.vitor.casaportemporada.adapter;

import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.celio.vitor.casaportemporada.R;
import com.celio.vitor.casaportemporada.Util.UtilAirbnb;
import com.celio.vitor.casaportemporada.helper.FirebaseHelper;
import com.celio.vitor.casaportemporada.model.Imovel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class AdapterImovelFavoritos extends RecyclerView.Adapter<AdapterImovelFavoritos.ImovelViewHolder> {

    private List<Imovel> imovelList;
    private int layout;
    private OnCLick onCLick;

    public AdapterImovelFavoritos(int layout, List<Imovel> imovelList, OnCLick onCLick) {
        this.layout = layout;
        this.imovelList = imovelList;
        this.onCLick = onCLick;
    }

    @NonNull
    @Override
    public ImovelViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(layout, parent, false);
        return new ImovelViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ImovelViewHolder holder, int position) {
        Imovel imovel = imovelList.get(position);

        Picasso.get().load(imovel.getImagem()).into(holder.imgAnuncio);
        holder.textTitulo.setText(imovel.getTitulo());
        holder.textPreco.setText(NumberFormat.getCurrencyInstance(new Locale("pt", "BR")).format(imovel.getPreco()));

        Drawable drawable = ContextCompat.getDrawable(holder.btnFavorito.getContext(), R.drawable.ic_fav);
        holder.btnFavorito.setImageDrawable(drawable);

        holder.textDescricao.setText(imovel.getDescricao());
        holder.btnFavorito.setOnClickListener(v -> {
            onCLick.onClickFavorito(imovel);
        });

        holder.itemView.setOnClickListener(v -> onCLick.onClickListener(imovel));
    }

    @Override
    public int getItemCount() {
        return imovelList.size();
    }

    public interface OnCLick {
        public void onClickListener(Imovel imovel);

        public void onClickFavorito(Imovel imovel);
    }

    static class ImovelViewHolder extends RecyclerView.ViewHolder {
        ImageButton btnFavorito;
        ImageView imgAnuncio;
        TextView textTitulo;
        TextView textPreco;
        TextView textDescricao;

        public ImovelViewHolder(@NonNull View v) {
            super(v);
            btnFavorito = v.findViewById(R.id.btn_favorito);
            textDescricao = v.findViewById(R.id.text_descricao);
            imgAnuncio = v.findViewById(R.id.img_anuncio);
            textTitulo = v.findViewById(R.id.text_titulo);
            textPreco = v.findViewById(R.id.text_preco_anuncio);
        }
    }
}
