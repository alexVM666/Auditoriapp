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
import kotlinx.android.synthetic.main.activity_recycler_auditorias.*
import org.json.JSONException
import org.json.JSONObject
import java.util.*
import kotlin.collections.ArrayList

class RecyclerAuditorias : AppCompatActivity() {
    private lateinit var idU: String
    var claveAudit: String = ""

    private lateinit var Auditorias: MutableList<Auditorias>
    private lateinit var viewAdapter: AuditoriasAdapter
    private lateinit var viewManager: RecyclerView.LayoutManager
    val auditoriasList: List<Auditorias> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recycler_auditorias)
        val audit = intent
        if (audit != null && audit.hasExtra("id_usuario")){
            idU = audit.getStringExtra("id_usuario")
        }
        finalizarAuditoriasRV.setOnClickListener {
            finalizarAu()
        }
        viewManager = LinearLayoutManager(this)
        viewAdapter = AuditoriasAdapter(auditoriasList, this,{ auditorr: Auditorias -> onItemClickListener(auditorr)  })

        rv_auditorias.apply {
            setHasFixedSize(true)
            layoutManager = viewManager
            adapter = viewAdapter
            addItemDecoration(DividerItemDecoration(this@RecyclerAuditorias,DividerItemDecoration.VERTICAL))
        }

        //Refrescando el recycler view al hacer swipe hacia abajo del recycler view
        swipeRefreshLayoutAud.setOnRefreshListener {
            retriveAuditorias()
            swipeRefreshLayoutAud.isRefreshing = false
        }

        // Metodo para implementar la eliminación de una nomina, cuando el usuario da un onSwiped en el recyclerview
        ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {
            override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean {
                return false
            }
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, swipeDir: Int) {
                val position = viewHolder.adapterPosition
                val auditor = viewAdapter.getTasks()
                claveAudit = auditor[position].c_audi
                val t_audi = auditor[position].t_audi
                val id_per = auditor[position].idP
                val id_c = auditor[position].idCar
                val fecha = auditor[position].fechaa
                val mot = auditor[position].motorr
                val carr = auditor[position].carroc
                val interior = auditor[position].inter
                val aditam = auditor[position].adit
                val equipo = auditor[position].equipo
                val n_confor = auditor[position].n_confor
                val conclu = auditor[position].conclu
                val idU = auditor[position].idUs
                val llantas = auditor[position].llantas
                val cinturones = auditor[position].cinturones
                val bolsas = auditor[position].bolsasA
                val testigos = auditor[position].testigos
                val admin = DataBase(baseContext)
                if (admin.Ejecuta("DELETE FROM auditorias WHERE c_auditorias = '$claveAudit'")){
                    retriveAuditorias()
                    Snackbar.make(rv_auditorias,"Se elimino la Auditoria : "+auditor[position].c_audi,
                        Snackbar.LENGTH_LONG).setAction("Deshacer", View.OnClickListener {
                        val sentencia = "INSERT INTO auditorias(c_auditorias,t_auditoria,id_persona,id_carro,fecha,motor,carroceria,interior,aditamentos,equipo_tactico,n_conformidades,conclusion,id_usuario,fechallantas,cinturones,bolsasAire,testigosTablero)" +
                                " values('$claveAudit','$t_audi','$id_per','$id_c','$fecha','$mot','$carr','$interior','$aditam','$equipo','$n_confor','$conclu','$idU','$llantas','$cinturones','$bolsas','$testigos')"
                        if (admin.Ejecuta(sentencia)){
                            Toast.makeText(baseContext,"Se deshizo la acción",Toast.LENGTH_LONG).show()
                            retriveAuditorias()
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
        }).attachToRecyclerView(rv_auditorias)

        //Funcion para buscar dentro del recycler view por nombre
        val searchView : SearchView = findViewById(R.id.searchViewAud)
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                return true
            }
            override fun onQueryTextChange(newText: String?): Boolean {
                Auditorias = java.util.ArrayList()
                var c_audi: String
                var t_audi: String
                var idP:String
                var idC:String
                var idUS:String
                var fecha:String
                var moto:String
                var carroc:String
                var int:String
                var adit:String
                var equip:String
                var n_confor:String
                var concl:String
                var llantas:String
                var cint:String
                var bolsas:String
                var testigos:String
                var nomi:String
                var inven:String
                var nomU:String
                var nombre:String
                if (newText!!.isNotEmpty()){
                    var search = newText.toLowerCase(Locale.getDefault())
                    var buscar = viewAdapter.getTasks()
                    eraserAuditorias()
                    for (auditoria in buscar){
                        if (auditoria.inv.toLowerCase(Locale.getDefault()).contains(search)){
                            c_audi = auditoria.c_audi
                            t_audi = auditoria.t_audi
                            idP = auditoria.idP
                            idC = auditoria.idCar
                            idUS = auditoria.idUs
                            fecha = auditoria.fechaa
                            moto = auditoria.motorr
                            carroc = auditoria.carroc
                            int = auditoria.inter
                            adit = auditoria.adit
                            equip = auditoria.equipo
                            n_confor = auditoria.n_confor
                            concl = auditoria.conclu
                            llantas = auditoria.llantas
                            cint = auditoria.cinturones
                            bolsas = auditoria.bolsasA
                            testigos = auditoria.testigos
                            nomi = auditoria.nomi
                            inven = auditoria.inv
                            nomU = auditoria.usu
                            nombre = auditoria.nomb
                            Auditorias.add(Auditorias(c_audi,t_audi,idP,idC,idUS,fecha,moto,carroc,int,adit,equip,n_confor,concl,llantas,cint,bolsas,testigos,nomi,inven,nomU,nombre))
                        }
                        viewAdapter.setTask(Auditorias!!)
                        rv_auditorias.adapter!!.notifyDataSetChanged()
                    }
                }else{
                    eraserAuditorias()
                    retriveAuditorias()
                }
                return true
            }
        })
    }

    private fun onItemClickListener(auditorr: Auditorias) {
        val builder = AlertDialog.Builder(this@RecyclerAuditorias)
        builder.setTitle("¿Quieres subir al servidor esta Auditoria?")
        builder.setCancelable(true)
        builder.setMessage("Aplicada al vehículo(Núm de Inventario): "+ auditorr.inv)
        builder.setNegativeButton("Cancelar", DialogInterface.OnClickListener{
                dialog, which ->
            Toast.makeText(this,"Cancelado",Toast.LENGTH_SHORT).show()
        })
        builder.setPositiveButton("Confirmar", DialogInterface.OnClickListener{
                dialog, which ->
            try {
                val jsonEntrada = JSONObject()
                jsonEntrada.put("c_auditorias", auditorr.c_audi)
                jsonEntrada.put("t_auditoria",auditorr.t_audi)
                jsonEntrada.put("id_persona",auditorr.idP)
                jsonEntrada.put("id_carro",auditorr.idCar)
                jsonEntrada.put("id_usuario",auditorr.idUs)
                jsonEntrada.put("fecha",auditorr.fechaa)
                jsonEntrada.put("motor",auditorr.motorr)
                jsonEntrada.put("carroceria",auditorr.carroc)
                jsonEntrada.put("interior",auditorr.inter)
                jsonEntrada.put("aditamentos",auditorr.adit)
                jsonEntrada.put("equipo_tactico",auditorr.equipo)
                jsonEntrada.put("n_conformidades",auditorr.n_confor)
                jsonEntrada.put("conclusion",auditorr.conclu)
                jsonEntrada.put("fechallantas",auditorr.llantas)
                jsonEntrada.put("cinturones",auditorr.cinturones)
                jsonEntrada.put("bolsasAire",auditorr.bolsasA)
                jsonEntrada.put("testigosTablero",auditorr.testigos)
                sendRequest(Address.IP + "Auditoriapp/Login/insertarAuditorias.php",jsonEntrada)
            }catch (e: JSONException){
                e.printStackTrace()
                Toast.makeText(this,"No se pudo subir al servidor, intente más tarde",Toast.LENGTH_SHORT).show()
            }
        })
        builder.show()
    }


    override fun onResume() {
        super.onResume()
        retriveAuditorias()
    }

    private fun retriveAuditorias() {
        val audito = getAuditorias()
        viewAdapter.setTask(audito!!)
    }

    private fun getAuditorias(): MutableList<Auditorias> {
        val audis: MutableList<Auditorias> = ArrayList()
        val admin = DataBase(this)

        val tupla = admin.Consulta("SELECT a.c_auditorias,a.t_auditoria,a.id_persona,a.id_carro,a.id_usuario,a.fecha,a.motor," +
                "a.carroceria,a.interior,a.aditamentos,a.equipo_tactico,a.n_conformidades,a.conclusion,a.fechallantas,a.cinturones,a.bolsasAire,a.testigosTablero,p.num_nomina,c.inventario,u.nomusuario,p.nombre" +
                " FROM auditorias as a inner join persona as p on a.id_persona=p.id_persona inner join carro as c on c.id_carro=a.id_carro inner join usuario as u on u.id_usuario=a.id_usuario where a.id_usuario = '$idU' order by a.t_auditoria")
        while (tupla!!.moveToNext()){
            val c_audi = tupla.getString(0)
            val t_audi = tupla.getString(1)
            val idP = tupla.getString(2)
            val idC = tupla.getString(3)
            val idUS = tupla.getString(4)
            val fecha = tupla.getString(5)
            val moto = tupla.getString(6)
            val carroc =  tupla.getString(7)
            val int = tupla.getString(8)
            val adit = tupla.getString(9)
            val equip = tupla.getString(10)
            val n_confor = tupla.getString(11)
            val concl = tupla.getString(12)
            val llantas = tupla.getString(13)
            val cint = tupla.getString(14)
            val bolsas = tupla.getString(15)
            val testigos = tupla.getString(16)
            val nomi = tupla.getString(17)
            val inven = tupla.getString(18)
            val nomU = tupla.getString(19)
            val nombre = tupla.getString(20)
            audis.add(Auditorias(c_audi,t_audi,idP,idC,idUS,fecha,moto,carroc,int,adit,equip,n_confor,concl,llantas,cint,bolsas,testigos,nomi,inven,nomU,nombre))
        }
        tupla.close()
        admin.close()
        return audis
    }

    private fun eraserAuditorias(){
        val audi = erase()
        viewAdapter.setTask(audi!!)
    }
    fun erase():MutableList<Auditorias>{
        var audii : MutableList<Auditorias> = java.util.ArrayList()
        audii.clear()
        return audii
    }

    private fun finalizarAu(){
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