package com.example.cazandopatos.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.cazandopatos.R;
import com.example.cazandopatos.common.Constantes;
import com.example.cazandopatos.models.User;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

public class LoginActivity extends AppCompatActivity {

    EditText et_nick;
    Button bt_btnStart;
    String nick;
    FirebaseFirestore db;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        db = FirebaseFirestore.getInstance();

        et_nick = findViewById(R.id.editTextNick);
        bt_btnStart = findViewById(R.id.button_iniciar);


        Typeface typeface = Typeface.createFromAsset(getAssets(), "pixel.ttf");
        et_nick.setTypeface(typeface);
        bt_btnStart.setTypeface(typeface);


        bt_btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nick = et_nick.getText().toString();
                if (nick.isEmpty()){
                    et_nick.setError("Debe escribir el nombre de User");
                }else if (nick.length() <3 ) {
                    et_nick.setError("Debe tener al menos 3 caracteres");
                }else {

                    addNickAndStart();

                }
            }
        });

    }

    private void addNickAndStart() {
        db.collection("user").whereEqualTo("nick", nick)
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {   //Listener es un metodo que cuando se cumple lanza un metodo.
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        if (queryDocumentSnapshots.size() > 0) {
                            et_nick.setError("El nick no esta disponible");
                        }else {
                            addNickToFirestore();
                        }

                    }
                });
    }

    private void addNickToFirestore() {
        User nuevoUsuario = new User(nick , 0);

        db.collection("user")
                .add(nuevoUsuario)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() { //metodo que actua como evento, cuando termina la peticion y esta ok lanza el onSucces
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        et_nick.setText("");

                        Intent i = new Intent(LoginActivity.this, GameActivity.class);
                        i.putExtra(Constantes.EXTRA_NICK, nick);
                        i.putExtra(Constantes.EXTRA_ID, documentReference.getId());
                        startActivity(i);

                    }
                });

    }
}