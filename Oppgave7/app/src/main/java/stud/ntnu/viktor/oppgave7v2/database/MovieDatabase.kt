package stud.ntnu.viktor.oppgave7v2.database

import androidx.room.Database
import androidx.room.RoomDatabase
import stud.ntnu.viktor.oppgave7v2.dao.MovieDao
import stud.ntnu.viktor.oppgave7v2.model.Actor
import stud.ntnu.viktor.oppgave7v2.model.Movie

@Database(entities = [Movie::class, Actor::class], version = 3, exportSchema = false)
abstract class MovieDatabase : RoomDatabase() {
    abstract fun movieDao(): MovieDao
}
