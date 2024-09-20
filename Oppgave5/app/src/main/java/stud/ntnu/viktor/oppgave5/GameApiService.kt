package stud.ntnu.viktor.oppgave5

import okhttp3.Cookie
import okhttp3.CookieJar
import okhttp3.HttpUrl
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface GameApiService {

    @FormUrlEncoded
    @POST("mobil/tallspill.jsp")
    suspend fun registerUser(
        @Field("navn") name: String,
        @Field("kortnummer") cardNumber: String
    ): String  // Change to String if the response is not JSON

    @FormUrlEncoded
    @POST("mobil/tallspill.jsp")
    suspend fun submitGuess(
        @Field("tall") guess: Int
    ): String  // Change to String if the response is not JSON

    companion object {
        private const val BASE_URL = "https://bigdata.idi.ntnu.no/"

        fun create(): GameApiService {
            val logger = HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BODY }

            val client = OkHttpClient.Builder()
                .cookieJar(object : CookieJar {
                    private var cookies: List<Cookie> = mutableListOf()

                    override fun saveFromResponse(url: HttpUrl, cookies: List<Cookie>) {
                        this.cookies = cookies
                    }

                    override fun loadForRequest(url: HttpUrl): List<Cookie> {
                        return cookies
                    }
                })
                .addInterceptor(logger)
                .build()

            return Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(client)
                .addConverterFactory(ScalarsConverterFactory.create())  // Use ScalarsConverterFactory
                .build()
                .create(GameApiService::class.java)
        }
    }
}
