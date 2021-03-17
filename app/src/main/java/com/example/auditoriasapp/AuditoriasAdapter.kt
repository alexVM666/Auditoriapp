package com.example.auditoriasapp

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.celda_prototipo_auditorias.view.*

class AuditoriasAdapter(private var mListaAuditorias: List<Auditorias>,
                        private val mContext: Context,
                        private val clickListener: (Auditorias) -> Unit)
    :RecyclerView.Adapter<AuditoriasAdapter.AuditoriasViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AuditoriasViewHolder {
       val layoutInflater =  LayoutInflater.from(mContext)
        return AuditoriasViewHolder(layoutInflater.inflate(R.layout.celda_prototipo_auditorias,parent,false))
    }

    override fun onBindViewHolder(holder: AuditoriasViewHolder, position: Int) {
     holder.bind(mListaAuditorias[position],mContext,clickListener)
    }

    override fun getItemCount(): Int = mListaAuditorias.size

    fun setTask(Audit:List<Auditorias>){
        mListaAuditorias = Audit
        notifyDataSetChanged()
    }

    fun getTasks(): List<Auditorias> = mListaAuditorias

    class AuditoriasViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        fun bind(audit:Auditorias,context: Context,clickListener: (Auditorias) -> Unit){
            itemView.tvC_Audi.text = "Clave: "+ audit.c_audi
            itemView.tv_T_audi.text = "Tipo de Auditoria: "+ audit.t_audi
            itemView.tv_fechaa.text = "Fecha: "+ audit.fechaa
            itemView.tv_nomina.text = "Nombre: "+ audit.nomb
            itemView.tv_num_eco.text = "Número de Inventario: "+ audit.inv
            itemView.tv_aplico.text = "Aplicó: " +audit.usu

            itemView.setOnClickListener { clickListener(audit) }
        }
    }
}
