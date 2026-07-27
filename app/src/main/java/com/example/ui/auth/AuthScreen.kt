package com.example.ui.auth

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Error
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.MarkEmailRead
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.data.auth.AuthTab
import com.example.data.auth.AuthViewModel
import com.example.data.auth.UserSession
import com.example.ui.theme.BackgroundDark
import com.example.ui.theme.BrandAccent
import com.example.ui.theme.BrandAmber
import com.example.ui.theme.BrandPrimary
import com.example.ui.theme.BrandPurple
import com.example.ui.theme.BrandSecondary
import com.example.ui.theme.SurfaceCard
import com.example.ui.theme.SurfaceCardBorder
import com.example.ui.theme.SurfaceDark
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary

@Composable
fun AuthScreen(
  viewModel: AuthViewModel,
  onNavigateBack: () -> Unit = {},
  onAuthSuccess: () -> Unit = {}
) {
  val uiState by viewModel.uiState.collectAsState()
  val userSession by viewModel.userSession.collectAsState()
  val focusManager = LocalFocusManager.current
  val scrollState = rememberScrollState()

  Box(
    modifier = Modifier
      .fillMaxSize()
      .background(BackgroundDark)
  ) {
    Column(
      modifier = Modifier
        .fillMaxSize()
        .verticalScroll(scrollState)
    ) {
      // Navigation Header
      Row(
        modifier = Modifier
          .fillMaxWidth()
          .statusBarsPadding()
          .padding(horizontal = 20.dp, vertical = 16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Row(
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
          IconButton(
            onClick = onNavigateBack,
            modifier = Modifier
              .size(38.dp)
              .clip(CircleShape)
              .background(SurfaceCard)
              .testTag("auth_back_button")
          ) {
            Icon(
              imageVector = Icons.AutoMirrored.Filled.ArrowBack,
              contentDescription = "Back",
              tint = TextPrimary
            )
          }

          Text(
            text = "Back to Home",
            color = TextSecondary,
            fontSize = 13.sp,
            fontWeight = FontWeight.Medium
          )
        }

        // Brand badge
        Row(
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
          Box(
            modifier = Modifier
              .size(32.dp)
              .clip(RoundedCornerShape(8.dp))
              .background(
                Brush.linearGradient(colors = listOf(BrandPrimary, BrandSecondary))
              ),
            contentAlignment = Alignment.Center
          ) {
            Icon(
              imageVector = Icons.Default.Description,
              contentDescription = null,
              tint = Color.White,
              modifier = Modifier.size(16.dp)
            )
          }

          Text(
            text = "Resume Studio",
            color = TextPrimary,
            fontSize = 15.sp,
            fontWeight = FontWeight.Bold
          )
        }
      }

      Spacer(modifier = Modifier.height(16.dp))

      // Main Container Body
      Column(
        modifier = Modifier
          .fillMaxWidth()
          .padding(horizontal = 20.dp, vertical = 8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
      ) {
        if (userSession != null) {
          // ACTIVE SECURE USER SESSION CARD
          ActiveSessionCard(
            session = userSession!!,
            onOpenVerification = { viewModel.openEmailVerification() },
            onLogout = { viewModel.logout() },
            onContinue = onAuthSuccess
          )
        } else {
          // AUTHENTICATION CONTAINER (LOGIN / REGISTER / GOOGLE)
          AuthContainerCard(
            viewModel = viewModel,
            uiState = uiState,
            onSuccess = onAuthSuccess
          )
        }
      }

      Spacer(modifier = Modifier.height(40.dp))
      Spacer(modifier = Modifier.navigationBarsPadding())
    }

    // Forgot Password Modal
    if (uiState.isForgotPasswordOpen) {
      ForgotPasswordDialog(
        viewModel = viewModel,
        uiState = uiState,
        onDismiss = { viewModel.closeForgotPassword() }
      )
    }

    // Email Verification Modal
    if (uiState.isEmailVerificationOpen && userSession != null) {
      EmailVerificationDialog(
        session = userSession!!,
        uiState = uiState,
        onResend = { viewModel.resendVerificationEmail() },
        onDismiss = { viewModel.closeEmailVerification() }
      )
    }
  }
}

// -----------------------------------------------------------------------------
// AUTH CONTAINER CARD
// -----------------------------------------------------------------------------
@Composable
fun AuthContainerCard(
  viewModel: AuthViewModel,
  uiState: com.example.data.auth.AuthUiState,
  onSuccess: () -> Unit
) {
  Card(
    modifier = Modifier
      .fillMaxWidth()
      .widthIn(max = 480.dp),
    shape = RoundedCornerShape(24.dp),
    colors = CardDefaults.cardColors(containerColor = SurfaceCard),
    border = CardDefaults.outlinedCardBorder().copy(
      brush = Brush.verticalGradient(
        colors = listOf(BrandPrimary.copy(alpha = 0.5f), SurfaceCardBorder)
      )
    )
  ) {
    Column(
      modifier = Modifier.padding(24.dp),
      horizontalAlignment = Alignment.CenterHorizontally
    ) {
      // Header Titles
      Box(
        modifier = Modifier
          .size(48.dp)
          .clip(RoundedCornerShape(14.dp))
          .background(BrandPrimary.copy(alpha = 0.2f)),
        contentAlignment = Alignment.Center
      ) {
        Icon(
          imageVector = Icons.Default.Security,
          contentDescription = null,
          tint = BrandPrimary,
          modifier = Modifier.size(26.dp)
        )
      }

      Spacer(modifier = Modifier.height(14.dp))

      Text(
        text = if (uiState.currentTab == AuthTab.LOGIN) "Welcome Back" else "Create Your Account",
        color = TextPrimary,
        fontSize = 24.sp,
        fontWeight = FontWeight.Bold,
        textAlign = TextAlign.Center
      )

      Text(
        text = if (uiState.currentTab == AuthTab.LOGIN)
          "Sign in to access your AI resume workspace"
        else
          "Start crafting high-converting tech & design resumes",
        color = TextSecondary,
        fontSize = 13.sp,
        textAlign = TextAlign.Center,
        modifier = Modifier.padding(top = 4.dp)
      )

      Spacer(modifier = Modifier.height(20.dp))

      // Segmented Tab Control (Linear style)
      Row(
        modifier = Modifier
          .fillMaxWidth()
          .clip(RoundedCornerShape(12.dp))
          .background(SurfaceDark)
          .padding(4.dp)
      ) {
        val loginTabBg by animateColorAsState(
          targetValue = if (uiState.currentTab == AuthTab.LOGIN) BrandPrimary else Color.Transparent,
          animationSpec = tween(200),
          label = "loginTabBg"
        )
        val registerTabBg by animateColorAsState(
          targetValue = if (uiState.currentTab == AuthTab.REGISTER) BrandPrimary else Color.Transparent,
          animationSpec = tween(200),
          label = "registerTabBg"
        )

        Box(
          modifier = Modifier
            .weight(1f)
            .height(38.dp)
            .clip(RoundedCornerShape(10.dp))
            .background(loginTabBg)
            .clickable { viewModel.setTab(AuthTab.LOGIN) }
            .testTag("auth_tab_login"),
          contentAlignment = Alignment.Center
        ) {
          Text(
            text = "Sign In",
            color = if (uiState.currentTab == AuthTab.LOGIN) Color.White else TextMuted,
            fontSize = 13.sp,
            fontWeight = FontWeight.Bold
          )
        }

        Box(
          modifier = Modifier
            .weight(1f)
            .height(38.dp)
            .clip(RoundedCornerShape(10.dp))
            .background(registerTabBg)
            .clickable { viewModel.setTab(AuthTab.REGISTER) }
            .testTag("auth_tab_register"),
          contentAlignment = Alignment.Center
        ) {
          Text(
            text = "Register",
            color = if (uiState.currentTab == AuthTab.REGISTER) Color.White else TextMuted,
            fontSize = 13.sp,
            fontWeight = FontWeight.Bold
          )
        }
      }

      Spacer(modifier = Modifier.height(20.dp))

      // Feedback Banners (Error / Success)
      if (uiState.errorMessage != null) {
        Row(
          modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(10.dp))
            .background(Color(0xFF3F1D1D))
            .border(1.dp, Color(0xFFEF4444), RoundedCornerShape(10.dp))
            .padding(12.dp),
          verticalAlignment = Alignment.CenterVertically
        ) {
          Icon(
            imageVector = Icons.Default.Error,
            contentDescription = null,
            tint = Color(0xFFEF4444),
            modifier = Modifier.size(18.dp)
          )
          Spacer(modifier = Modifier.width(8.dp))
          Text(
            text = uiState.errorMessage,
            color = Color(0xFFFCA5A5),
            fontSize = 12.sp,
            lineHeight = 16.sp
          )
        }
        Spacer(modifier = Modifier.height(14.dp))
      }

      if (uiState.successMessage != null) {
        Row(
          modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(10.dp))
            .background(Color(0xFF133829))
            .border(1.dp, BrandAccent, RoundedCornerShape(10.dp))
            .padding(12.dp),
          verticalAlignment = Alignment.CenterVertically
        ) {
          Icon(
            imageVector = Icons.Default.CheckCircle,
            contentDescription = null,
            tint = BrandAccent,
            modifier = Modifier.size(18.dp)
          )
          Spacer(modifier = Modifier.width(8.dp))
          Text(
            text = uiState.successMessage,
            color = Color(0xFFA7F3D0),
            fontSize = 12.sp,
            lineHeight = 16.sp
          )
        }
        Spacer(modifier = Modifier.height(14.dp))
      }

      // GOOGLE SIGN IN BUTTON
      OutlinedButton(
        onClick = { viewModel.signInWithGoogle() },
        modifier = Modifier
          .fillMaxWidth()
          .height(48.dp)
          .testTag("auth_google_signin_button"),
        shape = RoundedCornerShape(12.dp),
        border = ButtonDefaults.outlinedButtonBorder.copy(
          brush = Brush.horizontalGradient(listOf(SurfaceCardBorder, SurfaceCardBorder))
        ),
        colors = ButtonDefaults.outlinedButtonColors(
          containerColor = SurfaceDark,
          contentColor = TextPrimary
        )
      ) {
        Row(
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.Center
        ) {
          Icon(
            imageVector = Icons.Default.AutoAwesome,
            contentDescription = null,
            tint = BrandSecondary,
            modifier = Modifier.size(18.dp)
          )
          Spacer(modifier = Modifier.width(10.dp))
          Text(
            text = "Continue with Google",
            fontSize = 14.sp,
            fontWeight = FontWeight.SemiBold
          )
        }
      }

      Spacer(modifier = Modifier.height(18.dp))

      // OR Divider
      Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
      ) {
        HorizontalDivider(modifier = Modifier.weight(1f), color = SurfaceCardBorder)
        Text(
          text = "OR EMAIL",
          color = TextMuted,
          fontSize = 10.sp,
          fontWeight = FontWeight.Bold,
          modifier = Modifier.padding(horizontal = 12.dp)
        )
        HorizontalDivider(modifier = Modifier.weight(1f), color = SurfaceCardBorder)
      }

      Spacer(modifier = Modifier.height(18.dp))

      // FORM CONTENT BASED ON TAB
      if (uiState.currentTab == AuthTab.LOGIN) {
        LoginForm(viewModel = viewModel, uiState = uiState)
      } else {
        RegisterForm(viewModel = viewModel, uiState = uiState)
      }
    }
  }
}

