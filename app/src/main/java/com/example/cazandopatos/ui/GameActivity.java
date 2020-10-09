package com.example.cazandopatos.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Point;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.view.Display;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.cazandopatos.R;
import com.example.cazandopatos.common.Constantes;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Random;

public class GameActivity extends AppCompatActivity {

    TextView tvCounterDucks, tvTimer, tvNick;
    ImageView ivDuck;
    int counter = 0 ;
    int anchoPantalla;
    int altoPantalla;
    Random aleatorio;
    Boolean gameOver = false;
    String nick;
    String id;
    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        db = FirebaseFirestore.getInstance();

        initViewComponent();
        eventos();
        initPantalla();
        moveDuck();
        initCuentaRegresiva();

        
    }

    private void initCuentaRegresiva() {

        new CountDownTimer(10000, 1000) {

            public void onTick(long millisUntilFinished) {
                long segundosRestantes = millisUntilFinished / 1000;
                tvTimer.setText(segundosRestantes +  "s");
            }

            public void onFinish() {
                tvTimer.setText("0s");
                gameOver = true; 
                mostrarDialogoGameOver();
                saveResultFireStore();
            }
        }.start();

    }
    private void saveResultFireStore() {
        db.collection("user")
                .document(id)
                .update("ducks", counter);

    }

    private void mostrarDialogoGameOver() {

        AlertDialog.Builder builder = new AlertDialog.Builder(GameActivity.this);


        builder.setMessage("Pudiste cazar " + counter + " patos")
                .setTitle("Game Over");



        builder.setPositiveButton("Reiniciar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
              counter = 0;
              tvCounterDucks.setText("0");
              gameOver = false;
              initCuentaRegresiva();
              moveDuck();
            }
        });

        builder.setNegativeButton("Mostar Ranking", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.dismiss();
                Intent i = new Intent(GameActivity.this, RankingActivity.class);
                startActivity(i);
            }
        });


        AlertDialog dialog = builder.create();

        dialog.show();
    }

    private void initPantalla() {

        Display display  = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        anchoPantalla = size.x;
        altoPantalla = size.y;


        aleatorio = new Random();
    }

    private void initViewComponent() {

        tvCounterDucks = findViewById(R.id.textViewContador);
        tvNick = findViewById(R.id.textViewNickGame);
        tvTimer = findViewById(R.id.textViewTimer);
        ivDuck = findViewById(R.id.imageViewDuck);

        Typeface typeface = Typeface.createFromAsset(getAssets(), "pixel.ttf");
        tvCounterDucks.setTypeface(typeface);
        tvNick.setTypeface(typeface);
        tvTimer.setTypeface(typeface);


        Bundle extras = getIntent().getExtras();
        nick = extras.getString(Constantes.EXTRA_NICK);
        id = extras.getString(Constantes.EXTRA_ID);
        tvNick.setText(nick);
    }

    private void eventos() {

        ivDuck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!gameOver) {
                    counter++;
                    tvCounterDucks.setText(String.valueOf(counter));

                    ivDuck.setImageResource(R.drawable.duck_clicked);

                    new Handler().postDelayed(new Runnable() {   //metodo que hace que el codigo se ejecute unos segundos despues.
                        @Override
                        public void run() {
                            ivDuck.setImageResource(R.drawable.duck);
                            moveDuck();
                        }
                    }, 200);
                }
            }
        });
    }

       private void moveDuck() {
          int min = 0 ;
          int maximoX = anchoPantalla - ivDuck.getWidth();
          int maximoY = altoPantalla - ivDuck.getHeight();



           int randomX = aleatorio.nextInt(((maximoX - min) + 1 ) + min);
           int randomY = aleatorio.nextInt(((maximoX - min) + 1 ) + min);



           ivDuck.setX(randomX);
           ivDuck.setY(randomY);


    }


}