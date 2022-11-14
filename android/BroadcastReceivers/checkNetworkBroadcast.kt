package com.elzaman.android.BroadcastReceivers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AlertDialog
import com.elzaman.android.Utils.connectivity
import com.google.android.gms.measurement.sdk.api.R
import com.google.android.material.button.MaterialButton
import com.google.android.material.card.MaterialCardView

class checkNetworkBroadcast : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        if (!connectivity.iscoonectedToTheInternet(context!!)) {

            val alertDialogueBuilder = AlertDialog.Builder(context)
            val dialog: View = LayoutInflater.from(context)
                .inflate(com.elzaman.android.zamangameel.R.layout.nointernet_layout, null)
            alertDialogueBuilder.setView(dialog)
            val dialogue = alertDialogueBuilder.create()
            val button:Button = dialog.findViewById(com.elzaman.android.zamangameel.R.id.buttonretry)
            dialogue.show()
            dialogue.setCancelable(false)
            dialogue.window?.setGravity(Gravity.CENTER)
            dialogue.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            button.setOnClickListener {


                dialogue.dismiss()
                onReceive(context, intent)
            }
        }
    }
}