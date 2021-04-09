package com.example.auditoriasapp

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.view.View
import android.widget.Button
import android.widget.RadioButton
import android.widget.Toast
import com.example.auditoriasapp.Database.DataBase
import kotlinx.android.synthetic.main.activity_examen__operativo.*
import java.math.RoundingMode
import java.text.DecimalFormat

class Examen_Operativo : AppCompatActivity() {
    private lateinit var idU: String
    private lateinit var nomU: String
    var id_persona : Int = 0
    private lateinit var tipoNomina : String
    private lateinit var nombreEmp : String
    private lateinit var area : String
    private lateinit var fech: String
    private lateinit var vehiculosD : String
    private lateinit var tipoE: String
    private lateinit var aManejando: String

    private lateinit var countdowntimer: CountDownTimer

    var calificacion: Double = 0.0
    var califB1: Int = 0
    var califB2: Int = 0
    var califB3: Int = 0
    var percentil: String = ""
    var c_examen: String = ""
    val df = DecimalFormat("#.##")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_examen__operativo)
        val opera = intent
        if (opera != null && opera.hasExtra("id_usuario") && opera.hasExtra("aplico")&& opera.hasExtra("id_persona")) {
            tipoE = opera.getStringExtra("tipoExamen")
            id_persona = opera.getIntExtra("id_persona",0)
            nombreEmp = opera.getStringExtra("nombreEmp")
            idU = opera.getStringExtra("id_usuario")
            nomU = opera.getStringExtra("aplico")
            area = opera.getStringExtra("area")
            vehiculosD = opera.getStringExtra("vehiculosD")
            tipoNomina = opera.getStringExtra("tipoNomina")
            aManejando = opera.getStringExtra("aManejendo")
            fech = opera.getStringExtra("fechaAplicacion")
            c_examen = opera.getStringExtra("c_examen")
        }
        //tiempo: 20 minutos
        reverseTimer(Seconds = 1200)
    }



    fun reverseTimer(Seconds: Int) {
        countdowntimer = object : CountDownTimer((Seconds * 1000 + 1000).toLong(), 1000) {
            override fun onTick(millisUntilFinished: Long) {
                var seconds = (millisUntilFinished / 1000).toInt()
                val minutes = seconds / 60
                seconds = seconds % 60
                RelojExOpera.text = "Tiempo Restante: " + String.format("%02d", minutes) + ":" + String.format(
                    "%02d" + " Minutos.",
                    seconds
                )
            }
            override fun onFinish() {
                RelojExOpera.text = "Terminado"
                exOperaResul()
            }
        }.start()
    }
    private fun pauseTimer(){
        countdowntimer.cancel()
    }
    //pregunta 1
    fun Pregunta1op(view: View) {
        if (view is RadioButton) {
            val checked = view.isChecked
            when (view.getId()) {
                R.id.Pre1Op1ExaOpera ->
                    if (checked) {
                        califB1++
                        Toast.makeText(this, "Correcta", Toast.LENGTH_SHORT).show()
                        Pre1Op1ExaOpera.setBackgroundColor(Color.rgb(0, 143, 57))//verde
                        Pre1Op1ExaOpera.isEnabled = false
                        Pre1Op2ExaOpera.isEnabled = false
                        Pre1Op3ExaOpera.isEnabled = false
                        Pre1Op4ExaOpera.isEnabled = false
                    }
                R.id.Pre1Op2ExaOpera ->
                    if (checked) {
                        Toast.makeText(this, "Incorrecto", Toast.LENGTH_SHORT).show();
                        Pre1Op2ExaOpera.setBackgroundColor(Color.rgb(255, 0, 0))//rojo
                        Pre1Op1ExaOpera.isEnabled = false
                        Pre1Op2ExaOpera.isEnabled = false
                        Pre1Op3ExaOpera.isEnabled = false
                        Pre1Op4ExaOpera.isEnabled = false
                    }
                R.id.Pre1Op3ExaOpera ->
                    if (checked) {
                        Toast.makeText(this, "Incorrecto", Toast.LENGTH_SHORT).show();
                        Pre1Op3ExaOpera.setBackgroundColor(Color.rgb(255, 0, 0))//rojo
                        Pre1Op1ExaOpera.isEnabled = false
                        Pre1Op2ExaOpera.isEnabled = false
                        Pre1Op3ExaOpera.isEnabled = false
                        Pre1Op4ExaOpera.isEnabled = false
                    }
                R.id.Pre1Op4ExaOpera ->
                    if (checked) {
                        Toast.makeText(this, "Incorrecto", Toast.LENGTH_SHORT).show()
                        Pre1Op4ExaOpera.setBackgroundColor(Color.rgb(255, 0, 0))//rojo
                        Pre1Op1ExaOpera.isEnabled = false
                        Pre1Op2ExaOpera.isEnabled = false
                        Pre1Op3ExaOpera.isEnabled = false
                        Pre1Op4ExaOpera.isEnabled = false
                    }
            }
        }
    }
    //pregunta 2
    fun Pregunta2op(view: View) {
        if (view is RadioButton) {
            val checked = view.isChecked
            when (view.getId()) {
                R.id.Pre2Op1ExaOpera ->
                    if (checked) {
                        Toast.makeText(this, "Incorrecto", Toast.LENGTH_SHORT).show();
                        Pre2Op1ExaOpera.setBackgroundColor(Color.rgb(255, 0, 0))//rojo
                        Pre2Op1ExaOpera.isEnabled = false
                        Pre2Op2ExaOpera.isEnabled = false
                        Pre2Op3ExaOpera.isEnabled = false
                        Pre2Op4ExaOpera.isEnabled = false
                    }
                R.id.Pre2Op2ExaOpera ->
                    if (checked) {
                        califB1++
                        Toast.makeText(this, "Correcta", Toast.LENGTH_SHORT).show()
                        Pre2Op2ExaOpera.setBackgroundColor(Color.rgb(0, 143, 57))//verde
                        Pre2Op1ExaOpera.isEnabled = false
                        Pre2Op2ExaOpera.isEnabled = false
                        Pre2Op3ExaOpera.isEnabled = false
                        Pre2Op4ExaOpera.isEnabled = false
                    }
                R.id.Pre2Op3ExaOpera ->
                    if (checked) {
                        Toast.makeText(this, "Incorrecto", Toast.LENGTH_SHORT).show();
                        Pre2Op3ExaOpera.setBackgroundColor(Color.rgb(255, 0, 0))//rojo
                        Pre2Op1ExaOpera.isEnabled = false
                        Pre2Op2ExaOpera.isEnabled = false
                        Pre2Op3ExaOpera.isEnabled = false
                        Pre2Op4ExaOpera.isEnabled = false
                    }
                R.id.Pre2Op4ExaOpera ->
                    if (checked) {
                        Toast.makeText(this, "Incorrecto", Toast.LENGTH_SHORT).show()
                        Pre2Op4ExaOpera.setBackgroundColor(Color.rgb(255, 0, 0))//rojo
                        Pre2Op1ExaOpera.isEnabled = false
                        Pre2Op2ExaOpera.isEnabled = false
                        Pre2Op3ExaOpera.isEnabled = false
                        Pre2Op4ExaOpera.isEnabled = false
                    }
            }
        }
    }
    //pregunta 3
    fun Pregunta3op(view: View) {
        if (view is RadioButton) {
            val checked = view.isChecked
            when (view.getId()) {
                R.id.Pre3Op1ExaOpera ->
                    if (checked) {
                        califB1++
                        Toast.makeText(this, "Correcta", Toast.LENGTH_SHORT).show()
                        Pre3Op1ExaOpera.setBackgroundColor(Color.rgb(0, 143, 57))//verde
                        Pre3Op1ExaOpera.isEnabled = false
                        Pre3Op2ExaOpera.isEnabled = false
                        Pre3Op3ExaOpera.isEnabled = false
                        Pre3Op4ExaOpera.isEnabled = false
                    }
                R.id.Pre3Op2ExaOpera ->
                    if (checked) {
                        Toast.makeText(this, "Incorrecto", Toast.LENGTH_SHORT).show();
                        Pre3Op2ExaOpera.setBackgroundColor(Color.rgb(255, 0, 0))//rojo
                        Pre3Op1ExaOpera.isEnabled = false
                        Pre3Op2ExaOpera.isEnabled = false
                        Pre3Op3ExaOpera.isEnabled = false
                        Pre3Op4ExaOpera.isEnabled = false
                    }
                R.id.Pre3Op3ExaOpera ->
                    if (checked) {
                        Toast.makeText(this, "Incorrecto", Toast.LENGTH_SHORT).show();
                        Pre3Op3ExaOpera.setBackgroundColor(Color.rgb(255, 0, 0))//rojo
                        Pre3Op1ExaOpera.isEnabled = false
                        Pre3Op2ExaOpera.isEnabled = false
                        Pre3Op3ExaOpera.isEnabled = false
                        Pre3Op4ExaOpera.isEnabled = false
                    }
                R.id.Pre3Op4ExaOpera ->
                    if (checked) {
                        Toast.makeText(this, "Incorrecto", Toast.LENGTH_SHORT).show()
                        Pre3Op4ExaOpera.setBackgroundColor(Color.rgb(255, 0, 0))//rojo
                        Pre3Op1ExaOpera.isEnabled = false
                        Pre3Op2ExaOpera.isEnabled = false
                        Pre3Op3ExaOpera.isEnabled = false
                        Pre3Op4ExaOpera.isEnabled = false
                    }
            }
        }
    }
    fun Pregunta4op(view: View) {
        if (view is RadioButton) {
            val checked = view.isChecked
            when (view.getId()) {
                R.id.Pre4Op1ExaOpera ->
                    if (checked) {
                        Toast.makeText(this, "Incorrecto", Toast.LENGTH_SHORT).show();
                        Pre4Op1ExaOpera.setBackgroundColor(Color.rgb(255, 0, 0))//rojo
                        Pre4Op1ExaOpera.isEnabled = false
                        Pre4Op2ExaOpera.isEnabled = false
                        Pre4Op3ExaOpera.isEnabled = false
                        Pre4Op4ExaOpera.isEnabled = false
                    }
                R.id.Pre4Op2ExaOpera ->
                    if (checked) {
                        Toast.makeText(this, "Incorrecto", Toast.LENGTH_SHORT).show();
                        Pre4Op2ExaOpera.setBackgroundColor(Color.rgb(255, 0, 0))//rojo
                        Pre4Op1ExaOpera.isEnabled = false
                        Pre4Op2ExaOpera.isEnabled = false
                        Pre4Op3ExaOpera.isEnabled = false
                        Pre4Op4ExaOpera.isEnabled = false
                    }
                R.id.Pre4Op3ExaOpera ->
                    if (checked) {
                        califB1++
                        Toast.makeText(this, "Correcta", Toast.LENGTH_SHORT).show()
                        Pre4Op3ExaOpera.setBackgroundColor(Color.rgb(0, 143, 57))//verde
                        Pre4Op1ExaOpera.isEnabled = false
                        Pre4Op2ExaOpera.isEnabled = false
                        Pre4Op3ExaOpera.isEnabled = false
                        Pre4Op4ExaOpera.isEnabled = false
                    }
                R.id.Pre4Op4ExaOpera ->
                    if (checked) {
                        Toast.makeText(this, "Incorrecto", Toast.LENGTH_SHORT).show()
                        Pre4Op4ExaOpera.setBackgroundColor(Color.rgb(255, 0, 0))//rojo
                        Pre4Op1ExaOpera.isEnabled = false
                        Pre4Op2ExaOpera.isEnabled = false
                        Pre4Op3ExaOpera.isEnabled = false
                        Pre4Op4ExaOpera.isEnabled = false
                    }
            }
        }
    }
    fun Pregunta5op(view: View) {
        if (view is RadioButton) {
            val checked = view.isChecked
            when (view.getId()) {
                R.id.Pre5Op1ExaOpera ->
                    if (checked) {
                        Toast.makeText(this, "Incorrecto", Toast.LENGTH_SHORT).show();
                        Pre5Op1ExaOpera.setBackgroundColor(Color.rgb(255, 0, 0))//rojo
                        Pre5Op1ExaOpera.isEnabled = false
                        Pre5Op2ExaOpera.isEnabled = false
                        Pre5Op3ExaOpera.isEnabled = false
                        Pre5Op4ExaOpera.isEnabled = false
                    }
                R.id.Pre5Op2ExaOpera ->
                    if (checked) {
                        califB1++
                        Toast.makeText(this, "Correcta", Toast.LENGTH_SHORT).show()
                        Pre5Op2ExaOpera.setBackgroundColor(Color.rgb(0, 143, 57))//verde
                        Pre5Op1ExaOpera.isEnabled = false
                        Pre5Op2ExaOpera.isEnabled = false
                        Pre5Op3ExaOpera.isEnabled = false
                        Pre5Op4ExaOpera.isEnabled = false
                    }
                R.id.Pre5Op3ExaOpera ->
                    if (checked) {
                        Toast.makeText(this, "Incorrecto", Toast.LENGTH_SHORT).show();
                        Pre5Op3ExaOpera.setBackgroundColor(Color.rgb(255, 0, 0))//rojo
                        Pre5Op1ExaOpera.isEnabled = false
                        Pre5Op2ExaOpera.isEnabled = false
                        Pre5Op3ExaOpera.isEnabled = false
                        Pre5Op4ExaOpera.isEnabled = false
                    }
                R.id.Pre5Op4ExaOpera ->
                    if (checked) {
                        Toast.makeText(this, "Incorrecto", Toast.LENGTH_SHORT).show()
                        Pre5Op4ExaOpera.setBackgroundColor(Color.rgb(255, 0, 0))//rojo
                        Pre5Op1ExaOpera.isEnabled = false
                        Pre5Op2ExaOpera.isEnabled = false
                        Pre5Op3ExaOpera.isEnabled = false
                        Pre5Op4ExaOpera.isEnabled = false
                    }
            }
        }
    }
    fun Pregunta6op(view: View) {
        if (view is RadioButton) {
            val checked = view.isChecked
            when (view.getId()) {
                R.id.Pre6Op1ExaOpera ->
                    if (checked) {
                        Toast.makeText(this, "Incorrecto", Toast.LENGTH_SHORT).show();
                        Pre6Op1ExaOpera.setBackgroundColor(Color.rgb(255, 0, 0))//rojo
                        Pre6Op1ExaOpera.isEnabled = false
                        Pre6Op2ExaOpera.isEnabled = false
                        Pre6Op3ExaOpera.isEnabled = false
                        Pre6Op4ExaOpera.isEnabled = false
                    }
                R.id.Pre6Op2ExaOpera ->
                    if (checked) {
                        califB1++
                        Toast.makeText(this, "Correcta", Toast.LENGTH_SHORT).show()
                        Pre6Op2ExaOpera.setBackgroundColor(Color.rgb(0, 143, 57))//verde
                        Pre6Op1ExaOpera.isEnabled = false
                        Pre6Op2ExaOpera.isEnabled = false
                        Pre6Op3ExaOpera.isEnabled = false
                        Pre6Op4ExaOpera.isEnabled = false
                    }
                R.id.Pre6Op3ExaOpera ->
                    if (checked) {
                        Toast.makeText(this, "Incorrecto", Toast.LENGTH_SHORT).show();
                        Pre6Op3ExaOpera.setBackgroundColor(Color.rgb(255, 0, 0))//rojo
                        Pre6Op1ExaOpera.isEnabled = false
                        Pre6Op2ExaOpera.isEnabled = false
                        Pre6Op3ExaOpera.isEnabled = false
                        Pre6Op4ExaOpera.isEnabled = false
                    }
                R.id.Pre6Op4ExaOpera ->
                    if (checked) {
                        Toast.makeText(this, "Incorrecto", Toast.LENGTH_SHORT).show()
                        Pre6Op4ExaOpera.setBackgroundColor(Color.rgb(255, 0, 0))//rojo
                        Pre6Op1ExaOpera.isEnabled = false
                        Pre6Op2ExaOpera.isEnabled = false
                        Pre6Op3ExaOpera.isEnabled = false
                        Pre6Op4ExaOpera.isEnabled = false
                    }
            }
        }
    }
    fun Pregunta7op(view: View) {
        if (view is RadioButton) {
            val checked = view.isChecked
            when (view.getId()) {
                R.id.Pre7Op1ExaOpera ->
                    if (checked) {
                        Toast.makeText(this, "Incorrecto", Toast.LENGTH_SHORT).show();
                        Pre7Op1ExaOpera.setBackgroundColor(Color.rgb(255, 0, 0))//rojo
                        Pre7Op1ExaOpera.isEnabled = false
                        Pre7Op2ExaOpera.isEnabled = false
                        Pre7Op3ExaOpera.isEnabled = false
                        Pre7Op4ExaOpera.isEnabled = false
                    }
                R.id.Pre7Op2ExaOpera ->
                    if (checked) {
                        califB1++
                        Toast.makeText(this, "Correcta", Toast.LENGTH_SHORT).show()
                        Pre7Op2ExaOpera.setBackgroundColor(Color.rgb(0, 143, 57))//verde
                        Pre7Op1ExaOpera.isEnabled = false
                        Pre7Op2ExaOpera.isEnabled = false
                        Pre7Op3ExaOpera.isEnabled = false
                        Pre7Op4ExaOpera.isEnabled = false
                    }
                R.id.Pre7Op3ExaOpera ->
                    if (checked) {
                        Toast.makeText(this, "Incorrecto", Toast.LENGTH_SHORT).show();
                        Pre7Op3ExaOpera.setBackgroundColor(Color.rgb(255, 0, 0))//rojo
                        Pre7Op1ExaOpera.isEnabled = false
                        Pre7Op2ExaOpera.isEnabled = false
                        Pre7Op3ExaOpera.isEnabled = false
                        Pre7Op4ExaOpera.isEnabled = false
                    }
                R.id.Pre7Op4ExaOpera ->
                    if (checked) {
                        Toast.makeText(this, "Incorrecto", Toast.LENGTH_SHORT).show()
                        Pre7Op4ExaOpera.setBackgroundColor(Color.rgb(255, 0, 0))//rojo
                        Pre7Op1ExaOpera.isEnabled = false
                        Pre7Op2ExaOpera.isEnabled = false
                        Pre7Op3ExaOpera.isEnabled = false
                        Pre7Op4ExaOpera.isEnabled = false
                    }
            }
        }
    }
    fun Pregunta8op(view: View) {
        if (view is RadioButton) {
            val checked = view.isChecked
            when (view.getId()) {
                R.id.Pre8Op1ExaOpera ->
                    if (checked) {
                        califB1++
                        Toast.makeText(this, "Correcta", Toast.LENGTH_SHORT).show()
                        Pre8Op1ExaOpera.setBackgroundColor(Color.rgb(0, 143, 57))//verde
                        Pre8Op1ExaOpera.isEnabled = false
                        Pre8Op2ExaOpera.isEnabled = false
                        Pre8Op3ExaOpera.isEnabled = false
                        Pre8Op4ExaOpera.isEnabled = false
                    }
                R.id.Pre8Op2ExaOpera ->
                    if (checked) {
                        Toast.makeText(this, "Incorrecto", Toast.LENGTH_SHORT).show();
                        Pre8Op2ExaOpera.setBackgroundColor(Color.rgb(255, 0, 0))//rojo
                        Pre8Op1ExaOpera.isEnabled = false
                        Pre8Op2ExaOpera.isEnabled = false
                        Pre8Op3ExaOpera.isEnabled = false
                        Pre8Op4ExaOpera.isEnabled = false
                    }
                R.id.Pre8Op3ExaOpera ->
                    if (checked) {
                        Toast.makeText(this, "Incorrecto", Toast.LENGTH_SHORT).show();
                        Pre8Op3ExaOpera.setBackgroundColor(Color.rgb(255, 0, 0))//rojo
                        Pre8Op1ExaOpera.isEnabled = false
                        Pre8Op2ExaOpera.isEnabled = false
                        Pre8Op3ExaOpera.isEnabled = false
                        Pre8Op4ExaOpera.isEnabled = false
                    }
                R.id.Pre8Op4ExaOpera ->
                    if (checked) {
                        Toast.makeText(this, "Incorrecto", Toast.LENGTH_SHORT).show()
                        Pre8Op4ExaOpera.setBackgroundColor(Color.rgb(255, 0, 0))//rojo
                        Pre8Op1ExaOpera.isEnabled = false
                        Pre8Op2ExaOpera.isEnabled = false
                        Pre8Op3ExaOpera.isEnabled = false
                        Pre8Op4ExaOpera.isEnabled = false
                    }
            }
        }
    }
    fun Pregunta9op(view: View) {
        if (view is RadioButton) {
            val checked = view.isChecked
            when (view.getId()) {
                R.id.Pre9Op1ExaOpera ->
                    if (checked) {
                        Toast.makeText(this, "Incorrecto", Toast.LENGTH_SHORT).show();
                        Pre9Op1ExaOpera.setBackgroundColor(Color.rgb(255, 0, 0))//rojo
                        Pre9Op1ExaOpera.isEnabled = false
                        Pre9Op2ExaOpera.isEnabled = false
                        Pre9Op3ExaOpera.isEnabled = false
                        Pre9Op4ExaOpera.isEnabled = false
                    }
                R.id.Pre9Op2ExaOpera ->
                    if (checked) {
                        califB1++
                        Toast.makeText(this, "Correcta", Toast.LENGTH_SHORT).show()
                        Pre9Op2ExaOpera.setBackgroundColor(Color.rgb(0, 143, 57))//verde
                        Pre9Op1ExaOpera.isEnabled = false
                        Pre9Op2ExaOpera.isEnabled = false
                        Pre9Op3ExaOpera.isEnabled = false
                        Pre9Op4ExaOpera.isEnabled = false
                    }
                R.id.Pre9Op3ExaOpera ->
                    if (checked) {
                        Toast.makeText(this, "Incorrecto", Toast.LENGTH_SHORT).show();
                        Pre9Op3ExaOpera.setBackgroundColor(Color.rgb(255, 0, 0))//rojo
                        Pre9Op1ExaOpera.isEnabled = false
                        Pre9Op2ExaOpera.isEnabled = false
                        Pre9Op3ExaOpera.isEnabled = false
                        Pre9Op4ExaOpera.isEnabled = false
                    }
                R.id.Pre9Op4ExaOpera ->
                    if (checked) {
                        Toast.makeText(this, "Incorrecto", Toast.LENGTH_SHORT).show()
                        Pre9Op4ExaOpera.setBackgroundColor(Color.rgb(255, 0, 0))//rojo
                        Pre9Op1ExaOpera.isEnabled = false
                        Pre9Op2ExaOpera.isEnabled = false
                        Pre9Op3ExaOpera.isEnabled = false
                        Pre9Op4ExaOpera.isEnabled = false
                    }
            }
        }
    }
    //Pregunta 10
    fun Pregunta10op(view: View) {
        if (view is RadioButton) {
            val checked = view.isChecked
            when (view.getId()) {
                R.id.Pre10Op1ExaOpera ->
                    if (checked) {
                        Toast.makeText(this, "Incorrecto", Toast.LENGTH_SHORT).show();
                        Pre10Op1ExaOpera.setBackgroundColor(Color.rgb(255, 0, 0))//rojo
                        Pre10Op1ExaOpera.isEnabled = false
                        Pre10Op2ExaOpera.isEnabled = false
                        Pre10Op3ExaOpera.isEnabled = false
                        Pre10Op4ExaOpera.isEnabled = false
                    }
                R.id.Pre10Op2ExaOpera ->
                    if (checked) {
                        Toast.makeText(this, "Incorrecto", Toast.LENGTH_SHORT).show();
                        Pre10Op2ExaOpera.setBackgroundColor(Color.rgb(255, 0, 0))//rojo
                        Pre10Op1ExaOpera.isEnabled = false
                        Pre10Op2ExaOpera.isEnabled = false
                        Pre10Op3ExaOpera.isEnabled = false
                        Pre10Op4ExaOpera.isEnabled = false
                    }
                R.id.Pre10Op3ExaOpera ->
                    if (checked) {
                        califB1++
                        Toast.makeText(this, "Correcta", Toast.LENGTH_SHORT).show()
                        Pre10Op3ExaOpera.setBackgroundColor(Color.rgb(0, 143, 57))//verde
                        Pre10Op1ExaOpera.isEnabled = false
                        Pre10Op2ExaOpera.isEnabled = false
                        Pre10Op3ExaOpera.isEnabled = false
                        Pre10Op4ExaOpera.isEnabled = false
                    }
                R.id.Pre10Op4ExaOpera ->
                    if (checked) {
                        Toast.makeText(this, "Incorrecto", Toast.LENGTH_SHORT).show()
                        Pre10Op4ExaOpera.setBackgroundColor(Color.rgb(255, 0, 0))//rojo
                        Pre10Op1ExaOpera.isEnabled = false
                        Pre10Op2ExaOpera.isEnabled = false
                        Pre10Op3ExaOpera.isEnabled = false
                        Pre10Op4ExaOpera.isEnabled = false
                    }
            }
        }
    }
    //pregunta 11
    fun Pregunta11op(view: View) {
        if (view is RadioButton) {
            val checked = view.isChecked
            when (view.getId()) {
                R.id.Pre11Op1ExaOpera ->
                    if (checked) {
                        Toast.makeText(this, "Incorrecto", Toast.LENGTH_SHORT).show();
                        Pre11Op1ExaOpera.setBackgroundColor(Color.rgb(255, 0, 0))//rojo
                        Pre11Op1ExaOpera.isEnabled = false
                        Pre11Op2ExaOpera.isEnabled = false
                        Pre11Op3ExaOpera.isEnabled = false
                        Pre11Op4ExaOpera.isEnabled = false
                    }
                R.id.Pre11Op2ExaOpera ->
                    if (checked) {
                        Toast.makeText(this, "Incorrecto", Toast.LENGTH_SHORT).show();
                        Pre11Op2ExaOpera.setBackgroundColor(Color.rgb(255, 0, 0))//rojo
                        Pre11Op1ExaOpera.isEnabled = false
                        Pre11Op2ExaOpera.isEnabled = false
                        Pre11Op3ExaOpera.isEnabled = false
                        Pre11Op4ExaOpera.isEnabled = false
                    }
                R.id.Pre11Op3ExaOpera ->
                    if (checked) {
                        Toast.makeText(this, "Incorrecto", Toast.LENGTH_SHORT).show()
                        Pre11Op3ExaOpera.setBackgroundColor(Color.rgb(255, 0, 0))//rojo
                        Pre11Op1ExaOpera.isEnabled = false
                        Pre11Op2ExaOpera.isEnabled = false
                        Pre11Op3ExaOpera.isEnabled = false
                        Pre11Op4ExaOpera.isEnabled = false
                    }
                R.id.Pre11Op4ExaOpera ->
                    if (checked) {
                        califB1++
                        Toast.makeText(this, "Correcta", Toast.LENGTH_SHORT).show()
                        Pre11Op4ExaOpera.setBackgroundColor(Color.rgb(0, 143, 57))//verde
                        Pre11Op1ExaOpera.isEnabled = false
                        Pre11Op2ExaOpera.isEnabled = false
                        Pre11Op3ExaOpera.isEnabled = false
                        Pre11Op4ExaOpera.isEnabled = false
                    }
            }
        }
    }
    fun Pregunta12op(view: View) {
        if (view is RadioButton) {
            val checked = view.isChecked
            when (view.getId()) {
                R.id.Pre12Op1ExaOpera ->
                    if (checked) {
                        califB1++
                        Toast.makeText(this, "Correcta", Toast.LENGTH_SHORT).show()
                        Pre12Op1ExaOpera.setBackgroundColor(Color.rgb(0, 143, 57))//verde
                        Pre12Op1ExaOpera.isEnabled = false
                        Pre12Op2ExaOpera.isEnabled = false
                        Pre12Op3ExaOpera.isEnabled = false
                        Pre12Op4ExaOpera.isEnabled = false
                    }
                R.id.Pre12Op2ExaOpera ->
                    if (checked) {
                        Toast.makeText(this, "Incorrecto", Toast.LENGTH_SHORT).show();
                        Pre12Op2ExaOpera.setBackgroundColor(Color.rgb(255, 0, 0))//rojo
                        Pre12Op1ExaOpera.isEnabled = false
                        Pre12Op2ExaOpera.isEnabled = false
                        Pre12Op3ExaOpera.isEnabled = false
                        Pre12Op4ExaOpera.isEnabled = false
                    }
                R.id.Pre12Op3ExaOpera ->
                    if (checked) {
                        Toast.makeText(this, "Incorrecto", Toast.LENGTH_SHORT).show()
                        Pre12Op3ExaOpera.setBackgroundColor(Color.rgb(255, 0, 0))//rojo
                        Pre12Op1ExaOpera.isEnabled = false
                        Pre12Op2ExaOpera.isEnabled = false
                        Pre12Op3ExaOpera.isEnabled = false
                        Pre12Op4ExaOpera.isEnabled = false
                    }
                R.id.Pre12Op4ExaOpera ->
                    if (checked) {
                        Toast.makeText(this, "Incorrecto", Toast.LENGTH_SHORT).show();
                        Pre12Op4ExaOpera.setBackgroundColor(Color.rgb(255, 0, 0))//rojo
                        Pre12Op1ExaOpera.isEnabled = false
                        Pre12Op2ExaOpera.isEnabled = false
                        Pre12Op3ExaOpera.isEnabled = false
                        Pre12Op4ExaOpera.isEnabled = false
                    }
            }
        }
    }
    fun Pregunta13op(view: View) {
        if (view is RadioButton) {
            val checked = view.isChecked
            when (view.getId()) {
                R.id.Pre13Op1ExaOpera ->
                    if (checked) {
                        califB1++
                        Toast.makeText(this, "Correcta", Toast.LENGTH_SHORT).show()
                        Pre13Op1ExaOpera.setBackgroundColor(Color.rgb(0, 143, 57))//verde
                        Pre13Op1ExaOpera.isEnabled = false
                        Pre13Op2ExaOpera.isEnabled = false
                        Pre13Op3ExaOpera.isEnabled = false
                        Pre13Op4ExaOpera.isEnabled = false
                    }
                R.id.Pre13Op2ExaOpera ->
                    if (checked) {
                        Toast.makeText(this, "Incorrecto", Toast.LENGTH_SHORT).show();
                        Pre13Op2ExaOpera.setBackgroundColor(Color.rgb(255, 0, 0))//rojo
                        Pre13Op1ExaOpera.isEnabled = false
                        Pre13Op2ExaOpera.isEnabled = false
                        Pre13Op3ExaOpera.isEnabled = false
                        Pre13Op4ExaOpera.isEnabled = false
                    }
                R.id.Pre13Op3ExaOpera ->
                    if (checked) {
                        Toast.makeText(this, "Incorrecto", Toast.LENGTH_SHORT).show()
                        Pre13Op3ExaOpera.setBackgroundColor(Color.rgb(255, 0, 0))//rojo
                        Pre13Op1ExaOpera.isEnabled = false
                        Pre13Op2ExaOpera.isEnabled = false
                        Pre13Op3ExaOpera.isEnabled = false
                        Pre13Op4ExaOpera.isEnabled = false
                    }
                R.id.Pre13Op4ExaOpera ->
                    if (checked) {
                        Toast.makeText(this, "Incorrecto", Toast.LENGTH_SHORT).show();
                        Pre13Op4ExaOpera.setBackgroundColor(Color.rgb(255, 0, 0))//rojo
                        Pre13Op1ExaOpera.isEnabled = false
                        Pre13Op2ExaOpera.isEnabled = false
                        Pre13Op3ExaOpera.isEnabled = false
                        Pre13Op4ExaOpera.isEnabled = false
                    }
            }
        }
    }
    fun Pregunta14op(view: View) {
        if (view is RadioButton) {
            val checked = view.isChecked
            when (view.getId()) {
                R.id.Pre14Op1ExaOpera ->
                    if (checked) {
                        Toast.makeText(this, "Incorrecto", Toast.LENGTH_SHORT).show()
                        Pre14Op1ExaOpera.setBackgroundColor(Color.rgb(255, 0, 0))//rojo
                        Pre14Op1ExaOpera.isEnabled = false
                        Pre14Op2ExaOpera.isEnabled = false
                        Pre14Op3ExaOpera.isEnabled = false
                        Pre14Op4ExaOpera.isEnabled = false
                    }
                R.id.Pre14Op2ExaOpera ->
                    if (checked) {
                        califB1++
                        Toast.makeText(this, "Correcta", Toast.LENGTH_SHORT).show()
                        Pre14Op2ExaOpera.setBackgroundColor(Color.rgb(0, 143, 57))//verde
                        Pre14Op1ExaOpera.isEnabled = false
                        Pre14Op2ExaOpera.isEnabled = false
                        Pre14Op3ExaOpera.isEnabled = false
                        Pre14Op4ExaOpera.isEnabled = false
                    }
                R.id.Pre14Op3ExaOpera ->
                    if (checked) {
                        Toast.makeText(this, "Incorrecto", Toast.LENGTH_SHORT).show()
                        Pre14Op3ExaOpera.setBackgroundColor(Color.rgb(255, 0, 0))//rojo
                        Pre14Op1ExaOpera.isEnabled = false
                        Pre14Op2ExaOpera.isEnabled = false
                        Pre14Op3ExaOpera.isEnabled = false
                        Pre14Op4ExaOpera.isEnabled = false
                    }
                R.id.Pre14Op4ExaOpera ->
                    if (checked) {
                        Toast.makeText(this, "Incorrecto", Toast.LENGTH_SHORT).show();
                        Pre14Op4ExaOpera.setBackgroundColor(Color.rgb(255, 0, 0))//rojo
                        Pre14Op1ExaOpera.isEnabled = false
                        Pre14Op2ExaOpera.isEnabled = false
                        Pre14Op3ExaOpera.isEnabled = false
                        Pre14Op4ExaOpera.isEnabled = false
                    }
            }
        }
    }
    fun Pregunta15op(view: View) {
        if (view is RadioButton) {
            val checked = view.isChecked
            when (view.getId()) {
                R.id.Pre15Op1ExaOpera ->
                    if (checked) {
                        califB2++
                        Toast.makeText(this, "Correcta", Toast.LENGTH_SHORT).show()
                        Pre15Op1ExaOpera.setBackgroundColor(Color.rgb(0, 143, 57))//verde
                        Pre15Op1ExaOpera.isEnabled = false
                        Pre15Op2ExaOpera.isEnabled = false
                        Pre15Op3ExaOpera.isEnabled = false
                        Pre15Op4ExaOpera.isEnabled = false
                    }
                R.id.Pre15Op2ExaOpera ->
                    if (checked) {
                        Toast.makeText(this, "Incorrecto", Toast.LENGTH_SHORT).show();
                        Pre15Op2ExaOpera.setBackgroundColor(Color.rgb(255, 0, 0))//rojo
                        Pre15Op1ExaOpera.isEnabled = false
                        Pre15Op2ExaOpera.isEnabled = false
                        Pre15Op3ExaOpera.isEnabled = false
                        Pre15Op4ExaOpera.isEnabled = false
                    }
                R.id.Pre15Op3ExaOpera ->
                    if (checked) {
                        Toast.makeText(this, "Incorrecto", Toast.LENGTH_SHORT).show()
                        Pre15Op3ExaOpera.setBackgroundColor(Color.rgb(255, 0, 0))//rojo
                        Pre15Op1ExaOpera.isEnabled = false
                        Pre15Op2ExaOpera.isEnabled = false
                        Pre15Op3ExaOpera.isEnabled = false
                        Pre15Op4ExaOpera.isEnabled = false
                    }
                R.id.Pre15Op4ExaOpera ->
                    if (checked) {
                        Toast.makeText(this, "Incorrecto", Toast.LENGTH_SHORT).show();
                        Pre15Op4ExaOpera.setBackgroundColor(Color.rgb(255, 0, 0))//rojo
                        Pre15Op1ExaOpera.isEnabled = false
                        Pre15Op2ExaOpera.isEnabled = false
                        Pre15Op3ExaOpera.isEnabled = false
                        Pre15Op4ExaOpera.isEnabled = false
                    }
            }
        }
    }
    fun Pregunta16op(view: View) {
        if (view is RadioButton) {
            val checked = view.isChecked
            when (view.getId()) {
                R.id.Pre16Op1ExaOpera ->
                    if (checked) {
                        Toast.makeText(this, "Incorrecto", Toast.LENGTH_SHORT).show();
                        Pre16Op1ExaOpera.setBackgroundColor(Color.rgb(255, 0, 0))//rojo
                        Pre16Op1ExaOpera.isEnabled = false
                        Pre16Op2ExaOpera.isEnabled = false
                        Pre16Op3ExaOpera.isEnabled = false
                        Pre16Op4ExaOpera.isEnabled = false
                    }
                R.id.Pre16Op2ExaOpera ->
                    if (checked) {
                        califB2++
                        Toast.makeText(this, "Correcta", Toast.LENGTH_SHORT).show()
                        Pre16Op2ExaOpera.setBackgroundColor(Color.rgb(0, 143, 57))//verde
                        Pre16Op1ExaOpera.isEnabled = false
                        Pre16Op2ExaOpera.isEnabled = false
                        Pre16Op3ExaOpera.isEnabled = false
                        Pre16Op4ExaOpera.isEnabled = false
                    }
                R.id.Pre16Op3ExaOpera ->
                    if (checked) {
                        Toast.makeText(this, "Incorrecto", Toast.LENGTH_SHORT).show()
                        Pre16Op3ExaOpera.setBackgroundColor(Color.rgb(255, 0, 0))//rojo
                        Pre16Op1ExaOpera.isEnabled = false
                        Pre16Op2ExaOpera.isEnabled = false
                        Pre16Op3ExaOpera.isEnabled = false
                        Pre16Op4ExaOpera.isEnabled = false
                    }
                R.id.Pre16Op4ExaOpera ->
                    if (checked) {
                        Toast.makeText(this, "Incorrecto", Toast.LENGTH_SHORT).show();
                        Pre16Op4ExaOpera.setBackgroundColor(Color.rgb(255, 0, 0))//rojo
                        Pre16Op1ExaOpera.isEnabled = false
                        Pre16Op2ExaOpera.isEnabled = false
                        Pre16Op3ExaOpera.isEnabled = false
                        Pre16Op4ExaOpera.isEnabled = false
                    }
            }
        }
    }
    fun Pregunta17op(view: View) {
        if (view is RadioButton) {
            val checked = view.isChecked
            when (view.getId()) {
                R.id.Pre17Op1ExaOpera ->
                    if (checked) {
                        califB2++
                        Toast.makeText(this, "Correcta", Toast.LENGTH_SHORT).show()
                        Pre17Op1ExaOpera.setBackgroundColor(Color.rgb(0, 143, 57))//verde
                        Pre17Op1ExaOpera.isEnabled = false
                        Pre17Op2ExaOpera.isEnabled = false
                        Pre17Op3ExaOpera.isEnabled = false
                        Pre17Op4ExaOpera.isEnabled = false
                    }
                R.id.Pre17Op2ExaOpera ->
                    if (checked) {
                        Toast.makeText(this, "Incorrecto", Toast.LENGTH_SHORT).show();
                        Pre17Op2ExaOpera.setBackgroundColor(Color.rgb(255, 0, 0))//rojo
                        Pre17Op1ExaOpera.isEnabled = false
                        Pre17Op2ExaOpera.isEnabled = false
                        Pre17Op3ExaOpera.isEnabled = false
                        Pre17Op4ExaOpera.isEnabled = false
                    }
                R.id.Pre17Op3ExaOpera ->
                    if (checked) {
                        Toast.makeText(this, "Incorrecto", Toast.LENGTH_SHORT).show()
                        Pre17Op3ExaOpera.setBackgroundColor(Color.rgb(255, 0, 0))//rojo
                        Pre17Op1ExaOpera.isEnabled = false
                        Pre17Op2ExaOpera.isEnabled = false
                        Pre17Op3ExaOpera.isEnabled = false
                        Pre17Op4ExaOpera.isEnabled = false
                    }
                R.id.Pre17Op4ExaOpera ->
                    if (checked) {
                        Toast.makeText(this, "Incorrecto", Toast.LENGTH_SHORT).show();
                        Pre17Op4ExaOpera.setBackgroundColor(Color.rgb(255, 0, 0))//rojo
                        Pre17Op1ExaOpera.isEnabled = false
                        Pre17Op2ExaOpera.isEnabled = false
                        Pre17Op3ExaOpera.isEnabled = false
                        Pre17Op4ExaOpera.isEnabled = false
                    }
            }
        }
    }
    fun Pregunta18op(view: View) {
        if (view is RadioButton) {
            val checked = view.isChecked
            when (view.getId()) {
                R.id.Pre18Op1ExaOpera ->
                    if (checked) {
                        Toast.makeText(this, "Incorrecto", Toast.LENGTH_SHORT).show();
                        Pre18Op1ExaOpera.setBackgroundColor(Color.rgb(255, 0, 0))//rojo
                        Pre18Op1ExaOpera.isEnabled = false
                        Pre18Op2ExaOpera.isEnabled = false
                        Pre18Op3ExaOpera.isEnabled = false
                        Pre18Op4ExaOpera.isEnabled = false
                    }
                R.id.Pre18Op2ExaOpera ->
                    if (checked) {
                        califB2++
                        Toast.makeText(this, "Correcta", Toast.LENGTH_SHORT).show()
                        Pre18Op2ExaOpera.setBackgroundColor(Color.rgb(0, 143, 57))//verde
                        Pre18Op1ExaOpera.isEnabled = false
                        Pre18Op2ExaOpera.isEnabled = false
                        Pre18Op3ExaOpera.isEnabled = false
                        Pre18Op4ExaOpera.isEnabled = false
                    }
                R.id.Pre18Op3ExaOpera ->
                    if (checked) {
                        Toast.makeText(this, "Incorrecto", Toast.LENGTH_SHORT).show()
                        Pre18Op3ExaOpera.setBackgroundColor(Color.rgb(255, 0, 0))//rojo
                        Pre18Op1ExaOpera.isEnabled = false
                        Pre18Op2ExaOpera.isEnabled = false
                        Pre18Op3ExaOpera.isEnabled = false
                        Pre18Op4ExaOpera.isEnabled = false
                    }
                R.id.Pre18Op4ExaOpera ->
                    if (checked) {
                        Toast.makeText(this, "Incorrecto", Toast.LENGTH_SHORT).show();
                        Pre18Op4ExaOpera.setBackgroundColor(Color.rgb(255, 0, 0))//rojo
                        Pre18Op1ExaOpera.isEnabled = false
                        Pre18Op2ExaOpera.isEnabled = false
                        Pre18Op3ExaOpera.isEnabled = false
                        Pre18Op4ExaOpera.isEnabled = false
                    }
            }
        }
    }
    fun Pregunta19op(view: View) {
        if (view is RadioButton) {
            val checked = view.isChecked
            when (view.getId()) {
                R.id.Pre19Op1ExaOpera ->
                    if (checked) {
                        Toast.makeText(this, "Incorrecto", Toast.LENGTH_SHORT).show();
                        Pre19Op1ExaOpera.setBackgroundColor(Color.rgb(255, 0, 0))//rojo
                        Pre19Op1ExaOpera.isEnabled = false
                        Pre19Op2ExaOpera.isEnabled = false
                        Pre19Op3ExaOpera.isEnabled = false
                        Pre19Op4ExaOpera.isEnabled = false
                    }
                R.id.Pre19Op2ExaOpera ->
                    if (checked) {
                        califB2++
                        Toast.makeText(this, "Correcta", Toast.LENGTH_SHORT).show()
                        Pre19Op2ExaOpera.setBackgroundColor(Color.rgb(0, 143, 57))//verde
                        Pre19Op1ExaOpera.isEnabled = false
                        Pre19Op2ExaOpera.isEnabled = false
                        Pre19Op3ExaOpera.isEnabled = false
                        Pre19Op4ExaOpera.isEnabled = false
                    }
                R.id.Pre19Op3ExaOpera ->
                    if (checked) {
                        Toast.makeText(this, "Incorrecto", Toast.LENGTH_SHORT).show()
                        Pre19Op3ExaOpera.setBackgroundColor(Color.rgb(255, 0, 0))//rojo
                        Pre19Op1ExaOpera.isEnabled = false
                        Pre19Op2ExaOpera.isEnabled = false
                        Pre19Op3ExaOpera.isEnabled = false
                        Pre19Op4ExaOpera.isEnabled = false
                    }
                R.id.Pre19Op4ExaOpera ->
                    if (checked) {
                        Toast.makeText(this, "Incorrecto", Toast.LENGTH_SHORT).show();
                        Pre19Op4ExaOpera.setBackgroundColor(Color.rgb(255, 0, 0))//rojo
                        Pre19Op1ExaOpera.isEnabled = false
                        Pre19Op2ExaOpera.isEnabled = false
                        Pre19Op3ExaOpera.isEnabled = false
                        Pre19Op4ExaOpera.isEnabled = false
                    }
            }
        }
    }
    fun Pregunta20op(view: View) {
        if (view is RadioButton) {
            val checked = view.isChecked
            when (view.getId()) {
                R.id.Pre20Op1ExaOpera ->
                    if (checked) {
                        califB2++
                        Toast.makeText(this, "Correcta", Toast.LENGTH_SHORT).show()
                        Pre20Op1ExaOpera.setBackgroundColor(Color.rgb(0, 143, 57))//verde
                        Pre20Op1ExaOpera.isEnabled = false
                        Pre20Op2ExaOpera.isEnabled = false
                        Pre20Op3ExaOpera.isEnabled = false
                        Pre20Op4ExaOpera.isEnabled = false
                    }
                R.id.Pre20Op2ExaOpera ->
                    if (checked) {
                        Toast.makeText(this, "Incorrecto", Toast.LENGTH_SHORT).show();
                        Pre20Op2ExaOpera.setBackgroundColor(Color.rgb(255, 0, 0))//rojo
                        Pre20Op1ExaOpera.isEnabled = false
                        Pre20Op2ExaOpera.isEnabled = false
                        Pre20Op3ExaOpera.isEnabled = false
                        Pre20Op4ExaOpera.isEnabled = false
                    }
                R.id.Pre20Op3ExaOpera ->
                    if (checked) {
                        Toast.makeText(this, "Incorrecto", Toast.LENGTH_SHORT).show()
                        Pre20Op3ExaOpera.setBackgroundColor(Color.rgb(255, 0, 0))//rojo
                        Pre20Op1ExaOpera.isEnabled = false
                        Pre20Op2ExaOpera.isEnabled = false
                        Pre20Op3ExaOpera.isEnabled = false
                        Pre20Op4ExaOpera.isEnabled = false
                    }
                R.id.Pre20Op4ExaOpera ->
                    if (checked) {
                        Toast.makeText(this, "Incorrecto", Toast.LENGTH_SHORT).show();
                        Pre20Op4ExaOpera.setBackgroundColor(Color.rgb(255, 0, 0))//rojo
                        Pre20Op1ExaOpera.isEnabled = false
                        Pre20Op2ExaOpera.isEnabled = false
                        Pre20Op3ExaOpera.isEnabled = false
                        Pre20Op4ExaOpera.isEnabled = false
                    }
            }
        }
    }
    fun Pregunta21op(view: View) {
        if (view is RadioButton) {
            val checked = view.isChecked
            when (view.getId()) {
                R.id.Pre21Op1ExaOpera ->
                    if (checked) {
                        Toast.makeText(this, "Incorrecto", Toast.LENGTH_SHORT).show();
                        Pre21Op1ExaOpera.setBackgroundColor(Color.rgb(255, 0, 0))//rojo
                        Pre21Op1ExaOpera.isEnabled = false
                        Pre21Op2ExaOpera.isEnabled = false
                        Pre21Op3ExaOpera.isEnabled = false
                        Pre21Op4ExaOpera.isEnabled = false
                    }
                R.id.Pre21Op2ExaOpera ->
                    if (checked) {
                        Toast.makeText(this, "Incorrecto", Toast.LENGTH_SHORT).show();
                        Pre21Op2ExaOpera.setBackgroundColor(Color.rgb(255, 0, 0))//rojo
                        Pre21Op1ExaOpera.isEnabled = false
                        Pre21Op2ExaOpera.isEnabled = false
                        Pre21Op3ExaOpera.isEnabled = false
                        Pre21Op4ExaOpera.isEnabled = false
                    }
                R.id.Pre21Op3ExaOpera ->
                    if (checked) {
                        Toast.makeText(this, "Incorrecto", Toast.LENGTH_SHORT).show()
                        Pre21Op3ExaOpera.setBackgroundColor(Color.rgb(255, 0, 0))//rojo
                        Pre21Op1ExaOpera.isEnabled = false
                        Pre21Op2ExaOpera.isEnabled = false
                        Pre21Op3ExaOpera.isEnabled = false
                        Pre21Op4ExaOpera.isEnabled = false
                    }
                R.id.Pre21Op4ExaOpera ->
                    if (checked) {
                        califB2++
                        Toast.makeText(this, "Correcta", Toast.LENGTH_SHORT).show()
                        Pre21Op4ExaOpera.setBackgroundColor(Color.rgb(0, 143, 57))//verde
                        Pre21Op1ExaOpera.isEnabled = false
                        Pre21Op2ExaOpera.isEnabled = false
                        Pre21Op3ExaOpera.isEnabled = false
                        Pre21Op4ExaOpera.isEnabled = false
                    }
            }
        }
    }
    fun Pregunta22op(view: View) {
        if (view is RadioButton) {
            val checked = view.isChecked
            when (view.getId()) {
                R.id.Pre22Op1ExaOpera ->
                    if (checked) {
                        Toast.makeText(this, "Incorrecto", Toast.LENGTH_SHORT).show();
                        Pre22Op1ExaOpera.setBackgroundColor(Color.rgb(255, 0, 0))//rojo
                        Pre22Op1ExaOpera.isEnabled = false
                        Pre22Op2ExaOpera.isEnabled = false
                        Pre22Op3ExaOpera.isEnabled = false
                        Pre22Op4ExaOpera.isEnabled = false
                    }
                R.id.Pre22Op2ExaOpera ->
                    if (checked) {
                        Toast.makeText(this, "Incorrecto", Toast.LENGTH_SHORT).show();
                        Pre22Op2ExaOpera.setBackgroundColor(Color.rgb(255, 0, 0))//rojo
                        Pre22Op1ExaOpera.isEnabled = false
                        Pre22Op2ExaOpera.isEnabled = false
                        Pre22Op3ExaOpera.isEnabled = false
                        Pre22Op4ExaOpera.isEnabled = false
                    }
                R.id.Pre22Op3ExaOpera ->
                    if (checked) {
                        califB2++
                        Toast.makeText(this, "Correcta", Toast.LENGTH_SHORT).show()
                        Pre22Op3ExaOpera.setBackgroundColor(Color.rgb(0, 143, 57))//verde
                        Pre22Op1ExaOpera.isEnabled = false
                        Pre22Op2ExaOpera.isEnabled = false
                        Pre22Op3ExaOpera.isEnabled = false
                        Pre22Op4ExaOpera.isEnabled = false
                    }
                R.id.Pre22Op4ExaOpera ->
                    if (checked) {
                        Toast.makeText(this, "Incorrecto", Toast.LENGTH_SHORT).show()
                        Pre22Op4ExaOpera.setBackgroundColor(Color.rgb(255, 0, 0))//rojo
                        Pre22Op1ExaOpera.isEnabled = false
                        Pre22Op2ExaOpera.isEnabled = false
                        Pre22Op3ExaOpera.isEnabled = false
                        Pre22Op4ExaOpera.isEnabled = false
                    }
            }
        }
    }
    fun Pregunta23op(view: View) {
        if (view is RadioButton) {
            val checked = view.isChecked
            when (view.getId()) {
                R.id.Pre23Op1ExaOpera ->
                    if (checked) {
                        califB2++
                        Toast.makeText(this, "Correcta", Toast.LENGTH_SHORT).show()
                        Pre23Op1ExaOpera.setBackgroundColor(Color.rgb(0, 143, 57))//verde
                        Pre23Op1ExaOpera.isEnabled = false
                        Pre23Op2ExaOpera.isEnabled = false
                        Pre23Op3ExaOpera.isEnabled = false
                        Pre23Op4ExaOpera.isEnabled = false
                    }
                R.id.Pre23Op2ExaOpera ->
                    if (checked) {
                        Toast.makeText(this, "Incorrecto", Toast.LENGTH_SHORT).show();
                        Pre23Op2ExaOpera.setBackgroundColor(Color.rgb(255, 0, 0))//rojo
                        Pre23Op1ExaOpera.isEnabled = false
                        Pre23Op2ExaOpera.isEnabled = false
                        Pre23Op3ExaOpera.isEnabled = false
                        Pre23Op4ExaOpera.isEnabled = false
                    }
                R.id.Pre23Op3ExaOpera ->
                    if (checked) {
                        Toast.makeText(this, "Incorrecto", Toast.LENGTH_SHORT).show();
                        Pre23Op3ExaOpera.setBackgroundColor(Color.rgb(255, 0, 0))//rojo
                        Pre23Op1ExaOpera.isEnabled = false
                        Pre23Op2ExaOpera.isEnabled = false
                        Pre23Op3ExaOpera.isEnabled = false
                        Pre23Op4ExaOpera.isEnabled = false
                    }
                R.id.Pre23Op4ExaOpera ->
                    if (checked) {
                        Toast.makeText(this, "Incorrecto", Toast.LENGTH_SHORT).show()
                        Pre23Op4ExaOpera.setBackgroundColor(Color.rgb(255, 0, 0))//rojo
                        Pre23Op1ExaOpera.isEnabled = false
                        Pre23Op2ExaOpera.isEnabled = false
                        Pre23Op3ExaOpera.isEnabled = false
                        Pre23Op4ExaOpera.isEnabled = false
                    }
            }
        }
    }
    fun Pregunta24op(view: View) {
        if (view is RadioButton) {
            val checked = view.isChecked
            when (view.getId()) {
                R.id.Pre24Op1ExaOpera ->
                    if (checked) {
                        Toast.makeText(this, "Incorrecto", Toast.LENGTH_SHORT).show()
                        Pre24Op1ExaOpera.setBackgroundColor(Color.rgb(255, 0, 0))//rojo
                        Pre24Op1ExaOpera.isEnabled = false
                        Pre24Op2ExaOpera.isEnabled = false
                        Pre24Op3ExaOpera.isEnabled = false
                        Pre24Op4ExaOpera.isEnabled = false
                    }
                R.id.Pre24Op2ExaOpera ->
                    if (checked) {
                        Toast.makeText(this, "Incorrecto", Toast.LENGTH_SHORT).show();
                        Pre24Op2ExaOpera.setBackgroundColor(Color.rgb(255, 0, 0))//rojo
                        Pre24Op1ExaOpera.isEnabled = false
                        Pre24Op2ExaOpera.isEnabled = false
                        Pre24Op3ExaOpera.isEnabled = false
                        Pre24Op4ExaOpera.isEnabled = false
                    }
                R.id.Pre24Op3ExaOpera ->
                    if (checked) {
                        Toast.makeText(this, "Incorrecto", Toast.LENGTH_SHORT).show();
                        Pre24Op3ExaOpera.setBackgroundColor(Color.rgb(255, 0, 0))//rojo
                        Pre24Op1ExaOpera.isEnabled = false
                        Pre24Op2ExaOpera.isEnabled = false
                        Pre24Op3ExaOpera.isEnabled = false
                        Pre24Op4ExaOpera.isEnabled = false
                    }
                R.id.Pre24Op4ExaOpera ->
                    if (checked) {
                        califB2++
                        Toast.makeText(this, "Correcta", Toast.LENGTH_SHORT).show()
                        Pre24Op4ExaOpera.setBackgroundColor(Color.rgb(0, 143, 57))//verde
                        Pre24Op1ExaOpera.isEnabled = false
                        Pre24Op2ExaOpera.isEnabled = false
                        Pre24Op3ExaOpera.isEnabled = false
                        Pre24Op4ExaOpera.isEnabled = false
                    }
            }
        }
    }
    fun Pregunta25op(view: View) {
        if (view is RadioButton) {
            val checked = view.isChecked
            when (view.getId()) {
                R.id.Pre25Op1ExaOpera ->
                    if (checked) {
                        Toast.makeText(this, "Incorrecto", Toast.LENGTH_SHORT).show()
                        Pre25Op1ExaOpera.setBackgroundColor(Color.rgb(255, 0, 0))//rojo
                        Pre25Op1ExaOpera.isEnabled = false
                        Pre25Op2ExaOpera.isEnabled = false
                        Pre25Op3ExaOpera.isEnabled = false
                        Pre25Op4ExaOpera.isEnabled = false
                    }
                R.id.Pre25Op2ExaOpera ->
                    if (checked) {
                        Toast.makeText(this, "Incorrecto", Toast.LENGTH_SHORT).show();
                        Pre25Op2ExaOpera.setBackgroundColor(Color.rgb(255, 0, 0))//rojo
                        Pre25Op1ExaOpera.isEnabled = false
                        Pre25Op2ExaOpera.isEnabled = false
                        Pre25Op3ExaOpera.isEnabled = false
                        Pre25Op4ExaOpera.isEnabled = false
                    }
                R.id.Pre25Op3ExaOpera ->
                    if (checked) {
                        Toast.makeText(this, "Incorrecto", Toast.LENGTH_SHORT).show();
                        Pre25Op3ExaOpera.setBackgroundColor(Color.rgb(255, 0, 0))//rojo
                        Pre25Op1ExaOpera.isEnabled = false
                        Pre25Op2ExaOpera.isEnabled = false
                        Pre25Op3ExaOpera.isEnabled = false
                        Pre25Op4ExaOpera.isEnabled = false
                    }
                R.id.Pre25Op4ExaOpera ->
                    if (checked) {
                        califB3++
                        Toast.makeText(this, "Correcta", Toast.LENGTH_SHORT).show()
                        Pre25Op4ExaOpera.setBackgroundColor(Color.rgb(0, 143, 57))//verde
                        Pre25Op1ExaOpera.isEnabled = false
                        Pre25Op2ExaOpera.isEnabled = false
                        Pre25Op3ExaOpera.isEnabled = false
                        Pre25Op4ExaOpera.isEnabled = false
                    }
            }
        }
    }
    fun Pregunta26op(view: View) {
        if (view is RadioButton) {
            val checked = view.isChecked
            when (view.getId()) {
                R.id.Pre26Op1ExaOpera ->
                    if (checked) {
                        Toast.makeText(this, "Incorrecto", Toast.LENGTH_SHORT).show()
                        Pre26Op1ExaOpera.setBackgroundColor(Color.rgb(255, 0, 0))//rojo
                        Pre26Op1ExaOpera.isEnabled = false
                        Pre26Op2ExaOpera.isEnabled = false
                        Pre26Op3ExaOpera.isEnabled = false
                        Pre26Op4ExaOpera.isEnabled = false
                    }
                R.id.Pre26Op2ExaOpera ->
                    if (checked) {
                        califB3++
                        Toast.makeText(this, "Correcta", Toast.LENGTH_SHORT).show()
                        Pre26Op2ExaOpera.setBackgroundColor(Color.rgb(0, 143, 57))//verde
                        Pre26Op1ExaOpera.isEnabled = false
                        Pre26Op2ExaOpera.isEnabled = false
                        Pre26Op3ExaOpera.isEnabled = false
                        Pre26Op4ExaOpera.isEnabled = false
                    }
                R.id.Pre26Op3ExaOpera ->
                    if (checked) {
                        Toast.makeText(this, "Incorrecto", Toast.LENGTH_SHORT).show();
                        Pre26Op3ExaOpera.setBackgroundColor(Color.rgb(255, 0, 0))//rojo
                        Pre26Op1ExaOpera.isEnabled = false
                        Pre26Op2ExaOpera.isEnabled = false
                        Pre26Op3ExaOpera.isEnabled = false
                        Pre26Op4ExaOpera.isEnabled = false
                    }
                R.id.Pre26Op4ExaOpera ->
                    if (checked) {
                        Toast.makeText(this, "Incorrecto", Toast.LENGTH_SHORT).show();
                        Pre26Op4ExaOpera.setBackgroundColor(Color.rgb(255, 0, 0))//rojo
                        Pre26Op1ExaOpera.isEnabled = false
                        Pre26Op2ExaOpera.isEnabled = false
                        Pre26Op3ExaOpera.isEnabled = false
                        Pre26Op4ExaOpera.isEnabled = false
                    }
            }
        }
    }
    fun Pregunta27op(view: View) {
        if (view is RadioButton) {
            val checked = view.isChecked
            when (view.getId()) {
                R.id.Pre27Op1ExaOpera ->
                    if (checked) {
                        Toast.makeText(this, "Incorrecto", Toast.LENGTH_SHORT).show()
                        Pre27Op1ExaOpera.setBackgroundColor(Color.rgb(255, 0, 0))//rojo
                        Pre27Op1ExaOpera.isEnabled = false
                        Pre27Op2ExaOpera.isEnabled = false
                        Pre27Op3ExaOpera.isEnabled = false
                        Pre27Op4ExaOpera.isEnabled = false
                    }
                R.id.Pre27Op2ExaOpera ->
                    if (checked) {
                        Toast.makeText(this, "Incorrecto", Toast.LENGTH_SHORT).show();
                        Pre27Op2ExaOpera.setBackgroundColor(Color.rgb(255, 0, 0))//rojo
                        Pre27Op1ExaOpera.isEnabled = false
                        Pre27Op2ExaOpera.isEnabled = false
                        Pre27Op3ExaOpera.isEnabled = false
                        Pre27Op4ExaOpera.isEnabled = false
                    }
                R.id.Pre27Op3ExaOpera ->
                    if (checked) {
                        califB3++
                        Toast.makeText(this, "Correcta", Toast.LENGTH_SHORT).show()
                        Pre27Op3ExaOpera.setBackgroundColor(Color.rgb(0, 143, 57))//verde
                        Pre27Op1ExaOpera.isEnabled = false
                        Pre27Op2ExaOpera.isEnabled = false
                        Pre27Op3ExaOpera.isEnabled = false
                        Pre27Op4ExaOpera.isEnabled = false
                    }
                R.id.Pre27Op4ExaOpera ->
                    if (checked) {
                        Toast.makeText(this, "Incorrecto", Toast.LENGTH_SHORT).show();
                        Pre27Op4ExaOpera.setBackgroundColor(Color.rgb(255, 0, 0))//rojo
                        Pre27Op1ExaOpera.isEnabled = false
                        Pre27Op2ExaOpera.isEnabled = false
                        Pre27Op3ExaOpera.isEnabled = false
                        Pre27Op4ExaOpera.isEnabled = false
                    }
            }
        }
    }
    fun Pregunta28op(view: View) {
        if (view is RadioButton) {
            val checked = view.isChecked
            when (view.getId()) {
                R.id.Pre28Op1ExaOpera ->
                    if (checked) {
                        Toast.makeText(this, "Incorrecto", Toast.LENGTH_SHORT).show()
                        Pre28Op1ExaOpera.setBackgroundColor(Color.rgb(255, 0, 0))//rojo
                        Pre28Op1ExaOpera.isEnabled = false
                        Pre28Op2ExaOpera.isEnabled = false
                        Pre28Op3ExaOpera.isEnabled = false
                        Pre28Op4ExaOpera.isEnabled = false
                    }
                R.id.Pre28Op2ExaOpera ->
                    if (checked) {
                        Toast.makeText(this, "Incorrecto", Toast.LENGTH_SHORT).show();
                        Pre28Op2ExaOpera.setBackgroundColor(Color.rgb(255, 0, 0))//rojo
                        Pre28Op1ExaOpera.isEnabled = false
                        Pre28Op2ExaOpera.isEnabled = false
                        Pre28Op3ExaOpera.isEnabled = false
                        Pre28Op4ExaOpera.isEnabled = false
                    }
                R.id.Pre28Op3ExaOpera ->
                    if (checked) {
                        califB3++
                        Toast.makeText(this, "Correcta", Toast.LENGTH_SHORT).show()
                        Pre28Op3ExaOpera.setBackgroundColor(Color.rgb(0, 143, 57))//verde
                        Pre28Op1ExaOpera.isEnabled = false
                        Pre28Op2ExaOpera.isEnabled = false
                        Pre28Op3ExaOpera.isEnabled = false
                        Pre28Op4ExaOpera.isEnabled = false
                    }
                R.id.Pre28Op4ExaOpera ->
                    if (checked) {
                        Toast.makeText(this, "Incorrecto", Toast.LENGTH_SHORT).show();
                        Pre28Op4ExaOpera.setBackgroundColor(Color.rgb(255, 0, 0))//rojo
                        Pre28Op1ExaOpera.isEnabled = false
                        Pre28Op2ExaOpera.isEnabled = false
                        Pre28Op3ExaOpera.isEnabled = false
                        Pre28Op4ExaOpera.isEnabled = false
                    }
            }
        }
    }
    fun Pregunta29op(view: View) {
        if (view is RadioButton) {
            val checked = view.isChecked
            when (view.getId()) {
                R.id.Pre29Op1ExaOpera ->
                    if (checked) {
                        Toast.makeText(this, "Incorrecto", Toast.LENGTH_SHORT).show()
                        Pre29Op1ExaOpera.setBackgroundColor(Color.rgb(255, 0, 0))//rojo
                        Pre29Op1ExaOpera.isEnabled = false
                        Pre29Op2ExaOpera.isEnabled = false
                        Pre29Op3ExaOpera.isEnabled = false
                        Pre29Op4ExaOpera.isEnabled = false
                    }
                R.id.Pre29Op2ExaOpera ->
                    if (checked) {
                        Toast.makeText(this, "Incorrecto", Toast.LENGTH_SHORT).show();
                        Pre29Op2ExaOpera.setBackgroundColor(Color.rgb(255, 0, 0))//rojo
                        Pre29Op1ExaOpera.isEnabled = false
                        Pre29Op2ExaOpera.isEnabled = false
                        Pre29Op3ExaOpera.isEnabled = false
                        Pre29Op4ExaOpera.isEnabled = false
                    }
                R.id.Pre29Op3ExaOpera ->
                    if (checked) {
                        Toast.makeText(this, "Incorrecto", Toast.LENGTH_SHORT).show();
                        Pre29Op3ExaOpera.setBackgroundColor(Color.rgb(255, 0, 0))//rojo
                        Pre29Op1ExaOpera.isEnabled = false
                        Pre29Op2ExaOpera.isEnabled = false
                        Pre29Op3ExaOpera.isEnabled = false
                        Pre29Op4ExaOpera.isEnabled = false
                    }
                R.id.Pre29Op4ExaOpera ->
                    if (checked) {
                        califB3++
                        Toast.makeText(this, "Correcta", Toast.LENGTH_SHORT).show()
                        Pre29Op4ExaOpera.setBackgroundColor(Color.rgb(0, 143, 57))//verde
                        Pre29Op1ExaOpera.isEnabled = false
                        Pre29Op2ExaOpera.isEnabled = false
                        Pre29Op3ExaOpera.isEnabled = false
                        Pre29Op4ExaOpera.isEnabled = false
                    }
            }
        }
    }
    fun Pregunta30op(view: View) {
        if (view is RadioButton) {
            val checked = view.isChecked
            when (view.getId()) {
                R.id.Pre30Op1ExaOpera ->
                    if (checked) {
                        califB3++
                        Toast.makeText(this, "Correcta", Toast.LENGTH_SHORT).show()
                        Pre30Op1ExaOpera.setBackgroundColor(Color.rgb(0, 143, 57))//verde
                        Pre30Op1ExaOpera.isEnabled = false
                        Pre30Op2ExaOpera.isEnabled = false
                        Pre30Op3ExaOpera.isEnabled = false
                        Pre30Op4ExaOpera.isEnabled = false
                    }
                R.id.Pre30Op2ExaOpera ->
                    if (checked) {
                        Toast.makeText(this, "Incorrecto", Toast.LENGTH_SHORT).show();
                        Pre30Op2ExaOpera.setBackgroundColor(Color.rgb(255, 0, 0))//rojo
                        Pre30Op1ExaOpera.isEnabled = false
                        Pre30Op2ExaOpera.isEnabled = false
                        Pre30Op3ExaOpera.isEnabled = false
                        Pre30Op4ExaOpera.isEnabled = false
                    }
                R.id.Pre30Op3ExaOpera ->
                    if (checked) {
                        Toast.makeText(this, "Incorrecto", Toast.LENGTH_SHORT).show();
                        Pre30Op3ExaOpera.setBackgroundColor(Color.rgb(255, 0, 0))//rojo
                        Pre30Op1ExaOpera.isEnabled = false
                        Pre30Op2ExaOpera.isEnabled = false
                        Pre30Op3ExaOpera.isEnabled = false
                        Pre30Op4ExaOpera.isEnabled = false
                    }
                R.id.Pre30Op4ExaOpera ->
                    if (checked) {
                        Toast.makeText(this, "Incorrecto", Toast.LENGTH_SHORT).show()
                        Pre30Op4ExaOpera.setBackgroundColor(Color.rgb(255, 0, 0))//rojo
                        Pre30Op1ExaOpera.isEnabled = false
                        Pre30Op2ExaOpera.isEnabled = false
                        Pre30Op3ExaOpera.isEnabled = false
                        Pre30Op4ExaOpera.isEnabled = false
                    }
            }
        }
    }
    fun Pregunta31op(view: View) {
        if (view is RadioButton) {
            val checked = view.isChecked
            when (view.getId()) {
                R.id.Pre31Op1ExaOpera ->
                    if (checked) {
                        Toast.makeText(this, "Incorrecto", Toast.LENGTH_SHORT).show();
                        Pre31Op1ExaOpera.setBackgroundColor(Color.rgb(255, 0, 0))//rojo
                        Pre31Op1ExaOpera.isEnabled = false
                        Pre31Op2ExaOpera.isEnabled = false
                        Pre31Op3ExaOpera.isEnabled = false
                        Pre31Op4ExaOpera.isEnabled = false
                    }
                R.id.Pre31Op2ExaOpera ->
                    if (checked) {
                        Toast.makeText(this, "Incorrecto", Toast.LENGTH_SHORT).show();
                        Pre31Op2ExaOpera.setBackgroundColor(Color.rgb(255, 0, 0))//rojo
                        Pre31Op1ExaOpera.isEnabled = false
                        Pre31Op2ExaOpera.isEnabled = false
                        Pre31Op3ExaOpera.isEnabled = false
                        Pre31Op4ExaOpera.isEnabled = false
                    }
                R.id.Pre31Op3ExaOpera ->
                    if (checked) {
                        califB3++
                        Toast.makeText(this, "Correcta", Toast.LENGTH_SHORT).show()
                        Pre31Op3ExaOpera.setBackgroundColor(Color.rgb(0, 143, 57))//verde
                        Pre31Op1ExaOpera.isEnabled = false
                        Pre31Op2ExaOpera.isEnabled = false
                        Pre31Op3ExaOpera.isEnabled = false
                        Pre31Op4ExaOpera.isEnabled = false
                    }
                R.id.Pre31Op4ExaOpera ->
                    if (checked) {
                        Toast.makeText(this, "Incorrecto", Toast.LENGTH_SHORT).show()
                        Pre31Op4ExaOpera.setBackgroundColor(Color.rgb(255, 0, 0))//rojo
                        Pre31Op1ExaOpera.isEnabled = false
                        Pre31Op2ExaOpera.isEnabled = false
                        Pre31Op3ExaOpera.isEnabled = false
                        Pre31Op4ExaOpera.isEnabled = false
                    }
            }
        }
    }
    fun Pregunta32op(view: View) {
        if (view is RadioButton) {
            val checked = view.isChecked
            when (view.getId()) {
                R.id.Pre32Op1ExaOpera ->
                    if (checked) {
                        Toast.makeText(this, "Incorrecto", Toast.LENGTH_SHORT).show();
                        Pre32Op1ExaOpera.setBackgroundColor(Color.rgb(255, 0, 0))//rojo
                        Pre32Op1ExaOpera.isEnabled = false
                        Pre32Op2ExaOpera.isEnabled = false
                        Pre32Op3ExaOpera.isEnabled = false
                        Pre32Op4ExaOpera.isEnabled = false
                    }
                R.id.Pre32Op2ExaOpera ->
                    if (checked) {
                        califB3++
                        Toast.makeText(this, "Correcta", Toast.LENGTH_SHORT).show()
                        Pre32Op2ExaOpera.setBackgroundColor(Color.rgb(0, 143, 57))//verde
                        Pre32Op1ExaOpera.isEnabled = false
                        Pre32Op2ExaOpera.isEnabled = false
                        Pre32Op3ExaOpera.isEnabled = false
                        Pre32Op4ExaOpera.isEnabled = false
                    }
                R.id.Pre32Op3ExaOpera ->
                    if (checked) {
                        Toast.makeText(this, "Incorrecto", Toast.LENGTH_SHORT).show();
                        Pre32Op3ExaOpera.setBackgroundColor(Color.rgb(255, 0, 0))//rojo
                        Pre32Op1ExaOpera.isEnabled = false
                        Pre32Op2ExaOpera.isEnabled = false
                        Pre32Op3ExaOpera.isEnabled = false
                        Pre32Op4ExaOpera.isEnabled = false
                    }
                R.id.Pre32Op4ExaOpera ->
                    if (checked) {
                        Toast.makeText(this, "Incorrecto", Toast.LENGTH_SHORT).show()
                        Pre32Op4ExaOpera.setBackgroundColor(Color.rgb(255, 0, 0))//rojo
                        Pre32Op1ExaOpera.isEnabled = false
                        Pre32Op2ExaOpera.isEnabled = false
                        Pre32Op3ExaOpera.isEnabled = false
                        Pre32Op4ExaOpera.isEnabled = false
                    }
            }
        }
    }
    fun Pregunta33op(view: View) {
        if (view is RadioButton) {
            val checked = view.isChecked
            when (view.getId()) {
                R.id.Pre33Op1ExaOpera ->
                    if (checked) {
                        Toast.makeText(this, "Incorrecto", Toast.LENGTH_SHORT).show();
                        Pre33Op1ExaOpera.setBackgroundColor(Color.rgb(255, 0, 0))//rojo
                        Pre33Op1ExaOpera.isEnabled = false
                        Pre33Op2ExaOpera.isEnabled = false
                        Pre33Op3ExaOpera.isEnabled = false
                        Pre33Op4ExaOpera.isEnabled = false
                    }
                R.id.Pre33Op2ExaOpera ->
                    if (checked) {
                        califB3++
                        Toast.makeText(this, "Correcta", Toast.LENGTH_SHORT).show()
                        Pre33Op2ExaOpera.setBackgroundColor(Color.rgb(0, 143, 57))//verde
                        Pre33Op1ExaOpera.isEnabled = false
                        Pre33Op2ExaOpera.isEnabled = false
                        Pre33Op3ExaOpera.isEnabled = false
                        Pre33Op4ExaOpera.isEnabled = false
                    }
                R.id.Pre33Op3ExaOpera ->
                    if (checked) {
                        Toast.makeText(this, "Incorrecto", Toast.LENGTH_SHORT).show();
                        Pre33Op3ExaOpera.setBackgroundColor(Color.rgb(255, 0, 0))//rojo
                        Pre33Op1ExaOpera.isEnabled = false
                        Pre33Op2ExaOpera.isEnabled = false
                        Pre33Op3ExaOpera.isEnabled = false
                        Pre33Op4ExaOpera.isEnabled = false
                    }
                R.id.Pre33Op4ExaOpera ->
                    if (checked) {
                        Toast.makeText(this, "Incorrecto", Toast.LENGTH_SHORT).show()
                        Pre33Op4ExaOpera.setBackgroundColor(Color.rgb(255, 0, 0))//rojo
                        Pre33Op1ExaOpera.isEnabled = false
                        Pre33Op2ExaOpera.isEnabled = false
                        Pre33Op3ExaOpera.isEnabled = false
                        Pre33Op4ExaOpera.isEnabled = false
                    }
            }
        }
    }
    fun Pregunta34op(view: View) {
        if (view is RadioButton) {
            val checked = view.isChecked
            when (view.getId()) {
                R.id.Pre34Op1ExaOpera ->
                    if (checked) {
                        Toast.makeText(this, "Incorrecto", Toast.LENGTH_SHORT).show();
                        Pre34Op1ExaOpera.setBackgroundColor(Color.rgb(255, 0, 0))//rojo
                        Pre34Op1ExaOpera.isEnabled = false
                        Pre34Op2ExaOpera.isEnabled = false
                        Pre34Op3ExaOpera.isEnabled = false
                        Pre34Op4ExaOpera.isEnabled = false
                    }
                R.id.Pre34Op2ExaOpera ->
                    if (checked) {
                        califB3++
                        Toast.makeText(this, "Correcta", Toast.LENGTH_SHORT).show()
                        Pre34Op2ExaOpera.setBackgroundColor(Color.rgb(0, 143, 57))//verde
                        Pre34Op1ExaOpera.isEnabled = false
                        Pre34Op2ExaOpera.isEnabled = false
                        Pre34Op3ExaOpera.isEnabled = false
                        Pre34Op4ExaOpera.isEnabled = false
                    }
                R.id.Pre34Op3ExaOpera ->
                    if (checked) {
                        Toast.makeText(this, "Incorrecto", Toast.LENGTH_SHORT).show();
                        Pre34Op3ExaOpera.setBackgroundColor(Color.rgb(255, 0, 0))//rojo
                        Pre34Op1ExaOpera.isEnabled = false
                        Pre34Op2ExaOpera.isEnabled = false
                        Pre34Op3ExaOpera.isEnabled = false
                        Pre34Op4ExaOpera.isEnabled = false
                    }
                R.id.Pre34Op4ExaOpera ->
                    if (checked) {
                        Toast.makeText(this, "Incorrecto", Toast.LENGTH_SHORT).show()
                        Pre34Op4ExaOpera.setBackgroundColor(Color.rgb(255, 0, 0))//rojo
                        Pre34Op1ExaOpera.isEnabled = false
                        Pre34Op2ExaOpera.isEnabled = false
                        Pre34Op3ExaOpera.isEnabled = false
                        Pre34Op4ExaOpera.isEnabled = false
                    }
            }
        }
    }

    fun resultados(){
        var califT: Double = 0.0
        califT = (califB1.toDouble() + califB2.toDouble() + califB3.toDouble()) / 3.4
        df.roundingMode = RoundingMode.CEILING
        calificacion = df.format(califT).toDouble()

        if (8<=calificacion && calificacion<=10){
            percentil = "A"
        }
        if (calificacion<8){
            percentil = "NA"
        }
    }

    fun exOperaResul(){
        resultados()
        val sentencia = "Insert into examen(c_examen,tipo_examen,id_persona,id_usuario,fecha_aplicacion,calif_uno," +
                "calif_dos,calif_tres,resultado,percentil,aManejando,VehiculosD) " +
                "values('$c_examen','$tipoE','$id_persona','$idU','$fech','$califB1','$califB2','$califB3','$calificacion','$percentil','$aManejando','$vehiculosD')"
        val bd = DataBase(this)
        if (bd.Ejecuta(sentencia)) {
            Toast.makeText(this, "Se guardo el examen", Toast.LENGTH_LONG).show()
            val operaResul = Intent(this,resultados_examen_operativo::class.java)
            operaResul.putExtra("calificacionB1", califB1)
            operaResul.putExtra("calificacionB2", califB2)
            operaResul.putExtra("calificacionB3", califB3)
            operaResul.putExtra("calificacion", calificacion)
            operaResul.putExtra("percentil", percentil)
            operaResul.putExtra("nombreAplico", nomU)
            operaResul.putExtra("nombreRespondio", nombreEmp)
            operaResul.putExtra("fecha", fech)
            bd.close()
            startActivity(operaResul)
            finish()
        } else {
            Toast.makeText(this, "No se pudo guardar el examen", Toast.LENGTH_LONG).show()
            val regresar = Intent(this, EjemploExamenAdmin::class.java)
            startActivity(regresar)
            finish()
        }
    }
    fun onClickTerminarOperativo(v: View){
        pauseTimer()
        exOperaResul()
    }
}