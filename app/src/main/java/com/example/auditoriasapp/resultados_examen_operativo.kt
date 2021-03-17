package com.example.auditoriasapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import kotlinx.android.synthetic.main.activity_resultados_examen_operativo.*

class resultados_examen_operativo : AppCompatActivity() {
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
        setContentView(R.layout.activity_resultados_examen_operativo)
        val operaResul = intent
        if (operaResul != null && operaResul.hasExtra("nombreRespondio") && operaResul.hasExtra("nombreAplico")&& operaResul.hasExtra("calificacion")) {
            nomU = operaResul.getStringExtra("nombreAplico")
            califB1 = operaResul.getIntExtra("calificacionB1",0)
            califB2 = operaResul.getIntExtra("calificacionB2",0)
            califB3 = operaResul.getIntExtra("calificacionB3",0)
            calificacion = operaResul.getDoubleExtra("calificacion",0.0)
            percentil = operaResul.getStringExtra("percentil")
            nombreEmp = operaResul.getStringExtra("nombreRespondio")
            fech = operaResul.getStringExtra("fecha")
            NombreEvaluadorExOpera.text="$nomU"
            TextfechaResExOpera.text = "$fech"
            idNombreExOpera.text = "$nombreEmp"
            Resulbloque1ExOpera.text = "$califB1"+" /15"
            Resulbloque2ExOpera.text = "$califB2"+" /10"
            Resulbloque3ExOpera.text = "$califB3"+" /10"
            idResultadoFinalExOpera.text = "$calificacion"+" /10"
            idPercentilExOpera.text = "$percentil"

            if(idPercentilExOpera.text.equals("A")){
                caraResultExOperativo.setImageResource(R.drawable.bien)
            }
            if (idPercentilExOpera.text.equals("NA")){
                caraResultExOperativo.setImageResource(R.drawable.mal)
            }
        }
    }

    fun exOperaGuardar (v: View){
        val op = Intent(this,menuExamenes::class.java)
        startActivity(op)
        finish()
    }
}