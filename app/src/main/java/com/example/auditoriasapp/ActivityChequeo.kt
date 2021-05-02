package com.example.auditoriasapp

import android.app.DatePickerDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.RadioButton
import android.widget.Toast
import com.example.auditoriasapp.Database.DataBase
import kotlinx.android.synthetic.main.activity_chequeo.*
import kotlinx.android.synthetic.main.activity_chequeo.N_Economico
import kotlinx.android.synthetic.main.activity_chequeo.NomNomina
import kotlinx.android.synthetic.main.activity_chequeo.areanombre
import kotlinx.android.synthetic.main.activity_chequeo.id_auto
import kotlinx.android.synthetic.main.activity_chequeo.t_area
import kotlinx.android.synthetic.main.activity_chequeo.t_deauto
import kotlinx.android.synthetic.main.activity_chequeo.t_marca
import kotlinx.android.synthetic.main.activity_chequeo.t_placas
import kotlinx.android.synthetic.main.activity_chequeo.t_submarca
import kotlinx.android.synthetic.main.activity_chequeo.textNomina
import kotlinx.android.synthetic.main.activity_chequeo.txTipoNomina
import java.text.SimpleDateFormat
import java.util.*

class ActivityChequeo : AppCompatActivity() {
    private lateinit var idU: String
    private lateinit var nomU: String
    private lateinit var nombreUsuario:String
    private lateinit var Rol:String

    var id_persona:Int = 0
    var Tnomina:String = ""
    var fech: String = ""
    var fechaActual: String = ""
    var nomina : String=""
    var nombreEmp : String=""
    var area : String=""

    var numeroEconomico: String = ""
    var placas: String = ""
    var id_carro:Int = 0

