package com.example.adm_escola;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.adm_escola.ClassBean.DadosPessoas;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

public class SplashActivity extends AppCompatActivity {

    FirebaseAuth auth = FirebaseAuth.getInstance();
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        ImageView imageViewGif = findViewById(R.id.img_gifCarregar);
        Glide.with(this).asGif().load(R.drawable.carregar).into(imageViewGif);

        int[] cores = new int[2000];
        for (int i = 0; i < 2000; i++) {
            int r = (int) (Math.random() * 256);
            int g = (int) (Math.random() * 256);
            int b = (int) (Math.random() * 256);
            int alpha = 153;

            int cor = (alpha << 24) | (r << 16) | (g << 8) | b;
            cores[i] = cor;
        }
        DadosPessoas.cores = cores;

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if (user != null) {
            db.collection("Contas").document(auth.getCurrentUser().getUid()).get()
                    .addOnSuccessListener(documentSnapshot -> {
                        if (documentSnapshot.exists()) {
                            DadosPessoas.nome = documentSnapshot.getString("nome");
                            DadosPessoas.foto = documentSnapshot.getString("foto");

                            Intent menu = new Intent(SplashActivity.this, Menu.class);
                            startActivity(menu);
                            overridePendingTransition(R.anim.mover_direita, R.anim.mover_direita2);
                            finish();
                        }
                    });
        } else {
            Intent menu = new Intent(SplashActivity.this, MainActivity.class);
            startActivity(menu);
            finish();
        }
    }
}