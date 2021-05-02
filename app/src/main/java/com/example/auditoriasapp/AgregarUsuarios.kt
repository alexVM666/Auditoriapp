package com.example.auditoriasapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.example.auditoriasapp.Database.DataBase
import com.example.auditoriasapp.Volley.VolleySingleton
import kotlinx.android.synthetic.main.activity_agregar_usuarios.*
import org.json.JSONException
import org.json.JSONObject

class AgregarUsuarios : AppCompatActivity() {
    var Taccion: String = ""

    var tUsuario: String = ""
    var idUsu: Int = 0
    var id_rol: Int = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_agregar_usuarios)
        //Spinner Tipo de usuario
        val tipoUsu = arrayOf("Usuario","Administrador")
        val arrayAdapter = ArrayAdapter(this,R.layout.spinner_item_new,tipoUsu)
        tipousu_spinner.adapter = arrayAdapter
        tipousu_spinner.onItemSelectedListener = object :
            AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }

            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                Toast.makeText(parent?.context,"Tipo de Usuario: "+parent?.getItemAtPosition(position).toString(), Toast.LENGTH_SHORT).show()
                tUsuario = parent?.getItemAtPosition(position).toString()
                if (tUsuario.equals("Usuario")){
                    id_rol = 2
                }else{
                    id_rol = 1
                }
            }
        }

        val accion = arrayOf("Buscar","Agregar","Editar","Eliminar")
        val arrayAdapter2 = ArrayAdapter(this,R.layout.spinner_item_new,accion)
        accion_spinner.adapter = arrayAdapter2
        accion_spinner.onItemSelectedListener = object :
            AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }

            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                Toast.makeText(parent?.context,"Acción seleccionada: "+parent?.getItemAtPosition(position).toString(), Toast.LENGTH_SHORT).show()
                Taccion = parent?.getItemAtPosition(position).toString()
                if (Taccion.equals("Editar")){
                    usuario.isEnabled = true
                    nombre_usu.isEnabled = true
                    contrasena.isEnabled = true
                    tipousu_spinner.isEnabled = true
                }
                if (Taccion.equals("Buscar")){
                    usuario.setText("")
                    nombre_usu.setText("")
                    contrasena.setText("")
                    idUsu = 0
                }
                if (Taccion.equals("Agregar")){
                    usuario.setText("")
                    nombre_usu.setText("")
                    contrasena.setText("")
                    idUsu = 0
                }
            }
        }
        regresarM.setOnClickListener {
            val i = Intent(this,menuActivity::class.java)
            startActivity(i)
            finish()
        }

        borrar_txt.setOnClickListener {
            usuario.setText("")
            nombre_usu.setText("")
            contrasena.setText("")
            usuario.isEnabled = true
            nombre_usu.isEnabled = true
            contrasena.isEnabled = true
            tipousu_spinner.isEnabled = true
            idUsu = 0
        }

        b_accion.setOnClickListener {
            if (Taccion.equals("Buscar")){
                if (usuario.text.toString().isNotEmpty()){
                    val jsonEntrada = JSONObject()
                    jsonEntrada.put("usuario",usuario.text.toString())
                    jsonEntrada.put("id_rol",id_rol)
                    Buscar(jsonEntrada)
                }else{
                    Toast.makeText(this, "Debes de llenar los datos", Toast.LENGTH_SHORT).show()
                    usuario.requestFocus()
                }
            }
            if (Taccion.equals("Editar")){
                if (usuario.text.toString().isNotEmpty() && nombre_usu.text.toString().isNotEmpty() && contrasena.text.toString().isNotEmpty()){
                    Editar()
                    usuario.setText("")
                    nombre_usu.setText("")
                    contrasena.setText("")
                    llenarUsuarios()
                }else{
                    Toast.makeText(this, "Debes de llenar los datos", Toast.LENGTH_SHORT).show()
                    usuario.requestFocus()
                }
            }
            if (Taccion.equals("Agregar")){
                if (usuario.text.toString().isNotEmpty() && nombre_usu.text.toString().isNotEmpty() && contrasena.text.toString().isNotEmpty()){
                    Agregar()
                    usuario.setText("")
                    nombre_usu.setText("")
                    contrasena.setText("")
                    llenarUsuarios()
                }else{
                    Toast.makeText(this, "Debes de llenar los datos", Toast.LENGTH_SHORT).show()
                    usuario.requestFocus()
                }
            }
            if (Taccion.equals("Eliminar")){
                if (usuario.text.toString().isNotEmpty()){
                    Eliminar()
                    usuario.setText("")
                    nombre_usu.setText("")
                    contrasena.setText("")
                    llenarUsuarios()
                }else{
                    Toast.makeText(this, "Debes de llenar los datos", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
    private fun Eliminar() {
        val jsonEntrada = JSONObject()
        jsonEntrada.put("id_usuario", idUsu)
        jsonEntrada.put("id_rol",id_rol)
        sendRequest(Address.IP + "Auditoriapp/Login/borrarUsuario.php",jsonEntrada)
    }

    private fun Agregar() {
        val jsonEntrada = JSONObject()
        jsonEntrada.put("usuario", usuario.text.toString())
        jsonEntrada.put("contrasena", contrasena.text.toString())
        jsonEntrada.put("nomusuario",nombre_usu.text.toString())
        jsonEntrada.put("id_rol",id_rol)
        sendRequest(Address.IP + "Auditoriapp/Login/insertarUsuario.php",jsonEntrada)
    }

    private fun Editar() {
        val jsonEntrada = JSONObject()
        jsonEntrada.put("id_usuario", idUsu)
        jsonEntrada.put("usuario", usuario.text.toString())
        jsonEntrada.put("contrasena", contrasena.text.toString())
        jsonEntrada.put("nomusuario",nombre_usu.text.toString())
        jsonEntrada.put("id_rol",id_rol)
        sendRequest(Address.IP + "Auditoriapp/Login/editarUsuario.php",jsonEntrada)
    }

    private fun Buscar(jsonEntrada:JSONObject) {
        val wsURL = Address.IP + "Auditoriapp/Login/getUsuario.php"
        val jsonObjectRequest = JsonObjectRequest(
            Request.Method.POST,wsURL,jsonEntrada,
            { response ->
                try {
                    val usuariosJson = response.getJSONArray("usuario")
                    if (usuariosJson.length() > 0){
                        val idu = usuariosJson.getJSONObject(0).getString("id_usuario")
                        val usu = usuariosJson.getJSONObject(0).getString("usuario")
                        val contra = usuariosJson.getJSONObject(0).getString("contrasena")
                        val nomb = usuariosJson.getJSONObject(0).getString("nomusuario")
                        val id_r = usuariosJson.getJSONObject(0).getString("id_rol")
                        usuario.setText(usu)
                        contrasena.setText(contra)
                        nombre_usu.setText(nomb)
                        id_rol = id_r.toInt()
                        idUsu = idu.toInt()
                        usuario.isEnabled = false
                        contrasena.isEnabled = false
                        nombre_usu.isEnabled = false
                        tipousu_spinner.isEnabled = false
                    }
                }catch (e:JSONException){
                    e.printStackTrace()
                    Toast.makeText(this@AgregarUsuarios, "Usuario no encontrado", Toast.LENGTH_SHORT).show()
                }
            },
            { error ->
                Toast.makeText(this, "Error: " + error.message.toString() , Toast.LENGTH_LONG).show()
                Log.d("Auditoriapp",error.message.toString() )
            }
        )
        VolleySingleton.getInstance(this).addToRequestQueue(jsonObjectRequest)
    }
    fun llenarUsuarios(){
        val wsURL = Address.IP+"Auditoriapp/Login/llenarUsuarios.php"
        val admin = DataBase(this)
        admin.Ejecuta("DELETE FROM usuariosDisponibles")
        val jsonObjectRequest = JsonObjectRequest(
            Request.Method.POST,wsURL,null,
            { response ->
                try {
                    val succ = response["success"]
                    val msg = response["message"]
                    val tipo_vehiculosJson = response.getJSONArray("usuarios")
                    for (i in 0 until tipo_vehiculosJson.length()){
                        val id = tipo_vehiculosJson.getJSONObject(i).getString("id_usuario")
                        val usu = tipo_vehiculosJson.getJSONObject(i).getString("usuario")
                        val contra =  tipo_vehiculosJson.getJSONObject(i).getString("contrasena")
                        val nomusu =  tipo_vehiculosJson.getJSONObject(i).getString("nomusuario")
                        val idRol =  tipo_vehiculosJson.getJSONObject(i).getString("id_rol")
                        val sentencia = "insert into usuariosDisponibles(id_usuario,usuario,contrasena,nomusuario,id_rol) "+
                                "values ('${id}','${usu}','${contra}','${nomusu}','${idRol}')"
                        val res = admin.Ejecuta(sentencia)
                        //Toast.makeText(this,"res= ${res}",Toast.LENGTH_SHORT).show();
                    }
                } catch (e: JSONException) {
                    e.printStackTrace()
                    Toast.makeText(this, "Error al llenar los usuarios en la base de datos.", Toast.LENGTH_SHORT).show();
                }
            },
            //Response.ErrorListener
            { error ->
                Toast.makeText(this,"Error llenarUsuarios: "+ error.message.toString(), Toast.LENGTH_LONG).show();
                Log.d("Alejandro",error.message.toString())
            }
        )
        VolleySingleton.getInstance(this).addToRequestQueue(jsonObjectRequest)
    }


    fun sendRequest( wsURL: String, jsonEntrada: JSONObject){
        val jsonObjectRequest = JsonObjectRequest(
            Request.Method.POST, wsURL, jsonEntrada,
            { response ->
                try {
                    val succ = response["success"]
                    val msg = response["message"]
                    Toast.makeText(this, "Success:${succ}  Message:${msg}", Toast.LENGTH_SHORT).show()
                } catch (e: JSONException) {
                    e.printStackTrace()
                    Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show()
                }
            },
            { error ->
                Toast.makeText(this, "${error.message}", Toast.LENGTH_SHORT).show()
                Log.d("ERROR", "${error.message}")
                Toast.makeText(this, "Error de capa 8 checa URL", Toast.LENGTH_SHORT).show()
            }
        )
        VolleySingleton.getInstance(this).addToRequestQueue(jsonObjectRequest)
    }
}