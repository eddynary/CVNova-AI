package com.example.data.builder

import androidx.room.Entity
import androidx.room.PrimaryKey

data class EducationItem(
  val id: String = java.util.UUID.randomUUID().toString(),
  val school: String = "",
  val degree: String = "",
  val fieldOfStudy: String = "",
  val startDate: String = "",
  val endDate: String = "",
  val gpa: String = "",
  val honors: String = ""
)

data class WorkExperienceItem(
  val id: String = java.util.UUID.randomUUID().toString(),
  val company: String = "",
  val jobTitle: String = "",
  val location: String = "",
  val startDate: String = "",
  val endDate: String = "",
  val isCurrent: Boolean = false,
  val description: String = ""
)

data class SkillItem(
  val id: String = java.util.UUID.randomUUID().toString(),
  val name: String = "",
  val category: String = "Technical", // Technical, Soft, Tools
  val level: String = "Advanced" // Beginner, Intermediate, Advanced, Expert
)

data class LanguageItem(
  val id: String = java.util.UUID.randomUUID().toString(),
  val language: String = "",
  val proficiency: String = "Fluent" // Native, Fluent, Professional, Intermediate, Basic
)

data class ProjectItem(
  val id: String = java.util.UUID.randomUUID().toString(),
  val title: String = "",
  val role: String = "",
  val description: String = "",
  val techStack: String = "",
  val projectUrl: String = ""
)

data class CertificateItem(
  val id: String = java.util.UUID.randomUUID().toString(),
  val name: String = "",
  val issuer: String = "",
  val issueDate: String = "",
  val expiryDate: String = "",
  val credentialId: String = "",
  val credentialUrl: String = ""
)

data class ReferenceItem(
  val id: String = java.util.UUID.randomUUID().toString(),
  val name: String = "",
  val title: String = "",
  val company: String = "",
  val email: String = "",
  val phone: String = "",
  val relationship: String = ""
)

data class SocialLinkItem(
  val id: String = java.util.UUID.randomUUID().toString(),
  val platform: String = "LinkedIn", // LinkedIn, GitHub, Portfolio, Twitter/X, LeetCode, Dribbble, Medium
  val url: String = ""
)

enum class BuilderStep(val stepIndex: Int, val title: String, val subtitle: String) {
  COUNTRY(1, "Country & Format", "Select target location & standard formatting"),
  LANGUAGE(2, "Resume Language", "Language for ATS parsing & localization"),
  PROFESSION(3, "Profession & Level", "Your target role & experience tier"),
  PERSONAL_INFO(4, "Personal Info", "Contact details, location & headline"),
  SUMMARY(5, "Professional Summary", "A compelling elevator pitch for recruiters"),
  EDUCATION(6, "Education", "Degrees, universities & academic honors"),
  WORK_EXPERIENCE(7, "Work Experience", "Career history & measurable achievements"),
  SKILLS(8, "Skills & Tools", "Core technical & interpersonal competencies"),
  LANGUAGES(9, "Spoken Languages", "Language fluencies & proficiency levels"),
  PROJECTS(10, "Projects", "Key work, open-source or portfolio highlights"),
  CERTIFICATES(11, "Certificates", "Professional certifications & credentials"),
  REFERENCES(12, "References", "Professional contacts & recommendations"),
  SOCIAL_LINKS(13, "Social Links", "LinkedIn, GitHub & portfolio URLs");

  companion object {
    fun fromIndex(index: Int): BuilderStep {
      return values().firstOrNull { it.stepIndex == index } ?: COUNTRY
    }
  }
}

@Entity(tableName = "resume_drafts")
data class ResumeDraftEntity(
  @PrimaryKey val id: String = "default_draft",
  val title: String = "Untitled Resume",
  val country: String = "United States",
  val resumeLanguage: String = "English (US)",
  val profession: String = "Software Engineer",
  val experienceLevel: String = "Senior Level (5-8 yrs)",
  val fullName: String = "Alexander Wright",
  val email: String = "alex.wright@techstudio.io",
  val phone: String = "+1 (555) 234-5678",
  val location: String = "San Francisco, CA",
  val photoUrl: String = "",
  val headline: String = "Senior Distributed Systems & Cloud Architect",
  val professionalSummary: String = "Passionate Senior Software Engineer with 6+ years of expertise building high-throughput distributed backend architectures and reactive mobile platforms.",
  val educationJson: String = "[]",
  val workExperienceJson: String = "[]",
  val skillsJson: String = "[]",
  val languagesJson: String = "[]",
  val projectsJson: String = "[]",
  val certificatesJson: String = "[]",
  val referencesJson: String = "[]",
  val socialLinksJson: String = "[]",
  val currentStepIndex: Int = 1,
  val selectedTemplate: String = "Executive Linear",
  val atsScore: Int = 88,
  val lastSavedTimestamp: Long = System.currentTimeMillis()
)
