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

/**
 * Activity que muestra las estadísticas globales del usuario (sesiones y minutos)
 * y permite gestionar la cuenta, como cambiar clave, eliminar la cuenta y cerrar sesion.
 * @param Ninguno
 * @return void no retorna nada
 * @throws Exception no cuenta con exceptiones controladas.
 */
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


        /**
         * Cierra la pantalla de Perfil y regresa a la vista anterior.
         * @param v La vista (botón) que fue pulsado
         * @return void no retorna valor alguno
         * @throws no cuenta con exceptiones controladas
         */
        btnVolver.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(android.view.View v){
                finish();
            }
        });

        android.content.SharedPreferences preferencias = getSharedPreferences("MisSesiones", MODE_PRIVATE);
        String correoActual = preferencias.getString("correoUsuario", "");


        com.example.salvadordesemestres.vistas.db.UsuarioDbHelper dbHelper = new com.example.salvadordesemestres.vistas.db.UsuarioDbHelper(this);

        /**
         * Redirige al usuario a la vista para cambiar su contraseña.
         * @param v La vista (botón) que fue pulsado
         * @return void no retorna valor alguno
         * @throws no cuenta con exceptiones controladas
         */
        btnActualizarClave.setOnClickListener(new android.view.View.OnClickListener() {
            @Override
            public void onClick(android.view.View v) {
                Intent intent = new Intent(PerfilUsuario.this, CambiarClave.class);
                startActivity(intent);
            }
        });

        /**
         * Despliega un cuadro de alerta para confirmar y eliminar la cuenta del usuario en SQLite.
         * @param v La vista (botón) que fue pulsado
         * @return void no retorna valor alguno
         * @throws no cuenta con exceptiones controladas
         */
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

        /**
         * Borra la sesión activa de SharedPreferences y redirige al inicio cerrando todas las vistas.
         * @param v La vista (botón) que fue pulsado
         * @return void no retorna valor alguno
         * @throws no cuenta con exceptiones controladas
         */
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