package com.example.practica9.presentation

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import com.example.practica9.R

// Usamos Activity estándar para evitar líos de dependencias en Wear OS básico
class MainActivity : Activity(), SensorEventListener {

    private lateinit var sensorManager: SensorManager
    private var stepSensor: Sensor? = null

    private var pasosSimulados = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // 1. Permisos para API 29+ (Android 10/11 en Wear OS)
        if (checkSelfPermission(Manifest.permission.ACTIVITY_RECOGNITION) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(arrayOf(Manifest.permission.ACTIVITY_RECOGNITION), 1)
        }

        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        stepSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
        //stepSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER)

        if (stepSensor == null) {
            // Esto saldrá si el emulador no logra simularlo ni con rutas
            Toast.makeText(this, "Sensor de pasos no disponible", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onResume() {
        super.onResume()
        stepSensor?.let {
            sensorManager.registerListener(this, it, SensorManager.SENSOR_DELAY_UI)
        }
    }

    override fun onPause() {
        super.onPause()
        sensorManager.unregisterListener(this)
    }

    override fun onSensorChanged(event: SensorEvent?) {
        if (event?.sensor?.type == Sensor.TYPE_ACCELEROMETER) {
            val x = event.values[0]
            val y = event.values[1]
            val z = event.values[2]

            // Calculamos la magnitud total del movimiento
            // La gravedad normal es ~9.8.
            val magnitud = Math.sqrt((x*x + y*y + z*z).toDouble())

            // Si la magnitud supera 12, significa que hubo un "sacudón" (un paso)
            if (magnitud > 12) {
                pasosSimulados++

                // Actualizamos el texto
                try {
                    val textView = findViewById<TextView>(R.id.tvSteps)
                    if (textView != null) {
                        textView.text = "Pasos: $pasosSimulados"
                    }
                } catch (e: Exception) {
                    // Ignorar error
                }

                Log.d("WEAR_OS", "¡Paso detectado! Magnitud: $magnitud")
            }
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}
}