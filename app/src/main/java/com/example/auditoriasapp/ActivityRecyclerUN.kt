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
import kotlinx.android.synthetic.main.activity_recycler_u_n.*
import org.json.JSONObject
import java.util.ArrayList

class ActivityRecyclerUN : AppCompatActivity() {
    private lateinit var idU: String

    var idP: Int = 0
    private lateinit var viewAdapter: UsuariosNAdapter
    private lateinit var viewManager: RecyclerView.LayoutManager
    val usuariosList: List<UsuariosN> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recycler_u_n)
        val usuariosNU = intent
        if (usuariosNU != null && usuariosNU.hasExtra("id_usuario")) {
            idU = usuariosNU.getStringExtra("id_usuario")
        }

        finalizarUsNuevos.setOnClickListener{
            finalizarUN()
        }

        viewManager = LinearLayoutManager(this)
        viewAdapter = UsuariosNAdapter(usuariosList, this, { usua: UsuariosN -> onItemClickListener(usua) })

        rv_usuarios_nuevos.apply{
            setHasFixedSize(true)
            layoutManager = viewManager
            adapter = viewAdapter
            addItemDecoration(DividerItemDecoration(this@ActivityRecyclerUN,DividerItemDecoration.VERTICAL))
        }

        // Metodo para implementar la eliminación de una nomina, cuando el usuario da un onSwiped en el recyclerview
        ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {
            override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean {
                return false
            }
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, swipeDir: Int) {
                val position = viewHolder.adapterPosition
                val usuaaaa= viewAdapter.getTasks()
                idP = usuaaaa[position].idpersona
                val admin = DataBase(baseContext)
                if (admin.Ejecuta("DELETE FROM personaNuevo WHERE id_persona = '$idP'")){
                    retrieveUsuarios()
                }
            }
        }).attachToRecyclerView(rv_usuarios_nuevos)
    }

    //evento onclick en los items del cardview en el recycler
    private fun onItemClickListener(usua: UsuariosN) {
        val jsonEntrada =  JSONObject()
        //jsonEntrada.put("id_persona", usua.idpersona)
        jsonEntrada.put("num_nomina", usua.nomi)
        jsonEntrada.put("nombre", usua.nom)
        jsonEntrada.put("tiponomina", usua.tipnom)
        jsonEntrada.put("id_direccion",usua.idDir)
        jsonEntrada.put("id_departamento",usua.idDep)
        sendRequest(Address.IP + "Auditoriapp/Login/insertarNominasN.php",jsonEntrada)
    }

    override fun onResume() {
        super.onResume()
        retrieveUsuarios()
    }

    private fun retrieveUsuarios() {
        val usuarioss = getUsuarios()
        viewAdapter.setTask(usuarioss!!)
    }

    private fun getUsuarios(): MutableList<UsuariosN> {
        val usuarios: MutableList<UsuariosN> = ArrayList()
        val admin = DataBase(this)

        val tupla = admin.Consulta("Select id_persona,num_nomina,nombre,tiponomina,id_direccion,id_departamento,AgregadoP from personaNuevo where AgregadoP='$idU' Order by nombre")
        while (tupla!!.moveToNext()){
            val id_p =  tupla.getInt(0)
            val num_nom =  tupla.getString(1)
            val nomb =  tupla.getString(2)
            val tiponom = tupla.getString(3)
            val id_dir = tupla.getInt(4)
            val id_dep = tupla.getInt(5)
            val agrP = tupla.getInt(6)

            usuarios.add(UsuariosN(id_p,num_nom,nomb,tiponom,id_dir,id_dep,agrP))
        }
        tupla.close()
        admin.close()
        return usuarios
    }

    private fun finalizarUN(){
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