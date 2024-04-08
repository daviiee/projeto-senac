package com.example.myapplication;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class blocos extends AppCompatActivity {

    private ArrayList<String> noteList; // Lista para armazenar todas as notas
    private ArrayList<View> selectedNotes; // Lista para armazenar miniaturas de notas selecionadas

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blocos);

        // Inicializar a lista de notas
        noteList = new ArrayList<>();
        selectedNotes = new ArrayList<>();

        // Exibir as notas na tela
        displayNotes();
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Atualizar as notas ao retomar a atividade
        displayNotes();
    }

    private void displayNotes() {
        LinearLayout notesLayout = findViewById(R.id.notesLayout);

        // Armazenar as miniaturas existentes para evitar duplicatas
        ArrayList<String> existingNoteThumbnails = new ArrayList<>();
        for (View noteView : selectedNotes) {
            TextView textView = noteView.findViewById(R.id.noteTextView);
            existingNoteThumbnails.add(textView.getText().toString());
        }

        try {
            FileInputStream fis = openFileInput("note.txt");
            InputStreamReader isr = new InputStreamReader(fis);
            BufferedReader br = new BufferedReader(isr);
            String line;
            while ((line = br.readLine()) != null) {
                // Adicionar a nota à lista de notas
                noteList.add(line);
                // Verificar se a miniatura da nota já existe na tela
                if (!existingNoteThumbnails.contains(line)) {
                    // Criar uma nova miniatura apenas se ainda não existir
                    addNoteView(notesLayout, line);
                }
            }
            br.close();
            fis.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    private void addNoteView(LinearLayout parentLayout, String noteText) {
        // Verificar se já existe uma miniatura com a nota
        boolean noteExists = false;
        for (View noteView : selectedNotes) {
            TextView textView = noteView.findViewById(R.id.noteTextView);
            if (textView.getText().toString().equals(noteText)) {
                // Se a nota já existe, atualize o texto e marque como selecionada
                noteExists = true;
                textView.setText(noteText.substring(0, Math.min(noteText.length(), 20)));
                toggleNoteSelection(noteView);
                break;
            }
        }

        // Se a nota não existe, crie uma nova miniatura
        if (!noteExists) {
            View noteView = LayoutInflater.from(this).inflate(R.layout.note_thumbnail, parentLayout, false);

            // Configurar o texto da miniatura da nota
            TextView textView = noteView.findViewById(R.id.noteTextView);
            textView.setText(noteText.substring(0, Math.min(noteText.length(), 20))); // Exibir apenas os primeiros 20 caracteres

            // Adicionar a miniatura da nota ao layout pai
            parentLayout.addView(noteView);

            // Definir o clique na miniatura da nota para selecioná-la ou desselecioná-la
            noteView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    toggleNoteSelection(noteView); // Alternar a seleção da miniatura da nota ao clicar nela
                }
            });

            // Definir o arraste da miniatura da nota até o botão de lixeira
            noteView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    // Verificar se a miniatura da nota está selecionada
                    if (selectedNotes.contains(noteView)) {
                        // Exibir um diálogo de confirmação antes de excluir a nota selecionada
                        showDeleteConfirmationDialog(noteView);
                    }
                    return true;
                }
            });
        }
    }



    private void toggleNoteSelection(View noteView) {
        ImageView selectionIndicator = noteView.findViewById(R.id.selectionIndicator);

        if (selectedNotes.contains(noteView)) {
            // Se a nota já estiver selecionada, desmarque-a
            selectedNotes.remove(noteView);
            selectionIndicator.setVisibility(View.GONE); // Tornar o marcador de seleção invisível
        } else {
            // Se a nota não estiver selecionada, marque-a
            selectedNotes.add(noteView);
            selectionIndicator.setVisibility(View.VISIBLE); // Tornar o marcador de seleção visível
        }
    }

    public void excluirr(View view) {
        // Verifica se há notas selecionadas para exclusão
        if (selectedNotes.isEmpty()) {
            // Se não houver notas selecionadas, exibe uma mensagem de aviso
            new AlertDialog.Builder(this)
                    .setMessage("Nenhuma nota selecionada para exclusão.")
                    .setPositiveButton("OK", null)
                    .show();
        } else {
            // Se houver notas selecionadas, exibe um alerta de confirmação
            new AlertDialog.Builder(this)
                    .setMessage("Tem certeza de que deseja excluir as notas selecionadas?")
                    .setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // Remove as notas selecionadas da lista de notas
                            for (View noteView : selectedNotes) {
                                noteList.remove(((TextView) noteView.findViewById(R.id.noteTextView)).getText().toString());
                                LinearLayout parentLayout = (LinearLayout) noteView.getParent();
                                parentLayout.removeView(noteView);
                            }
                            // Limpa a lista de miniaturas de notas selecionadas
                            selectedNotes.clear();
                        }
                    })
                    .setNegativeButton("Não", null)
                    .show();
        }
    }

    private void showDeleteConfirmationDialog(View noteView) {
        new AlertDialog.Builder(this)
                .setMessage("Tem certeza de que deseja excluir esta nota?")
                .setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        deleteSelectedNote(noteView); // Excluir a nota selecionada
                    }
                })
                .setNegativeButton("Não", null)
                .show();
    }

    private void deleteSelectedNote(View noteView) {
        noteList.remove(((TextView) noteView.findViewById(R.id.noteTextView)).getText().toString());

        LinearLayout parentLayout = (LinearLayout) noteView.getParent();
        parentLayout.removeView(noteView);

        selectedNotes.remove(noteView);
    }

    public void addnotas(View view) {
        // Iniciar uma nova instância da atividade MainActivity sem texto prévio no EditText
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); // Limpar todas as instâncias anteriores da MainActivity
        startActivity(intent);
    }
}
