package net.squanchy.settings

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_wifi_config_error.*
import net.squanchy.R

class WifiConfigErrorActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_wifi_config_error)

        textSsid.text = intent.getSerializableExtra(EXTRA_WIFI_SSID).toString()
        textPassword.text = intent.getSerializableExtra(EXTRA_WIFI_PASSWORD).toString()
    }

    companion object {

        private val EXTRA_WIFI_SSID = WifiConfigErrorActivity::class.java.canonicalName + ".wifi_ssid"
        private val EXTRA_WIFI_PASSWORD = WifiConfigErrorActivity::class.java.canonicalName + ".wifi_password"

        fun createIntent(context: Context, ssid: String, password: String) =
            Intent(context, WifiConfigErrorActivity::class.java).apply {
                putExtra(EXTRA_WIFI_SSID, ssid)
                putExtra(EXTRA_WIFI_PASSWORD, password)
            }
    }
}
