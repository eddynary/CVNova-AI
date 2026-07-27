package com.example.data.admin

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class AdminDashboardViewModel : ViewModel() {

  private val _uiState = MutableStateFlow(AdminDashboardUiState())
  val uiState: StateFlow<AdminDashboardUiState> = _uiState.asStateFlow()

  init {
    loadMockAdminData()
  }

  private fun loadMockAdminData() {
    val initialUsers = listOf(
      AdminUser("u1", "Alexander Wright", "alex.wright@google.com", "Admin", "Pro Pass", "Active", 12, "Jan 12, 2026"),
      AdminUser("u2", "Sophia Chen", "sophia.chen@techcorp.io", "User", "Pro Pass", "Active", 8, "Feb 04, 2026"),
      AdminUser("u3", "Marcus Vance", "marcus.v@innovate.co", "User", "Free Starter", "Active", 2, "Mar 18, 2026"),
      AdminUser("u4", "Elena Rostova", "elena.r@design.fr", "Manager", "Pro Pass", "Active", 15, "Apr 01, 2026"),
      AdminUser("u5", "David K.", "dk_test@spammail.com", "User", "Free Starter", "Suspended", 1, "May 22, 2026")
    )

    val initialResumes = listOf(
      AdminResume("r1", "Senior Backend Architect - Stripe", "alex.wright@google.com", "Executive Linear", 98, true, "Jul 24, 2026"),
      AdminResume("r2", "Lead UX Researcher - Google", "sophia.chen@techcorp.io", "Modern Tech", 94, true, "Jul 22, 2026"),
      AdminResume("r3", "Staff AI Systems Engineer", "marcus.v@innovate.co", "Minimalist Elegant", 91, false, "Jul 20, 2026"),
      AdminResume("r4", "Product Manager - EMEA", "elena.r@design.fr", "European Standard", 96, false, "Jul 15, 2026")
    )

    val initialTemplates = listOf(
      AdminTemplate("t1", "Executive Linear", "Corporate / Tech", 4230, true, true, "#6366F1"),
      AdminTemplate("t2", "Modern Tech", "Engineering / AI", 3810, true, true, "#10B981"),
      AdminTemplate("t3", "Minimalist Elegant", "Design / Product", 2150, false, true, "#0284C7"),
      AdminTemplate("t4", "Creative Hybrid", "Marketing / Content", 1420, false, true, "#8B5CF6"),
      AdminTemplate("t5", "European Standard", "Global / Executive", 980, true, true, "#E11D48")
    )

    val initialSubscriptions = listOf(
      AdminSubscription("s1", "alex.wright@google.com", "Pro Annual Pass", 19.00, "Active", "Aug 27, 2026"),
      AdminSubscription("s2", "sophia.chen@techcorp.io", "Pro Monthly Pass", 24.00, "Active", "Aug 12, 2026"),
      AdminSubscription("s3", "elena.r@design.fr", "Enterprise Team", 99.00, "Active", "Sep 01, 2026"),
      AdminSubscription("s4", "old_user@domain.com", "Pro Monthly Pass", 24.00, "Canceled", "Expired")
    )

    val initialBlogPosts = listOf(
      AdminBlogPost("b1", "How to Pass ATS Filters in 2026: The Complete Guide", "Resume Advice", "Alexander Wright", 14250, "Published", "Jul 10, 2026"),
      AdminBlogPost("b2", "Top 10 High-Impact Metrics for Tech Resumes", "Career Tips", "Sophia Chen", 9820, "Published", "Jul 18, 2026"),
      AdminBlogPost("b3", "Writing Executive Cover Letters for US & EU Markets", "Cover Letters", "Elena Rostova", 4300, "Draft", "Jul 25, 2026")
    )

    val initialTickets = listOf(
      AdminSupportTicket("tk1", "sophia.chen@techcorp.io", "DOCX Export styling font alignment", "Medium", "Open", "Jul 26, 2026"),
      AdminSupportTicket("tk2", "marcus.v@innovate.co", "AI Cover Letter French translation request", "Low", "In Progress", "Jul 25, 2026"),
      AdminSupportTicket("tk3", "user_99@test.com", "Invoice billing download question", "High", "Resolved", "Jul 20, 2026")
    )

    _uiState.value = _uiState.value.copy(
      usersList = initialUsers,
      resumesList = initialResumes,
      templatesList = initialTemplates,
      subscriptionsList = initialSubscriptions,
      blogPostsList = initialBlogPosts,
      supportTicketsList = initialTickets
    )
  }

  fun setTab(tab: AdminTab) {
    _uiState.value = _uiState.value.copy(activeTab = tab)
  }

  fun setSearchQuery(query: String) {
    _uiState.value = _uiState.value.copy(searchQuery = query)
  }

  // --- USER ACTIONS ---
  fun toggleUserStatus(userId: String) {
    val updated = _uiState.value.usersList.map { u ->
      if (u.id == userId) {
        val newStatus = if (u.status == "Active") "Suspended" else "Active"
        u.copy(status = newStatus)
      } else u
    }
    _uiState.value = _uiState.value.copy(usersList = updated, toastMessage = "User status updated.")
  }

  fun changeUserRole(userId: String, newRole: String) {
    val updated = _uiState.value.usersList.map { u ->
      if (u.id == userId) u.copy(role = newRole) else u
    }
    _uiState.value = _uiState.value.copy(usersList = updated, toastMessage = "User role changed to $newRole.")
  }

  // --- TEMPLATE ACTIONS ---
  fun toggleTemplateActive(templateId: String) {
    val updated = _uiState.value.templatesList.map { t ->
      if (t.id == templateId) t.copy(isActive = !t.isActive) else t
    }
    _uiState.value = _uiState.value.copy(templatesList = updated, toastMessage = "Template visibility updated.")
  }

  fun addNewTemplate(name: String, category: String, isPremium: Boolean, hex: String) {
    val newTmpl = AdminTemplate(
      id = "t_${System.currentTimeMillis()}",
      name = name,
      category = category,
      usageCount = 0,
      isPremium = isPremium,
      isActive = true,
      accentColorHex = hex
    )
    _uiState.value = _uiState.value.copy(
      templatesList = _uiState.value.templatesList + newTmpl,
      toastMessage = "New template '$name' added to catalog!"
    )
  }

  // --- RESUME ACTIONS ---
  fun toggleResumeFeatured(resumeId: String) {
    val updated = _uiState.value.resumesList.map { r ->
      if (r.id == resumeId) r.copy(isFeatured = !r.isFeatured) else r
    }
    _uiState.value = _uiState.value.copy(resumesList = updated, toastMessage = "Resume featured status updated.")
  }

  fun deleteResume(resumeId: String) {
    val updated = _uiState.value.resumesList.filter { it.id != resumeId }
    _uiState.value = _uiState.value.copy(resumesList = updated, toastMessage = "Resume deleted from system.")
  }

  // --- BLOG ACTIONS ---
  fun toggleBlogPostStatus(postId: String) {
    val updated = _uiState.value.blogPostsList.map { p ->
      if (p.id == postId) {
        val newStatus = if (p.status == "Published") "Draft" else "Published"
        p.copy(status = newStatus)
      } else p
    }
    _uiState.value = _uiState.value.copy(blogPostsList = updated, toastMessage = "Blog post publication status updated.")
  }

  fun createBlogPost(title: String, category: String, author: String) {
    val newPost = AdminBlogPost(
      id = "b_${System.currentTimeMillis()}",
      title = title,
      category = category,
      author = author,
      views = 0,
      status = "Published",
      date = "Today"
    )
    _uiState.value = _uiState.value.copy(
      blogPostsList = _uiState.value.blogPostsList + newPost,
      toastMessage = "Blog article published!"
    )
  }

  // --- SUPPORT TICKET ACTIONS ---
  fun updateTicketStatus(ticketId: String, newStatus: String) {
    val updated = _uiState.value.supportTicketsList.map { tk ->
      if (tk.id == ticketId) tk.copy(status = newStatus) else tk
    }
    _uiState.value = _uiState.value.copy(supportTicketsList = updated, toastMessage = "Ticket #$ticketId marked as $newStatus.")
  }

  // --- SYSTEM SETTINGS ---
  fun updateSystemSettings(settings: AdminSystemSettings) {
    _uiState.value = _uiState.value.copy(
      systemSettings = settings,
      toastMessage = "SaaS System configuration saved successfully."
    )
  }

  fun clearToast() {
    _uiState.value = _uiState.value.copy(toastMessage = null)
  }
}
