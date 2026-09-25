package com.example.salvadordesemestres;

import android.content.Intent;
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

public class RegistroDeUsuarios extends AppCompatActivity {

    EditText etNombreUsuario;
    EditText etGmail;
    EditText etContrasena;
    EditText etTelefono;
    Button btnRegistrar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_registro_de_usuarios);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        etNombreUsuario = findViewById(R.id.NombreUsuario);
        etGmail = findViewById(R.id.Gmail);
        etContrasena = findViewById(R.id.Contrasena);
        etTelefono = findViewById(R.id.Telefono);
        btnRegistrar = findViewById(R.id.btnRegistrar);

        btnRegistrar.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                String nombre = etNombreUsuario.getText().toString().trim();
                String correo = etGmail.getText().toString().trim();
                String contrasena = etContrasena.getText().toString().trim();
                String telefono = etTelefono.getText().toString().trim();

                if (nombre.isEmpty() || correo.isEmpty() || contrasena.isEmpty() || telefono.isEmpty()){
                    Toast.makeText(RegistroDeUsuarios.this, "Por favor, completar todos los campos", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(RegistroDeUsuarios.this, "Registrado con exito", Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent(RegistroDeUsuarios.this, InicioDeSesion.class);
                    startActivity(intent);

                    finish();
                }
            }
        });
    }
}