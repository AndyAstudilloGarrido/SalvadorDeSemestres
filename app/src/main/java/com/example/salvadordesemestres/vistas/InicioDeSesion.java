package com.example.salvadordesemestres.vistas;

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

import com.example.salvadordesemestres.R;

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
                } else {

                    Intent intent = new Intent(InicioDeSesion.this, PantallaPrincipal.class);
                    startActivity(intent);
                    finish();
                }
            }
        });
    }
}
