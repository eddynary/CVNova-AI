package com.example.data.builder

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.ai.OpenAiResumeAssistant
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

data class ResumeBuilderUiState(
  val draftId: String = "default_draft",
  val title: String = "Untitled Resume",
  val currentStep: BuilderStep = BuilderStep.COUNTRY,
  // 1. Country
  val country: String = "United States",
  // 2. Language
  val resumeLanguage: String = "English (US)",
  // 3. Profession
  val profession: String = "Software Engineer",
  val experienceLevel: String = "Senior Level (5-8 yrs)",
  // 4. Personal Info
  val fullName: String = "Alexander Wright",
  val email: String = "alex.wright@techstudio.io",
  val phone: String = "+1 (555) 234-5678",
  val location: String = "San Francisco, CA",
  val photoUrl: String = "",
  val headline: String = "Senior Distributed Systems & Cloud Architect",
  // 5. Professional Summary
  val professionalSummary: String = "Passionate Senior Software Engineer with 6+ years of expertise building high-throughput distributed backend architectures and reactive mobile platforms.",
  // 6. Education
  val educationList: List<EducationItem> = emptyList(),
  // 7. Work Experience
  val workExperienceList: List<WorkExperienceItem> = emptyList(),
  // 8. Skills
  val skillsList: List<SkillItem> = emptyList(),
  // 9. Languages
  val languagesList: List<LanguageItem> = emptyList(),
  // 10. Projects
  val projectsList: List<ProjectItem> = emptyList(),
  // 11. Certificates
  val certificatesList: List<CertificateItem> = emptyList(),
  // 12. References
  val referencesList: List<ReferenceItem> = emptyList(),
  // 13. Social Links
  val socialLinksList: List<SocialLinkItem> = emptyList(),
  // General & Meta
  val selectedTemplate: String = "Executive Linear",
  val fontStyle: String = "Modern Sans", // Modern Sans, Classic Serif, Monospace, Display Bold
  val accentColorHex: String = "#6366F1", // #6366F1, #10B981, #0284C7, #8B5CF6, #E11D48, #D97706
  val spacingDensity: String = "Standard", // Compact, Standard, Spacious
  val zoomScale: Float = 1.0f, // 0.5f, 0.75f, 1.0f, 1.25f, 1.5f
  val showPhoto: Boolean = true,
  val showReferences: Boolean = true,
  val showSocialLinks: Boolean = true,

  // Cover Letter Generator State
  val targetJobTitle: String = "Senior Lead Architect",
  val targetCompany: String = "Google Studio / Stripe",
  val coverLetterCountry: String = "United States",
  val coverLetterLanguage: String = "English (US)",
  val coverLetterText: String = "",
  val coverLetterVersions: List<String> = emptyList(),
  val isCoverLetterLoading: Boolean = false,

  val atsScore: Int = 90,
  val isAutosaving: Boolean = false,
  val isAiLoading: Boolean = false,
  val lastSavedTimeFormatted: String = "Draft saved",
  val toastMessage: String? = null
) {
  val totalStepsCount: Int = 13
  val progressPercentage: Float = (currentStep.stepIndex.toFloat() / totalStepsCount.toFloat())
}

class ResumeBuilderViewModel(application: Application) : AndroidViewModel(application) {

  private val repository = ResumeBuilderRepository(application)
  private val openAiAssistant = OpenAiResumeAssistant()
  private val _uiState = MutableStateFlow(ResumeBuilderUiState())
  val uiState: StateFlow<ResumeBuilderUiState> = _uiState.asStateFlow()

  private var autosaveJob: Job? = null

  init {
    loadOrCreateDraft()
  }

  fun loadOrCreateDraft(draftId: String = "default_draft") {
    viewModelScope.launch {
      val existing = repository.getDraft(draftId).firstOrNull()
      if (existing != null) {
        populateStateFromEntity(existing)
      } else {
        val defaultEntity = repository.getInitialDefaultEntity(draftId)
        repository.saveDraft(defaultEntity)
        populateStateFromEntity(defaultEntity)
      }
    }
  }

