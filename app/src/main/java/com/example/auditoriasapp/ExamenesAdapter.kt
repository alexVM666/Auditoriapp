package com.example.auditoriasapp

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.celda_prototipo_examenes.view.*


class ExamenesAdapter(private var mListaExamenes: List<Examenes>,
                      private val mContext: Context,
                      private val clickListener: (Examenes) -> Unit)
    :RecyclerView.Adapter<ExamenesAdapter.ExamenesViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExamenesViewHolder {
        val layoutInflater = LayoutInflater.from(mContext)
        return ExamenesViewHolder(layoutInflater.inflate(R.layout.celda_prototipo_examenes,parent,false ))
    }

    override fun onBindViewHolder(holder: ExamenesViewHolder, position: Int) {
        holder.bind(mListaExamenes[position],mContext,clickListener)
    }

    override fun getItemCount(): Int = mListaExamenes.size

    fun setTask(Exa:List<Examenes>){
        mListaExamenes = Exa
        notifyDataSetChanged()
    }

    fun getTasks():List<Examenes> = mListaExamenes

    class ExamenesViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        fun bind(exaa:Examenes,context: Context,clickListener: (Examenes) -> Unit){
            itemView.tvC_Examen.text = "Clave: " + exaa.c_exa
            itemView.tv_aplico.text = "Aplicó: " + exaa.usu
            itemView.tv_fecha.text = "Fecha: " + exaa.fecha_apl
            itemView.tv_respondio.text = "Nombre: "+exaa.nomb
            itemView.tv_percentil.text = "Percentil final: "+exaa.perce
            itemView.tv_tipoE.text = "Tipo de Cuestionario: "+exaa.t_examen

            itemView.setOnClickListener { clickListener(exaa) }

        }
    }
}