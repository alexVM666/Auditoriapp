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
import com.bumptech.glide.Glide
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
import kotlinx.android.synthetic.main.activity_result_volley_auditorias.*
import kotlinx.android.synthetic.main.activity_result_volley_auditorias.btn_imprimir
import kotlinx.android.synthetic.main.activity_result_volley_auditorias.txt_fecha
import kotlinx.android.synthetic.main.activity_result_volley_auditorias.txt_nomina
import java.io.*
import java.lang.StringBuilder
import java.text.SimpleDateFormat
import java.util.*
import kotlin.jvm.Throws

class ActivityResultVolleyAuditorias : AppCompatActivity() {
    var imagenes = ArrayList<imagen>()
    companion object{
        private val file_name : String = "auditoria.pdf"

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

    private lateinit var c_auditorias: String
    private lateinit var t_auditorias: String
    private lateinit var nomina: String
    private lateinit var fecha: String
    private lateinit var motor: String
    var neco: String=""
    private lateinit var carroceria: String
    private lateinit var interior: String
    private lateinit var aditamentos: String
    private lateinit var equipo_tactico: String
    private lateinit var no_conformidades: String
    private lateinit var conclusion: String
    private lateinit var correo: String
    private lateinit var llantas: String
    private lateinit var cinturones: String
    private lateinit var bolsas: String
    private lateinit var testigos: String
    private lateinit var nombre: String

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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_result_volley_auditorias)
        val auditoResul =  intent
        if(auditoResul!= null && auditoResul.hasExtra("c_auditorias") && auditoResul.hasExtra("t_auditorias")
            && auditoResul.hasExtra("nomina")&& auditoResul.hasExtra("fecha")){
            c_auditorias = auditoResul.getStringExtra("c_auditorias")
            t_auditorias = auditoResul.getStringExtra("t_auditorias")
            nomina = auditoResul.getStringExtra("nomina")
            fecha = auditoResul.getStringExtra("fecha")
            neco = auditoResul.getStringExtra("no_economico")
            motor = auditoResul.getStringExtra("motor")
            carroceria = auditoResul.getStringExtra("carroceria")
            interior = auditoResul.getStringExtra("interior")
            aditamentos = auditoResul.getStringExtra("aditamentos")
            equipo_tactico = auditoResul.getStringExtra("equipo_tactico")
            no_conformidades = auditoResul.getStringExtra("no_conformidades")
            conclusion = auditoResul.getStringExtra("conclusion")
            correo = auditoResul.getStringExtra("correo")
            llantas = auditoResul.getStringExtra("fechallantas")
            cinturones = auditoResul.getStringExtra("cinturones")
            bolsas = auditoResul.getStringExtra("bolsasAire")
            testigos = auditoResul.getStringExtra("testigosTablero")
            nombre = auditoResul.getStringExtra("nombre")

            txtC_Auditoria.setText("$c_auditorias")
            txtT_Auditoria.setText("$t_auditorias")
            txt_nomina.setText("$nomina")
            txt_fecha.setText("$fecha")
            txt_motor.setText("$motor")
            txt_num_eco.setText("$neco")
            txt_carroceria.setText("$carroceria")
            txt_interior.setText("$interior")
            txt_aditamentos.setText("$aditamentos")
            txt_equipo.setText("$equipo_tactico")
            txt_nc.setText("$no_conformidades")
            txt_conclusiones.setText("$conclusion")
            txt_correoo.setText("$correo")
            txt_llantas.setText("$llantas")
            txt_bolsas.setText("$bolsas")
            txt_testigos.setText("$testigos")
            txt_cinturones.setText("$cinturones")
            txt_nombreAU.setText("$nombre")
        }
        btn_regresar2.setOnClickListener {
            val regre = Intent(this,Activity_Volley_Auditorias::class.java)
            startActivity(regre)
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
                    Toast.makeText(this@ActivityResultVolleyAuditorias, "Por favor habilita los permisos", Toast.LENGTH_SHORT).show()
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
            val valueStyle = Font(fontName,valueFontSize, Font.NORMAL, BaseColor.BLACK)

            //add title
            val titleStyle = Font(fontName, 20.0f, Font.NORMAL, BaseColor.BLACK)
            //Use RxJava to fetch image and add to PDF
            Observable.fromIterable(imagenes)
                .flatMap { model:imagen ->
                    ActivityResultVolleyExamenes.getBitmap(this, model, document)
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
                addNewItem(document,"Resultados de la Auditoria", Element.ALIGN_LEFT,titleStyle)

                //add title to document

                addNewItem(document,"Aplicó: ", Element.ALIGN_LEFT,headingStyle)

                addNewItem(document,correo, Element.ALIGN_LEFT,valueStyle)

                addLineSeperator(document)
                addNewItem(document,"Nómina del Usuario Vehicular: ", Element.ALIGN_LEFT,headingStyle)
                addNewItem(document,nomina, Element.ALIGN_LEFT,valueStyle)

                addLineSeperator(document)
                    addNewItem(document,"Nombre del Usuario Vehicular: ", Element.ALIGN_LEFT,headingStyle)
                    addNewItem(document,nombre, Element.ALIGN_LEFT,valueStyle)

                    addLineSeperator(document)

                addNewItem(document,"Fecha de Aplicación:", Element.ALIGN_LEFT,headingStyle)
                addNewItem(document,fecha, Element.ALIGN_LEFT,valueStyle)

                addLineSeperator(document)

                addNewItem(document,"Tipo de Auditoria:", Element.ALIGN_LEFT,headingStyle)
                addNewItem(document,t_auditorias, Element.ALIGN_LEFT,valueStyle)

                addLineSeperator(document)
                    addNewItem(document,"Número de Inventario: ", Element.ALIGN_LEFT,headingStyle)
                addNewItem(document,neco, Element.ALIGN_LEFT,valueStyle)

            addLineSeperator(document)
            addNewItem(document,"Clave:", Element.ALIGN_LEFT,headingStyle)
            addNewItem(document,c_auditorias, Element.ALIGN_LEFT,valueStyle)

            addLineSeperator(document)

            //product  detail
            addLineSpace(document)
            addNewItem(document,"Resultados de la Auditoria", Element.ALIGN_CENTER,titleStyle)

            addLineSeperator(document)
            addNewItem(document,"Hallazgos del motor:", Element.ALIGN_LEFT,headingStyle)
            addNewItem(document,motor, Element.ALIGN_LEFT,valueStyle)

            addLineSeperator(document)
            addNewItem(document,"Hallazgos de la carroceria:", Element.ALIGN_LEFT,headingStyle)
            addNewItem(document,carroceria, Element.ALIGN_LEFT,valueStyle)

            addLineSeperator(document)
            addNewItem(document,"Hallazgos del interior:", Element.ALIGN_LEFT,headingStyle)
            addNewItem(document,interior, Element.ALIGN_LEFT,valueStyle)
                    addLineSeperator(document)
                    addNewItem(document,"Hallazgos de las llantas (fecha de fabricación):", Element.ALIGN_LEFT,headingStyle)
                    addNewItem(document,llantas, Element.ALIGN_LEFT,valueStyle)
                    addLineSeperator(document)
                    addNewItem(document,"Hallazgos de los cinturones (Estado):", Element.ALIGN_LEFT,headingStyle)
                    addNewItem(document,cinturones, Element.ALIGN_LEFT,valueStyle)
                    addLineSeperator(document)
                    addNewItem(document,"Hallazgos de las bolsas de aire:", Element.ALIGN_LEFT,headingStyle)
                    addNewItem(document,bolsas, Element.ALIGN_LEFT,valueStyle)
                    addLineSeperator(document)
                    addNewItem(document,"Hallazgos de los Testigos del Tablero:", Element.ALIGN_LEFT,headingStyle)
                    addNewItem(document,testigos, Element.ALIGN_LEFT,valueStyle)
                    addLineSeperator(document)
            addNewItem(document,"Hallazgos de los Aditamentos:", Element.ALIGN_LEFT,headingStyle)
            addNewItem(document,aditamentos, Element.ALIGN_LEFT,valueStyle)

            addLineSeperator(document)
            addNewItem(document,"Hallazgos del Equipo Táctico:", Element.ALIGN_LEFT,headingStyle)
            addNewItem(document,equipo_tactico, Element.ALIGN_LEFT,valueStyle)

            addLineSeperator(document)
            addNewItem(document,"No Conformidades:", Element.ALIGN_LEFT,headingStyle)
            addNewItem(document,no_conformidades, Element.ALIGN_LEFT,valueStyle)

            addLineSeperator(document)
            addNewItem(document,"Conclusiones:", Element.ALIGN_LEFT,headingStyle)
            addNewItem(document,conclusion, Element.ALIGN_LEFT,valueStyle)

            addLineSeperator(document)

            //close
            document.close()
            Toast.makeText(this@ActivityResultVolleyAuditorias, "Success", Toast.LENGTH_SHORT).show()

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