  private fun populateStateFromEntity(entity: ResumeDraftEntity) {
    val edu: List<EducationItem> = repository.listFromJson(entity.educationJson)
    val work: List<WorkExperienceItem> = repository.listFromJson(entity.workExperienceJson)
    val skills: List<SkillItem> = repository.listFromJson(entity.skillsJson)
    val langs: List<LanguageItem> = repository.listFromJson(entity.languagesJson)
    val projs: List<ProjectItem> = repository.listFromJson(entity.projectsJson)
    val certs: List<CertificateItem> = repository.listFromJson(entity.certificatesJson)
    val refs: List<ReferenceItem> = repository.listFromJson(entity.referencesJson)
    val socials: List<SocialLinkItem> = repository.listFromJson(entity.socialLinksJson)

    _uiState.value = ResumeBuilderUiState(
      draftId = entity.id,
      title = entity.title,
      currentStep = BuilderStep.fromIndex(entity.currentStepIndex),
      country = entity.country,
      resumeLanguage = entity.resumeLanguage,
      profession = entity.profession,
      experienceLevel = entity.experienceLevel,
      fullName = entity.fullName,
      email = entity.email,
      phone = entity.phone,
      location = entity.location,
      photoUrl = entity.photoUrl,
      headline = entity.headline,
      professionalSummary = entity.professionalSummary,
      educationList = edu,
      workExperienceList = work,
      skillsList = skills,
      languagesList = langs,
      projectsList = projs,
      certificatesList = certs,
      referencesList = refs,
      socialLinksList = socials,
      selectedTemplate = entity.selectedTemplate,
      atsScore = entity.atsScore,
      lastSavedTimeFormatted = "Loaded draft from DB"
    )
  }

  private fun triggerAutosave() {
    autosaveJob?.cancel()
    autosaveJob = viewModelScope.launch {
      _uiState.value = _uiState.value.copy(isAutosaving = true)
      delay(400) // Debounce rapid keystrokes

      val state = _uiState.value
      val timeStr = SimpleDateFormat("hh:mm:ss a", Locale.getDefault()).format(Date())

      val entity = ResumeDraftEntity(
        id = state.draftId,
        title = state.title,
        country = state.country,
        resumeLanguage = state.resumeLanguage,
        profession = state.profession,
        experienceLevel = state.experienceLevel,
        fullName = state.fullName,
        email = state.email,
        phone = state.phone,
        location = state.location,
        photoUrl = state.photoUrl,
        headline = state.headline,
        professionalSummary = state.professionalSummary,
        educationJson = repository.toJson(state.educationList),
        workExperienceJson = repository.toJson(state.workExperienceList),
        skillsJson = repository.toJson(state.skillsList),
        languagesJson = repository.toJson(state.languagesList),
        projectsJson = repository.toJson(state.projectsList),
        certificatesJson = repository.toJson(state.certificatesList),
        referencesJson = repository.toJson(state.referencesList),
        socialLinksJson = repository.toJson(state.socialLinksList),
        currentStepIndex = state.currentStep.stepIndex,
        selectedTemplate = state.selectedTemplate,
        atsScore = calculateAtsScore(state),
        lastSavedTimestamp = System.currentTimeMillis()
      )

      repository.saveDraft(entity)
      _uiState.value = _uiState.value.copy(
        isAutosaving = false,
        lastSavedTimeFormatted = "Autosaved at $timeStr",
        atsScore = calculateAtsScore(state)
      )
    }
  }

  private fun calculateAtsScore(state: ResumeBuilderUiState): Int {
    var score = 50
    if (state.fullName.isNotBlank()) score += 5
    if (state.email.contains("@")) score += 5
    if (state.headline.isNotBlank()) score += 5
    if (state.professionalSummary.length > 50) score += 10
    if (state.workExperienceList.isNotEmpty()) score += 10
    if (state.educationList.isNotEmpty()) score += 5
    if (state.skillsList.size >= 3) score += 5
    if (state.languagesList.isNotEmpty()) score += 2
    if (state.projectsList.isNotEmpty()) score += 3
    return score.coerceAtMost(99)
  }

  // --- Step Navigation ---
  fun nextStep() {
    val current = _uiState.value.currentStep.stepIndex
    if (current < 13) {
      val next = BuilderStep.fromIndex(current + 1)
      _uiState.value = _uiState.value.copy(currentStep = next)
      triggerAutosave()
    }
  }

  fun prevStep() {
    val current = _uiState.value.currentStep.stepIndex
    if (current > 1) {
      val prev = BuilderStep.fromIndex(current - 1)
      _uiState.value = _uiState.value.copy(currentStep = prev)
      triggerAutosave()
    }
  }

  fun goToStep(step: BuilderStep) {
    _uiState.value = _uiState.value.copy(currentStep = step)
    triggerAutosave()
  }

