package io.showtrak.sample

import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.CompoundButton
import android.widget.EditText
import android.widget.Switch
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import io.showtrak.sdk.Ack
import io.showtrak.sdk.ConnectionState
import io.showtrak.sdk.EventOptions
import io.showtrak.sdk.ShowTrak

/**
 * Main screen for the Android demo app.
 *
 * It stores server settings, connects to ShowTrak, and registers
 * three simple actions that change the color box.
 */
class MainActivity : AppCompatActivity() {

    private lateinit var ipInput: EditText
    private lateinit var portInput: EditText
    private lateinit var idInput: EditText
    private lateinit var connectBtn: Button
    private lateinit var statusText: TextView
    private lateinit var clientIdText: TextView
    private lateinit var colourBox: View
    private lateinit var degradedSwitch: Switch

    private var connected = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        ipInput = findViewById(R.id.ipInput)
        portInput = findViewById(R.id.portInput)
        idInput = findViewById(R.id.idInput)
        connectBtn = findViewById(R.id.connectBtn)
        statusText = findViewById(R.id.statusText)
        clientIdText = findViewById(R.id.clientIdText)
        colourBox = findViewById(R.id.colourBox)
        degradedSwitch = findViewById(R.id.degradedSwitch)

        restoreSettings()

        connectBtn.setOnClickListener { if (connected) stop() else start() }

        degradedSwitch.setOnCheckedChangeListener { _: CompoundButton, isChecked: Boolean ->
            if (!connected) return@setOnCheckedChangeListener
            if (isChecked) ShowTrak.setState("DEGRADED", "Custom Status")
            else ShowTrak.setState("ONLINE")
        }
    }

    private fun start() {
        val ip = ipInput.text.toString().trim()
        val port = portInput.text.toString().trim().toIntOrNull()
        val id = idInput.text.toString().trim().ifBlank { null }
        if (ip.isEmpty() || port == null) {
            statusText.text = "Status: Enter a valid Server IP and Port"
            return
        }
        saveSettings(ip, port, id)

        ShowTrak.connect(this, ip, port, id)
        registerDemoEvents()
        ShowTrak.onStateChanged { state -> runOnUiThread { renderState(state) } }

        connected = true
        connectBtn.text = "Disconnect"
        setInputsEnabled(false)
    }

    private fun stop() {
        ShowTrak.disconnect()
        connected = false
        connectBtn.text = "Connect"
        setInputsEnabled(true)
        statusText.text = "Status: Disconnected"
        degradedSwitch.isChecked = false
    }

    private fun registerDemoEvents() {
        registerColourEvent("SetBoxRed", "Set Box Red", colour = 0, hex = "#e74c3c")
        registerColourEvent("SetBoxGreen", "Set Box Green", colour = 3, hex = "#2ecc71")
        registerColourEvent("SetBoxBlue", "Set Box Blue", colour = 4, hex = "#3498db")
    }

    private fun registerColourEvent(id: String, label: String, colour: Int, hex: String) {
        ShowTrak.registerEvent(id, EventOptions(label = label, colour = colour, hasFeedback = true)) { ack: Ack ->
            runOnUiThread {
                colourBox.setBackgroundColor(Color.parseColor(hex))
                findViewById<TextView>(R.id.boxLabel).text = "Box is now: $label"
            }
            ack.success()
        }
    }

    private fun renderState(state: ConnectionState) {
        statusText.text = when (state) {
            ConnectionState.DISCONNECTED -> "Status: Disconnected"
            ConnectionState.CONNECTING -> "Status: Connecting…"
            ConnectionState.PENDING_ADOPTION -> "Status: Pending adoption — adopt this device in ShowTrak"
            ConnectionState.ONLINE -> "Status: Online"
        }
    }

    private fun setInputsEnabled(enabled: Boolean) {
        ipInput.isEnabled = enabled
        portInput.isEnabled = enabled
        idInput.isEnabled = enabled
    }

    private fun restoreSettings() {
        val prefs = getSharedPreferences(PREFS, MODE_PRIVATE)
        ipInput.setText(prefs.getString(KEY_IP, ""))
        portInput.setText(prefs.getString(KEY_PORT, ""))
        idInput.setText(prefs.getString(KEY_ID, ""))
    }

    private fun saveSettings(ip: String, port: Int, id: String?) {
        getSharedPreferences(PREFS, MODE_PRIVATE).edit()
            .putString(KEY_IP, ip)
            .putString(KEY_PORT, port.toString())
            .putString(KEY_ID, id ?: "")
            .apply()
    }

    override fun onDestroy() {
        ShowTrak.disconnect()
        super.onDestroy()
    }

    companion object {
        private const val PREFS = "showtrak_demo"
        private const val KEY_IP = "ip"
        private const val KEY_PORT = "port"
        private const val KEY_ID = "id"
    }
}
