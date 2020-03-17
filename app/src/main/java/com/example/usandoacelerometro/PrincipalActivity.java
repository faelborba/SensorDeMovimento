package com.example.usandoacelerometro;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class PrincipalActivity extends AppCompatActivity implements SensorEventListener {
    int cont = 0;
    private TextView tvValorX;
    private TextView tvValorY;
    private TextView tvValorZ;

    private SensorManager mSensorManager;
    private Sensor mAcelerometro;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal);

        tvValorX = (TextView) findViewById(R.id.tvValorX);
        tvValorY = (TextView) findViewById(R.id.tvValorY);
        tvValorZ = (TextView) findViewById(R.id.tvValorZ);

        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        mAcelerometro = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
    }

    @Override
    protected void onResume(){
        super.onResume();
        mSensorManager.registerListener(this,mAcelerometro, SensorManager.SENSOR_DELAY_UI);
    }
    @Override
    protected void onPause(){
        super.onPause();
        mSensorManager.unregisterListener(this);
    }
    @Override
    public void onSensorChanged(SensorEvent event){
        float x = event.values[0];
        float y = event.values[1];
        float z = event.values[2];

        tvValorX.setText(String.valueOf(x));
        tvValorY.setText(String.valueOf(y));
        tvValorZ.setText(String.valueOf(z));

        if(y>5.0&&y<5.1){
            cont ++;
            Toast.makeText(this, "foi " + cont, Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    public void btMeusSensoresOnClick(View v){
        List<Sensor> listaSensores = mSensorManager.getSensorList(Sensor.TYPE_ALL);
        String[] lista = new String[listaSensores.size()];

        for (int i = 0; i < listaSensores.size(); i++){
            lista[i] = listaSensores.get(i).getName();
        }

        Intent i = new Intent(this, ListarActivity.class);
        i.putExtra("sensores", lista);
        startActivity(i);
    }
}