  // --- Field Mutators ---
  fun updateTitle(title: String) {
    _uiState.value = _uiState.value.copy(title = title)
    triggerAutosave()
  }

  fun updateCountry(country: String) {
    _uiState.value = _uiState.value.copy(country = country)
    triggerAutosave()
  }

  fun updateResumeLanguage(language: String) {
    _uiState.value = _uiState.value.copy(resumeLanguage = language)
    triggerAutosave()
  }

  fun updateProfession(profession: String) {
    _uiState.value = _uiState.value.copy(profession = profession)
    triggerAutosave()
  }

  fun updateExperienceLevel(level: String) {
    _uiState.value = _uiState.value.copy(experienceLevel = level)
    triggerAutosave()
  }

  fun updatePersonalInfo(
    fullName: String = _uiState.value.fullName,
    email: String = _uiState.value.email,
    phone: String = _uiState.value.phone,
    location: String = _uiState.value.location,
    photoUrl: String = _uiState.value.photoUrl,
    headline: String = _uiState.value.headline
  ) {
    _uiState.value = _uiState.value.copy(
      fullName = fullName,
      email = email,
      phone = phone,
      location = location,
      photoUrl = photoUrl,
      headline = headline
    )
    triggerAutosave()
  }

  fun updateProfessionalSummary(summary: String) {
    _uiState.value = _uiState.value.copy(professionalSummary = summary)
    triggerAutosave()
  }

  fun updateTemplate(template: String) {
    _uiState.value = _uiState.value.copy(selectedTemplate = template)
    triggerAutosave()
  }

  // --- Education Items ---
  fun addEducation(item: EducationItem) {
    _uiState.value = _uiState.value.copy(educationList = _uiState.value.educationList + item)
    triggerAutosave()
  }

  fun updateEducation(item: EducationItem) {
    val updated = _uiState.value.educationList.map { if (it.id == item.id) item else it }
    _uiState.value = _uiState.value.copy(educationList = updated)
    triggerAutosave()
  }

  fun removeEducation(id: String) {
    val updated = _uiState.value.educationList.filterNot { it.id == id }
    _uiState.value = _uiState.value.copy(educationList = updated)
    triggerAutosave()
  }

  // --- Work Experience Items ---
  fun addWorkExperience(item: WorkExperienceItem) {
    _uiState.value = _uiState.value.copy(workExperienceList = _uiState.value.workExperienceList + item)
    triggerAutosave()
  }

  fun updateWorkExperience(item: WorkExperienceItem) {
    val updated = _uiState.value.workExperienceList.map { if (it.id == item.id) item else it }
    _uiState.value = _uiState.value.copy(workExperienceList = updated)
    triggerAutosave()
  }

  fun removeWorkExperience(id: String) {
    val updated = _uiState.value.workExperienceList.filterNot { it.id == id }
    _uiState.value = _uiState.value.copy(workExperienceList = updated)
    triggerAutosave()
  }

  // --- Skills Items ---
  fun addSkill(item: SkillItem) {
    _uiState.value = _uiState.value.copy(skillsList = _uiState.value.skillsList + item)
    triggerAutosave()
  }

  fun updateSkill(item: SkillItem) {
    val updated = _uiState.value.skillsList.map { if (it.id == item.id) item else it }
    _uiState.value = _uiState.value.copy(skillsList = updated)
    triggerAutosave()
  }

  fun removeSkill(id: String) {
    val updated = _uiState.value.skillsList.filterNot { it.id == id }
    _uiState.value = _uiState.value.copy(skillsList = updated)
    triggerAutosave()
  }

  // --- Languages Items ---
  fun addLanguage(item: LanguageItem) {
    _uiState.value = _uiState.value.copy(languagesList = _uiState.value.languagesList + item)
    triggerAutosave()
  }

  fun updateLanguage(item: LanguageItem) {
    val updated = _uiState.value.languagesList.map { if (it.id == item.id) item else it }
    _uiState.value = _uiState.value.copy(languagesList = updated)
    triggerAutosave()
  }

  fun removeLanguage(id: String) {
    val updated = _uiState.value.languagesList.filterNot { it.id == id }
    _uiState.value = _uiState.value.copy(languagesList = updated)
    triggerAutosave()
  }

  // --- Projects Items ---
  fun addProject(item: ProjectItem) {
    _uiState.value = _uiState.value.copy(projectsList = _uiState.value.projectsList + item)
    triggerAutosave()
  }

