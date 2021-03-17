package com.example.auditoriasapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.auditoriasapp.Database.DataBase
import kotlinx.android.synthetic.main.activity_recycler_chequeo.*

class ActivityRecyclerChequeo : AppCompatActivity() {
    private lateinit var idU: String
    var claveChequeo: String = ""

    private lateinit var viewAdapter: ChequeoAdapter
    private lateinit var viewManager: RecyclerView.LayoutManager
    val chequeosList: List<Chequeo> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recycler_chequeo)
        val chequeos = intent
        if (chequeos != null && chequeos.hasExtra("id_usuario")) {
            idU = chequeos.getStringExtra("id_usuario")
        }
        finalizarChequeoRV.setOnClickListener {
            finalizarExa()
        }
        viewManager = LinearLayoutManager(this)
        viewAdapter =
            ChequeoAdapter(chequeosList, this, { check: Chequeo -> onItemClickListener(check) })

        rv_chequeo.apply {
            setHasFixedSize(true)
            layoutManager = viewManager
            adapter = viewAdapter
            addItemDecoration(
                DividerItemDecoration(
                    this@ActivityRecyclerChequeo,
                    DividerItemDecoration.VERTICAL)
            )
        }

        ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {
            override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean {
                return false
            }
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, swipeDir: Int) {
                val position = viewHolder.adapterPosition
                val cheq = viewAdapter.getTasks()
                claveChequeo = cheq[position].c_che
                val admin = DataBase(baseContext)
                if (admin.Ejecuta("DELETE FROM chequeo WHERE c_chequeo = '$claveChequeo'")){
                    retriveChequeo()
                }
            }
        }).attachToRecyclerView(rv_chequeo)
    }
    private fun onItemClickListener(check: Chequeo) {
        val resultCheck = Intent(this,ActivityResultadosChequeo::class.java)
        resultCheck.putExtra("c_chequeo",check.c_che)
        resultCheck.putExtra("id_persona",check.pers)
        resultCheck.putExtra("id_carro",check.carro)
        resultCheck.putExtra("id_usuario",check.usuario)
        resultCheck.putExtra("tipo_revision",check.tipr)
        resultCheck.putExtra("tipo_mantenimiento",check.tipm)
        resultCheck.putExtra("kilometraje_actual",check.kmAct)
        resultCheck.putExtra("fecha_actual",check.fech_Act)
        resultCheck.putExtra("kilometraje_anterior",check.kmAnt)
        resultCheck.putExtra("fecha_anterior",check.fechAnt)
        resultCheck.putExtra("proxima_revision",check.prox_rv)
        resultCheck.putExtra("observaciones",check.obs)
        resultCheck.putExtra("num_nomina",check.nomi)
        resultCheck.putExtra("inventario",check.inv)
        resultCheck.putExtra("usuario",check.usu)
        resultCheck.putExtra("nombre",check.nomb)
        startActivity(resultCheck)
        finish()
    }
    override fun onResume() {
        super.onResume()
        retriveChequeo()
    }

    private fun retriveChequeo() {
        val checks = getChequeos()
        viewAdapter.setTasks(checks!!)
    }

    private fun getChequeos(): MutableList<Chequeo> {
        val checkk: MutableList<Chequeo> = ArrayList()
        val admin = DataBase(this)
        val tupla = admin.Consulta("SELECT c.c_chequeo,c.id_persona,c.id_carro,c.id_usuario,c.tipo_revision" +
                ",c.tipo_mantenimiento,c.kilometraje_actual,c.fecha_actual,c.kilometraje_anterior,c.fecha_anterior,c.proxima_revision,c.observaciones,p.num_nomina," +
                "ca.inventario, u.nomusuario, p.nombre FROM chequeo as c inner join persona as p on c.id_persona=p.id_persona inner join carro as ca on ca.id_carro=c.id_carro " +
                "inner join usuario as u on u.id_usuario=c.id_usuario where c.id_usuario = '$idU' order by c.tipo_revision")
        while (tupla!!.moveToNext()){
            val c_chequeo = tupla.getString(0)
            val pers = tupla.getInt(1)
            val carr = tupla.getInt(2)
            val idusu = tupla.getInt(3)
            val tipor = tupla.getString(4)
            val tipom = tupla.getString(5)
            val kmact =  tupla.getInt(6)
            val fecha_ac = tupla.getString(7)
            val kmant = tupla.getInt(8)
            val fecha_an = tupla.getString(9)
            val proxi = tupla.getString(10)
            val obser = tupla.getString(11)
            val nomi = tupla.getString(12)
            val inv = tupla.getString(13)
            val usu = tupla.getString(14)
            val nombre = tupla.getString(15)
            checkk.add(Chequeo(c_chequeo,pers,carr,idusu,tipor,tipom,kmact,fecha_ac,kmant,fecha_an,proxi,obser,nomi,inv,usu,nombre))
        }
        tupla.close()
        admin.close()
        return checkk
    }

    private fun finalizarExa() {
        val finalizarUN = Intent(this,menuActivity::class.java)
        startActivity(finalizarUN)
        finish()
    }
}