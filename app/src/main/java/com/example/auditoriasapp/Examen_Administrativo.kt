package com.example.auditoriasapp

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.os.CountDownTimer
import android.view.View
import android.widget.RadioButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.auditoriasapp.Database.DataBase
import kotlinx.android.synthetic.main.activity_examen__administrativo.*
import java.math.RoundingMode
import java.text.DecimalFormat


class Examen_Administrativo : AppCompatActivity() {

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
        setContentView(R.layout.activity_examen__administrativo)
        val examenAd = intent
        if (examenAd != null && examenAd.hasExtra("id_usuario") && examenAd.hasExtra("usuario")&& examenAd.hasExtra("id_persona")) {
            tipoE = examenAd.getStringExtra("tipoExamen")
            id_persona = examenAd.getIntExtra("id_persona",0)
            nombreEmp = examenAd.getStringExtra("nombreEmp")
            idU = examenAd.getStringExtra("id_usuario")
            nomU = examenAd.getStringExtra("usuario")
            area = examenAd.getStringExtra("area")
            vehiculosD = examenAd.getStringExtra("vehiculosD")
            tipoNomina = examenAd.getStringExtra("tipoNomina")
            aManejando = examenAd.getStringExtra("aManejendo")
            fech = examenAd.getStringExtra("fechaAplicacion")
            c_examen = examenAd.getStringExtra("c_examen")
        }
        //Tiempo: 10 minutos.
        reverseTimer(Seconds = 600)
    }

    fun reverseTimer(Seconds: Int) {
        countdowntimer = object : CountDownTimer((Seconds * 1000 + 1000).toLong(), 1000) {
            override fun onTick(millisUntilFinished: Long) {
                var seconds = (millisUntilFinished / 1000).toInt()
                val minutes = seconds / 60
                seconds = seconds % 60
                RelojExAdmin.text = "Tiempo Restante: " + String.format("%02d", minutes) + ":" + String.format(
                    "%02d" + " Minutos.",
                    seconds
                )
            }
            override fun onFinish() {
                RelojExAdmin.text = "Terminado"
                exAdminResul()
            }
        }.start()
    }
    private fun pauseTimer(){
        countdowntimer.cancel()
    }

    fun onRadioButtonClicked(view: View) {
        if (view is RadioButton) {
            val checked = view.isChecked
            when (view.getId()) {
                R.id.Pre1Op1ExaAdmin ->
                    if (checked) {
                        califB1++
                        Toast.makeText(this, "Correcta", Toast.LENGTH_SHORT).show()
                        Pre1Op1ExaAdmin.setBackgroundColor(Color.rgb(0, 143, 57))
                        Pre1Op1ExaAdmin.isEnabled = false
                        Pre1Op2ExaAdmin.isEnabled = false
                        Pre1Op3ExaAdmin.isEnabled = false
                        Pre1Op4ExaAdmin.isEnabled = false
                    }
                R.id.Pre1Op2ExaAdmin ->
                    if (checked) {
                        Toast.makeText(this, "Incorrecto", Toast.LENGTH_SHORT).show()
                        Pre1Op2ExaAdmin.setBackgroundColor(Color.rgb(255, 0, 0))
                        Pre1Op1ExaAdmin.isEnabled = false
                        Pre1Op2ExaAdmin.isEnabled = false
                        Pre1Op3ExaAdmin.isEnabled = false
                        Pre1Op4ExaAdmin.isEnabled = false
                    }
                R.id.Pre1Op3ExaAdmin ->
                    if (checked) {
                        Toast.makeText(this, "Incorrecto", Toast.LENGTH_SHORT).show()
                        Pre1Op3ExaAdmin.setBackgroundColor(Color.rgb(255, 0, 0))
                        Pre1Op1ExaAdmin.isEnabled = false
                        Pre1Op2ExaAdmin.isEnabled = false
                        Pre1Op3ExaAdmin.isEnabled = false
                        Pre1Op4ExaAdmin.isEnabled = false
                    }
                R.id.Pre1Op4ExaAdmin ->
                    if (checked) {
                        Toast.makeText(this, "Incorrecto", Toast.LENGTH_SHORT).show()
                        Pre1Op4ExaAdmin.setBackgroundColor(Color.rgb(255, 0, 0))
                        Pre1Op1ExaAdmin.isEnabled = false
                        Pre1Op2ExaAdmin.isEnabled = false
                        Pre1Op3ExaAdmin.isEnabled = false
                        Pre1Op4ExaAdmin.isEnabled = false
                    }
            }
        }
    }

    fun Pregunta2(view: View) {
        if (view is RadioButton) {
            val checked = view.isChecked
            when (view.getId()) {
                R.id.Pre2Op1ExaAdmin ->
                    if (checked) {
                        Toast.makeText(this, "Incorrecto", Toast.LENGTH_SHORT).show();
                        Pre2Op1ExaAdmin.setBackgroundColor(Color.rgb(255, 0, 0))//rojo
                        Pre2Op1ExaAdmin.isEnabled = false
                        Pre2Op2ExaAdmin.isEnabled = false
                        Pre2Op3ExaAdmin.isEnabled = false
                        Pre2Op4ExaAdmin.isEnabled = false
                    }
                R.id.Pre2Op2ExaAdmin ->
                    if (checked) {
                        califB1++
                        Toast.makeText(this, "Correcta", Toast.LENGTH_SHORT).show()
                        Pre2Op2ExaAdmin.setBackgroundColor(Color.rgb(0, 143, 57))//verde
                        Pre2Op1ExaAdmin.isEnabled = false
                        Pre2Op2ExaAdmin.isEnabled = false
                        Pre2Op3ExaAdmin.isEnabled = false
                        Pre2Op4ExaAdmin.isEnabled = false
                    }
                R.id.Pre2Op3ExaAdmin ->
                    if (checked) {
                        Toast.makeText(this, "Incorrecto", Toast.LENGTH_SHORT).show();
                        Pre2Op3ExaAdmin.setBackgroundColor(Color.rgb(255, 0, 0))//rojo
                        Pre2Op1ExaAdmin.isEnabled = false
                        Pre2Op2ExaAdmin.isEnabled = false
                        Pre2Op3ExaAdmin.isEnabled = false
                        Pre2Op4ExaAdmin.isEnabled = false
                    }
                R.id.Pre2Op4ExaAdmin ->
                    if (checked) {
                        Toast.makeText(this, "Incorrecto", Toast.LENGTH_SHORT).show()
                        Pre2Op4ExaAdmin.setBackgroundColor(Color.rgb(255, 0, 0))//rojo
                        Pre2Op1ExaAdmin.isEnabled = false
                        Pre2Op2ExaAdmin.isEnabled = false
                        Pre2Op3ExaAdmin.isEnabled = false
                        Pre2Op4ExaAdmin.isEnabled = false
                    }
            }
        }
    }

    fun Pregunta3(view: View) {
        if (view is RadioButton) {
            val checked = view.isChecked
            when (view.getId()) {
                R.id.Pre3Op1ExaAdmin ->
                    if (checked) {
                        califB1++
                        Toast.makeText(this, "Correcta", Toast.LENGTH_SHORT).show()
                        Pre3Op1ExaAdmin.setBackgroundColor(Color.rgb(0, 143, 57))//verde
                        Pre3Op1ExaAdmin.isEnabled = false
                        Pre3Op2ExaAdmin.isEnabled = false
                        Pre3Op3ExaAdmin.isEnabled = false
                        Pre3Op4ExaAdmin.isEnabled = false
                    }
                R.id.Pre3Op2ExaAdmin ->
                    if (checked) {
                        Toast.makeText(this, "Incorrecto", Toast.LENGTH_SHORT).show();
                        Pre3Op2ExaAdmin.setBackgroundColor(Color.rgb(255, 0, 0))//rojo
                        Pre3Op1ExaAdmin.isEnabled = false
                        Pre3Op2ExaAdmin.isEnabled = false
                        Pre3Op3ExaAdmin.isEnabled = false
                        Pre3Op4ExaAdmin.isEnabled = false
                    }
                R.id.Pre3Op3ExaAdmin ->
                    if (checked) {
                        Toast.makeText(this, "Incorrecto", Toast.LENGTH_SHORT).show();
                        Pre3Op3ExaAdmin.setBackgroundColor(Color.rgb(255, 0, 0))//rojo
                        Pre3Op1ExaAdmin.isEnabled = false
                        Pre3Op2ExaAdmin.isEnabled = false
                        Pre3Op3ExaAdmin.isEnabled = false
                        Pre3Op4ExaAdmin.isEnabled = false
                    }
                R.id.Pre3Op4ExaAdmin ->
                    if (checked) {
                        Toast.makeText(this, "Incorrecto", Toast.LENGTH_SHORT).show()
                        Pre3Op4ExaAdmin.setBackgroundColor(Color.rgb(255, 0, 0))//rojo
                        Pre3Op1ExaAdmin.isEnabled = false
                        Pre3Op2ExaAdmin.isEnabled = false
                        Pre3Op3ExaAdmin.isEnabled = false
                        Pre3Op4ExaAdmin.isEnabled = false
                    }
            }
        }
    }

    fun Pregunta4(view: View) {
        if (view is RadioButton) {
            val checked = view.isChecked
            when (view.getId()) {
                R.id.Pre4Op1ExaAdmin ->
                    if (checked) {
                        Toast.makeText(this, "Incorrecto", Toast.LENGTH_SHORT).show()
                        Pre4Op1ExaAdmin.setBackgroundColor(Color.rgb(255, 0, 0))//rojo
                        Pre4Op1ExaAdmin.isEnabled = false
                        Pre4Op2ExaAdmin.isEnabled = false
                        Pre4Op3ExaAdmin.isEnabled = false
                        Pre4Op4ExaAdmin.isEnabled = false
                    }
                R.id.Pre4Op2ExaAdmin ->
                    if (checked) {
                        Toast.makeText(this, "Incorrecto", Toast.LENGTH_SHORT).show();
                        Pre4Op2ExaAdmin.setBackgroundColor(Color.rgb(255, 0, 0))//rojo
                        Pre4Op1ExaAdmin.isEnabled = false
                        Pre4Op2ExaAdmin.isEnabled = false
                        Pre4Op3ExaAdmin.isEnabled = false
                        Pre4Op4ExaAdmin.isEnabled = false
                    }
                R.id.Pre4Op3ExaAdmin ->
                    if (checked) {
                        Toast.makeText(this, "Incorrecto", Toast.LENGTH_SHORT).show();
                        Pre4Op3ExaAdmin.setBackgroundColor(Color.rgb(255, 0, 0))//rojo
                        Pre4Op1ExaAdmin.isEnabled = false
                        Pre4Op2ExaAdmin.isEnabled = false
                        Pre4Op3ExaAdmin.isEnabled = false
                        Pre4Op4ExaAdmin.isEnabled = false
                    }
                R.id.Pre4Op4ExaAdmin ->
                    if (checked) {
                        califB1++
                        Toast.makeText(this, "Correcta", Toast.LENGTH_SHORT).show()
                        Pre4Op4ExaAdmin.setBackgroundColor(Color.rgb(0, 143, 57))//verde
                        Pre4Op1ExaAdmin.isEnabled = false
                        Pre4Op2ExaAdmin.isEnabled = false
                        Pre4Op3ExaAdmin.isEnabled = false
                        Pre4Op4ExaAdmin.isEnabled = false
                    }
            }
        }
    }
    fun Pregunta5(view: View) {
        if (view is RadioButton) {
            val checked = view.isChecked
            when (view.getId()) {
                R.id.Pre5Op1ExaAdmin ->
                    if (checked) {
                        Toast.makeText(this, "Incorrecto", Toast.LENGTH_SHORT).show()
                        Pre5Op1ExaAdmin.setBackgroundColor(Color.rgb(255, 0, 0))//rojo
                        Pre5Op1ExaAdmin.isEnabled = false
                        Pre5Op2ExaAdmin.isEnabled = false
                        Pre5Op3ExaAdmin.isEnabled = false
                        Pre5Op4ExaAdmin.isEnabled = false
                    }
                R.id.Pre5Op2ExaAdmin ->
                    if (checked) {
                        califB1++
                        Toast.makeText(this, "Correcta", Toast.LENGTH_SHORT).show()
                        Pre5Op2ExaAdmin.setBackgroundColor(Color.rgb(0, 143, 57))//verde
                        Pre5Op1ExaAdmin.isEnabled = false
                        Pre5Op2ExaAdmin.isEnabled = false
                        Pre5Op3ExaAdmin.isEnabled = false
                        Pre5Op4ExaAdmin.isEnabled = false
                    }
                R.id.Pre5Op3ExaAdmin ->
                    if (checked) {
                        Toast.makeText(this, "Incorrecto", Toast.LENGTH_SHORT).show();
                        Pre5Op3ExaAdmin.setBackgroundColor(Color.rgb(255, 0, 0))//rojo
                        Pre5Op1ExaAdmin.isEnabled = false
                        Pre5Op2ExaAdmin.isEnabled = false
                        Pre5Op3ExaAdmin.isEnabled = false
                        Pre5Op4ExaAdmin.isEnabled = false
                    }
                R.id.Pre5Op4ExaAdmin ->
                    if (checked) {
                        Toast.makeText(this, "Incorrecto", Toast.LENGTH_SHORT).show();
                        Pre5Op4ExaAdmin.setBackgroundColor(Color.rgb(255, 0, 0))//rojo
                        Pre5Op1ExaAdmin.isEnabled = false
                        Pre5Op2ExaAdmin.isEnabled = false
                        Pre5Op3ExaAdmin.isEnabled = false
                        Pre5Op4ExaAdmin.isEnabled = false
                    }
            }
        }
    }

    fun Pregunta6(view: View) {
        if (view is RadioButton) {
            val checked = view.isChecked
            when (view.getId()) {
                R.id.Pre6Op1ExaAdmin ->
                    if (checked) {
                        califB2++
                        Toast.makeText(this, "Correcta", Toast.LENGTH_SHORT).show()
                        Pre6Op1ExaAdmin.setBackgroundColor(Color.rgb(0, 143, 57))//verde
                        Pre6Op1ExaAdmin.isEnabled = false
                        Pre6Op2ExaAdmin.isEnabled = false
                        Pre6Op3ExaAdmin.isEnabled = false
                        Pre6Op4ExaAdmin.isEnabled = false
                    }
                R.id.Pre6Op2ExaAdmin ->
                    if (checked) {
                        Toast.makeText(this, "Incorrecto", Toast.LENGTH_SHORT).show()
                        Pre6Op2ExaAdmin.setBackgroundColor(Color.rgb(255, 0, 0))//rojo
                        Pre6Op1ExaAdmin.isEnabled = false
                        Pre6Op2ExaAdmin.isEnabled = false
                        Pre6Op3ExaAdmin.isEnabled = false
                        Pre6Op4ExaAdmin.isEnabled = false
                    }
                R.id.Pre6Op3ExaAdmin ->
                    if (checked) {
                        Toast.makeText(this, "Incorrecto", Toast.LENGTH_SHORT).show();
                        Pre6Op3ExaAdmin.setBackgroundColor(Color.rgb(255, 0, 0))//rojo
                        Pre6Op1ExaAdmin.isEnabled = false
                        Pre6Op2ExaAdmin.isEnabled = false
                        Pre6Op3ExaAdmin.isEnabled = false
                        Pre6Op4ExaAdmin.isEnabled = false
                    }
                R.id.Pre6Op4ExaAdmin ->
                    if (checked) {
                        Toast.makeText(this, "Incorrecto", Toast.LENGTH_SHORT).show();
                        Pre6Op4ExaAdmin.setBackgroundColor(Color.rgb(255, 0, 0))//rojo
                        Pre6Op1ExaAdmin.isEnabled = false
                        Pre6Op2ExaAdmin.isEnabled = false
                        Pre6Op3ExaAdmin.isEnabled = false
                        Pre6Op4ExaAdmin.isEnabled = false
                    }
            }
        }
    }

    fun Pregunta7(view: View) {
        if (view is RadioButton) {
            val checked = view.isChecked
            when (view.getId()) {
                R.id.Pre7Op1ExaAdmin ->
                    if (checked) {
                        Toast.makeText(this, "Incorrecto", Toast.LENGTH_SHORT).show()
                        Pre7Op1ExaAdmin.setBackgroundColor(Color.rgb(255, 0, 0))//rojo
                        Pre7Op1ExaAdmin.isEnabled = false
                        Pre7Op2ExaAdmin.isEnabled = false
                        Pre7Op3ExaAdmin.isEnabled = false
                        Pre7Op4ExaAdmin.isEnabled = false
                    }
                R.id.Pre7Op2ExaAdmin ->
                    if (checked) {
                        califB2++
                        Toast.makeText(this, "Correcta", Toast.LENGTH_SHORT).show()
                        Pre7Op2ExaAdmin.setBackgroundColor(Color.rgb(0, 143, 57))//verde
                        Pre7Op1ExaAdmin.isEnabled = false
                        Pre7Op2ExaAdmin.isEnabled = false
                        Pre7Op3ExaAdmin.isEnabled = false
                        Pre7Op4ExaAdmin.isEnabled = false
                    }
                R.id.Pre7Op3ExaAdmin ->
                    if (checked) {
                        Toast.makeText(this, "Incorrecto", Toast.LENGTH_SHORT).show();
                        Pre7Op3ExaAdmin.setBackgroundColor(Color.rgb(255, 0, 0))//rojo
                        Pre7Op1ExaAdmin.isEnabled = false
                        Pre7Op2ExaAdmin.isEnabled = false
                        Pre7Op3ExaAdmin.isEnabled = false
                        Pre7Op4ExaAdmin.isEnabled = false
                    }
                R.id.Pre7Op4ExaAdmin ->
                    if (checked) {
                        Toast.makeText(this, "Incorrecto", Toast.LENGTH_SHORT).show();
                        Pre7Op4ExaAdmin.setBackgroundColor(Color.rgb(255, 0, 0))//rojo
                        Pre7Op1ExaAdmin.isEnabled = false
                        Pre7Op2ExaAdmin.isEnabled = false
                        Pre7Op3ExaAdmin.isEnabled = false
                        Pre7Op4ExaAdmin.isEnabled = false
                    }
            }
        }
    }
    fun Pregunta8(view: View) {
        if (view is RadioButton) {
            val checked = view.isChecked
            when (view.getId()) {
                R.id.Pre8Op1ExaAdmin ->
                    if (checked) {
                        califB2++
                        Toast.makeText(this, "Correcta", Toast.LENGTH_SHORT).show()
                        Pre8Op1ExaAdmin.setBackgroundColor(Color.rgb(0, 143, 57))//verde
                        Pre8Op1ExaAdmin.isEnabled = false
                        Pre8Op2ExaAdmin.isEnabled = false
                        Pre8Op3ExaAdmin.isEnabled = false
                        Pre8Op4ExaAdmin.isEnabled = false
                    }
                R.id.Pre8Op2ExaAdmin ->
                    if (checked) {
                        Toast.makeText(this, "Incorrecto", Toast.LENGTH_SHORT).show()
                        Pre8Op2ExaAdmin.setBackgroundColor(Color.rgb(255, 0, 0))//rojo
                        Pre8Op1ExaAdmin.isEnabled = false
                        Pre8Op2ExaAdmin.isEnabled = false
                        Pre8Op3ExaAdmin.isEnabled = false
                        Pre8Op4ExaAdmin.isEnabled = false
                    }
                R.id.Pre8Op3ExaAdmin ->
                    if (checked) {
                        Toast.makeText(this, "Incorrecto", Toast.LENGTH_SHORT).show();
                        Pre8Op3ExaAdmin.setBackgroundColor(Color.rgb(255, 0, 0))//rojo
                        Pre8Op1ExaAdmin.isEnabled = false
                        Pre8Op2ExaAdmin.isEnabled = false
                        Pre8Op3ExaAdmin.isEnabled = false
                        Pre8Op4ExaAdmin.isEnabled = false
                    }
                R.id.Pre8Op4ExaAdmin ->
                    if (checked) {
                        Toast.makeText(this, "Incorrecto", Toast.LENGTH_SHORT).show();
                        Pre8Op4ExaAdmin.setBackgroundColor(Color.rgb(255, 0, 0))//rojo
                        Pre8Op1ExaAdmin.isEnabled = false
                        Pre8Op2ExaAdmin.isEnabled = false
                        Pre8Op3ExaAdmin.isEnabled = false
                        Pre8Op4ExaAdmin.isEnabled = false
                    }
            }
        }
    }

    fun Pregunta9(view: View) {
        if (view is RadioButton) {
            val checked = view.isChecked
            when (view.getId()) {
                R.id.Pre9Op1ExaAdmin ->
                    if (checked) {
                        Toast.makeText(this, "Incorrecto", Toast.LENGTH_SHORT).show()
                        Pre9Op1ExaAdmin.setBackgroundColor(Color.rgb(255, 0, 0))//rojo
                        Pre9Op1ExaAdmin.isEnabled = false
                        Pre9Op2ExaAdmin.isEnabled = false
                        Pre9Op3ExaAdmin.isEnabled = false
                        Pre9Op4ExaAdmin.isEnabled = false
                    }
                R.id.Pre9Op2ExaAdmin ->
                    if (checked) {
                        califB2++
                        Toast.makeText(this, "Correcta", Toast.LENGTH_SHORT).show()
                        Pre9Op2ExaAdmin.setBackgroundColor(Color.rgb(0, 143, 57))//verde
                        Pre9Op1ExaAdmin.isEnabled = false
                        Pre9Op2ExaAdmin.isEnabled = false
                        Pre9Op3ExaAdmin.isEnabled = false
                        Pre9Op4ExaAdmin.isEnabled = false
                    }
                R.id.Pre9Op3ExaAdmin ->
                    if (checked) {
                        Toast.makeText(this, "Incorrecto", Toast.LENGTH_SHORT).show();
                        Pre9Op3ExaAdmin.setBackgroundColor(Color.rgb(255, 0, 0))//rojo
                        Pre9Op1ExaAdmin.isEnabled = false
                        Pre9Op2ExaAdmin.isEnabled = false
                        Pre9Op3ExaAdmin.isEnabled = false
                        Pre9Op4ExaAdmin.isEnabled = false
                    }
                R.id.Pre9Op4ExaAdmin ->
                    if (checked) {
                        Toast.makeText(this, "Incorrecto", Toast.LENGTH_SHORT).show();
                        Pre9Op4ExaAdmin.setBackgroundColor(Color.rgb(255, 0, 0))//rojo
                        Pre9Op1ExaAdmin.isEnabled = false
                        Pre9Op2ExaAdmin.isEnabled = false
                        Pre9Op3ExaAdmin.isEnabled = false
                        Pre9Op4ExaAdmin.isEnabled = false
                    }
            }
        }
    }
    fun Pregunta10(view: View) {
        if (view is RadioButton) {
            val checked = view.isChecked
            when (view.getId()) {
                R.id.Pre10Op1ExaAdmin ->
                    if (checked) {
                        Toast.makeText(this, "Incorrecto", Toast.LENGTH_SHORT).show()
                        Pre10Op1ExaAdmin.setBackgroundColor(Color.rgb(255, 0, 0))//rojo
                        Pre10Op1ExaAdmin.isEnabled = false
                        Pre10Op2ExaAdmin.isEnabled = false
                        Pre10Op3ExaAdmin.isEnabled = false
                        Pre10Op4ExaAdmin.isEnabled = false
                    }
                R.id.Pre10Op2ExaAdmin ->
                    if (checked) {
                        califB2++
                        Toast.makeText(this, "Correcta", Toast.LENGTH_SHORT).show()
                        Pre10Op2ExaAdmin.setBackgroundColor(Color.rgb(0, 143, 57))//verde
                        Pre10Op1ExaAdmin.isEnabled = false
                        Pre10Op2ExaAdmin.isEnabled = false
                        Pre10Op3ExaAdmin.isEnabled = false
                        Pre10Op4ExaAdmin.isEnabled = false
                    }
                R.id.Pre10Op3ExaAdmin ->
                    if (checked) {
                        Toast.makeText(this, "Incorrecto", Toast.LENGTH_SHORT).show();
                        Pre10Op3ExaAdmin.setBackgroundColor(Color.rgb(255, 0, 0))//rojo
                        Pre10Op1ExaAdmin.isEnabled = false
                        Pre10Op2ExaAdmin.isEnabled = false
                        Pre10Op3ExaAdmin.isEnabled = false
                        Pre10Op4ExaAdmin.isEnabled = false
                    }
                R.id.Pre10Op4ExaAdmin ->
                    if (checked) {
                        Toast.makeText(this, "Incorrecto", Toast.LENGTH_SHORT).show();
                        Pre10Op4ExaAdmin.setBackgroundColor(Color.rgb(255, 0, 0))//rojo
                        Pre10Op1ExaAdmin.isEnabled = false
                        Pre10Op2ExaAdmin.isEnabled = false
                        Pre10Op3ExaAdmin.isEnabled = false
                        Pre10Op4ExaAdmin.isEnabled = false
                    }
            }
        }
    }
    fun Pregunta11(view: View) {
        if (view is RadioButton) {
            val checked = view.isChecked
            when (view.getId()) {
                R.id.Pre11Op1ExaAdmin ->
                    if (checked) {
                        Toast.makeText(this, "Incorrecto", Toast.LENGTH_SHORT).show()
                        Pre11Op1ExaAdmin.setBackgroundColor(Color.rgb(255, 0, 0))//rojo
                        Pre11Op1ExaAdmin.isEnabled = false
                        Pre11Op2ExaAdmin.isEnabled = false
                        Pre11Op3ExaAdmin.isEnabled = false
                        Pre11Op4ExaAdmin.isEnabled = false
                    }
                R.id.Pre11Op2ExaAdmin ->
                    if (checked) {
                        Toast.makeText(this, "Incorrecto", Toast.LENGTH_SHORT).show();
                        Pre11Op2ExaAdmin.setBackgroundColor(Color.rgb(255, 0, 0))//rojo
                        Pre11Op1ExaAdmin.isEnabled = false
                        Pre11Op2ExaAdmin.isEnabled = false
                        Pre11Op3ExaAdmin.isEnabled = false
                        Pre11Op4ExaAdmin.isEnabled = false
                    }
                R.id.Pre11Op3ExaAdmin ->
                    if (checked) {
                        califB3++
                        Toast.makeText(this, "Correcta", Toast.LENGTH_SHORT).show()
                        Pre11Op3ExaAdmin.setBackgroundColor(Color.rgb(0, 143, 57))//verde
                        Pre11Op1ExaAdmin.isEnabled = false
                        Pre11Op2ExaAdmin.isEnabled = false
                        Pre11Op3ExaAdmin.isEnabled = false
                        Pre11Op4ExaAdmin.isEnabled = false
                    }
                R.id.Pre11Op4ExaAdmin ->
                    if (checked) {
                        Toast.makeText(this, "Incorrecto", Toast.LENGTH_SHORT).show();
                        Pre11Op4ExaAdmin.setBackgroundColor(Color.rgb(255, 0, 0))//rojo
                        Pre11Op1ExaAdmin.isEnabled = false
                        Pre11Op2ExaAdmin.isEnabled = false
                        Pre11Op3ExaAdmin.isEnabled = false
                        Pre11Op4ExaAdmin.isEnabled = false
                    }
            }
        }
    }
    fun Pregunta12(view: View) {
        if (view is RadioButton) {
            val checked = view.isChecked
            when (view.getId()) {
                R.id.Pre12Op1ExaAdmin ->
                    if (checked) {
                        Toast.makeText(this, "Incorrecto", Toast.LENGTH_SHORT).show()
                        Pre12Op1ExaAdmin.setBackgroundColor(Color.rgb(255, 0, 0))//rojo
                        Pre12Op1ExaAdmin.isEnabled = false
                        Pre12Op2ExaAdmin.isEnabled = false
                        Pre12Op3ExaAdmin.isEnabled = false
                        Pre12Op4ExaAdmin.isEnabled = false
                    }
                R.id.Pre12Op2ExaAdmin ->
                    if (checked) {
                        Toast.makeText(this, "Incorrecto", Toast.LENGTH_SHORT).show();
                        Pre12Op2ExaAdmin.setBackgroundColor(Color.rgb(255, 0, 0))//rojo
                        Pre12Op1ExaAdmin.isEnabled = false
                        Pre12Op2ExaAdmin.isEnabled = false
                        Pre12Op3ExaAdmin.isEnabled = false
                        Pre12Op4ExaAdmin.isEnabled = false
                    }
                R.id.Pre12Op3ExaAdmin ->
                    if (checked) {
                        Toast.makeText(this, "Incorrecto", Toast.LENGTH_SHORT).show();
                        Pre12Op3ExaAdmin.setBackgroundColor(Color.rgb(255, 0, 0))//rojo
                        Pre12Op1ExaAdmin.isEnabled = false
                        Pre12Op2ExaAdmin.isEnabled = false
                        Pre12Op3ExaAdmin.isEnabled = false
                        Pre12Op4ExaAdmin.isEnabled = false
                    }
                R.id.Pre12Op4ExaAdmin ->
                    if (checked) {
                        califB3++
                        Toast.makeText(this, "Correcta", Toast.LENGTH_SHORT).show()
                        Pre12Op4ExaAdmin.setBackgroundColor(Color.rgb(0, 143, 57))//verde
                        Pre12Op1ExaAdmin.isEnabled = false
                        Pre12Op2ExaAdmin.isEnabled = false
                        Pre12Op3ExaAdmin.isEnabled = false
                        Pre12Op4ExaAdmin.isEnabled = false
                    }
            }
        }
    }
    fun Pregunta13(view: View) {
        if (view is RadioButton) {
            val checked = view.isChecked
            when (view.getId()) {
                R.id.Pre13Op1ExaAdmin ->
                    if (checked) {
                        Toast.makeText(this, "Incorrecto", Toast.LENGTH_SHORT).show()
                        Pre13Op1ExaAdmin.setBackgroundColor(Color.rgb(255, 0, 0))//rojo
                        Pre13Op1ExaAdmin.isEnabled = false
                        Pre13Op2ExaAdmin.isEnabled = false
                        Pre13Op3ExaAdmin.isEnabled = false
                        Pre13Op4ExaAdmin.isEnabled = false
                    }
                R.id.Pre13Op2ExaAdmin ->
                    if (checked) {
                        Toast.makeText(this, "Incorrecto", Toast.LENGTH_SHORT).show();
                        Pre13Op2ExaAdmin.setBackgroundColor(Color.rgb(255, 0, 0))//rojo
                        Pre13Op1ExaAdmin.isEnabled = false
                        Pre13Op2ExaAdmin.isEnabled = false
                        Pre13Op3ExaAdmin.isEnabled = false
                        Pre13Op4ExaAdmin.isEnabled = false
                    }
                R.id.Pre13Op3ExaAdmin ->
                    if (checked) {
                        califB3++
                        Toast.makeText(this, "Correcta", Toast.LENGTH_SHORT).show()
                        Pre13Op3ExaAdmin.setBackgroundColor(Color.rgb(0, 143, 57))//verde
                        Pre13Op1ExaAdmin.isEnabled = false
                        Pre13Op2ExaAdmin.isEnabled = false
                        Pre13Op3ExaAdmin.isEnabled = false
                        Pre13Op4ExaAdmin.isEnabled = false
                    }
                R.id.Pre13Op4ExaAdmin ->
                    if (checked) {
                        Toast.makeText(this, "Incorrecto", Toast.LENGTH_SHORT).show();
                        Pre13Op4ExaAdmin.setBackgroundColor(Color.rgb(255, 0, 0))//rojo
                        Pre13Op1ExaAdmin.isEnabled = false
                        Pre13Op2ExaAdmin.isEnabled = false
                        Pre13Op3ExaAdmin.isEnabled = false
                        Pre13Op4ExaAdmin.isEnabled = false
                    }
            }
        }
    }
    fun Pregunta14(view: View) {
        if (view is RadioButton) {
            val checked = view.isChecked
            when (view.getId()) {
                R.id.Pre14Op1ExaAdmin ->
                    if (checked) {
                        califB3++
                        Toast.makeText(this, "Correcta", Toast.LENGTH_SHORT).show()
                        Pre14Op1ExaAdmin.setBackgroundColor(Color.rgb(0, 143, 57))//verde
                        Pre14Op1ExaAdmin.isEnabled = false
                        Pre14Op2ExaAdmin.isEnabled = false
                        Pre14Op3ExaAdmin.isEnabled = false
                        Pre14Op4ExaAdmin.isEnabled = false
                    }
                R.id.Pre14Op2ExaAdmin ->
                    if (checked) {
                        Toast.makeText(this, "Incorrecto", Toast.LENGTH_SHORT).show()
                        Pre14Op2ExaAdmin.setBackgroundColor(Color.rgb(255, 0, 0))//rojo
                        Pre14Op1ExaAdmin.isEnabled = false
                        Pre14Op2ExaAdmin.isEnabled = false
                        Pre14Op3ExaAdmin.isEnabled = false
                        Pre14Op4ExaAdmin.isEnabled = false
                    }
                R.id.Pre14Op3ExaAdmin ->
                    if (checked) {
                        Toast.makeText(this, "Incorrecto", Toast.LENGTH_SHORT).show();
                        Pre14Op3ExaAdmin.setBackgroundColor(Color.rgb(255, 0, 0))//rojo
                        Pre14Op1ExaAdmin.isEnabled = false
                        Pre14Op2ExaAdmin.isEnabled = false
                        Pre14Op3ExaAdmin.isEnabled = false
                        Pre14Op4ExaAdmin.isEnabled = false
                    }
                R.id.Pre14Op4ExaAdmin ->
                    if (checked) {
                        Toast.makeText(this, "Incorrecto", Toast.LENGTH_SHORT).show();
                        Pre14Op4ExaAdmin.setBackgroundColor(Color.rgb(255, 0, 0))//rojo
                        Pre14Op1ExaAdmin.isEnabled = false
                        Pre14Op2ExaAdmin.isEnabled = false
                        Pre14Op3ExaAdmin.isEnabled = false
                        Pre14Op4ExaAdmin.isEnabled = false
                    }
            }
        }
    }
    fun Pregunta15(view: View) {
        if (view is RadioButton) {
            val checked = view.isChecked
            when (view.getId()) {
                R.id.Pre15Op1ExaAdmin ->
                    if (checked) {
                        Toast.makeText(this, "Incorrecto", Toast.LENGTH_SHORT).show()
                        Pre15Op1ExaAdmin.setBackgroundColor(Color.rgb(255, 0, 0))//rojo
                        Pre15Op1ExaAdmin.isEnabled = false
                        Pre15Op2ExaAdmin.isEnabled = false
                        Pre15Op3ExaAdmin.isEnabled = false
                        Pre15Op4ExaAdmin.isEnabled = false
                    }
                R.id.Pre15Op2ExaAdmin ->
                    if (checked) {
                        califB3++
                        Toast.makeText(this, "Correcta", Toast.LENGTH_SHORT).show()
                        Pre15Op2ExaAdmin.setBackgroundColor(Color.rgb(0, 143, 57))//verde
                        Pre15Op1ExaAdmin.isEnabled = false
                        Pre15Op2ExaAdmin.isEnabled = false
                        Pre15Op3ExaAdmin.isEnabled = false
                        Pre15Op4ExaAdmin.isEnabled = false
                    }
                R.id.Pre15Op3ExaAdmin ->
                    if (checked) {
                        Toast.makeText(this, "Incorrecto", Toast.LENGTH_SHORT).show();
                        Pre15Op3ExaAdmin.setBackgroundColor(Color.rgb(255, 0, 0))//rojo
                        Pre15Op1ExaAdmin.isEnabled = false
                        Pre15Op2ExaAdmin.isEnabled = false
                        Pre15Op3ExaAdmin.isEnabled = false
                        Pre15Op4ExaAdmin.isEnabled = false
                    }
                R.id.Pre15Op4ExaAdmin ->
                    if (checked) {
                        Toast.makeText(this, "Incorrecto", Toast.LENGTH_SHORT).show();
                        Pre15Op4ExaAdmin.setBackgroundColor(Color.rgb(255, 0, 0))//rojo
                        Pre15Op1ExaAdmin.isEnabled = false
                        Pre15Op2ExaAdmin.isEnabled = false
                        Pre15Op3ExaAdmin.isEnabled = false
                        Pre15Op4ExaAdmin.isEnabled = false
                    }
            }
        }
    }
    fun resultados(){
        var califT: Double = 0.0
        califT = (califB1.toDouble() + califB2.toDouble() + califB3.toDouble()) / 3
        df.roundingMode = RoundingMode.CEILING
        calificacion = df.format(califT).toDouble()

        if (3.5<=calificacion && calificacion<=5){
            percentil = "A"
        }
        if (calificacion<3.5){
            percentil = "NA"
        }
    }

    fun exAdminResul(){
        resultados()
        var sentencia = "Insert into examen(c_examen,tipo_examen,id_persona,id_usuario,fecha_aplicacion,calif_uno,calif_dos,calif_tres,resultado,percentil,aManejando,VehiculosD) " +
                "values('$c_examen','$tipoE','$id_persona','$idU','$fech','$califB1','$califB2','$califB3','$calificacion','$percentil','$aManejando','$vehiculosD')"
        val bd = DataBase(this)
        if (bd.Ejecuta(sentencia)) {
            Toast.makeText(this, "Se guardo el examen", Toast.LENGTH_LONG).show()
            val examenResul = Intent(this, ActivityResultadosExamenAdmin::class.java)
            examenResul.putExtra("calificacionB1", califB1)
            examenResul.putExtra("calificacionB2", califB2)
            examenResul.putExtra("calificacionB3", califB3)
            examenResul.putExtra("calificacion", calificacion)
            examenResul.putExtra("percentil", percentil)
            examenResul.putExtra("nombreAplico", nomU)
            examenResul.putExtra("nombreRespondio", nombreEmp)
            examenResul.putExtra("fecha", fech)
            bd.close()
            startActivity(examenResul)
            finish()
        } else {
            Toast.makeText(this, "No se pudo guardar el examen", Toast.LENGTH_LONG).show();
            val regresar = Intent(this, EjemploExamenAdmin::class.java)
            startActivity(regresar)
            finish()
        }
    }
    fun onClickTerminar(v: View){
        pauseTimer()
        exAdminResul()
    }
}