// -----------------------------------------------------------------------------
// LOGIN FORM
// -----------------------------------------------------------------------------
@Composable
fun LoginForm(
  viewModel: AuthViewModel,
  uiState: com.example.data.auth.AuthUiState
) {
  val email by viewModel.loginEmail.collectAsState()
  val pass by viewModel.loginPassword.collectAsState()
  val rememberMe by viewModel.rememberMe.collectAsState()
  var passwordVisible by remember { mutableStateOf(false) }

  Column(modifier = Modifier.fillMaxWidth()) {
    // Email Field
    Text(text = "EMAIL ADDRESS", color = TextSecondary, fontSize = 10.sp, fontWeight = FontWeight.Bold)
    Spacer(modifier = Modifier.height(6.dp))
    OutlinedTextField(
      value = email,
      onValueChange = { viewModel.loginEmail.value = it },
      modifier = Modifier
        .fillMaxWidth()
        .testTag("login_input_email"),
      placeholder = { Text("alex@example.com", color = TextMuted, fontSize = 13.sp) },
      leadingIcon = {
        Icon(imageVector = Icons.Default.Email, contentDescription = null, tint = TextMuted)
      },
      singleLine = true,
      keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email, imeAction = ImeAction.Next),
      shape = RoundedCornerShape(12.dp),
      colors = OutlinedTextFieldDefaults.colors(
        focusedBorderColor = BrandPrimary,
        unfocusedBorderColor = SurfaceCardBorder,
        focusedContainerColor = SurfaceDark,
        unfocusedContainerColor = SurfaceDark,
        focusedTextColor = TextPrimary,
        unfocusedTextColor = TextPrimary
      )
    )

    Spacer(modifier = Modifier.height(14.dp))

    // Password Field
    Row(
      modifier = Modifier.fillMaxWidth(),
      horizontalArrangement = Arrangement.SpaceBetween,
      verticalAlignment = Alignment.CenterVertically
    ) {
      Text(text = "PASSWORD", color = TextSecondary, fontSize = 10.sp, fontWeight = FontWeight.Bold)
      Text(
        text = "Forgot Password?",
        color = BrandSecondary,
        fontSize = 11.sp,
        fontWeight = FontWeight.Bold,
        modifier = Modifier
          .clickable { viewModel.openForgotPassword() }
          .testTag("login_forgot_password_link")
      )
    }

    Spacer(modifier = Modifier.height(6.dp))

    OutlinedTextField(
      value = pass,
      onValueChange = { viewModel.loginPassword.value = it },
      modifier = Modifier
        .fillMaxWidth()
        .testTag("login_input_password"),
      placeholder = { Text("••••••••", color = TextMuted, fontSize = 13.sp) },
      leadingIcon = {
        Icon(imageVector = Icons.Default.Lock, contentDescription = null, tint = TextMuted)
      },
      trailingIcon = {
        IconButton(onClick = { passwordVisible = !passwordVisible }) {
          Icon(
            imageVector = if (passwordVisible) Icons.Default.VisibilityOff else Icons.Default.Visibility,
            contentDescription = "Toggle password visibility",
            tint = TextMuted
          )
        }
      },
      visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
      singleLine = true,
      keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password, imeAction = ImeAction.Done),
      shape = RoundedCornerShape(12.dp),
      colors = OutlinedTextFieldDefaults.colors(
        focusedBorderColor = BrandPrimary,
        unfocusedBorderColor = SurfaceCardBorder,
        focusedContainerColor = SurfaceDark,
        unfocusedContainerColor = SurfaceDark,
        focusedTextColor = TextPrimary,
        unfocusedTextColor = TextPrimary
      )
    )

    Spacer(modifier = Modifier.height(12.dp))

    // Remember Me Switch
    Row(
      modifier = Modifier.fillMaxWidth(),
      verticalAlignment = Alignment.CenterVertically,
      horizontalArrangement = Arrangement.SpaceBetween
    ) {
      Row(verticalAlignment = Alignment.CenterVertically) {
        Switch(
          checked = rememberMe,
          onCheckedChange = { viewModel.rememberMe.value = it },
          colors = SwitchDefaults.colors(
            checkedThumbColor = Color.White,
            checkedTrackColor = BrandPrimary,
            uncheckedThumbColor = TextMuted,
            uncheckedTrackColor = SurfaceDark
          ),
          modifier = Modifier.testTag("login_remember_me_switch")
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(text = "Keep me signed in", color = TextSecondary, fontSize = 12.sp)
      }
    }

    Spacer(modifier = Modifier.height(20.dp))

    // Submit Button
    Button(
      onClick = { viewModel.login() },
      enabled = !uiState.isLoading,
      modifier = Modifier
        .fillMaxWidth()
        .height(50.dp)
        .testTag("login_submit_button"),
      shape = RoundedCornerShape(12.dp),
      colors = ButtonDefaults.buttonColors(
        containerColor = BrandPrimary,
        contentColor = Color.White
      )
    ) {
      if (uiState.isLoading) {
        CircularProgressIndicator(modifier = Modifier.size(20.dp), color = Color.White, strokeWidth = 2.dp)
      } else {
        Row(
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.Center
        ) {
          Text(text = "Sign In to Resume Studio", fontSize = 15.sp, fontWeight = FontWeight.Bold)
          Spacer(modifier = Modifier.width(8.dp))
          Icon(
            imageVector = Icons.AutoMirrored.Filled.ArrowForward,
            contentDescription = null,
            modifier = Modifier.size(16.dp)
          )
        }
      }
    }
  }
}

