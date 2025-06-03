package com.fer.aula14_scrollviewealertdialog;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class TelaJogo extends AppCompatActivity {
    TextView txJ1, txJ2;
    Button btnVoltar;
    MediaPlayer goku, click, alert;
    ImageView[] imageViews;
    Integer[] imagensVet = {101, 102, 103, 104, 105, 106, 107, 108,
                            201, 202, 203, 204, 205, 206, 207, 208};

    Map<Integer, Integer> imagemMap;
    int primeiro, segundo, clique1, clique2, imgNum = 1, turno = 1;
    int ptsJ1 = 0, ptsJ2 = 0;
    String j1, j2;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tela_jogo);

        txJ1 = findViewById(R.id.txtJ1);
        txJ2 = findViewById(R.id.txtJ2);
        btnVoltar = findViewById(R.id.btnVoltar);
        inicializarImageViews();
        inicializarImageMap();

        goku = MediaPlayer.create(this, R.raw.goku);
        click = MediaPlayer.create(this, R.raw.pen_click);
        alert = MediaPlayer.create(this, R.raw.sms_alert);

        Intent dados = getIntent();
        j1 = dados.getStringExtra("j1");
        j2 = dados.getStringExtra("j2");

        txJ1.setText(j1+": "+ptsJ1);
        txJ1.setTextColor(Color.BLACK);
        txJ2.setText(j2+": "+ptsJ2);
        txJ2.setTextColor(Color.GRAY);

        btnVoltar.setOnClickListener(v -> {
            startActivity(new Intent(this, MainActivity.class));
            finish();
        });

        Collections.shuffle(Arrays.asList(imagensVet));
    }

    private void inicializarImageViews() {
        imageViews = new ImageView[16];
        int[] ids = {R.id.base00, R.id.base01, R.id.base02, R.id.base03,
                     R.id.base10, R.id.base11, R.id.base12, R.id.base13,
                     R.id.base20, R.id.base21, R.id.base22, R.id.base23,
                     R.id.base30, R.id.base31, R.id.base32, R.id.base33};
        for (int i=0;i<ids.length;i++){
            imageViews[i] = findViewById(ids[i]);
            imageViews[i].setTag(String.valueOf(i));
            imageViews[i].setOnClickListener(this::processarClique);
        }
    }

    private void inicializarImageMap() {
        imagemMap = new HashMap<>();
        int[] imagens = {
                R.drawable.dbz101, R.drawable.dbz102, R.drawable.dbz103, R.drawable.dbz104,
                R.drawable.dbz105, R.drawable.dbz106, R.drawable.dbz107, R.drawable.dbz108
        };

        for(int i=0 ;i<imagens.length; i++){
            int chaveNormal = 101+i;
            int chaveDuplicada = 201+i;
            imagemMap.put(chaveNormal, imagens[i]);
            imagemMap.put(chaveDuplicada, imagens[i]);
        }
    }

    private void processarClique(View view) {
        int aux = Integer.parseInt((String)view.getTag());
        click.start();

        int imgId = imagensVet[aux];
        Integer resId = imagemMap.get(imgId);
        if(resId != null) imageViews[aux].setImageResource(resId);
        if(imgNum == 1) {
            primeiro = imgId > 200 ? imgId - 100: imgId;
            clique1 = aux;
            imageViews[aux].setEnabled(false);
            imgNum = 2;
        } else {
            segundo = imgId > 200 ? imgId - 100: imgId;
            clique2 = aux;
            imgNum = 1;
            setImageViewEnable(false);
            new Handler().postDelayed(this::verificar,1000);
        }
    }

    private void verificar() {
        if(primeiro == segundo){
            imageViews[clique1].setVisibility(View.INVISIBLE);
            imageViews[clique2].setVisibility(View.INVISIBLE);

            if(turno == 1) {
                ptsJ1++;
                txJ1.setText(j1+": "+ptsJ1);
            } else {
                ptsJ2++;
                txJ2.setText(j2+": "+ptsJ2);
            }
            alert.start();
        } else {
            for (ImageView iv : imageViews){
                if(iv.getVisibility() == View.VISIBLE) {
                    iv.setImageResource(R.drawable.base00);
                }
            }
        }
        turno = (turno == 1) ? 2 : 1;
        txJ1.setTextColor(turno == 1 ? Color.BLACK : Color.GRAY);
        txJ2.setTextColor(turno == 2 ? Color.BLACK : Color.GRAY);
        setImageViewEnable(true);
        checarFim();
    }

    private void setImageViewEnable(boolean enable) {
        for(ImageView iv : imageViews){
            if(iv.getVisibility() == View.VISIBLE)
                iv.setEnabled(enable);
        }
    }

    private void checarFim() {
        for(ImageView iv : imageViews){
            if(iv.getVisibility() == View.VISIBLE)
                return;
        }
        new AlertDialog.Builder(this)
                .setMessage("Fim de Jogo!\n\n"
                        + j1+": "+ptsJ1+"\n"
                        + j2+": "+ptsJ2)
                .setCancelable(false)
                .setNegativeButton("Sair",((dialog, which) -> {
                    startActivity(new Intent(this, MainActivity.class));
                    finish();
                }))
                .setPositiveButton("Novo Jogo", (dialog, which) -> {
                    Intent i = new Intent(this, TelaJogo.class);
                    i.putExtra("j1",j1);
                    i.putExtra("j2",j2);
                    startActivity(i);
                    finish();
                })
                .show();
        goku.start();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        goku.release();
        click.release();
        alert.release();
    }
}
