package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

public class welcome extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        // Referência para a imagem
        ImageView starImageView = findViewById(R.id.star);

        // Carregar as animações
        Animation slideUpAnimation = AnimationUtils.loadAnimation(this, R.anim.slide_up);
        Animation fadeInAnimation = AnimationUtils.loadAnimation(this, R.anim.fade_in);

        // Aplicar animação à imagem
        starImageView.startAnimation(slideUpAnimation);

        // Adicionar um ouvinte de animação para iniciar a próxima animação após a conclusão da primeira
        slideUpAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                // Executa algo quando a animação de subida começa
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                // Quando a animação de subida termina, inicia a animação de fade in para a tela inteira
                welcome.this.findViewById(android.R.id.content).startAnimation(fadeInAnimation);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
                // Executa algo em cada repetição da animação
            }
        });
    }

    public void aaa(View view) {
        // Cria um Intent para iniciar a blocos
        Intent intent = new Intent(welcome.this, blocos.class);

        // Adiciona a animação de transição
        Bundle bundle = new Bundle();
        bundle.putString("transition", "slide_up"); // Nome da transição
        intent.putExtras(bundle);

        // Inicia a nova activity com animação
        startActivity(intent);
    }
}
