package wisp.filimoshka.accelerometer

import android.animation.AnimatorInflater
import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.content.Context
import android.content.res.Resources
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.LinearLayout
import android.widget.TextView

class MainActivity : AppCompatActivity() {

    lateinit var sManager: SensorManager

    private var magnetic = FloatArray(9)
    private var gravity = FloatArray(9)
    private var acclr = FloatArray(3)
    private var magnfield = FloatArray(3)
    private var values = FloatArray(3)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val tvSensor = findViewById<TextView>(R.id.tvSensor)
        val lRotation = findViewById<LinearLayout>(R.id.lRotation)

        sManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        val sensor = sManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
        val sensor2 = sManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD)
        val sListener = object : SensorEventListener{
            @SuppressLint("SetTextI18n")
            override fun onSensorChanged(event: SensorEvent?) {
//                val value = event?.values
//                val xValue = value?.get(0)
//                val yValue = value?.get(1)
//                val zValue = value?.get(2)
//                val sData = "X: $xValue\nY: $yValue\nZ: $zValue"
//                tvSensor.text = sData
                when (event?.sensor?.type) {
                    Sensor.TYPE_ACCELEROMETER-> acclr = event.values.clone()
                    Sensor.TYPE_MAGNETIC_FIELD-> magnfield = event.values.clone()
                }

                SensorManager.getRotationMatrix(gravity, magnetic, acclr, magnfield)
                val outGravity = FloatArray(9)
                SensorManager.remapCoordinateSystem(gravity,
                    SensorManager.AXIS_X,
                    SensorManager.AXIS_Z,
                    outGravity
                )
                SensorManager.getOrientation(outGravity, values)
                val degree = values[2] * 57.2957f
                val rotate = 270 + degree
                lRotation.rotation = rotate


                val rData = (90+degree).toInt()

                val resources: Resources = resources
                val color = if (rData == 0) {
                    resources.getColor(R.color.green, null)
                } else {
                    resources.getColor(R.color.red, null)
                }

                tvSensor.text = "$rData°"
                lRotation.setBackgroundColor(color)


            }

            override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {

            }

        }
        sManager.registerListener(sListener, sensor, SensorManager.SENSOR_DELAY_NORMAL)
        sManager.registerListener(sListener, sensor2, SensorManager.SENSOR_DELAY_NORMAL)
    }
}