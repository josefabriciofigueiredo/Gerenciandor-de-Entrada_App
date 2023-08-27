package com.example.adm_escola;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.adm_escola.ClassBean.DadosPessoas;

public class GraficoActivity extends AppCompatActivity {
    Button bt_voltarGrafico;
    LinearLayout btn_graficoIdade, btn_graficoQtdPessoas;
    TextView txt_titulo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grafico);

        iniciarComponentes();

        bt_voltarGrafico.setOnClickListener(v -> finish());
        txt_titulo.setText("Gráfico: " + DadosPessoas.grupo);

        if (DadosPessoas.grupo.equals("Idade")) {
            btn_graficoIdade.setVisibility(View.INVISIBLE);
        } else {
            btn_graficoIdade.setVisibility(View.VISIBLE);
        }
        btn_graficoIdade.setOnClickListener(v -> {
            btn_graficoIdade.setBackgroundResource(R.drawable.layout_btn_ativo);
            btn_graficoQtdPessoas.setBackgroundResource(R.drawable.layout_btn_desativado);

            DadosPessoas.tipo = "idadeMedia";
        });

        btn_graficoQtdPessoas.setOnClickListener(v -> {
            btn_graficoQtdPessoas.setBackgroundResource(R.drawable.layout_btn_ativo);
            btn_graficoIdade.setBackgroundResource(R.drawable.layout_btn_desativado);

            DadosPessoas.tipo = "qtdPessoas";
        });
    }

    public void iniciarComponentes() {
        bt_voltarGrafico = findViewById(R.id.bt_voltarGrafico);
        txt_titulo = findViewById(R.id.txt_titulo);
        btn_graficoIdade = findViewById(R.id.btn_graficoIdade);
        btn_graficoQtdPessoas = findViewById(R.id.btn_graficoQtdPessoas);
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(0, R.anim.mover_esquerda);
    }
}