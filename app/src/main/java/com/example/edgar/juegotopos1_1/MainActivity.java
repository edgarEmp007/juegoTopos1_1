package com.example.edgar.juegotopos1_1;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.drawable.AnimationDrawable;
import android.media.MediaPlayer;
import android.os.CountDownTimer;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Chronometer;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.HashMap;
import java.util.Map;


public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private MediaPlayer player,aplastado;
    TextView score;
    private int estadomusica;
    private int velocidad,contadorgolpe;
    private boolean golpe;
    private Chronometer tiempo;
    boolean sianimar=true;
    int numero;
    private int id[]={R.id.img1,R.id.img2,R.id.img3,R.id.img4,R.id.img5,
            R.id.img6};
    ImageView anayas[]=new ImageView[6];
    AnimationDrawable animacionTopo[]= new AnimationDrawable[6];
    long contador,puntaje;



    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_main);
        getSupportActionBar().setTitle("Aplasta al Topo");

        contador=0;
        velocidad=1500;
        contadorgolpe=0;
        golpe=false;
        estadomusica=1;
        puntaje=0;

        for(int i=0;i<id.length;i++){
            anayas[i]= (ImageView) findViewById(id[i]);
            anayas[i].setBackgroundResource(R.drawable.animacion);
            animacionTopo[i] =(AnimationDrawable) anayas[i].getBackground();
        }
        tiempo=(Chronometer) findViewById(R.id.tiempoConcentrese);
        score=(TextView) findViewById(R.id.tpuntos);

        player = MediaPlayer.create(this, R.raw.sonidoparatopo);
        aplastado=MediaPlayer.create(this,R.raw.aplastado);
        score.setText("Score: "+ String.valueOf(puntaje)); // se carga el puntaje en el textview
        for(int k=0;k<6;k++){
            anayas[k].setOnClickListener(this);
        }

        // Se activael sonido si el usuario lo permite
        if (estadomusica==1){
            player.setLooping(true);
            player.start();
        }

        //Crear un dialog para comezar
        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        builder.setTitle("¿Estas listo listo?");
        builder.setMessage("Presiona 'Listo' para iniciar el juego\n");
        builder.setPositiveButton("Listo", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                tiempo.start(); //Se inicia el reloj
                tiempo.setBase(SystemClock.elapsedRealtime()); // Se inicia el tiempo desde 0

                animar(); // Se inicia la animación
            }
        });
        builder.setNegativeButton("Aun no estoy listo", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                player.stop();
                finish();
            }
        });
        AlertDialog dialog= builder.create();
        dialog.setCancelable(false);
        dialog.show();



    }

    private void animar() {
        numero = (int) (Math.random() * 6 );
        if(sianimar) {
            animacionTopo[numero].stop();
            animacionTopo[numero].start();
            contador = (SystemClock.elapsedRealtime() - tiempo.getBase()) / 1000;
        }
        loop();
    }
    public void loop(){
        Long velaux=(SystemClock.elapsedRealtime()-tiempo.getBase())/1000;

        if(velaux<=30){
            velocidad=1500;
        }
        else if(velaux>30 && velaux<=60){
            velocidad=1200;
        }
        else if(velaux>60 && velaux<=90){
            velocidad=1000;
        }
        else{
            velocidad=750;
        }

        CountDownTimer countDownTimer=new CountDownTimer(velocidad,200) {
            @Override
            public void onTick(long l) {
            }
            @Override
            public void onFinish() {

                if(golpe==false){
                    if(contadorgolpe<3) {
                        contadorgolpe++;
                    }
                }
                if(contadorgolpe<3){
                    animar();
                    golpe=false;
                }
                else{
                    mensaje();
                }

            }
        };
        countDownTimer.start();
    }

    private void mensaje() {
        player.stop();
        final Intent intent=new Intent(getBaseContext(),MainActivity.class);

        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        builder.setTitle("JAJAJAJA LOOOSEEEER");
        builder.setMessage("¿Deseas reinciar el juego y mejorar tu puntaje?\n");
        builder.setPositiveButton("Si", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //puntaje=0;
                //editor_preferencias.putLong("puntajeTopo",puntaje).apply();
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);

            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                finish();

            }
        });
        AlertDialog dialog= builder.create();
        dialog.setCancelable(false);
        dialog.show();
    }
    public void onClick(View src) {

        int numeroaux=numero;
        int numeroaux2=0;
        long aux1= (SystemClock.elapsedRealtime()-tiempo.getBase())/1000;
        for (int i=0;i<id.length;i++){
            if(src==anayas[i]){
                numeroaux2=i;
                break;
            }
        }
        if(aux1-contador<=920 && numeroaux==numeroaux2){
            animacionTopo[numeroaux].stop();
            if(estadomusica==1){
                aplastado.start();
            }
            golpe=true;

            final int numerito=numeroaux;
            anayas[numeroaux].setBackgroundResource(R.drawable.golpe);
            puntaje+=5;
            //editor_preferencias.putLong("puntajeTopo",puntaje).apply();
            score.setText("Score: "+ String.valueOf(puntaje));


            CountDownTimer countDownTimer=new CountDownTimer(500,100) {
                @Override
                public void onTick(long l) {
                }

                @Override
                public void onFinish() {
                    anayas[numerito].setBackgroundResource(R.drawable.animacion);
                    animacionTopo[numerito] =(AnimationDrawable) anayas[numerito].getBackground();
                }
            };
            countDownTimer.start();
        }
    }
    public void onBackPressed(){

        finish();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {

        super.onStop();
    }
    @Override
    protected void onRestart() {

        if (estadomusica==1){
            player = MediaPlayer.create(this, R.raw.sonidoparatopo);
            player.setLooping(true);
            player.start();
        }
        super.onRestart();
    }
}
