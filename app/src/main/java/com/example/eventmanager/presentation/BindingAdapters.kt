package com.example.eventmanager.presentation

import androidx.databinding.BindingAdapter
import com.example.eventmanager.R
import com.google.android.material.textfield.TextInputLayout

@BindingAdapter("errorInputName")
fun bindErrorInputName(textInputLayout: TextInputLayout, isError: Boolean) {
    val message = if (isError) {
        textInputLayout.context.getString(R.string.error_input_name)
    } else {
        null
    }
    textInputLayout.error = message
}

@BindingAdapter("errorInputDate")
fun bindErrorInputDate(textInputLayout: TextInputLayout, isError: Boolean) {
    val message = if (isError) {
        textInputLayout.context.getString(R.string.error_input_date)
    } else {
        null
    }
    textInputLayout.error = message
}

@BindingAdapter("errorInputCity")
fun bindErrorInputCity(textInputLayout: TextInputLayout, isError: Boolean) {
    val message = if (isError) {
        textInputLayout.context.getString(R.string.error_input_city)
    } else {
        null
    }
    textInputLayout.error = message
}

