package com.arraigntech.mobioticstask

import android.content.DialogInterface
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.arraigntech.mobioticstask.databinding.ActivityDetailPageBinding
import com.arraigntech.mobioticstask.ui.detailpage.DetailViewModel
import com.arraigntech.mobioticstask.ui.detailpage.Video
import com.arraigntech.mobioticstask.ui.detailpage.VideoDataBase
import com.arraigntech.mobioticstask.ui.homeActivity.HomeItem
import com.arraigntech.mobioticstask.utils.Constants.Companion.LIST_STR
import com.arraigntech.mobioticstask.utils.Constants.Companion.USER
import com.google.android.exoplayer2.*
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory
import com.google.android.exoplayer2.source.ExtractorMediaSource
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector
import com.google.android.exoplayer2.upstream.DataSource
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory
import com.google.gson.Gson
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch


class DetailPageActivity : AppCompatActivity(), Player.EventListener {
    lateinit var binding: ActivityDetailPageBinding
    private lateinit var simpleExoplayer: SimpleExoPlayer
    lateinit var homeItem: HomeItem
    private lateinit var modelView: DetailViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_detail_page)
        modelView = ViewModelProvider(this).get(DetailViewModel::class.java)
        binding.model = modelView
        modelView.livData.observe(this, Observer {
            if (it) {
                onBackPressed()
            }
        })
        if (intent.getStringExtra(LIST_STR) != null) {
            val str = intent.getStringExtra(LIST_STR)
            val lists = Gson().fromJson(str, Array<HomeItem>::class.java).asList()
            val id = intent.getStringExtra(USER)
            for (x in lists) {
                if (x.id == id) {
                    homeItem = x
                    binding.item = homeItem
                    break;
                }
            }
            binding.recyclerRV.adapter = DetailItemAdapter(lists, homeItem.id)
            initialize()
            fetchLocalData()
        }
    }

    fun fetchLocalData() {
        lifecycleScope.launch {
            baseContext?.let {
                val obj = VideoDataBase(it).objDao().getItem(homeItem.id.toInt())
                if (obj != null) {
                    simpleExoplayer.playWhenReady = false
                    alertDialog(obj)
                }

            }
        }
    }

    fun alertDialog(obj: Video) {
        AlertDialog.Builder(this)
            .setTitle("Play Video")
            .setMessage("Resume your video from where you left")
            .setCancelable(false)
            .setPositiveButton("YES", DialogInterface.OnClickListener { dialog, which ->
                simpleExoplayer.seekTo(obj.continueVideo)
                simpleExoplayer.playWhenReady = true
                dialog.dismiss()

            })
            .setNegativeButton("NO", DialogInterface.OnClickListener { dialog, which ->
                simpleExoplayer.seekTo(0)
                simpleExoplayer.playWhenReady = true
                dialog.dismiss()
            })
            .show()
    }

    fun initialize() {
        val uri = Uri.parse(homeItem.url)
        simpleExoplayer = ExoPlayerFactory.newSimpleInstance(
            this,
            DefaultRenderersFactory(this),
            DefaultTrackSelector(),
            DefaultLoadControl()
        )
        val factory = DefaultHttpDataSourceFactory("exoplayer_video")
        val extractorsFactory = DefaultExtractorsFactory()
        val mediaSource = ExtractorMediaSource(uri, factory, extractorsFactory, null, null)
        binding.videoView.player = simpleExoplayer
        binding.videoView.keepScreenOn = true
        simpleExoplayer.prepare(mediaSource)
        simpleExoplayer.playWhenReady = true
        simpleExoplayer.addListener(this)

    }


    override fun onPlayerStateChanged(playWhenReady: Boolean, playbackState: Int) {
        when (playbackState) {
            Player.STATE_BUFFERING -> {
            }//show progress bar

            Player.STATE_READY -> {
            }//hide progress bar
        }

    }


    override fun onPause() {
        super.onPause()
        simpleExoplayer.playWhenReady = false
        simpleExoplayer.playbackState
        insertItem(simpleExoplayer.currentPosition, simpleExoplayer.contentDuration)
    }

    override fun onRestart() {
        super.onRestart()
        simpleExoplayer.playWhenReady = true
        simpleExoplayer.playbackState
    }

    private fun insertItem(currentDur: Long, totalDuration: Long) {
        GlobalScope.launch {
            val obj = Video(
                homeItem.id.toInt(),
                currentDur,
                totalDuration
            )
            baseContext?.let {
                VideoDataBase(it).objDao().insert(obj)
            }
        }
    }
}