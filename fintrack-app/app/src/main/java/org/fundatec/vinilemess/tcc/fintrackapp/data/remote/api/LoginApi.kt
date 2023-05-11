package org.fundatec.vinilemess.tcc.fintrackapp.data.remote.api

import org.fundatec.vinilemess.tcc.fintrackapp.data.remote.request.UserRequest
import org.fundatec.vinilemess.tcc.fintrackapp.data.remote.response.UserResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

private const val USERS_PATH = "/users"

interface LoginApi {

    @GET("$USERS_PATH/login")
    suspend fun login(
        @Query("email") email:String,
        @Query("password") password:String
    ): UserResponse

    @POST(USERS_PATH)
    suspend fun registerUser(@Body userRequest: UserRequest)
}