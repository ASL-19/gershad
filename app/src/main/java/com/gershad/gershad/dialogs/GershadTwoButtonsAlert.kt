package com.gershad.gershad.dialogs

import android.os.Bundle
import com.gershad.gershad.R
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.DialogFragment

class GershadTwoButtonsAlert (private val title: String, private val description: String?, private val image: Int?, private val actionButtonCaption: String, private val onClickListener:View.OnClickListener) : DialogFragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        dialog!!.window?.setBackgroundDrawableResource(R.drawable.rounded_bg_white);
        var view = inflater.inflate(R.layout.gershad_two_button_alert_layout, container, false)

        view.findViewById<TextView>(R.id.title).setText(title)

        if (image != null) {
            view.findViewById<ImageView>(R.id.image).setBackgroundResource(image)
        } else {
            view.findViewById<ImageView>(R.id.image).setVisibility(View.GONE)
        }

        if (description != null) {
            view.findViewById<TextView>(R.id.description).setText(description)
        } else {
            view.findViewById<TextView>(R.id.description).setVisibility(View.GONE)
        }

        view.findViewById<Button>(R.id.btn_action).setOnClickListener {
            onClickListener.onClick(it)
            getDialog().dismiss()
        }
        view.findViewById<Button>(R.id.btn_action).setText(actionButtonCaption)

        view.findViewById<Button>(R.id.btn_cancel).setOnClickListener({ getDialog().dismiss()})

        return view
    }

    override fun onStart() {
        super.onStart()
        val width = (resources.displayMetrics.widthPixels * 0.75).toInt()
        dialog!!.window?.setLayout(width, ViewGroup.LayoutParams.WRAP_CONTENT)
    }

    companion object {
        fun newInstance(title: String, description: String?, image: Int?, actionButtonCaption: String, onClickListener:View.OnClickListener): GershadTwoButtonsAlert {
            return GershadTwoButtonsAlert(title, description, image, actionButtonCaption, onClickListener)
        }
    }
}