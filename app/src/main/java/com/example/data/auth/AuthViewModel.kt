package com.example.data.auth

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

enum class AuthTab {
  LOGIN,
  REGISTER
}

data class AuthUiState(
  val currentTab: AuthTab = AuthTab.LOGIN,
  val isLoading: Boolean = false,
  val errorMessage: String? = null,
  val successMessage: String? = null,
  val isForgotPasswordOpen: Boolean = false,
  val isEmailVerificationOpen: Boolean = false,
  val resetEmailSent: Boolean = false,
  val verificationEmailSent: Boolean = false,
  val verificationCountdown: Int = 0
)

class AuthViewModel(application: Application) : AndroidViewModel(application) {

  val repository = AuthRepository(application.applicationContext)
  val userSession: StateFlow<UserSession?> = repository.currentSession

  private val _uiState = MutableStateFlow(AuthUiState())
  val uiState: StateFlow<AuthUiState> = _uiState.asStateFlow()

  // Form Fields
  var loginEmail = MutableStateFlow("")
  var loginPassword = MutableStateFlow("")
  var rememberMe = MutableStateFlow(true)

  var registerName = MutableStateFlow("")
  var registerEmail = MutableStateFlow("")
  var registerPassword = MutableStateFlow("")
  var registerConfirmPassword = MutableStateFlow("")
  var acceptTerms = MutableStateFlow(true)

  var forgotPasswordEmail = MutableStateFlow("")

  fun setTab(tab: AuthTab) {
    _uiState.value = _uiState.value.copy(
      currentTab = tab,
      errorMessage = null,
      successMessage = null
    )
  }

  fun login() {
    val email = loginEmail.value.trim()
    val pass = loginPassword.value

    if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
      _uiState.value = _uiState.value.copy(errorMessage = "Please enter a valid email address.")
      return
    }
    if (pass.length < 6) {
      _uiState.value = _uiState.value.copy(errorMessage = "Password must be at least 6 characters.")
      return
    }

    viewModelScope.launch {
      _uiState.value = _uiState.value.copy(isLoading = true, errorMessage = null)
      val result = repository.loginWithEmail(email, pass)
      if (result.isSuccess) {
        _uiState.value = _uiState.value.copy(
          isLoading = false,
          successMessage = "Welcome back, ${result.getOrNull()?.displayName}!"
        )
      } else {
        _uiState.value = _uiState.value.copy(
          isLoading = false,
          errorMessage = result.exceptionOrNull()?.localizedMessage ?: "Login failed. Please check credentials."
        )
      }
    }
  }

  fun register() {
    val name = registerName.value.trim()
    val email = registerEmail.value.trim()
    val pass = registerPassword.value
    val confirm = registerConfirmPassword.value

    if (name.length < 2) {
      _uiState.value = _uiState.value.copy(errorMessage = "Please enter your full name.")
      return
    }
    if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
      _uiState.value = _uiState.value.copy(errorMessage = "Please enter a valid email address.")
      return
    }
    if (pass.length < 6) {
      _uiState.value = _uiState.value.copy(errorMessage = "Password must be at least 6 characters.")
      return
    }
    if (pass != confirm) {
      _uiState.value = _uiState.value.copy(errorMessage = "Passwords do not match.")
      return
    }
    if (!acceptTerms.value) {
      _uiState.value = _uiState.value.copy(errorMessage = "Please agree to the Terms & Privacy Policy.")
      return
    }

    viewModelScope.launch {
      _uiState.value = _uiState.value.copy(isLoading = true, errorMessage = null)
      val result = repository.registerWithEmail(name, email, pass)
      if (result.isSuccess) {
        _uiState.value = _uiState.value.copy(
          isLoading = false,
          successMessage = "Account created! Verification email dispatched.",
          isEmailVerificationOpen = true
        )
      } else {
        _uiState.value = _uiState.value.copy(
          isLoading = false,
          errorMessage = result.exceptionOrNull()?.localizedMessage ?: "Registration failed."
        )
      }
    }
  }

  fun signInWithGoogle(email: String = "alex.wright@google.com", name: String = "Alexander Wright") {
    viewModelScope.launch {
      _uiState.value = _uiState.value.copy(isLoading = true, errorMessage = null)
      val result = repository.signInWithGoogle(email, name)
      _uiState.value = _uiState.value.copy(
        isLoading = false,
        successMessage = "Signed in with Google as $name"
      )
    }
  }

  fun openForgotPassword() {
    forgotPasswordEmail.value = loginEmail.value.ifEmpty { registerEmail.value }
    _uiState.value = _uiState.value.copy(
      isForgotPasswordOpen = true,
      resetEmailSent = false,
      errorMessage = null
    )
  }

  fun closeForgotPassword() {
    _uiState.value = _uiState.value.copy(isForgotPasswordOpen = false)
  }

  fun sendForgotPasswordEmail() {
    val email = forgotPasswordEmail.value.trim()
    if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
      _uiState.value = _uiState.value.copy(errorMessage = "Enter a valid email address to reset password.")
      return
    }

    viewModelScope.launch {
      _uiState.value = _uiState.value.copy(isLoading = true, errorMessage = null)
      repository.sendPasswordReset(email)
      _uiState.value = _uiState.value.copy(
        isLoading = false,
        resetEmailSent = true,
        successMessage = "Password reset instructions sent to $email"
      )
    }
  }

  fun openEmailVerification() {
    _uiState.value = _uiState.value.copy(isEmailVerificationOpen = true)
  }

  fun closeEmailVerification() {
    _uiState.value = _uiState.value.copy(isEmailVerificationOpen = false)
  }

  fun resendVerificationEmail() {
    viewModelScope.launch {
      _uiState.value = _uiState.value.copy(isLoading = true)
      repository.resendEmailVerification()
      _uiState.value = _uiState.value.copy(
        isLoading = false,
        verificationEmailSent = true,
        successMessage = "Verification link re-sent!"
      )
    }
  }

  fun upgradeSubscriptionPlan(plan: SubscriptionPlan) {
    repository.updateSubscriptionPlan(plan)
    _uiState.value = _uiState.value.copy(
      successMessage = "Subscription upgraded to ${plan.displayName}!"
    )
  }

  fun logout() {
    repository.logout()
    _uiState.value = AuthUiState()
    loginEmail.value = ""
    loginPassword.value = ""
    registerName.value = ""
    registerEmail.value = ""
    registerPassword.value = ""
    registerConfirmPassword.value = ""
  }

  fun clearMessages() {
    _uiState.value = _uiState.value.copy(errorMessage = null, successMessage = null)
  }

  fun calculatePasswordStrength(pass: String): Float {
    if (pass.isEmpty()) return 0f
    var score = 0f
    if (pass.length >= 6) score += 0.25f
    if (pass.length >= 10) score += 0.25f
    if (pass.any { it.isDigit() }) score += 0.25f
    if (pass.any { !it.isLetterOrDigit() } || pass.any { it.isUpperCase() }) score += 0.25f
    return score
  }
}
