package com.example.data.firestore

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

object FirestoreRepository {

  private const val TAG = "FirestoreRepository"

  private val firestore: FirebaseFirestore by lazy {
    FirebaseFirestore.getInstance()
  }

  // Collections
  private val usersCollection get() = firestore.collection("users")
  private val resumesCollection get() = firestore.collection("resumes")
  private val coverLettersCollection get() = firestore.collection("cover_letters")
  private val templatesCollection get() = firestore.collection("templates")
  private val subscriptionsCollection get() = firestore.collection("subscriptions")

  // ---------------------------------------------------------------------------
  // USERS OPERATIONS
  // ---------------------------------------------------------------------------
  suspend fun saveUser(user: FirestoreUser): Result<Unit> = runCatching {
    if (user.uid.isBlank()) error("User UID cannot be blank")
    usersCollection.document(user.uid).set(user).await()
  }

  suspend fun getUser(userId: String): FirestoreUser? = runCatching {
    if (userId.isBlank()) return null
    val snapshot = usersCollection.document(userId).get().await()
    snapshot.toObject(FirestoreUser::class.java)
  }.getOrNull()

  fun observeUser(userId: String): Flow<FirestoreUser?> = callbackFlow {
    if (userId.isBlank()) {
      trySend(null)
      close()
      return@callbackFlow
    }

    val listener = usersCollection.document(userId)
      .addSnapshotListener { snapshot, error ->
        if (error != null) {
          Log.e(TAG, "Error observing user $userId", error)
          trySend(null)
          return@addSnapshotListener
        }
        val user = snapshot?.toObject(FirestoreUser::class.java)
        trySend(user)
      }

    awaitClose { listener.remove() }
  }

  // ---------------------------------------------------------------------------
  // RESUMES OPERATIONS
  // ---------------------------------------------------------------------------
  suspend fun saveResume(resume: FirestoreResume): Result<String> = runCatching {
    val docRef = if (resume.id.isNotBlank()) {
      resumesCollection.document(resume.id)
    } else {
      resumesCollection.document()
    }
    val finalResume = resume.copy(
      id = docRef.id,
      updatedAt = System.currentTimeMillis()
    )
    docRef.set(finalResume).await()
    docRef.id
  }

  fun observeUserResumes(userId: String): Flow<List<FirestoreResume>> = callbackFlow {
    if (userId.isBlank()) {
      trySend(emptyList())
      close()
      return@callbackFlow
    }

    val query = resumesCollection
      .whereEqualTo("userId", userId)

    val listener = query.addSnapshotListener { snapshot, error ->
      if (error != null) {
        Log.e(TAG, "Error observing resumes for $userId", error)
        trySend(emptyList())
        return@addSnapshotListener
      }
      val list = snapshot?.documents?.mapNotNull { doc ->
        doc.toObject(FirestoreResume::class.java)
      } ?: emptyList()
      trySend(list.sortedByDescending { it.updatedAt })
    }

    awaitClose { listener.remove() }
  }

  suspend fun deleteResume(resumeId: String): Result<Unit> = runCatching {
    if (resumeId.isBlank()) return Result.success(Unit)
    resumesCollection.document(resumeId).delete().await()
  }

  // ---------------------------------------------------------------------------
  // COVER LETTERS OPERATIONS
  // ---------------------------------------------------------------------------
  suspend fun saveCoverLetter(letter: FirestoreCoverLetter): Result<String> = runCatching {
    val docRef = if (letter.id.isNotBlank()) {
      coverLettersCollection.document(letter.id)
    } else {
      coverLettersCollection.document()
    }
    val finalLetter = letter.copy(
      id = docRef.id,
      updatedAt = System.currentTimeMillis()
    )
    docRef.set(finalLetter).await()
    docRef.id
  }

  fun observeUserCoverLetters(userId: String): Flow<List<FirestoreCoverLetter>> = callbackFlow {
    if (userId.isBlank()) {
      trySend(emptyList())
      close()
      return@callbackFlow
    }

    val query = coverLettersCollection
      .whereEqualTo("userId", userId)

    val listener = query.addSnapshotListener { snapshot, error ->
      if (error != null) {
        Log.e(TAG, "Error observing cover letters for $userId", error)
        trySend(emptyList())
        return@addSnapshotListener
      }
      val list = snapshot?.documents?.mapNotNull { doc ->
        doc.toObject(FirestoreCoverLetter::class.java)
      } ?: emptyList()
      trySend(list.sortedByDescending { it.createdAt })
    }

    awaitClose { listener.remove() }
  }

