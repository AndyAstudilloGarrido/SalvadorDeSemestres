package com.example.salvadordesemestres.vistas;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.salvadordesemestres.R;

public class PerfilUsuario extends AppCompatActivity {

    TextView tvTotalSesiones;
    TextView tvTotalMinutos;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_perfil_usuario);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        tvTotalSesiones = findViewById(R.id.tvTotalSesiones);
        tvTotalMinutos = findViewById(R.id.tvTotalMinutos);

        Intent intent = getIntent();

        int sesiones = DatosGlobales.totalSesiones;
        int minutos = DatosGlobales.totalMinutos;

        tvTotalSesiones.setText("Sesiones completas: " + sesiones);
        tvTotalMinutos.setText("Minutos estudiados: " + minutos);

        android.widget.Button btnVolver = findViewById(R.id.btnVolverPerfil);
        btnVolver.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(android.view.View v){
                finish();
            }
        });
    }
}