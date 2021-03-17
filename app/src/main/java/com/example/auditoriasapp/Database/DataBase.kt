package com.example.auditoriasapp.Database

import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DataBase(context: Context):SQLiteOpenHelper(context, DATABASE,null,1) {
    companion object{
        val DATABASE = "auditorias"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL(
            "Create table usuario("+
                    "usuario text,"+
                    "id_usuario int not null primary key,"+
                    "contrasena text not null,"+
                    "id_rol int,"+
                    "nomusuario text not null)"
        )
        db?.execSQL(
            "Create table usuariosDisponibles("+
                    "usuario text,"+
                    "id_usuario int not null primary key,"+
                    "contrasena text not null,"+
                    "id_rol int,"+
                    "nomusuario text not null)"
        )
        db?.execSQL(
            "Create table persona("+
                    "id_persona int not null primary key,"+
                    "num_nomina text not null,"+
                    "nombre text,"+
                    "tiponomina text,"+
                    "id_direccion integer not null,"+
                    "id_departamento integer not null)"
        )
        db?.execSQL(
            "Create table personaNuevo("+
                    "id_persona INTEGER PRIMARY KEY AUTOINCREMENT,"+
                    "num_nomina text not null,"+
                    "nombre text,"+
                    "tiponomina text,"+
                    "id_direccion integer not null,"+
                    "id_departamento integer not null,"+
                    "AgregadoP integer not null)"
        )
        db?.execSQL(
            "Create table direccion("+
                    "id_direccion integer not null primary key,"+
                    "direccion text)"
        )
        db?.execSQL(
            "Create table departamento("+
                    "id_departamento integer not null primary key,"+
                    "departamento text not null,"+
                    "id_direccion integer)"
        )
        db?.execSQL(
            "Create table examen("+
                    "c_examen text not null primary key,"+
                    "tipo_examen text,"+
                    "aManejando int,"+
                    "VehiculosD text,"+
                    "id_persona integer not null,"+
                    "id_usuario integer not null,"+
                    "fecha_aplicacion text,"+
                    "calif_uno int,"+
                    "calif_dos int,"+
                    "calif_tres int,"+
                    "resultado real,"+
                    "percentil text)"
        )
        db?.execSQL(
            "Create table tipovehiculo("+
                    "id_vehiculo integer not null primary key,"+
                    "tipovehiculo text)"
        )
        db?.execSQL(
            "Create table carro("+
                    "id_carro int not null primary key,"+
                    "id_direccion integer not null," +
                    "id_departamento integer not null,"+
                    "id_vehiculo integer not null,"+
                    "id_persona integer not null,"+
                    "marca text,"+
                    "submarca text,"+
                    "modelo text,"+
                    "serie text," +
                    "motor text," +
                    "placas text," +
                    "inventario text,"+
                    "subtipo text)"
        )
        db?.execSQL(
            "Create table auditorias("+
                    "c_auditorias text not null primary key,"+
                    "t_auditoria text,"+
                    "id_persona integer not null,"+
                    "id_carro integer not null,"+
                    "fecha text,"+
                    "motor text,"+
                    "carroceria text,"+
                    "interior text,"+
                    "aditamentos text,"+
                    "equipo_tactico text,"+
                    "n_conformidades text,"+
                    "conclusion text,"+
                    "id_usuario integer not null,"+
                    "fechallantas text,"+
                    "cinturones text,"+
                    "bolsasAire text,"+
                    "testigosTablero text)"
        )
        db?.execSQL(
            "Create table auditoriasVolley("+
                    "c_auditorias text not null primary key,"+
                    "t_auditoria text,"+
                    "id_persona integer not null,"+
                    "id_carro integer not null,"+
                    "fecha text,"+
                    "motor text,"+
                    "carroceria text,"+
                    "interior text,"+
                    "aditamentos text,"+
                    "equipo_tactico text,"+
                    "n_conformidades text,"+
                    "conclusion text,"+
                    "id_usuario integer not null,"+
                    "fechallantas text,"+
                    "cinturones text,"+
                    "bolsasAire text,"+
                    "testigosTablero text)"
        )
        db?.execSQL(
            "Create table examenVolley("+
                    "c_examen text not null primary key,"+
                    "tipo_examen text,"+
                    "aManejando int,"+
                    "VehiculosD text,"+
                    "id_persona integer not null,"+
                    "id_usuario integer not null,"+
                    "fecha_aplicacion text,"+
                    "calif_uno int,"+
                    "calif_dos int,"+
                    "calif_tres int,"+
                    "resultado real,"+
                    "percentil text)"
        )
        db?.execSQL(
            "Create table chequeo("+
                    "c_chequeo text not null primary key,"+
                    "id_persona int not null,"+
                    "id_carro int not null,"+
                    "id_usuario int not null,"+
                    "tipo_revision text,"+
                    "tipo_mantenimiento text,"+
                    "kilometraje_actual int,"+
                    "fecha_actual text,"+
                    "kilometraje_anterior int,"+
                    "fecha_anterior text,"+
                    "proxima_revision text,"+
                    "observaciones text)"
        )
        db?.execSQL(
            "Create table chequeoVolley("+
                    "c_chequeo text not null primary key,"+
                    "id_persona int not null,"+
                    "id_carro int not null,"+
                    "id_usuario int not null,"+
                    "tipo_revision text,"+
                    "tipo_mantenimiento text,"+
                    "kilometraje_actual int,"+
                    "fecha_actual text,"+
                    "kilometraje_anterior int,"+
                    "fecha_anterior text,"+
                    "proxima_revision text,"+
                    "observaciones text)"
        )
    }
    fun Ejecuta(sentencia: String):Boolean{
        try {
            val db = this.writableDatabase
            db.execSQL(sentencia)
            db.close()
            return true
        }catch (ex:Exception){
            return false
        }
    }
    fun Consulta(query:String):Cursor?{
        try {
            val db = this.readableDatabase
            return db.rawQuery(query,null)
        }catch (ex:Exception){
            return null
        }
    }
    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
    }
}