package com.example.nfckotlin


import android.content.Intent
import android.nfc.NfcAdapter
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity


class MainActivity : AppCompatActivity() {
    private var mNfcAdapter: NfcAdapter? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val btDispatcher = findViewById<View>(R.id.btDispatcher) as Button
        val btBeamData = findViewById<View>(R.id.btBeamData) as Button

        btDispatcher.setOnClickListener(View.OnClickListener { btDispatcherOnClick() })

        btBeamData.setOnClickListener(View.OnClickListener { btBeamDataOnClick() })
        mNfcAdapter = NfcAdapter.getDefaultAdapter(this)
        if (mNfcAdapter != null) {
            Toast.makeText(this, "NFC presente no device...", Toast.LENGTH_LONG).show()
        } else {
            Toast.makeText(this, "NFC ausente no device...", Toast.LENGTH_LONG).show()
        }
    }

    fun btDispatcherOnClick() {
        val intent = Intent(this, DispatcherTagActivity::class.java)
        startActivity(intent)
    }

    fun btBeamDataOnClick() {
        val intent = Intent(this, BeamDataActivity::class.java)
        startActivity(intent)
    }

}