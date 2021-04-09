package com.example.auditoriasapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.SearchView
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.example.auditoriasapp.Database.DataBase
import com.example.auditoriasapp.Volley.VolleySingleton
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity__volley__chequeo.*
import kotlinx.coroutines.launch
import org.json.JSONException
import org.json.JSONObject
import java.util.*
import kotlin.collections.ArrayList

class Activity_Volley_Chequeo : AppCompatActivity() {
    var claveChequeo: String = ""

    private lateinit var Revisiones: MutableList<Chequeo>
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
        viewAdapter = ChequeoAdapter(chequeosList, this, { check: Chequeo -> onItemClickListener(check) })

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

        //refrescando el recycler view al hacer el swipe sobre el recycler view
        swipeRefreshLayoutVch.setOnRefreshListener {
            retriveChequeo()
            swipeRefreshLayoutVch.isRefreshing = false
        }

        ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {
            override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean {
                return false
            }
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, swipeDir: Int) {
                val position = viewHolder.adapterPosition
                val cheq = viewAdapter.getTasks()
                claveChequeo = cheq[position].c_che
                val id_p = cheq[position].pers
                val id_c = cheq[position].carro
                val id_u = cheq[position].usuario
                val tipoR= cheq[position].tipr
                val tipoM = cheq[position].tipm
                val kmA = cheq[position].kmAct
                val fechaA = cheq[position].fech_Act
                val kmAnt = cheq[position].kmAnt
                val fechAnt = cheq[position].fechAnt
                val proxR = cheq[position].prox_rv
                val obs = cheq[position].obs

                val admin = DataBase(baseContext)
                if (admin.Ejecuta("DELETE FROM chequeoVolley WHERE c_chequeo = '$claveChequeo'")){
                    val jsonEntrada = JSONObject()
                    jsonEntrada.put("c_chequeo", claveChequeo)
                    sendRequest(Address.IP + "Auditoriapp/Login/borrarRevisiones.php",jsonEntrada)
                    retriveChequeo()
                    Snackbar.make(rv_chequeoVolley,"Se elimino la revisión con núm de inventario: "+cheq[position].inv,
                        Snackbar.LENGTH_LONG).setAction("Deshacer", View.OnClickListener {
                        val sentencia = "INSERT INTO chequeoVolley(c_chequeo, id_persona,id_carro,id_usuario,tipo_revision,tipo_mantenimiento," +
                                "kilometraje_actual,fecha_actual,kilometraje_anterior,fecha_anterior,proxima_revision,observaciones) " +
                                "values ('$claveChequeo','$id_p','$id_c','$id_u','$tipoR','$tipoM','$kmA','$fechaA','$kmAnt','$fechAnt','$proxR','$obs')"
                        if (admin.Ejecuta(sentencia)){
                            try {
                                jsonEntrada.put("c_chequeo",cheq[position].c_che)
                                jsonEntrada.put("id_persona",cheq[position].pers)
                                jsonEntrada.put("id_carro",cheq[position].carro)
                                jsonEntrada.put("id_usuario",cheq[position].usuario)
                                jsonEntrada.put("tipo_revision",cheq[position].tipr)
                                jsonEntrada.put("tipo_mantenimiento",cheq[position].tipm)
                                jsonEntrada.put("kilometraje_actual",cheq[position].kmAct)
                                jsonEntrada.put("fecha_actual",cheq[position].fech_Act)
                                jsonEntrada.put("kilometraje_anterior",cheq[position].kmAnt)
                                jsonEntrada.put("fecha_anterior",cheq[position].fechAnt)
                                jsonEntrada.put("proxima_revision",cheq[position].prox_rv)
                                jsonEntrada.put("observaciones",cheq[position].obs)
                                sendRequest(Address.IP + "Auditoriapp/Login/insertarRevisiones.php",jsonEntrada)
                                Toast.makeText(baseContext,"Se deshizo la acción", Toast.LENGTH_LONG).show()
                                retriveChequeo()
                                admin.close()
                            }catch (e: JSONException){
                                e.printStackTrace()
                                Toast.makeText(baseContext,"No se pudo subir al servidor, intente más tarde",Toast.LENGTH_SHORT).show()
                            }
                        }
                        else{
                            Toast.makeText(baseContext,"No se pudo deshacer el borrado", Toast.LENGTH_SHORT).show()
                            admin.close()
                        }
                    }).show()
                }
                admin.close()
            }
        }).attachToRecyclerView(rv_chequeoVolley)

        //Funcion para buscar dentro del recycler view por nombre
        val searchView : SearchView = findViewById(R.id.searchViewVch)
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                return true
            }
            override fun onQueryTextChange(newText: String?): Boolean {
                Revisiones = java.util.ArrayList()
                var c_chequeo: String
                var pers:Int
                var carr:Int
                var idusu:Int
                var tipor:String
                var tipom:String
                var kmact:Int
                var fecha_ac:String
                var kmant:Int
                var fecha_an:String
                var proxi:String
                var obser:String
                var nomi:String
                var inv:String
                var usu:String
                var nombre:String
                if (newText!!.isNotEmpty()){
                    val search = newText.toLowerCase(Locale.getDefault())
                    val buscar = viewAdapter.getTasks()
                    eraserChequeoV()
                    for (revisiones in buscar){
                        if (revisiones.inv.toLowerCase(Locale.getDefault()).contains(search)){
                            c_chequeo = revisiones.c_che
                            pers = revisiones.pers
                            carr = revisiones.carro
                            idusu = revisiones.usuario
                            tipor = revisiones.tipr
                            tipom = revisiones.tipm
                            kmact = revisiones.kmAct
                            fecha_ac = revisiones.fech_Act
                            kmant = revisiones.kmAnt
                            fecha_an = revisiones.fechAnt
                            proxi = revisiones.prox_rv
                            obser = revisiones.obs
                            nomi = revisiones.nomi
                            inv = revisiones.inv
                            usu = revisiones.usu
                            nombre = revisiones.nomb
                            Revisiones.add(Chequeo(c_chequeo,pers,carr,idusu,tipor,tipom,kmact,fecha_ac,kmant,fecha_an,proxi,obser,nomi,inv,usu,nombre))
                        }
                        viewAdapter.setTasks(Revisiones!!)
                        rv_chequeoVolley.adapter!!.notifyDataSetChanged()
                    }
                }else{
                    eraserChequeoV()
                    retriveChequeo()
                }
                return true
            }
        })
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


    private fun eraserChequeoV(){
        val chequeo = erase()
        viewAdapter.setTasks(chequeo!!)
    }
    fun erase():MutableList<Chequeo>{
        val chequeo : MutableList<Chequeo> = java.util.ArrayList()
        chequeo.clear()
        return chequeo
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