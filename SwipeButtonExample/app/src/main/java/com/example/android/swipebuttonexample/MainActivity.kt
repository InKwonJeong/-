package com.example.android.swipebuttonexample

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        character_button.rightSwipeListener = {
            Toast.makeText(this, "Right Swipe", Toast.LENGTH_SHORT).show()
        }
        character_button.leftSwipeListener = {
            Toast.makeText(this, "Left Swipe", Toast.LENGTH_SHORT).show()
        }
    }
}
