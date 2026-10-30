package com.example.salvadordesemestres.vistas.vistas;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import android.content.Intent;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.salvadordesemestres.R;

public class PantallaPrincipal extends AppCompatActivity implements SensorEventListener {

    EditText etTemaEstudio, etDescripcionFinal;
    Spinner spinnerMinutos;
    TextView tvCronometro;

    Button btnIniciarTimer, btnIrHistorial, btnDetenerTimer, btnIrPerfil;

    CountDownTimer temporizador;
    String ultimoTemaTerminado = "";
    String ultimoTiempoTerminado = "";
    String ultimaDescrpcion = "";
    boolean cronometroCorriendo = false;
    boolean estaPausado = false;
    long tiempoMilisegundos = 0;
    String temaActual = "";
    String tiempoElegidoString = "";

    private SensorManager sensorManager;
    private Sensor acelerometro;
    private long ultimaVezAgitado = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_pantalla_principal);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        etTemaEstudio = findViewById(R.id.etTemaEstudio);
        spinnerMinutos = findViewById(R.id.spinnerMinutos);
        tvCronometro = findViewById(R.id.tvCronometro);
        btnIniciarTimer = findViewById(R.id.btnIniciarTimer);
        btnIrHistorial = findViewById(R.id.btnIrHistorial);
        btnDetenerTimer = findViewById(R.id.btnDetenerTimer);
        etDescripcionFinal = findViewById(R.id.etDescripcionFinal);
        btnIrPerfil = findViewById(R.id.btnIrPerfil);

        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        if (sensorManager !=null){
            acelerometro = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        }

        etDescripcionFinal.setVisibility(View.GONE);
        /**
         * Gestiona el inicio y pausa del cronómetro y cambia el estado visual del botón
         * @param v La vista (botón) que fue pulsado
         * @return void No retorna valor alguno
         * @throws No lanza excepciones controladas
         */
        btnIniciarTimer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cronometroCorriendo) {
                    temporizador.cancel();
                    cronometroCorriendo = false;
                    estaPausado = true;
                    btnIniciarTimer.setText("Retomar Estudio");
                    Toast.makeText(PantallaPrincipal.this, "Tiempo Pausado", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (estaPausado) {
                    estaPausado = false;
                    btnIniciarTimer.setText("Pausar");
                    iniciarCronometro(temaActual);
                    return;
                }

                String tema = etTemaEstudio.getText().toString().trim();
                String opcionElegida = spinnerMinutos.getSelectedItem().toString();
                String minutosStr = opcionElegida.replace(" minutos", "");
                minutosStr = minutosStr.replace(" minuto", "");

                tiempoElegidoString = minutosStr;

                if (tema.isEmpty()) {
                    Toast.makeText(PantallaPrincipal.this, "Complete los campos", Toast.LENGTH_SHORT).show();
                    return;
                }
                temaActual = tema;
                int minutosInt = Integer.parseInt(minutosStr);
                tiempoMilisegundos = minutosInt * 60000L;

                btnIniciarTimer.setText("Pausar");
                iniciarCronometro(temaActual);
            }
        });

        /**
         * reinicia los valores del temporizador a cero y
         * restaura la interfaz visual a su estado inicial
         * @param v La vista (botón) que fue pulsado
         * @return void No retorna ningún valor
         * @throws No lanza excepciones controladas
         */
        btnDetenerTimer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(cronometroCorriendo || estaPausado){
                    if(temporizador != null) temporizador.cancel();
                    cronometroCorriendo = false;
                    estaPausado = false;
                    tiempoMilisegundos = 0;
                    tvCronometro.setText("00:00");
                    btnIniciarTimer.setText("Empezar a Estudiar");
                    Toast.makeText(PantallaPrincipal.this, "No dejes de Estudiar, Retómalo cuando puedas", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(PantallaPrincipal.this, "No estas estudiando, ¿que quieres detener?", Toast.LENGTH_SHORT).show();
                }
            }
        });

        /**
         * Guarda los datos de la última sesión en la memoria global
         * y redirige a la pantalla de historial de estudio
         * @param v La vista (botón) que fue pulsado
         * @return void No retorna ningún valor
         * @throws No lanza excepciones controladas
         */
        btnIrHistorial.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!ultimoTemaTerminado.isEmpty()) {

                    ultimaDescrpcion = etDescripcionFinal.getText().toString().trim();
                    if (ultimaDescrpcion.isEmpty()) {
                        ultimaDescrpcion = "no sabes que estudiaste";
                    }

                    String registro = ultimoTemaTerminado + " | " + ultimaDescrpcion + " | " + ultimoTiempoTerminado;
                    DatosGlobales.historialCompleto.add(registro);

                    ultimoTemaTerminado = "";
                }

                Intent intent = new Intent(PantallaPrincipal.this, HistorialDeEstudio.class);
                startActivity(intent);

                etDescripcionFinal.setVisibility(View.GONE);
                etDescripcionFinal.setText("");
            }
        });

        /**
         * nos redirige a traves de un intent a la pantalla de perfil de usuario
         * @param v La vista (botón) que fue pulsado
         * @return void No retorna ningún valor
         * @throws No lanza excepciones controladas
         */
        btnIrPerfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PantallaPrincipal.this, PerfilUsuario.class);
                startActivity(intent);
            }
        });
    }

    private void iniciarCronometro(String tema){
        temporizador = new CountDownTimer(tiempoMilisegundos, 1000) {
            @Override
            public void onFinish() {
                cronometroCorriendo = false;
                tvCronometro.setText("00:00");
                btnIniciarTimer.setText("Empezar a Estudiar");
                ultimoTemaTerminado = temaActual;
                ultimoTiempoTerminado = tiempoElegidoString + " Minutos";
                etDescripcionFinal.setVisibility(View.VISIBLE);
                DatosGlobales.totalSesiones++;
                DatosGlobales.totalMinutos = DatosGlobales.totalMinutos + Integer.parseInt(tiempoElegidoString);
                Toast.makeText(PantallaPrincipal.this, "Terminaste de estudiar: " + tema , Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onTick(long tiempoRestante) {
                tiempoMilisegundos = tiempoRestante;
                actualizarTextoCronometro();

            }
        }.start();

        cronometroCorriendo = true;
        Toast.makeText(this, "Empezando a estudiar: " + tema, Toast.LENGTH_SHORT).show();
    }

    private void actualizarTextoCronometro(){
        int minutos = (int) (tiempoMilisegundos / 1000) / 60;
        int segundos = (int) (tiempoMilisegundos / 1000) % 60;

        String tiempoFormateado = String.format("%02d:%02d", minutos , segundos);
        tvCronometro.setText((tiempoFormateado));
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            float x = event.values[0];
            float y = event.values[1];
            float z = event.values[2];

            double aceleracionG = Math.sqrt(x * x + y * y + z * z) / SensorManager.GRAVITY_EARTH;

            if (aceleracionG > 2.5) {
                long tiempoActual = System.currentTimeMillis();

                if (tiempoActual - ultimaVezAgitado > 3000) {
                    ultimaVezAgitado = tiempoActual;

                    Toast.makeText(this, "Calmate todo esta bien, respira", Toast.LENGTH_LONG).show();
                }
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (acelerometro != null) {
            sensorManager.registerListener(this, acelerometro, SensorManager.SENSOR_DELAY_NORMAL);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (sensorManager != null) {
            sensorManager.unregisterListener(this);
        }
    }
}