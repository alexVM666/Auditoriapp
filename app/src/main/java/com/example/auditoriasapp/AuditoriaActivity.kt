package com.example.auditoriasapp

import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.core.content.FileProvider
import com.example.auditoriasapp.Database.DataBase
import kotlinx.android.synthetic.main.activity_auditoria.*
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*


class AuditoriaActivity : AppCompatActivity() {
    private lateinit var idU: String
    private lateinit var nomU: String
    private lateinit var nombreUsuario:String
    private lateinit var Rol:String

    var id_persona:Int = 0
    var Tnomina: String = ""
    var nomina : String=""
    var nombreEmp : String=""
    var area : String=""
    var fech: String=""
    var fechaAudi: String=""

    var numeroEconomico: String = ""
    var placas: String = ""
    var id_carro:Int = 0

    var claveAudi: String = ""
    var tipoAudi: String = "Individual"
    var caracteristicas: String = ""
    var Hmotor: String = ""
    var HCarroceria: String = ""
    var HInterior: String = ""
    var Emergencia: String = ""
    var policiaco: String = ""
    var NoConfor: String = ""
    var Conclusiones: String = ""
    var llantas: String = ""
    var cinturones: String = ""
    var bolsas: String = ""
    var testigos: String = ""

    lateinit var photoPath : String
    val REQUEST_TAKE_PHOTO = 1
    val TOMARFOTOADITAMENTOS = 2
    val TOMARFOTOPOLICIACOS = 3
    val TOMARFOTOINTERIOR = 4
    val TOMARFOTOCARROCERIA = 5
    val TOMARFOTONC = 6
    val TOMARFOTOLLANTAS = 7
    val TOMARFOTOCINTURONES = 8
    val TOMARFOTOBOLSAS = 9
    val TOMARFOTOTESTIGOS = 10

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_auditoria)
        val audiI = intent
        if(audiI != null && audiI.hasExtra("id_usuario") && audiI.hasExtra("usuario")){
            idU = audiI.getStringExtra("id_usuario")
            nomU = audiI.getStringExtra("usuario")
            nombreUsuario = audiI.getStringExtra("nomusuario")
            Rol = audiI.getStringExtra("id_rol")
            nombre.text = "$nombreUsuario"
        }
        //Spinner Tipo de Nomina
        val tipoNom = arrayOf("Catorcenal","Quincenal")
        val arrayAdapter = ArrayAdapter(this,R.layout.spinner_item_new,tipoNom)
        id_spinnerTipoNomina.adapter = arrayAdapter
        id_spinnerTipoNomina.onItemSelectedListener = object :
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
        txFechaAud.setText(strDate)
        val timeStamp: String = SimpleDateFormat("yyyyMMddHHmmss").format(Date())
        fech = timeStamp

        btn_borrartxt1.setOnClickListener {
            etNombreNom.isEnabled = true
            id_spinnerTipoNomina.isEnabled = true
            Nomina_id.isEnabled = true
            idSwitch.isEnabled = true
            Nomina_id.setText("")
            etNombreNom.setText("")
            areaNom.setText("")
            Nomina_id.requestFocus()
        }
        btn_borrartxt2.setOnClickListener {
            id_NEconomico.isEnabled = true
            id_placas.isEnabled =  true
            id_placas.setText("")
            id_NEconomico.setText("")
            id_area_nombre.setText("")
            tipo_vehiculo.setText("")
            T_vehiculo.setText("")
            id_marca.setText("")
            id_submarca.setText("")
            id_NEconomico.requestFocus()
        }
        checkPoliciaco.setOnClickListener {
            if (checkPoliciaco.isChecked){
                id_eTactico.visibility = View.GONE
                btnEvidenciaPoliciaco.visibility = View.GONE
                imPoliciaco.visibility = View.GONE
                txEvicenciaPoliciaco.visibility = View.GONE
                imagenPoliciacoEvidencia.visibility = View.GONE
            }
                else{
                id_eTactico.visibility = View.VISIBLE
                btnEvidenciaPoliciaco.visibility = View.VISIBLE
                imPoliciaco.visibility = View.VISIBLE
                txEvicenciaPoliciaco.visibility = View.VISIBLE
                imagenPoliciacoEvidencia.visibility = View.VISIBLE
            }
        }
        checkEmergencia.setOnClickListener {
            if(checkEmergencia.isChecked){
                id_emergencia.visibility = View.GONE
                btnEvidenciaAditamentos.visibility = View.GONE
                imEmergencia.visibility = View.GONE
                txEvicenciaAditamentos.visibility = View.GONE
                imagenAditamentosEvidencia.visibility = View.GONE
            }else{
                id_emergencia.visibility = View.VISIBLE
                btnEvidenciaAditamentos.visibility = View.VISIBLE
                imEmergencia.visibility = View.VISIBLE
                txEvicenciaAditamentos.visibility = View.VISIBLE
                imagenAditamentosEvidencia.visibility = View.VISIBLE
            }
        }

        btnEvidenciaMotor.setOnClickListener {
            if (tipo_vehiculo.text.toString().isEmpty()){
                Toast.makeText(this, "debes de tener un auto a auditar", Toast.LENGTH_SHORT).show()
                id_NEconomico.requestFocus()
            }else
            {
                takePicture()
            }
        }
        btnEvidenciaAditamentos.setOnClickListener {
            if (tipo_vehiculo.text.toString().isEmpty()){
                Toast.makeText(this, "debes de tener un auto a auditar", Toast.LENGTH_SHORT).show()
                id_NEconomico.requestFocus()
            }else
            {
                tomarFotoAditamentos()
            }
        }
        btnEvidenciaCarroceria.setOnClickListener {
            if (tipo_vehiculo.text.toString().isEmpty()){
                Toast.makeText(this, "debes de tener un auto a auditar", Toast.LENGTH_SHORT).show()
                id_NEconomico.requestFocus()
            }else
            {
                tomarFotoCarroceria()
            }
        }
        btnEvidenciaNoConformidades.setOnClickListener {
            if (tipo_vehiculo.text.toString().isEmpty()){
                Toast.makeText(this, "debes de tener un auto a auditar", Toast.LENGTH_SHORT).show()
                id_NEconomico.requestFocus()
            }else
            {
                tomarFotoNC()
            }
        }
        btnEvidenciaInterior.setOnClickListener {
            if (tipo_vehiculo.text.toString().isEmpty()){
                Toast.makeText(this, "debes de tener un auto a auditar", Toast.LENGTH_SHORT).show()
                id_NEconomico.requestFocus()
            }else
            {
                tomarFotoInterior()
            }
        }
        btnEvidenciaPoliciaco.setOnClickListener {
            if (tipo_vehiculo.text.toString().isEmpty()){
                Toast.makeText(this, "debes de tener un auto a auditar", Toast.LENGTH_SHORT).show()
                id_NEconomico.requestFocus()
            }else
            {
                tomarFotoPoliciacos()
            }
        }
        btnEvidenciaLlantas.setOnClickListener {
            if (tipo_vehiculo.text.toString().isEmpty()){
                Toast.makeText(this, "Debes de tener un vehículo a auditar",Toast.LENGTH_SHORT).show()
                id_NEconomico.requestFocus()
            }else{
                tomarFotoLLantas()
            }
        }

        btnEvidenciaCinturones.setOnClickListener {
            if (tipo_vehiculo.text.toString().isEmpty()){
                Toast.makeText(this, "Debes de tener un vehículo a auditar", Toast.LENGTH_SHORT).show()
                id_NEconomico.requestFocus()
            }else{
                tomarFotoCinturones()
            }
        }

        btnEvidenciaBolsasAire.setOnClickListener {
            if (tipo_vehiculo.text.toString().isEmpty()){
                Toast.makeText(this, "Debes de tener un vehículo a auditar", Toast.LENGTH_SHORT).show()
                id_NEconomico.requestFocus()
            }else{
                tomarFotoBolsas()
            }
        }

        btnEvidenciatestigosTablero.setOnClickListener {
            if (tipo_vehiculo.text.toString().isEmpty()){
                Toast.makeText(this, "Debes de tener un vehículo a auditar", Toast.LENGTH_SHORT).show()
                id_NEconomico.requestFocus()
            }else{
                tomarFotoTestigos()
            }
        }

        checkInfoNomina.setOnClickListener {
            if(checkInfoNomina.isChecked){
                textNomina.visibility = View.VISIBLE
                Nomina_id.visibility = View.VISIBLE
                b_nomina.visibility = View.VISIBLE
                txTipoNomina.visibility = View.VISIBLE
                id_spinnerTipoNomina.visibility = View.VISIBLE
                NomNomina.visibility = View.VISIBLE
                etNombreNom.visibility = View.VISIBLE
                t_area.visibility = View.VISIBLE
                areaNom.visibility = View.VISIBLE
                idSwitch.visibility = View.VISIBLE
                btn_borrartxt1.visibility = View.VISIBLE
            }else{
                textNomina.visibility = View.GONE
                Nomina_id.visibility = View.GONE
                b_nomina.visibility = View.GONE
                txTipoNomina.visibility = View.GONE
                id_spinnerTipoNomina.visibility = View.GONE
                NomNomina.visibility = View.GONE
                etNombreNom.visibility = View.GONE
                t_area.visibility = View.GONE
                areaNom.visibility = View.GONE
                idSwitch.visibility = View.GONE
                btn_borrartxt1.visibility = View.GONE
            }
        }
        //Nomina
        textNomina.visibility = View.GONE
        Nomina_id.visibility = View.GONE
        b_nomina.visibility = View.GONE
        txTipoNomina.visibility = View.GONE
        id_spinnerTipoNomina.visibility = View.GONE
        NomNomina.visibility = View.GONE
        etNombreNom.visibility = View.GONE
        t_area.visibility = View.GONE
        areaNom.visibility = View.GONE
        idSwitch.visibility = View.GONE
        btn_borrartxt1.visibility = View.GONE

        checkInfoVehiculo.setOnClickListener {
            if (checkInfoVehiculo.isChecked){
                N_Economico.visibility = View.VISIBLE
                id_NEconomico.visibility = View.VISIBLE
                btnB_NEco.visibility = View.VISIBLE
                areanombre.visibility = View.VISIBLE
                id_area_nombre.visibility = View.VISIBLE
                t_deauto.visibility = View.VISIBLE
                tipo_vehiculo.visibility = View.VISIBLE
                id_auto.visibility = View.VISIBLE
                T_vehiculo.visibility = View.VISIBLE
                t_marca.visibility = View.VISIBLE
                id_marca.visibility = View.VISIBLE
                t_submarca.visibility = View.VISIBLE
                id_submarca.visibility = View.VISIBLE
                t_placas.visibility = View.VISIBLE
                id_placas.visibility = View.VISIBLE
                btn_borrartxt2.visibility = View.VISIBLE
            }else{
                N_Economico.visibility = View.GONE
                id_NEconomico.visibility = View.GONE
                btnB_NEco.visibility = View.GONE
                areanombre.visibility = View.GONE
                id_area_nombre.visibility = View.GONE
                t_deauto.visibility = View.GONE
                tipo_vehiculo.visibility = View.GONE
                id_auto.visibility = View.GONE
                T_vehiculo.visibility = View.GONE
                t_marca.visibility = View.GONE
                id_marca.visibility = View.GONE
                t_submarca.visibility = View.GONE
                id_submarca.visibility = View.GONE
                t_placas.visibility = View.GONE
                id_placas.visibility = View.GONE
                btn_borrartxt2.visibility = View.GONE
            }
        }
        //Vehiculo
        N_Economico.visibility = View.GONE
        id_NEconomico.visibility = View.GONE
        btnB_NEco.visibility = View.GONE
        areanombre.visibility = View.GONE
        id_area_nombre.visibility = View.GONE
        t_deauto.visibility = View.GONE
        tipo_vehiculo.visibility = View.GONE
        id_auto.visibility = View.GONE
        T_vehiculo.visibility = View.GONE
        t_marca.visibility = View.GONE
        id_marca.visibility = View.GONE
        t_submarca.visibility = View.GONE
        id_submarca.visibility = View.GONE
        t_placas.visibility = View.GONE
        id_placas.visibility = View.GONE
        btn_borrartxt2.visibility = View.GONE
    }

    private fun tomarFotoAditamentos() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        if (intent.resolveActivity(packageManager) != null) {
            var photoFile: File? = null
            try {
                photoFile = createImageFile2()
            } catch (e: IOException) { }
            if (photoFile != null){
                val photoUri = FileProvider.getUriForFile(this,"com.example.android.fileprovider",photoFile)
                intent.putExtra(MediaStore.EXTRA_OUTPUT,photoUri)
                startActivityForResult(intent,TOMARFOTOADITAMENTOS)
            }
        }
    }
    private fun createImageFile2(): File? {
        val timeStamp: String = SimpleDateFormat("ddMMyyyyHHmmss").format(Date())
        val FileName = "ADITA"+timeStamp
        val storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        val image =  File.createTempFile(FileName,".jpg",storageDir)
        photoPath = image.absolutePath
        return image
    }
    private fun createImageFile(): File? {
        val timeStamp: String = SimpleDateFormat("ddMMyyyyHHmmss").format(Date())
        val FileName = "MOTOR"+timeStamp
        val storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        val image =  File.createTempFile(FileName,".jpg",storageDir)
        photoPath = image.absolutePath
        return image
    }
    //funcion para tomar foto
    private fun takePicture() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        if (intent.resolveActivity(packageManager) != null) {
            var photoFile: File? = null
            try {
                photoFile = createImageFile()
            } catch (e: IOException) { }
            if (photoFile != null){
                val photoUri = FileProvider.getUriForFile(this,"com.example.android.fileprovider",photoFile)
                intent.putExtra(MediaStore.EXTRA_OUTPUT,photoUri)
                startActivityForResult(intent,REQUEST_TAKE_PHOTO)
            }
        }
    }

    private fun tomarFotoPoliciacos() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        if (intent.resolveActivity(packageManager) != null) {
            var photoFile: File? = null
            try {
                photoFile = createImageFile3()
            } catch (e: IOException) { }
            if (photoFile != null){
                val photoUri = FileProvider.getUriForFile(this,"com.example.android.fileprovider",photoFile)
                intent.putExtra(MediaStore.EXTRA_OUTPUT,photoUri)
                startActivityForResult(intent,TOMARFOTOPOLICIACOS)
            }
        }
    }
    private fun createImageFile3(): File? {
        val timeStamp: String = SimpleDateFormat("ddMMyyyyHHmmss").format(Date())
        val FileName = "POLI"+timeStamp
        val storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        val image =  File.createTempFile(FileName,".jpg",storageDir)
        photoPath = image.absolutePath
        return image
    }

    private fun tomarFotoInterior() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        if (intent.resolveActivity(packageManager) != null) {
            var photoFile: File? = null
            try {
                photoFile = createImageFile4()
            } catch (e: IOException) { }
            if (photoFile != null){
                val photoUri = FileProvider.getUriForFile(this,"com.example.android.fileprovider",photoFile)
                intent.putExtra(MediaStore.EXTRA_OUTPUT,photoUri)
                startActivityForResult(intent,TOMARFOTOINTERIOR)
            }
        }
    }
    private fun createImageFile4(): File? {
        val timeStamp: String = SimpleDateFormat("ddMMyyyyHHmmss").format(Date())
        val FileName = "INT"+timeStamp
        val storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        val image =  File.createTempFile(FileName,".jpg",storageDir)
        photoPath = image.absolutePath
        return image
    }

    private fun tomarFotoNC() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        if (intent.resolveActivity(packageManager) != null) {
            var photoFile: File? = null
            try {
                photoFile = createImageFile5()
            } catch (e: IOException) { }
            if (photoFile != null){
                val photoUri = FileProvider.getUriForFile(this,"com.example.android.fileprovider",photoFile)
                intent.putExtra(MediaStore.EXTRA_OUTPUT,photoUri)
                startActivityForResult(intent,TOMARFOTONC)
            }
        }
    }
    private fun createImageFile5(): File? {
        val timeStamp: String = SimpleDateFormat("ddMMyyyyHHmmss").format(Date())
        val FileName = "NOCONF"+timeStamp
        val storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        val image =  File.createTempFile(FileName,".jpg",storageDir)
        photoPath = image.absolutePath
        return image
    }

    private fun tomarFotoCarroceria() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        if (intent.resolveActivity(packageManager) != null) {
            var photoFile: File? = null
            try {
                photoFile = createImageFile6()
            } catch (e: IOException) { }
            if (photoFile != null){
                val photoUri = FileProvider.getUriForFile(this,"com.example.android.fileprovider",photoFile)
                intent.putExtra(MediaStore.EXTRA_OUTPUT,photoUri)
                startActivityForResult(intent,TOMARFOTOCARROCERIA)
            }
        }
    }
    private fun createImageFile6(): File? {
        val timeStamp: String = SimpleDateFormat("ddMMyyyyHHmmss").format(Date())
        val FileName = "CARROC"+timeStamp
        val storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        val image =  File.createTempFile(FileName,".jpg",storageDir)
        photoPath = image.absolutePath
        return image
    }

    private fun tomarFotoLLantas() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        if (intent.resolveActivity(packageManager) != null) {
            var photoFile: File? = null
            try {
                photoFile = createImageFile7()
            } catch (e: IOException) { }
            if (photoFile != null){
                val photoUri = FileProvider.getUriForFile(this,"com.example.android.fileprovider",photoFile)
                intent.putExtra(MediaStore.EXTRA_OUTPUT,photoUri)
                startActivityForResult(intent,TOMARFOTOLLANTAS)
            }
        }
    }
    private fun createImageFile7(): File? {
        val timeStamp: String = SimpleDateFormat("ddMMyyyyHHmmss").format(Date())
        val FileName = "LLANTAS"+timeStamp
        val storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        val image =  File.createTempFile(FileName,".jpg",storageDir)
        photoPath = image.absolutePath
        return image
    }

    private fun tomarFotoCinturones() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        if (intent.resolveActivity(packageManager) != null) {
            var photoFile: File? = null
            try {
                photoFile = createImageFile8()
            } catch (e: IOException) { }
            if (photoFile != null){
                val photoUri = FileProvider.getUriForFile(this,"com.example.android.fileprovider",photoFile)
                intent.putExtra(MediaStore.EXTRA_OUTPUT,photoUri)
                startActivityForResult(intent,TOMARFOTOCINTURONES)
            }
        }
    }
    private fun createImageFile8(): File? {
        val timeStamp: String = SimpleDateFormat("ddMMyyyyHHmmss").format(Date())
        val FileName = "CINTURONES"+timeStamp
        val storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        val image =  File.createTempFile(FileName,".jpg",storageDir)
        photoPath = image.absolutePath
        return image
    }
    private fun tomarFotoBolsas() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        if (intent.resolveActivity(packageManager) != null) {
            var photoFile: File? = null
            try {
                photoFile = createImageFile9()
            } catch (e: IOException) { }
            if (photoFile != null){
                val photoUri = FileProvider.getUriForFile(this,"com.example.android.fileprovider",photoFile)
                intent.putExtra(MediaStore.EXTRA_OUTPUT,photoUri)
                startActivityForResult(intent,TOMARFOTOBOLSAS)
            }
        }
    }
    private fun createImageFile9(): File? {
        val timeStamp: String = SimpleDateFormat("ddMMyyyyHHmmss").format(Date())
        val FileName = "BOLSAS"+timeStamp
        val storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        val image =  File.createTempFile(FileName,".jpg",storageDir)
        photoPath = image.absolutePath
        return image
    }
    private fun tomarFotoTestigos() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        if (intent.resolveActivity(packageManager) != null) {
            var photoFile: File? = null
            try {
                photoFile = createImageFile10()
            } catch (e: IOException) { }
            if (photoFile != null){
                val photoUri = FileProvider.getUriForFile(this,"com.example.android.fileprovider",photoFile)
                intent.putExtra(MediaStore.EXTRA_OUTPUT,photoUri)
                startActivityForResult(intent,TOMARFOTOTESTIGOS)
            }
        }
    }
    private fun createImageFile10(): File? {
        val timeStamp: String = SimpleDateFormat("ddMMyyyyHHmmss").format(Date())
        val FileName = "TESTIGOS"+timeStamp
        val storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        val image =  File.createTempFile(FileName,".jpg",storageDir)
        photoPath = image.absolutePath
        return image
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_TAKE_PHOTO && resultCode == Activity.RESULT_OK){
            imagenMotorEvidencia.setImageURI(Uri.parse(photoPath))
        }
        if (requestCode == TOMARFOTOADITAMENTOS && resultCode == Activity.RESULT_OK){
            imagenAditamentosEvidencia.setImageURI(Uri.parse(photoPath))
        }
        if (requestCode == TOMARFOTOCARROCERIA && resultCode == Activity.RESULT_OK){
            imagenCarroceria.setImageURI(Uri.parse(photoPath))
        }
        if (requestCode == TOMARFOTONC && resultCode == Activity.RESULT_OK){
            imagenNoConformidadesEvidencia.setImageURI(Uri.parse(photoPath))
        }
        if (requestCode == TOMARFOTOPOLICIACOS && resultCode == Activity.RESULT_OK){
            imagenPoliciacoEvidencia.setImageURI(Uri.parse(photoPath))
        }
        if (requestCode == TOMARFOTOINTERIOR && resultCode == Activity.RESULT_OK){
            imagenInteriorEvidencia.setImageURI(Uri.parse(photoPath))
        }
        if (requestCode == TOMARFOTOLLANTAS && resultCode == Activity.RESULT_OK){
            imagenLlantasEvidencia.setImageURI(Uri.parse(photoPath))
        }
        if (requestCode == TOMARFOTOCINTURONES && resultCode == Activity.RESULT_OK){
            imagenCinturonesEvidencia.setImageURI(Uri.parse(photoPath))
        }
        if (requestCode == TOMARFOTOBOLSAS && resultCode == Activity.RESULT_OK){
            imagenBolsasAireEvidencia.setImageURI(Uri.parse(photoPath))
        }
        if (requestCode == TOMARFOTOTESTIGOS && resultCode == Activity.RESULT_OK){
            imagentestigosTableroEvidencia.setImageURI(Uri.parse(photoPath))
        }
    }
    fun bAuto(v:View){
        if (id_NEconomico.text.toString().isEmpty() && id_placas.text.toString().isEmpty()){
            Toast.makeText(this,"Debes de llenar toda la información.",Toast.LENGTH_LONG).show()
        }
        //buscar automovil por numero economico
        when(id_NEconomico.text.toString().isNotEmpty() && id_placas.text.toString().isEmpty()){
            true ->{
                bNumeroEco()
            }
        }
        when (id_NEconomico.text.toString().isEmpty() && id_placas.text.toString().isNotEmpty()){
            true->{
                bPlacas()
            }
        }
    }

    fun bNumeroEco(){
        var query: String = ""
        numeroEconomico = id_NEconomico.text.toString()
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
            Toast.makeText(this, "Error de Capa 8", Toast.LENGTH_SHORT).show()
        } else {
            if (cur.moveToFirst()) {
                Toast.makeText(this, "Se encontro el vehiculo", Toast.LENGTH_SHORT).show()
                id_area_nombre.setText(cur.getString(15)) //area donde pertenece
                tipo_vehiculo.setText(cur.getString(13))//usuario del vehiculo
                T_vehiculo.setText(cur.getString(12))//Tvehiculo
                id_marca.setText(cur.getString(5))//marca
                id_submarca.setText(cur.getString(16))//subTipo
                id_placas.setText(cur.getString(10))//placas
                id_carro = cur.getInt(0)//id_carro
                id_NEconomico.isEnabled = false
                id_placas.isEnabled = false
                admin.close()
            } else {
                admin.close()
                Toast.makeText(this, "No se Encontro el Vehiculo", Toast.LENGTH_SHORT).show()
            }
        }
    }

    fun bPlacas(){
            var query2: String = ""
            placas = id_placas.text.toString()
            query2 =
                "Select c.id_carro,c.id_direccion,c.id_departamento,c.id_vehiculo,c.id_persona," +
                        "c.marca,c.submarca,c.modelo,c.serie,c.motor,c.placas,c.inventario,v.tipovehiculo,p.nombre,dep.departamento,d.direccion,c.subtipo " +
                        "from carro as c inner join direccion as d on c.id_direccion=d.id_direccion " +
                        "inner join departamento as dep on dep.id_departamento = c.id_departamento" +
                        " inner join  tipovehiculo as v on v.id_vehiculo=c.id_vehiculo " +
                        "inner join persona as p on p.id_persona = c.id_persona where upper(c.placas) = upper('$placas')"
            var admin = DataBase(this)
            var cur = admin.Consulta(query2)
            if (cur == null) {
                admin.close()
                Toast.makeText(this, "Error de Capa 8", Toast.LENGTH_SHORT).show()
            } else {
                if (cur.moveToFirst()) {
                    Toast.makeText(this, "Se encontro el Automóvil", Toast.LENGTH_SHORT).show()
                    id_NEconomico.setText(cur.getString(11))//inventario
                    id_area_nombre.setText(cur.getString(15))//area o direccion
                    tipo_vehiculo.setText(cur.getString(13))//usuario
                    T_vehiculo.setText(cur.getString(12))//t vehiculo
                    id_marca.setText(cur.getString(5))//marca
                    id_submarca.setText(cur.getString(16))//subtipo
                    id_placas.setText(cur.getString(10))//placas
                    id_carro = cur.getInt(0)//id_carro
                    id_NEconomico.isEnabled = false
                    id_placas.isEnabled = false
                    admin.close()
                } else {
                    admin.close()
                    Toast.makeText(this, "No se Encontro el Automóvil", Toast.LENGTH_SHORT).show()
                }
            }
    }
    fun BEmpleado(v:View){
        if(etNombreNom.text.toString().isNotEmpty() && Nomina_id.text.toString().isNotEmpty()){
            Toast.makeText(this,"Se debe de llenar toda la información.",Toast.LENGTH_LONG).show()
        }
        when(etNombreNom.text.toString().isNotEmpty() && idSwitch.isChecked && Nomina_id.text.toString().isEmpty() && Rol.equals("2")){
            true ->{
                Toast.makeText(this,"No se tiene permiso para insertar nuevos usuarios",Toast.LENGTH_LONG).show()
            }
        }
        //insertar empleado
        when(etNombreNom.text.toString().isNotEmpty() && idSwitch.isChecked && Nomina_id.text.toString().isEmpty() && Rol.equals("1")){
            true ->{
                AgregarEmp()
                BporNombre()
            }
        }
        //buscar por nomina
        when(Nomina_id.text.toString().isNotEmpty() && areaNom.text.toString().isEmpty()){
            true ->{
                BporNomina()
            }
        }
        //buscar por nombre
        when(etNombreNom.text.toString().isNotEmpty() && areaNom.text.toString().isEmpty() && !idSwitch.isChecked){
            true ->{
                BporNombre()
            }
        }
    }
    fun AgregarEmp(){
        var sentencia: String = ""
        var sentencia2: String = ""
        nombreEmp = etNombreNom.text.toString()
        val timeStamp: String = SimpleDateFormat("ddHHmmss").format(Date())
        Tnomina
        nomina = "EN"+timeStamp
        area = "99999"
        var direccion = "11111"
        sentencia =
            "Insert into personaNuevo(id_persona,num_nomina,nombre,tiponomina,id_direccion,id_departamento,AgregadoP) values " +
                    "('$timeStamp','$nomina','$nombreEmp','$Tnomina','$area','$direccion','$idU')"
        sentencia2 =
            "Insert into persona(id_persona,num_nomina,nombre,tiponomina,id_direccion,id_departamento) values " +
                    "('$timeStamp','$nomina','$nombreEmp','$Tnomina','$area','$direccion')"
        val admin = DataBase(this)
        if (admin.Ejecuta(sentencia) &&admin.Ejecuta(sentencia2)) {
            admin.close()
            Toast.makeText(this, "Se guardo el empleado", Toast.LENGTH_LONG).show()
        } else {
            admin.close()
            Toast.makeText(this, "Error usuario ya existe", Toast.LENGTH_LONG).show()
        }
    }
    fun BporNomina(){
        var query3: String = ""
        nomina = Nomina_id.text.toString()
        Tnomina
        query3 =
            "Select p.id_persona,p.num_nomina,p.nombre,p.tiponomina,p.id_direccion,p.id_departamento,d.direccion,dp.departamento " +
                    "from persona as p inner join direccion as d on p.id_direccion=d.id_direccion inner join departamento as dp on dp.id_departamento=p.id_departamento where p.num_nomina ='$nomina' and p.tiponomina ='$Tnomina'"
        var admin = DataBase(this)
        var cur = admin.Consulta(query3)
        if (cur == null) {
            admin.close()
            Toast.makeText(this, "Error de Capa 8", Toast.LENGTH_SHORT).show();
        } else {
            if (cur.moveToFirst()) {
                Toast.makeText(this, "Se encontro el empleado", Toast.LENGTH_SHORT).show()
                etNombreNom.setText(cur.getString(2))//nombre
                areaNom.setText(cur.getString(6))// direccion
                id_persona = cur.getInt(0)//id_persona
                etNombreNom.isEnabled = false
                id_spinnerTipoNomina.isEnabled = false
                Nomina_id.isEnabled = false
                idSwitch.isEnabled = false
                admin.close()
            } else {
                admin.close()
                Toast.makeText(this, "No se Encontro el empleado", Toast.LENGTH_SHORT).show()
            }
        }
    }
    fun BporNombre(){
        var query: String = ""
        nombreEmp = etNombreNom.text.toString()
        Tnomina
        query = "Select p.id_persona,p.num_nomina,p.nombre,p.tiponomina,p.id_direccion,p.id_departamento,d.direccion,dp.departamento " +
                "from persona as p inner join direccion as d on p.id_direccion=d.id_direccion inner join departamento as dp on dp.id_departamento=p.id_departamento where upper(p.nombre) =upper('$nombreEmp') and p.tiponomina ='$Tnomina'"
        val admin = DataBase(this)
        val cur = admin.Consulta(query)
        if (cur == null) {
            admin.close()
            Toast.makeText(this, "Error de Capa 8", Toast.LENGTH_SHORT).show()
            Nomina_id.requestFocus()
        } else {
            if (cur.moveToFirst()) {
                Toast.makeText(this, "Se encontro el empleado", Toast.LENGTH_SHORT).show()
                Nomina_id.setText(cur.getString(1))//nomina
                areaNom.setText(cur.getString(6))//direccion
                id_persona = cur.getInt(0)//id_persona
                etNombreNom.setText(cur.getString(2))//nombre
                etNombreNom.isEnabled = false
                id_spinnerTipoNomina.isEnabled = false
                Nomina_id.isEnabled = false
                idSwitch.isEnabled = false
                admin.close()
            } else {
                Toast.makeText(this, "No se Encontro el empleado", Toast.LENGTH_SHORT).show()
                admin.close()
            }
        }
    }

    fun regresarMenuAudi(v: View){
        var audimenu = Intent(this,menuActivity::class.java)
        startActivity(audimenu)
        finish()
    }

    fun registrarAuditoria(v: View){
        if(id_carro != 0 && id_persona != 0 && id_motor.text.toString().isNotEmpty() && id_carroceria.text.toString().isNotEmpty() && id_interior.text.toString().isNotEmpty()
             && id_noConformidades.text.toString().isNotEmpty() && id_Conclusiones.text.toString().isNotEmpty())
        {
            if (checkPoliciaco.isChecked){
                policiaco = "N/A"
            }
            else{
                policiaco =  id_eTactico.text.toString()
            }
            if(checkEmergencia.isChecked){
                Emergencia = "N/A"
            }else{
                Emergencia = id_emergencia.text.toString()
            }
            claveAudi = "AU"+fech
            tipoAudi
            idU
            id_carro
            id_persona
            nomina = Nomina_id.text.toString()
            numeroEconomico =  id_NEconomico.text.toString()
            fechaAudi = txFechaAud.text.toString()
            Hmotor = id_motor.text.toString()
            HCarroceria = id_carroceria.text.toString()
            HInterior =  id_interior.text.toString()
            NoConfor = id_noConformidades.text.toString()
            Conclusiones = id_Conclusiones.text.toString()
            llantas = id_llantas.text.toString()
            cinturones = id_cinturones.text.toString()
            bolsas = id_bolsasAire.text.toString()
            testigos = id_testigosTablero.text.toString()

            val sentencia = "INSERT INTO auditorias(c_auditorias,t_auditoria,id_persona,id_carro,id_usuario," +
                    "fecha,motor,carroceria,interior,aditamentos,equipo_tactico,n_conformidades,conclusion,fechallantas,cinturones,bolsasAire,testigosTablero) " +
                        "VALUES ('$claveAudi','$tipoAudi','$id_persona','$id_carro','$idU','$fechaAudi','$Hmotor','$HCarroceria'," +
                    "'$HInterior','$Emergencia','$policiaco','$NoConfor','$Conclusiones','$llantas','$cinturones','$bolsas','$testigos')"
            val admin = DataBase(this)
            if (admin.Ejecuta(sentencia)) {
                admin.close()
                Toast.makeText(this, "Se guardo la auditoria.", Toast.LENGTH_LONG).show()
                val audiG = Intent(this, menuActivity::class.java)
                startActivity(audiG)
                finish()
            } else {
                admin.close()
                Toast.makeText(this, "Error no se pudo guardar.", Toast.LENGTH_LONG).show()
                etNombreNom.requestFocus()
            }
        }else{
            Toast.makeText(this, "Debes de llenar todos los campos.", Toast.LENGTH_SHORT).show()
        }
    }
}