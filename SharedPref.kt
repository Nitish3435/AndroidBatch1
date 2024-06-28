package com.example.siliconfirebase

import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.*
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import coil.compose.rememberAsyncImagePainter
import coil.compose.rememberImagePainter
import com.example.siliconfirebase.ui.theme.SiliconFirebaseTheme
import com.google.firebase.Firebase
import com.google.firebase.FirebaseException
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthMissingActivityForRecaptchaException
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import com.google.firebase.firestore.firestore
import com.google.firebase.firestore.toObject
import com.google.firebase.storage.storage
import java.util.UUID
import java.util.concurrent.TimeUnit

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
//            val userList = remember {
//                mutableStateOf<List<FirebaseUser>>(emptyList())
//            }
//            LaunchedEffect(Unit) {
//                fetchFirebaseUsers {users ->
//                    userList.value = users
//                }
//            }
            SiliconFirebaseTheme {
//                LazyColumn(
//                    modifier = Modifier
//                        .fillMaxSize()
//                        .padding(20.dp)
//                ) {
//                  items(userList.value) {user ->
//                      Text(text = "Name: ${user.name}, Age: ${user.age}")
//                      Divider()
//                  }
//                }
                //AddUserScreen()
                //ImageUploadScreen()
//                Column(
//                    modifier = Modifier
//                        .padding(20.dp)
//                        .fillMaxSize(),
//                    verticalArrangement = Arrangement.Center,
//                    horizontalAlignment = Alignment.CenterHorizontally
//                ) {
//                    LocalStateExample()
//                }
                SharedPrefExample(context = this)
                //WebViewScreen(url = "https://youtu.be/CoQfichK5Iw")
            }
        }
    }

    @Composable
    fun LocalStateExample() {
        var text by remember { mutableStateOf("") }
        var savedText by remember { mutableStateOf("") }
        Column {
            TextField(value = text, onValueChange = {
                text = it
            }, label = { Text(text = "Enter Some Text")})
            Text(text = "Saved Text: $savedText")
            Button(onClick = {
                savedText = text
            }) {
                Text(text = "Save Text")
            }
        }

    }

    @Composable
    fun SharedPrefExample(context: Context) {
        val sharedPref = context.getSharedPreferences("my_prefs", Context.MODE_PRIVATE)
        val editor = sharedPref.edit()

        var text by remember {
            mutableStateOf(sharedPref.getString("saved_text", "") ?: "")
        }
        Column(
            modifier = Modifier
                .padding(20.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            TextField(value = text, onValueChange ={
                text = it
            }, label = {Text(text = "Enter some text")})
            Button(onClick = {
                editor.putString("saved_text", text)
                editor.apply()
            }) {
                Text(text = "Save to Shared Pref")
            }
            Text(text = "Saved Text: ${sharedPref.getString("saved_text", "")}")
        }
    }
















    @Composable
    fun LinkText(text: String, url: String) {
        val context = LocalContext.current
        val annotatedString = buildAnnotatedString {
            append(text)
            addStyle(
                style =  SpanStyle(
                    color = Color.Blue,
                    textDecoration = TextDecoration.Underline
                ),
                start = 0,
                end = text.length
            )
            addStringAnnotation(
                tag = "URL",
                annotation = url,
                start = 0,
                end = url.length
            )
        }

        ClickableText(text = annotatedString,
            onClick = { offset ->
                annotatedString.getStringAnnotations("URL", offset, offset)
                    .firstOrNull()?.let {stringAnnotation->
                        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(stringAnnotation.item))
                        context.startActivity(intent)
                    }
            }
        )
    }
    
    @Composable
    fun WebViewScreen(url: String) {
        val context = LocalContext.current
        AndroidView(factory = {
            WebView(context).apply { 
                webViewClient = WebViewClient()
                settings.javaScriptEnabled = true
                loadUrl(url)
            }
        },
            update = {webView ->
                webView.loadUrl(url)
                
            },
            modifier = Modifier.fillMaxSize()
        )
    }













    val auth = FirebaseAuth.getInstance()
    val firebaseDB = Firebase.firestore
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
    fun addUserToFirebaseDB(name: String, age: Int) {
        val isAdult = age >= 18
        val firebasUser = FirebaseUser(name, age, isAdult)
        firebaseDB.collection("users")
            .add(firebasUser)
            .addOnSuccessListener { dRef ->
                Log.d(TAG, "Document added with ID: ${dRef.id}")
            }
            .addOnFailureListener {e ->
                Log.w(TAG, "Document Could not be added ${e.message}")
            }
    }
    fun fetchFirebaseUsers(onResult: (List<FirebaseUser>) -> Unit) {
        firebaseDB.collection("users")
            .get()
            .addOnSuccessListener {result ->
                val usersList = result.map {document ->
                    document.toObject(FirebaseUser::class.java)
                }
                onResult(usersList)
            }
            .addOnFailureListener{e ->
                Log.w(TAG, "Error Getting Data", e)
            }
    }

    val storage = Firebase.storage
    val storageRef = storage.reference
    fun uploadImage(uri: Uri, context: Context) {
        val fileName = "images/${UUID.randomUUID()}.jpg"
        val imageRef = storageRef.child(fileName)

        imageRef.putFile(uri)
            .addOnCompleteListener{takeSnapShot ->
                imageRef.downloadUrl.addOnSuccessListener {uri ->
                    
                    Toast.makeText(context,"Image Uploaded Successfully: ${uri}", Toast.LENGTH_SHORT).show()
                }
            }
            .addOnFailureListener { exception ->
                Toast.makeText(context, "Image upload failed: ${exception.message}", Toast.LENGTH_SHORT).show()
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
    @Composable
    fun AddUserScreen() {
        val name = remember { mutableStateOf("") }
        val age = remember { mutableStateOf("") }
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ){
            TextField(value = name.value, onValueChange = {
                name.value = it
            },
                label = { Text(text = "Enter Name")},
                modifier = Modifier.fillMaxWidth())

            Spacer(modifier = Modifier.height(16.dp))
            TextField(value = age.value, onValueChange = {
                age.value = it
            },
                label = { Text(text = "Enter Age")},
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(16.dp))
            Button(onClick = {
                //FirebaseAuth.getInstance().signOut()
               addUserToFirebaseDB(name.value.toString(), age.value.toInt())
            }) {
                Text(text = "Add User")
            }
        }
    }

    @Composable
    fun ImageUploadScreen() {
        val context = LocalContext.current
        val imageUri = remember {
            mutableStateOf<Uri?>(null)
        }
        val launcher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
            imageUri.value = uri
            uri?.let {
                uploadImage(it, context)
            }
        }
        Column(
            modifier = Modifier.padding(20.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Button(onClick = {
                launcher.launch("image/*")
            }) {
                Text(text = "Select Image")
            }
            Spacer(modifier = Modifier.height(20.dp))
            imageUri.value?.let {uri ->
                Image(painter = rememberAsyncImagePainter(uri), contentDescription = "Uploaded Image",
                    modifier = Modifier.size(250.dp))
            }

        }
    }














}

data class FirebaseUser(
    val name: String = "",
    val age: Int = 0,
    val isAdult: Boolean = false
)

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
