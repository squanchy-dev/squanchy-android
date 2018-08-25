package net.squanchy.settings

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_wifi_config_error.*
import net.squanchy.R
import net.squanchy.remoteconfig.WifiConfiguration

class WifiConfigErrorActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_wifi_config_error)

        ssidValue.text = intent.getSerializableExtra(EXTRA_WIFI_SSID).toString()
        val password = intent.getSerializableExtra(EXTRA_WIFI_PASSWORD).toString()
        passwordValue.text = password
        copyPasswordButton.setOnClickListener {
            copyToClipboard(password)
            Toast.makeText(this, R.string.wifi_config_error_password_copied, Toast.LENGTH_SHORT).show()
        }
        buttonCancel.setOnClickListener { finish() }

        val component = wifiConfigErrorActivityComponent(this)
        val navigator = component.navigator()
        buttonSettings.setOnClickListener { navigator.toWifiSystemSettings(); finish() }
    }

    private fun copyToClipboard(password: String) {
        val clipboard = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clip = ClipData.newPlainText(COPIED_PASSWORD_LABEL, password)
        clipboard.primaryClip = clip
    }

    companion object {

        private val EXTRA_WIFI_SSID = "${WifiConfigErrorActivity::class.java.name}.wifi_ssid"
        private val EXTRA_WIFI_PASSWORD = "${WifiConfigErrorActivity::class.java.name}.wifi_password"
        private const val COPIED_PASSWORD_LABEL = "password"

        fun createIntent(context: Context, wifiConfiguration: WifiConfiguration) =
            Intent(context, WifiConfigErrorActivity::class.java).apply {
                putExtra(EXTRA_WIFI_SSID, wifiConfiguration.ssid)
                putExtra(EXTRA_WIFI_PASSWORD, wifiConfiguration.password)
            }
    }
}
