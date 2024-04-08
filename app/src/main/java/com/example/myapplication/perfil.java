package com.example.myapplication;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

public class perfil extends AppCompatActivity {

    private EditText editTextNome;
    private EditText editTextSenha;
    private ImageView ftperfil;

    private static final int PICK_IMAGE_REQUEST = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil);

        editTextNome = findViewById(R.id.editTextNome);
        editTextSenha = findViewById(R.id.editTextSenha);
        ftperfil = findViewById(R.id.ftperfil);

        // Adiciona um KeyListener ao editTextNome
        editTextNome.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                // Verifica se a tecla "Enter" foi pressionada e se o evento é de soltar a tecla
                if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_UP) {
                    // Muda o foco para o próximo EditText (editTextSenha)
                    editTextSenha.requestFocus();
                    return true; // Indica que o evento foi tratado
                }
                return false; // Indica que o evento não foi tratado
            }
        });

        // Adiciona um KeyListener ao editTextSenha para evitar que "Enter" pule uma linha
        editTextSenha.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                // Verifica se a tecla "Enter" foi pressionada e se o evento é de soltar a tecla
                return keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_UP;
            }
        });
    }

    public void continuarr(View view) {
        String nome = editTextNome.getText().toString().trim();
        String senha = editTextSenha.getText().toString().trim();

        if (!nome.isEmpty() && !senha.isEmpty()) {
            Intent in = new Intent(perfil.this, welcome.class);
            startActivity(in);
        } else {
            // Campos em branco, mostra um AlertDialog
            showAlertDialog();
        }
    }

    public void selectImageFromGallery(View view) {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri imageUri = data.getData();

            ftperfil.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
            ftperfil.setImageURI(imageUri);

            // Definir tamanho específico da imagem
            ftperfil.getLayoutParams().width = (int) (getResources().getDisplayMetrics().widthPixels * 0.497);
            ftperfil.getLayoutParams().height = (int) (getResources().getDisplayMetrics().heightPixels * 0.369);
        }
    }


    private void showAlertDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Campos Vazios");
        builder.setMessage("Preencha todos os campos para continuar.");

        // Adiciona um botão "OK" ao AlertDialog
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Este método é chamado quando o botão "OK" é clicado
                dialog.dismiss(); // Fecha o AlertDialog
            }
        });

        // Cria e exibe o AlertDialog
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
}
