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
import kotlinx.android.synthetic.main.activity_result_volley_examenes.*
import kotlinx.android.synthetic.main.activity_result_volley_examenes.btn_imprimir
import kotlinx.android.synthetic.main.activity_result_volley_examenes.txt_fecha
import kotlinx.android.synthetic.main.activity_result_volley_examenes.txt_nomina
import java.io.*
import java.lang.StringBuilder
import java.text.SimpleDateFormat
import java.util.*
import kotlin.jvm.Throws

class ActivityResultVolleyExamenes : AppCompatActivity() {
    var imagenes = ArrayList<imagen>()
    companion object{
        private val file_name : String = "cuestionarios.pdf"

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

    private lateinit var c_examen: String
    private lateinit var tipo_examen: String
    private lateinit var nomina: String
    private lateinit var correo: String
    private lateinit var fecha_aplicacion: String
    var calif1: Int = 0
    var calif2: Int = 0
    var calif3: Int = 0
    var resultado: Double = 0.0
    private lateinit var percentil: String
    var aManejando: Int = 0
    private lateinit var VehiculosD: String
    var nombre: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_result_volley_examenes)
        val resulExa = intent
        if (resulExa != null && resulExa.hasExtra("c_examen")&& resulExa.hasExtra("tipo_examen")&&resulExa.hasExtra("nomina")){
            c_examen = resulExa.getStringExtra("c_examen")
            tipo_examen = resulExa.getStringExtra("tipo_examen")
            nomina = resulExa.getStringExtra("nomina")
            correo = resulExa.getStringExtra("correo")
            fecha_aplicacion = resulExa.getStringExtra("fecha_aplicacion")
            calif1 = resulExa.getIntExtra("calif1",0)
            calif2 = resulExa.getIntExtra("calif2",0)
            calif3 = resulExa.getIntExtra("calif3",0)
            resultado = resulExa.getDoubleExtra("resultado",0.0)
            percentil = resulExa.getStringExtra("percentil")
            aManejando = resulExa.getIntExtra("aManejando",0)
            VehiculosD = resulExa.getStringExtra("VehiculosD")
            nombre = resulExa.getStringExtra("nombre")

            txtC_examen.setText("$c_examen")
            txtT_Examen.setText("$tipo_examen")
            txt_nomina.setText("$nomina")
            txt_correo.setText("$correo")
            txt_fecha.setText("$fecha_aplicacion")
            txt_percentil.setText("$percentil")
            txt_añosm.setText("$aManejando")
            txt_vehiculos.setText("$VehiculosD")
            txt_nombreEX.setText("$nombre")
            if (tipo_examen.equals("Administrativo")){
                txt_bloque1.setText("$calif1/5")
                txt_bloque2.setText("$calif2/5")
                txt_bloque3.setText("$calif3/5")
                txt_resultadof.setText("$resultado/5")
            }else{
                txt_bloque1.setText("$calif1/14")
                txt_bloque2.setText("$calif2/10")
                txt_bloque3.setText("$calif3/10")
                txt_resultadof.setText("$resultado/10")
            }
        }
        btn_regresar.setOnClickListener{
            val finalizar = Intent(this,Activity_Volley_Examenes::class.java)
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
                    Toast.makeText(this@ActivityResultVolleyExamenes, "Por favor habilita los permisos", Toast.LENGTH_SHORT).show()
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
            val headingStyle = Font(fontName,headingFontSize,Font.NORMAL,colorAccent)
            val valueStyle = Font(fontName,valueFontSize,Font.NORMAL,BaseColor.BLACK)
            //add title
            val titleStyle = Font(fontName, 20.0f, Font.NORMAL,BaseColor.BLACK)
            //Use RxJava to fetch image and add to PDF
            Observable.fromIterable(imagenes)
                .flatMap { model:imagen -> getBitmap(this, model, document)
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
            addNewItem(document,"Resultados del Examen", Element.ALIGN_LEFT,titleStyle)
            addLineSeperator(document)
            //add title to document

            addNewItem(document,"Aplicó:",Element.ALIGN_LEFT,headingStyle)

            addNewItem(document,correo,Element.ALIGN_LEFT,valueStyle)

            addLineSeperator(document)

            addNewItem(document,"Respondió(Nómina): ",Element.ALIGN_LEFT,headingStyle)
            addNewItem(document,nomina,Element.ALIGN_LEFT,valueStyle)
            addLineSeperator(document)

                    addNewItem(document,"Respondió(Nombre): ",Element.ALIGN_LEFT,headingStyle)
                    addNewItem(document,nombre,Element.ALIGN_LEFT,valueStyle)
                    addLineSeperator(document)

            addNewItem(document,"Clave:",Element.ALIGN_LEFT,headingStyle)
            addNewItem(document,c_examen,Element.ALIGN_LEFT,valueStyle)

            addLineSeperator(document)

            addNewItem(document,"Fecha de Aplicación:",Element.ALIGN_LEFT,headingStyle)
            addNewItem(document,fecha_aplicacion,Element.ALIGN_LEFT,valueStyle)

            addLineSeperator(document)

            addNewItem(document,"Tipo de Cuestionario:",Element.ALIGN_LEFT,headingStyle)
            addNewItem(document,tipo_examen,Element.ALIGN_LEFT,valueStyle)

            addLineSeperator(document)

            addNewItem(document,"Tiempo manejando(Años):",Element.ALIGN_LEFT,headingStyle)
            addNewItem(document,aManejando.toString(),Element.ALIGN_LEFT,valueStyle)

            addLineSeperator(document)
            addNewItem(document,"Vehículos que domina:",Element.ALIGN_LEFT,headingStyle)
            addNewItem(document,VehiculosD,Element.ALIGN_LEFT,valueStyle)

            addLineSeperator(document)

            //product  detail
            addLineSpace(document)
            addNewItem(document,"Resultados", Element.ALIGN_CENTER,titleStyle)

            addLineSeperator(document)
             if (tipo_examen.equals("Administrativo")){
                 //item 1
                 addNewItemWithLeftAndRight(document,"Bloque 1: ",calif1.toString()+"/5",titleStyle,valueStyle)
                 addLineSeperator(document)
                 //item 2
                 addNewItemWithLeftAndRight(document,"Bloque 2: ",calif2.toString()+"/5",titleStyle,valueStyle)
                 addLineSeperator(document)
                 //item 3
                 addNewItemWithLeftAndRight(document,"Bloque 3: ",calif3.toString()+"/5",titleStyle,valueStyle)
                 addLineSeperator(document)
                 //total
                 addLineSpace(document)
                 addLineSpace(document)

                 addNewItemWithLeftAndRight(document,"Resultado Final: ",resultado.toString()+"/5",titleStyle,valueStyle)
                 addLineSeperator(document)
             }else{
                 //item 1
                 addNewItemWithLeftAndRight(document,"Bloque 1: ",calif1.toString()+"/15",titleStyle,valueStyle)
                 addLineSeperator(document)
                 //item 2
                 addNewItemWithLeftAndRight(document,"Bloque 2: ",calif2.toString()+"/10",titleStyle,valueStyle)
                 addLineSeperator(document)
                 //item 3
                 addNewItemWithLeftAndRight(document,"Bloque 3: ",calif3.toString()+"/10",titleStyle,valueStyle)
                 addLineSeperator(document)
                 //total
                 addLineSpace(document)
                 addLineSpace(document)

                 addNewItemWithLeftAndRight(document,"Resultado Final: ",resultado.toString()+"/10",titleStyle,valueStyle)
                 addLineSeperator(document)
             }
            addLineSpace(document)
            addLineSpace(document)

            addNewItemWithLeftAndRight(document,"Percentil: ",percentil,titleStyle,valueStyle)

            //close
            document.close()
            Toast.makeText(this@ActivityResultVolleyExamenes, "Success", Toast.LENGTH_SHORT).show()

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
    private fun addNewItemWithLeftAndRight(document: Document, textLeft: String, textRight: String, leftStyle: Font,rightStyle:Font) {
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