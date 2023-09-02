package com.celio.vitor.casaportemporada.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.celio.vitor.casaportemporada.R;
import com.celio.vitor.casaportemporada.helper.FirebaseHelper;
import com.celio.vitor.casaportemporada.model.Filtro;
import com.celio.vitor.casaportemporada.model.Imovel;
import com.celio.vitor.casaportemporada.model.enums.AcomodacaoEspaco;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class FiltroAnuncioActivity extends AppCompatActivity {

    private TextView textPrecoMedio;
    private EditText editprecoMinimo;
    private EditText editprecoMaximo;
    private CheckBox cbEspacoInteiro;
    private CheckBox cbQuartoPrivativo;
    private CheckBox cbQuartoCompartilhado;
    private TextView removerFiltros;
    private Button mostrarAcomodacoes;
    private ImageButton btnClose;
    private List<Imovel> imovelList = new ArrayList<>();
    private Filtro filtro;
    private int qtdQuartos;
    private int qtdCamas;
    private int qtdBanheiros;
    private int qtdGaragens;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filtro_anuncio);

        iniciaComponentes();
        filtro = (Filtro) getIntent().getSerializableExtra("filtro");
        if (filtro == null) {
            filtro = new Filtro();
        } else {
            configFiltros();
        }

        recuperaAnuncios();
        configCliques();
    }

    private void configFiltros() {
        editprecoMinimo.setText(filtro.getPrecoMinimo() != 0 ? String.valueOf(filtro.getPrecoMinimo()) : "");
        editprecoMaximo.setText(filtro.getPrecoMaximo() != 0 ? String.valueOf(filtro.getPrecoMaximo()) : "");
        verificaListDetalhes(listGaragens().get(filtro.getQtdGaragens() != null ? Integer.parseInt(filtro.getQtdGaragens()) : 0), listGaragens());
        verificaListDetalhes(listCamas().get(filtro.getQtdCamas() != null ? Integer.parseInt(filtro.getQtdCamas()) : 0), listCamas());
        verificaListDetalhes(listQuartos().get(filtro.getQtdQuartos() != null ? Integer.parseInt(filtro.getQtdQuartos()) : 0), listQuartos());
        verificaListDetalhes(listBanheiros().get(filtro.getQtdBanheiros() != null ? Integer.parseInt(filtro.getQtdBanheiros()) : 0), listBanheiros());

        if(filtro.getTipoLugar() != null) {
            if (filtro.getTipoLugar().contains(AcomodacaoEspaco.ESPACO_INTEIRO.getDescricao())) {
                cbEspacoInteiro.setChecked(true);
            }

            if (filtro.getTipoLugar().contains(AcomodacaoEspaco.QUARTO_INTEIRO.getDescricao())) {
                cbQuartoPrivativo.setChecked(true);
            }

            if (filtro.getTipoLugar().contains(AcomodacaoEspaco.QUARTO_COMPARTILHADO.getDescricao())) {
                cbQuartoCompartilhado.setChecked(true);
            }
        }
    }

    private void iniciaComponentes() {
        textPrecoMedio = findViewById(R.id.text_preco_medio);
        editprecoMinimo = findViewById(R.id.edit_preco_minimo);
        editprecoMaximo = findViewById(R.id.edit_preco_maximo);
        cbEspacoInteiro = findViewById(R.id.cb_espaco_inteiro);
        cbQuartoPrivativo = findViewById(R.id.cb_quarto_privado);
        cbQuartoCompartilhado = findViewById(R.id.cb_quarto_compartilhado);
        removerFiltros = findViewById(R.id.text_remover_filtros);
        mostrarAcomodacoes = findViewById(R.id.btn_mostrar_acomodacao);
        btnClose = findViewById(R.id.btn_close);
    }

    private void configCliques() {
        btnClose.setOnClickListener(v -> {
            finish();
        });

        removerFiltros.setOnClickListener(v -> {
            verificaListDetalhes(R.id.btn_garagem_qualquer_um, listGaragens());
            verificaListDetalhes(R.id.btn_cama_qualquer_um, listCamas());
            verificaListDetalhes(R.id.btn_quarto_qualquer_um, listQuartos());
            verificaListDetalhes(R.id.btn_banheiro_qualquer_um, listBanheiros());

            cbEspacoInteiro.setChecked(false);
            cbQuartoPrivativo.setChecked(false);
            cbQuartoCompartilhado.setChecked(false);
            editprecoMinimo.setText("");
            editprecoMaximo.setText("");

            setResult(RESULT_CANCELED);
            finish();
        });

        cbEspacoInteiro.setOnCheckedChangeListener((compoundButton, isChecked) -> {
            if (isChecked) {
                if (filtro.getTipoLugar() != null && !filtro.getTipoLugar().isEmpty()) {
                    filtro.setTipoLugar(filtro.getTipoLugar() + AcomodacaoEspaco.ESPACO_INTEIRO.getDescricao());
                } else {
                    filtro.setTipoLugar(AcomodacaoEspaco.ESPACO_INTEIRO.getDescricao());
                }
            } else {
                filtro.setTipoLugar(filtro.getTipoLugar().replace(AcomodacaoEspaco.ESPACO_INTEIRO.getDescricao(), ""));
            }
        });

        cbQuartoPrivativo.setOnCheckedChangeListener((compoundButton, isChecked) -> {
            if (isChecked) {
                if (filtro.getTipoLugar() != null && !filtro.getTipoLugar().isEmpty()) {
                    filtro.setTipoLugar(filtro.getTipoLugar() + AcomodacaoEspaco.QUARTO_INTEIRO.getDescricao());
                } else {
                    filtro.setTipoLugar(AcomodacaoEspaco.QUARTO_INTEIRO.getDescricao());
                }
            } else {
                filtro.setTipoLugar(filtro.getTipoLugar().replace(AcomodacaoEspaco.QUARTO_INTEIRO.getDescricao(), ""));
            }
        });

        cbQuartoCompartilhado.setOnCheckedChangeListener((compoundButton, isChecked) -> {
            if (isChecked) {
                if (filtro.getTipoLugar() != null && !filtro.getTipoLugar().isEmpty()) {
                    filtro.setTipoLugar(filtro.getTipoLugar() + AcomodacaoEspaco.QUARTO_COMPARTILHADO.getDescricao());
                } else {
                    filtro.setTipoLugar(AcomodacaoEspaco.QUARTO_COMPARTILHADO.getDescricao());
                }
            } else {
                filtro.setTipoLugar(filtro.getTipoLugar().replace(AcomodacaoEspaco.QUARTO_COMPARTILHADO.getDescricao(), ""));
            }
        });

        mostrarAcomodacoes.setOnClickListener(v -> {
            if (this.filtro == null) filtro = new Filtro();

            if (qtdQuartos > 0 || qtdBanheiros > 0 || qtdCamas > 0 || qtdGaragens > 0
                    || cbEspacoInteiro.isChecked() || cbQuartoPrivativo.isChecked() || cbQuartoCompartilhado.isChecked()
                    || !editprecoMinimo.getText().toString().trim().isEmpty() || !editprecoMaximo.getText().toString().trim().isEmpty()) {
                if (filtro.getTipoLugar() != null && filtro.getTipoLugar().isEmpty()) {
                    filtro.setTipoLugar(null);
                }

                filtro.setQtdQuartos(qtdQuartos != 0 ? String.valueOf(qtdQuartos) : null);
                filtro.setQtdBanheiros(qtdBanheiros != 0 ? String.valueOf(qtdBanheiros) : null);
                filtro.setQtdCamas(qtdCamas != 0 ? String.valueOf(qtdCamas) : null);
                filtro.setQtdGaragens(qtdGaragens != 0 ? String.valueOf(qtdGaragens) : null);

                try {
                    filtro.setPrecoMinimo(Integer.parseInt(editprecoMinimo.getText().toString()));
                } catch (Exception e) {
                    filtro.setPrecoMinimo(0);
                }

                try {
                    filtro.setPrecoMaximo(Integer.parseInt(editprecoMaximo.getText().toString()));
                } catch (Exception e) {
                    filtro.setPrecoMaximo(0);
                }

                Intent intent = new Intent();
                intent.putExtra("filtro", this.filtro);
                setResult(RESULT_OK, intent);
            } else {
                setResult(RESULT_CANCELED);
            }

            finish();
        });
    }

    private void recuperaAnuncios() {
        DatabaseReference reference = FirebaseHelper.getDatabaseReference()
                .child("anuncios_publicos");

        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    int precoTotal = 0;
                    for (DataSnapshot snap : snapshot.getChildren()) {
                        Imovel imovel = snap.getValue(Imovel.class);
                        precoTotal += imovel.getPreco();
                        imovelList.add(imovel);
                    }

                    double precoMedio = precoTotal / imovelList.size();
                    String precoMedioFormat = NumberFormat.getCurrencyInstance(new Locale("pt", "BR")).format(precoMedio);
                    textPrecoMedio.setText(textPrecoMedio.getText().toString().replace("x", precoMedioFormat));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private List<Integer> listQuartos() {
        List<Integer> listQuartos = new ArrayList<>();
        listQuartos.add(R.id.btn_quarto_qualquer_um);
        listQuartos.add(R.id.btn_quarto_um);
        listQuartos.add(R.id.btn_quarto_dois);
        listQuartos.add(R.id.btn_quarto_tres);
        listQuartos.add(R.id.btn_quarto_quatro);
        listQuartos.add(R.id.btn_quarto_cinco);
        listQuartos.add(R.id.btn_quarto_seis);
        listQuartos.add(R.id.btn_quarto_sete);
        listQuartos.add(R.id.btn_quarto_oito_mais);

        return listQuartos;
    }

    public void qtdQuartos(View view) {
        int quarto = verificaListDetalhes(view.getId(), listQuartos());
        qtdQuartos = quarto;
    }

    private List<Integer> listCamas() {
        List<Integer> listCamas = new ArrayList<>();
        listCamas.add(R.id.btn_cama_qualquer_um);
        listCamas.add(R.id.btn_cama_um);
        listCamas.add(R.id.btn_cama_dois);
        listCamas.add(R.id.btn_cama_tres);
        listCamas.add(R.id.btn_cama_quatro);
        listCamas.add(R.id.btn_cama_cinco);
        listCamas.add(R.id.btn_cama_seis);
        listCamas.add(R.id.btn_cama_sete);
        listCamas.add(R.id.btn_cama_oito_mais);

        return listCamas;
    }

    public void qtdCamas(View view) {
        int cama = verificaListDetalhes(view.getId(), listCamas());
        qtdCamas = cama;
    }

    private List<Integer> listBanheiros() {
        List<Integer> listBanheiros = new ArrayList<>();
        listBanheiros.add(R.id.btn_banheiro_qualquer_um);
        listBanheiros.add(R.id.btn_banheiro_um);
        listBanheiros.add(R.id.btn_banheiro_dois);
        listBanheiros.add(R.id.btn_banheiro_tres);
        listBanheiros.add(R.id.btn_banheiro_quatro);
        listBanheiros.add(R.id.btn_banheiro_cinco);
        listBanheiros.add(R.id.btn_banheiro_seis);
        listBanheiros.add(R.id.btn_banheiro_sete);
        listBanheiros.add(R.id.btn_banheiro_oito_mais);

        return listBanheiros;
    }

    public void qtdBanheiros(View view) {
        int banheiro = verificaListDetalhes(view.getId(), listBanheiros());
        qtdBanheiros = banheiro;
    }

    private List<Integer> listGaragens() {
        List<Integer> listGaragens = new ArrayList<>();
        listGaragens.add(R.id.btn_garagem_qualquer_um);
        listGaragens.add(R.id.btn_garagem_um);
        listGaragens.add(R.id.btn_garagem_dois);
        listGaragens.add(R.id.btn_garagem_tres);
        listGaragens.add(R.id.btn_garagem_quatro);
        listGaragens.add(R.id.btn_garagem_cinco);
        listGaragens.add(R.id.btn_garagem_seis);
        listGaragens.add(R.id.btn_garagem_sete);
        listGaragens.add(R.id.btn_garagem_oito_mais);

        return listGaragens;
    }

    public void qtdGaragens(View view) {
        int garagem = verificaListDetalhes(view.getId(), listGaragens());
        qtdGaragens = garagem;
    }

    private int verificaListDetalhes(int idComponente, List<Integer> list) {
        Button btn;
        for (Integer id : list) {
            if (idComponente != id) {
                btn = findViewById(id);
                btn.setBackground(getDrawable(R.drawable.bg_button_branco));
                btn.setTextColor(ColorStateList.valueOf(ContextCompat.getColor(this, R.color.black)));
            } else {
                btn = findViewById(idComponente);
                btn.setBackground(getDrawable(R.drawable.bg_button_preto2));
                btn.setTextColor(ColorStateList.valueOf(ContextCompat.getColor(this, R.color.white)));
            }
        }

        return list.indexOf(idComponente);
    }
}