  fun updateProject(item: ProjectItem) {
    val updated = _uiState.value.projectsList.map { if (it.id == item.id) item else it }
    _uiState.value = _uiState.value.copy(projectsList = updated)
    triggerAutosave()
  }

  fun removeProject(id: String) {
    val updated = _uiState.value.projectsList.filterNot { it.id == id }
    _uiState.value = _uiState.value.copy(projectsList = updated)
    triggerAutosave()
  }

  // --- Certificates Items ---
  fun addCertificate(item: CertificateItem) {
    _uiState.value = _uiState.value.copy(certificatesList = _uiState.value.certificatesList + item)
    triggerAutosave()
  }

  fun updateCertificate(item: CertificateItem) {
    val updated = _uiState.value.certificatesList.map { if (it.id == item.id) item else it }
    _uiState.value = _uiState.value.copy(certificatesList = updated)
    triggerAutosave()
  }

  fun removeCertificate(id: String) {
    val updated = _uiState.value.certificatesList.filterNot { it.id == id }
    _uiState.value = _uiState.value.copy(certificatesList = updated)
    triggerAutosave()
  }

  // --- References Items ---
  fun addReference(item: ReferenceItem) {
    _uiState.value = _uiState.value.copy(referencesList = _uiState.value.referencesList + item)
    triggerAutosave()
  }

  fun updateReference(item: ReferenceItem) {
    val updated = _uiState.value.referencesList.map { if (it.id == item.id) item else it }
    _uiState.value = _uiState.value.copy(referencesList = updated)
    triggerAutosave()
  }

  fun removeReference(id: String) {
    val updated = _uiState.value.referencesList.filterNot { it.id == id }
    _uiState.value = _uiState.value.copy(referencesList = updated)
    triggerAutosave()
  }

  // --- Social Links Items ---
  fun addSocialLink(item: SocialLinkItem) {
    _uiState.value = _uiState.value.copy(socialLinksList = _uiState.value.socialLinksList + item)
    triggerAutosave()
  }

  fun updateSocialLink(item: SocialLinkItem) {
    val updated = _uiState.value.socialLinksList.map { if (it.id == item.id) item else it }
    _uiState.value = _uiState.value.copy(socialLinksList = updated)
    triggerAutosave()
  }

  fun removeSocialLink(id: String) {
    val updated = _uiState.value.socialLinksList.filterNot { it.id == id }
    _uiState.value = _uiState.value.copy(socialLinksList = updated)
    triggerAutosave()
  }

  // --- OPENAI API ASSISTANT INTEGRATION ---

  fun aiEnhanceSummary() {
    viewModelScope.launch {
      _uiState.value = _uiState.value.copy(isAiLoading = true)
      val state = _uiState.value
      val skills = state.skillsList.map { it.name }
      val summary = openAiAssistant.generateProfessionalSummary(
        profession = state.profession,
        experienceLevel = state.experienceLevel,
        skills = skills
      )
      _uiState.value = _uiState.value.copy(
        professionalSummary = summary,
        isAiLoading = false,
        toastMessage = "✨ AI generated human executive summary!"
      )
      triggerAutosave()
    }
  }

  fun aiRewriteWorkExperience(item: WorkExperienceItem) {
    viewModelScope.launch {
      _uiState.value = _uiState.value.copy(isAiLoading = true)
      val rewritten = openAiAssistant.rewriteWorkExperience(
        rawDescription = item.description,
        jobTitle = item.jobTitle,
        company = item.company
      )
      val updatedItem = item.copy(description = rewritten)
      updateWorkExperience(updatedItem)
      _uiState.value = _uiState.value.copy(
        isAiLoading = false,
        toastMessage = "✨ Rewrote position with ATS metrics!"
      )
    }
  }

  fun aiSuggestMissingSkills() {
    viewModelScope.launch {
      _uiState.value = _uiState.value.copy(isAiLoading = true)
      val currentNames = _uiState.value.skillsList.map { it.name }
      val suggestions = openAiAssistant.suggestMissingSkills(
        profession = _uiState.value.profession,
        currentSkillNames = currentNames
      )
      var addedCount = 0
      suggestions.forEach { skill ->
        if (!_uiState.value.skillsList.any { it.name.equals(skill.name, ignoreCase = true) }) {
          addSkill(skill)
          addedCount++
        }
      }
      _uiState.value = _uiState.value.copy(
        isAiLoading = false,
        toastMessage = "✨ AI added $addedCount high-impact skills!"
      )
    }
  }

