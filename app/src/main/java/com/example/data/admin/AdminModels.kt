package com.example.data.admin

enum class AdminTab {
  USERS,
  RESUMES,
  TEMPLATES,
  SUBSCRIPTIONS,
  ANALYTICS,
  REPORTS,
  BLOG,
  SUPPORT,
  SETTINGS
}

data class AdminUser(
  val id: String,
  val fullName: String,
  val email: String,
  val role: String, // "Admin", "User", "Manager"
  val plan: String, // "Pro Pass", "Free Starter", "Enterprise"
  val status: String, // "Active", "Suspended", "Pending"
  val resumesCount: Int,
  val joinedDate: String
)

data class AdminResume(
  val id: String,
  val title: String,
  val ownerEmail: String,
  val templateName: String,
  val atsScore: Int,
  val isFeatured: Boolean,
  val createdDate: String
)

data class AdminTemplate(
  val id: String,
  val name: String,
  val category: String,
  val usageCount: Int,
  val isPremium: Boolean,
  val isActive: Boolean,
  val accentColorHex: String
)

data class AdminSubscription(
  val id: String,
  val userEmail: String,
  val planName: String,
  val amountMonthly: Double,
  val status: String, // "Active", "Past Due", "Canceled"
  val renewalDate: String
)

data class AdminBlogPost(
  val id: String,
  val title: String,
  val category: String,
  val author: String,
  val views: Int,
  val status: String, // "Published", "Draft"
  val date: String
)

data class AdminSupportTicket(
  val id: String,
  val userEmail: String,
  val subject: String,
  val priority: String, // "High", "Medium", "Low"
  val status: String, // "Open", "In Progress", "Resolved"
  val date: String
)

data class AdminSystemSettings(
  val platformName: String = "Resume Studio Pro",
  val maintenanceMode: Boolean = false,
  val allowNewRegistrations: Boolean = true,
  val freeTierResumeLimit: Int = 3,
  val maxAiCreditsPerMonth: Int = 100,
  val smtpGatewayStatus: String = "Connected",
  val paymentGatewayStatus: String = "Stripe Live"
)

data class AdminDashboardUiState(
  val activeTab: AdminTab = AdminTab.ANALYTICS,
  val searchQuery: String = "",
  val mrrTotal: Double = 12840.00,
  val activeUsersCount: Int = 1420,
  val totalResumesCount: Int = 8940,
  val systemHealthStatus: String = "99.98% Operational",
  val usersList: List<AdminUser> = emptyList(),
  val resumesList: List<AdminResume> = emptyList(),
  val templatesList: List<AdminTemplate> = emptyList(),
  val subscriptionsList: List<AdminSubscription> = emptyList(),
  val blogPostsList: List<AdminBlogPost> = emptyList(),
  val supportTicketsList: List<AdminSupportTicket> = emptyList(),
  val systemSettings: AdminSystemSettings = AdminSystemSettings(),
  val toastMessage: String? = null
)
