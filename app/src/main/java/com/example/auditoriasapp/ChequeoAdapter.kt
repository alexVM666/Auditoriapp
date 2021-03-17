package com.example.auditoriasapp

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.celda_prototipo_chequeo.view.*

class ChequeoAdapter(private var mListaChequeo: List<Chequeo>,
                     private val mContext:Context,
                     private val clickListener: (Chequeo)->Unit)
    :RecyclerView.Adapter<ChequeoAdapter.ChequeoViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChequeoViewHolder {
        val layoutInflater = LayoutInflater.from(mContext)
        return ChequeoViewHolder(layoutInflater.inflate(R.layout.celda_prototipo_chequeo,parent,false))
    }

    override fun onBindViewHolder(holder: ChequeoViewHolder, position: Int) {
        holder.bind(mListaChequeo[position],mContext,clickListener)
    }

    override fun getItemCount(): Int = mListaChequeo.size

    fun setTasks(Che:List<Chequeo>){
        mListaChequeo = Che
        notifyDataSetChanged()
    }

    fun getTasks():List<Chequeo> = mListaChequeo

    class ChequeoViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        fun bind(Chee:Chequeo,context: Context,clickListener: (Chequeo) -> Unit){
            itemView.tvC_chequeo.text = "Clave: " + Chee.c_che
            itemView.tvNomina.text = "Nombre: " + Chee.nomb
            itemView.tvT_Rev.text = "Tipo de Revisión: " +Chee.tipr
            itemView.tvNumEco.text = "Número de Inventario: " +Chee.inv
            itemView.tvKilAct.text = "Kilometraje verificado: "+ Chee.kmAct
            itemView.tvfecha.text = "Fecha: " +Chee.fech_Act
            itemView.tvAplicoC.text = "Aplicó: "+Chee.usu

            itemView.setOnClickListener { clickListener(Chee) }
        }
    }
}