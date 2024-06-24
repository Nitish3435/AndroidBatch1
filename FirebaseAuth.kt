// Add this browser dependency for Captch Validations
// implementation("androidx.browser:browser:1.3.0")


package com.example.siliconfirebase

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.siliconfirebase.ui.theme.SiliconFirebaseTheme
import com.google.firebase.FirebaseException
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthMissingActivityForRecaptchaException
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import java.util.concurrent.TimeUnit

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SiliconFirebaseTheme {
                OTPScreen()
            }
        }
    }

    val auth = FirebaseAuth.getInstance()
    var storedVerificationId: String? = null
    lateinit var resendToken: PhoneAuthProvider.ForceResendingToken
    fun signUP(email: String, password: String) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener{task ->
                if (task.isSuccessful) {
                    val user = auth.currentUser
                    println(user)
                } else {
                    println("User Couldn't be created")
                    println(task.exception?.message)
                }

            }
    }
    fun signIN(email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener {task ->
                if (task.isSuccessful) {
                    val user = auth.currentUser
                    println("User Logged In ${user}")
                } else {
                    println("User Couldn't logged in")
                    println("User Couldn't logged in ${task.exception?.message}")
                }

            }
    }
    val callbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

        override fun onVerificationCompleted(credential: PhoneAuthCredential) {
            // This callback will be invoked in two situations:
            // 1 - Instant verification. In some cases the phone number can be instantly
            //     verified without needing to send or enter a verification code.
            // 2 - Auto-retrieval. On some devices Google Play services can automatically
            //     detect the incoming verification SMS and perform verification without
            //     user action.
            Log.d(TAG, "onVerificationCompleted:$credential")
            signINWithPhoneCred(credential)
        }

        override fun onVerificationFailed(e: FirebaseException) {
            // This callback is invoked in an invalid request for verification is made,
            // for instance if the the phone number format is not valid.
            Log.w(TAG, "onVerificationFailed", e)

            if (e is FirebaseAuthInvalidCredentialsException) {
                // Invalid request
            } else if (e is FirebaseTooManyRequestsException) {
                // The SMS quota for the project has been exceeded
            } else if (e is FirebaseAuthMissingActivityForRecaptchaException) {
                // reCAPTCHA verification attempted with null Activity
            }

            // Show a message and update the UI
        }

        override fun onCodeSent(
            verificationId: String,
            token: PhoneAuthProvider.ForceResendingToken,
        ) {
            // The SMS verification code has been sent to the provided phone number, we
            // now need to ask the user to enter the code and then construct a credential
            // by combining the code with a verification ID.
            Log.d(TAG, "onCodeSent:$verificationId")

            // Save verification ID and resending token so we can use them later
            storedVerificationId = verificationId
            resendToken = token
        }
    }
    fun startPhoneNumberVerification(phoneNumber: String) {
        val options = PhoneAuthOptions.newBuilder(auth)
            .setPhoneNumber(phoneNumber) // Phone number to verify
            .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
            .setActivity(this) // Activity (for callback binding)
            .setCallbacks(callbacks) // OnVerificationStateChangedCallbacks
            .build()
        PhoneAuthProvider.verifyPhoneNumber(options)
    }
    fun verifyPhoneWithCode(code: String) {
        val credential = PhoneAuthProvider.getCredential(storedVerificationId!!, code)
        signINWithPhoneCred(credential)

    }
    fun signINWithPhoneCred(cred: PhoneAuthCredential) {
        auth.signInWithCredential(cred)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(TAG, "signInWithCredential:success")

                    val user = task.result?.user
                } else {
                    // Sign in failed, display a message and update the UI
                    Log.w(TAG, "signInWithCredential:failure", task.exception)
                    if (task.exception is FirebaseAuthInvalidCredentialsException) {
                        // The verification code entered was invalid
                    }
                    // Update UI
                }
            }
    }

    @Composable
    fun LoginScreen() {
        val email = remember { mutableStateOf("") }
        val password = remember { mutableStateOf("") }
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ){
            TextField(value = email.value, onValueChange = {
                email.value = it
            },
             label = { Text(text = "Enter Email")},
                modifier = Modifier.fillMaxWidth())

            Spacer(modifier = Modifier.height(16.dp))
            TextField(value = password.value, onValueChange = {
                password.value = it
            },
                label = { Text(text = "Password")},
                modifier = Modifier.fillMaxWidth(),
                visualTransformation = PasswordVisualTransformation())
            Spacer(modifier = Modifier.height(16.dp))
            Button(onClick = {
                signIN(email.value, password.value)
            }) {
                Text(text = "Login")
            }
        }
    }
    @Composable
    fun OTPScreen() {
        val phoneNumber = remember { mutableStateOf("") }
        val otpCode = remember { mutableStateOf("") }
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ){
            TextField(value = phoneNumber.value, onValueChange = {
                phoneNumber.value = it
            },
                label = { Text(text = "Enter Phone Number")},
                modifier = Modifier.fillMaxWidth())

            Spacer(modifier = Modifier.height(16.dp))
            Button(onClick = {
                startPhoneNumberVerification(phoneNumber.value)
            }) {
                Text(text = "Send OTP")
            }
            TextField(value = otpCode.value, onValueChange = {
                otpCode.value = it
            },
                label = { Text(text = "Enter OTP")},
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(16.dp))
            Button(onClick = {
                verifyPhoneWithCode(otpCode.value)
            }) {
                Text(text = "Verify OTP")
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    SiliconFirebaseTheme {
        Greeting("Android")
    }
}
