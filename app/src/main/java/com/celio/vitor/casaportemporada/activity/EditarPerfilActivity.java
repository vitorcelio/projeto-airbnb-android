package com.celio.vitor.casaportemporada.activity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.Manifest;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.ImageDecoder;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.celio.vitor.casaportemporada.R;
import com.celio.vitor.casaportemporada.helper.FirebaseHelper;
import com.celio.vitor.casaportemporada.model.Usuario;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.normal.TedPermission;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.Date;
import java.util.List;

public class EditarPerfilActivity extends AppCompatActivity {

    private static final int REQUEST_GALERIA = 100;
    private EditText editSobre;
    private EditText editLocalizacao;
    private EditText editTrabalho;
    private TextView textCancelar;
    private Button btnSalvar;
    private ImageView imgPerfil;
    private ImageButton btnVoltar;
    private TextView btnEditar;
    private TextView textSobre;
    private TextView textLocalizacao;
    private TextView textTrabalho;
    private ConstraintLayout layoutImage;
    private LinearLayout layoutSobre;
    private LinearLayout layoutCadastroSobre;
    private Usuario usuario;
    private String caminhoDaImagem;
    private Bitmap imagem;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_perfil);

        iniciaComponentes();
        usuario = (Usuario) getIntent().getSerializableExtra("usuario");
        if (usuario != null) {
            carregarUsuario(usuario);
        }

        configCliques();
    }

    private void carregarUsuario(Usuario usuario) {
        editSobre.setText(usuario.getDescricao() != null ? usuario.getDescricao() : "");
        editLocalizacao.setText(usuario.getLocalizacao() != null ? usuario.getLocalizacao() : "");
        editTrabalho.setText(usuario.getTrabalho() != null ? usuario.getTrabalho() : "");

        textSobre.setText(usuario.getDescricao() != null && !usuario.getDescricao().trim().isEmpty() ? usuario.getDescricao() : "Conte algo sobre você");
        textLocalizacao.setText(usuario.getLocalizacao() != null && !usuario.getLocalizacao().trim().isEmpty() ? usuario.getLocalizacao() : "Insira sua localização");
        textTrabalho.setText(usuario.getTrabalho() != null && !usuario.getTrabalho().trim().isEmpty() ? usuario.getTrabalho() : "Insira sua profissão");

        if (usuario.getImagem() != null) {
            Picasso.get().load(usuario.getImagem()).into(imgPerfil);
        }
    }

    private void iniciaComponentes() {
        layoutImage = findViewById(R.id.layout_img);
        layoutSobre = findViewById(R.id.ll_sobre);
        layoutCadastroSobre = findViewById(R.id.ll_cadastro_sobre);
        btnVoltar = findViewById(R.id.btn_voltar);
        btnEditar = findViewById(R.id.botao_pagina);
        imgPerfil = findViewById(R.id.img_perfil);
        textSobre = findViewById(R.id.text_sobre);
        textLocalizacao = findViewById(R.id.text_localizacao);
        textTrabalho = findViewById(R.id.text_trabalho);
        editSobre = findViewById(R.id.edit_sobre);
        editLocalizacao = findViewById(R.id.edit_localizacao);
        editTrabalho = findViewById(R.id.edit_trabalho);
        textCancelar = findViewById(R.id.text_cancelar);
        btnSalvar = findViewById(R.id.btn_salvar);
        progressBar = findViewById(R.id.progress_bar);
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

    private void configCliques() {
        btnVoltar.setOnClickListener(v -> {
            finish();
        });

        btnEditar.setOnClickListener(v -> {
            if (btnEditar.getText().toString().equals("Salvar")) {
                salvarImagemPerfil();
            } else {
                layoutCadastroSobre.setVisibility(View.VISIBLE);
                layoutSobre.setVisibility(View.GONE);
            }
        });

        textCancelar.setOnClickListener(v -> {
            layoutCadastroSobre.setVisibility(View.GONE);
            layoutSobre.setVisibility(View.VISIBLE);
        });

        btnSalvar.setOnClickListener(v -> {
            if (!editSobre.getText().toString().equals(usuario.getDescricao()) ||
                    !editLocalizacao.getText().toString().equals(usuario.getLocalizacao()) ||
                    !editTrabalho.getText().toString().equals(usuario.getTrabalho())) {
                usuario.setDescricao(editSobre.getText().toString());
                usuario.setLocalizacao(editLocalizacao.getText().toString());
                usuario.setTrabalho(editTrabalho.getText().toString());
                usuario.salvar();
                carregarUsuario(usuario);
                layoutCadastroSobre.setVisibility(View.GONE);
                layoutSobre.setVisibility(View.VISIBLE);
            } else {
                Toast.makeText(this, "Nenhuma informação foi alterada.", Toast.LENGTH_SHORT).show();
            }
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
                Toast.makeText(EditarPerfilActivity.this, "Permissão negada", Toast.LENGTH_SHORT).show();
            }
        };

        showDialogPermissaoGaleria(permissionListener, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE});
    }

    public void abrirGaleria() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, REQUEST_GALERIA);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_GALERIA) {
                Uri uri = data.getData();
                caminhoDaImagem = uri.toString();

                if (Build.VERSION.SDK_INT < 28) {
                    try {
                        imagem = MediaStore.Images.Media.getBitmap(getBaseContext().getContentResolver(), uri);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    ImageDecoder.Source source = ImageDecoder.createSource(getBaseContext().getContentResolver(), uri);

                    try {
                        imagem = ImageDecoder.decodeBitmap(source);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                imgPerfil.setImageBitmap(imagem);
                btnEditar.setText(R.string.salvar);
            }
        }
    }

    private void salvarImagemPerfil() {
        progressBar.setVisibility(View.VISIBLE);

        StorageReference reference = FirebaseHelper.getStorageReference()
                .child("imagens")
                .child("usuarios")
                .child(usuario.getId() + ".jpeg");

        UploadTask uploadTask = reference.putFile(Uri.parse(caminhoDaImagem));
        uploadTask.addOnSuccessListener(taskSnapshot -> {
            reference.getDownloadUrl().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    String urlImagem = task.getResult().toString();
                    usuario.setImagem(urlImagem);
                    usuario.salvar();
                    btnEditar.setText(R.string.editar);
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(this, "Imagem salva com sucesso.", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "Erro ao salvar imagem.", Toast.LENGTH_SHORT).show();
                }
            });
        }).addOnFailureListener(e -> Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show());
    }
}