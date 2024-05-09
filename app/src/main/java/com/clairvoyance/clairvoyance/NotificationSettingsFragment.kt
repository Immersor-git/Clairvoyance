package com.clairvoyance.clairvoyance

import android.content.Context
import android.media.RingtoneManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.fragment.app.Fragment


class NotificationSettingsFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_notification_settings, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val composeView = view.findViewById(R.id.composeView) as ComposeView
        val context = requireContext()
        composeView.setContent {
            Option(context)
        }
    }
}


@Composable
fun Option(context: Context) {
    var checked by remember { mutableStateOf(true)
    }

    Column(
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.Top,
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text = "Notification Settings",
            fontSize = 24.sp,
            modifier = Modifier.padding(vertical = 16.dp, horizontal = 85.dp)
        )

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(vertical = 8.dp)
        ) {
            Text(
                text = "Mute",
                fontSize = 20.sp,
                modifier = Modifier.padding(end = 10.dp, start = 10.dp)
            )

            Switch(
                checked = checked,
                onCheckedChange = {
                    checked = it
                    // Here insert what actually mutes the notifications
                }
            )
        }

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(vertical = 8.dp)
        ) {
            Text(
                text = "Tone",
                fontSize = 20.sp,
                modifier = Modifier.padding(end = 10.dp, start = 8.dp)
            )

            val list = listRingtones(context)

            RingtonesDropdown(context, list, null)

        }

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(vertical = 8.dp)
        ) {
            Text(
                text = "Frequency",
                fontSize = 20.sp,
                modifier = Modifier.padding(end = 10.dp, start = 8.dp )
            )

            SimpleFilledTextFieldSample()

            // allow the user to choose how often they want to to be reminded before a task starts
            // have to save it and then share it to all reminders somehow
        }
    }
}

@Composable
fun SimpleFilledTextFieldSample() {
    var mins by remember { mutableStateOf("0") }

    TextField(
        value = mins,
        onValueChange = { mins = it }
    )
}


fun listRingtones(context: Context): List<String> {
    val manager = RingtoneManager(context)
    manager.setType(RingtoneManager.TYPE_RINGTONE)
    val cursor = manager.cursor
    val ringtoneNames = mutableListOf<String>()
    while (cursor.moveToNext()) {
        val title = cursor.getString(RingtoneManager.TITLE_COLUMN_INDEX)
        ringtoneNames.add(title)
    }
    return ringtoneNames
}



@Composable
fun RingtonesDropdown(
    context: Context,
    ringtoneNames: List<String>,
    onRingtoneSelected: ((String) -> Unit?)?
) {
    var expanded by remember { mutableStateOf(false) }
    var selectedRingtone by remember {
        mutableStateOf(
            ringtoneNames.firstOrNull()
        )
    }

    Column {
        Box(
            modifier = Modifier
                .padding(16.dp)
                .width(200.dp)
                .clickable {
                    expanded = true
                }
        ) {
            Text(
                text = selectedRingtone ?: "Select Ringtone",
                modifier = Modifier.padding(8.dp)
            )
            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },
                modifier = Modifier.fillMaxWidth()
            ) {
                ringtoneNames.forEach { ringtone ->
                    DropdownMenuItem(
                        { Text(text = ringtone, modifier = Modifier.padding(8.dp)) },
                        onClick = {
                        selectedRingtone = ringtone
                        expanded = false
                        saveSelectedRingtone(context, ringtone) // Save the selected ringtone
                        if (onRingtoneSelected != null) {
                            onRingtoneSelected(ringtone)
                        }
                    })
                }
            }
        }
    }
}


@Composable
fun RingtonesDropdown1(
    ringtoneNames: List<String>,
    onRingtoneSelected: ((String) -> Unit?)?
) {
    var expanded by remember { mutableStateOf(false) }
    var selectedRingtone by remember { mutableStateOf(ringtoneNames.firstOrNull()) }

    Column {
        Box(
            modifier = Modifier
                .padding(16.dp)
                .width(200.dp)
                .clickable {
                    expanded = true
                }
        ) {
            Text(
                text = selectedRingtone ?: "Select Ringtone",
                modifier = Modifier.padding(8.dp)
            )
            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },
                modifier = Modifier.fillMaxWidth()
            ) {
                ringtoneNames.forEach { ringtone ->
                    DropdownMenuItem(
                        { Text(text = ringtone, modifier = Modifier.padding(8.dp)) },
                        onClick = {
                        selectedRingtone = ringtone
                        expanded = false
                        if (onRingtoneSelected != null) {
                            onRingtoneSelected(ringtone)
                        }
                    })
                }
            }
        }
    }
}

fun saveSelectedRingtone(context: Context, ringtone: String) {
    val sharedPref = context.getSharedPreferences(
        "com.clairvoyance.clairvoyance.PREFERENCES", Context.MODE_PRIVATE
    )
    with(sharedPref.edit()) {
        putString("selectedRingtone", ringtone)
        apply()
    }
}







