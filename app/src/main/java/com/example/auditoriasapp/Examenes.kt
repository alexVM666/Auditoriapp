package com.example.auditoriasapp

class Examenes(c_examen: String,tipo_examen: String,id_persona: String,id_usuario: String,fecha_aplicacion: String,
               calif_uno: Int,calif_dos: Int,calif_tres: Int,resultado: Double,percentil: String,aManejando: Int,VehiculosD: String,num_nomina:String,usuario:String, nombre:String) {

    var c_exa: String = ""
    var t_examen: String = ""
    var idP: String = ""
    var idU: String = ""
    var fecha_apl: String = ""
    var calif_1: Int = 0
    var calif_2: Int = 0
    var calif_3: Int = 0
    var resul: Double = 0.0
    var perce:String = ""
    var aMane: Int = 0
    var Vehi: String = ""

    var nomi:String = ""
    var usu:String = ""
    var nomb:String = ""

    init {
        this.c_exa = c_examen
        this.t_examen = tipo_examen
        this.idP =  id_persona
        this.idU = id_usuario
        this.fecha_apl = fecha_aplicacion
        this.calif_1 = calif_uno
        this.calif_2 = calif_dos
        this.calif_3 = calif_tres
        this.resul = resultado
        this.perce = percentil
        this.aMane = aManejando
        this.Vehi = VehiculosD

        this.nomi = num_nomina
        this.usu = usuario
        this.nomb = nombre
    }
}