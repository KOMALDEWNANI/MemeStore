package com.example.memestore

import android.content.Intent
import android.content.Intent.ACTION_SEND
import android.graphics.drawable.Drawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.example.memestore.databinding.ActivityMainBinding
var ImageUrl : String? = null

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        setContentView(R.layout.activity_main)
        LoadMeme()

    }
    private fun LoadMeme(){
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        binding.progressBar.visibility = View.VISIBLE



        val url = "https://meme-api.herokuapp.com/gimme"



        val jsonObjectRequest = JsonObjectRequest(
                Request.Method.GET, url, null,
                Response.Listener{ response ->
                    ImageUrl = response.getString("url")

                    Glide.with(this).load(ImageUrl).listener(
                            object : RequestListener<Drawable> {
                                override fun onLoadFailed(e: GlideException?, model: Any?, target: Target<Drawable>?, isFirstResource: Boolean): Boolean {
                                    binding.progressBar.visibility = View.GONE
                                    return false
                                }

                                override fun onResourceReady(resource: Drawable?, model: Any?, target: Target<Drawable>?, dataSource: DataSource?, isFirstResource: Boolean): Boolean {
                                    binding.progressBar.visibility = View.GONE
                                    return false

                                }
                            },
                    ).into(binding.MemeImage)
                },
                Response.ErrorListener {
                    Toast.makeText(this, "Something went wrong", Toast.LENGTH_LONG).show()

                    binding.progressBar.visibility = View.GONE
                }
        )
        MySingleton.getInstance(this).addToRequestQueue(jsonObjectRequest)

    }
    fun Share(view: View) {
        val intent = Intent(Intent.ACTION_SEND)
        intent.putExtra(Intent.EXTRA_TEXT,"Hey ! Checkout this cool meme I found on MemeStore App $ImageUrl ")
        intent.setType("text/plain")
        val chooser = Intent.createChooser(intent, "Share this meme using...")
        startActivity(chooser)

    }
    fun Next(view: View) {
        LoadMeme()
    }
}