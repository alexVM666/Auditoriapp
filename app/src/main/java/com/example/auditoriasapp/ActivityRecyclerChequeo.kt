package com.example.auditoriasapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.SearchView
import android.widget.Toast
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.auditoriasapp.Database.DataBase
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_recycler_chequeo.*
import java.util.*
import kotlin.collections.ArrayList

class ActivityRecyclerChequeo : AppCompatActivity() {
    private lateinit var idU: String
    var claveChequeo: String = ""

    private lateinit var Revisiones: MutableList<Chequeo>
    private lateinit var viewAdapter: ChequeoAdapter
    private lateinit var viewManager: RecyclerView.LayoutManager
    val chequeosList: List<Chequeo> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recycler_chequeo)
        val chequeos = intent
        if (chequeos != null && chequeos.hasExtra("id_usuario")) {
            idU = chequeos.getStringExtra("id_usuario")
        }
        finalizarChequeoRV.setOnClickListener {
            finalizarExa()
        }
        viewManager = LinearLayoutManager(this)
        viewAdapter =
            ChequeoAdapter(chequeosList, this, { check: Chequeo -> onItemClickListener(check) })

        rv_chequeo.apply {
            setHasFixedSize(true)
            layoutManager = viewManager
            adapter = viewAdapter
            addItemDecoration(
                DividerItemDecoration(
                    this@ActivityRecyclerChequeo,
                    DividerItemDecoration.VERTICAL)
            )
        }

        //refrescando el recycler view al hacer swipe hacia abajo sobre el recycler view
        swipeRefreshLayout.setOnRefreshListener {
            retriveChequeo()
            swipeRefreshLayout.isRefreshing = false
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
                if (admin.Ejecuta("DELETE FROM chequeo WHERE c_chequeo = '$claveChequeo'")){
                    retriveChequeo()
                    Snackbar.make(rv_chequeo,"Se elimino la revisión con núm de inventario: "+cheq[position].inv,
                        Snackbar.LENGTH_LONG).setAction("Deshacer", View.OnClickListener {
                        val sentencia = "INSERT INTO chequeo(c_chequeo, id_persona,id_carro,id_usuario,tipo_revision,tipo_mantenimiento," +
                                "kilometraje_actual,fecha_actual,kilometraje_anterior,fecha_anterior,proxima_revision,observaciones) " +
                                "values ('$claveChequeo','$id_p','$id_c','$id_u','$tipoR','$tipoM','$kmA','$fechaA','$kmAnt','$fechAnt','$proxR','$obs')"
                        if (admin.Ejecuta(sentencia)){
                            Toast.makeText(baseContext,"Se deshizo la acción", Toast.LENGTH_LONG).show()
                            retriveChequeo()
                            admin.close()
                        }
                        else{
                            Toast.makeText(baseContext,"No se pudo deshacer el borrado", Toast.LENGTH_SHORT).show()
                            admin.close()
                        }
                    }).show()
                }
                admin.close()
            }
        }).attachToRecyclerView(rv_chequeo)

        //Funcion para buscar dentro del recycler view por nombre
        val searchView : SearchView = findViewById(R.id.searchView)
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
                    var search = newText.toLowerCase(Locale.getDefault())
                    var buscar = viewAdapter.getTasks()
                    eraserChequeo()
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
                        rv_chequeo.adapter!!.notifyDataSetChanged()
                    }
                }else{
                    eraserChequeo()
                    retriveChequeo()
                }
                return true
            }
        })
    }
    private fun onItemClickListener(check: Chequeo) {
        val resultCheck = Intent(this,ActivityResultadosChequeo::class.java)
        resultCheck.putExtra("c_chequeo",check.c_che)
        resultCheck.putExtra("id_persona",check.pers)
        resultCheck.putExtra("id_carro",check.carro)
        resultCheck.putExtra("id_usuario",check.usuario)
        resultCheck.putExtra("tipo_revision",check.tipr)
        resultCheck.putExtra("tipo_mantenimiento",check.tipm)
        resultCheck.putExtra("kilometraje_actual",check.kmAct)
        resultCheck.putExtra("fecha_actual",check.fech_Act)
        resultCheck.putExtra("kilometraje_anterior",check.kmAnt)
        resultCheck.putExtra("fecha_anterior",check.fechAnt)
        resultCheck.putExtra("proxima_revision",check.prox_rv)
        resultCheck.putExtra("observaciones",check.obs)
        resultCheck.putExtra("num_nomina",check.nomi)
        resultCheck.putExtra("inventario",check.inv)
        resultCheck.putExtra("usuario",check.usu)
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
        val tupla = admin.Consulta("SELECT c.c_chequeo,c.id_persona,c.id_carro,c.id_usuario,c.tipo_revision" +
                ",c.tipo_mantenimiento,c.kilometraje_actual,c.fecha_actual,c.kilometraje_anterior,c.fecha_anterior,c.proxima_revision,c.observaciones,p.num_nomina," +
                "ca.inventario, u.nomusuario, p.nombre FROM chequeo as c inner join persona as p on c.id_persona=p.id_persona inner join carro as ca on ca.id_carro=c.id_carro " +
                "inner join usuario as u on u.id_usuario=c.id_usuario where c.id_usuario = '$idU' order by c.tipo_revision")
        while (tupla!!.moveToNext()){
            val c_chequeo = tupla.getString(0)
            val pers = tupla.getInt(1)
            val carr = tupla.getInt(2)
            val idusu = tupla.getInt(3)
            val tipor = tupla.getString(4)
            val tipom = tupla.getString(5)
            val kmact =  tupla.getInt(6)
            val fecha_ac = tupla.getString(7)
            val kmant = tupla.getInt(8)
            val fecha_an = tupla.getString(9)
            val proxi = tupla.getString(10)
            val obser = tupla.getString(11)
            val nomi = tupla.getString(12)
            val inv = tupla.getString(13)
            val usu = tupla.getString(14)
            val nombre = tupla.getString(15)
            checkk.add(Chequeo(c_chequeo,pers,carr,idusu,tipor,tipom,kmact,fecha_ac,kmant,fecha_an,proxi,obser,nomi,inv,usu,nombre))
        }
        tupla.close()
        admin.close()
        return checkk
    }

    private fun eraserChequeo(){
        val chequeo = erase()
        viewAdapter.setTasks(chequeo!!)
    }
    fun erase():MutableList<Chequeo>{
        var chequeo : MutableList<Chequeo> = java.util.ArrayList()
        chequeo.clear()
        return chequeo
    }

    private fun finalizarExa() {
        val finalizarUN = Intent(this,menuActivity::class.java)
        startActivity(finalizarUN)
        finish()
    }
}