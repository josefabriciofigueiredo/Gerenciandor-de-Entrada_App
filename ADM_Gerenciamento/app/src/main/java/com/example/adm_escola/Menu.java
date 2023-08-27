package com.example.adm_escola;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.adm_escola.ClassBean.DadosPessoas;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class Menu extends AppCompatActivity {
    Button btn_logout, btn_config;
    TextView txt_nomeConta;
    LinearLayout linerLayout_gerarLista, linerLayout_gerarIdade, linerLayout_gerarInscricao, linerLayout_gerarEscola, linearLayout_Conteudo;
    ImageView image_perfil, img_perfil_dialog;
    ProgressBar progressBar_Carregar;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    Bitmap imgPerfil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        iniciarComponentes();

        ArrayList<HashMap<String, String>> dados = new ArrayList<>();

        db.collection("Inscricao")
                .addSnapshotListener((value, error) -> {
                    if (value != null) {
                        value.getDocumentChanges().forEach(result -> {
                            Timestamp timestamp = result.getDocument().getTimestamp("timestamp");
                            Date date = timestamp.toDate();
                            int hours = date.getHours();
                            int minutes = date.getMinutes();

                            if (timestamp != null) {
                                HashMap<String, String> item = new HashMap<>();
                                item.put("nome", result.getDocument().getString("nome"));
                                item.put("idade", result.getDocument().getString("idade"));
                                item.put("escola", result.getDocument().getString("escola").equals("") ? "Não Identificado" : result.getDocument().getString("escola"));
                                item.put("email", result.getDocument().getString("email"));
                                item.put("telefone", result.getDocument().getString("telefone"));
                                item.put("hora", hours + ":" + minutes);
                                dados.add(item);
                            }
                        });

                        DadosPessoas.dados = dados;

                        progressBar_Carregar.setVisibility(View.GONE);
                        linearLayout_Conteudo.setVisibility(View.VISIBLE);
                    }
                });

        btn_logout.setOnClickListener(v -> {
            // Dialog
            Dialog dialog = new Dialog(this);
            dialog.setContentView(R.layout.layout_dialog_confirmar);
            dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            dialog.setCancelable(true);

            Button btn_cancelar_dialog = dialog.findViewById(R.id.btn_cancelar_dialog);
            Button btn_confirmar_dialog = dialog.findViewById(R.id.btn_confirmar_dialog);

            btn_cancelar_dialog.setOnClickListener(v2 -> dialog.dismiss());
            btn_confirmar_dialog.setOnClickListener(v2 -> {
                FirebaseAuth.getInstance().signOut();

                Intent menu = new Intent(Menu.this, MainActivity.class);
                startActivity(menu);
                finish();
            });

            dialog.show();
        });

        btn_config.setOnClickListener(v -> {
            // Dialog
            Dialog dialog = new Dialog(this);
            dialog.setContentView(R.layout.layout_dialog_config);
            dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            dialog.setCancelable(true);

            Button btn_salvar_dialog = dialog.findViewById(R.id.btn_salvar_dialog);
            Button btn_fechar_dialog = dialog.findViewById(R.id.btn_fechar_dialog);
            EditText ed_nome_dialog = dialog.findViewById(R.id.ed_nome_dialog);
            img_perfil_dialog = dialog.findViewById(R.id.img_perfil_dialog);
            TextView txt_mudar_dialog = dialog.findViewById(R.id.txt_mudar_dialog);

            btn_fechar_dialog.setOnClickListener(v2 -> dialog.dismiss());
            ed_nome_dialog.setText(DadosPessoas.nome);
            img_perfil_dialog.setImageBitmap(imgPerfil);

            txt_mudar_dialog.setOnClickListener(v2 -> {
                Intent galeria = new Intent(Intent.ACTION_PICK);
                galeria.setType("image/*");
                startActivityForResult(galeria, 1);
            });

            btn_salvar_dialog.setOnClickListener(v2 -> {
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                db.collection("Contas").document(user.getUid())
                        .update("nome", ed_nome_dialog.getText().toString(), "foto", DadosPessoas.foto);

                txt_nomeConta.setText(ed_nome_dialog.getText().toString());
                image_perfil.setImageBitmap(imgPerfil);
                dialog.dismiss();
            });

            dialog.show();
        });

        linerLayout_gerarLista.setOnClickListener(v -> {
            Intent lista = new Intent(Menu.this, ListaActivity.class);
            startActivity(lista);
            overridePendingTransition(R.anim.mover_direita, R.anim.mover_direita2);
        });

        linerLayout_gerarEscola.setOnClickListener(v -> {
            Intent grafico = new Intent(Menu.this, GraficoActivity.class);

            DadosPessoas.grupo = "Escola";
            DadosPessoas.tipo = "qtdPessoas";

            startActivity(grafico);
            overridePendingTransition(R.anim.mover_direita, R.anim.mover_direita2);
        });

        linerLayout_gerarIdade.setOnClickListener(v -> {
            Intent grafico = new Intent(Menu.this, GraficoActivity.class);

            DadosPessoas.grupo = "Idade";
            DadosPessoas.tipo = "qtdPessoas";

            startActivity(grafico);
            overridePendingTransition(R.anim.mover_direita, R.anim.mover_direita2);
        });

        linerLayout_gerarInscricao.setOnClickListener(v -> {
            Intent grafico = new Intent(Menu.this, GraficoActivity.class);

            DadosPessoas.grupo = "Inscrições por Hora";
            DadosPessoas.tipo = "qtdPessoas";

            startActivity(grafico);
            overridePendingTransition(R.anim.mover_direita, R.anim.mover_direita2);
        });

        // Obter a imagem como uma string
        byte[] imageBytes = Base64.decode(DadosPessoas.foto, Base64.DEFAULT);
        imgPerfil = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
        image_perfil.setImageBitmap(imgPerfil);

        txt_nomeConta.setText(DadosPessoas.nome);
    }

    public void iniciarComponentes() {
        btn_logout = findViewById(R.id.btn_logout);
        btn_config = findViewById(R.id.btn_config);

        linerLayout_gerarLista = findViewById(R.id.linerLayout_gerarLista);
        linerLayout_gerarIdade = findViewById(R.id.linerLayout_gerarIdade);
        linerLayout_gerarInscricao = findViewById(R.id.linerLayout_gerarInscricao);
        linerLayout_gerarEscola = findViewById(R.id.linerLayout_gerarEscola);
        linearLayout_Conteudo = findViewById(R.id.linearLayout_Conteudo);

        progressBar_Carregar = findViewById(R.id.progressBar_Carregar);

        txt_nomeConta = findViewById(R.id.txt_nomeConta);

        image_perfil = findViewById(R.id.image_perfil);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                try {
                    Uri imageUri = data.getData();
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri);

                    int porcentagem = 100;

                    do {
                        byte[] fotoBytes;
                        ByteArrayOutputStream streamFotoByte = new ByteArrayOutputStream();
                        bitmap.compress(Bitmap.CompressFormat.JPEG, porcentagem, streamFotoByte);
                        fotoBytes = streamFotoByte.toByteArray();

                        DadosPessoas.foto = Base64.encodeToString(fotoBytes, Base64.DEFAULT);
                        porcentagem -= 5;
                    } while (DadosPessoas.foto.length() > 500000);

                    if (porcentagem >= 30) {
                        byte[] imageBytes = Base64.decode(DadosPessoas.foto, Base64.DEFAULT);
                        imgPerfil = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
                        img_perfil_dialog.setImageBitmap(imgPerfil);
                    } else {
                        Toast.makeText(this, "Qualidade da imagem muito alta", Toast.LENGTH_LONG).show();
                    }
                } catch (Exception ignored) {}
            }
        }
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(0, R.anim.mover_esquerda);
    }
}