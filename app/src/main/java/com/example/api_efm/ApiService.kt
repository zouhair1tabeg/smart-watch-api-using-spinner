package com.example.api_efm

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

data class User(
    val id :Int,
    val nom: String,
    val prenom: String,

    val mail:String,
    val motDePasse: String
)
data class AddResponse(
    val code:Int,
    val message: String,
    val fullName: String
)
data class Smart_Watch(
    val id : Int,
    val name : String,
    val price : Double,
    val battery_life : String,
    val in_stock : Boolean,
    val image_url : String
)


interface ApiService {

    @POST("/AccountAPI/login.php")
    fun login_user(@Body account_user: User): Call<AddResponse>


    @GET("/WatchAPI/readAll.php")
    fun getWatch(): retrofit2.Call<List<Smart_Watch>>
}