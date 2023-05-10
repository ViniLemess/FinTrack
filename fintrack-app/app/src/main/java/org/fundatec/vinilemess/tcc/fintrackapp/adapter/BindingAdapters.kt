package org.fundatec.vinilemess.tcc.fintrackapp.adapter

import android.view.View
import android.widget.TextView
import androidx.databinding.BindingAdapter

@BindingAdapter(value = ["textOrHide"])
fun TextView.textOrHide(textOrHide:String?){
    if (textOrHide != null){
        text = textOrHide
    } else {
        visibility = View.GONE
    }
}