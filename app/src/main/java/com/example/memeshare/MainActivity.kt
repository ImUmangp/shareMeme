 package com.example.memeshare

import android.content.Intent
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import kotlinx.android.synthetic.main.activity_main.*

 class MainActivity : AppCompatActivity() {

     var currentImageurl: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        loadMeme()
    }
    private fun loadMeme () {
         progressBar.visibility = View.VISIBLE
        nextButton.isEnabled = false
        shareButton.isEnabled = false
        val queue = Volley.newRequestQueue(this)
        val url = "https://meme-api.herokuapp.com/gimme"


        val JsonObjectRequest = JsonObjectRequest(
            Request.Method.GET, url,null,
            Response.Listener{ response ->
                 currentImageurl = response.getString("url")


                 Glide.with(this).load(currentImageurl).listener(object: RequestListner<Drawable>,
                    RequestListener<Drawable> {
                    override fun onLoadFailed(
                        e: GlideException?,
                        model:Any?,
                        target: com.bumptech.glide.request.target.Target<Drawable>?,
                        dataSource: Boolean
                    ):Boolean{
                        progressBar.visibility = View.GONE
                        nextButton.isEnabled = true
                        return false
                    }

                    override fun onResourceReady(
                        resource: Drawable?,
                        model: Any?,
                        target: com.bumptech.glide.request.target.Target<Drawable>?,
                        dataSource: DataSource?,
                        isFirstResource: Boolean
                    ): Boolean {
                        progressBar.visibility = View.GONE
                        nextButton.isEnabled = true
                        shareButton.isEnabled = true
                        return false
                    }


                }).into(memeImageView)
            },
            Response.ErrorListener {
                Toast.makeText(this,"Something went wrong", Toast.LENGTH_LONG).show()
            })


        queue.add(JsonObjectRequest)
    }

    fun shareMeme(view: View) {
       val intent = Intent(Intent.ACTION_SEND)
        intent.type = "text/plain"
        intent.putExtra(Intent.EXTRA_TEXT, "hey,checkout this $currentImageurl")
        val chooser = Intent.createChooser(intent,"share this meme using....")
        startActivity(chooser)
    }
    fun nextMeme(view: View) {
        loadMeme()
    }
}