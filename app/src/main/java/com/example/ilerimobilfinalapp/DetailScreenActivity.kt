package com.example.ilerimobilfinalapp

import android.os.Build
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.ilerimobilfinalapp.app.MovieEntity

class DetailScreenActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_screen)

        val data = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getParcelableExtra("data", MovieEntity::class.java)
        } else {
            intent.getParcelableExtra("data")
        }

        if (data != null) {
            Glide.with(this)
                .load(data.poster)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(findViewById(R.id.image))

            findViewById<TextView>(R.id.title).text = "${data.id}: ${data.name}"
            findViewById<TextView>(R.id.time).text = "${data.release}, ${data.playtime}"

            findViewById<TextView>(R.id.info).text = data.description
            findViewById<TextView>(R.id.plot).text = data.plot
        }
    }
}