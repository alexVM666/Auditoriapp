package com.example.auditoriasapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_menu__resultados_volley.*
import kotlinx.android.synthetic.main.activity_subir_datos_menu.*

class SubirDatosMenu : AppCompatActivity() {
    private lateinit var idU: String
    private lateinit var nomU: String
    private lateinit var Rol: String
    private lateinit var nombreUsuario:String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_subir_datos_menu)
        val subir = intent
        if (subir !=null && subir.hasExtra("usuario") && subir.hasExtra("id_usuario")){
            nomU =  subir.getStringExtra("usuario")
            idU =  subir.getStringExtra("id_usuario")
            Rol =  subir.getStringExtra("id_rol")
            nombreUsuario = subir.getStringExtra("nomusuario")
            txtNombreUs3.text = "$nombreUsuario"
        }
        txtRegresar3.setOnClickListener{
            regresar3()
        }
        txtSubirCuestionarios.setOnClickListener {
            Examen()
        }
        txtAuditoriasSubir.setOnClickListener {
            Audit()
        }
        txtUsuariosN.setOnClickListener {
           usuariosN()
        }
        txtSubirChequeos.setOnClickListener {
            Chequeos()
        }

    }
    private fun usuariosN(){
        if (Rol.equals("1")){
            val usuariosNU = Intent(this,ActivityRecyclerUN::class.java)
            usuariosNU.putExtra("id_usuario",idU)
            startActivity(usuariosNU)
            finish()
        }else{
            Toast.makeText(this, "No tienes Acceso.", Toast.LENGTH_SHORT).show()
        }
    }
    private fun Audit(){
        if (Rol.equals("1")){
            val audit = Intent(this,RecyclerAuditorias::class.java)
            audit.putExtra("id_usuario",idU)
            startActivity(audit)
            finish()
        }else{
            Toast.makeText(this, "No tienes Acceso.", Toast.LENGTH_SHORT).show()
        }
    }
    private fun Examen(){
        val examen = Intent(this, ActivityRecyclerExamenes::class.java)
        examen.putExtra("id_usuario",idU)
        startActivity(examen)
        finish()
    }
    private fun Chequeos(){
        val chequeos = Intent(this, ActivityRecyclerChequeo::class.java)
        chequeos.putExtra("id_usuario",idU)
        startActivity(chequeos)
        finish()
    }
    private fun regresar3(){
        val regresar3 = Intent(this, menuActivity::class.java)
        startActivity(regresar3)
        finish()
    }
}