// -----------------------------------------------------------------------------
// REGISTER FORM
// -----------------------------------------------------------------------------
@Composable
fun RegisterForm(
  viewModel: AuthViewModel,
  uiState: com.example.data.auth.AuthUiState
) {
  val name by viewModel.registerName.collectAsState()
  val email by viewModel.registerEmail.collectAsState()
  val pass by viewModel.registerPassword.collectAsState()
  val confirm by viewModel.registerConfirmPassword.collectAsState()
  val acceptTerms by viewModel.acceptTerms.collectAsState()

  var passwordVisible by remember { mutableStateOf(false) }
  val passStrength = viewModel.calculatePasswordStrength(pass)

  Column(modifier = Modifier.fillMaxWidth()) {
    // Full Name Field
    Text(text = "FULL NAME", color = TextSecondary, fontSize = 10.sp, fontWeight = FontWeight.Bold)
    Spacer(modifier = Modifier.height(6.dp))
    OutlinedTextField(
      value = name,
      onValueChange = { viewModel.registerName.value = it },
      modifier = Modifier
        .fillMaxWidth()
        .testTag("register_input_name"),
      placeholder = { Text("Alexander Wright", color = TextMuted, fontSize = 13.sp) },
      leadingIcon = {
        Icon(imageVector = Icons.Default.Person, contentDescription = null, tint = TextMuted)
      },
      singleLine = true,
      shape = RoundedCornerShape(12.dp),
      colors = OutlinedTextFieldDefaults.colors(
        focusedBorderColor = BrandPrimary,
        unfocusedBorderColor = SurfaceCardBorder,
        focusedContainerColor = SurfaceDark,
        unfocusedContainerColor = SurfaceDark,
        focusedTextColor = TextPrimary,
        unfocusedTextColor = TextPrimary
      )
    )

    Spacer(modifier = Modifier.height(12.dp))

    // Email Field
    Text(text = "EMAIL ADDRESS", color = TextSecondary, fontSize = 10.sp, fontWeight = FontWeight.Bold)
    Spacer(modifier = Modifier.height(6.dp))
    OutlinedTextField(
      value = email,
      onValueChange = { viewModel.registerEmail.value = it },
      modifier = Modifier
        .fillMaxWidth()
        .testTag("register_input_email"),
      placeholder = { Text("alex@example.com", color = TextMuted, fontSize = 13.sp) },
      leadingIcon = {
        Icon(imageVector = Icons.Default.Email, contentDescription = null, tint = TextMuted)
      },
      singleLine = true,
      keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
      shape = RoundedCornerShape(12.dp),
      colors = OutlinedTextFieldDefaults.colors(
        focusedBorderColor = BrandPrimary,
        unfocusedBorderColor = SurfaceCardBorder,
        focusedContainerColor = SurfaceDark,
        unfocusedContainerColor = SurfaceDark,
        focusedTextColor = TextPrimary,
        unfocusedTextColor = TextPrimary
      )
    )

    Spacer(modifier = Modifier.height(12.dp))

    // Password Field
    Text(text = "CREATE PASSWORD", color = TextSecondary, fontSize = 10.sp, fontWeight = FontWeight.Bold)
    Spacer(modifier = Modifier.height(6.dp))
    OutlinedTextField(
      value = pass,
      onValueChange = { viewModel.registerPassword.value = it },
      modifier = Modifier
        .fillMaxWidth()
        .testTag("register_input_password"),
      placeholder = { Text("At least 6 chars", color = TextMuted, fontSize = 13.sp) },
      leadingIcon = {
        Icon(imageVector = Icons.Default.Lock, contentDescription = null, tint = TextMuted)
      },
      trailingIcon = {
        IconButton(onClick = { passwordVisible = !passwordVisible }) {
          Icon(
            imageVector = if (passwordVisible) Icons.Default.VisibilityOff else Icons.Default.Visibility,
            contentDescription = null,
            tint = TextMuted
          )
        }
      },
      visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
      singleLine = true,
      shape = RoundedCornerShape(12.dp),
      colors = OutlinedTextFieldDefaults.colors(
        focusedBorderColor = BrandPrimary,
        unfocusedBorderColor = SurfaceCardBorder,
        focusedContainerColor = SurfaceDark,
        unfocusedContainerColor = SurfaceDark,
        focusedTextColor = TextPrimary,
        unfocusedTextColor = TextPrimary
      )
    )

    // Password Strength Indicator
    if (pass.isNotEmpty()) {
      Spacer(modifier = Modifier.height(6.dp))
      Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
      ) {
        LinearProgressIndicator(
          progress = { passStrength },
          modifier = Modifier
            .weight(1f)
            .height(4.dp)
            .clip(RoundedCornerShape(2.dp)),
          color = when {
            passStrength <= 0.25f -> Color(0xFFEF4444)
            passStrength <= 0.50f -> BrandAmber
            passStrength <= 0.75f -> BrandSecondary
            else -> BrandAccent
          },
          trackColor = SurfaceDark
        )
        Spacer(modifier = Modifier.width(10.dp))
        Text(
          text = when {
            passStrength <= 0.25f -> "Weak"
            passStrength <= 0.50f -> "Fair"
            passStrength <= 0.75f -> "Good"
            else -> "Strong"
          },
          color = when {
            passStrength <= 0.25f -> Color(0xFFEF4444)
            passStrength <= 0.50f -> BrandAmber
            passStrength <= 0.75f -> BrandSecondary
            else -> BrandAccent
          },
          fontSize = 11.sp,
          fontWeight = FontWeight.Bold
        )
      }
    }

    Spacer(modifier = Modifier.height(12.dp))

    // Confirm Password
    Text(text = "CONFIRM PASSWORD", color = TextSecondary, fontSize = 10.sp, fontWeight = FontWeight.Bold)
    Spacer(modifier = Modifier.height(6.dp))
    OutlinedTextField(
      value = confirm,
      onValueChange = { viewModel.registerConfirmPassword.value = it },
      modifier = Modifier
        .fillMaxWidth()
        .testTag("register_input_confirm_password"),
      placeholder = { Text("Re-enter password", color = TextMuted, fontSize = 13.sp) },
      leadingIcon = {
        Icon(imageVector = Icons.Default.Lock, contentDescription = null, tint = TextMuted)
      },
      visualTransformation = PasswordVisualTransformation(),
      singleLine = true,
      shape = RoundedCornerShape(12.dp),
      colors = OutlinedTextFieldDefaults.colors(
        focusedBorderColor = BrandPrimary,
        unfocusedBorderColor = SurfaceCardBorder,
        focusedContainerColor = SurfaceDark,
        unfocusedContainerColor = SurfaceDark,
        focusedTextColor = TextPrimary,
        unfocusedTextColor = TextPrimary
      )
    )

    Spacer(modifier = Modifier.height(14.dp))

    // Accept Terms Checkbox
    Row(
      modifier = Modifier.fillMaxWidth(),
      verticalAlignment = Alignment.CenterVertically
    ) {
      Checkbox(
        checked = acceptTerms,
        onCheckedChange = { viewModel.acceptTerms.value = it },
        colors = CheckboxDefaults.colors(
          checkedColor = BrandPrimary,
          uncheckedColor = SurfaceCardBorder,
          checkmarkColor = Color.White
        ),
        modifier = Modifier.testTag("register_terms_checkbox")
      )
      Text(
        text = "I agree to the Terms of Service & Privacy Policy",
        color = TextSecondary,
        fontSize = 11.sp,
        modifier = Modifier.padding(start = 4.dp)
      )
    }

    Spacer(modifier = Modifier.height(18.dp))

    // Register Button
    Button(
      onClick = { viewModel.register() },
      enabled = !uiState.isLoading,
      modifier = Modifier
        .fillMaxWidth()
        .height(50.dp)
        .testTag("register_submit_button"),
      shape = RoundedCornerShape(12.dp),
      colors = ButtonDefaults.buttonColors(
        containerColor = BrandPrimary,
        contentColor = Color.White
      )
    ) {
      if (uiState.isLoading) {
        CircularProgressIndicator(modifier = Modifier.size(20.dp), color = Color.White, strokeWidth = 2.dp)
      } else {
        Row(
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.Center
        ) {
          Text(text = "Create Free Account", fontSize = 15.sp, fontWeight = FontWeight.Bold)
          Spacer(modifier = Modifier.width(8.dp))
          Icon(
            imageVector = Icons.AutoMirrored.Filled.ArrowForward,
            contentDescription = null,
            modifier = Modifier.size(16.dp)
          )
        }
      }
    }
  }
}

