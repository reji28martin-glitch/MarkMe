package com.markme.attendance.data.model

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(tableName = "students")
data class StudentEntity(
    @PrimaryKey val id: String,
    val name: String,
    val rollNo: String,
    val admNo: String,
    val batch: String,
    val photoUri: String?,
    val faceEmbedding: String?, // Serialized FloatArray (128-d MobileFaceNet)
    val faceReady: Boolean,
    val createdAt: Long = System.currentTimeMillis()
)

@Entity(
    tableName = "attendance",
    indices = [Index(value = ["studentId", "date"], unique = true)] // Unique per student per day
)
data class AttendanceEntity(
    @PrimaryKey val id: String,
    val studentId: String,
    val studentName: String,
    val rollNo: String,
    val batch: String,
    val date: String, // YYYY-MM-DD
    val time: String, // HH:mm:ss
    var status: String, // "present", "late", "absent"
    val cameraName: String,
    val verifiedVia: String, // "ai_face", "manual", "cctv_ai"
    val syncedToRemote: Boolean = false
)

@Entity(tableName = "unknown_faces")
data class UnknownFaceEntity(
    @PrimaryKey val id: String,
    val timestamp: Long,
    val date: String,
    val formattedTime: String,
    val cameraName: String,
    val photoUri: String,
    var isNew: Boolean = true,
    val detectionCount: Int = 1
)

@Entity(tableName = "admins")
data class AdminEntity(
    @PrimaryKey val username: String,
    val passwordHash: String,
    val role: String = "admin"
)
