package com.example.auditoriasapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import kotlinx.android.synthetic.main.activity_menu_examenes.*

class menuExamenes : AppCompatActivity() {
    private lateinit var idU: String
    private lateinit var nomU: String
    private lateinit var Rol: String
    private lateinit var nombreUsuario:String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu_examenes)
        val examenesMenu = intent

        if (examenesMenu != null && examenesMenu.hasExtra("id_usuario") && examenesMenu.hasExtra("usuario") && examenesMenu.hasExtra("id_rol")) {
            idU = examenesMenu.getStringExtra("id_usuario")
            nomU = examenesMenu.getStringExtra("usuario")
            Rol = examenesMenu.getStringExtra("id_rol")
            nombreUsuario = examenesMenu.getStringExtra("nomusuario")
            txtNombreUs2.text="$nombreUsuario"
        }
    }
    fun ExamenAdminEjemplo(v: View){
        val exa = Intent(this,EjemploExamenAdmin::class.java)
        exa.putExtra("usuario",nomU)
        exa.putExtra("id_usuario",idU)
        exa.putExtra("id_rol",Rol)
        exa.putExtra("nomusuario", nombreUsuario)
        startActivity(exa)
        finish()
    }
    fun ExamenOperativoEjemplo(v: View){
        val exaOp = Intent(this,EjemploExamenOperativo::class.java)
        exaOp.putExtra("usuario",nomU)
        exaOp.putExtra("id_usuario",idU)
        exaOp.putExtra("id_rol",Rol)
        exaOp.putExtra("nomusuario", nombreUsuario)
        startActivity(exaOp)
        finish()
    }
    fun RegresarMenuPrin(v: View){
        val reg = Intent(this,menuActivity::class.java)
        startActivity(reg)
        finish()
    }
}