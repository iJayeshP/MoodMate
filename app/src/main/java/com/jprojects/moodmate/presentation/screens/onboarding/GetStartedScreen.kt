package com.jprojects.moodmate.presentation.screens.onboarding

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun GetStartedScreen(
    modifier: Modifier = Modifier,
    onSignUpClick: () -> Unit,
    onSignInClick: () -> Unit
) {

    Scaffold(modifier = modifier.fillMaxSize()) { paddingValues ->

        Surface(modifier = Modifier.padding(paddingValues)) {

            Column(
                modifier = Modifier.padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Spacer(Modifier.weight(1f))

                Text(
                    text = "Track Your Mood,\nElevate Your Day",
                    fontSize = 26.sp,
                    lineHeight = 36.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "Stay connected with your emotions. Start journaling your moods and uncover patterns that help you feel better, every day.",
                    fontSize = 16.sp,
                    textAlign = TextAlign.Center,
                    color = Color.Gray,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp)
                )

                Spacer(Modifier.weight(2.5f))

                Button(onClick = {

                    onSignUpClick()

                }, Modifier.fillMaxWidth(0.98f)) {
                    Text(
                        "Get Onboard!",
                        fontSize = 19.sp,
                        lineHeight = 24.sp,
                        fontWeight = FontWeight.SemiBold,
                        modifier = Modifier.padding(vertical = 8.dp)
                    )
                }

                Spacer(Modifier.height(16.dp))

                TextButton(onClick = {

                    onSignInClick()

                }, Modifier.fillMaxWidth()) {
                    Text(
                        "Access Your Account!",
                        fontSize = 19.sp,
                        fontWeight = FontWeight.SemiBold,
                    )
                }
                Spacer(Modifier.height(16.dp))
            }
        }
    }
}