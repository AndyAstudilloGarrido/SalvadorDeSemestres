package com.example.salvadordesemestres.vistas.db;

import android.provider.BaseColumns;

/**
 * clase que define la estructura de la tabla de usuarios y sus columnas en la base de datos SQLite
 * @param Ninguno
 * @return void no retorna nada.
 * @throws Exception no cuenta con excepciones controladas.
 */
public final class UsuarioContract {

    private UsuarioContract() {}

    /**
     * Clase interna que define el nombre de la tabla 'usuarios' y los nombres
     * de cada una de sus columnas heredando la clave primaria _ID.
     * @param Ninguno
     * @return void no retorna nada.
     * @throws Exception no cuenta con excepciones controladas.
     */
    public static class UsuarioEntry implements BaseColumns{
        public static final String TABLE_NAME = "usuarios";

        public static final String COLUMN_NOMBRE = "nombre";
        public static final String COLUMN_CORREO = "correo";

        public static final String COLUMN_CONTRASENA = "contrasena";

        public static final String CLUMN_TELEFONO = "telefono";
    }


    public static class SesionEntry implements BaseColumns {
        public static final String TABLE_NAME = "sesiones_estudio";

        public static final String COLUMN_CORREO_USUARIO = "correo_usuario";
        public static final String COLUMN_TEMA = "tema";
        public static final String COLUMN_DESCRIPCION = "descripcion";
        public static final String COLUMN_TIEMPO = "tiempo";
    }
}