  suspend fun deleteCoverLetter(letterId: String): Result<Unit> = runCatching {
    if (letterId.isBlank()) return Result.success(Unit)
    coverLettersCollection.document(letterId).delete().await()
  }

  // ---------------------------------------------------------------------------
  // TEMPLATES OPERATIONS
  // ---------------------------------------------------------------------------
  fun observeTemplates(): Flow<List<FirestoreTemplate>> = callbackFlow {
    val listener = templatesCollection.addSnapshotListener { snapshot, error ->
      if (error != null) {
        Log.e(TAG, "Error observing templates", error)
        trySend(getDefaultTemplates())
        return@addSnapshotListener
      }
      val list = snapshot?.documents?.mapNotNull { doc ->
        doc.toObject(FirestoreTemplate::class.java)
      } ?: emptyList()

      if (list.isEmpty()) {
        trySend(getDefaultTemplates())
      } else {
        trySend(list)
      }
    }

    awaitClose { listener.remove() }
  }

  suspend fun seedDefaultTemplatesIfEmpty(): Result<Unit> = runCatching {
    val snapshot = templatesCollection.get().await()
    if (snapshot.isEmpty) {
      val defaults = getDefaultTemplates()
      defaults.forEach { tmpl ->
        templatesCollection.document(tmpl.id).set(tmpl).await()
      }
    }
  }

  // ---------------------------------------------------------------------------
  // SUBSCRIPTIONS OPERATIONS
  // ---------------------------------------------------------------------------
  suspend fun saveSubscription(subscription: FirestoreSubscription): Result<String> = runCatching {
    val docRef = if (subscription.id.isNotBlank()) {
      subscriptionsCollection.document(subscription.id)
    } else {
      subscriptionsCollection.document()
    }
    val finalSub = subscription.copy(
      id = docRef.id,
      updatedAt = System.currentTimeMillis()
    )
    docRef.set(finalSub).await()
    docRef.id
  }

  fun observeUserSubscription(userId: String): Flow<FirestoreSubscription?> = callbackFlow {
    if (userId.isBlank()) {
      trySend(null)
      close()
      return@callbackFlow
    }

    val query = subscriptionsCollection.whereEqualTo("userId", userId)

    val listener = query.addSnapshotListener { snapshot, error ->
      if (error != null) {
        Log.e(TAG, "Error observing subscription for $userId", error)
        trySend(null)
        return@addSnapshotListener
      }
      val sub = snapshot?.documents?.firstOrNull()?.toObject(FirestoreSubscription::class.java)
      trySend(sub)
    }

    awaitClose { listener.remove() }
  }

  // Default initial seed data for templates
  fun getDefaultTemplates(): List<FirestoreTemplate> {
    return listOf(
      FirestoreTemplate(
        id = "tmpl_executive_linear",
        name = "Executive Linear",
        category = "Executive",
        description = "High impact single column layout for C-level & Senior Leaders.",
        isPremium = false,
        primaryColorHex = "#1E293B",
        tags = listOf("executive", "clean", "minimalist")
      ),
      FirestoreTemplate(
        id = "tmpl_modern_tech",
        name = "Modern Tech",
        category = "Tech",
        description = "Optimized for Software Engineers, Product Managers & Tech Roles.",
        isPremium = false,
        primaryColorHex = "#3B82F6",
        tags = listOf("tech", "developer", "modern")
      ),
      FirestoreTemplate(
        id = "tmpl_creative_hybrid",
        name = "Creative Hybrid",
        category = "Creative",
        description = "Dual-tone accent theme for Designers, Marketers & Creators.",
        isPremium = true,
        primaryColorHex = "#8B5CF6",
        tags = listOf("creative", "design", "pro")
      ),
      FirestoreTemplate(
        id = "tmpl_european_standard",
        name = "European Standard",
        category = "General",
        description = "Europass compliant standard structure with clear typography.",
        isPremium = true,
        primaryColorHex = "#10B981",
        tags = listOf("europass", "international", "standard")
      )
    )
  }
}
