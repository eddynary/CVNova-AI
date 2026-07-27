package com.example.data.auth

import java.io.Serializable

enum class SubscriptionPlan(
  val id: String,
  val displayName: String,
  val monthlyPrice: Double,
  val yearlyPrice: Double,
  val maxResumes: Int,
  val allowAiAutoWriter: Boolean,
  val allowPremiumTemplates: Boolean,
  val allowCoverLetters: Boolean,
  val allowDocxExport: Boolean,
  val allowAtsDeepAudit: Boolean
) {
  FREE(
    id = "free",
    displayName = "Free Starter",
    monthlyPrice = 0.0,
    yearlyPrice = 0.0,
    maxResumes = 1,
    allowAiAutoWriter = false,
    allowPremiumTemplates = false,
    allowCoverLetters = false,
    allowDocxExport = false,
    allowAtsDeepAudit = false
  ),
  PRO(
    id = "pro",
    displayName = "Pro Pass",
    monthlyPrice = 19.99,
    yearlyPrice = 149.99,
    maxResumes = 10,
    allowAiAutoWriter = true,
    allowPremiumTemplates = true,
    allowCoverLetters = true,
    allowDocxExport = true,
    allowAtsDeepAudit = true
  ),
  PREMIUM(
    id = "premium",
    displayName = "Premium Ultra",
    monthlyPrice = 39.99,
    yearlyPrice = 299.99,
    maxResumes = 999,
    allowAiAutoWriter = true,
    allowPremiumTemplates = true,
    allowCoverLetters = true,
    allowDocxExport = true,
    allowAtsDeepAudit = true
  );

  companion object {
    fun fromId(id: String?): SubscriptionPlan {
      return values().firstOrNull { it.id.equals(id, ignoreCase = true) } ?: FREE
    }
  }
}

data class UserSession(
  val uid: String = "",
  val email: String = "",
  val displayName: String = "",
  val isEmailVerified: Boolean = false,
  val photoUrl: String? = null,
  val authProvider: String = "Email/Password", // "Email/Password" or "Google"
  val subscriptionPlan: SubscriptionPlan = SubscriptionPlan.FREE,
  val createdAt: Long = System.currentTimeMillis()
) : Serializable

