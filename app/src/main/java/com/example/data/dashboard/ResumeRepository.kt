package com.example.data.dashboard

import androidx.compose.ui.graphics.Color
import com.example.ui.theme.BrandAccent
import com.example.ui.theme.BrandPrimary
import com.example.ui.theme.BrandPurple
import com.example.ui.theme.BrandSecondary
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

data class ResumeItem(
  val id: String,
  val title: String,
  val targetRole: String,
  val templateName: String,
  val atsScore: Int,
  val lastModified: String,
  val viewsCount: Int,
  val downloadsCount: Int,
  val isFavorite: Boolean = false,
  val accentColorHex: Long = 0xFF6366F1
)

data class DashboardStats(
  val totalResumes: Int = 4,
  val avgAtsScore: Int = 96,
  val totalViews: Int = 184,
  val applicationsSent: Int = 18,
  val aiCreditsRemaining: Int = 85,
  val profileCompletionPercentage: Int = 85
)

data class UserSettingsState(
  val fullName: String = "Alexander Wright",
  val email: String = "alex.wright@google.com",
  val jobTitle: String = "Senior Staff Software Engineer",
  val phone: String = "+1 (555) 234-5678",
  val location: String = "San Francisco, CA",
  val portfolioUrl: String = "https://alexwright.dev",
  val emailNotifications: Boolean = true,
  val aiAutoEnhance: Boolean = true,
  val publicLinkSharing: Boolean = true
)

class DashboardRepository {

  private val _resumes = MutableStateFlow(
    listOf(
      ResumeItem(
        id = "res_1",
        title = "Staff Software Engineer 2026",
        targetRole = "Senior / Staff Distributed Systems",
        templateName = "Executive Linear",
        atsScore = 98,
        lastModified = "2 hours ago",
        viewsCount = 112,
        downloadsCount = 24,
        isFavorite = true,
        accentColorHex = 0xFF6366F1
      ),
      ResumeItem(
        id = "res_2",
        title = "AI Product Manager Lead",
        targetRole = "Director of Product Management",
        templateName = "Notion Minimalist",
        atsScore = 95,
        lastModified = "Yesterday",
        viewsCount = 48,
        downloadsCount = 12,
        isFavorite = true,
        accentColorHex = 0xFF06B6D4
      ),
      ResumeItem(
        id = "res_3",
        title = "Full Stack Tech Lead",
        targetRole = "Principal Engineer",
        templateName = "Stripe Gradient",
        atsScore = 97,
        lastModified = "3 days ago",
        viewsCount = 24,
        downloadsCount = 6,
        isFavorite = false,
        accentColorHex = 0xFFA855F7
      ),
      ResumeItem(
        id = "res_4",
        title = "Stanford Academic & Research",
        targetRole = "Research Scientist - ML",
        templateName = "Stanford Classic",
        atsScore = 94,
        lastModified = "1 week ago",
        viewsCount = 16,
        downloadsCount = 3,
        isFavorite = false,
        accentColorHex = 0xFF10B981
      )
    )
  )

  val resumes: StateFlow<List<ResumeItem>> = _resumes.asStateFlow()

  private val _stats = MutableStateFlow(DashboardStats())
  val stats: StateFlow<DashboardStats> = _stats.asStateFlow()

  private val _settings = MutableStateFlow(UserSettingsState())
  val settings: StateFlow<UserSettingsState> = _settings.asStateFlow()

  fun createNewResume(title: String, targetRole: String, templateName: String): ResumeItem {
    val newResume = ResumeItem(
      id = "res_" + System.currentTimeMillis().toString().takeLast(6),
      title = title.ifEmpty { "Untitled Resume" },
      targetRole = targetRole.ifEmpty { "Software Engineer" },
      templateName = templateName,
      atsScore = 98,
      lastModified = "Just now",
      viewsCount = 0,
      downloadsCount = 0,
      isFavorite = false,
      accentColorHex = 0xFF6366F1
    )
    _resumes.value = listOf(newResume) + _resumes.value
    _stats.value = _stats.value.copy(
      totalResumes = _resumes.value.size
    )
    return newResume
  }

  fun toggleFavorite(id: String) {
    _resumes.value = _resumes.value.map {
      if (it.id == id) it.copy(isFavorite = !it.isFavorite) else it
    }
  }

  fun deleteResume(id: String) {
    _resumes.value = _resumes.value.filter { it.id != id }
    _stats.value = _stats.value.copy(totalResumes = _resumes.value.size)
  }

  fun duplicateResume(id: String) {
    val existing = _resumes.value.find { it.id == id } ?: return
    val copy = existing.copy(
      id = "res_" + System.currentTimeMillis().toString().takeLast(6),
      title = "${existing.title} (Copy)",
      lastModified = "Just now",
      viewsCount = 0,
      downloadsCount = 0
    )
    _resumes.value = listOf(copy) + _resumes.value
  }

  fun updateSettings(newSettings: UserSettingsState) {
    _settings.value = newSettings
  }
}
