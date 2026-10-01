package com.example.salvadordesemestres.vistas;

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
import android.widget.TextView;
import android.widget.Toast;

import com.example.salvadordesemestres.R;

public class PantallaPrincipal extends AppCompatActivity {

    EditText etTemaEstudio, etMinutos, etDescripcionFinal;
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
        etMinutos = findViewById(R.id.etMinutos);
        tvCronometro = findViewById(R.id.tvCronometro);
        btnIniciarTimer = findViewById(R.id.btnIniciarTimer);
        btnIrHistorial = findViewById(R.id.btnIrHistorial);
        btnDetenerTimer = findViewById(R.id.btnDetenerTimer);
        etDescripcionFinal = findViewById(R.id.etDescripcionFinal);
        btnIrPerfil = findViewById(R.id.btnIrPerfil);

        etDescripcionFinal.setVisibility(View.GONE);

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
                    btnIniciarTimer.setText("PAUSAR");
                    iniciarCronometro(temaActual);
                    return;
                }

                String tema = etTemaEstudio.getText().toString().trim();
                String minutosStr = etMinutos.getText().toString().trim();

                if (tema.isEmpty() || minutosStr.isEmpty()) {
                    Toast.makeText(PantallaPrincipal.this, "Complete los campos", Toast.LENGTH_SHORT).show();
                    return;
                }
                temaActual = tema;
                int minutosInt = Integer.parseInt(minutosStr);
                tiempoMilisegundos = minutosInt * 60000L;

                btnIniciarTimer.setText("PAUSAR");
                iniciarCronometro(temaActual);
            }
        });

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

        btnIrHistorial.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!ultimoTemaTerminado.isEmpty()) {

                    ultimaDescrpcion = etDescripcionFinal.getText().toString().trim();
                    if (ultimaDescrpcion.isEmpty()) {
                        ultimaDescrpcion = "Sin notas";
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
                ultimoTiempoTerminado = etMinutos.getText().toString() + " Minutos";
                etDescripcionFinal.setVisibility(View.VISIBLE);
                DatosGlobales.totalSesiones++;
                DatosGlobales.totalMinutos = DatosGlobales.totalMinutos + Integer.parseInt(etMinutos.getText().toString().trim());
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
}