// -----------------------------------------------------------------------------
// ACTIVE USER SESSION CARD
// -----------------------------------------------------------------------------
@Composable
fun ActiveSessionCard(
  session: UserSession,
  onOpenVerification: () -> Unit,
  onLogout: () -> Unit,
  onContinue: () -> Unit
) {
  Card(
    modifier = Modifier
      .fillMaxWidth()
      .widthIn(max = 480.dp),
    shape = RoundedCornerShape(24.dp),
    colors = CardDefaults.cardColors(containerColor = SurfaceCard),
    border = CardDefaults.outlinedCardBorder().copy(
      brush = Brush.verticalGradient(
        colors = listOf(BrandAccent.copy(alpha = 0.5f), SurfaceCardBorder)
      )
    )
  ) {
    Column(
      modifier = Modifier.padding(24.dp),
      horizontalAlignment = Alignment.CenterHorizontally
    ) {
      // User Avatar Initials
      val initials = session.displayName
        .split(" ")
        .mapNotNull { it.firstOrNull()?.toString() }
        .take(2)
        .joinToString("")
        .ifEmpty { "U" }

      Box(
        modifier = Modifier
          .size(64.dp)
          .clip(CircleShape)
          .background(
            Brush.linearGradient(colors = listOf(BrandPrimary, BrandSecondary))
          ),
        contentAlignment = Alignment.Center
      ) {
        Text(
          text = initials.uppercase(),
          color = Color.White,
          fontSize = 22.sp,
          fontWeight = FontWeight.Bold
        )
      }

      Spacer(modifier = Modifier.height(12.dp))

      Text(
        text = session.displayName,
        color = TextPrimary,
        fontSize = 20.sp,
        fontWeight = FontWeight.Bold
      )

      Text(
        text = session.email,
        color = TextSecondary,
        fontSize = 13.sp
      )

      Spacer(modifier = Modifier.height(16.dp))

      // Provider & Email Verified Badges Row
      Row(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically
      ) {
        Box(
          modifier = Modifier
            .clip(RoundedCornerShape(8.dp))
            .background(SurfaceDark)
            .border(1.dp, SurfaceCardBorder, RoundedCornerShape(8.dp))
            .padding(horizontal = 10.dp, vertical = 4.dp)
        ) {
          Text(
            text = "Auth: ${session.authProvider}",
            color = TextMuted,
            fontSize = 11.sp,
            fontWeight = FontWeight.Medium
          )
        }

        Box(
          modifier = Modifier
            .clip(RoundedCornerShape(8.dp))
            .background(
              if (session.isEmailVerified) BrandAccent.copy(alpha = 0.15f) else BrandAmber.copy(alpha = 0.15f)
            )
            .border(
              1.dp,
              if (session.isEmailVerified) BrandAccent else BrandAmber,
              RoundedCornerShape(8.dp)
            )
            .clickable {
              if (!session.isEmailVerified) onOpenVerification()
            }
            .padding(horizontal = 10.dp, vertical = 4.dp)
        ) {
          Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
            Icon(
              imageVector = if (session.isEmailVerified) Icons.Default.CheckCircle else Icons.Default.MarkEmailRead,
              contentDescription = null,
              tint = if (session.isEmailVerified) BrandAccent else BrandAmber,
              modifier = Modifier.size(12.dp)
            )
            Text(
              text = if (session.isEmailVerified) "Verified" else "Verify Email",
              color = if (session.isEmailVerified) BrandAccent else BrandAmber,
              fontSize = 11.sp,
              fontWeight = FontWeight.Bold
            )
          }
        }
      }

      Spacer(modifier = Modifier.height(24.dp))

      // Action Buttons
      Button(
        onClick = onContinue,
        modifier = Modifier
          .fillMaxWidth()
          .height(50.dp)
          .testTag("session_continue_button"),
        shape = RoundedCornerShape(12.dp),
        colors = ButtonDefaults.buttonColors(
          containerColor = BrandPrimary,
          contentColor = Color.White
        )
      ) {
        Row(
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.Center
        ) {
          Icon(
            imageVector = Icons.Default.AutoAwesome,
            contentDescription = null,
            modifier = Modifier.size(18.dp)
          )
          Spacer(modifier = Modifier.width(8.dp))
          Text(text = "Launch Resume Workspace", fontSize = 15.sp, fontWeight = FontWeight.Bold)
        }
      }

      Spacer(modifier = Modifier.height(10.dp))

      OutlinedButton(
        onClick = onLogout,
        modifier = Modifier
          .fillMaxWidth()
          .height(44.dp)
          .testTag("session_logout_button"),
        shape = RoundedCornerShape(12.dp),
        colors = ButtonDefaults.outlinedButtonColors(contentColor = Color(0xFFEF4444)),
        border = ButtonDefaults.outlinedButtonBorder.copy(
          brush = Brush.horizontalGradient(listOf(Color(0xFF7F1D1D), Color(0xFF7F1D1D)))
        )
      ) {
        Text(text = "Sign Out", fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
      }
    }
  }
}