  fun aiGenerateProjects() {
    viewModelScope.launch {
      _uiState.value = _uiState.value.copy(isAiLoading = true)
      val projects = openAiAssistant.generateProjects(_uiState.value.profession)
      projects.forEach { addProject(it) }
      _uiState.value = _uiState.value.copy(
        isAiLoading = false,
        toastMessage = "✨ AI generated high-impact projects!"
      )
    }
  }

  fun aiImproveGrammarAndTone() {
    viewModelScope.launch {
      _uiState.value = _uiState.value.copy(isAiLoading = true)
      val currentSummary = _uiState.value.professionalSummary
      val improved = openAiAssistant.improveGrammarAndTone(currentSummary)
      _uiState.value = _uiState.value.copy(
        professionalSummary = improved,
        isAiLoading = false,
        toastMessage = "✨ Grammar & tone perfected by AI!"
      )
      triggerAutosave()
    }
  }

  // --- LIVE PREVIEW & CUSTOMIZATION CONTROLS ---
  fun updateSelectedTemplate(template: String) {
    _uiState.value = _uiState.value.copy(selectedTemplate = template)
    triggerAutosave()
  }

  fun updateFontStyle(font: String) {
    _uiState.value = _uiState.value.copy(fontStyle = font)
  }

  fun updateAccentColorHex(hex: String) {
    _uiState.value = _uiState.value.copy(accentColorHex = hex)
  }

  fun updateSpacingDensity(spacing: String) {
    _uiState.value = _uiState.value.copy(spacingDensity = spacing)
  }

  fun updateZoomScale(scale: Float) {
    _uiState.value = _uiState.value.copy(zoomScale = scale)
  }

  fun toggleShowPhoto(show: Boolean) {
    _uiState.value = _uiState.value.copy(showPhoto = show)
  }

  fun toggleShowReferences(show: Boolean) {
    _uiState.value = _uiState.value.copy(showReferences = show)
  }

  fun toggleShowSocialLinks(show: Boolean) {
    _uiState.value = _uiState.value.copy(showSocialLinks = show)
  }

  // --- COVER LETTER GENERATOR ACTIONS ---
  fun updateCoverLetterTarget(
    targetTitle: String? = null,
    targetCompany: String? = null,
    country: String? = null,
    language: String? = null
  ) {
    _uiState.value = _uiState.value.copy(
      targetJobTitle = targetTitle ?: _uiState.value.targetJobTitle,
      targetCompany = targetCompany ?: _uiState.value.targetCompany,
      coverLetterCountry = country ?: _uiState.value.coverLetterCountry,
      coverLetterLanguage = language ?: _uiState.value.coverLetterLanguage
    )
  }

  fun updateCoverLetterText(newText: String) {
    _uiState.value = _uiState.value.copy(coverLetterText = newText)
  }

  fun aiGenerateCoverLetter() {
    viewModelScope.launch {
      _uiState.value = _uiState.value.copy(isCoverLetterLoading = true)
      val state = _uiState.value
      val skills = state.skillsList.map { it.name }
      val topExp = state.workExperienceList.firstOrNull()?.let { "${it.jobTitle} at ${it.company}: ${it.description}" } ?: state.professionalSummary
      val edu = state.educationList.firstOrNull()?.let { "${it.degree} in ${it.fieldOfStudy} (${it.school})" } ?: "Higher Education Degree"

      val letter = openAiAssistant.generateCoverLetter(
        fullName = state.fullName,
        targetJobTitle = state.targetJobTitle,
        companyName = state.targetCompany,
        profession = state.profession,
        experienceLevel = state.experienceLevel,
        country = state.coverLetterCountry,
        language = state.coverLetterLanguage,
        skills = skills,
        topExperience = topExp,
        education = edu,
        email = state.email,
        phone = state.phone,
        location = state.location
      )

      val updatedVersions = state.coverLetterVersions + letter
      _uiState.value = _uiState.value.copy(
        coverLetterText = letter,
        coverLetterVersions = updatedVersions,
        isCoverLetterLoading = false,
        toastMessage = "✨ AI generated human cover letter for ${state.coverLetterCountry}!"
      )
    }
  }

  fun selectCoverLetterVersion(index: Int) {
    val versions = _uiState.value.coverLetterVersions
    if (index in versions.indices) {
      _uiState.value = _uiState.value.copy(coverLetterText = versions[index])
    }
  }

  fun clearToast() {
    _uiState.value = _uiState.value.copy(toastMessage = null)
  }
}

