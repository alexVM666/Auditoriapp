package com.example.auditoriasapp
import android.content.DialogInterface
import android.content.Intent
import android.net.ConnectivityManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.lifecycleScope
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.example.auditoriasapp.Database.DataBase
import com.example.auditoriasapp.Volley.VolleySingleton
import kotlinx.android.synthetic.main.activity_menu.*
import kotlinx.coroutines.launch
import org.json.JSONException
import org.json.JSONObject

class menuActivity : AppCompatActivity() {
    private lateinit var idU: String
    private lateinit var contraU: String
    private lateinit var Usuario: String
    private lateinit var Rol: String
    private lateinit var nombreUsuario:String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu)

        val actividad = intent

        if(actividad != null && actividad.hasExtra("id_usuario")&& actividad.hasExtra("contrasena"))
        {
            idU = actividad.getStringExtra("id_usuario")
            contraU = actividad.getStringExtra("contrasena")
            Usuario = actividad.getStringExtra("usuario")
            Rol = actividad.getStringExtra("id_rol")
            nombreUsuario = actividad.getStringExtra("nomusuario")

            txtNomUsr.text = "$nombreUsuario"
            if (Rol.equals("2")){
                results.isEnabled = false
                agregarusuarios.isEnabled = false
            }else{
                aud.isEnabled = true
                exa.isEnabled = true
                results.isEnabled = true
            }
        }else{
            val admin = DataBase(this)
            val result = admin.Consulta("Select id_usuario,usuario,contrasena,id_rol,nomusuario from usuario")
            if (result!!.moveToFirst()){
                Usuario = result.getString(1)
                idU = result.getString(0)
                contraU = result.getString(2)
                Rol = result.getString(3)
                nombreUsuario = result.getString(4)
                result.close()
                admin.close()
                txtNomUsr.text = "$nombreUsuario"
                if (Rol.equals("2")){
                    results.isEnabled = false
                    agregarusuarios.isEnabled = false
                }else{
                    aud.isEnabled = true
                    exa.isEnabled = true
                    results.isEnabled = true
                }
            }else{
                val actividadReg = Intent(this,MainActivity::class.java)
                startActivity(actividadReg)
                finish()
            }
            actualizar_bd.setOnClickListener {
                val builder = AlertDialog.Builder(this@menuActivity)
                builder.setTitle("¿Deseas actualizar la base de datos de la app con la BD del servidor?")
                builder.setCancelable(true)
                builder.setMessage("Actualizaras los datos de los Automóviles, nóminas y usuarios con la información más reciente.")
                builder.setNegativeButton("Cancelar", DialogInterface.OnClickListener{
                        dialog, which ->
                    Toast.makeText(this,"Cancelado",Toast.LENGTH_SHORT).show()
                })
                builder.setPositiveButton("Confirmar", DialogInterface.OnClickListener{
                        dialog, which ->
                    actDatos()
                })
                builder.show()
            }

            agregarusuarios.setOnClickListener {
                if(tengoInternet()){
                    Toast.makeText(this, "Conexión a internet.", Toast.LENGTH_SHORT).show()
                    val i = Intent(this,AgregarUsuarios::class.java)
                    startActivity(i)
                    finish()
                }else{
                    Toast.makeText(this, "No tienes conexión a internet", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        super.onCreateOptionsMenu(menu)
        menuInflater.inflate(R.menu.overflowmenu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item?.itemId){
            R.id.idSalir -> {
                val admin = DataBase(this)
                val sentencia = "Delete From usuario where id_usuario = '$idU'"
                val sentencia2 = "Delete from persona"
                val sentencia3 =  "Delete from direccion"
                val sentencia4 = "Delete from departamento"
                val sentencia5 =  "Delete from tipovehiculo"
                val sentencia6 =  "Delete from carro"
                val sentencia7 = "Delete from usuariosDisponibles"
                if (admin.Ejecuta(sentencia) && admin.Ejecuta(sentencia2)&& admin.Ejecuta(sentencia3)&& admin.Ejecuta(sentencia4)
                    &&admin.Ejecuta(sentencia5)&&admin.Ejecuta(sentencia6)&&admin.Ejecuta(sentencia7)) {
                    val actividadCloseSession = Intent(this, MainActivity::class.java)
                    startActivity(actividadCloseSession)
                    finish()
                } else {
                    Toast.makeText(this, "No se pudo Cerrar Sesión", Toast.LENGTH_SHORT).show();
                }
                Toast.makeText(this, "¡Adios, vuelve pronto!", Toast.LENGTH_SHORT).show()
            }

        }
        return super.onOptionsItemSelected(item)
    }
    fun auditorias(v: View){
            val audi = Intent(this,MenuAuditorias::class.java)
            audi.putExtra("usuario", Usuario)
            audi.putExtra("id_usuario",idU)
            audi.putExtra("nomusuario",nombreUsuario)
            audi.putExtra("id_rol",Rol)
            startActivity(audi)
            finish()
    }
    fun examenes(v: View){
            val examenesMenu = Intent(this,menuExamenes::class.java)
            examenesMenu.putExtra("usuario", Usuario)
            examenesMenu.putExtra("id_usuario",idU)
            examenesMenu.putExtra("id_rol",Rol)
            examenesMenu.putExtra("nomusuario",nombreUsuario)
            startActivity(examenesMenu)
            finish()
    }
    fun resultados(v: View){
        if (tengoInternet()){
            if (Rol.equals("1")){
                val resul = Intent(this,menu_ResultadosVolley::class.java)
                resul.putExtra("usuario", Usuario)
                resul.putExtra("id_usuario",idU)
                resul.putExtra("nomusuario",nombreUsuario)
                lifecycleScope.launch {
                    getAllCuestionarios()
                    getAllAuditorias()
                    getAllRevisiones()
                }
                startActivity(resul)
                finish()
            } else{
                Toast.makeText(this,"No tienes acceso.",Toast.LENGTH_SHORT).show()
            }
        }else{
            Toast.makeText(this, "No tienes internet", Toast.LENGTH_SHORT).show();
        }
    }
    fun getAllAuditorias(){
        val wsURL = Address.IP + "Auditoriapp/Login/llenarAuditorias.php"
        val admin = DataBase(this)
        admin.Ejecuta("DELETE FROM auditoriasVolley")
        val jsonObjectRequest = JsonObjectRequest(
            Request.Method.POST,wsURL,null,
            { response ->
                try{
                    val succ = response["success"]
                    val msg = response["message"]
                    val auditoriasJson = response.getJSONArray("auditorias")
                    for (i in 0 until auditoriasJson.length()){
                        val caud = auditoriasJson.getJSONObject(i).getString("c_auditorias")
                        val t_au = auditoriasJson.getJSONObject(i).getString("t_auditoria")
                        val nomi = auditoriasJson.getJSONObject(i).getString("id_persona")
                        val nume = auditoriasJson.getJSONObject(i).getString("id_carro")
                        val correo = auditoriasJson.getJSONObject(i).getString("id_usuario")
                        val fecha = auditoriasJson.getJSONObject(i).getString("fecha")
                        val motor = auditoriasJson.getJSONObject(i).getString("motor")
                        val carroc = auditoriasJson.getJSONObject(i).getString("carroceria")
                        val interior = auditoriasJson.getJSONObject(i).getString("interior")
                        val adit = auditoriasJson.getJSONObject(i).getString("aditamentos")
                        val equip = auditoriasJson.getJSONObject(i).getString("equipo_tactico")
                        val n_confor = auditoriasJson.getJSONObject(i).getString("n_conformidades")
                        val conclu = auditoriasJson.getJSONObject(i).getString("conclusion")
                        val fechallantas = auditoriasJson.getJSONObject(i).getString("fechallantas")
                        val cinturon = auditoriasJson.getJSONObject(i).getString("cinturones")
                        val bolsas = auditoriasJson.getJSONObject(i).getString("bolsasAire")
                        val testigos = auditoriasJson.getJSONObject(i).getString("testigosTablero")
                        val sentencia = "Insert into auditoriasVolley (c_auditorias,t_auditoria,id_persona,id_carro,id_usuario,fecha,motor," +
                                "carroceria,interior,aditamentos,equipo_tactico,n_conformidades,conclusion,fechallantas,cinturones,bolsasAire,testigosTablero) values " +
                                "('${caud}', '${t_au}','${nomi}','${nume}','${correo}','${fecha}','${motor}','${carroc}','${interior}','${adit}','${equip}','${n_confor}','${conclu}','${fechallantas}','${cinturon}','${bolsas}','${testigos}')"
                        val res = admin.Ejecuta(sentencia)
                        //Toast.makeText(this, "res= ${res}", Toast.LENGTH_SHORT).show();
                    }
                }catch (e: JSONException){
                    e.printStackTrace()
                    Toast.makeText(this, "No hay auditorias en la base de datos", Toast.LENGTH_SHORT).show()
                }
            },
            { error ->
                Toast.makeText(this, "Error llenarAuditorias: " + error.message.toString() , Toast.LENGTH_LONG).show()
                Log.d("Auditorias",error.message.toString() )
            }
        )
        VolleySingleton.getInstance(this).addToRequestQueue(jsonObjectRequest)
    }

    fun getAllCuestionarios(){
        val wsURL = Address.IP + "Auditoriapp/Login/llenarExamenes.php"
        val admin = DataBase(this)
        admin.Ejecuta("DELETE FROM examenVolley")
        val jsonObjectRequest = JsonObjectRequest(
            Request.Method.POST,wsURL,null,
            { response ->
                try{
                    val succ = response["success"]
                    val msg = response["message"]
                    val examenesJson = response.getJSONArray("examenes")
                    for (i in 0 until examenesJson.length()){
                        val cexa = examenesJson.getJSONObject(i).getString("c_examen")
                        val tipoe = examenesJson.getJSONObject(i).getString("tipo_examen")
                        val nomina = examenesJson.getJSONObject(i).getString("id_persona")
                        val corr = examenesJson.getJSONObject(i).getString("id_usuario")
                        val fech = examenesJson.getJSONObject(i).getString("fecha_aplicacion")
                        val calif1 = examenesJson.getJSONObject(i).getString("calif_uno")
                        val calif2 = examenesJson.getJSONObject(i).getString("calif_dos")
                        val calif3 = examenesJson.getJSONObject(i).getString("calif_tres")
                        val resultado = examenesJson.getJSONObject(i).getString("resultado")
                        val percentil = examenesJson.getJSONObject(i).getString("percentil")
                        val amanejando = examenesJson.getJSONObject(i).getString("aManejando")
                        val vehiculosd = examenesJson.getJSONObject(i).getString("VehiculosD")
                        val sentencia = "Insert into examenVolley(c_examen,tipo_examen,id_persona,id_usuario,fecha_aplicacion,calif_uno,calif_dos," +
                                "calif_tres,resultado,percentil,aManejando,VehiculosD) values " +
                                "('${cexa}', '${tipoe}','${nomina}','${corr}','${fech}','${calif1}','${calif2}','${calif3}','${resultado}','${percentil}','${amanejando}','${vehiculosd}')"
                        val res = admin.Ejecuta(sentencia)
                        //Toast.makeText(this, "res= ${res}", Toast.LENGTH_SHORT).show();
                    }
                }catch (e: JSONException){
                    e.printStackTrace()
                    Toast.makeText(this, "No hay examenes en la base de datos.", Toast.LENGTH_SHORT).show()
                }
            },
            { error ->
                Toast.makeText(this, "Error llenarExamenes: " + error.message.toString() , Toast.LENGTH_LONG).show();
                Log.d("Examenes",error.message.toString() )
            }
        )
        VolleySingleton.getInstance(this).addToRequestQueue(jsonObjectRequest)
    }

    fun getAllRevisiones(){
        val wsURL = Address.IP + "Auditoriapp/Login/llenarRevisiones.php"
        val admin = DataBase(this)
        admin.Ejecuta("DELETE FROM chequeoVolley")
        val jsonObjectRequest = JsonObjectRequest(
            Request.Method.POST,wsURL,null,
            { response ->
                try{
                    val succ = response["success"]
                    val msg = response["message"]
                    val RevisionesJson = response.getJSONArray("revision")
                    for (i in 0 until RevisionesJson.length()){
                        val cche = RevisionesJson.getJSONObject(i).getString("c_chequeo")
                        val nomina = RevisionesJson.getJSONObject(i).getString("id_persona")
                        val num = RevisionesJson.getJSONObject(i).getString("id_carro")
                        val corr = RevisionesJson.getJSONObject(i).getString("id_usuario")
                        val tipr = RevisionesJson.getJSONObject(i).getString("tipo_revision")
                        val tipm = RevisionesJson.getJSONObject(i).getString("tipo_mantenimiento")
                        val kmact = RevisionesJson.getJSONObject(i).getString("kilometraje_actual")
                        val fechact = RevisionesJson.getJSONObject(i).getString("fecha_actual")
                        val kmant = RevisionesJson.getJSONObject(i).getString("kilometraje_anterior")
                        val fechant = RevisionesJson.getJSONObject(i).getString("fecha_anterior")
                        val prox = RevisionesJson.getJSONObject(i).getString("proxima_revision")
                        val obs = RevisionesJson.getJSONObject(i).getString("observaciones")
                        val sentencia = "Insert into chequeoVolley(c_chequeo,id_persona,id_carro,id_usuario,tipo_revision,tipo_mantenimiento," +
                                "kilometraje_actual,fecha_actual,kilometraje_anterior,fecha_anterior,proxima_revision,observaciones) values " +
                                "('${cche}', '${nomina}','${num}','${corr}','${tipr}','${tipm}','${kmact}','${fechact}','${kmant}','${fechant}','${prox}','${obs}')"
                        val res = admin.Ejecuta(sentencia)
                        //Toast.makeText(this, "res= ${res}", Toast.LENGTH_SHORT).show();
                    }
                }catch (e: JSONException){
                    e.printStackTrace()
                    Toast.makeText(this, "No hay chequeos en la base de datos", Toast.LENGTH_SHORT).show()
                }
            },
            { error ->
                Toast.makeText(this, "Error llenarRevisiones: " + error.message.toString() , Toast.LENGTH_LONG).show();
                Log.d("Revisiones",error.message.toString() )
            }
        )
        VolleySingleton.getInstance(this).addToRequestQueue(jsonObjectRequest)
    }
    fun subirDatos(v: View){
        if (tengoInternet()){
            val subir =  Intent(this,SubirDatosMenu::class.java)
            subir.putExtra("id_usuario",idU)
            subir.putExtra("usuario",Usuario)
            subir.putExtra("id_rol",Rol)
            subir.putExtra("nomusuario",nombreUsuario)
            startActivity(subir)
            finish()
        }else{
            Toast.makeText(this, "No tienes conexion a internet.", Toast.LENGTH_SHORT).show();
        }
    }
    fun actDatos(){
        if(tengoInternet()){
            Toast.makeText(this, "Conexion a internet", Toast.LENGTH_SHORT).show()
            val admin = DataBase(this)
            val sentencia2 = "Delete from persona"
            val sentencia3 =  "Delete from direccion"
            val sentencia4 = "Delete from departamento"
            val sentencia5 =  "Delete from tipovehiculo"
            val sentencia6 =  "Delete from carro"
            val sentencia7 = "Delete from usuariosDisponibles"
            if (admin.Ejecuta(sentencia2)&& admin.Ejecuta(sentencia3)&& admin.Ejecuta(sentencia4)
                &&admin.Ejecuta(sentencia5)&&admin.Ejecuta(sentencia6)&&admin.Ejecuta(sentencia7)) {
                lifecycleScope.launch {
                    llenarempleados()
                    llenardependencias()
                    llenarareas()
                    llenarT_vehiculos()
                    llenarAutomoviles()
                    llenarUsuarios()
                }
                Toast.makeText(this, "¡Listo! "+nombreUsuario +" Base de Datos actualizada.", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "No se pudo Actualizar la BD.", Toast.LENGTH_SHORT).show()
            }
        }else{
            Toast.makeText(this, "No tienes Conexion a Internet, intenta más tarde", Toast.LENGTH_SHORT).show()
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

    fun llenarempleados(){
        val wsURL = Address.IP+"Auditoriapp/Login/llenarEmpleado.php"
        val admin = DataBase(this)
        admin.Ejecuta("DELETE FROM persona")
        val jsonObjectRequest = JsonObjectRequest(
            Request.Method.POST,wsURL,null,
            //Response.Listener
            { response ->
                try {
                    val succ = response["success"]
                    val msg = response["message"]
                    val empleadoJson = response.getJSONArray("empleados")
                    for (i in 0 until empleadoJson.length()){
                        val id = empleadoJson.getJSONObject(i).getString("id_persona")
                        val nomi = empleadoJson.getJSONObject(i).getString("num_nomina")
                        val nomb = empleadoJson.getJSONObject(i).getString("nombre")
                        val tip = empleadoJson.getJSONObject(i).getString("tiponomina")
                        val idDir = empleadoJson.getJSONObject(i).getString("id_direccion")
                        val idDepto = empleadoJson.getJSONObject(i).getString("id_departamento")
                        val sentencia = "insert into persona(id_persona,num_nomina,nombre,tiponomina,id_direccion,id_departamento)" +
                                "values ('${id}','${nomi}','${nomb}','${tip}','${idDir}','${idDepto}')"
                        val res = admin.Ejecuta(sentencia)
                        //Toast.makeText(this,"res= ${res}",Toast.LENGTH_SHORT).show();
                    }
                }catch (e: JSONException){
                    e.printStackTrace()
                    Toast.makeText(this,"Error al llenar los empleados en la base de datos.",Toast.LENGTH_LONG).show()
                }
            },
            { error ->
                Toast.makeText(this,"Error llenarEmpleado: "+ error.message.toString(), Toast.LENGTH_LONG).show();
                Log.d("Alejandro",error.message.toString())
            }
        )
        VolleySingleton.getInstance(this).addToRequestQueue(jsonObjectRequest)
    }

    fun llenardependencias(){
        val wsURL = Address.IP+"Auditoriapp/Login/llenarDependencias.php"
        val admin = DataBase(this)
        admin.Ejecuta("DELETE FROM direccion")
        val jsonObjectRequest = JsonObjectRequest(
            Request.Method.POST, wsURL, null,
            //Response.Listener
            { response ->
                try {
                    val succ = response["success"]
                    val msg = response["message"]
                    val dependenciasJson = response.getJSONArray("dependencia")
                    for (i in 0 until dependenciasJson.length()) {
                        val cdep = dependenciasJson.getJSONObject(i).getString("id_direccion")
                        val nomd = dependenciasJson.getJSONObject(i).getString("direccion")
                        val sentencia = "insert into direccion(id_direccion,direccion) " +
                                "values ('${cdep}','${nomd}')"
                        val res = admin.Ejecuta(sentencia)
                        //Toast.makeText(this, "res= ${res}", Toast.LENGTH_SHORT).show();
                    }
                } catch (e: JSONException) {
                    e.printStackTrace()
                    Toast.makeText(this, "Error al llenar las dependencias en la base de datos.", Toast.LENGTH_SHORT).show();
                }
            },
            //Response.ErrorListener
            { error ->
                Toast.makeText(this, "Error LlenarDirecciones: " + error.message.toString(),Toast.LENGTH_LONG).show();
                Log.d("Alejandro", error.message.toString())
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
                        //Toast.makeText(this,"res= ${res}",Toast.LENGTH_SHORT).show();
                    }
                } catch (e: JSONException) {
                    e.printStackTrace()
                    Toast.makeText(this, "Error al llenar las dependencias en la base de datos.", Toast.LENGTH_SHORT).show();
                }
            },
            //Response.ErrorListener
            { error ->
                Toast.makeText(this,"Error LlenarDepto: "+ error.message.toString(), Toast.LENGTH_LONG).show();
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
                        //Toast.makeText(this,"res= ${res}",Toast.LENGTH_SHORT).show();
                    }
                } catch (e: JSONException) {
                    e.printStackTrace()
                    Toast.makeText(this, "Error al llenar los tipos de vehiculo en la base de datos.", Toast.LENGTH_SHORT).show();
                }
            },
            //Response.ErrorListener
            { error ->
                Toast.makeText(this,"Error llenarTipo_vehiculo: "+ error.message.toString(), Toast.LENGTH_LONG).show();
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
                        //Toast.makeText(this,"res= ${res}",Toast.LENGTH_SHORT).show();
                    }
                } catch (e: JSONException) {
                    e.printStackTrace()
                    Toast.makeText(this, "Error al llenar los carros en la base de datos.", Toast.LENGTH_SHORT).show();
                }
            },
            //Response.ErrorListener
            { error ->
                Toast.makeText(this,"Error llenarCarro: "+ error.message.toString(), Toast.LENGTH_LONG).show();
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