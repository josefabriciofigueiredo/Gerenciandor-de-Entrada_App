package com.example.adm_escola;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.adm_escola.ClassBean.DadosPessoas;
import com.example.adm_escola.ListView.NomesAdapter;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

public class ListaActivity extends AppCompatActivity {

    ListView listView_nome;
    Button btn_voltar;
    TextView txt_gerarPlanilha;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista);

        iniciarComponentes();

        btn_voltar.setOnClickListener(v -> finish());

        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(() -> {
                    int currentPosition = listView_nome.getFirstVisiblePosition();
                    View currentView = listView_nome.getChildAt(0);
                    int currentTop = (currentView == null) ? 0 : currentView.getTop();
                    listView_nome.setAdapter(new NomesAdapter(ListaActivity.this, DadosPessoas.dados));
                    listView_nome.setSelectionFromTop(currentPosition, currentTop);
                });
            }
        }, 0, 10000);

        // Gerar Planilha
        txt_gerarPlanilha.setOnClickListener(view1 -> {
            Intent intent = new Intent(Intent.ACTION_CREATE_DOCUMENT);
            intent.setType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            intent.putExtra(Intent.EXTRA_TITLE, "planilha.xlsx");
            startActivityForResult(intent, 1);
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && requestCode == 1) {
            Uri uri = data.getData();

            // Exibe a ProgressBar
            txt_gerarPlanilha.setText("Salvando...");

            new Thread(() -> {
                try {
                    OutputStream os = getContentResolver().openOutputStream(uri);

                    XSSFWorkbook workbook = new XSSFWorkbook();
                    XSSFSheet sheet = workbook.createSheet("inscricoes");

                    // Título
                    Row row = sheet.createRow(0);

                    row.createCell(0).setCellValue("Nome");
                    row.createCell(1).setCellValue("Idade");
                    row.createCell(2).setCellValue("Escola");
                    row.createCell(3).setCellValue("Telefone");
                    row.createCell(4).setCellValue("Email");

                    int x = 1;
                    for (HashMap<String, String> info : DadosPessoas.dados) {
                        row = sheet.createRow(x);

                        row.createCell(0).setCellValue(info.get("nome"));
                        row.createCell(1).setCellValue(info.get("idade"));
                        row.createCell(2).setCellValue(info.get("escola"));
                        row.createCell(3).setCellValue(info.get("telefone"));
                        row.createCell(4).setCellValue(info.get("email"));
                        x++;
                    }

                    workbook.write(os);
                    os.close();

                    runOnUiThread(() -> {
                        txt_gerarPlanilha.setText("Gerar Planilha");
                        Toast.makeText(ListaActivity.this, "Arquivo salvo com sucesso", Toast.LENGTH_SHORT).show();
                    });
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }).start();
        }
    }

    public void iniciarComponentes() {
        listView_nome = findViewById(R.id.listView_nome);
        btn_voltar = findViewById(R.id.btn_voltar);
        txt_gerarPlanilha = findViewById(R.id.txt_gerarPlanilha);
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(0, R.anim.mover_esquerda);
    }
}