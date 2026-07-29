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
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

/**
 * Main screen for the Android demo app.
 *
 * It stores server settings, connects to ShowTrak, and registers four actions
 * that change the color box plus one that demonstrates progress feedback.
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
    private lateinit var progressLabel: TextView

    private var connected = false

    // Runs slow event handlers off the SDK's callback thread.
    private val worker: ExecutorService = Executors.newSingleThreadExecutor()

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
        progressLabel = findViewById(R.id.progressLabel)

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

    /**
     * Demonstrates [Ack.feedback] and nothing else. It makes five calls, one a
     * second, and each message says exactly which call it is — there is no
     * pretend workload here, and the sleep exists only to space the messages
     * out far enough to watch them land in ShowTrak's execution view.
     *
     * It runs on a background thread because holding the SDK's callback thread
     * for five seconds would stall its heartbeats.
     */
    private fun registerFeedbackDemoEvent() {
        val options = EventOptions(
            label = "Feedback Demo",
            colour = 5,
            hasFeedback = true,
            icon = "broadcast",
            // Comfortably longer than the five calls; drop it below 5000 to
            // watch the ack resolve as RESOLVED_TIMEOUT instead.
            timeoutMs = 20_000,
        )
        ShowTrak.registerEvent("FeedbackDemo", options) { ack: Ack ->
            worker.execute {
                try {
                    for (call in 1..FEEDBACK_CALLS) {
                        // Something else may have resolved this ack already (the
                        // timeout, most likely) — stop rather than report on.
                        if (ack.isResolved()) {
                            showProgress("Stopped early: ack is ${ack.getStatus()}")
                            return@execute
                        }
                        Thread.sleep(1000)
                        val message = "ack.feedback() call $call of $FEEDBACK_CALLS"
                        ack.feedback(message)
                        showProgress(message)
                    }
                    ack.success()
                    showProgress("ack.success() sent — ack is ${ack.getStatus()}")
                } catch (e: InterruptedException) {
                    Thread.currentThread().interrupt()
                    ack.error("Interrupted before ack.success()")
                }
            }
        }
    }

    private fun showProgress(message: String) {
        runOnUiThread { progressLabel.text = message }
    }

    private fun registerDemoEvents() {
        registerFeedbackDemoEvent()
        // Each event names a Bootstrap Icons glyph, shown beside it in the
        // ShowTrak menu and tinted with the event's colour. The three colour
        // events share a plain filled circle so the colour is what tells them
        // apart; Reset Box gets its own glyph and colour.
        registerColourEvent("SetBoxRed", "Set Box Red", colour = 0, hex = "#e74c3c", icon = "circle-fill")
        registerColourEvent("SetBoxGreen", "Set Box Green", colour = 3, hex = "#2ecc71", icon = "circle-fill")
        registerColourEvent("SetBoxBlue", "Set Box Blue", colour = 4, hex = "#3498db", icon = "circle-fill")
        registerColourEvent(
            "ResetBox",
            "Reset Box",
            colour = 1,
            hex = DEFAULT_BOX_COLOUR,
            icon = "arrow-counterclockwise",
        )
    }

    private fun registerColourEvent(id: String, label: String, colour: Int, hex: String, icon: String?) {
        val options = EventOptions(label = label, colour = colour, hasFeedback = true, icon = icon)
        ShowTrak.registerEvent(id, options) { ack: Ack ->
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
        worker.shutdownNow()
        super.onDestroy()
    }

    companion object {
        private const val FEEDBACK_CALLS = 5

        // Matches the box's starting colour in activity_main.xml.
        private const val DEFAULT_BOX_COLOUR = "#7f8c8d"
        private const val PREFS = "showtrak_demo"
        private const val KEY_IP = "ip"
        private const val KEY_PORT = "port"
        private const val KEY_ID = "id"
    }
}
