package com.example.auditoriasapp

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.celda_prototipo_usuarios.view.*

class UsuariosNAdapter(private var mListaUsuarios:List<UsuariosN>,
                       private val mContext: Context,
                       private val clickListener: (UsuariosN) -> Unit)
    :RecyclerView.Adapter<UsuariosNAdapter.UsuariosViewHolder>(){


    /**
     * onCreateViewHolder() que como su nombre indica lo que hará será devolvernos
     * un objeto ViewHolder al cual le pasamos la celda prototió que hemos creado.
     *
     * @return Un nuevo UsuariosViewHolder que contiene la vista para cada usuario nuevo
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UsuariosViewHolder {
       val layoutInflater =  LayoutInflater.from(mContext)
        return UsuariosViewHolder(layoutInflater.inflate(R.layout.celda_prototipo_usuarios,parent,false))
    }

    /**
     * La clase RecyclerView. onBindViewHolder() se encarga de coger cada una de las
     * posiciones de la lista de usuarios y pasarlas a la clase ViewHolder(
     *
     * @param holder   Vincular los datos del cursor al ViewHolder
     * @param position La posición de los datos en la lista
     */
    override fun onBindViewHolder(holder: UsuariosViewHolder, position: Int) {
     holder.bind(mListaUsuarios[position],mContext,clickListener)
    }
    /**
     * El metodo getItemcount nos devuelve el tamaño de la lista,
     * que necesita el RecyclerView.
     * */
    override fun getItemCount(): Int = mListaUsuarios.size
    /**
     * Cuando los datos cambian, este metodo actualiza la lista de los usuarios nuevos
     * y notifica al adaptador a usar los nuevos valores
     * */
    fun setTask(Usuarios: List<UsuariosN>){
        mListaUsuarios = Usuarios
        notifyDataSetChanged()
    }

    fun getTasks(): List<UsuariosN> = mListaUsuarios

    //clase interna para crear viewholders
    class UsuariosViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        fun bind(usuar:UsuariosN,context: Context,clickListener: (UsuariosN) -> Unit){
            //se le asignan los valores a la celda prototipo de los usuarios
            itemView.tvNombreUN.text = "Nombre: "+ usuar.nom
            itemView.tvNominaUN.text = "Nómina: "+ usuar.nomi

            itemView.setOnClickListener { clickListener(usuar)}
        }
    }
}