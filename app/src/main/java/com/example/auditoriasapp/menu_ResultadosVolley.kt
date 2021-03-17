package com.example.auditoriasapp



import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_auditoria.*
import kotlinx.android.synthetic.main.activity_menu__resultados_volley.*

class menu_ResultadosVolley : AppCompatActivity() {
    private lateinit var idU: String
    private lateinit var nomU: String
    private lateinit var nombreUsuario:String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu__resultados_volley)
        val resul = intent
        if(resul != null && resul.hasExtra("id_usuario") && resul.hasExtra("usuario")){
            idU = resul.getStringExtra("id_usuario")
            nomU =  resul.getStringExtra("usuario")
            nombreUsuario = resul.getStringExtra("nomusuario")
            txtNombreUsrResul.text = "$nombreUsuario"
        }
        auditoriasResul.setOnClickListener {
            val audit = Intent(this,Activity_Volley_Auditorias::class.java)
            startActivity(audit)
            finish()
        }
        cuestionariosResul.setOnClickListener {
            val cuest = Intent(this,Activity_Volley_Examenes::class.java)
            startActivity(cuest)
            finish()
        }
        regresarmenup.setOnClickListener {
            val reg = Intent(this,menuActivity::class.java)
            startActivity(reg)
            finish()
        }
        Revisiones.setOnClickListener {
            val rev = Intent(this,Activity_Volley_Chequeo::class.java)
            startActivity(rev)
            finish()
        }
    }

}