    var c_chequeo: String = ""
    var tipo_revision: String = ""
    var tipo_mantenimiento: String = ""
    var km_actual: String = ""
    var km_anterior: String = ""
    var observaciones: String = ""
    var fechaAnterior: String = ""
    var proximaRev: String = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chequeo)
        val check = intent
        if(check != null && check.hasExtra("id_usuario") && check.hasExtra("usuario")){
            idU = check.getStringExtra("id_usuario")
            nomU = check.getStringExtra("usuario")
            nombreUsuario = check.getStringExtra("nomusuario")
            Rol = check.getStringExtra("id_rol")
            nombrechequeo.text = "$nombreUsuario"
        }
        //Spinner Tipo de Nomina
        val tipoNom = arrayOf("Catorcenal","Quincenal")
        val arrayAdapter = ArrayAdapter(this,R.layout.spinner_item_new,tipoNom)
        id_spinnerTipoNominaChequeo.adapter = arrayAdapter
        id_spinnerTipoNominaChequeo.onItemSelectedListener = object :
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
                Tnomina = parent?.getItemAtPosition(position).toString()
            }
        }
        //Calendario
        val C = Calendar.getInstance()
        //Se obtiene el formato requerido
        val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
        val strDate = sdf.format(C.time)
        //Se le asigna la fecha al al textview
        txFechaCheck.setText(strDate)
        fechaActual = strDate
        val timeStamp: String = SimpleDateFormat("yyyyMMddHHmmss").format(Date())
        fech = timeStamp

        btn_borrartxtChe1.setOnClickListener {
            Nomina_id_Chequeo.isEnabled = true
            etNombreNom_chequeo.isEnabled = true
            areaNom_chequeo.isEnabled = true
            idSwitch_chequeo.isEnabled = true
            Nomina_id_Chequeo.setText("")
            etNombreNom_chequeo.setText("")
            areaNom_chequeo.setText("")
            Nomina_id_Chequeo.requestFocus()
        }
        btn_borrartxtChe2.setOnClickListener {
            id_NEconomicoChequeo.isEnabled = true
            id_placasChequeo.isEnabled =  true
            id_placasChequeo.setText("")
            id_NEconomicoChequeo.setText("")
            id_area_nombreChequeo.setText("")
            tipo_vehiculoChequeo.setText("")
            T_vehiculoChequeo.setText("")
            id_marcaChequeo.setText("")
            id_submarcaChequeo.setText("")
            idUltimoKM.setText("")
            fechaUltimaRevision.setText("")
            id_NEconomicoChequeo.requestFocus()
        }
        //Calendario
        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)
        //Boton click fecha
        b_proximafecha.setOnClickListener {
            val dpd = DatePickerDialog(this, DatePickerDialog.OnDateSetListener { view, mYear, mMonth, mDay ->
                val nMonth = mMonth+1
                idProximafecha.setText(""+mDay + "/" + nMonth + "/"+ mYear)
            },year,month,day)
            dpd.datePicker.minDate = System.currentTimeMillis() - 1000
            dpd.show()
        }

        id_otroTipoRevision.visibility = View.GONE
        id_otroTipoMantenimiento.visibility = View.GONE
        txtProximafecha.visibility = View.GONE
        idProximafecha.visibility = View.GONE
        b_proximafecha.visibility = View.GONE
        txtProximoKM.visibility = View.GONE
        id_kmProximo.visibility = View.GONE

        checkinfoUsuario.setOnClickListener {
            if (checkinfoUsuario.isChecked){
                textNomina.visibility = View.VISIBLE
                Nomina_id_Chequeo.visibility = View.VISIBLE
                b_nomina_chequeo.visibility = View.VISIBLE
                txTipoNomina.visibility = View.VISIBLE
                id_spinnerTipoNominaChequeo.visibility = View.VISIBLE
                NomNomina.visibility = View.VISIBLE
                etNombreNom_chequeo.visibility = View.VISIBLE
                t_area.visibility = View.VISIBLE
                areaNom_chequeo.visibility = View.VISIBLE
                idSwitch_chequeo.visibility = View.VISIBLE
                btn_borrartxtChe1.visibility = View.VISIBLE
            }else{
                textNomina.visibility = View.GONE
                Nomina_id_Chequeo.visibility = View.GONE
                b_nomina_chequeo.visibility = View.GONE
                txTipoNomina.visibility = View.GONE
                id_spinnerTipoNominaChequeo.visibility = View.GONE
                NomNomina.visibility = View.GONE
                etNombreNom_chequeo.visibility = View.GONE
                t_area.visibility = View.GONE
                areaNom_chequeo.visibility = View.GONE
                idSwitch_chequeo.visibility = View.GONE
                btn_borrartxtChe1.visibility = View.GONE
            }
        }
        textNomina.visibility = View.GONE
        Nomina_id_Chequeo.visibility = View.GONE
        b_nomina_chequeo.visibility = View.GONE
        txTipoNomina.visibility = View.GONE
        id_spinnerTipoNominaChequeo.visibility = View.GONE
        NomNomina.visibility = View.GONE
        etNombreNom_chequeo.visibility = View.GONE
        t_area.visibility = View.GONE
        areaNom_chequeo.visibility = View.GONE
        idSwitch_chequeo.visibility = View.GONE
        btn_borrartxtChe1.visibility = View.GONE

        checkInfoVehiculoChequeo.setOnClickListener {
            if (checkInfoVehiculoChequeo.isChecked) {
                N_Economico.visibility = View.VISIBLE
                id_NEconomicoChequeo.visibility = View.VISIBLE
                btnB_Neco_chequeo.visibility = View.VISIBLE
                areanombre.visibility = View.VISIBLE
                id_area_nombreChequeo.visibility = View.VISIBLE
                t_deauto.visibility = View.VISIBLE
                tipo_vehiculoChequeo.visibility = View.VISIBLE
                id_auto.visibility = View.VISIBLE
                T_vehiculoChequeo.visibility = View.VISIBLE
                t_marca.visibility = View.VISIBLE
                id_marcaChequeo.visibility = View.VISIBLE
                t_submarca.visibility = View.VISIBLE
                id_submarcaChequeo.visibility = View.VISIBLE
                t_placas.visibility = View.VISIBLE
                id_placasChequeo.visibility = View.VISIBLE
                txtultimafecha_rev.visibility = View.VISIBLE
                fechaUltimaRevision.visibility = View.VISIBLE
                txtUltimoKM.visibility = View.VISIBLE
                idUltimoKM.visibility = View.VISIBLE
                btn_borrartxtChe2.visibility = View.VISIBLE
            } else {
                N_Economico.visibility = View.GONE
                id_NEconomicoChequeo.visibility = View.GONE
                btnB_Neco_chequeo.visibility = View.GONE
                areanombre.visibility = View.GONE
                id_area_nombreChequeo.visibility = View.GONE
                t_deauto.visibility = View.GONE
                tipo_vehiculoChequeo.visibility = View.GONE
                id_auto.visibility = View.GONE
                T_vehiculoChequeo.visibility = View.GONE
                t_marca.visibility = View.GONE
                id_marcaChequeo.visibility = View.GONE
                t_submarca.visibility = View.GONE
                id_submarcaChequeo.visibility = View.GONE
                t_placas.visibility = View.GONE
                id_placasChequeo.visibility = View.GONE
                txtultimafecha_rev.visibility = View.GONE
                fechaUltimaRevision.visibility = View.GONE
                txtUltimoKM.visibility = View.GONE
                idUltimoKM.visibility = View.GONE
                btn_borrartxtChe2.visibility = View.GONE
            }
        }
        N_Economico.visibility = View.GONE
        id_NEconomicoChequeo.visibility = View.GONE
        btnB_Neco_chequeo.visibility = View.GONE
        areanombre.visibility = View.GONE
        id_area_nombreChequeo.visibility = View.GONE
        t_deauto.visibility = View.GONE
        tipo_vehiculoChequeo.visibility = View.GONE
        id_auto.visibility = View.GONE
        T_vehiculoChequeo.visibility = View.GONE
        t_marca.visibility = View.GONE
        id_marcaChequeo.visibility = View.GONE
        t_submarca.visibility = View.GONE
        id_submarcaChequeo.visibility = View.GONE
        t_placas.visibility = View.GONE
        id_placasChequeo.visibility = View.GONE
        txtultimafecha_rev.visibility = View.GONE
        fechaUltimaRevision.visibility = View.GONE
        txtUltimoKM.visibility = View.GONE
        idUltimoKM.visibility = View.GONE
        btn_borrartxtChe2.visibility = View.GONE

        checkInfoChequeo.setOnClickListener {
                if (checkInfoChequeo.isChecked){
                    txtTipoRevision.visibility = View.VISIBLE
                    rd_km.visibility = View.VISIBLE
                    rd_fecha.visibility = View.VISIBLE
                    rd_horasTrabajo.visibility = View.VISIBLE
                    rd_otroRevision.visibility = View.VISIBLE
                    //id_otroTipoRevision.visibility = View.VISIBLE
                    txtTipoMantenimiento.visibility = View.VISIBLE
                    rd_Preventivo.visibility = View.VISIBLE
                    rd_Correctivo.visibility = View.VISIBLE
                    rd_otroMant.visibility = View.VISIBLE
                    //id_otroTipoMantenimiento.visibility = View.VISIBLE
                    txtkmActual.visibility = View.VISIBLE
                    id_kmActual.visibility = View.VISIBLE
                    //txtProximafecha.visibility = View.VISIBLE
                    //idProximafecha.visibility = View.VISIBLE
                    //b_proximafecha.visibility = View.VISIBLE
                    txtProximoKM.visibility = View.VISIBLE
                    id_kmProximo.visibility = View.VISIBLE
                    txtObservaciones.visibility = View.VISIBLE
                    id_Observaciones.visibility = View.VISIBLE
                }else{
                    txtTipoRevision.visibility = View.GONE
                    rd_km.visibility = View.GONE
                    rd_fecha.visibility = View.GONE
                    rd_horasTrabajo.visibility = View.GONE
                    rd_otroRevision.visibility = View.GONE
                    //id_otroTipoRevision.visibility = View.GONE
                    txtTipoMantenimiento.visibility = View.GONE
                    rd_Preventivo.visibility = View.GONE
                    rd_Correctivo.visibility = View.GONE
                    rd_otroMant.visibility = View.GONE
                    //id_otroTipoMantenimiento.visibility = View.GONE
                    txtkmActual.visibility = View.GONE
                    id_kmActual.visibility = View.GONE
                    //txtProximafecha.visibility = View.GONE
                    //idProximafecha.visibility = View.GONE
                    //b_proximafecha.visibility = View.GONE
                    txtProximoKM.visibility = View.GONE
                    id_kmProximo.visibility = View.GONE
                    txtObservaciones.visibility = View.GONE
                    id_Observaciones.visibility = View.GONE
                }
            }
        txtTipoRevision.visibility = View.GONE
        rd_km.visibility = View.GONE
        rd_fecha.visibility = View.GONE
        rd_horasTrabajo.visibility = View.GONE
        rd_otroRevision.visibility = View.GONE
        id_otroTipoRevision.visibility = View.GONE
        txtTipoMantenimiento.visibility = View.GONE
        rd_Preventivo.visibility = View.GONE
        rd_Correctivo.visibility = View.GONE
        rd_otroMant.visibility = View.GONE
        id_otroTipoMantenimiento.visibility = View.GONE
        txtkmActual.visibility = View.GONE
        id_kmActual.visibility = View.GONE
        txtProximafecha.visibility = View.GONE
        idProximafecha.visibility = View.GONE
        b_proximafecha.visibility = View.GONE
        txtProximoKM.visibility = View.GONE
        id_kmProximo.visibility = View.GONE
        txtObservaciones.visibility = View.GONE
        id_Observaciones.visibility = View.GONE
    }
    fun bAutomovil(v:View){
        if (id_NEconomicoChequeo.text.toString().isEmpty() && id_placasChequeo.text.toString().isEmpty()){
            Toast.makeText(this,"Debes de llenar la info. para buscar un Automóvil",Toast.LENGTH_LONG).show()
        }
        when(id_NEconomicoChequeo.text.toString().isNotEmpty() && id_placasChequeo.text.toString().isEmpty()){
            true ->{
               bNumEco()
            }
        }
        when(id_NEconomicoChequeo.text.toString().isEmpty() && id_placasChequeo.text.toString().isNotEmpty()){
            true ->{
                bPlacas()
            }
        }
    }
    fun bNumEco(){
        var query: String = ""
        numeroEconomico = id_NEconomicoChequeo.text.toString()
        query =
            "Select c.id_carro,c.id_direccion,c.id_departamento,c.id_vehiculo,c.id_persona," +
                    "c.marca,c.submarca,c.modelo,c.serie,c.motor,c.placas,c.inventario,v.tipovehiculo,p.nombre,dep.departamento,d.direccion,c.subtipo " +
                    "from carro as c inner join direccion as d on c.id_direccion=d.id_direccion " +
                    "inner join departamento as dep on dep.id_departamento = c.id_departamento" +
                    " inner join  tipovehiculo as v on v.id_vehiculo=c.id_vehiculo " +
                    "inner join persona as p on p.id_persona = c.id_persona where c.inventario = '$numeroEconomico'"
        var admin = DataBase(this)
        var cur = admin.Consulta(query)
        if (cur == null) {
            admin.close()
            Toast.makeText(this, "Error de Capa 8", Toast.LENGTH_SHORT).show();
            Nomina_id_Chequeo.requestFocus()
        } else {
            if (cur.moveToFirst()) {
                Toast.makeText(this, "Se encontro el vehiculo", Toast.LENGTH_SHORT).show()
                id_area_nombreChequeo.setText(cur.getString(15))//area donde pertenece
                tipo_vehiculoChequeo.setText(cur.getString(13))//usuario
                T_vehiculoChequeo.setText(cur.getString(12))//Tvehiculo
                id_marcaChequeo.setText(cur.getString(5))//marca
                id_submarcaChequeo.setText(cur.getString(16))//subtipo
                id_placasChequeo.setText(cur.getString(10))//placas
                id_carro = cur.getInt(0)//id_carro
                id_NEconomicoChequeo.isEnabled = false
                id_placasChequeo.isEnabled = false
                admin.close()
            } else {
                admin.close()
                Toast.makeText(this, "No se Encontro el Vehiculo", Toast.LENGTH_SHORT).show()
            }
        }
        var sentencia2: String
        numeroEconomico = id_NEconomicoChequeo.text.toString()
        sentencia2 = "Select fecha_actual, kilometraje_actual from chequeo where " +
                "id_carro = '$id_carro' and id_usuario='$idU' and fecha_actual = (select max(fecha_actual) from chequeo) "
        var admin2 = DataBase(this)
        var cur2 = admin.Consulta(sentencia2)
        if (cur2 == null) {
            admin2.close()
            Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show();
        } else {
            if (cur2.moveToFirst()) {
                Toast.makeText(this, "Se encontro antecedentes", Toast.LENGTH_SHORT).show()
                fechaUltimaRevision.setText(cur2.getString(0))
                idUltimoKM.setText(cur2.getString(1))
                admin2.close()
            } else {
                admin2.close()
                Toast.makeText(this, "No se encontraron registros previos", Toast.LENGTH_SHORT).show()
            }
        }
    }
    fun bPlacas(){
            var query: String = ""
            placas = id_placasChequeo.text.toString()
            query =
                "Select c.id_carro,c.id_direccion,c.id_departamento,c.id_vehiculo,c.id_persona," +
                        "c.marca,c.submarca,c.modelo,c.serie,c.motor,c.placas,c.inventario,v.tipovehiculo,p.nombre,dep.departamento,d.direccion,c.subtipo " +
                        "from carro as c inner join direccion as d on c.id_direccion=d.id_direccion " +
                        "inner join departamento as dep on dep.id_departamento = c.id_departamento" +
                        " inner join  tipovehiculo as v on v.id_vehiculo=c.id_vehiculo " +
                        "inner join persona as p on p.id_persona = c.id_persona where upper(c.placas) = upper('$placas')"
            val admin = DataBase(this)
            val cur = admin.Consulta(query)
            if (cur == null) {
                admin.close()
                Toast.makeText(this, "Error de Capa 8", Toast.LENGTH_SHORT).show();
                Nomina_id_Chequeo.requestFocus()
            } else {
                if (cur.moveToFirst()) {
                    Toast.makeText(this, "Se encontro el Automóvil", Toast.LENGTH_SHORT).show()
                    id_NEconomicoChequeo.setText(cur.getString(11))//inventario
                    id_area_nombreChequeo.setText(cur.getString(15))//area o direccion
                    tipo_vehiculoChequeo.setText(cur.getString(13))//usuario
                    T_vehiculoChequeo.setText(cur.getString(12))//tvehiculo
                    id_marcaChequeo.setText(cur.getString(5))//marca
                    id_submarcaChequeo.setText(cur.getString(16))//subtipo
                    id_placasChequeo.setText(cur.getString(10))//placas
                    id_carro = cur.getInt(0)//id_carro
                    id_NEconomicoChequeo.isEnabled = false
                    id_placasChequeo.isEnabled = false
                    admin.close()
                } else {
                    admin.close()
                    Toast.makeText(this, "No se Encontro el No se encontro el Automóvil", Toast.LENGTH_SHORT).show()
                }
            }
            var sentencia2: String
            numeroEconomico = id_NEconomicoChequeo.text.toString()
            sentencia2 = "Select fecha_actual, kilometraje_actual from chequeo where id_carro = '$id_carro' and id_usuario='$idU' " +
                    "and fecha_actual = (select max(fecha_actual) from chequeo) "
            var admin2 = DataBase(this)
            var cur2 = admin.Consulta(sentencia2)
            if (cur2 == null) {
                admin2.close()
                Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show();
            } else {
                if (cur2.moveToFirst()) {
                    Toast.makeText(this, "Se encontro antecedentes", Toast.LENGTH_SHORT).show()
                    fechaUltimaRevision.setText(cur2.getString(0))
                    idUltimoKM.setText(cur2.getString(1))
                    admin2.close()
                } else {
                    admin2.close()
                    Toast.makeText(this, "No se encontraron registros previos", Toast.LENGTH_SHORT).show()
                }
            }
    }
    //buscar al empleado
    fun bEmpleado(v:View){
        if (etNombreNom_chequeo.text.toString().isEmpty() && Nomina_id_Chequeo.text.toString().isEmpty()){
            Toast.makeText(this,"Debes de llenar la información.",Toast.LENGTH_LONG).show()
        }
        when(etNombreNom_chequeo.text.toString().isNotEmpty() && idSwitch_chequeo.isChecked && Nomina_id_Chequeo.text.toString().isEmpty() && Rol.equals("2")){
            true ->{
                Toast.makeText(this,"No puedes agregar una nueva nómina",Toast.LENGTH_LONG).show()
            }
        }
        //guardar un empleado
        when(etNombreNom_chequeo.text.toString().isNotEmpty() && idSwitch_chequeo.isChecked && Nomina_id_Chequeo.text.toString().isEmpty() && Rol.equals("1")){
            true ->{
                AgregarNom()
                bporNombre()
            }
        }
        //Buscar por nomina
        when(Nomina_id_Chequeo.text.toString().isNotEmpty() && areaNom_chequeo.text.toString().isEmpty() && !idSwitch_chequeo.isChecked){
            true ->{
                bPorNomina()
            }
        }
        //buscar por nombre
        when(etNombreNom_chequeo.text.toString().isNotEmpty() && areaNom_chequeo.text.toString().isEmpty()){
            true ->{
                bporNombre()
            }
        }
    }
    fun AgregarNom(){
        var sentencia: String = ""
        var sentencia2: String = ""
        nombreEmp = etNombreNom_chequeo.text.toString()
        val timeStamp: String = SimpleDateFormat("ddHHmmss").format(Date())
        Tnomina
        nomina = "EN"+timeStamp
        area = "99999"
        var direccion = "11111"
        sentencia =
            "Insert into personaNuevo(id_persona,num_nomina,nombre,tiponomina,id_direccion,id_departamento,AgregadoP) values" +
                    "('$timeStamp','$nomina','$nombreEmp','$Tnomina','$area','$direccion','$idU')"
        sentencia2 =
            "Insert into persona(id_persona,num_nomina,nombre,tiponomina,id_direccion,id_departamento) values" +
                    "('$timeStamp','$nomina','$nombreEmp','$Tnomina','$area','$direccion')"
        val admin = DataBase(this)
        if (admin.Ejecuta(sentencia) &&admin.Ejecuta(sentencia2)) {
            admin.close()
            Toast.makeText(this, "Se guardo el empleado", Toast.LENGTH_LONG).show();
        } else {
            admin.close()
            Toast.makeText(this, "Error usuario ya existe", Toast.LENGTH_LONG).show();
            etNombreNom_chequeo.requestFocus()
        }
    }
    fun bPorNomina(){
        var query4: String = ""
        nomina = Nomina_id_Chequeo.text.toString()
        Tnomina
        query4 =
            "Select p.id_persona,p.num_nomina,p.nombre,p.tiponomina,p.id_direccion,p.id_departamento,d.direccion,dp.departamento " +
                    "from persona as p inner join direccion as d on p.id_direccion=d.id_direccion inner join departamento as dp on dp.id_departamento=p.id_departamento where p.num_nomina ='$nomina' and p.tiponomina ='$Tnomina'"
        val admin = DataBase(this)
        val cur = admin.Consulta(query4)
        if (cur == null) {
            admin.close()
            Toast.makeText(this, "Error de Capa 8", Toast.LENGTH_SHORT).show();
            Nomina_id_Chequeo.requestFocus()
        } else {
            if (cur.moveToFirst()) {
                Toast.makeText(this, "Se encontro el empleado", Toast.LENGTH_SHORT).show()
                etNombreNom_chequeo.setText(cur.getString(2))
                areaNom_chequeo.setText(cur.getString(6))
                id_persona = cur.getInt(0)//id_persona
                etNombreNom_chequeo.isEnabled = false
                id_spinnerTipoNominaChequeo.isEnabled = false
                Nomina_id_Chequeo.isEnabled = false
                idSwitch_chequeo.isEnabled = false
                admin.close()
            } else {
                admin.close()
                Toast.makeText(this, "No se Encontro el empleado", Toast.LENGTH_SHORT).show()
            }
        }
    }
    fun bporNombre(){
        var query: String = ""
        nombreEmp = etNombreNom_chequeo.text.toString()
        Tnomina
        query = "Select p.id_persona,p.num_nomina,p.nombre,p.tiponomina,p.id_direccion,p.id_departamento,d.direccion,dp.departamento " +
                "from persona as p inner join direccion as d on p.id_direccion=d.id_direccion inner join departamento as dp on dp.id_departamento=p.id_departamento where upper(p.nombre) =upper('$nombreEmp') and p.tiponomina ='$Tnomina'"
        val admin = DataBase(this)
        val cur = admin.Consulta(query)
        if (cur == null) {
            admin.close()
            Toast.makeText(this, "Error de Capa 8", Toast.LENGTH_SHORT).show()
            Nomina_id_Chequeo.requestFocus()
        } else {
            if (cur.moveToFirst()) {
                Toast.makeText(this, "Se encontro el empleado", Toast.LENGTH_SHORT).show()
                Nomina_id_Chequeo.setText(cur.getString(1))//nomina
                areaNom_chequeo.setText(cur.getString(6))//direccion
                id_persona = cur.getInt(0)//id_persona
                etNombreNom_chequeo.setText(cur.getString(2))//nombre
                etNombreNom_chequeo.isEnabled = false
                id_spinnerTipoNominaChequeo.isEnabled = false
                Nomina_id_Chequeo.isEnabled = false
                idSwitch_chequeo.isEnabled = false
                admin.close()
            } else {
                Toast.makeText(this, "No se Encontro el empleado", Toast.LENGTH_SHORT).show()
            }
        }
    }

    fun regresarMenu(v: View){
        val audimenu = Intent(this,menuActivity::class.java)
        startActivity(audimenu)
        finish()
    }

    fun tipoRevision(view: View) {
        if (view is RadioButton) {
            val checked = view.isChecked
            when (view.getId()) {
                R.id.rd_km ->
                    if (checked) {
                        Toast.makeText(this, "Por kilometraje", Toast.LENGTH_SHORT).show()
                        id_otroTipoRevision.visibility = View.GONE
                        txtProximafecha.visibility = View.GONE
                        idProximafecha.visibility = View.GONE
                        b_proximafecha.visibility = View.GONE
                        txtProximoKM.visibility = View.VISIBLE
                        id_kmProximo.visibility = View.VISIBLE
                    }
                R.id.rd_fecha ->
                    if (checked) {
                        Toast.makeText(this, "Por Fecha", Toast.LENGTH_SHORT).show()
                        id_otroTipoRevision.visibility = View.GONE
                        txtProximafecha.visibility = View.VISIBLE
                        idProximafecha.visibility = View.VISIBLE
                        b_proximafecha.visibility = View.VISIBLE
                        txtProximoKM.visibility = View.GONE
                        id_kmProximo.visibility = View.GONE
                    }
                R.id.rd_horasTrabajo ->
                    if (checked) {
                        Toast.makeText(this, "Dependiendo de las horas de trabajo", Toast.LENGTH_SHORT).show()
                        id_otroTipoRevision.visibility = View.GONE
                        txtProximafecha.visibility = View.GONE
                        idProximafecha.visibility = View.GONE
                        b_proximafecha.visibility = View.GONE
                        txtProximoKM.visibility = View.GONE
                        id_kmProximo.visibility = View.GONE
                    }
                R.id.rd_otroRevision ->
                    if (checked) {
                        Toast.makeText(this, "Por favor de apuntar abajo en la caja el tipo de revisión", Toast.LENGTH_SHORT).show()
                        id_otroTipoRevision.visibility = View.VISIBLE
                    }
            }
        }
    }
    fun tipoMantenimiento(view: View) {
        if (view is RadioButton) {
            val checked = view.isChecked
            when (view.getId()) {
                R.id.rd_Preventivo ->
                    if (checked) {
                        Toast.makeText(this, "Tipo Preventivo", Toast.LENGTH_SHORT).show()
                        id_otroTipoMantenimiento.visibility = View.GONE
                    }
                R.id.rd_Correctivo ->
                    if (checked) {
                        Toast.makeText(this, "Tipo Correctivo", Toast.LENGTH_SHORT).show()
                        id_otroTipoMantenimiento.visibility = View.GONE
                    }
                R.id.rd_otroMant ->
                    if (checked) {
                        Toast.makeText(this, "Por favor de apuntar abajo en la caja el tipo de Mantenimiento", Toast.LENGTH_SHORT).show()
                        id_otroTipoMantenimiento.visibility = View.VISIBLE
                    }
            }
        }
    }
    fun guardar(v: View){
        if (id_kmActual.text.toString().isNotEmpty() && id_carro !=0 && id_persona != 0){
            if (rd_Correctivo.isChecked){
                tipo_mantenimiento = "Tipo Correctivo"
            }
            if (rd_Preventivo.isChecked){
                tipo_mantenimiento = "Tipo Preventivo"
            }
            if (rd_otroMant.isChecked){
                tipo_mantenimiento = id_otroTipoMantenimiento.text.toString()
            }

            if (rd_fecha.isChecked){
                tipo_revision = "Por fecha"
                proximaRev = "La proxima fecha sera el "+idProximafecha.text.toString()
            }
            if (rd_horasTrabajo.isChecked){
                tipo_revision = "Dependiendo de las horas de trabajo"
                proximaRev = "Dependiendo de las horas de trabajo"
            }
            if (rd_km.isChecked){
                tipo_revision = "Por Kilometraje"
                proximaRev = "Por km a los "+id_kmProximo.text.toString()
            }
            if (rd_otroRevision.isChecked){
                tipo_revision = id_otroTipoRevision.text.toString()
                proximaRev = id_otroTipoRevision.text.toString()
            }

            if (idUltimoKM.text.toString().isEmpty()){
                km_anterior = "0"
            }else{
                km_anterior = idUltimoKM.text.toString()
            }
            if (fechaUltimaRevision.text.toString().isEmpty()){
                fechaAnterior = "Primera Revision"
            }else{
                fechaAnterior = fechaUltimaRevision.text.toString()
            }

            c_chequeo = "CH"+fech
            km_actual = id_kmActual.text.toString()
            nomina = Nomina_id_Chequeo.text.toString()
            numeroEconomico = id_NEconomicoChequeo.text.toString()
            observaciones = id_Observaciones.text.toString()

            val sentencia = "Insert into chequeo(c_chequeo,id_persona,id_carro,id_usuario,tipo_revision,tipo_mantenimiento," +
                    "kilometraje_actual,fecha_actual,kilometraje_anterior,fecha_anterior,proxima_revision,observaciones)" +
                    "values('$c_chequeo','$id_persona','$id_carro','$idU','$tipo_revision','$tipo_mantenimiento','$km_actual'," +
                    "'$fechaActual','$km_anterior','$fechaAnterior','$proximaRev','$observaciones')"
            val admin = DataBase(this)
            if (admin.Ejecuta(sentencia)) {
                admin.close()
                Toast.makeText(this, "Se guardo el Chequeo del Automóvil.", Toast.LENGTH_LONG).show()
                val audiG = Intent(this, menuActivity::class.java)
                startActivity(audiG)
                finish()
            } else {
                admin.close()
                Toast.makeText(this, "Error no se pudo guardar.", Toast.LENGTH_LONG).show()
                etNombreNom_chequeo.requestFocus()
            }
        }else{
            Toast.makeText(this,"Debes de llenar todos los datos.",Toast.LENGTH_LONG).show()
        }
    }
}