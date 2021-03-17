package com.example.auditoriasapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_menu_auditorias.*

class MenuAuditorias : AppCompatActivity() {
    private lateinit var idU: String
    private lateinit var nomU: String
    private lateinit var nombreUsuario:String
    private lateinit var Rol:String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu_auditorias)
        val audi = intent
        if (audi != null && audi.hasExtra("id_usuario") && audi.hasExtra("usuario")){
            idU = audi.getStringExtra("id_usuario")
            nomU = audi.getStringExtra("usuario")
            nombreUsuario = audi.getStringExtra("nomusuario")
            Rol = audi.getStringExtra("id_rol")
            txtNombreUsr.text = "$nombreUsuario"
        }
    }
    fun auditoriaIndividual(v: View){
        if (Rol.equals("1")){
            val audiI = Intent(this,AuditoriaActivity::class.java)
            audiI.putExtra("id_usuario",idU)
            audiI.putExtra("usuario",nomU)
            audiI.putExtra("nomusuario",nombreUsuario)
            audiI.putExtra("id_rol",Rol)
            startActivity(audiI)
            finish()
        }else{
            Toast.makeText(this, "No tienes Acceso.", Toast.LENGTH_SHORT).show();
        }
    }
    fun Chequeo(v: View){
        val check = Intent(this,ActivityChequeo::class.java)
        check.putExtra("id_usuario",idU)
        check.putExtra("usuario",nomU)
        check.putExtra("nomusuario",nombreUsuario)
        check.putExtra("id_rol",Rol)
        startActivity(check)
        finish()
    }
    fun VolverMenuPrincipal(v: View){
        val audi = Intent(this,menuActivity::class.java)
        startActivity(audi)
        finish()
    }

}