package com.example.auditoriasapp

class UsuariosN(id_persona: Int, num_nomina: String, nombre: String, tiponomina: String,id_direccion: Int, id_departamento:Int,AgregadoP:Int) {
    var idpersona: Int = 0
    var nomi: String = ""
    var nom: String = ""
    var tipnom: String = ""
    var idDir: Int = 0
    var idDep: Int = 0
    var agrP: Int = 0

    init{
        this.idpersona = id_persona
        this.nomi = num_nomina
        this.nom = nombre
        this.tipnom = tiponomina
        this.idDir = id_direccion
        this.idDep = id_departamento
        this.agrP = AgregadoP
    }
}