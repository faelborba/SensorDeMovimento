package com.example.usandoacelerometro;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class PrincipalActivity extends AppCompatActivity implements SensorEventListener {
    int cont = 0;
    private TextView tvValorX;
    private TextView tvValorY;
    private TextView tvValorZ;
    private TextView positionX, positionY;

    private SensorManager mSensorManager;
    private Sensor mAcelerometro;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal);

        tvValorX = (TextView) findViewById(R.id.tvValorX);
        tvValorY = (TextView) findViewById(R.id.tvValorY);
        tvValorZ = (TextView) findViewById(R.id.tvValorZ);
        positionX = (TextView) findViewById(R.id.positionX);
        positionY = (TextView) findViewById(R.id.positionY);

        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        mAcelerometro = mSensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE_UNCALIBRATED);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mSensorManager.registerListener(this, mAcelerometro, SensorManager.SENSOR_DELAY_UI);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mSensorManager.unregisterListener(this);
    }

    public List<Float> listaX = new ArrayList<>();
    public List<Float> listaY = new ArrayList<>();
    float atualX, atualY;
    public int stepX = 0, stepY = 0, stepsX = 0, stepsY = 0;



    @Override
    public void onSensorChanged(SensorEvent event) {
        synchronized (this) {
            float x = event.values[0];
            float y = event.values[1];
            float z = event.values[2];

            tvValorX.setText(String.valueOf(x));
            tvValorY.setText(String.valueOf(y));
            tvValorZ.setText(String.valueOf(z));

            //atual = (float) x * x + y * y + z * z;
            atualX = (float) x * x ;
            atualY = (float) y * y ;
            listaX.add(atualX);
            listaY.add(atualY);

            //tratando os valores do eixo x
            if (listaX.size() > 40) {
                listaX.remove(0);
                for (Float f : listaX) {
                    if (Math.abs(atualX - f) > 9.0) {
                        stepX++;
                    }
                }
                if (stepX > 10) {
                    stepsX++;
                    listaX = new ArrayList<>();
                    vibrar();// vibrar
                }
                stepX = 0;
            }
            positionX.setText("X: " + String.valueOf(stepsX));

            //tratando os valores do eixo y
            if (listaY.size() > 40) {
                listaY.remove(0);
                for (Float f : listaY) {
                    if (Math.abs(atualY - f) > 9.0) {
                        stepY++;
                    }
                }
                if (stepY > 10) {
                    stepsY++;
                    listaY = new ArrayList<>();
                    vibrar();// vibrar
                }
                stepY = 0;
            }
            positionY.setText("Y: " + String.valueOf(stepsY));

            //Toast.makeText(this, "foi " + steps, Toast.LENGTH_SHORT).show();
        /*if(y>5.0&&y<5.1){
            cont ++;
            Toast.makeText(this, "foi " + cont, Toast.LENGTH_SHORT).show();
        }*/

        }
    }

    private void vibrar()// cotrole de vibração
    {
        Vibrator rr = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        long milliseconds = 30;//'30' é o tempo em milissegundos,duração da vibração.
        rr.vibrate(milliseconds);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    public void btMeusSensoresOnClick(View v) {
        List<Sensor> listaSensores = mSensorManager.getSensorList(Sensor.TYPE_ALL);
        String[] lista = new String[listaSensores.size()];

        for (int i = 0; i < listaSensores.size(); i++) {
            lista[i] = listaSensores.get(i).getName();
        }

        Intent i = new Intent(this, ListarActivity.class);
        i.putExtra("sensores", lista);
        startActivity(i);
    }
}
