package com.example.auditoriasapp.Volley

import android.content.Context
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.Volley

// Clase que se implementa, para aplicar el patron de diseño Singleton,
// que permite una sola instancia de esta clase
class VolleySingleton constructor(context: Context){
    companion object {
        @Volatile
        private var INSTANCE: VolleySingleton? = null
        fun getInstance(context: Context)=
            INSTANCE ?: synchronized(this){
                INSTANCE ?: VolleySingleton(context).also {
                    INSTANCE = it
                }
            }
    }
    val requestQueue: RequestQueue by lazy {
        Volley.newRequestQueue(context.applicationContext)
    }
    fun <T> addToRequestQueue(req: Request<T>){
        requestQueue.add(req)
    }
}