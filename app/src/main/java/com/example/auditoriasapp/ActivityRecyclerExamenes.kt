package com.example.auditoriasapp

import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.SearchView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.example.auditoriasapp.Database.DataBase
import com.example.auditoriasapp.Volley.VolleySingleton
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_recycler_examenes.*
import org.json.JSONException
import org.json.JSONObject
import java.util.*
import kotlin.collections.ArrayList

class ActivityRecyclerExamenes : AppCompatActivity() {
    private lateinit var idU: String

    var claveExamen: String = ""

    private lateinit var Examenes: MutableList<Examenes>
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
        //Refrescando el recycler view
        swipeRefreshLayoutExa.setOnRefreshListener {
            retriveExamen()
            swipeRefreshLayoutExa.isRefreshing = false
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
                if (admin.Ejecuta("DELETE FROM examen WHERE c_examen = '$claveExamen'")){
                    retriveExamen()
                    Snackbar.make(rv_examenes,"Se elimino el examen con clave: "+exam[position].c_exa,
                        Snackbar.LENGTH_LONG).setAction("Deshacer", View.OnClickListener {
                        val sentencia = "INSERT INTO examen(c_examen,tipo_examen,aManejando,VehiculosD,id_persona,id_usuario,fecha_aplicacion,calif_uno,calif_dos,calif_tres,resultado,percentil) " +
                                "values ('$claveExamen','$tipoEx','$aMane','$vehiD','$idP','$idUsu','$fech','$calif1','$calif2','$calif3','$res','$per')"
                        if (admin.Ejecuta(sentencia)){
                            Toast.makeText(baseContext,"Se deshizo la acción",Toast.LENGTH_LONG).show()
                            retriveExamen()
                            admin.close()
                        }
                        else{
                            Toast.makeText(baseContext,"No se pudo deshacer el borrado",Toast.LENGTH_SHORT).show()
                            admin.close()
                        }
                    }).show()
                }
                admin.close()
            }
        }).attachToRecyclerView(rv_examenes)

        //Funcion para buscar dentro del recycler view por
        val searchView : SearchView = findViewById(R.id.searchViewExa)
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                return true
            }
            override fun onQueryTextChange(newText: String?): Boolean {
                Examenes = java.util.ArrayList()
                var c_examen: String
                var tipoEx:String
                var aMane:Int
                var vehiD:String
                var idPer:String
                var idUsu:String
                var fecha:String
                var calif1: Int
                var calif2: Int
                var calif3: Int
                var res:Double
                var perc:String
                var nomina:String
                var usuario:String
                var nombre:String

                if (newText!!.isNotEmpty()){
                    var search = newText.toLowerCase(Locale.getDefault())
                    var buscar = viewAdapter.getTasks()
                    eraserExamenes()
                    for (examen in buscar){
                        if (examen.nomb.toLowerCase(Locale.getDefault()).contains(search)){
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
                            Examenes.add(Examenes(c_examen,tipoEx,idPer,idUsu,fecha,calif1,calif2,calif3,res,perc,aMane,vehiD,nomina,usuario,nombre))
                        }
                        viewAdapter.setTask(Examenes!!)
                        rv_examenes.adapter!!.notifyDataSetChanged()
                    }
                }else{
                    eraserExamenes()
                    retriveExamen()
                }
                return true
            }
        })
    }




    private fun onItemClickListener(examen: Examenes) {
        val builder = AlertDialog.Builder(this@ActivityRecyclerExamenes)
        builder.setTitle("¿Quieres subir al servidor este Cuestionario?")
        builder.setCancelable(true)
        builder.setMessage("Respondido por la nómina: "+ examen.nomi)
        builder.setNegativeButton("Cancelar", DialogInterface.OnClickListener{
                dialog, which ->
            Toast.makeText(this,"Cancelado",Toast.LENGTH_SHORT).show()
        })
        builder.setPositiveButton("Confirmar", DialogInterface.OnClickListener{
                dialog, which ->
            try {
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
            }catch (e: JSONException){
                e.printStackTrace()
                Toast.makeText(this,"No se pudo subir al servidor, intente más tarde",Toast.LENGTH_SHORT).show()
            }
        })
        builder.show()
    }


    override fun onResume() {
        super.onResume()
        retriveExamen()
    }

    private fun retriveExamen() {
        val examens = getExamenes()
        viewAdapter.setTask(examens!!)
    }

    private fun eraserExamenes(){
        val Exams = erase()
        viewAdapter.setTask(Exams!!)
    }
    fun erase():MutableList<Examenes>{
        val Exameness : MutableList<Examenes> = java.util.ArrayList()
        Exameness.clear()
        return Exameness
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