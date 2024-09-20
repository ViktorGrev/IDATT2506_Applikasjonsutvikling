package stud.ntnu.viktor.oppgave7v2.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import stud.ntnu.viktor.oppgave7v2.model.Actor
import stud.ntnu.viktor.oppgave7v2.model.Movie

@Dao
interface MovieDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMovies(movies: List<Movie>): List<Long>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertActors(actors: List<Actor>): List<Long>

    @Query("SELECT * FROM Movie")
    suspend fun getAllMovies(): List<Movie>

    @Query("SELECT * FROM Actor")
    suspend fun getAllActors(): List<Actor>
}



