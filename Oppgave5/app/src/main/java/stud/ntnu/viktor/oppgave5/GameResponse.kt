package stud.ntnu.viktor.oppgave5

import com.google.gson.annotations.SerializedName

data class GameResponse(
    @SerializedName("message") val message: String
)