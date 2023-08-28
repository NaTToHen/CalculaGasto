package com.nattodev.calculagasto

import android.content.Context
import android.view.LayoutInflater
import android.widget.TextView
import android.widget.Toast
import java.text.DecimalFormat

fun toastSucesso(texto:String, context: Context) {
    val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
    val layout = inflater.inflate(R.layout.toast_sucesso, null)

    val textView = layout.findViewById<TextView>(R.id.textoToastSucesso)
    textView.text = texto

    val toast = Toast(context)
    toast.duration = Toast.LENGTH_SHORT
    toast.view = layout
    toast.show()
}

fun toastErro(texto:String, context: Context) {

    val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
    val layout = inflater.inflate(R.layout.toast_erro, null)

    val textView = layout.findViewById<TextView>(R.id.textoToast)
    textView.text = texto

    val toast = Toast(context)
    toast.duration = Toast.LENGTH_SHORT
    toast.view = layout
    toast.show()
}

fun formataFloat(valor:String):String {
    val decimalFormat = DecimalFormat("#.00")
    val valorFormatado = decimalFormat.format(valor.toFloat()).replace(',', '.')
    return valorFormatado
}

