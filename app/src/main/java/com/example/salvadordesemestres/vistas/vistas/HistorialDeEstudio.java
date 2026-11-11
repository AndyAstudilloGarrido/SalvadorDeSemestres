package com.example.salvadordesemestres.vistas.vistas;

import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.salvadordesemestres.R;

/**
 * Activity que dibuja dinámicamente una tabla (TableLayout) con todos los registros
 * de estudio almacenados en la lista global de DatosGlobales.
 * @param Ninguno
 * @return no retorna nada
 * @throws Exception no cuenta con exceptiones controladas.
 */

public class HistorialDeEstudio extends AppCompatActivity {

    TableLayout tablaHistorial;
    Button btnVolver;

    static int contadorID = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_historial_de_estudio);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        tablaHistorial = findViewById(R.id.tableHistorial);
        btnVolver = findViewById(R.id.btnVolverHistorial);

        for (int i = 0; i < DatosGlobales.historialCompleto.size(); i++){
            String registroString = DatosGlobales.historialCompleto.get(i);
            String[] partes = registroString.split("\\| ");

            if(partes.length == 3){
                String tema = partes[0];
                String desc = partes[1];
                String tiempo = partes[2];

                agregarFila(String.valueOf(i + 1), tema, desc, tiempo);
            }
        }

        /**
         * Cierra la pantalla del Historial y regresa al cronómetro.
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
    }

    /**
     * Explicación: Genera dinámicamente un TextView (celda).
     * @param texto El contenido en String que mostrará la celda.
     * @return TextView El elemento visual configurado.
     * @throws Exception no cuenta con exceptiones controladas.
     */
    private TextView crearCelda(String texto){
        TextView celda = new TextView(this);
        celda.setText(texto);
        celda.setTextColor(Color.parseColor("#333333"));
        celda.setPadding(8,8,8,8);
        celda.setGravity(Gravity.CENTER);
        celda.setBackgroundColor(Color.parseColor("#FFFFFF"));

        TableRow.LayoutParams params = new TableRow.LayoutParams(
                TableRow.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT

        );
        params.setMargins(1, 1, 1, 1);
        celda.setLayoutParams(params);

        return celda;
    }

    /**
     * Explicación: Crea un nuevo TableRow y le inserta celdas para armar la fila del historial.
     * @param id Identificador numérico de la fila, ramo Tema estudiado, descripcion Nota o aprendizaje ingresado por el usuario,
     * tiempo Duración de la sesión.
     * @return void No retorna ningún valor.
     * @throws Exception no cuenta con exceptiones controladas.
     */
    private void agregarFila(String id, String ramo, String descripcion, String tiempo){
        TableRow nuevaFila = new TableRow(this);
        nuevaFila.setBackgroundColor(Color.WHITE);

        nuevaFila.addView(crearCelda(id));
        nuevaFila.addView(crearCelda(ramo));
        nuevaFila.addView(crearCelda(descripcion));
        nuevaFila.addView(crearCelda(tiempo));

        tablaHistorial.addView(nuevaFila);

    }
}