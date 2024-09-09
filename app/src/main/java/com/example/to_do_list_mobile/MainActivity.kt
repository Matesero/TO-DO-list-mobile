package com.example.to_do_list_mobile

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        toolbar.setOnMenuItemClickListener {
            when(it.itemId) {
                R.id.download->{
                    Toast.makeText(this,"download", Toast.LENGTH_SHORT).show()
                    true
                }
                R.id.upload->{
                    Toast.makeText(this,"upload", Toast.LENGTH_SHORT).show()
                    true
                }
                R.id.reset->{
                    Toast.makeText(this,"reset", Toast.LENGTH_SHORT).show()
                    true
                }
                else->{
                    false
                }
            }
        }
    }
}