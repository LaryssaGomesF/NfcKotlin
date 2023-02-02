package com.example.nfckotlin


import android.app.Activity
import android.app.PendingIntent
import android.content.Intent
import android.nfc.NfcAdapter
import android.nfc.Tag
import android.nfc.tech.*
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast


class MainActivity : Activity() {

    private var nfcAdapter: NfcAdapter? = null
    private lateinit var textView: TextView
    private var pendingIntent: PendingIntent? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        textView = findViewById(R.id.textView)
        // Check for available NFC Adapter
        nfcAdapter = NfcAdapter.getDefaultAdapter(this)
        if (nfcAdapter == null) {
            Toast.makeText(this, "NFC is not available", Toast.LENGTH_LONG).show()
            finish()
            return
        }
        // Register callback
        pendingIntent = PendingIntent.getActivity(
            this,
            0,
            Intent(this, MainActivity::class.java).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP),
            0
        )

        //  nfcAdapter?.setNdefPushMessageCallback(this, this)
    }


    override fun onResume() {
        super.onResume()
        assert(nfcAdapter != null)

        nfcAdapter?.enableForegroundDispatch(this, pendingIntent, null, null)
    }

    override fun onPause() {
        super.onPause()
        if (nfcAdapter != null) {
            nfcAdapter?.disableForegroundDispatch(this);
        }
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        intent?.let { getTagInfo(it) }
    }

    private fun getTagInfo(intent: Intent) {
        val tag = intent.getParcelableExtra<Tag>(NfcAdapter.EXTRA_TAG)
        val techList = tag!!.techList
        for (i in techList.indices) {
            if (techList[i] == MifareClassic::class.java.name) {
                val mifareClassicTag = MifareClassic.get(tag)
                when (mifareClassicTag.type) {
                    MifareClassic.TYPE_CLASSIC -> {}
                    MifareClassic.TYPE_PLUS -> {}
                    MifareClassic.TYPE_PRO -> {}
                }
            } else if (techList[i] == MifareUltralight::class.java.name) {
                //For Mifare Ultralight
                val mifareUlTag = MifareUltralight.get(tag)
                when (mifareUlTag.type) {
                    MifareUltralight.TYPE_ULTRALIGHT -> {}
                    MifareUltralight.TYPE_ULTRALIGHT_C -> {}
                }
            } else if (techList[i] == IsoDep::class.java.name) {
                // info[1] = "IsoDep";
                val isoDepTag = IsoDep.get(tag)
            } else if (techList[i] == Ndef::class.java.name) {
                Ndef.get(tag)
            } else if (techList[i] == NdefFormatable::class.java.name) {
                val ndefFormatableTag = NdefFormatable.get(tag)
            }
        }
    }
}

