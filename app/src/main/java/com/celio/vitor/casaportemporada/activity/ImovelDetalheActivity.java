package com.celio.vitor.casaportemporada.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.celio.vitor.casaportemporada.R;
import com.celio.vitor.casaportemporada.Util.UtilAirbnb;
import com.celio.vitor.casaportemporada.helper.FirebaseHelper;
import com.celio.vitor.casaportemporada.model.Imovel;
import com.celio.vitor.casaportemporada.model.Usuario;
import com.celio.vitor.casaportemporada.model.enums.AcomodacaoEspaco;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.NumberFormat;
import java.util.Locale;

public class ImovelDetalheActivity extends AppCompatActivity {

    private ImageButton btnVoltar;
    private ImageButton btnFavoritar;
    private ImageView imgAnuncio;
    private TextView textTitulo;
    private TextView textLocaliza;
    private TextView textEspaco;
    private TextView textDetalhes;
    private TextView textDescricao;
    private ImageView imgAcomodacao;
    private TextView textAcomodacao;
    private TextView textAcomodacaoDetalhe;
    private TextView textLocaliza2;
    private TextView textBairroRua;
    private Button btnFalarAnfitriao;
    private TextView textPrecoAnuncio;
    private Button btnReservar;
    private Imovel imovel;
    private Usuario usuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_imovel_detalhe);

        iniciaComponentes();

        imovel = (Imovel) getIntent().getSerializableExtra("imovel");
        if (imovel != null) {
            recuperaAnunciante();
            colocarDadosImoveis();
            if (FirebaseHelper.getAutenticado()) {
                verificarFavoritos(imovel, btnFavoritar);
            }
        }

        configCliques();
    }

    private void iniciaComponentes() {
        btnVoltar = findViewById(R.id.btn_voltar);
        btnFavoritar = findViewById(R.id.btn_favoritar);
        imgAnuncio = findViewById(R.id.img_anuncio);
        textTitulo = findViewById(R.id.text_titulo);
        textLocaliza = findViewById(R.id.text_localiza);
        textEspaco = findViewById(R.id.text_espaco);
        textDetalhes = findViewById(R.id.text_detalhes);
        textDescricao = findViewById(R.id.text_descricao);
        imgAcomodacao = findViewById(R.id.img_acomodacao);
        textAcomodacao = findViewById(R.id.text_tipo_acomodacao);
        textAcomodacaoDetalhe = findViewById(R.id.text_acomodacao_detalhe);
        textLocaliza2 = findViewById(R.id.text_localiza2);
        textBairroRua = findViewById(R.id.text_bairro_rua);
        btnFalarAnfitriao = findViewById(R.id.btn_falar_anfitriao);
        textPrecoAnuncio = findViewById(R.id.text_preco_anuncio);
        btnReservar = findViewById(R.id.btn_reservar);
    }

    private void configCliques() {
        btnVoltar.setOnClickListener(v -> finish());
        btnFavoritar.setOnClickListener(v -> {
            if (FirebaseHelper.getAutenticado()) {

                String drawable = getResources().getResourceName(R.drawable.ic_fav);
                String drawable2 = getResources().getResourceName(Integer.parseInt(btnFavoritar.getTag().toString()));

                boolean isFav = drawable.equals(drawable2);

                if (isFav) {
                    imovel.desfavoritar();
                } else {
                    imovel.favoritar();
                }

                verificarFavoritos(imovel, btnFavoritar);
            } else {
                UtilAirbnb.showDialogLogin(this, null, null, "Não", "Sim", "Criar conta", true);
            }
        });

        btnFalarAnfitriao.setOnClickListener(v -> {
            if (usuario != null) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:" + usuario.getTelefone()));
                startActivity(intent);
            } else {
                Toast.makeText(this, "Carregando informações, aguarde...", Toast.LENGTH_SHORT).show();
            }
        });

        if (FirebaseHelper.getAutenticado()) {
            if (FirebaseHelper.getIdFirebase().equals(imovel.getIdUsuario())) {
                btnFalarAnfitriao.setVisibility(View.GONE);
            }
        }
    }

    private void recuperaAnunciante() {
        DatabaseReference reference = FirebaseHelper.getDatabaseReference()
                .child("usuarios")
                .child(imovel.getIdUsuario());

        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                usuario = snapshot.getValue(Usuario.class);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
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
                                drawable = ContextCompat.getDrawable(btnFavorito.getContext(), R.drawable.ic_fav2);
                                btnFavorito.setTag(R.drawable.ic_fav2);

                            } else {
                                drawable = ContextCompat.getDrawable(btnFavorito.getContext(), R.drawable.ic_nao_fav2);
                                btnFavorito.setTag(R.drawable.ic_nao_fav2);
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

    private void colocarDadosImoveis() {
        Picasso.get().load(imovel.getImagem()).into(imgAnuncio);

        textTitulo.setText(imovel.getTitulo());

        String localizacao = imovel.getCidade() + ", " + imovel.getEstado() + ", " + imovel.getPais();
        textLocaliza.setPaintFlags(Paint.UNDERLINE_TEXT_FLAG);
        textLocaliza.setText(localizacao);

        textEspaco.setText(textEspaco.getText().toString()
                .replace("acomoda", imovel.getAcomodacaoDoEspaco())
                .replace("tipoEspaco", imovel.getTipoEspaco()));

        String detalhes = textDetalhes.getText().toString()
                .replace("xh", String.valueOf(imovel.getHospede()))
                .replace("xq", String.valueOf(imovel.getQuarto()))
                .replace("xc", String.valueOf(imovel.getCama()))
                .replace("xb", String.valueOf(imovel.getBanheiro()))
                .replace("xg", String.valueOf(imovel.getGaragem()));

        if(imovel.getHospede() == 1) detalhes = detalhes.replace("hóspedes", "hóspede");
        if(imovel.getQuarto() == 1) detalhes = detalhes.replace("quartos", "quarto");
        if(imovel.getCama() == 1) detalhes = detalhes.replace("camas", "cama");
        if(imovel.getBanheiro() == 1) detalhes = detalhes.replace("banheiros", "banheiro");
        if(imovel.getGaragem() == 1) detalhes = detalhes.replace("garagens", "garagem");

        textDetalhes.setText(detalhes);

        textDescricao.setText(imovel.getDescricao());

        if (imovel.getAcomodacaoDoEspaco().equals(AcomodacaoEspaco.ESPACO_INTEIRO.getDescricao())) {
            Drawable drawable = getDrawable(R.drawable.ic_casa);
            imgAcomodacao.setImageDrawable(drawable);
            textAcomodacao.setText("Um espaço inteiro");
            textAcomodacaoDetalhe.setText(R.string.text_espaco_inteiro);
        } else if (imovel.getAcomodacaoDoEspaco().equals(AcomodacaoEspaco.QUARTO_INTEIRO.getDescricao())) {
            Drawable drawable = getDrawable(R.drawable.ic_porta);
            imgAcomodacao.setImageDrawable(drawable);
            textAcomodacao.setText("Um quarto inteiro");
            textAcomodacaoDetalhe.setText(R.string.text_quarto_inteiro);
        } else {
            Drawable drawable = getDrawable(R.drawable.ic_casa_compartilhada);
            imgAcomodacao.setImageDrawable(drawable);
            textAcomodacao.setText("Um quarto compartilhado");
            textAcomodacaoDetalhe.setText(R.string.text_quarto_compartilhado);
        }

        textLocaliza2.setText(localizacao);

        String bairroRua = imovel.getRua() + ", " + (imovel.getApto() != null ? imovel.getApto() + ", " : "") + "CEP: " + imovel.getCep();
        textBairroRua.setText(bairroRua);

        String precoAnuncio = NumberFormat.getCurrencyInstance(new Locale("pt", "BR")).format(imovel.getPreco());
        textPrecoAnuncio.setText(precoAnuncio);
    }
}