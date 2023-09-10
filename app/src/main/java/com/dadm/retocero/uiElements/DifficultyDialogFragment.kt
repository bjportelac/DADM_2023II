package com.dadm.retocero.uiElements

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import com.dadm.retocero.R

class DifficultyDialogFragment(private val selected: Int, private val onItemSelected: (Int) -> Unit): DialogFragment(){
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(activity)
        val levels = arrayOf(
            getString(R.string.difficulty_easy),
            getString(R.string.difficulty_hard),
            getString(R.string.difficulty_expert)
        )

        builder.setTitle(R.string.difficulty_choose)
            .setSingleChoiceItems(levels,selected){ dialog, item ->
                dialog.dismiss()
                onItemSelected(item)
                Toast.makeText(context,levels[item], Toast.LENGTH_SHORT).show()
            }

        builder.setPositiveButton(android.R.string.ok) { dialog, _ ->
            dialog.dismiss()
        }

        builder.setCancelable(false)

        val dialog = builder.create()

        dialog.listView?.setItemChecked(selected, true)

        return dialog
    }
}