// -----------------------------------------------------------------------------
// FORGOT PASSWORD DIALOG
// -----------------------------------------------------------------------------
@Composable
fun ForgotPasswordDialog(
  viewModel: AuthViewModel,
  uiState: com.example.data.auth.AuthUiState,
  onDismiss: () -> Unit
) {
  val email by viewModel.forgotPasswordEmail.collectAsState()

  Dialog(onDismissRequest = onDismiss) {
    Surface(
      shape = RoundedCornerShape(20.dp),
      color = SurfaceCard,
      modifier = Modifier.padding(16.dp),
      border = CardDefaults.outlinedCardBorder().copy(
        brush = Brush.verticalGradient(listOf(BrandPrimary, SurfaceCardBorder))
      )
    ) {
      Column(
        modifier = Modifier.padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
      ) {
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Text(text = "Reset Password", color = TextPrimary, fontSize = 18.sp, fontWeight = FontWeight.Bold)
          IconButton(onClick = onDismiss) {
            Icon(imageVector = Icons.Default.Close, contentDescription = "Close", tint = TextMuted)
          }
        }

        Spacer(modifier = Modifier.height(8.dp))

        Text(
          text = "Enter your registered email address and we'll send a secure password reset link.",
          color = TextSecondary,
          fontSize = 12.sp,
          lineHeight = 16.sp
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
          value = email,
          onValueChange = { viewModel.forgotPasswordEmail.value = it },
          modifier = Modifier
            .fillMaxWidth()
            .testTag("forgot_password_input_email"),
          placeholder = { Text("alex@example.com", color = TextMuted, fontSize = 13.sp) },
          leadingIcon = {
            Icon(imageVector = Icons.Default.Email, contentDescription = null, tint = TextMuted)
          },
          singleLine = true,
          shape = RoundedCornerShape(12.dp),
          colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = BrandPrimary,
            unfocusedBorderColor = SurfaceCardBorder,
            focusedContainerColor = SurfaceDark,
            unfocusedContainerColor = SurfaceDark,
            focusedTextColor = TextPrimary,
            unfocusedTextColor = TextPrimary
          )
        )

        Spacer(modifier = Modifier.height(20.dp))

        Button(
          onClick = { viewModel.sendForgotPasswordEmail() },
          enabled = !uiState.isLoading,
          modifier = Modifier
            .fillMaxWidth()
            .height(46.dp)
            .testTag("forgot_password_submit_button"),
          shape = RoundedCornerShape(10.dp),
          colors = ButtonDefaults.buttonColors(containerColor = BrandPrimary, contentColor = Color.White)
        ) {
          if (uiState.isLoading) {
            CircularProgressIndicator(modifier = Modifier.size(18.dp), color = Color.White, strokeWidth = 2.dp)
          } else {
            Text(text = "Send Reset Link", fontSize = 14.sp, fontWeight = FontWeight.Bold)
          }
        }
      }
    }
  }
}

