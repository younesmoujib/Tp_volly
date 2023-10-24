package com.example.tpvolly.adapter;


import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.tpvolly.R;
import com.example.tpvolly.beans.Etudiant;

import java.util.List;

public class EtudiantAdapter extends BaseAdapter {
      private List<Etudiant> etudiants;
    private LayoutInflater inflater;

    public EtudiantAdapter(List<Etudiant> etudiants, Activity activity) {
        this.etudiants = etudiants;
        this.inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return etudiants.size();
    }

    @Override
    public Object getItem(int position) {
        return etudiants.get(position);
    }

    @Override
    public long getItemId(int position) {
        return etudiants.get(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView == null) convertView = inflater.inflate(R.layout.item, null);
        TextView nom =convertView.findViewById(R.id.nom1);
        TextView prenom =convertView.findViewById(R.id.prenom1);
        TextView ville=convertView.findViewById(R.id.ville1);
        TextView sexe =convertView.findViewById(R.id.sexe1);
        TextView id =convertView.findViewById(R.id.id);

        id.setText(etudiants.get(position).getId()+"");
        nom.setText(etudiants.get(position).getNom());
        prenom.setText(etudiants.get(position).getPrenom());
        ville.setText(etudiants.get(position).getVille());
        sexe.setText(etudiants.get(position).getSexe());
        return convertView;
    }
}
