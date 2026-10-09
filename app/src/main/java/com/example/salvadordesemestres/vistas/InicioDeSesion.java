package com.example.salvadordesemestres.vistas;

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
            @Override
            public void onClick(View v){
                String correo = etCorreo.getText().toString().trim();
                String contrasena = etContrasena.getText().toString().trim();

                if (correo.isEmpty() || contrasena.isEmpty()){
                    Toast.makeText(InicioDeSesion.this, "Por favor, complete todos los campos", Toast.LENGTH_SHORT).show();
                    return;
                }

                com.example.salvadordesemestres.vistas.db.UsuarioDbHelper dbHelper = new com.example.salvadordesemestres.vistas.db.UsuarioDbHelper(InicioDeSesion.this);
                android.database.sqlite.SQLiteDatabase db = dbHelper.getReadableDatabase();

                String [] projection = {
                  UsuarioContract.UsuarioEntry._ID
                };

                String selection = UsuarioContract.UsuarioEntry.COLUMN_CORREO + " = ? AND "+
                        UsuarioContract.UsuarioEntry.COLUMN_CONTRASENA + " = ?";

                String[] selectionArgs = { correo, contrasena};

                android.database.Cursor cursor = db.query(
                  UsuarioContract.UsuarioEntry.TABLE_NAME,
                  projection,
                  selection,
                  selectionArgs,
                  null, null, null
                );

                if (cursor.getCount() > 0){
                    Toast.makeText(InicioDeSesion.this, "Sesión iniciada", Toast.LENGTH_SHORT).show();

                    android.content.SharedPreferences preferences = getSharedPreferences("MisSesiones", MODE_PRIVATE);
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putBoolean("estaLogueado", true);
                    editor.putString("correoUsuario", correo);
                    editor.apply();

                    Intent intent = new Intent(InicioDeSesion.this, PantallaPrincipal.class);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(InicioDeSesion.this, "Correo o contraseña incorrectos", Toast.LENGTH_SHORT).show();
                }

                cursor.close();
                db.close();
            }
        });
    }
}
