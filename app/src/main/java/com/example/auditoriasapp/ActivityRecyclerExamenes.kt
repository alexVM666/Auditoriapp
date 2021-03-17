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
import kotlinx.android.synthetic.main.activity_recycler_examenes.*
import org.json.JSONObject

class ActivityRecyclerExamenes : AppCompatActivity() {
    private lateinit var idU: String

    var claveExamen: String = ""

    private lateinit var viewAdapter: ExamenesAdapter
    private lateinit var viewManager: RecyclerView.LayoutManager
    val examenesList: List<Examenes> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recycler_examenes)
        val examen = intent
        if (examen != null && examen.hasExtra("id_usuario")){
            idU = examen.getStringExtra("id_usuario")
        }
        finalizarExamenesRV.setOnClickListener {
            finalizarExa()
        }
        viewManager = LinearLayoutManager(this)
        viewAdapter = ExamenesAdapter(examenesList, this,{ examen: Examenes -> onItemClickListener(examen)  })

        rv_examenes.apply {
            setHasFixedSize(true)
            layoutManager = viewManager
            adapter = viewAdapter
            addItemDecoration(
                DividerItemDecoration(this@ActivityRecyclerExamenes,
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
                if (admin.Ejecuta("DELETE FROM examen WHERE c_examen = '$claveExamen'")){
                    retriveExamen()
                }
            }
        }).attachToRecyclerView(rv_examenes)
    }

    private fun onItemClickListener(examen: Examenes) {
        val jsonEntrada = JSONObject()
        jsonEntrada.put("c_examen", examen.c_exa)
        jsonEntrada.put("tipo_examen",examen.t_examen)
        jsonEntrada.put("id_persona",examen.idP)
        jsonEntrada.put("id_usuario",examen.idU)
        jsonEntrada.put("fecha_aplicacion",examen.fecha_apl)
        jsonEntrada.put("calif_uno",examen.calif_1)
        jsonEntrada.put("calif_dos",examen.calif_2)
        jsonEntrada.put("calif_tres",examen.calif_3)
        jsonEntrada.put("resultado",examen.resul)
        jsonEntrada.put("percentil",examen.perce)
        jsonEntrada.put("aManejando",examen.aMane)
        jsonEntrada.put("VehiculosD",examen.Vehi)
        sendRequest(Address.IP + "Auditoriapp/Login/insertarExamenes.php",jsonEntrada)
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
                "e.calif_tres,e.resultado,e.percentil,e.aManejando,e.VehiculosD,p.num_nomina,u.nomusuario,p.nombre FROM examen as e inner join persona as p on p.id_persona=e.id_persona inner join usuario as u on u.id_usuario=e.id_usuario where e.id_usuario = '$idU' order by e.tipo_examen")
        while (tupla!!.moveToNext()){
            val c_exaa = tupla.getString(0)
            val t_exaa = tupla.getString(1)
            val idP = tupla.getString(2)
            val idU = tupla.getString(3)
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
            exaa.add(Examenes(c_exaa,t_exaa,idP,idU,fecha_a,calif1,calif2,calif3,resultad,percent,aManeja,vehiculosD,nomi,usu,nombre))
        }
        tupla.close()
        admin.close()
        return exaa
    }

    private fun finalizarExa() {
        val finalizarUN = Intent(this,menuActivity::class.java)
        startActivity(finalizarUN)
        finish()
    }

    fun sendRequest( wsURL: String, jsonEntrada: JSONObject){
        val jsonObjectRequest = JsonObjectRequest(
            Request.Method.POST, wsURL,jsonEntrada,
            { response ->
                val succ = response["success"]
                val msg = response["message"]
                Toast.makeText(this, "Success:${succ}  Message:${msg}", Toast.LENGTH_SHORT).show()
            },
            { error ->
                Toast.makeText(this, "${error.message}", Toast.LENGTH_SHORT).show()
                Log.d("ERROR","${error.message}");
                Toast.makeText(this, "Error, por favor checa URL", Toast.LENGTH_SHORT).show()
            }
        )
        VolleySingleton.getInstance(this).addToRequestQueue(jsonObjectRequest)
    }
}