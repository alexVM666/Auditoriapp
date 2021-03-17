package com.example.auditoriasapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.example.auditoriasapp.Database.DataBase
import com.example.auditoriasapp.Volley.VolleySingleton
import kotlinx.android.synthetic.main.activity__volley__examenes.*
import org.json.JSONObject

class Activity_Volley_Examenes : AppCompatActivity() {
    var claveExamen: String = ""

    private lateinit var viewAdapter: ExamenesAdapter
    private lateinit var viewManager: RecyclerView.LayoutManager
    val examenesList: List<Examenes> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity__volley__examenes)
        val cuest = intent
        finalizarExamenesVolley.setOnClickListener {
            finalizarExaVolley()
        }
        viewManager = LinearLayoutManager(this)
        viewAdapter = ExamenesAdapter(examenesList, this,{ examen: Examenes -> onItemClickListener(examen)  })

        rv_examenes_volley.apply {
            setHasFixedSize(true)
            layoutManager = viewManager
            adapter = viewAdapter
            addItemDecoration(
                DividerItemDecoration(this@Activity_Volley_Examenes,
                    DividerItemDecoration.VERTICAL)
            )
        }
        // Metodo para implementar la eliminación de una nomina, cuando el usuario da un onSwiped en el recyclerview
        ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {
            override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean {
                return false
            }
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, swipeDir: Int) {
                val position = viewHolder.adapterPosition
                val exam = viewAdapter.getTasks()
                claveExamen = exam[position].c_exa
                val admin = DataBase(baseContext)
                if (admin.Ejecuta("DELETE FROM examenVolley WHERE c_examen = '$claveExamen'")){
                    val jsonEntrada = JSONObject()
                    jsonEntrada.put("c_examen", claveExamen)
                    sendRequest(Address.IP + "Auditoriapp/Login/borrarExamen.php",jsonEntrada)
                    retriveExamen()
                }
            }
        }).attachToRecyclerView(rv_examenes_volley)
    }

    private fun onItemClickListener(examen: Examenes) {
        val resultExa = Intent(this,ActivityResultVolleyExamenes::class.java)
        resultExa.putExtra("c_examen",examen.c_exa)
        resultExa.putExtra("tipo_examen",examen.t_examen)
        resultExa.putExtra("nomina",examen.nomi)
        resultExa.putExtra("correo",examen.usu)
        resultExa.putExtra("fecha_aplicacion",examen.fecha_apl)
        resultExa.putExtra("calif1",examen.calif_1)
        resultExa.putExtra("calif2",examen.calif_2)
        resultExa.putExtra("calif3",examen.calif_3)
        resultExa.putExtra("resultado",examen.resul)
        resultExa.putExtra("percentil",examen.perce)
        resultExa.putExtra("aManejando",examen.aMane)
        resultExa.putExtra("VehiculosD",examen.Vehi)
        resultExa.putExtra("nombre",examen.nomb)
        startActivity(resultExa)
        finish()
    }
    override fun onResume() {
        super.onResume()
        retriveExamen()
    }

    private fun retriveExamen() {
        val examens = getExamenes()
        viewAdapter.setTask(examens!!)
    }

    private fun getExamenes(): MutableList<Examenes> {
        val exaa: MutableList<Examenes> = ArrayList()
        val admin = DataBase(this)

        val tupla = admin.Consulta("SELECT e.c_examen,e.tipo_examen,e.id_persona,e.id_usuario,e.fecha_aplicacion,e.calif_uno,e.calif_dos," +
                " e.calif_tres,e.resultado,e.percentil,e.aManejando,e.VehiculosD,p.num_nomina,u.nomusuario,p.nombre FROM " +
                "examenVolley as e inner join persona as p on e.id_persona=p.id_persona " +
                "inner join usuariosDisponibles as u on e.id_usuario=u.id_usuario order by e.tipo_examen")
        while (tupla!!.moveToNext()){
            val c_exaa = tupla.getString(0)
            val t_exaa = tupla.getString(1)
            val nom = tupla.getString(2)
            val corr = tupla.getString(3)
            val fecha_a = tupla.getString(4)
            val calif1 = tupla.getInt(5)
            val calif2 =  tupla.getInt(6)
            val calif3 = tupla.getInt(7)
            val resultad = tupla.getDouble(8)
            val percent = tupla.getString(9)
            val aManeja = tupla.getInt(10)
            val vehiculosD = tupla.getString(11)
            val nomi = tupla.getString(12)
            val usu = tupla.getString(13)
            val nombre = tupla.getString(14)
            exaa.add(Examenes(c_exaa,t_exaa,nom,corr,fecha_a,calif1,calif2,calif3,resultad,percent,aManeja,vehiculosD,nomi,usu,nombre))
        }
        tupla.close()
        admin.close()
        return exaa
    }

    private fun finalizarExaVolley() {
        val finalizarUN = Intent(this,menuActivity::class.java)
        startActivity(finalizarUN)
        finish()
    }

    fun sendRequest( wsURL: String, jsonEntrada: JSONObject){
        val jsonObjectRequest = JsonObjectRequest(
            Request.Method.POST, wsURL,jsonEntrada,
            Response.Listener { response ->
                val succ = response["success"]
                val msg = response["message"]
                Toast.makeText(this, "Success:${succ}  Message:${msg}", Toast.LENGTH_SHORT).show()
            },
            Response.ErrorListener{ error ->
                Toast.makeText(this, "${error.message}", Toast.LENGTH_SHORT).show()
                Log.d("ERROR","${error.message}");
                Toast.makeText(this, "Error, por favor checa URL", Toast.LENGTH_SHORT).show()
            }
        )
        VolleySingleton.getInstance(this).addToRequestQueue(jsonObjectRequest)
    }
}