package com.example.adm_escola;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.adm_escola.ClassBean.DadosPessoas;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class MainActivity extends AppCompatActivity {

    FirebaseAuth auth = FirebaseAuth.getInstance();
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    LinearLayout btn_logar;
    EditText et_email, et_senha;
    ProgressBar progressBar;
    TextView txt_redefinirSenha;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        iniciarComponentes();

        btn_logar.setOnClickListener(v -> {
            String email = et_email.getText().toString();
            String senha = et_senha.getText().toString();

            if (!email.trim().equals("") && !senha.trim().equals("")) {
                btn_logar.setVisibility(View.GONE);
                progressBar.setVisibility(View.VISIBLE);

                auth.signInWithEmailAndPassword(email, senha)
                        .addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                // Coletar só uma vez no FireStore
                                db.collection("Contas").document(auth.getCurrentUser().getUid()).get()
                                        .addOnSuccessListener(documentSnapshot -> {
                                            if (documentSnapshot.exists()) {
                                                DadosPessoas.nome = documentSnapshot.getString("nome");
                                                DadosPessoas.foto = documentSnapshot.getString("foto");

                                                Intent menu = new Intent(MainActivity.this, Menu.class);
                                                startActivity(menu);
                                                overridePendingTransition(R.anim.mover_direita, R.anim.mover_direita2);
                                                finish();
                                            }
                                        });
                            } else {
                                btn_logar.setVisibility(View.VISIBLE);
                                progressBar.setVisibility(View.GONE);
                                Toast.makeText(MainActivity.this, "Email ou Senha errada!", Toast.LENGTH_LONG).show();
                            }
                        });
            } else {
                Toast.makeText(MainActivity.this, "Email e Senha Obrigatório!", Toast.LENGTH_LONG).show();
            }
        });

        txt_redefinirSenha.setOnClickListener(v -> {
            // Dialog
            Dialog dialog = new Dialog(this);
            dialog.setContentView(R.layout.layout_dialog_redefinir_senha);
            dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            dialog.setCancelable(true);

            Button btn_cancelarEnvio_dialog = dialog.findViewById(R.id.btn_cancelarEnvio_dialog);
            Button btn_enviarRedefinirSenha_dialog = dialog.findViewById(R.id.btn_enviarRedefinirSenha_dialog);
            EditText ed_nome_redefinirSenha_dialog = dialog.findViewById(R.id.ed_nome_redefinirSenha_dialog);

            btn_cancelarEnvio_dialog.setOnClickListener(v2 -> dialog.dismiss());
            btn_enviarRedefinirSenha_dialog.setOnClickListener(v2 -> {
                String email = ed_nome_redefinirSenha_dialog.getText().toString().trim();
                if (!email.equals("")) {
                    FirebaseAuth auth = FirebaseAuth.getInstance();

                    auth.sendPasswordResetEmail(email)
                            .addOnCompleteListener(task -> {
                                if (task.isSuccessful()) {
                                    Toast.makeText(MainActivity.this, "Um email enviado para redefinir", Toast.LENGTH_LONG).show();
                                } else {
                                    Toast.makeText(MainActivity.this, "Email não existe", Toast.LENGTH_LONG).show();
                                }
                                dialog.dismiss();
                            });
                } else {
                    Toast.makeText(this, "Informe um email", Toast.LENGTH_LONG).show();
                }
            });

            dialog.show();
        });
    }

    public void iniciarComponentes() {
        et_email = findViewById(R.id.et_email);
        et_senha = findViewById(R.id.et_senha);
        btn_logar = findViewById(R.id.btn_logar);
        progressBar = findViewById(R.id.progressBar);
        txt_redefinirSenha = findViewById(R.id.txt_redefinirSenha);
    }
}

