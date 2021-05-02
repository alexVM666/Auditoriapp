package com.example.auditoriasapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.annotation.DrawableRes
import kotlinx.android.synthetic.main.activity_resultados_examen_admin.*


class ActivityResultadosExamenAdmin : AppCompatActivity() {

    private lateinit var nomU: String
    private lateinit var nombreEmp : String
    private lateinit var fech: String
    var calificacion: Double = 0.0
    var califB1: Int = 0
    var califB2: Int = 0
    var califB3: Int = 0
    private lateinit var percentil: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_resultados_examen_admin)
        val examenResul = intent
        if (examenResul != null && examenResul.hasExtra("nombreRespondio") && examenResul.hasExtra("nombreAplico")&& examenResul.hasExtra("calificacion")) {
            nomU = examenResul.getStringExtra("nombreAplico")
            califB1 = examenResul.getIntExtra("calificacionB1",0)
            califB2 = examenResul.getIntExtra("calificacionB2",0)
            califB3 = examenResul.getIntExtra("calificacionB3",0)
            calificacion = examenResul.getDoubleExtra("calificacion",0.0)
            percentil = examenResul.getStringExtra("percentil")
            nombreEmp = examenResul.getStringExtra("nombreRespondio")
            fech = examenResul.getStringExtra("fecha")
            NombreEvaluadorExAdmin.text="$nomU"
            TextfechaResExAdmin.text = "$fech"
            idNombreExAdmin.text = "$nombreEmp"
            Resulbloque1ExAdmin.text = "$califB1"+" /5"
            Resulbloque2ExAdmin.text = "$califB2"+" /5"
            Resulbloque3ExAdmin.text = "$califB3"+" /5"
            idResultadoFinalExAdmin.text = "$calificacion"+" /5"
            idPercentilExAdmin.text = "$percentil"

            if(idPercentilExAdmin.text.equals("A")){
                caraResultExAdmin2.setImageResource(R.drawable.cheque)
            }
            if (idPercentilExAdmin.text.equals("NA")){
                caraResultExAdmin2.setImageResource(R.drawable.multiplicar)
            }
        }
    }
    fun exAdminGuardar (v: View){
        var terminar = Intent(this,menuActivity::class.java)
        startActivity(terminar)
        finish()
    }
}