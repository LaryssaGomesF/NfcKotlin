package com.example.nfckotlin

import android.app.Activity
import android.app.PendingIntent
import android.content.Intent
import android.content.IntentFilter
import android.nfc.NfcAdapter
import android.nfc.tech.NfcF
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView


class DispatcherTagActivity: Activity() {
    private var tvMensagem: TextView? = null

    private var mNfcAdapter: NfcAdapter? = null
    private var mPendingIntent: PendingIntent? = null
    private var mIntentFilters: Array<IntentFilter>? = null
    private var mNFCTechLists: Array<Array<String>>? = null

    override fun onCreate(savedState: Bundle?) {
        super.onCreate(savedState)
        setContentView(R.layout.activity_dispatcher_tag)
        tvMensagem = findViewById(R.id.tvMensagem)
        mNfcAdapter = NfcAdapter.getDefaultAdapter(this)
        if (mNfcAdapter != null) {
            tvMensagem!!.text = "Toque em Beam Data no outro dispositivo"
        } else {
            tvMensagem!!.text = "NFC ausente no device..."
        }
        mPendingIntent = PendingIntent.getActivity(
            this, 0, Intent(
                this,
                javaClass
            ).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), PendingIntent.FLAG_IMMUTABLE
        )
        val ndefIntent = IntentFilter(
            NfcAdapter.ACTION_NDEF_DISCOVERED
        )
        try {
            ndefIntent.addDataType("*/*")
            mIntentFilters = arrayOf(ndefIntent)
        } catch (e: Exception) {
            Log.e("TagDispatch", e.toString())
        }
        mNFCTechLists = arrayOf(arrayOf(NfcF::class.java.name))
    }
}