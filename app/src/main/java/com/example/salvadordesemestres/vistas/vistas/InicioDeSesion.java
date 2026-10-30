package com.example.salvadordesemestres.vistas.vistas;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.salvadordesemestres.R;
import com.example.salvadordesemestres.vistas.db.UsuarioContract;
/**
 * se encarga de autenticar a los usuarios registrados utilizando hilos secuandarios en las consultas a SQLite.
 */

public class InicioDeSesion extends AppCompatActivity{

    EditText etCorreo;
    EditText etContrasena;

    Button btnEntrar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_inicio_de_sesion);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        etCorreo = findViewById(R.id.etCorreoInicio);
        etContrasena = findViewById(R.id.etContrasenaInicio);
        btnEntrar = findViewById(R.id.btnEntrar);

        btnEntrar.setOnClickListener(new View.OnClickListener(){

            /**
             * Evento que lanza un hilo secundario para verificar el correo y contraseña
             * en la base de datos sin congelar la UI
             *
             * @param v La vista que fue pulsada
             * @return void no retorna valor alguno
             * @throws Exception No lanza excepciones controladas.
             */
            @Override
            public void onClick(View v){
                String correo = etCorreo.getText().toString().trim();
                String contrasena = etContrasena.getText().toString().trim();

                if (correo.isEmpty() || contrasena.isEmpty()){
                    Toast.makeText(InicioDeSesion.this, "Por favor, completa todos los campos", Toast.LENGTH_SHORT).show();
                    return;
                }

                Toast.makeText(InicioDeSesion.this, "Verificando datos", Toast.LENGTH_SHORT).show();

                java.util.concurrent.ExecutorService executor = java.util.concurrent.Executors.newSingleThreadExecutor();
                android.os.Handler handler = new android.os.Handler(android.os.Looper.getMainLooper());

                executor.execute(new Runnable() {
                    @Override
                    public void run() {
                        com.example.salvadordesemestres.vistas.db.UsuarioDbHelper dbHelper = new com.example.salvadordesemestres.vistas.db.UsuarioDbHelper(InicioDeSesion.this);
                        android.database.sqlite.SQLiteDatabase db = dbHelper.getReadableDatabase();

                        String[] projection = { com.example.salvadordesemestres.vistas.db.UsuarioContract.UsuarioEntry._ID };
                        String selection = com.example.salvadordesemestres.vistas.db.UsuarioContract.UsuarioEntry.COLUMN_CORREO + " = ? AND " +
                                com.example.salvadordesemestres.vistas.db.UsuarioContract.UsuarioEntry.COLUMN_CONTRASENA + " = ?";
                        String[] selectionArgs = { correo, contrasena };

                        android.database.Cursor cursor = db.query(
                                com.example.salvadordesemestres.vistas.db.UsuarioContract.UsuarioEntry.TABLE_NAME,
                                projection, selection, selectionArgs, null, null, null
                        );

                        final boolean existeUsuario = cursor.getCount() > 0;

                        cursor.close();
                        db.close();

                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                if (existeUsuario) {
                                    android.content.SharedPreferences preferencias = getSharedPreferences("MisSesiones", MODE_PRIVATE);
                                    android.content.SharedPreferences.Editor editor = preferencias.edit();
                                    editor.putBoolean("estaLogueado", true);
                                    editor.putString("correoUsuario", correo);
                                    editor.apply();

                                    Toast.makeText(InicioDeSesion.this, "Iniciando sesion", Toast.LENGTH_SHORT).show();

                                    android.content.Intent intent = new android.content.Intent(InicioDeSesion.this, PantallaPrincipal.class);
                                    startActivity(intent);
                                    finish();
                                } else {
                                    Toast.makeText(InicioDeSesion.this, "Correo o contraseña incorrectos", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }
                });
            }
        });
    }
}
