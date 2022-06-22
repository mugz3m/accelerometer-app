package ru.movika.sensorsapp.usecases

import android.content.ContentValues.TAG
import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.LinearLayoutCompat
import ru.movika.sensorsapp.R
import ru.movika.sensorsapp.data.SensorData


class Gyroscope(context: Context) : SensorEventListener {

    private val parentContext: Context = context
    private val view: LinearLayoutCompat =
        View.inflate(parentContext, R.layout.gyroscope_layout, null) as LinearLayoutCompat
    private val ball: AppCompatImageView = view.findViewById(R.id.gyroscope_ball)
    private val pane: AppCompatImageView = view.findViewById(R.id.gyroscope_pane)

    private lateinit var sensorManager: SensorManager
    private lateinit var sensorAccelerometer: Sensor

    private var accelerationData: SensorData? = null

    companion object {
        private const val BALL_SPEED = 5
    }

    init {
        initSensors()
        registerListener()
    }

    fun getView(): View = view

    private fun initSensors() {
        sensorManager =
            parentContext.getSystemService(AppCompatActivity.SENSOR_SERVICE) as SensorManager
        if (sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER) != null) {
            sensorAccelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
        } else {
            Log.e(TAG, "Default accelerometer sensor unavailable.")
        }
    }

    private fun registerListener() {
        if (sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER) != null) {
            sensorManager.registerListener(this, sensorAccelerometer,
                SensorManager.SENSOR_DELAY_NORMAL)
        }
    }

    private fun unregisterListener() {
        if (sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER) != null) {
            sensorManager.unregisterListener(this, sensorAccelerometer)
        }
    }

    override fun onSensorChanged(event: SensorEvent?) {
        if(event!!.sensor.type == Sensor.TYPE_ACCELEROMETER) {
            getAccelerationData(event)
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) = Unit

    private fun getAccelerationData(event: SensorEvent?) {
        if (accelerationData == null) {
            accelerationData =
                SensorData(event!!.values[0], event.values[1], event.values[2],event.timestamp)
        } else {
            accelerationData!!.x1 = event!!.values[0]
            accelerationData!!.x2 = event.values[1]
            accelerationData!!.x3 = event.values[2]
        }

        with(ball) {
            if (x < pane.left + width) x = (pane.left + width).toFloat()
            if (x > pane.right - width) x = (pane.right - width).toFloat()
            if (x >= pane.left + width && x <= pane.right - width)
                x += accelerationData!!.x2 * BALL_SPEED
        }
    }
}
