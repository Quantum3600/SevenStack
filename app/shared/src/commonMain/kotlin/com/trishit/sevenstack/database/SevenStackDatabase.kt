package com.trishit.sevenstack.database

import androidx.room.Dao
import androidx.room.Database
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.RoomDatabase
import androidx.room.Transaction
import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.Flow

@Dao
interface AppDao {
    @Transaction
    @Query("SELECT * FROM days ORDER BY date ASC")
    fun observeAllDays(): Flow<List<DayWithContent>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTask(task: TaskEntity)

    @Query("UPDATE tasks SET isCompleted = :isCompleted WHERE id = :taskId")
    suspend fun updateTaskStatus(
        taskId: String,
        isCompleted: Boolean
    )

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNote(note: NoteEntity)

    @Query("DELETE FROM tasks WHERE id = :taskId")
    suspend fun deleteTask(taskId: String)

    @Query("DELETE FROM notes WHERE id = :noteId")
    suspend fun deleteNote(noteId: String)
}

@Database(
    entities = [DayEntity::class, TaskEntity::class, NoteEntity::class],
    version = 1
)
abstract class SevenStackDatabase: RoomDatabase() {
    abstract fun appDao(): AppDao
}

expect class DatabaseFactory {
    fun createBuilder(): RoomDatabase.Builder<SevenStackDatabase>
}
fun getRoomDatabase(factory: DatabaseFactory): SevenStackDatabase =
    factory
        .createBuilder()
        .setDriver(BundledSQLiteDriver())
        .setQueryCoroutineContext(kotlinx.coroutines.Dispatchers.IO)
        .build()
/**
 * Provides the database name used across platforms.
 */
const val DATABASE_NAME = "sevenstack.db"
