package com.example.adm_escola.ListView;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.adm_escola.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;

public class NomesAdapter  extends BaseAdapter {
    private static LayoutInflater inflater=null;
    Activity activity;
    ArrayList<HashMap<String, String>> dados;

    public NomesAdapter(Activity activity, ArrayList<HashMap<String, String>> dados) {
        this.activity = activity;
        this.dados = dados;

        inflater=(LayoutInflater) activity.getSystemService(activity.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View view1=inflater.inflate(R.layout.layout_nomes, null);

        // iniciarComponentes
        TextView txt_nome = view1.findViewById(R.id.txt_nome);
        TextView txt_idade = view1.findViewById(R.id.txt_idade);
        TextView txt_escola = view1.findViewById(R.id.txt_escola);
        TextView txt_hora = view1.findViewById(R.id.txt_hora);
        TextView txt_email = view1.findViewById(R.id.txt_email);
        TextView txt_telefone = view1.findViewById(R.id.txt_telefone);

        // Nome e Idade
        txt_nome.setText(dados.get(i).get("nome"));
        txt_idade.setText(dados.get(i).get("idade") + " anos");

        // Hora
        String[] horaNaoFormatada = dados.get(i).get("hora").split(":");
        String hora = Integer.parseInt(horaNaoFormatada[0]) < 10 ? "0" + horaNaoFormatada[0] : horaNaoFormatada[0];
        String minuto = Integer.parseInt(horaNaoFormatada[1]) < 10 ? "0" + horaNaoFormatada[1] : horaNaoFormatada[1];
        txt_hora.setText(hora + ":" + minuto);

        // Escola
        if (!dados.get(i).get("escola").trim().equals("Não Identificado")) {
            txt_escola.setText(dados.get(i).get("escola"));
        } else {
            txt_escola.setVisibility(View.GONE);
        }

        // Telefone
        if (!dados.get(i).get("telefone").trim().equals("")) {
            txt_telefone.setText(dados.get(i).get("telefone"));
        } else {
            txt_telefone.setVisibility(View.GONE);
        }

        // Email
        if (!dados.get(i).get("email").trim().equals("")) {
            txt_email.setText(dados.get(i).get("email"));
        } else {
            txt_email.setVisibility(View.GONE);
        }

        return view1;
    }

    @Override
    public int getCount() {
        return dados.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }
}
