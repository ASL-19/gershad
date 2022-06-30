package com.gershad.gershad.dialogs

import android.os.Bundle
import com.gershad.gershad.R
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.DialogFragment

class GershadOneButtonAlert (private val title: String, private val description: String?, private val buttonCaption: String) : DialogFragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        dialog!!.window?.setBackgroundDrawableResource(R.drawable.rounded_bg_white);
        var view = inflater.inflate(R.layout.gershad_one_button_alert_layout, container, false)

        view.findViewById<TextView>(R.id.title).setText(title)

        if (description != null) {
            view.findViewById<TextView>(R.id.description).setText(description)
        } else {
            view.findViewById<TextView>(R.id.description).setVisibility(View.GONE)
        }

        view.findViewById<Button>(R.id.btn_action).setOnClickListener({ getDialog().dismiss()})
        view.findViewById<Button>(R.id.btn_action).setText(buttonCaption)

        return view
    }

    override fun onStart() {
        super.onStart()
        val width = (resources.displayMetrics.widthPixels * 0.75).toInt()
        dialog!!.window?.setLayout(width, ViewGroup.LayoutParams.WRAP_CONTENT)
    }

    companion object {
        fun newInstance(title: String, description: String?, buttonCaption: String): GershadOneButtonAlert {
            return GershadOneButtonAlert(title, description, buttonCaption)
        }
    }
}