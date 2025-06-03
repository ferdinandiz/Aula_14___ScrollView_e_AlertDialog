package com.fer.aula14_scrollviewealertdialog;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    EditText edJogador1, edJogador2;
    Button btnIniciar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        edJogador1 = findViewById(R.id.edJ1);
        edJogador2 = findViewById(R.id.edJ2);
        btnIniciar = findViewById(R.id.btnIniciar);

        btnIniciar.setOnClickListener(v -> {
            String j1 = edJogador1.getText().toString();
            String j2 = edJogador2.getText().toString();

            if(j1.isEmpty()) j1 = "Jogador 1";
            if(j2.isEmpty()) j2 = "Jogador 2";
            Intent i = new Intent(this, TelaJogo.class);
            i.putExtra("j1",j1);
            i.putExtra("j2",j2);
            startActivity(i);
            finish();
        });

    }
}