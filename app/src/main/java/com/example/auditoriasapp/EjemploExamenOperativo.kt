package com.example.auditoriasapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import com.example.auditoriasapp.Database.DataBase
import kotlinx.android.synthetic.main.activity_auditoria.*
import kotlinx.android.synthetic.main.activity_ejemplo_examen_operativo.*
import java.text.SimpleDateFormat
import java.util.*

class EjemploExamenOperativo : AppCompatActivity() {
    private lateinit var idU: String
    private lateinit var nomU: String
    private lateinit var Rol: String
    private lateinit var nombreUsuario:String

    var nomina : String=""
    var tipoNomina : String=""
    var nombreEmp : String=""
    var area : String=""
    var fech: String=""
    var fecha:String= ""
    var vehiculosD : String= ""
    var tipoE: String= "Operativo"
    var aManejando: String = ""
    var c_examen:String = ""
    var id_persona:Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ejemplo_examen_operativo)
        val exaOp = intent
        if (exaOp != null && exaOp.hasExtra("id_usuario") && exaOp.hasExtra("usuario")&& exaOp.hasExtra("id_rol")) {
            idU = exaOp.getStringExtra("id_usuario")
            nomU = exaOp.getStringExtra("usuario")
            Rol = exaOp.getStringExtra("id_rol")
            nombreUsuario = exaOp.getStringExtra("nomusuario")
            aplicaraExOpera.text="$nombreUsuario"
        }
        val C = Calendar.getInstance()
        //Se obtiene el formato requerido
        val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
        val strDate = sdf.format(C.time)
        //Se le asigna la fecha al al textview
        FechaExOpera.setText(strDate)
        fecha = FechaExOpera.text.toString()

        //Spinner Tipo de Nomina
        val tipoNom = arrayOf("Catorcenal","Quincenal")
        val arrayAdapter = ArrayAdapter(this,R.layout.spinner_item_new,tipoNom)
        id_spinnerTipoNomina_exaOpera.adapter = arrayAdapter
        id_spinnerTipoNomina_exaOpera.onItemSelectedListener = object :
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
        checkInfoInstruccion.setOnClickListener {
            if(checkInfoInstruccion.isChecked){
                txtInst.visibility = View.VISIBLE
                txt_instruccion.visibility = View.VISIBLE
            }else{
                txtInst.visibility = View.GONE
                txt_instruccion.visibility = View.GONE
            }
        }
        txtInst.visibility = View.GONE
        txt_instruccion.visibility = View.GONE

        checkInfoNomi.setOnClickListener {
            if (checkInfoNomi.isChecked){
                nominaOpe.visibility = View.VISIBLE
                numero_nomExOpera.visibility = View.VISIBLE
                btn_nomina_exOpera.visibility = View.VISIBLE
                txTipoNomina_exaOpera.visibility = View.VISIBLE
                id_spinnerTipoNomina_exaOpera.visibility = View.VISIBLE
                NomNomina_ExaOpera.visibility = View.VISIBLE
                etNombreNom_ExaOpera.visibility = View.VISIBLE
                t_area_exaOpera.visibility = View.VISIBLE
                areaNom_exaOpera.visibility = View.VISIBLE
                idSwitchExaOpera.visibility = View.VISIBLE
                txtVehiculos.visibility = View.VISIBLE
                dominio_vehiculos.visibility = View.VISIBLE
                txtExperiencia.visibility = View.VISIBLE
                Ex_Manejando.visibility = View.VISIBLE
            }else{
                nominaOpe.visibility = View.GONE
                numero_nomExOpera.visibility = View.GONE
                btn_nomina_exOpera.visibility = View.GONE
                txTipoNomina_exaOpera.visibility = View.GONE
                id_spinnerTipoNomina_exaOpera.visibility = View.GONE
                NomNomina_ExaOpera.visibility = View.GONE
                etNombreNom_ExaOpera.visibility = View.GONE
                t_area_exaOpera.visibility = View.GONE
                areaNom_exaOpera.visibility = View.GONE
                idSwitchExaOpera.visibility = View.GONE
                txtVehiculos.visibility = View.GONE
                dominio_vehiculos.visibility = View.GONE
                txtExperiencia.visibility = View.GONE
                Ex_Manejando.visibility = View.GONE
            }
        }
        nominaOpe.visibility = View.GONE
        numero_nomExOpera.visibility = View.GONE
        btn_nomina_exOpera.visibility = View.GONE
        txTipoNomina_exaOpera.visibility = View.GONE
        id_spinnerTipoNomina_exaOpera.visibility = View.GONE
        NomNomina_ExaOpera.visibility = View.GONE
        etNombreNom_ExaOpera.visibility = View.GONE
        t_area_exaOpera.visibility = View.GONE
        areaNom_exaOpera.visibility = View.GONE
        idSwitchExaOpera.visibility = View.GONE
        txtVehiculos.visibility = View.GONE
        dominio_vehiculos.visibility = View.GONE
        txtExperiencia.visibility = View.GONE
        Ex_Manejando.visibility = View.GONE

        checkInfoquestion.setOnClickListener {
            if (checkInfoquestion.isChecked){
                txt_Explicacion.visibility = View.VISIBLE
                Question.visibility = View.VISIBLE
                ExaOperaEjemploUno.visibility = View.VISIBLE
                ExaOperaEjemploDos.visibility = View.VISIBLE
                ExaOperaEjemploTres.visibility = View.VISIBLE
                ExaOperaEjemploCuatro.visibility = View.VISIBLE
            }else{
                txt_Explicacion.visibility = View.GONE
                Question.visibility = View.GONE
                ExaOperaEjemploUno.visibility = View.GONE
                ExaOperaEjemploDos.visibility = View.GONE
                ExaOperaEjemploTres.visibility = View.GONE
                ExaOperaEjemploCuatro.visibility = View.GONE
            }
        }
        txt_Explicacion.visibility = View.GONE
        Question.visibility = View.GONE
        ExaOperaEjemploUno.visibility = View.GONE
        ExaOperaEjemploDos.visibility = View.GONE
        ExaOperaEjemploTres.visibility = View.GONE
        ExaOperaEjemploCuatro.visibility = View.GONE
    }
    fun buscarEmpleadoOp(v:View){
        if (etNombreNom_ExaOpera.text.toString().isEmpty() && numero_nomExOpera.text.toString().isEmpty()){
            Toast.makeText(this,"Debes llenar los datos para buscar",Toast.LENGTH_LONG).show()
        }
        when(etNombreNom_ExaOpera.text.toString().isNotEmpty() && idSwitchExaOpera.isChecked && Rol.equals("2") && numero_nomExOpera.text.toString().isEmpty()){
            true ->{
                Toast.makeText(this,"No puedes agregar una nueva nómina",Toast.LENGTH_LONG).show()
            }
        }
        //agregar una nomina provisional
        when(etNombreNom_ExaOpera.text.toString().isNotEmpty() && idSwitchExaOpera.isChecked && Rol.equals("1") && numero_nomExOpera.text.toString().isEmpty()){
            true->{
                agregarN()
                bNombre()
            }
        }
        //buscar por nomina
        when(numero_nomExOpera.text.toString().isNotEmpty() && areaNom_exaOpera.text.toString().isEmpty()){
            true->{
                bNomina()
            }
        }
        //buscar por nombre
        when(etNombreNom_ExaOpera.text.toString().isNotEmpty() && areaNom_exaOpera.text.toString().isEmpty() && !idSwitchExaOpera.isChecked){
            true->{
                bNombre()
            }
        }
    }
    //Agregar nomina nueva
    fun agregarN(){
        var sentencia: String = ""
        var sentencia2: String = ""
        nombreEmp = etNombreNom_ExaOpera.text.toString()
        val timeStamp: String = SimpleDateFormat("ddHHmmss").format(Date())
        tipoNomina
        nomina = "EN"+timeStamp
        area = "99999"
        val direccion = "11111"
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
    fun bNomina(){
        var query: String = ""
        nomina = numero_nomExOpera.text.toString()
        tipoNomina
        query =
            "Select p.id_persona,p.num_nomina,p.nombre,p.tiponomina,p.id_direccion,p.id_departamento,d.direccion,dp.departamento " +
                    "from persona as p inner join direccion as d on p.id_direccion=d.id_direccion inner join departamento as dp on dp.id_departamento=p.id_departamento where p.num_nomina ='$nomina' and p.tiponomina ='$tipoNomina'"
        val admin = DataBase(this)
        val cur = admin.Consulta(query)
        if (cur == null) {
            admin.close()
            Toast.makeText(this, "Error de Capa 8", Toast.LENGTH_SHORT).show()
        } else {
            if (cur.moveToFirst()) {
                Toast.makeText(this, "Se encontro el empleado", Toast.LENGTH_SHORT).show()
                etNombreNom_ExaOpera.setText(cur.getString(2))//nombre
                areaNom_exaOpera.setText(cur.getString(6))//direccion
                id_persona = cur.getInt(0)//id_persona
                etNombreNom_ExaOpera.isEnabled = false
                id_spinnerTipoNomina_exaOpera.isEnabled = false
                numero_nomExOpera.isEnabled = false
                idSwitchExaOpera.isEnabled = false
                admin.close()
            } else {
                admin.close()
                Toast.makeText(this, "No se Encontro el empleado", Toast.LENGTH_SHORT).show()
            }
        }
    }
    fun bNombre(){
        var query: String = ""
        nombreEmp = etNombreNom_ExaOpera.text.toString()
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
                numero_nomExOpera.setText(cur.getString(1))//nombre
                areaNom_exaOpera.setText(cur.getString(6))//direccion
                id_persona = cur.getInt(0)//id_persona
                etNombreNom_ExaOpera.setText(cur.getString(2))//nombre
                etNombreNom_ExaOpera.isEnabled = false
                id_spinnerTipoNomina_exaOpera.isEnabled = false
                numero_nomExOpera.isEnabled = false
                idSwitchExaOpera.isEnabled = false
                admin.close()
            } else {
                admin.close()
                Toast.makeText(this, "No se Encontro el empleado", Toast.LENGTH_SHORT).show()
            }
        }
    }


    fun limpiar_clickOperativo(v: View){
        etNombreNom_ExaOpera.isEnabled = true
        id_spinnerTipoNomina_exaOpera.isEnabled = true
        numero_nomExOpera.isEnabled = true
        idSwitchExaOpera.isEnabled = true
        numero_nomExOpera.setText("")
        etNombreNom_ExaOpera.setText("")
        areaNom_exaOpera.setText("")
        numero_nomExOpera.requestFocus()
    }
    fun regresarMenuExamenes(v: View){
        val menuExa = Intent(this,menuActivity::class.java)
        startActivity(menuExa)
        finish()
    }

    fun exOpera (v: View){
        if (id_persona !=0 && dominio_vehiculos.text.toString().isNotEmpty() && Ex_Manejando.text.toString().isNotEmpty()){
            nomina = numero_nomExOpera.text.toString()
            tipoNomina
            nombreEmp = etNombreNom_ExaOpera.text.toString()
            area = areaNom_exaOpera.text.toString()
            vehiculosD = dominio_vehiculos.text.toString()
            tipoE
            aManejando = Ex_Manejando.text.toString()
            val timeStamp: String = SimpleDateFormat("ddMMyyyyHHmmss").format(Date())
            fech = timeStamp
            fecha
            idU
            nombreUsuario
            c_examen = "EO"+fech
        val opera = Intent(this,Examen_Operativo::class.java)
        opera.putExtra("tipoExamen", tipoE)
        opera.putExtra("id_persona",id_persona)
        opera.putExtra("nombreEmp",nombreEmp)
        opera.putExtra("id_usuario",idU)
        opera.putExtra("aplico",nombreUsuario)
        opera.putExtra("area",area)
        opera.putExtra("vehiculosD",vehiculosD)
        opera.putExtra("tipoNomina",tipoNomina)
        opera.putExtra("aManejendo",aManejando)
        opera.putExtra("fechaAplicacion",fecha)
        opera.putExtra("c_examen",c_examen)
        startActivity(opera)
        finish()
    }else{
            Toast.makeText(this, "Debes llenar todos los datos", Toast.LENGTH_SHORT).show();
            numero_nomExOpera.requestFocus()
        }
    }
}