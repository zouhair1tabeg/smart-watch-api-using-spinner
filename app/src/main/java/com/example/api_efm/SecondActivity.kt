package com.example.api_efm

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.Spinner
import android.widget.Switch
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class SecondActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_second)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val fullName = intent.getStringExtra("fullname")

        val infoTextView = findViewById<TextView>(R.id.info)
        infoTextView.text = "Bonjour ${fullName}"

        val spinner_watch = findViewById<Spinner>(R.id.sppiner)
        val price_txt = findViewById<TextView>(R.id.prix)
        val stock_switch = findViewById<Switch>(R.id.stock_switch)
        val watch_image = findViewById<ImageView>(R.id.imgV)

        val retrofit = Retrofit.Builder()
            .baseUrl("https://apiyes.net/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val apiService = retrofit.create(ApiService::class.java)

        apiService.getWatch().enqueue(object : Callback<List<Smart_Watch>> {
            override fun onResponse(call: Call<List<Smart_Watch>>, response: Response<List<Smart_Watch>>) {
                if (response.isSuccessful) {
                    val watches = response.body() ?: emptyList()

                    val watch_name = watches.map { it.name }
                    val adapter = ArrayAdapter(this@SecondActivity, android.R.layout.simple_spinner_item, watch_name)
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                    spinner_watch.adapter = adapter

                    spinner_watch.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                        override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {

                            val selectedWatch = watches[position]
                            price_txt.text = "Price: ${selectedWatch.price} MAD"

                            stock_switch.isChecked = selectedWatch.in_stock

                            Glide.with(this@SecondActivity)
                                .load(selectedWatch.image_url)
                                .into(watch_image)
                        }

                        override fun onNothingSelected(parent: AdapterView<*>) {
                            price_txt.text = ""
                            stock_switch.isChecked = false
                            watch_image.setImageDrawable(null)
                        }
                    }
                }
            }

            override fun onFailure(call: Call<List<Smart_Watch>>, t: Throwable) {
                Toast.makeText(this@SecondActivity, "Failed to load data: ${t.message}", Toast.LENGTH_LONG).show()
            }
        })
    }
}