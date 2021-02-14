package com.guru.composecookbook.ui.templates.logins

import android.animation.ValueAnimator
import android.content.Context
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.Interaction
import androidx.compose.foundation.InteractionState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Facebook
import androidx.compose.material.icons.filled.RemoveRedEye
import androidx.compose.material.icons.filled.VpnKey
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.AmbientContext
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.*
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import com.airbnb.lottie.LottieAnimationView
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieAnimationSpec
import com.airbnb.lottie.compose.rememberLottieAnimationState
import com.guru.composecookbook.R
import com.guru.composecookbook.ui.templates.onboardings.OnBoardingScreen1
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


@Composable
fun LoginOnboarding() {
    var loggedIn by remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()
    Crossfade(targetState = loggedIn) {
        if (loggedIn) {
            OnBoardingScreen1 {
                loggedIn = false
            }
        } else {
            LoginScreen1 {
                coroutineScope.launch {
                    delay(2000)
                    loggedIn = true
                }
            }
        }
    }
}

@Composable
fun LoginScreen1(onLoginSuccess: () -> Unit) {
    Scaffold {

        //TextFields
        var email by remember { mutableStateOf(TextFieldValue("")) }
        var password by remember { mutableStateOf(TextFieldValue("")) }
        var hasError by remember { mutableStateOf(false) }
        var passwordVisualTransformation by remember {
            mutableStateOf<VisualTransformation>(
                PasswordVisualTransformation()
            )
        }
        val passwordInteractionState by remember { mutableStateOf(InteractionState()) }
        val emailInteractionState by remember { mutableStateOf(InteractionState()) }

        LazyColumn(modifier = Modifier.fillMaxSize().padding(horizontal = 16.dp)) {
            item { Spacer(modifier = Modifier.height(20.dp)) }
            item { LottieLoadingView(context = AmbientContext.current) }
            item {
                Text(
                    text = "Welcome Back",
                    style = MaterialTheme.typography.h5.copy(fontWeight = FontWeight.ExtraBold),
                    modifier = Modifier.padding(top = 8.dp)
                )
            }
            item {
                Text(
                    text = "We have missed you, Let's start by Sign In!",
                    style = MaterialTheme.typography.caption,
                    modifier = Modifier.padding(bottom = 12.dp)
                )
            }

            item {
                OutlinedTextField(
                    value = email,
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Email,
                            contentDescription = "Email"
                        )
                    },
                    maxLines = 1,
                    isErrorValue = hasError,
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text(text = "Email address") },
                    placeholder = { Text(text = "abc@gmail.com") },
                    onValueChange = {
                        email = it
                    },
                    interactionState = emailInteractionState,
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Text,
                        imeAction = ImeAction.Next
                    ),
                    keyboardActions = KeyboardActions(onDone = {
                        passwordInteractionState.addInteraction(interaction = Interaction.Focused)
                        passwordInteractionState.addInteraction(interaction = Interaction.Pressed)
                        emailInteractionState.removeInteraction(Interaction.Focused)
                    })
                )
            }
            item {
                OutlinedTextField(
                    value = password,
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.VpnKey, contentDescription =
                            null
                        )
                    },
                    trailingIcon = {
                        Icon(
                            imageVector = Icons.Default.RemoveRedEye,
                            contentDescription = null,
                            modifier = Modifier.clickable(onClick = {
                                passwordVisualTransformation =
                                    if (passwordVisualTransformation != VisualTransformation.None) {
                                        VisualTransformation.None
                                    } else {
                                        PasswordVisualTransformation()
                                    }
                            })
                        )
                    },
                    maxLines = 1,
                    isErrorValue = hasError,
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text(text = "Password") },
                    placeholder = { Text(text = "12334444") },
                    interactionState = passwordInteractionState,
                    visualTransformation = passwordVisualTransformation,
                    onValueChange = {
                        password = it
                    },
                    keyboardActions = KeyboardActions(onDone = {
                        //
                        passwordInteractionState.removeInteraction(interaction = Interaction.Focused)
                    }),
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Password,
                        imeAction = ImeAction.Done
                    )
                )
            }
            item {
                var loading by remember { mutableStateOf(false) }
                Button(
                    onClick = {
                        if (invalidInput(email.text, password.text)) {
                            hasError = true
                            loading = false
                        } else {
                            loading = true
                            hasError = false
                            onLoginSuccess.invoke()
                        }
                    },
                    modifier = Modifier.fillMaxWidth().padding(vertical = 16.dp).height(50.dp)
                        .clip(CircleShape)
                ) {
                    if (loading) {
                        //TODO fix progress bar animations
                        //HorizontalDottedProgressBar()
                    } else {
                        Text(text = "Log In")
                    }
                }
            }
            item {
                Box(modifier = Modifier.padding(vertical = 16.dp)) {
                    Spacer(
                        modifier = Modifier.align(Alignment.Center)
                            .height(1.dp).fillMaxWidth().background(Color.LightGray)
                    )
                    Text(
                        text = "Or use",
                        color = Color.LightGray,
                        modifier = Modifier.align(Alignment.Center)
                            .background(MaterialTheme.colors.background).padding(horizontal = 16.dp)
                    )
                }
            }

            item {
                OutlinedButton(onClick = { }, modifier = Modifier.fillMaxWidth().height(50.dp)) {
                    Icon(imageVector = Icons.Default.Facebook, contentDescription = "facebook")
                    Text(
                        text = "Sign in with Facebook",
                        style = MaterialTheme.typography.h6.copy(fontSize = 14.sp),
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }

            item { Spacer(modifier = Modifier.height(8.dp)) }

            item {
                OutlinedButton(onClick = { }, modifier = Modifier.fillMaxWidth().height(50.dp)) {
                    Icon(imageVector = Icons.Default.Email, contentDescription = "Gmail")
                    Text(
                        text = "Sign in with Gmail",
                        style = MaterialTheme.typography.h6.copy(fontSize = 14.sp),
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }

            item {
                val primaryColor = MaterialTheme.colors.primary
                val annotatedString = remember {
                    AnnotatedString.Builder("Don't have an account? Register")
                        .apply {
                            addStyle(style = SpanStyle(color = primaryColor), 23, 31)
                        }
                }
                Text(
                    text = annotatedString.toAnnotatedString(),
                    modifier = Modifier.fillMaxWidth().padding(vertical = 16.dp)
                        .clickable(onClick = {}),
                    textAlign = TextAlign.Center
                )
            }

            item { Spacer(modifier = Modifier.height(100.dp)) }
        }
    }
}

fun invalidInput(email: String, password: String) =
    email.isNullOrBlank() || password.isNullOrBlank()


@Composable
fun LottieLoadingView(context: Context) {
    val lottieView = remember {
        LottieAnimationView(context).apply {
            setAnimation("working.json")
            repeatCount = ValueAnimator.INFINITE
        }
    }
    AndroidView({ lottieView }, modifier = Modifier.fillMaxWidth().height(250.dp)) {
        it.playAnimation()
    }

    val lottieSpec = remember { LottieAnimationSpec.RawRes(R.raw.cryptoload) }
    val lottieAnimationState =
        rememberLottieAnimationState(autoPlay = true, repeatCount = Integer.MAX_VALUE)

    LottieAnimation(
        spec = lottieSpec,
        modifier = Modifier.preferredSize(100.dp)
    )
}