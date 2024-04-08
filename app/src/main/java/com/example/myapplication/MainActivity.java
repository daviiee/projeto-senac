package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;

import java.io.*;

public class MainActivity extends AppCompatActivity {

    private EditText editText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        editText = findViewById(R.id.editText);
        loadNote();
    }

    private void loadNote() {
        // Carregue o texto da nota do armazenamento, se existir
        try {
            FileInputStream fis = openFileInput("note.txt");
            InputStreamReader isr = new InputStreamReader(fis);
            BufferedReader br = new BufferedReader(isr);
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) {
                sb.append(line).append("\n");
            }
            br.close();
            fis.close();
            // Defina o texto carregado no EditText
            editText.setText(sb.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void saveNote(View view) {
        // Salve o texto da nota no armazenamento
        String textToSave = editText.getText().toString().trim();
        if (!textToSave.isEmpty()) {
            try {
                FileOutputStream fos = openFileOutput("note.txt", MODE_PRIVATE);
                fos.write(textToSave.getBytes());
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        // Retorne para a atividade "blocos"
        Intent intent = new Intent(this, blocos.class);
        startActivity(intent);
    }
}
