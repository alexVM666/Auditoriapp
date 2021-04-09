package com.example.auditoriasapp

import android.Manifest
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Bitmap
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.print.PrintAttributes
import android.print.PrintManager
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.bumptech.glide.Glide
import com.example.auditoriasapp.Volley.VolleySingleton
import com.itextpdf.text.*
import com.itextpdf.text.pdf.BaseFont
import com.itextpdf.text.pdf.PdfWriter
import com.itextpdf.text.pdf.draw.LineSeparator
import com.itextpdf.text.pdf.draw.VerticalPositionMark
import com.karumi.dexter.Dexter
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.single.PermissionListener
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_resultados_chequeo.*
import kotlinx.android.synthetic.main.activity_resultados_chequeo.btn_imprimir
import kotlinx.android.synthetic.main.activity_resultados_chequeo.btn_regresar
import kotlinx.android.synthetic.main.activity_resultados_chequeo.txt_correo
import kotlinx.android.synthetic.main.activity_resultados_chequeo.txt_nomina
import org.json.JSONException
import org.json.JSONObject
import java.io.*
import java.lang.StringBuilder
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList
import kotlin.jvm.Throws

class ActivityResultadosChequeo : AppCompatActivity() {
    var imagenes = ArrayList<imagen>()
    companion object{
        private val file_name : String = "revision.pdf"

        fun getBitmap(context: Context,model: imagen , document: Document): Observable<imagen> {
            return Observable.fromCallable {
                val bitmap = Glide.with(context)
                    .asBitmap()
                    .load(model.imagen)
                    .submit().get()
                val image = Image.getInstance(bitmapToByteArray(bitmap))
                image.scaleAbsolute(90f,50f)
                document.add(image)
                model
            }
        }

        private fun bitmapToByteArray(bitmap: Bitmap?): ByteArray {
            val stream = ByteArrayOutputStream()
            bitmap!!.compress(Bitmap.CompressFormat.PNG,100,stream)
            return stream.toByteArray()
        }
    }
    private val appPath: String
        private get() {
            val dir = File(
                Environment.getExternalStorageDirectory()
                    .toString() + File.separator
                        + resources.getString(R.string.app_name)
                        +File.separator)
            if (!dir.exists()) dir.mkdir()
            return dir.path + File.separator
        }
    var idU:Int = 0
    var c_chequeo:String = ""
    var id_persona:Int = 0
    var id_carro:Int = 0
    var tipR:String = ""
    var tipM:String = ""
    var kmAct: Int = 0
    var fechaActual:String = ""
    var kmAnt:Int = 0
    var fechaAnterior:String = ""
    var proximaR:String = ""
    var observaciones:String = ""
    var nombre:String = ""

