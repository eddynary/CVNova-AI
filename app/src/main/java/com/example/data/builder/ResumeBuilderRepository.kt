package com.example.data.builder

import android.content.Context
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.flow.Flow
import java.lang.reflect.Type

class ResumeBuilderRepository(context: Context) {

  private val dao = AppDatabase.getDatabase(context).resumeDao()
  @PublishedApi
  internal val moshi: Moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()

  val allDrafts: Flow<List<ResumeDraftEntity>> = dao.getAllDrafts()

  fun getDraft(id: String): Flow<ResumeDraftEntity?> = dao.getResumeDraft(id)

  suspend fun saveDraft(draft: ResumeDraftEntity) {
    dao.insertOrUpdate(draft)
  }

  suspend fun deleteDraft(id: String) {
    dao.deleteDraftById(id)
  }

  // --- JSON Helpers ---
  inline fun <reified T> toJson(list: List<T>): String {
    return try {
      val type: Type = Types.newParameterizedType(List::class.java, T::class.java)
      val adapter = moshi.adapter<List<T>>(type)
      adapter.toJson(list)
    } catch (e: Exception) {
      "[]"
    }
  }

  inline fun <reified T> listFromJson(json: String): List<T> {
    return try {
      val type: Type = Types.newParameterizedType(List::class.java, T::class.java)
      val adapter = moshi.adapter<List<T>>(type)
      adapter.fromJson(json) ?: emptyList()
    } catch (e: Exception) {
      emptyList()
    }
  }

  fun getInitialDefaultEntity(draftId: String = "default_draft"): ResumeDraftEntity {
    val sampleEdu = listOf(
      EducationItem(
        school = "Stanford University",
        degree = "B.S. Computer Science",
        fieldOfStudy = "Software Systems & Artificial Intelligence",
        startDate = "2018",
        endDate = "2022",
        gpa = "3.92 / 4.0",
        honors = "Dean's High Honors, Tau Beta Pi"
      )
    )

    val sampleWork = listOf(
      WorkExperienceItem(
        company = "Stripe Inc.",
        jobTitle = "Senior Software Engineer",
        location = "San Francisco, CA",
        startDate = "2022",
        endDate = "Present",
        isCurrent = true,
        description = "Architected high-throughput payment settlement microservices handling $1.2B daily volume with 99.999% SLA. Optimized database connection pooling reducing p99 latency by 42%."
      ),
      WorkExperienceItem(
        company = "Google Cloud",
        jobTitle = "Software Engineering Intern",
        location = "Mountain View, CA",
        startDate = "2021",
        endDate = "2021",
        isCurrent = false,
        description = "Developed automated synthetic telemetry benchmarking suite for Kubernetes cluster networking nodes."
      )
    )

    val sampleSkills = listOf(
      SkillItem(name = "Kotlin / Java", category = "Technical", level = "Expert"),
      SkillItem(name = "Jetpack Compose & Android", category = "Technical", level = "Expert"),
      SkillItem(name = "System Architecture & Microservices", category = "Technical", level = "Advanced"),
      SkillItem(name = "GraphQL & REST APIs", category = "Tools", level = "Advanced"),
      SkillItem(name = "Distributed Team Leadership", category = "Soft", level = "Advanced")
    )

    val sampleLanguages = listOf(
      LanguageItem(language = "English", proficiency = "Native"),
      LanguageItem(language = "Spanish", proficiency = "Professional")
    )

    val sampleProjects = listOf(
      ProjectItem(
        title = "Resume Studio Mobile Platform",
        role = "Lead Creator",
        description = "Modern Android resume builder powered by Jetpack Compose & Gemini AI targeting recruiter parsing algorithms.",
        techStack = "Kotlin, Jetpack Compose, Room, Coroutines, Gemini API",
        projectUrl = "https://github.com/aistudio/resume-studio"
      )
    )

    val sampleCerts = listOf(
      CertificateItem(
        name = "Google Certified Professional Cloud Architect",
        issuer = "Google Cloud",
        issueDate = "2023",
        expiryDate = "2026",
        credentialId = "GCP-883921",
        credentialUrl = "https://cloud.google.com/certification"
      )
    )

    val sampleRefs = listOf(
      ReferenceItem(
        name = "Sarah Jenkins",
        title = "VP of Engineering",
        company = "Stripe Inc.",
        email = "sjenkins@stripe.com",
        phone = "+1 (555) 987-6543",
        relationship = "Direct Manager"
      )
    )

    val sampleSocials = listOf(
      SocialLinkItem(platform = "LinkedIn", url = "https://linkedin.com/in/alexanderwright"),
      SocialLinkItem(platform = "GitHub", url = "https://github.com/alexwright-dev"),
      SocialLinkItem(platform = "Portfolio", url = "https://alexwright.dev")
    )

    return ResumeDraftEntity(
      id = draftId,
      title = "Senior Software Engineer - Tech 2026",
      country = "United States",
      resumeLanguage = "English (US)",
      profession = "Senior Software Engineer",
      experienceLevel = "Senior Level (5-8 yrs)",
      fullName = "Alexander Wright",
      email = "alex.wright@techstudio.io",
      phone = "+1 (555) 234-5678",
      location = "San Francisco, CA",
      photoUrl = "",
      headline = "Senior Distributed Systems & Android Architect",
      professionalSummary = "Impact-driven Senior Software Engineer with 6+ years of expertise designing scalable cloud services, reactive Android architectures, and high-performance APIs. Proven track record of boosting system throughput by 40%+.",
      educationJson = toJson(sampleEdu),
      workExperienceJson = toJson(sampleWork),
      skillsJson = toJson(sampleSkills),
      languagesJson = toJson(sampleLanguages),
      projectsJson = toJson(sampleProjects),
      certificatesJson = toJson(sampleCerts),
      referencesJson = toJson(sampleRefs),
      socialLinksJson = toJson(sampleSocials),
      currentStepIndex = 1,
      selectedTemplate = "Executive Linear",
      atsScore = 92
    )
  }
}
