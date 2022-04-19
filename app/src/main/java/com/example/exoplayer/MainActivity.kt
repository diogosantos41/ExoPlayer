package com.example.exoplayer

import android.os.Bundle
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import com.example.exoplayer.databinding.ActivityMainBinding
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.PlaybackException
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.source.MediaSource
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.upstream.DataSource
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.google.android.exoplayer2.util.Util
import com.google.android.material.snackbar.Snackbar

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var exoPlayer: SimpleExoPlayer
    private lateinit var mediaSource: MediaSource

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)

        initExoPlayer()
    }

    override fun onResume() {
        super.onResume()

        exoPlayer.playWhenReady = true
        exoPlayer.play()
    }

    override fun onPause() {
        super.onPause()

        exoPlayer.pause()
        exoPlayer.playWhenReady = false
    }

    override fun onDestroy() {
        super.onDestroy()

        exoPlayer.pause()
        exoPlayer.playWhenReady = false

        window.clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
    }


    private fun initExoPlayer() {
        exoPlayer = SimpleExoPlayer.Builder(this).build()
        exoPlayer.addListener(getPlayerListener())

        binding.playerView.player = exoPlayer

        initMediaSource()

        exoPlayer.setMediaSource(mediaSource)
        exoPlayer.prepare()
    }

    private fun getPlayerListener(): Player.Listener {
        var playerListener = object : Player.Listener {
            override fun onRenderedFirstFrame() {
                super.onRenderedFirstFrame()
                binding.playerView.useController = true
            }

            override fun onPlayerError(error: PlaybackException) {
                super.onPlayerError(error)
                Snackbar.make(
                    binding.root,
                    "Ups! Error message: " + error.message,
                    Snackbar.LENGTH_LONG
                ).show()
            }
        }
        return playerListener
    }

    private fun initMediaSource() {
        exoPlayer.seekTo(0)

        val dataSourceFactory: DataSource.Factory = DefaultDataSourceFactory(
            this,
            Util.getUserAgent(this, applicationInfo.name)
        )

        mediaSource = ProgressiveMediaSource.Factory(dataSourceFactory)
            .createMediaSource(MediaItem.fromUri("http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ElephantsDream.mp4"))
    }
}