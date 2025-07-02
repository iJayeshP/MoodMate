package com.jprojects.moodmate.presentation.screens.onboarding.auth.singin

import android.util.Patterns
import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material.icons.rounded.MailOutline
import androidx.compose.material.icons.rounded.Password
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.jprojects.moodmate.presentation.screens.onboarding.auth.AuthState
import com.jprojects.moodmate.presentation.screens.onboarding.auth.AuthViewModel

@Composable
fun SignInScreen(
    modifier: Modifier = Modifier,
    onSignUpScreen: () -> Unit,
    onAuthenticated: () -> Unit,
    authViewModel: AuthViewModel,
) {
    val authState by authViewModel.authState.collectAsStateWithLifecycle()

    var userEmail by remember { mutableStateOf("") }
    var userPass by remember { mutableStateOf("") }
    var emailError by remember { mutableStateOf<String?>(null) }
    var passwordError by remember { mutableStateOf<String?>(null) }

    var passVisibility by remember { mutableStateOf(false) }

    val localFocusManager = LocalFocusManager.current
    val context = LocalContext.current

    val isLoading = authState is AuthState.Loading

    fun validateEmail(email: String): Boolean {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    fun validatePassword(password: String): Boolean {
        return password.length >= 6
    }

    LaunchedEffect(authState) {
        if (authState is AuthState.Authenticated) {
            onAuthenticated()
        } else if (authState is AuthState.Error) {
            Toast.makeText(
                context,
                (authState as AuthState.Error).message,
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    Surface(modifier.fillMaxSize()) {
        Column(
            Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Spacer(Modifier.weight(1f))

            Text(
                text = "Welcome back!",
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center,
                fontSize = 24.sp,
                fontWeight = FontWeight.SemiBold
            )

            Spacer(Modifier.height(32.dp))

            OutlinedTextField(
                value = userEmail,
                onValueChange = {
                    userEmail = it
                    emailError = if (validateEmail(it)) null else "Invalid email format"
                },
                enabled = !isLoading,
                label = { Text(text = "Email") },
                supportingText = {
                    emailError?.let { Text(it) }
                },
                isError = emailError != null,
                singleLine = true,
                leadingIcon = {
                    Icon(Icons.Rounded.MailOutline, contentDescription = "Email Icon")
                },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Email,
                    imeAction = ImeAction.Next
                ),
                keyboardActions = KeyboardActions(
                    onNext = {
                        localFocusManager.moveFocus(FocusDirection.Down)
                    }
                ),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.fillMaxWidth(0.92f)
            )

            Spacer(Modifier.height(16.dp))

            OutlinedTextField(
                value = userPass,
                onValueChange = {
                    userPass = it
                    passwordError = if (validatePassword(it)) null else "Password must be at least 6 characters"
                },
                enabled = !isLoading,
                label = { Text(text = "Password") },
                supportingText = {
                    passwordError?.let { Text(it) }
                },
                isError = passwordError != null,
                singleLine = true,
                leadingIcon = {
                    Icon(Icons.Rounded.Password, contentDescription = "Password Icon")
                },
                visualTransformation = if (passVisibility) VisualTransformation.None else PasswordVisualTransformation(),
                trailingIcon = {
                    IconButton(onClick = { passVisibility = !passVisibility }) {
                        Icon(
                            imageVector = if (passVisibility) Icons.Filled.Visibility else Icons.Filled.VisibilityOff,
                            contentDescription = if (passVisibility) "Hide Password" else "Show Password"
                        )
                    }
                },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Password,
                    imeAction = ImeAction.Done
                ),
                keyboardActions = KeyboardActions(onDone = {
                    localFocusManager.clearFocus()
                }),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.fillMaxWidth(0.92f)
            )

            Spacer(Modifier.height(8.dp))

            TextButton(
                onClick = {
                    if (userEmail.trim().isEmpty()) {
                        Toast.makeText(context, "Enter your registered email", Toast.LENGTH_SHORT).show()
                    } else {
                        // Forgot Password - can be handled later if required
                    }
                },
                modifier = Modifier
                    .align(Alignment.End)
                    .padding(end = 16.dp)
            ) {
                Text(
                    text = "Forgot Password?",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = {
                    emailError = if (validateEmail(userEmail)) null else "Invalid email"
                    passwordError = if (validatePassword(userPass)) null else "Password too short"

                    if (emailError == null && passwordError == null) {
                        authViewModel.signIn(userEmail, userPass)
                    } else {
                        Toast.makeText(context, "Fix validation errors", Toast.LENGTH_SHORT).show()
                    }
                },
                enabled = !isLoading,
                modifier = Modifier.fillMaxWidth(0.92f)
            ) {
                if (isLoading) {
                    CircularProgressIndicator(modifier = Modifier.size(20.dp))
                } else {
                    Text(
                        text = "Sign In",
                        fontSize = 17.sp,
                        fontWeight = FontWeight.SemiBold,
                        modifier = Modifier.padding(vertical = 8.dp)
                    )
                }
            }

            Spacer(Modifier.weight(1f))

            TextButton(onClick = onSignUpScreen) {
                Text(
                    text = "Don't have an account? Sign Up here",
                    fontSize = 17.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }

            Spacer(Modifier.height(24.dp))
        }
    }
}
