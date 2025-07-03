package com.jprojects.moodmate.presentation.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jprojects.moodmate.domain.model.MoodDetails
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


@Composable
fun MoodListItem(mood: MoodDetails) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(2.dp),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = mood.mood,
                fontSize = 24.sp
            )
            Text(
                text = SimpleDateFormat("dd MMM yyyy • hh:mm a", Locale.getDefault())
                    .format(Date(mood.timestamp)),
                style = MaterialTheme.typography.labelSmall,
                color = Color.Gray
            )

            Text(
                text = mood.moodReason,
                style = MaterialTheme.typography.bodyMedium
            )

        }
    }
}
