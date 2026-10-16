package com.example.salvadordesemestres.vistas.vistas;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.salvadordesemestres.R;

public class PerfilUsuario extends AppCompatActivity {

    TextView tvTotalSesiones;
    TextView tvTotalMinutos;

    Button btnCerrarSesion;
    Button btnVolver;
    Button btnActualizarClave;
    Button btnEliminarCuenta;


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
        btnVolver = findViewById(R.id.btnVolverPerfil);
        btnActualizarClave = findViewById(R.id.btnActualizarClave);
        btnEliminarCuenta = findViewById(R.id.btnEliminarCuenta);
        btnCerrarSesion = findViewById(R.id.btnCerrarSesion);


        Intent intent = getIntent();

        int sesiones = DatosGlobales.totalSesiones;
        int minutos = DatosGlobales.totalMinutos;

        tvTotalSesiones.setText("Sesiones completas: " + sesiones);
        tvTotalMinutos.setText("Minutos estudiados: " + minutos);


        btnVolver.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(android.view.View v){
                finish();
            }
        });

        android.content.SharedPreferences preferencias = getSharedPreferences("MisSesiones", MODE_PRIVATE);
        String correoActual = preferencias.getString("correoUsuario", "");


        com.example.salvadordesemestres.vistas.db.UsuarioDbHelper dbHelper = new com.example.salvadordesemestres.vistas.db.UsuarioDbHelper(this);

        btnActualizarClave.setOnClickListener(new android.view.View.OnClickListener() {
            @Override
            public void onClick(android.view.View v) {
                Intent intent = new Intent(PerfilUsuario.this, CambiarClave.class);
                startActivity(intent);
            }
        });

        btnEliminarCuenta.setOnClickListener(new android.view.View.OnClickListener() {
            @Override
            public void onClick(android.view.View v) {
                new androidx.appcompat.app.AlertDialog.Builder(PerfilUsuario.this)
                        .setTitle("¿Estás seguro?")
                        .setMessage("Si eliminas tu cuenta, perderas todo tu progreso")
                        .setPositiveButton("Sí, eliminar", new android.content.DialogInterface.OnClickListener() {
                            public void onClick(android.content.DialogInterface dialog, int which) {
                                int filasBorradas = dbHelper.eliminarUsuario(correoActual);
                                if (filasBorradas > 0) {
                                    android.widget.Toast.makeText(PerfilUsuario.this, "Cuenta eliminada", android.widget.Toast.LENGTH_SHORT).show();
                                    preferencias.edit().clear().apply();

                                    Intent intent = new Intent(PerfilUsuario.this, MainActivity.class);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    startActivity(intent);
                                }
                            }
                        })
                        .setNegativeButton("Cancelar", null)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();
            }
        });

        btnCerrarSesion.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(android.view.View v) {
                preferencias.edit().clear().apply();

                android.widget.Toast.makeText(PerfilUsuario.this, "Cerrando sesión", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(PerfilUsuario.this, MainActivity.class);

                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

                startActivity(intent);
                finish();

            }
        });
    }
}