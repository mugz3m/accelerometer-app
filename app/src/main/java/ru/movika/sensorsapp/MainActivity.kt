package ru.movika.sensorsapp

import android.os.Bundle
import android.widget.RelativeLayout
import androidx.appcompat.app.AppCompatActivity
import ru.movika.sensorsapp.usecases.Gyroscope


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val rootView = findViewById<RelativeLayout>(R.id.container)

        val gyroscope = Gyroscope(this)
        rootView.addView(gyroscope.getView())
    }
}
