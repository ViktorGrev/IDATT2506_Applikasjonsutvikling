package stud.ntnu.viktor.oppgave7v2.model

import androidx.room.Entity
import androidx.room.ForeignKey

@Entity(
    primaryKeys = ["movieTitle", "actorName"],
    foreignKeys = [ForeignKey(entity = Movie::class, parentColumns = ["title"], childColumns = ["movieTitle"], onDelete = ForeignKey.CASCADE)]
)
data class Actor(
    val movieTitle: String,
    val actorName: String
)
