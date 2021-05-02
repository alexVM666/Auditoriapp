package com.example.auditoriasapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import com.example.auditoriasapp.Database.DataBase
import kotlinx.android.synthetic.main.activity_auditoria.*
import kotlinx.android.synthetic.main.activity_ejemplo_examen_admin.*
import java.text.SimpleDateFormat
import java.util.*

class EjemploExamenAdmin : AppCompatActivity() {
    private lateinit var buscarEmpleado:String
    private lateinit var idU: String
    private lateinit var nomU: String
    private lateinit var Rol: String
    private lateinit var nombreUsuario:String

    var nomina : String=""
    var tipoNomina : String=""
    var nombreEmp : String=""
    var area : String=""
    var fech: String=""
    var fechaA: String =""
    var vehiculosD : String= ""
    var tipoE: String= "Administrativo"
    var aManejando: String = ""
    var c_examen:String = ""
    var id_persona: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ejemplo_examen_admin)
        val exa = intent
        if (exa != null && exa.hasExtra("id_usuario") && exa.hasExtra("usuario")&& exa.hasExtra("id_rol")) {
            idU = exa.getStringExtra("id_usuario")
            nomU = exa.getStringExtra("usuario")
            Rol = exa.getStringExtra("id_rol")
            nombreUsuario = exa.getStringExtra("nomusuario")
            aplicaraExAdmin.text="$nombreUsuario"
        }
        val C = Calendar.getInstance()
        //Se obtiene el formato requerido
        val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
        val strDate = sdf.format(C.time)
        //Se le asigna la fecha al al textview
        FechaExAdmin.setText(strDate)
        fechaA = FechaExAdmin.text.toString()

        //Spinner Tipo de Nomina
        val tipoNom = arrayOf("Catorcenal","Quincenal")
        val arrayAdapter = ArrayAdapter(this,R.layout.spinner_item_new,tipoNom)
        id_spinnerTipoNomina_exaAdmin.adapter = arrayAdapter
        id_spinnerTipoNomina_exaAdmin.onItemSelectedListener = object :
            AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                Toast.makeText(parent?.context,"Tipo de nomina: "+parent?.getItemAtPosition(position).toString(), Toast.LENGTH_SHORT).show()
                tipoNomina = parent?.getItemAtPosition(position).toString()
            }
        }
        checkInfoInstrucciones.setOnClickListener {
            if (checkInfoInstrucciones.isChecked){
                infoInstrucciones.visibility = View.VISIBLE
                instrucciones.visibility = View.VISIBLE
            }else{
                infoInstrucciones.visibility = View.GONE
                instrucciones.visibility = View.GONE
            }
        }
        infoInstrucciones.visibility = View.GONE
        instrucciones.visibility = View.GONE

        checkInfoNominaa.setOnClickListener {
            if (checkInfoNominaa.isChecked){
                txtNominaExamenAdmin.visibility = View.VISIBLE
                nomina_ExaAdmin.visibility = View.VISIBLE
                btn_nomina_exAdmin.visibility = View.VISIBLE
                txTipoNomina_exaAdmin.visibility = View.VISIBLE
                id_spinnerTipoNomina_exaAdmin.visibility = View.VISIBLE
                NomNomina_ExaAdmin.visibility = View.VISIBLE
                etNombreNom_ExaAdmin.visibility = View.VISIBLE
                t_area_exaAdmin.visibility = View.VISIBLE
                areaNom_exaAdmin.visibility = View.VISIBLE
                idSwitchExaAdmin.visibility = View.VISIBLE
                dominio_vehiculos_admin.visibility = View.VISIBLE
                txt_dominioAdmin.visibility = View.VISIBLE
                txt_experiencia.visibility = View.VISIBLE
                Ex_Manejando_Admin.visibility = View.VISIBLE
            }else{
                txtNominaExamenAdmin.visibility = View.GONE
                nomina_ExaAdmin.visibility = View.GONE
                btn_nomina_exAdmin.visibility = View.GONE
                txTipoNomina_exaAdmin.visibility = View.GONE
                id_spinnerTipoNomina_exaAdmin.visibility = View.GONE
                NomNomina_ExaAdmin.visibility = View.GONE
                etNombreNom_ExaAdmin.visibility = View.GONE
                t_area_exaAdmin.visibility = View.GONE
                areaNom_exaAdmin.visibility = View.GONE
                idSwitchExaAdmin.visibility = View.GONE
                dominio_vehiculos_admin.visibility = View.GONE
                txt_dominioAdmin.visibility = View.GONE
                txt_experiencia.visibility = View.GONE
                Ex_Manejando_Admin.visibility = View.GONE
            }
        }
        txtNominaExamenAdmin.visibility = View.GONE
        nomina_ExaAdmin.visibility = View.GONE
        btn_nomina_exAdmin.visibility = View.GONE
        txTipoNomina_exaAdmin.visibility = View.GONE
        id_spinnerTipoNomina_exaAdmin.visibility = View.GONE
        NomNomina_ExaAdmin.visibility = View.GONE
        etNombreNom_ExaAdmin.visibility = View.GONE
        t_area_exaAdmin.visibility = View.GONE
        areaNom_exaAdmin.visibility = View.GONE
        idSwitchExaAdmin.visibility = View.GONE
        dominio_vehiculos_admin.visibility = View.GONE
        txt_dominioAdmin.visibility = View.GONE
        txt_experiencia.visibility = View.GONE
        Ex_Manejando_Admin.visibility = View.GONE

        checkejemploPregunta.setOnClickListener {
            if (checkejemploPregunta.isChecked){
                txtInstruccionesPre.visibility = View.VISIBLE
                pregunta.visibility = View.VISIBLE
                ExaAdminEjemploUno.visibility = View.VISIBLE
                ExaAdminEjemploDos.visibility = View.VISIBLE
                ExaAdminEjemploTres.visibility = View.VISIBLE
                ExaAdminEjemploCuatro.visibility = View.VISIBLE
            }else{
                txtInstruccionesPre.visibility = View.GONE
                pregunta.visibility = View.GONE
                ExaAdminEjemploUno.visibility = View.GONE
                ExaAdminEjemploDos.visibility = View.GONE
                ExaAdminEjemploTres.visibility = View.GONE
                ExaAdminEjemploCuatro.visibility = View.GONE
            }
        }
        txtInstruccionesPre.visibility = View.GONE
        pregunta.visibility = View.GONE
        ExaAdminEjemploUno.visibility = View.GONE
        ExaAdminEjemploDos.visibility = View.GONE
        ExaAdminEjemploTres.visibility = View.GONE
        ExaAdminEjemploCuatro.visibility = View.GONE
    }
    fun buscarem(v: View){
        if (etNombreNom_ExaAdmin.text.toString().isEmpty()&&nomina_ExaAdmin.text.toString().isEmpty() ){
            Toast.makeText(this,"Llena el nombre o nomina, para buscar",Toast.LENGTH_LONG).show()
        }
        //por nombre
        when(etNombreNom_ExaAdmin.text.toString().isNotEmpty()&& areaNom_exaAdmin.text.toString().isEmpty()&& !idSwitchExaAdmin.isChecked){
            true ->{
                bNombre()
            }
        }
        //por nomina
        when(nomina_ExaAdmin.text.toString().isNotEmpty() && areaNom_exaAdmin.text.toString().isEmpty()){
            true ->{
                BporNomina()
            }
        }
        //agregar una nueva nomina
        when(etNombreNom_ExaAdmin.text.toString().isNotEmpty() && idSwitchExaAdmin.isChecked && Rol.equals("1") && nomina_ExaAdmin.text.toString().isEmpty()){
            true ->{
                agregarNom()
                bNombre()
            }
        }
        when(etNombreNom_ExaAdmin.text.toString().isNotEmpty() && idSwitchExaAdmin.isChecked && Rol.equals("2") && nomina_ExaAdmin.text.toString().isEmpty()){
            true ->{
                Toast.makeText(this,"No puedes insertar una nomina nueva",Toast.LENGTH_LONG).show()
            }
        }
    }
    fun agregarNom(){
        var sentencia: String = ""
        var sentencia2: String = ""
        nombreEmp = etNombreNom_ExaAdmin.text.toString()
        val timeStamp: String = SimpleDateFormat("ddHHmmss").format(Date())
        tipoNomina
        nomina = "EN"+timeStamp
        area = "99999"
        var direccion = "11111"
        sentencia =
            "Insert into personaNuevo(id_persona,num_nomina,nombre,tiponomina,id_direccion,id_departamento,AgregadoP) values" +
                    "('$timeStamp','$nomina','$nombreEmp','$tipoNomina','$area','$direccion','$idU')"
        sentencia2 =
            "Insert into persona(id_persona,num_nomina,nombre,tiponomina,id_direccion,id_departamento) values" +
                    "('$timeStamp','$nomina','$nombreEmp','$tipoNomina','$area','$direccion')"
        val admin = DataBase(this)
        if (admin.Ejecuta(sentencia)&& admin.Ejecuta(sentencia2)) {
            admin.close()
            Toast.makeText(this, "Se guardo el empleado", Toast.LENGTH_LONG).show()
        } else {
            admin.close()
            Toast.makeText(this, "Error usuario ya existe", Toast.LENGTH_LONG).show()
        }
    }
    fun BporNomina(){
        var query: String = ""
        nomina = nomina_ExaAdmin.text.toString()
        tipoNomina
        query =
            "Select p.id_persona,p.num_nomina,p.nombre,p.tiponomina,p.id_direccion,p.id_departamento,d.direccion,dp.departamento " +
                    "from persona as p inner join direccion as d on p.id_direccion=d.id_direccion inner join departamento as dp on dp.id_departamento=p.id_departamento where p.num_nomina ='$nomina' and p.tiponomina ='$tipoNomina'"
        var admin = DataBase(this)
        var cur = admin.Consulta(query)
        if (cur == null) {
            admin.close()
            Toast.makeText(this, "Error de Capa 8", Toast.LENGTH_SHORT).show()
        } else {
            if (cur.moveToFirst()) {
                Toast.makeText(this, "Se encontro el empleado", Toast.LENGTH_SHORT).show()
                etNombreNom_ExaAdmin.setText(cur.getString(2))//nombre
                areaNom_exaAdmin.setText(cur.getString(6))//direccion
                id_persona = cur.getInt(0)//id_persona
                etNombreNom_ExaAdmin.isEnabled = false
                id_spinnerTipoNomina_exaAdmin.isEnabled = false
                nomina_ExaAdmin.isEnabled = false
                idSwitchExaAdmin.isEnabled = false
                admin.close()
            } else {
                admin.close()
                Toast.makeText(this, "No se Encontro el empleado", Toast.LENGTH_SHORT).show()
            }
        }
    }
    fun bNombre() {
                var query: String = ""
                nombreEmp = etNombreNom_ExaAdmin.text.toString()
                tipoNomina
                query = "Select p.id_persona,p.num_nomina,p.nombre,p.tiponomina,p.id_direccion,p.id_departamento,d.direccion,dp.departamento " +
                        "from persona as p inner join direccion as d on p.id_direccion=d.id_direccion inner join departamento as dp on dp.id_departamento=p.id_departamento where upper(p.nombre) =upper('$nombreEmp') and p.tiponomina ='$tipoNomina'"
                val admin = DataBase(this)
                val cur = admin.Consulta(query)
                if (cur == null) {
                    admin.close()
                    Toast.makeText(this, "Error de Capa 8", Toast.LENGTH_SHORT).show()
                } else {
                    if (cur.moveToFirst()) {
                        Toast.makeText(this, "Se encontro el empleado", Toast.LENGTH_SHORT).show()
                        nomina_ExaAdmin.setText(cur.getString(1))//nomina
                        areaNom_exaAdmin.setText(cur.getString(6))//direccion
                        id_persona = cur.getInt(0)//id_persona
                        etNombreNom_ExaAdmin.setText(cur.getString(2))//nombre
                        etNombreNom_ExaAdmin.isEnabled = false
                        id_spinnerTipoNomina_exaAdmin.isEnabled = false
                        nomina_ExaAdmin.isEnabled = false
                        idSwitchExaAdmin.isEnabled = false
                        admin.close()
                    } else {
                        Toast.makeText(this, "No se Encontro el empleado", Toast.LENGTH_SHORT).show()
                        admin.close()
                    }
                }
    }

    fun limpiar_click(v: View){
        etNombreNom_ExaAdmin.isEnabled = true
        id_spinnerTipoNomina_exaAdmin.isEnabled = true
        nomina_ExaAdmin.isEnabled = true
        idSwitchExaAdmin.isEnabled = true
        nomina_ExaAdmin.setText("")
        etNombreNom_ExaAdmin.setText("")
        areaNom_exaAdmin.setText("")
        nomina_ExaAdmin.requestFocus()
    }
    fun regresarMenuExa(v: View){
        val menuExa = Intent(this,menuActivity::class.java)
        startActivity(menuExa)
        finish()
    }
    fun exAdmin (v: View){
        if (dominio_vehiculos_admin.text.toString().isNotEmpty() && Ex_Manejando_Admin.text.toString().isNotEmpty()&& id_persona != 0 ){
            nomina = nomina_ExaAdmin.text.toString()
            tipoNomina
            nombreEmp = etNombreNom_ExaAdmin.text.toString()
            area = areaNom_exaAdmin.text.toString()
            vehiculosD = dominio_vehiculos_admin.text.toString()
            tipoE
            aManejando = Ex_Manejando_Admin.text.toString()
            val timeStamp: String = SimpleDateFormat("yyyyMMddHHmmss").format(Date())
            fech = timeStamp
            fechaA
            idU
            nombreUsuario
            c_examen = "EA"+fech

            val examenAd = Intent(this,Examen_Administrativo::class.java)
            examenAd.putExtra("c_examen",c_examen)
            examenAd.putExtra("tipoExamen", tipoE)
            examenAd.putExtra("id_usuario",idU)
            examenAd.putExtra("id_persona",id_persona)
            examenAd.putExtra("usuario",nombreUsuario)
            examenAd.putExtra("nombreEmp",nombreEmp)
            examenAd.putExtra("area",area)
            examenAd.putExtra("vehiculosD",vehiculosD)
            examenAd.putExtra("tipoNomina",tipoNomina)
            examenAd.putExtra("aManejendo",aManejando)
            examenAd.putExtra("fechaAplicacion",fechaA)
            startActivity(examenAd)
            finish()
        }else{
            Toast.makeText(this, "Debes llenar todos los datos", Toast.LENGTH_SHORT).show();
            nomina_ExaAdmin.requestFocus()
        }
    }
}

