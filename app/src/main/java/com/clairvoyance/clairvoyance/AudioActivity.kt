package com.clairvoyance.clairvoyance

//import androidx.compose.setContent
import android.Manifest
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.view.ContextThemeWrapper
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import com.clairvoyance.clairvoyance.playback.AndroidAudioPlayer
import com.clairvoyance.clairvoyance.record.AndroidAudioRecorder
import java.io.File
import kotlin.reflect.KProperty0


class AudioActivity(kProperty0: KProperty0<(fragment: Fragment) -> Unit>) : Fragment() {



    private val recorder by lazy{
        AndroidAudioRecorder(applicationContext)
    }

    private val player by lazy{
        AndroidAudioPlayer(applicationContext)
    }

    private var audioFile: File? = null


    private lateinit var mainActivity : MainActivity;
    private lateinit var applicationContext : Context
    override fun onGetLayoutInflater(savedInstanceState: Bundle?): LayoutInflater { //Applies theme on loading
        val inflater = super.onGetLayoutInflater(savedInstanceState)
        mainActivity = getActivity() as MainActivity
        val contextThemeWrapper: Context = ContextThemeWrapper(requireContext(), mainActivity.getCustomTheme())
        return inflater.cloneInContext(contextThemeWrapper)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        val view = inflater.inflate(R.layout.fragment_weekly_view, container, false)
        applicationContext = requireContext()
        ActivityCompat.requestPermissions(
            mainActivity,
            arrayOf(Manifest.permission.RECORD_AUDIO),0
        )

        val composeView = view.findViewById<ComposeView>(R.id.composeView)
        composeView.apply {
            // Dispose of the Composition when the view's LifecycleOwner
            // is destroyed
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            setContent {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    OutlinedButton(onClick = {
                        File(applicationContext.cacheDir, "audio.mp3").also {
                            recorder.start(it)
                            audioFile = it
                        }
                    }) {
                        Text(text = "Start recording")
                    }
                    OutlinedButton(onClick = {
                        recorder.stop()
                    }) {
                        Text(text = "Stop recording")
                    }
                    OutlinedButton(onClick = {
                        player.play(audioFile ?: return@OutlinedButton)
                    }) {
                        Text(text = "Play")
                    }
                    OutlinedButton(onClick = {
                        player.stop()
                    }) {
                        Text(text = "Stop playing")
                    }
                }
            }
        }
        return view
    }
}


