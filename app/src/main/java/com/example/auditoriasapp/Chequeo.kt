package com.example.auditoriasapp

class Chequeo(c_chequeo: String,id_persona: Int,id_carro: Int,id_usuario: Int,tipo_revision: String,tipo_mantenimiento: String,
              kilometraje_actual: Int,fecha_actual: String,kilometraje_anterior: Int,fecha_anterior: String,proxima_revision:String,
              observaciones:String, num_nomina:String,inventario:String,usuario:String,nombre:String)
{
    var c_che: String = ""
    var pers: Int = 0
    var carro: Int = 0
    var usuario: Int = 0
    var tipr: String = ""
    var tipm: String = ""
    var kmAct: Int = 0
    var fech_Act: String = ""
    var kmAnt: Int = 0
    var fechAnt: String = ""
    var prox_rv: String = ""
    var obs: String = ""

    var nomi:String = ""
    var inv:String = ""
    var usu:String = ""
    var nomb: String = ""

    init {
        this.c_che = c_chequeo
        this.pers = id_persona
        this.carro = id_carro
        this.usuario = id_usuario
        this.tipr = tipo_revision
        this.tipm = tipo_mantenimiento
        this.kmAct = kilometraje_actual
        this.fech_Act = fecha_actual
        this.kmAnt = kilometraje_anterior
        this.fechAnt = fecha_anterior
        this.prox_rv = proxima_revision
        this.obs = observaciones

        this.nomi = num_nomina
        this.inv = inventario
        this.usu = usuario
        this.nomb = nombre
    }
}