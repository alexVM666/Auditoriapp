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
import com.android.volley.toolbox.JsonObjectRequest
import com.example.auditoriasapp.Database.DataBase
import com.example.auditoriasapp.Volley.VolleySingleton
import kotlinx.android.synthetic.main.activity__volley__chequeo.*
import org.json.JSONObject

class Activity_Volley_Chequeo : AppCompatActivity() {
    var claveChequeo: String = ""

    private lateinit var viewAdapter: ChequeoAdapter
    private lateinit var viewManager: RecyclerView.LayoutManager
    val chequeosList: List<Chequeo> = ArrayList()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity__volley__chequeo)
        finalizarChequeoRVvolley.setOnClickListener {
            finalizarChe()
        }

        viewManager = LinearLayoutManager(this)
        viewAdapter =
            ChequeoAdapter(chequeosList, this, { check: Chequeo -> onItemClickListener(check) })

        rv_chequeoVolley.apply {
            setHasFixedSize(true)
            layoutManager = viewManager
            adapter = viewAdapter
            addItemDecoration(
                DividerItemDecoration(
                    this@Activity_Volley_Chequeo,
                    DividerItemDecoration.VERTICAL)
            )
        }

        ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {
            override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean {
                return false
            }
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, swipeDir: Int) {
                val position = viewHolder.adapterPosition
                val cheq = viewAdapter.getTasks()
                claveChequeo = cheq[position].c_che
                val admin = DataBase(baseContext)
                if (admin.Ejecuta("DELETE FROM chequeoVolley WHERE c_chequeo = '$claveChequeo'")){
                    val jsonEntrada = JSONObject()
                    jsonEntrada.put("c_chequeo", claveChequeo)
                    sendRequest(Address.IP + "Auditoriapp/Login/borrarRevisiones.php",jsonEntrada)
                    retriveChequeo()
                }
            }
        }).attachToRecyclerView(rv_chequeoVolley)
    }

    private fun onItemClickListener(check: Chequeo) {
        val resultCheck = Intent(this,ActivityResultVolleyChequeo::class.java)
        resultCheck.putExtra("c_chequeo",check.c_che)
        resultCheck.putExtra("nomina",check.nomi)
        resultCheck.putExtra("num_economico",check.inv)
        resultCheck.putExtra("correo",check.usu)
        resultCheck.putExtra("tipo_revision",check.tipr)
        resultCheck.putExtra("tipo_mantenimiento",check.tipm)
        resultCheck.putExtra("kilometraje_actual",check.kmAct)
        resultCheck.putExtra("fecha_actual",check.fech_Act)
        resultCheck.putExtra("kilometraje_anterior",check.kmAnt)
        resultCheck.putExtra("fecha_anterior",check.fechAnt)
        resultCheck.putExtra("proxima_revision",check.prox_rv)
        resultCheck.putExtra("observaciones",check.obs)
        resultCheck.putExtra("nombre",check.nomb)
        startActivity(resultCheck)
        finish()
    }
    override fun onResume() {
        super.onResume()
        retriveChequeo()
    }

    private fun retriveChequeo() {
        val checks = getChequeos()
        viewAdapter.setTasks(checks!!)
    }

    private fun getChequeos(): MutableList<Chequeo> {
        val checkk: MutableList<Chequeo> = ArrayList()
        val admin = DataBase(this)

        val tupla = admin.Consulta("SELECT ch.c_chequeo,ch.id_persona,ch.id_carro,ch.id_usuario,ch.tipo_revision,ch.tipo_mantenimiento," +
                "ch.kilometraje_actual,ch.fecha_actual,ch.kilometraje_anterior,ch.fecha_anterior,ch.proxima_revision,ch.observaciones,p.num_nomina,c.inventario,u.nomusuario,p.nombre " +
                "FROM chequeoVolley as ch inner join persona as p on ch.id_persona=p.id_persona inner join carro as c on c.id_carro=ch.id_carro " +
                "inner join usuariosDisponibles as u on ch.id_usuario=u.id_usuario order by ch.tipo_revision")
        while (tupla!!.moveToNext()){
            val c_chequeo = tupla.getString(0)
            val nomi = tupla.getInt(1)
            val num_eco = tupla.getInt(2)
            val corr = tupla.getInt(3)
            val tipor = tupla.getString(4)
            val tipom = tupla.getString(5)
            val kmact =  tupla.getInt(6)
            val fecha_ac = tupla.getString(7)
            val kmant = tupla.getInt(8)
            val fecha_an = tupla.getString(9)
            val proxi = tupla.getString(10)
            val obser = tupla.getString(11)
            val num_nomina = tupla.getString(12)
            val inventario = tupla.getString(13)
            val usuario = tupla.getString(14)
            val nombre = tupla.getString(15)

            checkk.add(Chequeo(c_chequeo,nomi,num_eco,corr,tipor,tipom,kmact,fecha_ac,kmant,fecha_an,proxi,obser,num_nomina,inventario,usuario,nombre))
        }
        tupla.close()
        admin.close()
        return checkk
    }

    private fun finalizarChe() {
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