// -----------------------------------------------------------------------------
// EMAIL VERIFICATION DIALOG
// -----------------------------------------------------------------------------
@Composable
fun EmailVerificationDialog(
  session: UserSession,
  uiState: com.example.data.auth.AuthUiState,
  onResend: () -> Unit,
  onDismiss: () -> Unit
) {
  Dialog(onDismissRequest = onDismiss) {
    Surface(
      shape = RoundedCornerShape(20.dp),
      color = SurfaceCard,
      modifier = Modifier.padding(16.dp),
      border = CardDefaults.outlinedCardBorder().copy(
        brush = Brush.verticalGradient(listOf(BrandSecondary, SurfaceCardBorder))
      )
    ) {
      Column(
        modifier = Modifier.padding(22.dp),
        horizontalAlignment = Alignment.CenterHorizontally
      ) {
        Box(
          modifier = Modifier
            .size(52.dp)
            .clip(CircleShape)
            .background(BrandSecondary.copy(alpha = 0.2f)),
          contentAlignment = Alignment.Center
        ) {
          Icon(
            imageVector = Icons.Default.MarkEmailRead,
            contentDescription = null,
            tint = BrandSecondary,
            modifier = Modifier.size(28.dp)
          )
        }

        Spacer(modifier = Modifier.height(14.dp))

        Text(
          text = "Verify Your Email Address",
          color = TextPrimary,
          fontSize = 18.sp,
          fontWeight = FontWeight.Bold,
          textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(6.dp))

        Text(
          text = "We sent a confirmation link to:\n${session.email}",
          color = TextSecondary,
          fontSize = 12.sp,
          textAlign = TextAlign.Center,
          lineHeight = 16.sp
        )

        Spacer(modifier = Modifier.height(20.dp))

        Button(
          onClick = onResend,
          enabled = !uiState.isLoading,
          modifier = Modifier
            .fillMaxWidth()
            .height(44.dp)
            .testTag("verification_resend_button"),
          shape = RoundedCornerShape(10.dp),
          colors = ButtonDefaults.buttonColors(containerColor = BrandSecondary, contentColor = Color.White)
        ) {
          Text(text = "Resend Verification Email", fontSize = 13.sp, fontWeight = FontWeight.Bold)
        }

        Spacer(modifier = Modifier.height(8.dp))

        TextButton(onClick = onDismiss) {
          Text(text = "I'll verify later", color = TextMuted, fontSize = 12.sp)
        }
      }
    }
  }
}
