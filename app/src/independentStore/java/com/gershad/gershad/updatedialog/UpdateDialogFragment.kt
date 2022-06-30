package com.gershad.gershad.savedlocationsdeletedialog

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.widget.Toast
import com.gershad.gershad.BaseApplication
import com.gershad.gershad.R
import com.gershad.gershad.dependency.GershadPreferences
import com.gershad.gershad.dependency.Module
import com.gershad.gershad.service.UpdateDownloadIntentService
import kotlinx.android.synthetic.independentStore.fragment_update_dialog.*
import space.traversal.kapsule.Injects
import space.traversal.kapsule.inject
import space.traversal.kapsule.required

/**
 * Saved locations view.
 */
class UpdateDialogFragment : DialogFragment(), Injects<Module> {

    private val gershadSettings: GershadPreferences by required { preferences }


    companion object {
        fun newInstance() = UpdateDialogFragment()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_update_dialog, container, false)
        setCancelable(false)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        inject(BaseApplication.module(context!!))
        gershadSettings.updateNotificationDate = System.currentTimeMillis()

        dialog.window?.setLayout(MATCH_PARENT, MATCH_PARENT)

        download_update.setOnClickListener {
            context?.startService(Intent(activity, UpdateDownloadIntentService::class.java))
            Toast.makeText(context, R.string.download_in_progress, Toast.LENGTH_LONG).show()
            dismiss()
        }

        cancel_update.setOnClickListener {
            dismiss()
        }

    }

    override fun onStart() {
        super.onStart()
        dialog?.let { it.window!!.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT) }
    }
}