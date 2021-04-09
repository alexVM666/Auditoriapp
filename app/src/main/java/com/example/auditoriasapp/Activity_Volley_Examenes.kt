package com.example.auditoriasapp

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.SearchView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.example.auditoriasapp.Database.DataBase
import com.example.auditoriasapp.Volley.VolleySingleton
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity__volley__examenes.*
import org.json.JSONException
import org.json.JSONObject
import java.util.*
import kotlin.collections.ArrayList


class Activity_Volley_Examenes : AppCompatActivity() {
    var claveExamen: String = ""

    private lateinit var Examenes: MutableList<Examenes>
    private lateinit var viewAdapter: ExamenesAdapter
    private lateinit var viewManager: RecyclerView.LayoutManager
    val examenesList: List<Examenes> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity__volley__examenes)
        finalizarExamenesVolley.setOnClickListener {
            finalizarExaVolley()
        }
        viewManager = LinearLayoutManager(this)
        viewAdapter = ExamenesAdapter(examenesList, this, { examen: Examenes -> onItemClickListener(examen) })

        rv_examenes_volley.apply {
            setHasFixedSize(true)
            layoutManager = viewManager
            adapter = viewAdapter
            addItemDecoration(
                DividerItemDecoration(
                    this@Activity_Volley_Examenes,
                    DividerItemDecoration.VERTICAL
                )
            )
        }
        //refrescando el recycler view de los examenes del recycler
        swipeRefreshLayoutEx.setOnRefreshListener {
                    retriveExamen()
                    swipeRefreshLayoutEx.isRefreshing = false
        }

        // Metodo para implementar la eliminación de una nomina, cuando el usuario da un onSwiped en el recyclerview
        ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(
            0,
            ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
        ) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, swipeDir: Int) {
                val position = viewHolder.adapterPosition
                val exam = viewAdapter.getTasks()
                claveExamen = exam[position].c_exa
                val tipoEx = exam[position].t_examen
                val aMane = exam[position].aMane
                val vehiD = exam[position].Vehi
                val idP = exam[position].idP
                val idUsu = exam[position].idU
                val fech = exam[position].fecha_apl
                val calif1 = exam[position].calif_1
                val calif2 = exam[position].calif_2
                val calif3 = exam[position].calif_3
                val res = exam[position].resul
                val per = exam[position].perce

                val admin = DataBase(baseContext)
                if (admin.Ejecuta("DELETE FROM examenVolley WHERE c_examen = '$claveExamen'")) {
                    var jsonEntrada = JSONObject()
                    jsonEntrada.put("c_examen", claveExamen)
                    sendRequest(Address.IP + "Auditoriapp/Login/borrarExamen.php", jsonEntrada)
                    retriveExamen()
                    Snackbar.make(
                        rv_examenes_volley,
                        "Se elimino el examen con clave: " + exam[position].c_exa,
                        Snackbar.LENGTH_LONG
                    ).setAction("Deshacer", View.OnClickListener {
                        val sentencia =
                            "INSERT INTO examenVolley(c_examen,tipo_examen,aManejando,VehiculosD,id_persona,id_usuario,fecha_aplicacion,calif_uno,calif_dos,calif_tres,resultado,percentil) " +
                                    "values ('$claveExamen','$tipoEx','$aMane','$vehiD','$idP','$idUsu','$fech','$calif1','$calif2','$calif3','$res','$per')"
                        if (admin.Ejecuta(sentencia)) {
                            try {
                                jsonEntrada = JSONObject()
                                jsonEntrada.put("c_examen", exam[position].c_exa)
                                jsonEntrada.put("tipo_examen", exam[position].t_examen)
                                jsonEntrada.put("id_persona", exam[position].idP)
                                jsonEntrada.put("id_usuario", exam[position].idU)
                                jsonEntrada.put("fecha_aplicacion", exam[position].fecha_apl)
                                jsonEntrada.put("calif_uno", exam[position].calif_1)
                                jsonEntrada.put("calif_dos", exam[position].calif_2)
                                jsonEntrada.put("calif_tres", exam[position].calif_3)
                                jsonEntrada.put("resultado", exam[position].resul)
                                jsonEntrada.put("percentil", exam[position].perce)
                                jsonEntrada.put("aManejando", exam[position].aMane)
                                jsonEntrada.put("VehiculosD", exam[position].Vehi)
                                sendRequest(
                                    Address.IP + "Auditoriapp/Login/insertarExamenes.php",
                                    jsonEntrada
                                )
                                Toast.makeText(
                                    baseContext,
                                    "Se deshizo la acción",
                                    Toast.LENGTH_LONG
                                ).show()
                                retriveExamen()
                                admin.close()
                            } catch (e: JSONException) {
                                e.printStackTrace()
                                Toast.makeText(
                                    baseContext,
                                    "No se pudo subir al servidor, intente más tarde",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        } else {
                            Toast.makeText(
                                baseContext,
                                "No se pudo deshacer el borrado",
                                Toast.LENGTH_SHORT
                            ).show()
                            admin.close()
                        }
                    }).show()
                }
                admin.close()
            }
        }).attachToRecyclerView(rv_examenes_volley)

        //Funcion para buscar dentro del recycler view por
        val searchView : SearchView = findViewById(R.id.searchViewEx)
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                Examenes = java.util.ArrayList()
                var c_examen: String
                var tipoEx: String
                var aMane: Int
                var vehiD: String
                var idPer: String
                var idUsu: String
                var fecha: String
                var calif1: Int
                var calif2: Int
                var calif3: Int
                var res: Double
                var perc: String
                var nomina: String
                var usuario: String
                var nombre: String

                if (newText!!.isNotEmpty()) {
                    val search = newText.toLowerCase(Locale.getDefault())
                    val buscar = viewAdapter.getTasks()
                    eraserExamenes()
                    for (examen in buscar) {
                        if (examen.nomb.toLowerCase(Locale.getDefault()).contains(search)) {
                            c_examen = examen.c_exa
                            tipoEx = examen.t_examen
                            aMane = examen.aMane
                            vehiD = examen.Vehi
                            idPer = examen.idP
                            idUsu = examen.idU
                            fecha = examen.fecha_apl
                            calif1 = examen.calif_1
                            calif2 = examen.calif_2
                            calif3 = examen.calif_3
                            res = examen.resul
                            perc = examen.perce
                            nomina = examen.nomi
                            usuario = examen.usu
                            nombre = examen.nomb
                            Examenes.add(
                                Examenes(
                                    c_examen,
                                    tipoEx,
                                    idPer,
                                    idUsu,
                                    fecha,
                                    calif1,
                                    calif2,
                                    calif3,
                                    res,
                                    perc,
                                    aMane,
                                    vehiD,
                                    nomina,
                                    usuario,
                                    nombre
                                )
                            )
                        }
                        viewAdapter.setTask(Examenes!!)
                        rv_examenes_volley.adapter!!.notifyDataSetChanged()
                    }
                } else {
                    eraserExamenes()
                    retriveExamen()
                }
                return true
            }
        })
    }

    private fun onItemClickListener(examen: Examenes) {
        val resultExa = Intent(this, ActivityResultVolleyExamenes::class.java)
        resultExa.putExtra("c_examen", examen.c_exa)
        resultExa.putExtra("tipo_examen", examen.t_examen)
        resultExa.putExtra("nomina", examen.nomi)
        resultExa.putExtra("correo", examen.usu)
        resultExa.putExtra("fecha_aplicacion", examen.fecha_apl)
        resultExa.putExtra("calif1", examen.calif_1)
        resultExa.putExtra("calif2", examen.calif_2)
        resultExa.putExtra("calif3", examen.calif_3)
        resultExa.putExtra("resultado", examen.resul)
        resultExa.putExtra("percentil", examen.perce)
        resultExa.putExtra("aManejando", examen.aMane)
        resultExa.putExtra("VehiculosD", examen.Vehi)
        resultExa.putExtra("nombre", examen.nomb)
        startActivity(resultExa)
        finish()
    }
    override fun onResume() {
        super.onResume()
        retriveExamen()
    }
    private fun eraserExamenes(){
        val Exams = erase()
        viewAdapter.setTask(Exams!!)
    }
    fun erase():MutableList<Examenes>{
        var Exameness : MutableList<Examenes> = java.util.ArrayList()
        Exameness.clear()
        return Exameness
    }
    

    private fun retriveExamen() {
        val examens = getExamenes()
        viewAdapter.setTask(examens!!)
    }

    private fun getExamenes(): MutableList<Examenes> {
        val exaa: MutableList<Examenes> = ArrayList()
        val admin = DataBase(this)

        val tupla = admin.Consulta(
            "SELECT e.c_examen,e.tipo_examen,e.id_persona,e.id_usuario,e.fecha_aplicacion,e.calif_uno,e.calif_dos," +
                    " e.calif_tres,e.resultado,e.percentil,e.aManejando,e.VehiculosD,p.num_nomina,u.nomusuario,p.nombre FROM " +
                    "examenVolley as e inner join persona as p on e.id_persona=p.id_persona " +
                    "inner join usuariosDisponibles as u on e.id_usuario=u.id_usuario order by e.tipo_examen"
        )
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
            exaa.add(
                Examenes(
                    c_exaa,
                    t_exaa,
                    nom,
                    corr,
                    fecha_a,
                    calif1,
                    calif2,
                    calif3,
                    resultad,
                    percent,
                    aManeja,
                    vehiculosD,
                    nomi,
                    usu,
                    nombre
                )
            )
        }
        tupla.close()
        admin.close()
        return exaa
    }

    private fun finalizarExaVolley() {
        val finalizarUN = Intent(this, menuActivity::class.java)
        startActivity(finalizarUN)
        finish()
    }

    fun sendRequest(wsURL: String, jsonEntrada: JSONObject){
        val jsonObjectRequest = JsonObjectRequest(
            Request.Method.POST, wsURL, jsonEntrada,
            { response ->
                val succ = response["success"]
                val msg = response["message"]
                Toast.makeText(this, "Success:${succ}  Message:${msg}", Toast.LENGTH_SHORT).show()
            },
            { error ->
                Toast.makeText(this, "${error.message}", Toast.LENGTH_SHORT).show()
                Log.d("ERROR", "${error.message}")
                Toast.makeText(this, "Error, por favor checa URL", Toast.LENGTH_SHORT).show()
            }
        )
        VolleySingleton.getInstance(this).addToRequestQueue(jsonObjectRequest)
    }
}