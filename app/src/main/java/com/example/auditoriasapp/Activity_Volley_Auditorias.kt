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
import kotlinx.android.synthetic.main.activity__volley__auditorias.*
import org.json.JSONObject

class Activity_Volley_Auditorias : AppCompatActivity() {
    var claveAudit: String = ""

    private lateinit var viewAdapter: AuditoriasAdapter
    private lateinit var viewManager: RecyclerView.LayoutManager
    val auditoriasList: List<Auditorias> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity__volley__auditorias)
        finalizarAuditoriasVolley.setOnClickListener {
            finalizarAu()
        }
        viewManager = LinearLayoutManager(this)
        viewAdapter = AuditoriasAdapter(auditoriasList, this,{ auditorr: Auditorias -> onItemClickListener(auditorr)  })

        rv_auditorias_volley.apply {
            setHasFixedSize(true)
            layoutManager = viewManager
            adapter = viewAdapter
            addItemDecoration(
                DividerItemDecoration(this@Activity_Volley_Auditorias,
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
                val auditor = viewAdapter.getTasks()
                claveAudit = auditor[position].c_audi
                val admin = DataBase(baseContext)
                if (admin.Ejecuta("DELETE FROM auditoriasVolley WHERE c_auditorias = '$claveAudit'")){
                    val jsonEntrada = JSONObject()
                    jsonEntrada.put("c_auditorias", claveAudit)
                    sendRequest(Address.IP + "Auditoriapp/Login/borrarAuditoria.php",jsonEntrada)
                    retriveAuditorias()
                }
            }
        }).attachToRecyclerView(rv_auditorias_volley)
    }
    private fun onItemClickListener(auditorr: Auditorias) {
        val auditoResul = Intent(this,ActivityResultVolleyAuditorias::class.java)
        auditoResul.putExtra("c_auditorias",auditorr.c_audi)
        auditoResul.putExtra("t_auditorias",auditorr.t_audi)
        auditoResul.putExtra("nomina",auditorr.nomi)
        auditoResul.putExtra("fecha",auditorr.fechaa)
        auditoResul.putExtra("motor",auditorr.motorr)
        auditoResul.putExtra("no_economico",auditorr.inv)
        auditoResul.putExtra("carroceria",auditorr.carroc)
        auditoResul.putExtra("interior",auditorr.inter)
        auditoResul.putExtra("aditamentos",auditorr.adit)
        auditoResul.putExtra("equipo_tactico",auditorr.equipo)
        auditoResul.putExtra("no_conformidades",auditorr.n_confor)
        auditoResul.putExtra("conclusion",auditorr.conclu)
        auditoResul.putExtra("correo",auditorr.usu)
        auditoResul.putExtra("fechallantas",auditorr.llantas)
        auditoResul.putExtra("cinturones",auditorr.cinturones)
        auditoResul.putExtra("bolsasAire",auditorr.bolsasA)
        auditoResul.putExtra("testigosTablero",auditorr.testigos)
        auditoResul.putExtra("nombre",auditorr.nomb)
        startActivity(auditoResul)
        finish()
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
                "a.carroceria,a.interior,a.aditamentos,a.equipo_tactico,a.n_conformidades,a.conclusion,a.fechallantas,p.num_nomina,c.inventario," +
                "u.nomusuario,a.cinturones,a.bolsasAire,a.testigosTablero,p.nombre " +
                "FROM auditoriasVolley as a inner join persona as p on a.id_persona=p.id_persona inner join carro as c on c.id_carro=a.id_carro" +
                " inner join usuariosDisponibles as u on a.id_usuario=u.id_usuario order by a.t_auditoria")
        while (tupla!!.moveToNext()){
            val c_audi = tupla.getString(0)
            val t_audi = tupla.getString(1)
            val idP = tupla.getString(2)
            val idC = tupla.getString(3)
            val idU = tupla.getString(4)
            val fecha = tupla.getString(5)
            val moto = tupla.getString(6)
            val carroc =  tupla.getString(7)
            val int = tupla.getString(8)
            val adit = tupla.getString(9)
            val equip = tupla.getString(10)
            val n_confor = tupla.getString(11)
            val concl = tupla.getString(12)
            val llantas = tupla.getString(13)
            val num_nom = tupla.getString(14)
            val carro = tupla.getString(15)
            val usuario = tupla.getString(16)
            val cinturon = tupla.getString(17)
            val bolsas = tupla.getString(18)
            val testigos = tupla.getString(19)
            val nombre = tupla.getString(20)
            audis.add(Auditorias(c_audi,t_audi,idP,idC,idU,fecha,moto,carroc,int,adit,equip,n_confor,concl,llantas,cinturon,bolsas,testigos,num_nom,carro,usuario,nombre))
        }
        tupla.close()
        admin.close()
        return audis
    }

    private fun finalizarAu(){
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