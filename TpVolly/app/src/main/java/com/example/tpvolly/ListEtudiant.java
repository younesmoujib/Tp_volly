package com.example.tpvolly;

import static android.content.ContentValues.TAG;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.tpvolly.adapter.EtudiantAdapter;
import com.example.tpvolly.beans.Etudiant;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ListEtudiant extends AppCompatActivity {
    ListView listView;
    private EtudiantAdapter etudiantAdapter;
    private Etudiant etudiant;
    RequestQueue requestQueue;
    public static List<Etudiant> etudiants = new ArrayList<>();
    String insertUrl = "http://192.168.1.4/PhpP1/ws/loadEtudiant.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_etudiant);
        listView = findViewById(R.id.List);


        RequestQueue requestQueue = Volley.newRequestQueue(this);


        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
                Request.Method.POST, insertUrl, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {

                        try {
                            etudiants.clear();
                            for (int i = 0; i < response.length(); i++) {

                                JSONObject etudiantJson = response.getJSONObject(i);
                                Etudiant etudiant = new Etudiant();
                                etudiant.setId(etudiantJson.getInt("id"));
                                etudiant.setNom(etudiantJson.getString("nom"));
                                etudiant.setPrenom(etudiantJson.getString("prenom"));
                                etudiant.setVille(etudiantJson.getString("ville"));
                                etudiant.setSexe(etudiantJson.getString("sexe"));

                                etudiants.add(etudiant);

                            }


                            etudiantAdapter = new EtudiantAdapter(etudiants, ListEtudiant.this);
                            listView.setAdapter(etudiantAdapter);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        Toast.makeText(ListEtudiant.this, "Erreur de chargement des données", Toast.LENGTH_SHORT).show();
                    }
                }
        );


        requestQueue.add(jsonArrayRequest);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Etudiant etudiantSelectionne = etudiants.get(position);


                AlertDialog.Builder builder = new AlertDialog.Builder(ListEtudiant.this);
                builder.setTitle("Options");
                builder.setMessage("Que voulez-vous faire avec " + etudiantSelectionne.getNom() + "?");

                builder.setPositiveButton("Supprimer", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        showDeleteConfirmationDialog(etudiantSelectionne);



                    }
                });


                builder.setNegativeButton("Modifier", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        showEditDialog(etudiantSelectionne);
                    }
                });


                builder.setNeutralButton("Annuler", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });


                builder.show();
            }
        });

    }
    private void showDeleteConfirmationDialog(final Etudiant etudiant) {
        AlertDialog.Builder confirmationDialog = new AlertDialog.Builder(this);
        confirmationDialog.setTitle("Confirmation de suppression");
        confirmationDialog.setMessage("Voulez-vous vraiment supprimer " + etudiant.getNom() + "?");

        confirmationDialog.setPositiveButton("Oui", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                supprimerEtudiantAPI(etudiant);
                etudiants.remove(etudiant);
                listView.setAdapter(new EtudiantAdapter(etudiants,ListEtudiant.this));
                Toast.makeText(ListEtudiant.this, "Étudiant supprimé avec succès", Toast.LENGTH_SHORT).show();

                dialog.dismiss();
            }
        });

        confirmationDialog.setNegativeButton("Non", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        confirmationDialog.show();
    }

    private void showEditDialog(final Etudiant etudiant) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Modification de l'etudiant : "+etudiant.getNom());

        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);

        final EditText nomInput = new EditText(this);
        nomInput.setHint("Nom");
        nomInput.setText(etudiant.getNom());
        layout.addView(nomInput);

        final EditText prenomInput = new EditText(this);
        prenomInput.setHint("Prénom");
        prenomInput.setText(etudiant.getPrenom());
        layout.addView(prenomInput);

        final Spinner villeSpinner = new Spinner(this);
        ArrayAdapter<CharSequence> adapterV = ArrayAdapter.createFromResource(this, R.array.villes, android.R.layout.simple_spinner_item);
        adapterV.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        villeSpinner.setAdapter(adapterV);
        villeSpinner.setSelection(adapterV.getPosition(etudiant.getVille()));
        layout.addView(villeSpinner);

        final RadioGroup sexeRadioGroup = new RadioGroup(this);

        RadioButton hommeRadio = new RadioButton(this);
        hommeRadio.setText("Homme");
        hommeRadio.setId(View.generateViewId());
        sexeRadioGroup.addView(hommeRadio);

        RadioButton femmeRadio = new RadioButton(this);
        femmeRadio.setText("Femme");
        femmeRadio.setId(View.generateViewId());
        sexeRadioGroup.addView(femmeRadio);

        if (etudiant.getSexe().equals("Homme")) {
            hommeRadio.setChecked(true);
        } else {
            femmeRadio.setChecked(true);
        }

        layout.addView(sexeRadioGroup);

        builder.setView(layout);

        builder.setPositiveButton("Enregistrer", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String newNom = nomInput.getText().toString();
                String newPrenom = prenomInput.getText().toString();
                String newVille = villeSpinner.getSelectedItem().toString();
                String newSexe = (hommeRadio.isChecked()) ? "Homme" : "Femme";

                Etudiant updatedEtudiant = new Etudiant(
                        etudiant.getId(),
                        newNom,
                        newPrenom,
                        newVille,
                        newSexe
                );

                int position = etudiants.indexOf(etudiant);

                etudiants.set(position, updatedEtudiant);
                String updateUrl = "http://192.168.1.4/PhpP1/ws/updateEtudiant.php?id="  +updatedEtudiant.getId() +
                        "&nom=" + updatedEtudiant.getNom() + "&prenom=" + updatedEtudiant.getPrenom() + "&ville=" + updatedEtudiant.getVille() + "&sexe=" + updatedEtudiant.getSexe();
                Log.d("etudiant",updatedEtudiant.toString());

                    requestQueue = Volley.newRequestQueue(getApplicationContext());
                    StringRequest request = new StringRequest(Request.Method.POST, updateUrl, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Log.d(TAG, response);
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {

                        }
                    });

                    requestQueue.add(request);


                etudiantAdapter = new EtudiantAdapter(etudiants, ListEtudiant.this);
                listView.setAdapter(etudiantAdapter);
                Toast.makeText(ListEtudiant.this, "Étudiant modifer avec succès", Toast.LENGTH_SHORT).show();
            }
        });

        builder.setNegativeButton("Annuler", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
    }




    private void supprimerEtudiantAPI(final Etudiant etudiant) {

        String deleteUrl = "http://192.168.1.4/PhpP1/ws/deleteEtudiant.php?id=" + etudiant.getId();

        StringRequest request = new StringRequest(Request.Method.POST, deleteUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(ListEtudiant.this, "Erreur de réseau lors de la suppression de l'étudiant", Toast.LENGTH_SHORT).show();
            }
        });

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(request);
    }



}
