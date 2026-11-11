package com.example.salvadordesemestres.vistas.vistas;

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
import com.example.salvadordesemestres.vistas.db.UsuarioDbHelper;

/**
 * Activity encargada de actualizar (Update) la contraseña del usuario activo en SQLite.
 * @param Ninguno
 * @return void no retorna nada
 * @throws Exception no cuenta con exceptiones controladas
 */
public class CambiarClave extends AppCompatActivity {

    EditText etClaveNueva;
    Button btnGuardarClave;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_cambiar_clave);
        getWindow().setFlags(android.view.WindowManager.LayoutParams.FLAG_SECURE, android.view.WindowManager.LayoutParams.FLAG_SECURE);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;

        });
        etClaveNueva = findViewById(R.id.etNuevaContrasena);
        btnGuardarClave = findViewById(R.id.btnActualizarClave);

        UsuarioDbHelper dbHelper = new UsuarioDbHelper(this);


        android.content.SharedPreferences preferencias = getSharedPreferences("MisSesiones", MODE_PRIVATE);
        String correoActual = preferencias.getString("correoUsuario", "");

        /**
         * Captura la nueva contraseña e invoca el metodo para actualizar en SQLite.
         * @param v La vista (botón) que fue pulsado
         * @return void no retorna valor alguno
         * @throws no cuenta con exceptiones controladas
         */
        btnGuardarClave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nuevaClave = etClaveNueva.getText().toString().trim();

                if (nuevaClave.isEmpty()) {
                    Toast.makeText(CambiarClave.this, "Escribe una contraseña", Toast.LENGTH_SHORT).show();
                    return;
                }

                int filasActualizadas = dbHelper.actualizarContrasena(correoActual, nuevaClave);

                if (filasActualizadas > 0) {
                    Toast.makeText(CambiarClave.this, "Contraseña actualizada", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(CambiarClave.this, "Error al actualizar", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}