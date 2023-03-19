package com.xwk.someview

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.xwk.someview.password.CustomPassword
import com.xwk.someview.password.PasswordEditText

class MainActivity : AppCompatActivity() {

    private lateinit var customPassword: PasswordEditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        customPassword = findViewById(R.id.psd)
    }
}