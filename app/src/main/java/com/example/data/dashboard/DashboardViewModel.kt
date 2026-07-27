package com.example.data.dashboard

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

enum class DashboardNavTab {
  OVERVIEW,
  MY_RESUMES,
  ANALYTICS,
  SUBSCRIPTION,
  SETTINGS
}

data class DashboardUiState(
  val activeTab: DashboardNavTab = DashboardNavTab.OVERVIEW,
  val searchQuery: String = "",
  val isCreateModalOpen: Boolean = false,
  val newResumeTitle: String = "",
  val newResumeTargetRole: String = "Senior Software Engineer",
  val selectedTemplate: String = "Executive Linear",
  val toastMessage: String? = null
)

class DashboardViewModel(application: Application) : AndroidViewModel(application) {

  private val repository = DashboardRepository()

  val resumes: StateFlow<List<ResumeItem>> = repository.resumes
  val stats: StateFlow<DashboardStats> = repository.stats
  val settings: StateFlow<UserSettingsState> = repository.settings

  private val _uiState = MutableStateFlow(DashboardUiState())
  val uiState: StateFlow<DashboardUiState> = _uiState.asStateFlow()

  fun setTab(tab: DashboardNavTab) {
    _uiState.value = _uiState.value.copy(activeTab = tab)
  }

  fun setSearchQuery(query: String) {
    _uiState.value = _uiState.value.copy(searchQuery = query)
  }

  fun openCreateModal() {
    _uiState.value = _uiState.value.copy(
      isCreateModalOpen = true,
      newResumeTitle = "",
      newResumeTargetRole = "Senior Software Engineer"
    )
  }

  fun closeCreateModal() {
    _uiState.value = _uiState.value.copy(isCreateModalOpen = false)
  }

  fun updateNewResumeTitle(title: String) {
    _uiState.value = _uiState.value.copy(newResumeTitle = title)
  }

  fun updateNewResumeTargetRole(role: String) {
    _uiState.value = _uiState.value.copy(newResumeTargetRole = role)
  }

  fun selectTemplate(templateName: String) {
    _uiState.value = _uiState.value.copy(selectedTemplate = templateName)
  }

  fun createResume() {
    val title = _uiState.value.newResumeTitle.trim()
    val role = _uiState.value.newResumeTargetRole.trim()
    val template = _uiState.value.selectedTemplate

    repository.createNewResume(title, role, template)
    _uiState.value = _uiState.value.copy(
      isCreateModalOpen = false,
      toastMessage = "New resume '$title' created successfully!"
    )
  }

  fun toggleFavorite(id: String) {
    repository.toggleFavorite(id)
  }

  fun duplicateResume(id: String) {
    repository.duplicateResume(id)
    _uiState.value = _uiState.value.copy(toastMessage = "Resume duplicated successfully!")
  }

  fun deleteResume(id: String) {
    repository.deleteResume(id)
    _uiState.value = _uiState.value.copy(toastMessage = "Resume moved to trash.")
  }

  fun saveSettings(newSettings: UserSettingsState) {
    repository.updateSettings(newSettings)
    _uiState.value = _uiState.value.copy(toastMessage = "Settings saved successfully!")
  }

  fun clearToast() {
    _uiState.value = _uiState.value.copy(toastMessage = null)
  }
}
