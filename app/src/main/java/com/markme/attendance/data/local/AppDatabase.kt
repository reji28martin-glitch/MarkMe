package com.markme.attendance.data.local

import android.content.Context
import androidx.room.*
import com.markme.attendance.data.model.*
import kotlinx.coroutines.flow.Flow

@Dao
interface StudentDao {
    @Query("SELECT * FROM students ORDER BY name ASC")
    fun getAllStudents(): Flow<List<StudentEntity>>

    @Query("SELECT * FROM students WHERE id = :id LIMIT 1")
    suspend fun getStudentById(id: String): StudentEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertStudent(student: StudentEntity)

    @Delete
    suspend fun deleteStudent(student: StudentEntity)
}

@Dao
interface AttendanceDao {
    @Query("SELECT * FROM attendance WHERE date = :date ORDER BY time DESC")
    fun getAttendanceByDate(date: String): Flow<List<AttendanceEntity>>

    @Query("SELECT * FROM attendance ORDER BY date DESC, time DESC")
    fun getAllAttendance(): Flow<List<AttendanceEntity>>

    @Query("SELECT * FROM attendance WHERE studentId = :studentId AND date = :date LIMIT 1")
    suspend fun getRecordForStudent(studentId: String, date: String): AttendanceEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdateAttendance(record: AttendanceEntity)
}

@Dao
interface UnknownFaceDao {
    @Query("SELECT * FROM unknown_faces ORDER BY timestamp DESC")
    fun getAllUnknownFaces(): Flow<List<UnknownFaceEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUnknownFace(face: UnknownFaceEntity)

    @Query("UPDATE unknown_faces SET isNew = 0 WHERE id = :id")
    suspend fun markAsSeen(id: String)

    @Query("DELETE FROM unknown_faces WHERE id = :id")
    suspend fun deleteFace(id: String)

    @Query("DELETE FROM unknown_faces WHERE id IN (:ids)")
    suspend fun deleteFaces(ids: List<String>): Int

    @Query("DELETE FROM unknown_faces WHERE timestamp < :cutoffTimestamp")
    suspend fun deleteOlderThan(cutoffTimestamp: Long): Int
}

@Database(
    entities = [StudentEntity::class, AttendanceEntity::class, UnknownFaceEntity::class, AdminEntity::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun studentDao(): StudentDao
    abstract fun attendanceDao(): AttendanceDao
    abstract fun unknownFaceDao(): UnknownFaceDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "markme_attendance.db"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}
