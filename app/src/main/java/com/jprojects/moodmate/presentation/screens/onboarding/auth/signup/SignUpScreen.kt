package com.jprojects.moodmate.presentation.screens.onboarding.auth.signup

import android.content.Intent
import android.util.Patterns
import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material.icons.rounded.AccountCircle
import androidx.compose.material.icons.rounded.Password
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
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
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.net.toUri
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.jprojects.moodmate.presentation.screens.onboarding.auth.AuthState
import com.jprojects.moodmate.presentation.screens.onboarding.auth.AuthViewModel

@Composable
fun SignUpScreen(
    modifier: Modifier = Modifier,
    authViewModel: AuthViewModel,
    onAuthenticated: () -> Unit
) {
    val authState by authViewModel.authState.collectAsStateWithLifecycle()

    var userEmail by remember { mutableStateOf("") }
    var userPass by remember { mutableStateOf("") }
    var displayName by remember { mutableStateOf("") }

    var passVisibility by remember { mutableStateOf(false) }
    var isPrivacyAccepted by remember { mutableStateOf(false) }

    val localFocusManager = LocalFocusManager.current
    val context = LocalContext.current


    var emailError by remember { mutableStateOf<String?>(null) }
    var passwordError by remember { mutableStateOf<String?>(null) }
    var nameError by remember { mutableStateOf<String?>(null) }

    fun validateEmail(email: String): Boolean {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    fun validatePassword(password: String): Boolean {
        val passwordPattern =
            "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@\$!%*?&])[A-Za-z\\d@\$!%*?&]{6,}".toRegex()
        return passwordPattern.matches(password)
    }

    val isLoading = authState is AuthState.Loading

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

    Scaffold { paddingValues ->
        Surface(
            modifier = modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(Modifier.weight(1f))
                Text(
                    "Let's light up your mood",
                    textAlign = TextAlign.Center,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(Modifier.height(48.dp))

                OutlinedTextField(
                    value = userEmail,
                    onValueChange = {
                        userEmail = it
                        emailError = if (validateEmail(it)) null else "Invalid email format"
                    },
                    label = { Text("Email") },
                    supportingText = {
                        emailError?.let { Text(it) }
                    },
                    isError = emailError != null,
                    singleLine = true,
                    enabled = !isLoading,
                    leadingIcon = {
                        Icon(Icons.Rounded.AccountCircle, contentDescription = "Email Icon")
                    },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Email,
                        imeAction = ImeAction.Next
                    ),
                    keyboardActions = KeyboardActions(
                        onNext = { localFocusManager.moveFocus(FocusDirection.Down) }
                    ),
                    modifier = Modifier.fillMaxWidth(0.92f),
                    shape = RoundedCornerShape(12.dp),
                )

                Spacer(Modifier.height(16.dp))

                OutlinedTextField(
                    value = displayName,
                    onValueChange = {
                        displayName = it
                        nameError = if (it.isNotBlank()) null else "Display name cannot be empty"
                    },
                    label = { Text("Display Name") },
                    supportingText = {
                        nameError?.let { Text(it) }
                    },
                    isError = nameError != null,
                    singleLine = true,
                    enabled = !isLoading,
                    leadingIcon = {
                        Icon(Icons.Rounded.Person, contentDescription = "Display Name Icon")
                    },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Text,
                        imeAction = ImeAction.Next
                    ),
                    keyboardActions = KeyboardActions(
                        onNext = { localFocusManager.moveFocus(FocusDirection.Down) }
                    ),
                    modifier = Modifier.fillMaxWidth(0.92f),
                    shape = RoundedCornerShape(12.dp),
                )

                Spacer(Modifier.height(16.dp))

                OutlinedTextField(
                    value = userPass,
                    onValueChange = {
                        userPass = it
                        passwordError = if (validatePassword(it)) null
                        else "Password must be 6+ chars with uppercase, lowercase, digit & symbol"
                    },
                    label = { Text("Password") },
                    supportingText = {
                        passwordError?.let { Text(it) }
                    },
                    isError = passwordError != null,
                    singleLine = true,
                    enabled = !isLoading,
                    leadingIcon = {
                        Icon(Icons.Rounded.Password, contentDescription = "Password Icon")
                    },
                    visualTransformation = if (passVisibility) VisualTransformation.None else PasswordVisualTransformation(),
                    trailingIcon = {
                        IconButton(onClick = { passVisibility = !passVisibility }) {
                            Icon(
                                imageVector = if (passVisibility) Icons.Filled.Visibility else Icons.Filled.VisibilityOff,
                                contentDescription = null
                            )
                        }
                    },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Password,
                        imeAction = ImeAction.Done
                    ),
                    keyboardActions = KeyboardActions(
                        onDone = { localFocusManager.clearFocus() }
                    ),
                    modifier = Modifier.fillMaxWidth(0.92f),
                    shape = RoundedCornerShape(12.dp),
                )

                Spacer(Modifier.height(16.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(0.92f),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Checkbox(
                        checked = isPrivacyAccepted,
                        onCheckedChange = { isPrivacyAccepted = it }
                    )
                    Spacer(Modifier.width(8.dp))

                    val annotatedText = buildAnnotatedString {
                        append("I agree to the ")
                        pushStringAnnotation("url", "https://your-policy-url")
                        withStyle(
                            style = SpanStyle(
                                color = MaterialTheme.colorScheme.primary,
                                textDecoration = TextDecoration.Underline
                            )
                        ) {
                            append("Privacy Policy")
                        }
                        pop()
                        append(" and Terms of Service.")
                    }

                    ClickableText(
                        text = annotatedText,
                        style = MaterialTheme.typography.bodySmall,
                        onClick = { offset ->
                            annotatedText.getStringAnnotations("url", offset, offset)
                                .firstOrNull()?.let {
                                    val intent = Intent(Intent.ACTION_VIEW, it.item.toUri())
                                    context.startActivity(intent)
                                }
                        }
                    )
                }

                Spacer(Modifier.height(24.dp))

                Button(
                    onClick = {
                        emailError = if (validateEmail(userEmail)) null else "Invalid email format"
                        passwordError =
                            if (validatePassword(userPass)) null else "Weak password"
                        nameError =
                            if (displayName.isNotBlank()) null else "Display name is required"

                        if (!isPrivacyAccepted) {
                            Toast.makeText(
                                context,
                                "Please accept Privacy Policy",
                                Toast.LENGTH_SHORT
                            ).show()

                        }

                        if (emailError == null && passwordError == null && nameError == null) {
                            authViewModel.signUp(
                                email = userEmail,
                                password = userPass,
                                displayName = displayName
                            )
                        } else {
                            Toast.makeText(
                                context,
                                "Please fix the errors before continuing",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    },
                    modifier = Modifier.fillMaxWidth(0.92f),
                    enabled = !isLoading
                ) {
                    if (isLoading) {
                        CircularProgressIndicator(modifier = Modifier.size(24.dp))
                    } else {
                        Text(
                            "Sign Up",
                            fontSize = 17.sp,
                            fontWeight = FontWeight.SemiBold,
                            modifier = Modifier.padding(vertical = 8.dp)
                        )
                    }
                }

                Spacer(Modifier.weight(1f))
            }
        }
    }
}
