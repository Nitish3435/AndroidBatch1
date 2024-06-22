package com.example.andrioidbatch1

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.*
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.POST

class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalFoundationApi::class)
    private val productVM: ProductViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //enableEdgeToEdge()
        setContent {
            val products by productVM.products.observeAsState(emptyList())
            Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
                LazyColumn {
                    items(products) {product ->
                        ProductItem(product = product)
                    }
                }
            }
        }
    }

    @Composable
    fun ProductItem(product: Product) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp)
        ) {
            Column(
                modifier = Modifier.padding(10.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(text = product.title, color = Color.Red)
                Spacer(modifier = Modifier.height(8.dp))
                Text(text = product.category)
            }
        }
    }
}


//Model Class
data class  Product(
    val id: Int,
    val title: String,
    val price: Double,
    val description: String,
    val category: String,
    val image: String,
    val rating: Rating
)

data class Rating(
    val rate: Float,
    val count: Int
)

interface  ApiService {
    @GET("products")
    suspend fun getProducts(): List<Product>
}

object RetrofitClient {
    private const val BASE_URL = "https://fakestoreapi.com/"

    val  apiService: ApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }
}

class ProductRepository(private val apiService: ApiService) {
    suspend fun getProducts(): List<Product> {
        return apiService.getProducts()
    }
}

class ProductViewModel: ViewModel() {
    private  val _products = MutableLiveData<List<Product>>()
    val products: LiveData<List<Product>> get() = _products
    
    private  val repository = ProductRepository(RetrofitClient.apiService)
    init {
        fetchProducts()
    }
    
    fun fetchProducts() {
        viewModelScope.launch { 
            try {
                val productList = repository.getProducts()
                _products.postValue(productList)
                println(productList)
                
            } catch (e: Exception) {
                
            }
        }
    }
}








