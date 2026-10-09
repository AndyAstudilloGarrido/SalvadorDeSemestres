package com.example.salvadordesemestres.vistas.db;

import android.provider.BaseColumns;

public final class UsuarioContract {

    private UsuarioContract() {}

    public static class UsuarioEntry implements BaseColumns{
        public static final String TABLE_NAME = "usuarios";

        public static final String COLUMN_NOMBRE = "nombre";
        public static final String COLUMN_CORREO = "correo";

        public static final String COLUMN_CONTRASENA = "contrasena";

        public static final String CLUMN_TELEFONO = "telefono";
    }
}
