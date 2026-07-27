package com.example.data.firestore

import androidx.annotation.Keep
import com.google.firebase.firestore.IgnoreExtraProperties

// -----------------------------------------------------------------------------
// FIRESTORE USER MODEL
// -----------------------------------------------------------------------------
@Keep
@IgnoreExtraProperties
data class FirestoreUser(
  val uid: String = "",
  val email: String = "",
  val displayName: String = "",
  val photoUrl: String = "",
  val role: String = "USER", // "USER", "ADMIN", "PRO_USER"
  val subscriptionPlan: String = "FREE", // "FREE", "PRO", "PREMIUM"
  val createdAt: Long = System.currentTimeMillis(),
  val updatedAt: Long = System.currentTimeMillis()
)

// -----------------------------------------------------------------------------
// FIRESTORE RESUME MODEL
// -----------------------------------------------------------------------------
@Keep
@IgnoreExtraProperties
data class FirestoreResume(
  val id: String = "",
  val userId: String = "",
  val title: String = "Untitled Resume",
  val fullName: String = "",
  val email: String = "",
  val phone: String = "",
  val location: String = "",
  val website: String = "",
  val linkedin: String = "",
  val github: String = "",
  val profession: String = "",
  val headline: String = "",
  val professionalSummary: String = "",
  val selectedTemplate: String = "Executive Linear",
  val accentColorHex: String = "#3B82F6",
  val isPublic: Boolean = false,
  val atsScore: Int = 85,
  val targetJobTitle: String = "",
  val workExperienceJson: String = "[]",
  val educationJson: String = "[]",
  val skillsJson: String = "[]",
  val projectsJson: String = "[]",
  val createdAt: Long = System.currentTimeMillis(),
  val updatedAt: Long = System.currentTimeMillis()
)

// -----------------------------------------------------------------------------
// FIRESTORE COVER LETTER MODEL
// -----------------------------------------------------------------------------
@Keep
@IgnoreExtraProperties
data class FirestoreCoverLetter(
  val id: String = "",
  val userId: String = "",
  val resumeId: String = "",
  val targetJobTitle: String = "",
  val companyName: String = "",
  val hiringManagerName: String = "Hiring Manager",
  val letterText: String = "",
  val tone: String = "Professional",
  val language: String = "English",
  val createdAt: Long = System.currentTimeMillis(),
  val updatedAt: Long = System.currentTimeMillis()
)

// -----------------------------------------------------------------------------
// FIRESTORE TEMPLATE MODEL
// -----------------------------------------------------------------------------
@Keep
@IgnoreExtraProperties
data class FirestoreTemplate(
  val id: String = "",
  val name: String = "",
  val category: String = "General", // "Executive", "Tech", "Creative", "Modern"
  val description: String = "",
  val previewImageUrl: String = "",
  val isPremium: Boolean = false,
  val primaryColorHex: String = "#3B82F6",
  val tags: List<String> = emptyList(),
  val createdAt: Long = System.currentTimeMillis()
)

// -----------------------------------------------------------------------------
// FIRESTORE SUBSCRIPTION MODEL
// -----------------------------------------------------------------------------
@Keep
@IgnoreExtraProperties
data class FirestoreSubscription(
  val id: String = "",
  val userId: String = "",
  val planId: String = "FREE", // "FREE", "PRO_MONTHLY", "PREMIUM_YEARLY"
  val status: String = "active", // "active", "canceled", "past_due", "trialing"
  val stripeCustomerId: String = "",
  val stripeSubscriptionId: String = "",
  val currentPeriodStart: Long = System.currentTimeMillis(),
  val currentPeriodEnd: Long = System.currentTimeMillis() + 30L * 24 * 3600 * 1000,
  val cancelAtPeriodEnd: Boolean = false,
  val createdAt: Long = System.currentTimeMillis(),
  val updatedAt: Long = System.currentTimeMillis()
)
