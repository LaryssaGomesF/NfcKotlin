package com.example.nfckotlin


import android.app.Activity
import android.nfc.NdefMessage
import android.nfc.NdefRecord
import android.nfc.NfcAdapter
import android.os.Bundle
import android.view.View
import android.widget.TextView
import java.nio.charset.Charset
import java.util.*


class BeamDataActivity: Activity() {

    private var tvMensagem: TextView? = null
    private var mNfcAdapter: NfcAdapter? = null
    private var mNdefMessage: NdefMessage? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_beam_data)
        tvMensagem = findViewById<TextView>(R.id.tvMensagem)
        mNfcAdapter = NfcAdapter.getDefaultAdapter(this)
        if (mNfcAdapter != null) {
            tvMensagem!!.text = "Toque em Beam Data no outro dispositivo"
        } else {
            tvMensagem!!.text = "NFC ausente no device..."
        }
        mNdefMessage = NdefMessage(
            arrayOf(
                criarRegistroTexto(
                    "Um texto qualquer enviado para o outro device!",
                    Locale.ENGLISH, true
                ),
                criarRegistroTexto(
                    "Segundo texto qualquer enviado ao outro device!",
                    Locale.ENGLISH, true
                )
            )
        )
    }

    fun criarRegistroTexto(
        text: String, locale: Locale,
        encodeInUtf8: Boolean
    ): NdefRecord {
        val langBytes = locale.language.toByteArray(Charset.forName("US-ASCII"))
        val utfEncoding = if (encodeInUtf8) Charset.forName("UTF-8") else Charset.forName("UTF-16")
        val textBytes = text.toByteArray(utfEncoding)
        val utfBit = if (encodeInUtf8) 0 else 1 shl 7
        val status = (utfBit + langBytes.size).toChar()
        val data = ByteArray(1 + langBytes.size + textBytes.size)
        data[0] = status.code.toByte()
        System.arraycopy(langBytes, 0, data, 1, langBytes.size)
        System.arraycopy(textBytes, 0, data, 1 + langBytes.size, textBytes.size)
        return NdefRecord(NdefRecord.TNF_WELL_KNOWN, NdefRecord.RTD_TEXT, ByteArray(0), data)
    }

    override fun onResume() {
        super.onResume()
        if (mNfcAdapter != null) mNfcAdapter!!.enableForegroundNdefPush(this, mNdefMessage)
    }

    override fun onPause() {
        super.onPause()
        if (mNfcAdapter != null) mNfcAdapter!!.disableForegroundNdefPush(this)
    }

}