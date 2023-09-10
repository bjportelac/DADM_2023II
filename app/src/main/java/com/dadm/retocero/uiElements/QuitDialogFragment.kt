package com.dadm.retocero.uiElements

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import com.dadm.retocero.R

class QuitDialogFragment: DialogFragment() {
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val builder = AlertDialog.Builder(it)
            builder.setMessage(R.string.quit_question)
                .setCancelable(false)
                .setNegativeButton(R.string.no, null)
                .setPositiveButton(R.string.yes){ _,_ -> activity?.finish()}

            builder.create()
        }?: throw IllegalStateException("Activity cannot be null")
    }
}