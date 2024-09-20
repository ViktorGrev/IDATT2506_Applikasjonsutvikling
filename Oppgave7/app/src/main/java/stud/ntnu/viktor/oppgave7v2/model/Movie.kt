package stud.ntnu.viktor.oppgave7v2.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Movie(
    @PrimaryKey val title: String,
    val director: String
)
