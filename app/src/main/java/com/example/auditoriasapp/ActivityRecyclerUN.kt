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
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.example.auditoriasapp.Database.DataBase
import com.example.auditoriasapp.Volley.VolleySingleton
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_agregar_usuarios.*
import kotlinx.android.synthetic.main.activity_recycler_u_n.*
import org.json.JSONException
import org.json.JSONObject
import java.util.*

class ActivityRecyclerUN : AppCompatActivity() {
    private lateinit var idU: String

    var idP: Int = 0
    private lateinit var nominasN: MutableList<UsuariosN>
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
        //refrescando el recycler view al hacer swipe hacia abajo el recycler
        swipeRefreshLayoutUsu.setOnRefreshListener {
            retrieveUsuarios()
            swipeRefreshLayoutUsu.isRefreshing = false
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
                val num_nom = usuaaaa[position].nomi
                val nombre = usuaaaa[position].nom
                val tipoN = usuaaaa[position].tipnom
                val idD = usuaaaa[position].idDep
                val idDir = usuaaaa[position].idDir
                val Agp = usuaaaa[position].agrP
                val admin = DataBase(baseContext)
                if (admin.Ejecuta("DELETE FROM personaNuevo WHERE id_persona = '$idP'")){
                    retrieveUsuarios()
                        Snackbar.make(rv_usuarios_nuevos,"Se elimino la nómina provicional: "+usuaaaa[position].nomi,Snackbar.LENGTH_LONG).setAction("Deshacer", View.OnClickListener {
                                val sentencia = "INSERT INTO personaNuevo(num_nomina,nombre,tiponomina,id_direccion,id_departamento,AgregadoP) " +
                                        "values ('$num_nom','$nombre','$tipoN','$idDir','$idD','$Agp')"
                                if (admin.Ejecuta(sentencia)){
                                    Toast.makeText(baseContext,"Se deshizo la acción",Toast.LENGTH_LONG).show()
                                    retrieveUsuarios()
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
        }).attachToRecyclerView(rv_usuarios_nuevos)


        //Funcion para buscar dentro del recycler view por nombre
        val searchView : SearchView = findViewById(R.id.searchViewUsu)
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                return true
            }
            override fun onQueryTextChange(newText: String?): Boolean {
                nominasN = ArrayList()
                var id_p: Int
                var num_nom:String
                var nomb:String
                var tiponom:String
                var id_dir:Int
                var id_dep:Int
                var agrP:Int
                if (newText!!.isNotEmpty()){
                    var search = newText.toLowerCase(Locale.getDefault())
                    var buscar = viewAdapter.getTasks()
                    eraserUsuarios()
                    for (nomina in buscar){
                        if (nomina.nom.toLowerCase(Locale.getDefault()).contains(search)){
                            id_p = nomina.idpersona
                            num_nom = nomina.nomi
                            nomb = nomina.nom
                            tiponom = nomina.tipnom
                            id_dir = nomina.idDir
                            id_dep = nomina.idDep
                            agrP = nomina.agrP
                            nominasN.add(UsuariosN(id_p,num_nom,nomb,tiponom,id_dir,id_dep,agrP))
                        }
                        viewAdapter.setTask(nominasN!!)
                        rv_usuarios_nuevos.adapter!!.notifyDataSetChanged()
                    }
                }else{
                    eraserUsuarios()
                    retrieveUsuarios()
                }
                return true
            }
        })
    }

    //evento onclick en los items del cardview en el recycler
    private fun onItemClickListener(usua: UsuariosN) {
        val builder = AlertDialog.Builder(this@ActivityRecyclerUN)
        builder.setTitle("¿Quieres subir al servidor esta nómina provisional?")
        builder.setCancelable(true)
        builder.setMessage("Con nombre: "+ usua.nom)
        builder.setNegativeButton("Cancelar",DialogInterface.OnClickListener{
            dialog, which ->
            Toast.makeText(this,"Cancelado",Toast.LENGTH_SHORT).show()
        })
        builder.setPositiveButton("Confirmar",DialogInterface.OnClickListener{
                dialog, which ->
            try {
                idP = usua.idpersona
                val jsonEntrada =  JSONObject()
                jsonEntrada.put("num_nomina", usua.nomi)
                jsonEntrada.put("nombre", usua.nom)
                jsonEntrada.put("tiponomina", usua.tipnom)
                jsonEntrada.put("id_direccion",usua.idDir)
                jsonEntrada.put("id_departamento",usua.idDep)
                insertarNom(jsonEntrada)
                retrieveUsuarios()
            }catch (e:JSONException){
                e.printStackTrace()
                Toast.makeText(this,"No se pudo subir al servidor, intente más tarde",Toast.LENGTH_SHORT).show()
            }
        })
        builder.show()
    }
    private fun insertarNom(jsonEntrada2:JSONObject) {
        val jsonEntrada:JSONObject = jsonEntrada2
        val wsURL = Address.IP + "Auditoriapp/Login/insertarNominasN.php"
        val jsonObjectRequest = JsonObjectRequest(
            Request.Method.POST,wsURL,jsonEntrada,
            { response ->
                try {
                    val usuariosJson = response.getJSONArray("persona")
                    if (usuariosJson.length() > 0){
                        val idp = usuariosJson.getJSONObject(0).getString("id_persona")
                        val nomina = usuariosJson.getJSONObject(0).getString("num_nomina")
                        val tipn = usuariosJson.getJSONObject(0).getString("tiponomina")
                        val sentencia:String
                        val sentencia2:String
                        sentencia = "Update personaNuevo set id_persona='$idp' where num_nomina = '$nomina' and tiponomina='$tipn'"
                        sentencia2 = "Update persona set id_persona='$idp' where num_nomina = '$nomina' and tiponomina='$tipn'"
                        val admin = DataBase(this)
                        if (admin.Ejecuta(sentencia) && admin.Ejecuta(sentencia2)) {
                            val sen:String
                            val sen2:String
                            val sen3:String
                            sen = "Update examen set id_persona='$idp' where id_persona = '$idP'"
                            sen2 = "Update auditorias set id_persona='$idp' where id_persona = '$idP'"
                            sen3 = "Update chequeo set id_persona='$idp' where id_persona = '$idP'"
                            if (admin.Ejecuta(sen) && admin.Ejecuta(sen2) && admin.Ejecuta(sen3)){
                                admin.close()
                                Toast.makeText(this, "Se actualizo la info.", Toast.LENGTH_LONG).show()
                            }
                            else{
                                Toast.makeText(this,"No se pudo Actualizar las auditorias, revisiones o auditorias",Toast.LENGTH_LONG).show()
                            }
                        } else {
                            admin.close()
                            Toast.makeText(this, "No se pudo actualizar", Toast.LENGTH_LONG).show()
                        }
                    }
                }catch (e:JSONException){
                    e.printStackTrace()
                    Toast.makeText(this@ActivityRecyclerUN, "Persona no insertada", Toast.LENGTH_SHORT).show()
                }
            },
            { error ->
                Toast.makeText(this, "Error: " + error.message.toString() , Toast.LENGTH_LONG).show()
                Log.d("Auditoriapp",error.message.toString() )
            }
        )
        VolleySingleton.getInstance(this).addToRequestQueue(jsonObjectRequest)
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
    private fun eraserUsuarios(){
        val usuarioss = erase()
        viewAdapter.setTask(usuarioss!!)
    }
    fun erase():MutableList<UsuariosN>{
        var UsuariosNo : MutableList<UsuariosN> = ArrayList()
        UsuariosNo.clear()
        return UsuariosNo
    }

    private fun finalizarUN(){
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