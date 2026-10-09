package com.example.salvadordesemestres.vistas.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class UsuarioDbHelper extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "SalvadroDeSemestres.db";

    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + UsuarioContract.UsuarioEntry.TABLE_NAME + " (" +
                    UsuarioContract.UsuarioEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    UsuarioContract.UsuarioEntry.COLUMN_NOMBRE + " TEXT NOT NULL, " +
                    UsuarioContract.UsuarioEntry.COLUMN_CORREO + " TEXT UNIQUE NOT NULL, " +
                    UsuarioContract.UsuarioEntry.COLUMN_CONTRASENA + " TEXT NOT NULL, " +
                    UsuarioContract.UsuarioEntry.CLUMN_TELEFONO + " TEXT)";

    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + UsuarioContract.UsuarioEntry.TABLE_NAME;

    public UsuarioDbHelper(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db){
        db.execSQL(SQL_CREATE_ENTRIES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
        db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);
    }
}
