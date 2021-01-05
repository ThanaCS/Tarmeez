package com.thanaa.tarmeezapp

import android.content.Context
import androidx.appcompat.app.AlertDialog

class CustomProgressDialog(context: Context?) : AlertDialog(context!!) {
    override fun show() {
        super.show()
        setContentView(R.layout.loading)
    }
}