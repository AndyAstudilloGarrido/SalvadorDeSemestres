package com.example.salvadordesemestres.vistas.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Clase Helper para crear, estructurar y actualizar la base de datos SQLite
 * @param Ninguno
 * @return void no retorna nada.
 * @throws Exception no cuenta con excepciones controladas.
 */
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


    /**
     * Constructor del helper que establece la conexión con la base de datos.
     * @param context El contexto de la Activity que invoca la base de datos.
     * @return void no retorna nada.
     * @throws Exception no cuenta con excepciones controladas.
     */
    public UsuarioDbHelper(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    /**
     * Ejecuta la sentencia SQL para crear la tabla de usuarios al instalar la app.
     * @param db La instancia de la base de datos SQLite.
     * @return void no retorna nada.
     * @throws Exception no cuenta con excepciones controladas.
     */
    @Override
    public void onCreate(SQLiteDatabase db){
        db.execSQL(SQL_CREATE_ENTRIES);
    }

    /**
     * Recrea las tablas si se detecta una nueva versión de la base de datos.
     * @param db La instancia de la base de datos SQLite, oldVersion Número de versión anterior, newVersion Número de versión nueva.
     * @return void no retorna nada.
     * @throws Exception no cuenta con excepciones controladas.
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
        db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);
    }

    /**
     * Realiza la operación Update modificando la contraseña del usuario en SQLite.
     * @param correo Correo del usuario como filtro WHERE, nuevaContrasena La nueva contraseña a registrar.
     * @return int Cantidad de filas afectadas en la base de datos.
     * @throws Exception no cuenta con excepciones controladas.
     */
    public int actualizarContrasena(String correo, String nuevaContrasena){
        SQLiteDatabase db = this.getWritableDatabase();
        android.content.ContentValues values = new android.content.ContentValues();

        String claveEncriptada = encriptarPBKDF2(nuevaContrasena);
        values.put(UsuarioContract.UsuarioEntry.COLUMN_CONTRASENA, claveEncriptada);

        String selection = UsuarioContract.UsuarioEntry.COLUMN_CORREO + " = ?";
        String[] selectionArgs = { correo };

        int count = db.update(UsuarioContract.UsuarioEntry.TABLE_NAME, values, selection, selectionArgs);
        db.close();
        return count;
    }

    /**
     * Realiza un barrodado logico en lugar de un borrado fisico, mantiene la integridad la integridad de la base de datos..
     * @param correo Correo del usuario a desactivar.
     * @return int Cantidad de filas eliminadas en la base de datos.
     * @throws Exception no cuenta con excepciones controladas.
     */
    public int eliminarUsuario(String correo){
        SQLiteDatabase db = this.getWritableDatabase();
        android.content.ContentValues values = new android.content.ContentValues();

        values.put(UsuarioContract.UsuarioEntry.COLUMN_NOMBRE, "Usuario Eliminado");
        values.put(UsuarioContract.UsuarioEntry.COLUMN_CONTRASENA, "INACTIVO_DESACTIVADO");
        values.putNull(UsuarioContract.UsuarioEntry.CLUMN_TELEFONO);

        String selection = UsuarioContract.UsuarioEntry.COLUMN_CORREO + " = ?";
        String[] selectionArgs = { correo };

        int count = db.update(UsuarioContract.UsuarioEntry.TABLE_NAME, values, selection, selectionArgs);
        db.close();
        return count;
    }

    /**
     * Encripta una contraseña utilizando el algoritmo PBKDF2 con HmacSHA256 y 10,000 iteraciones.
     * @param contrasena La contraseña ingresada por el usuario en texto plano.
     * @return String La contraseña encriptada en formato hexadecimal PBKDF2.
     * @throws Exception no cuenta con excepciones controladas
     */
    public static String encriptarPBKDF2(String contrasena) {
        try {
            char[] passwordChars = contrasena.toCharArray();
            byte[] salt = "SalvadorDeSemestresSalt2026".getBytes(java.nio.charset.StandardCharsets.UTF_8);
            int iterations = 10000;
            int keyLength = 256;

            javax.crypto.spec.PBEKeySpec spec = new javax.crypto.spec.PBEKeySpec(passwordChars, salt, iterations, keyLength);
            javax.crypto.SecretKeyFactory skf = javax.crypto.SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
            byte[] hash = skf.generateSecret(spec).getEncoded();

            StringBuilder hexString = new StringBuilder();
            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (Exception e) {
            return contrasena;
        }
    }
}
