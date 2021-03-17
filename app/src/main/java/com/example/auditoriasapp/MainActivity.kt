package com.example.auditoriasapp

import android.content.Intent
import android.net.ConnectivityManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.example.auditoriasapp.Database.DataBase
import com.example.auditoriasapp.Volley.VolleySingleton
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.launch
import org.json.JSONException
import org.json.JSONObject

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        login.setOnClickListener {
            lifecycleScope.launch {
                login()
            }
        }
    }
    fun login(){
        if(tengoInternet()){
            Toast.makeText(this, "Conexion a internet", Toast.LENGTH_SHORT).show()
            if(!txtCorreo.text.isEmpty() && !txtContra.text.isEmpty()){
                val jsonEntrada = JSONObject()
                jsonEntrada.put("usuario",txtCorreo.text.toString())
                jsonEntrada.put("contrasena",txtContra.text.toString())
                buscarUsuario(jsonEntrada)
            }else{
                Toast.makeText(this, "Debes de llenar los datos", Toast.LENGTH_SHORT).show()
                txtCorreo.requestFocus()
            }
        }else{
            Toast.makeText(this, "No tienes conexion, reintenta mas tarde", Toast.LENGTH_SHORT).show()
        }
    }
    //funcion para detectar la conexion de internet o datos
    private fun tengoInternet(): Boolean {
        var tengowifi = false
        var tengodatos = false
        val connectivityManager = getSystemService(CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfos = connectivityManager.allNetworkInfo
        for (info in networkInfos){
            if (info.typeName.equals("WIFI", ignoreCase = true)) if (info.isConnected) tengowifi =  true
            if (info.typeName.equals("Datos",ignoreCase = true)) if (info.isConnected) tengodatos = true
        }
        return tengowifi || tengodatos
    }

    fun buscarUsuario(jsonEnt: JSONObject){
        val wsURL = Address.IP + "Auditoriapp/Login/login.php"
        val jsonObjectRequest =  JsonObjectRequest(
            Request.Method.POST, wsURL, jsonEnt,
            //Response.Listener
            { response ->
                try {
                    val usuarioJson = response.getJSONArray("usuario")
                    if (usuarioJson.length() > 0) {
                        val nom = usuarioJson.getJSONObject(0).getString("id_usuario")
                        val corr = usuarioJson.getJSONObject(0).getString("usuario")
                        val psw = usuarioJson.getJSONObject(0).getString("contrasena")
                        val tipousr = usuarioJson.getJSONObject(0).getString("id_rol")
                        val nomusuario = usuarioJson.getJSONObject(0).getString("nomusuario")
                        val sentencia = "insert into usuario (id_usuario,usuario,contrasena,id_rol,nomusuario) values('$nom','$corr','$psw','$tipousr','$nomusuario')"
                        val admin = DataBase(this)
                        if (admin.Ejecuta(sentencia)) {
                            val actividad = Intent(this, menuActivity::class.java)
                            actividad.putExtra("id_usuario", nom.toString())
                            actividad.putExtra("usuario", corr.toString())
                            actividad.putExtra("contrasena", psw.toString())
                            actividad.putExtra("id_rol", tipousr.toString())
                            actividad.putExtra("nomusuario",nomusuario.toString())
                            admin.close()
                            llenarempleados()
                            llenardependencias()
                            llenarareas()
                            llenarT_vehiculos()
                            llenarAutomoviles()
                            llenarUsuarios()
                            startActivity(actividad)
                            Toast.makeText(this, "Hola Usuario "+nomusuario, Toast.LENGTH_LONG).show()
                            finish()
                        }else{
                            Toast.makeText(this@MainActivity,"No se pudo guardar el usuario", Toast.LENGTH_LONG).show()
                        }
                    }
                } catch (e: JSONException) {
                    e.printStackTrace()
                    Toast.makeText(this@MainActivity, "Contraseña incorrecta o Usuario incorrecto.", Toast.LENGTH_SHORT).show()
                }
            },
            //Response.ErrorListener
            { error ->
                Toast.makeText(
                    this, "Error en Web service: " + error.message.toString(), Toast.LENGTH_LONG
                ).show()
                Log.d("Alejandro", error.message.toString())
            }
        )
    VolleySingleton.getInstance(this).addToRequestQueue(jsonObjectRequest)
   }
    fun llenarempleados(){
        val wsURL = Address.IP+"Auditoriapp/Login/llenarEmpleado.php"
        val admin = DataBase(this)
        admin.Ejecuta("DELETE FROM persona")
        val jsonObjectRequest = JsonObjectRequest(
            Request.Method.POST, wsURL, null,
            //Response.Listener
            { response ->
                try {
                    val succ = response["success"]
                    val msg = response["message"]
                    val empleadoJson = response.getJSONArray("empleados")
                    for (i in 0 until empleadoJson.length()) {
                        val id = empleadoJson.getJSONObject(i).getString("id_persona")
                        val nomi = empleadoJson.getJSONObject(i).getString("num_nomina")
                        val nomb = empleadoJson.getJSONObject(i).getString("nombre")
                        val tip = empleadoJson.getJSONObject(i).getString("tiponomina")
                        val idDir = empleadoJson.getJSONObject(i).getString("id_direccion")
                        val idDepto = empleadoJson.getJSONObject(i).getString("id_departamento")
                        val sentencia =
                            "insert into persona(id_persona,num_nomina,nombre,tiponomina,id_direccion,id_departamento)" +
                                    "values ('${id}','${nomi}','${nomb}','${tip}','${idDir}','${idDepto}')"
                        val res = admin.Ejecuta(sentencia)
                        //Toast.makeText(this@MainActivity, "res= ${res}", Toast.LENGTH_SHORT).show();
                    }
                } catch (e: JSONException) {
                    e.printStackTrace()
                    Toast.makeText(this, "Error al llenar los empleados en la base de datos", Toast.LENGTH_SHORT).show();
                }
            },
            //Response.ErrorListener
            { error ->
                Toast.makeText(
                    this@MainActivity,
                    "Error llenarEmpleado: " + error.message.toString(),
                    Toast.LENGTH_LONG
                ).show();
                Log.d("Alejandro", error.message.toString())
            }
        )
        VolleySingleton.getInstance(this).addToRequestQueue(jsonObjectRequest)
    }

    fun llenardependencias(){
        val wsURL = Address.IP+"Auditoriapp/Login/llenarDependencias.php"
        val admin = DataBase(this)
        admin.Ejecuta("DELETE FROM direccion")
        val jsonObjectRequest = JsonObjectRequest(
            Request.Method.POST,wsURL,null,
            //Response.Listener
            { response ->
                try {
                    val succ = response["success"]
                    val msg = response["message"]
                    val dependenciasJson = response.getJSONArray("dependencia")
                    for (i in 0 until dependenciasJson.length()){
                        val cdep = dependenciasJson.getJSONObject(i).getString("id_direccion")
                        val nomd = dependenciasJson.getJSONObject(i).getString("direccion")
                        val sentencia = "insert into direccion(id_direccion,direccion) " +
                                "values ('${cdep}','${nomd}')"
                        val res = admin.Ejecuta(sentencia)
                        //Toast.makeText(this@MainActivity,"res= ${res}",Toast.LENGTH_SHORT).show();
                    }
                } catch (e: JSONException) {
                    e.printStackTrace()
                    Toast.makeText(this, "Error al llenar las direcciones en la base de datos", Toast.LENGTH_SHORT).show();
                }
            },
            //Response.ErrorListener
            { error ->
                Toast.makeText(this@MainActivity,"Error LlenarDirecciones: "+ error.message.toString(), Toast.LENGTH_LONG).show();
                Log.d("Alejandro",error.message.toString())
            }
        )
        VolleySingleton.getInstance(this).addToRequestQueue(jsonObjectRequest)
    }

    fun llenarareas(){
        val wsURL = Address.IP+"Auditoriapp/Login/llenarAreas.php"
        val admin = DataBase(this)
        admin.Ejecuta("DELETE FROM departamento")
        val jsonObjectRequest = JsonObjectRequest(
            Request.Method.POST,wsURL,null,
            //Response.Listener
            { response ->
                try {
                    val succ = response["success"]
                    val msg = response["message"]
                    val areasJson = response.getJSONArray("areas")
                    for (i in 0 until areasJson.length()){
                        val idD = areasJson.getJSONObject(i).getString("id_departamento")
                        val nomD = areasJson.getJSONObject(i).getString("departamento")
                        val direcc = areasJson.getJSONObject(i).getString("id_direccion")
                        val sentencia = "insert into departamento(id_departamento,departamento,id_direccion) " +
                                "values ('${idD}','${nomD}','${direcc}')"
                        val res = admin.Ejecuta(sentencia)
                        //Toast.makeText(this@MainActivity,"res= ${res}",Toast.LENGTH_SHORT).show();
                    }
                } catch (e: JSONException) {
                    e.printStackTrace()
                    Toast.makeText(this, "Error al llenar los departamentos en la base de datos", Toast.LENGTH_SHORT).show();
                }
            },
            //Response.ErrorListener
            { error ->
                Toast.makeText(this@MainActivity,"Error LlenarDepto: "+ error.message.toString(), Toast.LENGTH_LONG).show();
                Log.d("Alejandro",error.message.toString())
            }
        )
        VolleySingleton.getInstance(this).addToRequestQueue(jsonObjectRequest)
    }
    fun llenarT_vehiculos(){
        val wsURL = Address.IP+"Auditoriapp/Login/llenarTautos.php"
        val admin = DataBase(this)
        admin.Ejecuta("DELETE FROM tipovehiculo")
        val jsonObjectRequest = JsonObjectRequest(
            Request.Method.POST,wsURL,null,
            //Response.Listener
            { response ->
                try {
                    val succ = response["success"]
                    val msg = response["message"]
                    val tipo_vehiculosJson = response.getJSONArray("tipo_vehiculo")
                    for (i in 0 until tipo_vehiculosJson.length()){
                        val id = tipo_vehiculosJson.getJSONObject(i).getString("id_vehiculo")
                        val tip = tipo_vehiculosJson.getJSONObject(i).getString("tipovehiculo")
                        val sentencia = "insert into tipovehiculo(id_vehiculo,tipovehiculo)" +
                                "values ('${id}','${tip}')"
                        val res = admin.Ejecuta(sentencia)
                        //Toast.makeText(this@MainActivity,"res= ${res}",Toast.LENGTH_SHORT).show();
                    }
                } catch (e: JSONException) {
                    e.printStackTrace()
                    Toast.makeText(this, "Error al llenar los tipos de vehiculos en la base de datos", Toast.LENGTH_SHORT).show();
                }
            },
            //Response.ErrorListener
            { error ->
                Toast.makeText(this@MainActivity,"Error llenarTipo_vehiculo: "+ error.message.toString(), Toast.LENGTH_LONG).show();
                Log.d("Alejandro",error.message.toString())
            }
        )
        VolleySingleton.getInstance(this).addToRequestQueue(jsonObjectRequest)
    }

    fun llenarAutomoviles(){
        val wsURL = Address.IP+"Auditoriapp/Login/llenarAutos.php"
        val admin = DataBase(this)
        admin.Ejecuta("DELETE FROM carro")
        val jsonObjectRequest = JsonObjectRequest(
            Request.Method.POST,wsURL,null,
            { response ->
                try {
                    val succ = response["success"]
                    val msg = response["message"]
                    val tipo_vehiculosJson = response.getJSONArray("vehiculos")
                    for (i in 0 until tipo_vehiculosJson.length()){
                        val id = tipo_vehiculosJson.getJSONObject(i).getString("id_carro")
                        val idD = tipo_vehiculosJson.getJSONObject(i).getString("id_direccion")
                        val idDep =  tipo_vehiculosJson.getJSONObject(i).getString("id_departamento")
                        val idVe =  tipo_vehiculosJson.getJSONObject(i).getString("id_vehiculo")
                        val idPer =  tipo_vehiculosJson.getJSONObject(i).getString("id_persona")
                        val marca =  tipo_vehiculosJson.getJSONObject(i).getString("marca")
                        val sub =  tipo_vehiculosJson.getJSONObject(i).getString("submarca")
                        val modelo =  tipo_vehiculosJson.getJSONObject(i).getString("modelo")
                        val serie =  tipo_vehiculosJson.getJSONObject(i).getString("serie")
                        val motor =  tipo_vehiculosJson.getJSONObject(i).getString("motor")
                        val placas =  tipo_vehiculosJson.getJSONObject(i).getString("placas")
                        val inven =  tipo_vehiculosJson.getJSONObject(i).getString("inventario")
                        val subtipo = tipo_vehiculosJson.getJSONObject(i).getString("subtipo")
                        val sentencia = "insert into carro(id_carro,id_direccion,id_departamento,id_vehiculo,id_persona," +
                                "marca,submarca,modelo,serie,motor,placas,inventario,subtipo)" +
                                "values ('${id}','${idD}','${idDep}','${idVe}','${idPer}','${marca}','${sub}','${modelo}','${serie}','${motor}','${placas}','${inven}','${subtipo}')"
                        val res = admin.Ejecuta(sentencia)
                        //Toast.makeText(this@MainActivity,"res= ${res}",Toast.LENGTH_SHORT).show();
                    }
                } catch (e: JSONException) {
                    e.printStackTrace()
                    Toast.makeText(this, "Error al llenar los carros en la base de datos", Toast.LENGTH_SHORT).show();
                }
            },
            //Response.ErrorListener
            { error ->
                Toast.makeText(this@MainActivity,"Error llenarCarro: "+ error.message.toString(), Toast.LENGTH_LONG).show();
                Log.d("Alejandro",error.message.toString())
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
}
