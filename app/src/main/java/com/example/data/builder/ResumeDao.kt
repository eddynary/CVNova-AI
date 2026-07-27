package com.example.data.builder

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface ResumeDao {
  @Query("SELECT * FROM resume_drafts WHERE id = :id LIMIT 1")
  fun getResumeDraft(id: String): Flow<ResumeDraftEntity?>

  @Query("SELECT * FROM resume_drafts ORDER BY lastSavedTimestamp DESC")
  fun getAllDrafts(): Flow<List<ResumeDraftEntity>>

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insertOrUpdate(draft: ResumeDraftEntity)

  @Query("DELETE FROM resume_drafts WHERE id = :id")
  suspend fun deleteDraftById(id: String)
}
