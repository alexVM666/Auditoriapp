package com.example.auditoriasapp

class Auditorias(c_auditorias: String,t_auditoria: String, id_persona: String, id_carro: String,id_usuario:String, fecha: String, motor: String,
                 carroceria: String, interior: String, aditamentos: String, equipo_tactico: String, n_conformidades: String,
                 conclusion: String,fechallantas:String,cinturones:String,bolsasAire:String,testigosTablero:String,num_nomina:String,inventario:String,usuario:String,nombre:String) {

    var c_audi: String = ""
    var t_audi: String = ""
    var idP: String = ""
    var idCar: String = ""
    var idUs:String = ""
    var fechaa: String = ""
    var motorr: String = ""
    var carroc: String = ""
    var inter: String = ""
    var adit: String = ""
    var equipo: String = ""
    var n_confor: String = ""
    var conclu: String = ""
    var llantas:String = ""
    var cinturones:String = ""
    var bolsasA: String = ""
    var testigos: String = ""

    var nomi:String = ""
    var inv:String = ""
    var usu:String = ""
    var nomb:String = ""

    init{
        this.c_audi = c_auditorias
        this.t_audi = t_auditoria
        this.idP = id_persona
        this.idCar = id_carro
        this.fechaa = fecha
        this.motorr = motor
        this.carroc = carroceria
        this.inter = interior
        this.adit = aditamentos
        this.equipo = equipo_tactico
        this.n_confor = n_conformidades
        this.conclu = conclusion
        this.idUs = id_usuario
        this.llantas = fechallantas
        this.cinturones = cinturones
        this.bolsasA = bolsasAire
        this.testigos = testigosTablero

        this.nomi = num_nomina
        this.inv = inventario
        this.usu = usuario
        this.nomb = nombre
    }
}