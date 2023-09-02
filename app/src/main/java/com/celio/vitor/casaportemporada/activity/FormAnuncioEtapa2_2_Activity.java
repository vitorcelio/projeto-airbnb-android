package com.celio.vitor.casaportemporada.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentContainerView;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.ImageDecoder;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.celio.vitor.casaportemporada.R;
import com.celio.vitor.casaportemporada.Util.UtilAirbnb;
import com.celio.vitor.casaportemporada.helper.FirebaseHelper;
import com.celio.vitor.casaportemporada.model.Imovel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.normal.TedPermission;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.List;

public class FormAnuncioEtapa2_2_Activity extends AppCompatActivity {

    private static final int REQUEST_GALERIA = 100;
    private TextView textView;
    private TextView btnVoltar;
    private Button btnAvancar;
    private Button btnSair;
    private ProgressBar progressBar1;
    private ProgressBar progressBar2;
    private ProgressBar progressBarImg;
    private ImageButton btnMenu;
    private LinearLayout menu;
    private LinearLayout escolheFoto;
    private FrameLayout fotoSelecionada;
    private ImageView imgAnuncio;
    private Bitmap imagem;
    private String caminhoDaImagem;
    private Button btnEditar;
    private Button btnExcluir;
    private Imovel imovel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_anuncio_etapa22);

        iniciaComponenetes();
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            imovel = (Imovel) bundle.getSerializable("imovel");
            if (imovel.getImagem() != null) {
                Picasso.get().load(imovel.getImagem()).into(imgAnuncio);
                verificarImagemImovel(true);
            }

            textView.setText(textView.getText().toString().replace("tipoAcomodacao", imovel.getTipoEspaco()));
        }

        configCliques();
    }

    private void iniciaComponenetes() {
        textView = findViewById(R.id.textView);
        btnVoltar = findViewById(R.id.botao_voltar);
        btnAvancar = findViewById(R.id.btn_avancar);
        btnSair = findViewById(R.id.btn_sair);
        progressBar1 = findViewById(R.id.progressBar);
        progressBar1.incrementProgressBy(100);
        progressBar2 = findViewById(R.id.progressBar2);
        progressBar2.incrementProgressBy(25);
        progressBarImg = findViewById(R.id.progress_bar_img);
        btnMenu = findViewById(R.id.btn_menu);
        escolheFoto = findViewById(R.id.escolhe_foto);
        imgAnuncio = findViewById(R.id.img_anuncio);
        fotoSelecionada = findViewById(R.id.frame_foto);
        menu = findViewById(R.id.ll_menu);
        btnEditar = findViewById(R.id.btn_editar);
        btnExcluir = findViewById(R.id.btn_excluir);
    }

    private void configCliques() {
        btnVoltar.setOnClickListener(v -> {
            finish();
            Intent intent = new Intent(this, FormAnuncioEtapa2_1_Activity.class);
            intent.putExtra("imovel", this.imovel);
            startActivity(intent);
        });

        btnAvancar.setOnClickListener(v -> {
            if (imovel.getUriImagem() != null || imovel.getImagem() != null) {
                salvarImagemAnuncio();
            } else {
                Toast.makeText(this, "Selecione uma imagem para prosseguir com o cadastro do imóvel.", Toast.LENGTH_SHORT).show();
            }
        });

        btnSair.setOnClickListener(v -> {
            imovel.salvar();
            finish();
        });

        btnMenu.setOnClickListener(v -> {
            if (menu.getVisibility() == View.VISIBLE) {
                menu.setVisibility(View.GONE);
            } else {
                menu.setVisibility(View.VISIBLE);
            }
        });

        btnEditar.setOnClickListener(v -> {
            menu.setVisibility(View.GONE);
            verificarPermissaoGaleria(null);
        });

        btnExcluir.setOnClickListener(v -> {
            caminhoDaImagem = null;
            imovel.setImagem(null);
            menu.setVisibility(View.GONE);
            verificarImagemImovel(false);
        });
    }

    public void verificarPermissaoGaleria(View view) {
        PermissionListener permissionListener = new PermissionListener() {
            @Override
            public void onPermissionGranted() {
                abrirGaleria();
            }

            @Override
            public void onPermissionDenied(List<String> deniedPermissions) {
                Toast.makeText(FormAnuncioEtapa2_2_Activity.this, "Permissão negada", Toast.LENGTH_SHORT).show();
            }
        };

        showDialogPermissaoGaleria(permissionListener, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE});
    }

    private void showDialogPermissaoGaleria(PermissionListener listener, String[] permissoes) {
        TedPermission.create()
                .setPermissionListener(listener)
                .setDeniedTitle("Permissões negadas")
                .setDeniedMessage(R.string.text_denied_message)
                .setDeniedCloseButtonText("Não")
                .setGotoSettingButtonText("Sim")
                .setPermissions(permissoes)
                .check();
    }

    private void abrirGaleria() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, REQUEST_GALERIA);
    }

    private void verificarImagemImovel(boolean img) {
        if (img) {
            fotoSelecionada.setVisibility(View.VISIBLE);
            escolheFoto.setVisibility(View.GONE);
        } else {
            fotoSelecionada.setVisibility(View.GONE);
            escolheFoto.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_GALERIA) {
                Uri localImage = data.getData();
                caminhoDaImagem = localImage.toString();
                imovel.setUriImagem(caminhoDaImagem);

                if (Build.VERSION.SDK_INT < 28) {
                    try {
                        imagem = MediaStore.Images.Media.getBitmap(getBaseContext().getContentResolver(), localImage);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    ImageDecoder.Source source = ImageDecoder.createSource(getBaseContext().getContentResolver(), localImage);
                    try {
                        imagem = ImageDecoder.decodeBitmap(source);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                imgAnuncio.setImageBitmap(imagem);
                verificarImagemImovel(true);
            }
        }
    }

    private void salvarImagemAnuncio() {
        if (imovel.getUriImagem() != null) {
            progressBarImg.setVisibility(View.VISIBLE);
            btnMenu.setVisibility(View.GONE);

            StorageReference storageReference = FirebaseHelper.getStorageReference()
                    .child("imagens")
                    .child("anuncios")
                    .child(imovel.getId() + ".jpeg");

            UploadTask uploadTask = null;

            try {
                uploadTask = storageReference.putFile(Uri.parse(imovel.getUriImagem()));
            } catch (Exception e) {
                Toast.makeText(this, "Erro ao tentar fazer upload na imagem.", Toast.LENGTH_SHORT).show();
                progressBarImg.setVisibility(View.GONE);
                btnMenu.setVisibility(View.VISIBLE);
                return;
            }

            uploadTask.addOnSuccessListener(taskSnapshot -> {
                storageReference.getDownloadUrl().addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        String urlImagem = task.getResult().toString();
                        imovel.setImagem(urlImagem);
                        imovel.salvar();
                        //Fechar e enviar imovel
                        finish();
                        Intent intent = new Intent(this, FormAnuncioEtapa2_3_Activity.class);
                        intent.putExtra("imovel", this.imovel);
                        startActivity(intent);
                    } else {
                        Toast.makeText(this, "Erro ao salvar imagem do anúncio.", Toast.LENGTH_SHORT).show();
                        progressBarImg.setVisibility(View.GONE);
                        btnMenu.setVisibility(View.VISIBLE);
                    }
                });
            }).addOnFailureListener(e -> Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show());
        } else if(imovel.getImagem() != null) {
            //Fechar e enviar imovel
            finish();
            Intent intent = new Intent(this, FormAnuncioEtapa2_3_Activity.class);
            intent.putExtra("imovel", this.imovel);
            startActivity(intent);
        }

    }

//    private void pegarImgSharedPreferences(String url) {
//        SharedPreferences.Editor ed = preferences.edit();
//        ed.putString("urlImg", url);
//        ed.apply();
//    }
}