    var nomina: String = ""
    var inventario: String = ""
    var usuario: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_resultados_chequeo)
        val resultCheck = intent
        if (resultCheck != null && resultCheck.hasExtra("c_chequeo")){
            idU = resultCheck.getIntExtra("id_usuario",0)
            c_chequeo = resultCheck.getStringExtra("c_chequeo")
            id_persona = resultCheck.getIntExtra("id_persona",0)
            id_carro = resultCheck.getIntExtra("id_carro",0)
            tipR = resultCheck.getStringExtra("tipo_revision")
            tipM = resultCheck.getStringExtra("tipo_mantenimiento")
            kmAct = resultCheck.getIntExtra("kilometraje_actual",0)
            fechaActual = resultCheck.getStringExtra("fecha_actual")
            kmAnt = resultCheck.getIntExtra("kilometraje_anterior",0)
            fechaAnterior = resultCheck.getStringExtra("fecha_anterior")
            proximaR = resultCheck.getStringExtra("proxima_revision")
            observaciones = resultCheck.getStringExtra("observaciones")

            nomina = resultCheck.getStringExtra("num_nomina")
            inventario = resultCheck.getStringExtra("inventario")
            usuario = resultCheck.getStringExtra("usuario")
            nombre = resultCheck.getStringExtra("nombre")

            txtC_Chequeo.setText("$c_chequeo")
            txtFecha_Rev.setText("$fechaActual")
            txt_Obs.setText("$observaciones")
            txt_TipM.setText("$tipM")
            txt_TipR.setText("$tipR")
            txt_fechaAnt.setText("$fechaAnterior")
            txt_correo.setText("$usuario")
            txt_kmAct.setText("$kmAct")
            txt_kmAnt.setText("$kmAnt")
            txt_proxRev.setText("$proximaR")
            txt_num_eco.setText("$inventario")
            txt_nomina.setText("$nomina")
            txt_nombreUV.setText("$nombre")
        }
        btn_Subir.setOnClickListener {
            val builder = AlertDialog.Builder(this@ActivityResultadosChequeo)
            builder.setTitle("¿Quieres subir al servidor esta Revisión Automovilística?")
            builder.setCancelable(true)
            builder.setMessage("Con la siguiente clave: "+ c_chequeo)
            builder.setNegativeButton("Cancelar", DialogInterface.OnClickListener{
                    dialog, which ->
                Toast.makeText(this,"Cancelado",Toast.LENGTH_SHORT).show()
            })
            builder.setPositiveButton("Confirmar", DialogInterface.OnClickListener{
                    dialog, which ->
                try {
                    val jsonEntrada = JSONObject()
                    jsonEntrada.put("c_chequeo",c_chequeo)
                    jsonEntrada.put("id_persona",id_persona)
                    jsonEntrada.put("id_carro",id_carro)
                    jsonEntrada.put("id_usuario",idU)
                    jsonEntrada.put("tipo_revision",tipR)
                    jsonEntrada.put("tipo_mantenimiento",tipM)
                    jsonEntrada.put("kilometraje_actual",kmAct)
                    jsonEntrada.put("fecha_actual",fechaActual)
                    jsonEntrada.put("kilometraje_anterior",kmAnt)
                    jsonEntrada.put("fecha_anterior",fechaAnterior)
                    jsonEntrada.put("proxima_revision",proximaR)
                    jsonEntrada.put("observaciones",observaciones)
                    sendRequest(Address.IP + "Auditoriapp/Login/insertarRevisiones.php",jsonEntrada)
                }catch (e: JSONException){
                    e.printStackTrace()
                    Toast.makeText(this,"No se pudo subir al servidor, intente más tarde",Toast.LENGTH_SHORT).show()
                }
            })
            builder.show()
        }

        btn_regresar.setOnClickListener {
            val finalizar = Intent(this,menuActivity::class.java)
            startActivity(finalizar)
            finish()
        }
        initModel()
        Dexter.withActivity(this)
            .withPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
            .withListener(object : PermissionListener {
                override fun onPermissionGranted(response: PermissionGrantedResponse?) {
                    btn_imprimir.setOnClickListener {
                        createPDFFile(StringBuilder(appPath).append(file_name).toString())
                    }
                }

                override fun onPermissionDenied(response: PermissionDeniedResponse?) {
                    Toast.makeText(this@ActivityResultadosChequeo, "Por favor habilita los permisos", Toast.LENGTH_SHORT).show()
                }

                override fun onPermissionRationaleShouldBeShown(
                    permission: PermissionRequest?,
                    token: PermissionToken?
                ) {

                }

            })
            .check()
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

    private fun createPDFFile(path: String) {
        if (File(path).exists())
            File(path).delete()
        try {
            val document = Document()
            //save
            PdfWriter.getInstance(document, FileOutputStream(path))
            //open to write
            document.open()
            //Setting
            document.pageSize = PageSize.A4
            document.addCreationDate()
            document.addAuthor("Alejandro")
            document.addCreator("luis")

            val C = Calendar.getInstance()
            //Se obtiene el formato requerido
            val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
            val strDate = sdf.format(C.time)
            //font setting
            val colorAccent = BaseColor(0,153,255)
            val headingFontSize = 16.0f
            val valueFontSize = 14.0f
            //custom font
            val fontName = BaseFont.createFont("assets/fonts/Delicious-Roman.otf",BaseFont.IDENTITY_H, BaseFont.EMBEDDED)

            val headingStyle = Font(fontName,headingFontSize, Font.NORMAL,colorAccent)
            val valueStyle = Font(fontName,valueFontSize, Font.NORMAL, BaseColor.BLACK)
            //add title
            val titleStyle = Font(fontName, 20.0f, Font.NORMAL, BaseColor.BLACK)

            //Use RxJava to fetch image and add to PDF
            Observable.fromIterable(imagenes)
                .flatMap { model:imagen ->getBitmap(this,model,document) }
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ model: imagen ->

                }, { t: Throwable? ->
                    //on Error
                    Toast.makeText(this, t!!.message, Toast.LENGTH_SHORT).show()
                }, {
                    addNewItem(document,"Gestión de Seguridad Vital", Element.ALIGN_CENTER,titleStyle)
                    addLineSpace(document)
                    addNewItem(document,"Fecha: "+strDate,Element.ALIGN_RIGHT,valueStyle)
                    addLineSpace(document)
                    addNewItem(document,"Resultados de la Revisión Vehicular", Element.ALIGN_LEFT,titleStyle)

                    //add title to document

                    addNewItem(document,"Clave:", Element.ALIGN_LEFT,headingStyle)

                    addNewItem(document,c_chequeo, Element.ALIGN_LEFT,valueStyle)

                    addLineSeperator(document)

                    addNewItem(document,"Fecha de la Revisión: ", Element.ALIGN_LEFT,headingStyle)
                    addNewItem(document,fechaActual, Element.ALIGN_LEFT,valueStyle)

                    addLineSeperator(document)
                    addNewItem(document,"Aplicó:", Element.ALIGN_LEFT,headingStyle)
                    addNewItem(document,usuario, Element.ALIGN_LEFT,valueStyle)

                    addLineSeperator(document)

                    addNewItem(document,"Usuario Vehicular(nómina):", Element.ALIGN_LEFT,headingStyle)
                    addNewItem(document,nomina, Element.ALIGN_LEFT,valueStyle)

                    addLineSeperator(document)

                    addNewItem(document,"Usuario Vehicular(Nombre):",Element.ALIGN_LEFT,headingStyle)
                    addNewItem(document,nombre,Element.ALIGN_LEFT,valueStyle)

                    addLineSeperator(document)

                    addNewItem(document,"Número de Inventario: ", Element.ALIGN_LEFT,headingStyle)
                    addNewItem(document,inventario, Element.ALIGN_LEFT,valueStyle)


                    addLineSeperator(document)

                    //product  detail
                    addLineSpace(document)
                    addNewItem(document,"Datos de la Revisión", Element.ALIGN_CENTER,titleStyle)

                    addLineSeperator(document)

                    addNewItem(document,"Tipo de Revisión:", Element.ALIGN_LEFT,headingStyle)
                    addNewItem(document,tipR, Element.ALIGN_LEFT,valueStyle)

                    addLineSeperator(document)
                    addNewItem(document,"Tipo de Mantenimiento:", Element.ALIGN_LEFT,headingStyle)
                    addNewItem(document,tipM, Element.ALIGN_LEFT,valueStyle)

                    addLineSeperator(document)
                    addNewItem(document,"Kilometraje previo:", Element.ALIGN_LEFT,headingStyle)
                    addNewItem(document,kmAnt.toString(), Element.ALIGN_LEFT,valueStyle)

                    addLineSeperator(document)
                    addNewItem(document,"Fecha de la revisión previa:", Element.ALIGN_LEFT,headingStyle)
                    addNewItem(document,fechaAnterior, Element.ALIGN_LEFT,valueStyle)

                    addLineSeperator(document)
                    addNewItem(document,"Kilometraje: ", Element.ALIGN_LEFT,headingStyle)
                    addNewItem(document,kmAct.toString(), Element.ALIGN_LEFT,valueStyle)

                    addLineSeperator(document)
                    addNewItem(document,"Próxima Revisión:", Element.ALIGN_LEFT,headingStyle)
                    addNewItem(document,proximaR, Element.ALIGN_LEFT,valueStyle)

                    addLineSeperator(document)
                    addNewItem(document,"Observaciones:", Element.ALIGN_LEFT,headingStyle)
                    addNewItem(document,observaciones, Element.ALIGN_LEFT,valueStyle)

                    addLineSeperator(document)
                    //on Complete
                    addLineSpace(document)
                    addLineSpace(document)

                    //Close
                    document.close()
                    Toast.makeText(this, "Success", Toast.LENGTH_SHORT).show()
                    printPDF()
                })

        }catch (e: FileNotFoundException){
            e.printStackTrace()
        }catch (e: IOException){
            e.printStackTrace()
        }catch (e:DocumentException){
            e.printStackTrace()
        }finally {
        }
    }

    private fun printPDF() {
        val printManager = getSystemService(Context.PRINT_SERVICE) as PrintManager
        try {
            val printDocumentAdapter = PdfDocumentAdapter(
                StringBuilder(appPath).append(file_name).toString(),
                file_name)
            printManager.print("Document",printDocumentAdapter,PrintAttributes.Builder().build())
        }catch (e: Exception){
            e.printStackTrace()
        }
    }


    @Throws(DocumentException::class)
    private fun addNewItemWithLeftAndRight(document: Document, textLeft: String, textRight: String, leftStyle: Font, rightStyle: Font) {
        val chunkTextLeft = Chunk(textLeft,leftStyle)
        val chunkTextRight = Chunk(textRight,rightStyle)
        val p = Paragraph(chunkTextLeft)
        p.add(Chunk(VerticalPositionMark()))
        p.add(chunkTextRight)
        document.add(p)
    }

    @Throws(DocumentException::class)
    private fun addLineSeperator(document: Document) {
        val lineSeparator = LineSeparator()
        lineSeparator.lineColor = BaseColor(0,0,0,68)
        addLineSpace(document)
        document.add(Chunk(lineSeparator))
        addLineSpace(document)
    }

    @Throws(DocumentException::class)
    private fun addLineSpace(document: Document) {
        document.add(Paragraph(""))
    }

    @Throws(DocumentException::class)
    private fun addNewItem(document: Document, text: String, align: Int, style: Font) {
        val chunk = Chunk(text,style)
        val p = Paragraph(chunk)
        p.alignment = align
        document.add(p)
    }
    private fun initModel() {
        var model = imagen()
        model.imagen = R.drawable.escudocelaya
        imagenes.add(model)
    }
}