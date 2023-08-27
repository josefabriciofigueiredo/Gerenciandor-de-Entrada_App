package com.example.adm_escola;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.adm_escola.ClassBean.DadosPessoas;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;
import java.util.Random;

public class MyCanvasView extends View {

    private Paint paint;
    private float lastX = 0;
    private float lastY = 0;
    private float posX = 0;
    private float posY = 0;

    private Handler handler = new Handler();
    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            invalidate();
            handler.postDelayed(this, 1000);
        }
    };

    public MyCanvasView(Context context) {
        super(context);
        init();
    }

    public MyCanvasView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.FILL);

        handler.postDelayed(runnable, 1000);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        HashMap<String, Integer> info = new HashMap<>();

        if (Objects.equals(DadosPessoas.grupo, "Escola") && Objects.equals(DadosPessoas.tipo, "qtdPessoas")) {
            for (HashMap<String, String> dadosPessoa : DadosPessoas.dados) {
                String escola = dadosPessoa.get("escola");
                if (info.containsKey(escola)) {
                    info.put(escola, info.get(escola) + 1);
                } else {
                    info.put(escola, 1);
                }
            }

            atualizarGrafico(canvas, info);
        } else if (Objects.equals(DadosPessoas.grupo, "Escola") && Objects.equals(DadosPessoas.tipo, "idadeMedia")) {
            HashMap<String, Integer> qtdPessoas = new HashMap<>();
            HashMap<String, Integer> somaIdades = new HashMap<>();

            for (HashMap<String, String> dadosPessoa : DadosPessoas.dados) {
                String escola = dadosPessoa.get("escola");
                int idade = Integer.valueOf(dadosPessoa.get("idade"));

                if (qtdPessoas.containsKey(escola)) {
                    qtdPessoas.put(escola, qtdPessoas.get(escola) + 1);
                    somaIdades.put(escola, somaIdades.get(escola) + idade);
                } else {
                    qtdPessoas.put(escola, 1);
                    somaIdades.put(escola, idade);
                }
            }

            for (String escola : qtdPessoas.keySet()) {
                int qtd = qtdPessoas.get(escola);
                int soma = somaIdades.get(escola);
                double media = (double) soma / qtd;
                info.put(escola, (int) media);
            }

            atualizarGrafico(canvas, info);
        } else if (Objects.equals(DadosPessoas.grupo, "Idade") && Objects.equals(DadosPessoas.tipo, "qtdPessoas")) {
            for (HashMap<String, String> dadosPessoa : DadosPessoas.dados) {
                String idade = dadosPessoa.get("idade") + " anos";
                if (info.containsKey(idade)) {
                    info.put(idade, info.get(idade) + 1);
                } else {
                    info.put(idade, 1);
                }
            }

            atualizarGrafico(canvas, info);
        } else if (Objects.equals(DadosPessoas.grupo, "Inscrições por Hora") && Objects.equals(DadosPessoas.tipo, "qtdPessoas")) {
            for (HashMap<String, String> dadosPessoa : DadosPessoas.dados) {
                String horaCheia = dadosPessoa.get("hora").split(":")[0];
                String hora = (Integer.parseInt(horaCheia) < 10 ? "0" + horaCheia : horaCheia) + ":00";

                if (info.containsKey(hora)) {
                    info.put(hora, info.get(hora) + 1);
                } else {
                    info.put(hora, 1);
                }
            }

            atualizarGrafico(canvas, info);
        } else if (Objects.equals(DadosPessoas.grupo, "Inscrições por Hora") && Objects.equals(DadosPessoas.tipo, "idadeMedia")) {
            HashMap<String, Integer> qtdPessoas = new HashMap<>();
            HashMap<String, Integer> somaIdades = new HashMap<>();

            for (HashMap<String, String> dadosPessoa : DadosPessoas.dados) {
                String horaCheia = dadosPessoa.get("hora").split(":")[0];
                String hora = (Integer.parseInt(horaCheia) < 10 ? "0" + horaCheia : horaCheia) + ":00";

                int idade = Integer.valueOf(dadosPessoa.get("idade"));

                if (qtdPessoas.containsKey(hora)) {
                    qtdPessoas.put(hora, qtdPessoas.get(hora) + 1);
                    somaIdades.put(hora, somaIdades.get(hora) + idade);
                } else {
                    qtdPessoas.put(hora, 1);
                    somaIdades.put(hora, idade);
                }
            }

            for (String hora : qtdPessoas.keySet()) {
                int qtd = qtdPessoas.get(hora);
                int soma = somaIdades.get(hora);
                double media = (double) soma / qtd;
                info.put(hora, (int) media);
            }

            atualizarGrafico(canvas, info);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        super.onTouchEvent(event);

        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            if (event.getY() <= dpParaPexle(10f) + dpParaPexle(350f)) {
                this.lastX = event.getX();
                this.lastY = -1;
            } else {
                this.lastY = event.getY();
                this.lastX = -1;
            }
        } else if (event.getAction() == MotionEvent.ACTION_MOVE) {
            if (this.lastX != -1) {
                float currentX = event.getX();
                float deltaX = this.lastX - currentX;

                this.posX += (deltaX / 10);
            } else if (this.lastY != -1) {
                float currentX = event.getY();
                float deltaX = this.lastY - currentX;

                this.posY += (deltaX / 10);
            }

            invalidate();
        }

        return true;
    }

    public void atualizarGrafico(Canvas canvas, @NonNull HashMap<String, Integer> info) {
        int top = 50;

        // Difinir o valor máximo da lista
        int valorMAX = 0;
        for (Integer valor : info.values()) {
            if (valor > valorMAX) {
                valorMAX = valor;
            }
        }

        // Definir o valor pré estabelecido
        int[] numeros = {10, 20, 30, 40, 50, 60, 70, 80, 90, 100, 200, 300, 400, 500, 600, 700, 800, 900, 1000, 1100, 1200, 1300, 1400, 1500, 1600, 1700, 1800, 1900, 2000};
        int numMAX = 0;
        for (int num : numeros) {
            if (num > valorMAX) {
                numMAX = num;
                break;
            }
        }

        // Legenda
        int contBarra = 0;

        for (String chave : info.keySet()) {
            paint.setColor(DadosPessoas.cores[contBarra]);
            canvas.drawRect(contBarra, dpParaPexle(420f) + dpParaPexle(contBarra*30f) - posY, 50+contBarra, dpParaPexle(430f) + dpParaPexle(contBarra*30f) - posY, paint);

            paint.setColor(Color.BLACK);
            paint.setTextSize(30);
            canvas.drawText(chave, 70+contBarra, dpParaPexle(430f) + dpParaPexle(contBarra*30f) - posY, paint);

            contBarra++;
        }

        // Tampar legenda
        paint.setColor(Color.WHITE);
        canvas.drawRect(0, 0, dpParaPexle(350f), dpParaPexle(10f) + dpParaPexle(400f), paint);

        // Texto "Legenda"
        paint.setColor(Color.BLACK);
        paint.setTextSize(40);
        canvas.drawText("Legenda:", 10, dpParaPexle(10f) + dpParaPexle(390f), paint);

        // Barra
        paint.setTextSize(25);
        contBarra = 0;

        for (String chave : info.keySet()) {
            paint.setColor(DadosPessoas.cores[contBarra]);
            canvas.drawRect((dpParaPexle(100f)+dpParaPexle(contBarra*60f)) - posX, dpParaPexle(340f) - (info.get(chave)*100/numMAX)*dpParaPexle(3f), dpParaPexle(50f)+dpParaPexle(contBarra*60f) - posX, dpParaPexle(342f), paint);

            contBarra++;
        }

        paint.setColor(0x99FFFFFF);
        canvas.drawRect(0, top, 80, top + 600, paint);

        // Linha
        paint.setColor(Color.BLACK);
        paint.setStrokeWidth(6);
        canvas.drawLine(dpParaPexle(45f), dpParaPexle(10f), dpParaPexle(45f), dpParaPexle(370f), paint);
        canvas.drawLine(0, dpParaPexle(10f) + dpParaPexle(332f), dpParaPexle(350f), dpParaPexle(10f) + dpParaPexle(332f), paint);

        // Valores das linha
        for (int cont=1;cont<=11;cont++) {
            paint.setColor(Color.BLACK);
            canvas.drawText(String.valueOf((numMAX/10)*(11-cont)), 5, dpParaPexle(10f) + dpParaPexle(cont*30f), paint);

            paint.setColor(0xFF332F2E);
            paint.setStrokeWidth(2);
            canvas.drawLine(0, dpParaPexle(10f) + dpParaPexle(cont*30f), dpParaPexle(350f), dpParaPexle(10f) + dpParaPexle(cont*30f), paint);
        }
    }

    public float dpParaPexle(float dp) {
        float dpSize = dp;
        float scale = getResources().getDisplayMetrics().density;
        float pxSize = dpSize * scale;

        return pxSize;
    }
}