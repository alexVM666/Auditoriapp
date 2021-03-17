package com.example.auditoriasapp

import android.Manifest
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.print.PrintAttributes
import android.print.PrintManager
import android.util.Log
import android.widget.Toast
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
import kotlinx.android.synthetic.main.activity_result_volley_chequeo.*
import kotlinx.android.synthetic.main.activity_result_volley_chequeo.btn_imprimir
import kotlinx.android.synthetic.main.activity_result_volley_chequeo.btn_regresar
import kotlinx.android.synthetic.main.activity_result_volley_chequeo.txtC_Chequeo
import kotlinx.android.synthetic.main.activity_result_volley_chequeo.txtFecha_Rev
import kotlinx.android.synthetic.main.activity_result_volley_chequeo.txt_Obs
import kotlinx.android.synthetic.main.activity_result_volley_chequeo.txt_TipM
import kotlinx.android.synthetic.main.activity_result_volley_chequeo.txt_TipR
import kotlinx.android.synthetic.main.activity_result_volley_chequeo.txt_correo
import kotlinx.android.synthetic.main.activity_result_volley_chequeo.txt_fechaAnt
import kotlinx.android.synthetic.main.activity_result_volley_chequeo.txt_kmAct
import kotlinx.android.synthetic.main.activity_result_volley_chequeo.txt_kmAnt
import kotlinx.android.synthetic.main.activity_result_volley_chequeo.txt_nomina
import kotlinx.android.synthetic.main.activity_result_volley_chequeo.txt_num_eco
import kotlinx.android.synthetic.main.activity_result_volley_chequeo.txt_proxRev
import org.json.JSONObject
import java.io.*
import java.lang.StringBuilder
import java.text.SimpleDateFormat
import java.util.*
import kotlin.jvm.Throws

class ActivityResultVolleyChequeo : AppCompatActivity() {
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

    private lateinit var sCorr:String
    var c_chequeo:String = ""
    var nomina:String = ""
    var num_eco:String = ""
    var tipR:String = ""
    var tipM:String = ""
    var kmAct: Int = 0
    var fechaActual:String = ""
    var kmAnt:Int = 0
    var fechaAnterior:String = ""
    var proximaR:String = ""
    var observaciones:String = ""
    var nombre:String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_result_volley_chequeo)
        val resultCheck = intent
        if (resultCheck != null && resultCheck.hasExtra("c_chequeo")){
            sCorr = resultCheck.getStringExtra("correo")
            c_chequeo = resultCheck.getStringExtra("c_chequeo")
            nomina = resultCheck.getStringExtra("nomina")
            num_eco = resultCheck.getStringExtra("num_economico")
            tipR = resultCheck.getStringExtra("tipo_revision")
            tipM = resultCheck.getStringExtra("tipo_mantenimiento")
            kmAct = resultCheck.getIntExtra("kilometraje_actual",0)
            fechaActual = resultCheck.getStringExtra("fecha_actual")
            kmAnt = resultCheck.getIntExtra("kilometraje_anterior",0)
            fechaAnterior = resultCheck.getStringExtra("fecha_anterior")
            proximaR = resultCheck.getStringExtra("proxima_revision")
            observaciones = resultCheck.getStringExtra("observaciones")
            nombre = resultCheck.getStringExtra("nombre")

            txtC_Chequeo.setText("$c_chequeo")
            txtFecha_Rev.setText("$fechaActual")
            txt_Obs.setText("$observaciones")
            txt_TipM.setText("$tipM")
            txt_TipR.setText("$tipR")
            txt_fechaAnt.setText("$fechaAnterior")
            txt_correo.setText("$sCorr")
            txt_kmAct.setText("$kmAct")
            txt_kmAnt.setText("$kmAnt")
            txt_proxRev.setText("$proximaR")
            txt_num_eco.setText("$num_eco")
            txt_nomina.setText("$nomina")
            txt_nombreCH.setText("$nombre")
            if(fechaAnterior == "0000-00-00 00:00:00"){
                fechaAnterior = "Primera Revision"
                txt_fechaAnt.setText("$fechaAnterior")
            }
        }

        btn_regresar.setOnClickListener {
            val finalizar = Intent(this,Activity_Volley_Chequeo::class.java)
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
                    Toast.makeText(this@ActivityResultVolleyChequeo, "Por favor habilita los permisos", Toast.LENGTH_SHORT).show()
                }

                override fun onPermissionRationaleShouldBeShown(
                    permission: PermissionRequest?,
                    token: PermissionToken?
                ) {

                }

            })
            .check()
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
            //add title
            val titleStyle = Font(fontName, 20.0f, Font.NORMAL, BaseColor.BLACK)
            val valueStyle = Font(fontName,valueFontSize, Font.NORMAL, BaseColor.BLACK)
            //Use RxJava to fetch image and add to PDF
            Observable.fromIterable(imagenes)
                .flatMap { model:imagen ->getBitmap(this,model,document)
                }
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
            addNewItem(document,sCorr, Element.ALIGN_LEFT,valueStyle)

            addLineSeperator(document)

            addNewItem(document,"Usuario Vehicular(Nómina):", Element.ALIGN_LEFT,headingStyle)
            addNewItem(document,nomina, Element.ALIGN_LEFT,valueStyle)

            addLineSeperator(document)
                    addNewItem(document,"Usuario Vehicular(Nombre):",Element.ALIGN_LEFT,headingStyle)
                    addNewItem(document,nombre,Element.ALIGN_LEFT,valueStyle)
                    addLineSeperator(document)

            addNewItem(document,"Número de Inventario: ", Element.ALIGN_LEFT,headingStyle)
            addNewItem(document,num_eco, Element.ALIGN_LEFT,valueStyle)



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
            addNewItem(document,"Fecha de la Revisión previa:", Element.ALIGN_LEFT,headingStyle)
            addNewItem(document,fechaAnterior, Element.ALIGN_LEFT,valueStyle)

            addLineSeperator(document)
            addNewItem(document,"Kilometraje: ", Element.ALIGN_LEFT,headingStyle)
            addNewItem(document,kmAct.toString(), Element.ALIGN_LEFT,valueStyle)

            addLineSeperator(document)
            addNewItem(document,"Próxima Revisión(Por km, Fecha, por Hras, etc.):", Element.ALIGN_LEFT,headingStyle)
            addNewItem(document,proximaR, Element.ALIGN_LEFT,valueStyle)

            addLineSeperator(document)
            addNewItem(document,"Observaciones:", Element.ALIGN_LEFT,headingStyle)
            addNewItem(document,observaciones, Element.ALIGN_LEFT,valueStyle)

            addLineSeperator(document)
            //close
            document.close()
            Toast.makeText(this@ActivityResultVolleyChequeo, "Success", Toast.LENGTH_SHORT).show()

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
            val printDocumentAdapter = PdfDocumentAdapter(StringBuilder(appPath).append(file_name).toString(),
                file_name)
            printManager.print("Document",printDocumentAdapter,PrintAttributes.Builder().build())
        }catch (e: java.lang.Exception){
            e.printStackTrace()
        }
    }
    private fun initModel() {
        var model = imagen()
        model.imagen = R.drawable.escudocelaya
        imagenes.add(model)
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

}