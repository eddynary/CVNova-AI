package com.example.ui.builder

import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
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
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Public
import androidx.compose.material.icons.filled.Save
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.Work
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
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.data.builder.BuilderStep
import com.example.data.builder.CertificateItem
import com.example.data.builder.EducationItem
import com.example.data.builder.LanguageItem
import com.example.data.builder.ProjectItem
import com.example.data.builder.ReferenceItem
import com.example.data.builder.ResumeBuilderUiState
import com.example.data.builder.ResumeBuilderViewModel
import com.example.data.builder.SkillItem
import com.example.data.builder.SocialLinkItem
import com.example.data.builder.WorkExperienceItem
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
import com.example.data.auth.SubscriptionPlan
import com.example.data.auth.UserSession
import com.example.ui.billing.StripeBillingModal
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary

@Composable
fun ResumeBuilderScreen(
  viewModel: ResumeBuilderViewModel,
  userSession: UserSession? = null,
  onNavigateBack: () -> Unit,
  onUpdateSubscription: (SubscriptionPlan) -> Unit = {}
) {
  val uiState by viewModel.uiState.collectAsState()
  val context = LocalContext.current

  LaunchedEffect(uiState.toastMessage) {
    uiState.toastMessage?.let { msg ->
      Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
      viewModel.clearToast()
    }
  }

  val configuration = LocalConfiguration.current
  val isWideScreen = configuration.screenWidthDp >= 840

  var showPreviewModal by remember { mutableStateOf(false) }
  var showStripeBillingModal by remember { mutableStateOf(false) }

  Box(
    modifier = Modifier
      .fillMaxSize()
      .background(BackgroundDark)
  ) {
    Column(
      modifier = Modifier
        .fillMaxSize()
        .statusBarsPadding()
        .navigationBarsPadding()
    ) {
      // 1. TOP HEADER & AUTOSAVE INDICATOR
      BuilderTopBar(
        uiState = uiState,
        onNavigateBack = onNavigateBack,
        onTogglePreview = { showPreviewModal = true },
        onTitleChange = { viewModel.updateTitle(it) }
      )

      // 2. PROGRESS INDICATOR & STEP PILl NAVIGATOR
      BuilderProgressIndicator(
        uiState = uiState,
        onStepSelected = { viewModel.goToStep(it) }
      )

      if (uiState.isAiLoading) {
        Box(
          modifier = Modifier
            .fillMaxWidth()
            .background(Brush.horizontalGradient(listOf(BrandPurple.copy(alpha = 0.85f), BrandPrimary.copy(alpha = 0.85f))))
            .padding(horizontal = 16.dp, vertical = 8.dp)
        ) {
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
          ) {
            CircularProgressIndicator(
              modifier = Modifier.size(16.dp),
              color = Color.White,
              strokeWidth = 2.dp
            )
            Spacer(modifier = Modifier.width(10.dp))
            Text(
              text = "✨ OpenAI Assistant generating human ATS-optimized content...",
              color = Color.White,
              fontSize = 12.sp,
              fontWeight = FontWeight.Bold
            )
          }
        }
      }

      // 3. STEP CONTENT BODY
      Box(
        modifier = Modifier
          .weight(1f)
          .fillMaxWidth()
      ) {
        Crossfade(
          targetState = uiState.currentStep,
          animationSpec = tween(250),
          label = "builder_step_crossfade"
        ) { step ->
          Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.TopCenter
          ) {
            Column(
              modifier = Modifier
                .widthIn(max = if (isWideScreen) 800.dp else 600.dp)
                .fillMaxHeight()
                .padding(horizontal = 20.dp, vertical = 12.dp)
            ) {
              val scrollState = rememberScrollState()

              Column(
                modifier = Modifier
                  .weight(1f)
                  .verticalScroll(scrollState),
                verticalArrangement = Arrangement.spacedBy(16.dp)
              ) {
                // Step Header Card
                StepHeaderCard(step = step)

                // Step Form Content
                when (step) {
                  BuilderStep.COUNTRY -> CountryStepContent(uiState = uiState, viewModel = viewModel)
                  BuilderStep.LANGUAGE -> LanguageStepContent(uiState = uiState, viewModel = viewModel)
                  BuilderStep.PROFESSION -> ProfessionStepContent(uiState = uiState, viewModel = viewModel)
                  BuilderStep.PERSONAL_INFO -> PersonalInfoStepContent(uiState = uiState, viewModel = viewModel)
                  BuilderStep.SUMMARY -> SummaryStepContent(uiState = uiState, viewModel = viewModel)
                  BuilderStep.EDUCATION -> EducationStepContent(uiState = uiState, viewModel = viewModel)
                  BuilderStep.WORK_EXPERIENCE -> WorkExperienceStepContent(uiState = uiState, viewModel = viewModel)
                  BuilderStep.SKILLS -> SkillsStepContent(uiState = uiState, viewModel = viewModel)
                  BuilderStep.LANGUAGES -> LanguagesStepContent(uiState = uiState, viewModel = viewModel)
                  BuilderStep.PROJECTS -> ProjectsStepContent(uiState = uiState, viewModel = viewModel)
                  BuilderStep.CERTIFICATES -> CertificatesStepContent(uiState = uiState, viewModel = viewModel)
                  BuilderStep.REFERENCES -> ReferencesStepContent(uiState = uiState, viewModel = viewModel)
                  BuilderStep.SOCIAL_LINKS -> SocialLinksStepContent(uiState = uiState, viewModel = viewModel)
                }

                Spacer(modifier = Modifier.height(24.dp))
              }

              // 4. BOTTOM NAVIGATION BUTTONS (BACK / NEXT)
              BuilderBottomNavButtons(
                uiState = uiState,
                onPrev = { viewModel.prevStep() },
                onNext = {
                  if (uiState.currentStep == BuilderStep.SOCIAL_LINKS) {
                    showPreviewModal = true
                  } else {
                    viewModel.nextStep()
                  }
                }
              )
            }
          }
        }
      }
    }

    // PREVIEW & EXPORT & AI COVER LETTER MODAL
    if (showPreviewModal) {
      ResumePreviewAndCoverLetterModal(
        uiState = uiState,
        viewModel = viewModel,
        userSession = userSession,
        onOpenBilling = { showStripeBillingModal = true },
        onDismiss = { showPreviewModal = false }
      )
    }

    if (showStripeBillingModal) {
      StripeBillingModal(
        currentPlan = userSession?.subscriptionPlan ?: SubscriptionPlan.FREE,
        onSelectPlan = { plan ->
          onUpdateSubscription(plan)
        },
        onDismiss = { showStripeBillingModal = false }
      )
    }
  }
}

// -----------------------------------------------------------------------------
// TOP BAR WITH AUTOSAVE
// -----------------------------------------------------------------------------
@Composable
fun BuilderTopBar(
  uiState: ResumeBuilderUiState,
  onNavigateBack: () -> Unit,
  onTogglePreview: () -> Unit,
  onTitleChange: (String) -> Unit
) {
  Row(
    modifier = Modifier
      .fillMaxWidth()
      .background(SurfaceDark)
      .border(1.dp, SurfaceCardBorder, RoundedCornerShape(0.dp))
      .padding(horizontal = 16.dp, vertical = 10.dp),
    horizontalArrangement = Arrangement.SpaceBetween,
    verticalAlignment = Alignment.CenterVertically
  ) {
    Row(
      verticalAlignment = Alignment.CenterVertically,
      horizontalArrangement = Arrangement.spacedBy(10.dp),
      modifier = Modifier.weight(1f)
    ) {
      IconButton(
        onClick = onNavigateBack,
        modifier = Modifier
          .size(36.dp)
          .clip(CircleShape)
          .background(SurfaceCard)
          .testTag("builder_back_button")
      ) {
        Icon(
          imageVector = Icons.AutoMirrored.Filled.ArrowBack,
          contentDescription = "Back",
          tint = TextPrimary
        )
      }

      Column(modifier = Modifier.weight(1f)) {
        OutlinedTextField(
          value = uiState.title,
          onValueChange = onTitleChange,
          singleLine = true,
          modifier = Modifier
            .fillMaxWidth()
            .height(44.dp)
            .testTag("builder_title_input"),
          textStyle = androidx.compose.ui.text.TextStyle(
            color = TextPrimary,
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold
          ),
          colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = BrandPrimary,
            unfocusedBorderColor = Color.Transparent,
            focusedContainerColor = SurfaceDark,
            unfocusedContainerColor = Color.Transparent
          )
        )

        // Live Autosave status badge
        Row(
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
          if (uiState.isAutosaving) {
            CircularProgressIndicator(
              modifier = Modifier.size(10.dp),
              color = BrandPrimary,
              strokeWidth = 1.5.dp
            )
            Text(text = "Saving to database...", color = TextMuted, fontSize = 10.sp)
          } else {
            Icon(imageVector = Icons.Default.Check, contentDescription = null, tint = BrandAccent, modifier = Modifier.size(12.dp))
            Text(text = uiState.lastSavedTimeFormatted, color = TextMuted, fontSize = 10.sp)
          }
        }
      }
    }

    Row(
      verticalAlignment = Alignment.CenterVertically,
      horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
      // ATS Badge
      Box(
        modifier = Modifier
          .clip(RoundedCornerShape(8.dp))
          .background(BrandAccent.copy(alpha = 0.15f))
          .border(1.dp, BrandAccent.copy(alpha = 0.4f), RoundedCornerShape(8.dp))
          .padding(horizontal = 8.dp, vertical = 4.dp)
      ) {
        Text(text = "ATS Score: ${uiState.atsScore}%", color = BrandAccent, fontSize = 11.sp, fontWeight = FontWeight.Bold)
      }

      // Preview Button
      Button(
        onClick = onTogglePreview,
        modifier = Modifier
          .height(38.dp)
          .testTag("builder_preview_button"),
        shape = RoundedCornerShape(10.dp),
        colors = ButtonDefaults.buttonColors(containerColor = BrandPrimary, contentColor = Color.White)
      ) {
        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
          Icon(imageVector = Icons.Default.Visibility, contentDescription = null, modifier = Modifier.size(14.dp))
          Text(text = "Preview & Export", fontSize = 12.sp, fontWeight = FontWeight.Bold)
        }
      }
    }
  }
}

// -----------------------------------------------------------------------------
// PROGRESS INDICATOR & STEP SELECTOR PILLS
// -----------------------------------------------------------------------------
@Composable
fun BuilderProgressIndicator(
  uiState: ResumeBuilderUiState,
  onStepSelected: (BuilderStep) -> Unit
) {
  Column(
    modifier = Modifier
      .fillMaxWidth()
      .background(SurfaceDark)
      .padding(vertical = 8.dp)
  ) {
    // Progress Bar Header
    Row(
      modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 20.dp, vertical = 2.dp),
      horizontalArrangement = Arrangement.SpaceBetween,
      verticalAlignment = Alignment.CenterVertically
    ) {
      Text(
        text = "STEP ${uiState.currentStep.stepIndex} OF ${uiState.totalStepsCount} • ${uiState.currentStep.title}",
        color = BrandSecondary,
        fontSize = 11.sp,
        fontWeight = FontWeight.Bold
      )

      Text(
        text = "${(uiState.progressPercentage * 100).toInt()}% COMPLETED",
        color = TextMuted,
        fontSize = 11.sp,
        fontWeight = FontWeight.Bold
      )
    }

    // Linear Progress Bar
    LinearProgressIndicator(
      progress = { uiState.progressPercentage },
      modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 20.dp, vertical = 6.dp)
        .height(4.dp)
        .clip(RoundedCornerShape(2.dp)),
      color = BrandPrimary,
      trackColor = SurfaceCard
    )

    // Horizontal Scrollable Step Pills
    val scrollState = rememberScrollState()
    Row(
      modifier = Modifier
        .fillMaxWidth()
        .horizontalScroll(scrollState)
        .padding(horizontal = 16.dp, vertical = 4.dp),
      horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
      BuilderStep.values().forEach { step ->
        val isSelected = uiState.currentStep == step
        val isPassed = step.stepIndex < uiState.currentStep.stepIndex

        Box(
          modifier = Modifier
            .clip(RoundedCornerShape(12.dp))
            .background(
              when {
                isSelected -> BrandPrimary
                isPassed -> BrandPrimary.copy(alpha = 0.2f)
                else -> SurfaceCard
              }
            )
            .border(
              1.dp,
              when {
                isSelected -> BrandPrimary
                isPassed -> BrandPrimary.copy(alpha = 0.4f)
                else -> SurfaceCardBorder
              },
              RoundedCornerShape(12.dp)
            )
            .clickable { onStepSelected(step) }
            .padding(horizontal = 10.dp, vertical = 6.dp)
            .testTag("step_pill_${step.stepIndex}")
        ) {
          Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp)
          ) {
            Text(
              text = "${step.stepIndex}.",
              color = if (isSelected) Color.White else if (isPassed) BrandPrimary else TextMuted,
              fontSize = 10.sp,
              fontWeight = FontWeight.Bold
            )
            Text(
              text = step.title,
              color = if (isSelected) Color.White else if (isPassed) TextPrimary else TextSecondary,
              fontSize = 11.sp,
              fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
            )
            if (isPassed) {
              Icon(imageVector = Icons.Default.Check, contentDescription = null, tint = BrandPrimary, modifier = Modifier.size(10.dp))
            }
          }
        }
      }
    }
  }
}

@Composable
fun StepHeaderCard(step: BuilderStep) {
  Card(
    modifier = Modifier.fillMaxWidth(),
    shape = RoundedCornerShape(16.dp),
    colors = CardDefaults.cardColors(containerColor = SurfaceCard),
    border = CardDefaults.outlinedCardBorder().copy(
      brush = Brush.horizontalGradient(listOf(BrandPrimary, BrandSecondary))
    )
  ) {
    Row(
      modifier = Modifier
        .fillMaxWidth()
        .padding(16.dp),
      verticalAlignment = Alignment.CenterVertically,
      horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
      Box(
        modifier = Modifier
          .size(40.dp)
          .clip(CircleShape)
          .background(BrandPrimary.copy(alpha = 0.2f)),
        contentAlignment = Alignment.Center
      ) {
        Text(
          text = "${step.stepIndex}",
          color = BrandPrimary,
          fontSize = 16.sp,
          fontWeight = FontWeight.Bold
        )
      }

      Column {
        Text(text = step.title, color = TextPrimary, fontSize = 16.sp, fontWeight = FontWeight.Bold)
        Text(text = step.subtitle, color = TextSecondary, fontSize = 12.sp)
      }
    }
  }
}

// -----------------------------------------------------------------------------
// STEP 1: COUNTRY
// -----------------------------------------------------------------------------
@Composable
fun CountryStepContent(uiState: ResumeBuilderUiState, viewModel: ResumeBuilderViewModel) {
  val countries = listOf(
    "United States" to "🇺🇸 (US Standard Letter)",
    "United Kingdom" to "🇬🇧 (UK / EU A4)",
    "Canada" to "🇨🇦 (North America Standard)",
    "Germany" to "🇩🇪 (EU Lebenslauf Format)",
    "France" to "🇫🇷 (French European Format)",
    "Australia" to "🇦🇺 (ANZ Standard)",
    "Singapore" to "🇸🇬 (Asia Global Tech)",
    "India" to "🇮🇳 (Tech & Global Standard)",
    "Brazil" to "🇧🇷 (LATAM Standard)"
  )

  Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
    Text(text = "Target Country & Format Standard", color = TextPrimary, fontSize = 14.sp, fontWeight = FontWeight.Bold)
    Text(text = "ATS parser rules change by geography. Selecting your target country applies appropriate paper dimensions and section layouts.", color = TextMuted, fontSize = 11.sp)

    countries.forEach { (countryName, desc) ->
      val isSelected = uiState.country == countryName
      Box(
        modifier = Modifier
          .fillMaxWidth()
          .clip(RoundedCornerShape(12.dp))
          .background(if (isSelected) BrandPrimary.copy(alpha = 0.2f) else SurfaceCard)
          .border(1.dp, if (isSelected) BrandPrimary else SurfaceCardBorder, RoundedCornerShape(12.dp))
          .clickable { viewModel.updateCountry(countryName) }
          .padding(14.dp)
          .testTag("country_option_${countryName.replace(" ", "_")}")
      ) {
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Column {
            Text(text = countryName, color = if (isSelected) Color.White else TextPrimary, fontSize = 14.sp, fontWeight = FontWeight.Bold)
            Text(text = desc, color = TextSecondary, fontSize = 11.sp)
          }
          if (isSelected) {
            Icon(imageVector = Icons.Default.CheckCircle, contentDescription = null, tint = BrandPrimary, modifier = Modifier.size(20.dp))
          }
        }
      }
    }
  }
}

// -----------------------------------------------------------------------------
// STEP 2: LANGUAGE
// -----------------------------------------------------------------------------
@Composable
fun LanguageStepContent(uiState: ResumeBuilderUiState, viewModel: ResumeBuilderViewModel) {
  val languages = listOf(
    "English (US)" to "Default standard for US & international tech roles",
    "English (UK)" to "British spelling & terminology for UK/EU markets",
    "Spanish" to "Español para mercados de España y Latinoamérica",
    "French" to "Français pour la France, la Suisse et le Canada",
    "German" to "Deutsch für Deutschland, Österreich und die Schweiz",
    "Portuguese" to "Português para o Brasil e Portugal"
  )

  Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
    Text(text = "Primary Language for Resume Text", color = TextPrimary, fontSize = 14.sp, fontWeight = FontWeight.Bold)

    languages.forEach { (langName, desc) ->
      val isSelected = uiState.resumeLanguage == langName
      Box(
        modifier = Modifier
          .fillMaxWidth()
          .clip(RoundedCornerShape(12.dp))
          .background(if (isSelected) BrandSecondary.copy(alpha = 0.2f) else SurfaceCard)
          .border(1.dp, if (isSelected) BrandSecondary else SurfaceCardBorder, RoundedCornerShape(12.dp))
          .clickable { viewModel.updateResumeLanguage(langName) }
          .padding(14.dp)
          .testTag("language_option_${langName.replace(" ", "_")}")
      ) {
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Column {
            Text(text = langName, color = if (isSelected) Color.White else TextPrimary, fontSize = 14.sp, fontWeight = FontWeight.Bold)
            Text(text = desc, color = TextSecondary, fontSize = 11.sp)
          }
          if (isSelected) {
            Icon(imageVector = Icons.Default.CheckCircle, contentDescription = null, tint = BrandSecondary, modifier = Modifier.size(20.dp))
          }
        }
      }
    }
  }
}

// -----------------------------------------------------------------------------
// STEP 3: PROFESSION
// -----------------------------------------------------------------------------
@Composable
fun ProfessionStepContent(uiState: ResumeBuilderUiState, viewModel: ResumeBuilderViewModel) {
  val expLevels = listOf(
    "Entry Level (0-2 yrs)",
    "Mid Level (2-5 yrs)",
    "Senior Level (5-8 yrs)",
    "Lead / Architect (8+ yrs)",
    "Executive / Director"
  )

  Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
    OutlinedTextField(
      value = uiState.profession,
      onValueChange = { viewModel.updateProfession(it) },
      label = { Text("Target Role / Profession", color = TextMuted) },
      placeholder = { Text("e.g. Senior Backend Engineer", color = TextMuted) },
      modifier = Modifier
        .fillMaxWidth()
        .testTag("profession_input"),
      shape = RoundedCornerShape(10.dp),
      colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = BrandPrimary, unfocusedBorderColor = SurfaceCardBorder, focusedTextColor = TextPrimary, unfocusedTextColor = TextPrimary)
    )

    Text(text = "Experience Level", color = TextPrimary, fontSize = 14.sp, fontWeight = FontWeight.Bold)

    expLevels.forEach { lvl ->
      val isSelected = uiState.experienceLevel == lvl
      Box(
        modifier = Modifier
          .fillMaxWidth()
          .clip(RoundedCornerShape(10.dp))
          .background(if (isSelected) BrandPurple.copy(alpha = 0.2f) else SurfaceCard)
          .border(1.dp, if (isSelected) BrandPurple else SurfaceCardBorder, RoundedCornerShape(10.dp))
          .clickable { viewModel.updateExperienceLevel(lvl) }
          .padding(12.dp)
      ) {
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Text(text = lvl, color = if (isSelected) Color.White else TextPrimary, fontSize = 13.sp, fontWeight = FontWeight.Medium)
          if (isSelected) {
            Icon(imageVector = Icons.Default.CheckCircle, contentDescription = null, tint = BrandPurple, modifier = Modifier.size(18.dp))
          }
        }
      }
    }
  }
}

// -----------------------------------------------------------------------------
// STEP 4: PERSONAL INFO
// -----------------------------------------------------------------------------
@Composable
fun PersonalInfoStepContent(uiState: ResumeBuilderUiState, viewModel: ResumeBuilderViewModel) {
  Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
    OutlinedTextField(
      value = uiState.fullName,
      onValueChange = { viewModel.updatePersonalInfo(fullName = it) },
      label = { Text("Full Name", color = TextMuted) },
      modifier = Modifier
        .fillMaxWidth()
        .testTag("personal_input_name"),
      shape = RoundedCornerShape(10.dp),
      colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = BrandPrimary, unfocusedBorderColor = SurfaceCardBorder, focusedTextColor = TextPrimary, unfocusedTextColor = TextPrimary)
    )

    OutlinedTextField(
      value = uiState.headline,
      onValueChange = { viewModel.updatePersonalInfo(headline = it) },
      label = { Text("Professional Headline", color = TextMuted) },
      placeholder = { Text("e.g. Senior Software & Cloud Architect", color = TextMuted) },
      modifier = Modifier
        .fillMaxWidth()
        .testTag("personal_input_headline"),
      shape = RoundedCornerShape(10.dp),
      colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = BrandPrimary, unfocusedBorderColor = SurfaceCardBorder, focusedTextColor = TextPrimary, unfocusedTextColor = TextPrimary)
    )

    Row(
      modifier = Modifier.fillMaxWidth(),
      horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
      OutlinedTextField(
        value = uiState.email,
        onValueChange = { viewModel.updatePersonalInfo(email = it) },
        label = { Text("Email Address", color = TextMuted) },
        modifier = Modifier
          .weight(1f)
          .testTag("personal_input_email"),
        shape = RoundedCornerShape(10.dp),
        colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = BrandPrimary, unfocusedBorderColor = SurfaceCardBorder, focusedTextColor = TextPrimary, unfocusedTextColor = TextPrimary)
      )

      OutlinedTextField(
        value = uiState.phone,
        onValueChange = { viewModel.updatePersonalInfo(phone = it) },
        label = { Text("Phone", color = TextMuted) },
        modifier = Modifier
          .weight(1f)
          .testTag("personal_input_phone"),
        shape = RoundedCornerShape(10.dp),
        colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = BrandPrimary, unfocusedBorderColor = SurfaceCardBorder, focusedTextColor = TextPrimary, unfocusedTextColor = TextPrimary)
      )
    }

    OutlinedTextField(
      value = uiState.location,
      onValueChange = { viewModel.updatePersonalInfo(location = it) },
      label = { Text("City, State / Country", color = TextMuted) },
      modifier = Modifier
        .fillMaxWidth()
        .testTag("personal_input_location"),
      shape = RoundedCornerShape(10.dp),
      colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = BrandPrimary, unfocusedBorderColor = SurfaceCardBorder, focusedTextColor = TextPrimary, unfocusedTextColor = TextPrimary)
    )
  }
}

// -----------------------------------------------------------------------------
// STEP 5: PROFESSIONAL SUMMARY
// -----------------------------------------------------------------------------
@Composable
fun SummaryStepContent(uiState: ResumeBuilderUiState, viewModel: ResumeBuilderViewModel) {
  Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
    Row(
      modifier = Modifier.fillMaxWidth(),
      horizontalArrangement = Arrangement.SpaceBetween,
      verticalAlignment = Alignment.CenterVertically
    ) {
      Text(text = "Executive Summary", color = TextPrimary, fontSize = 14.sp, fontWeight = FontWeight.Bold)
      Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
        OutlinedButton(
          onClick = { viewModel.aiImproveGrammarAndTone() },
          shape = RoundedCornerShape(8.dp),
          colors = ButtonDefaults.outlinedButtonColors(contentColor = BrandAccent)
        ) {
          Text(text = "✨ Grammar & Tone", fontSize = 10.sp, fontWeight = FontWeight.Bold)
        }
        Button(
          onClick = { viewModel.aiEnhanceSummary() },
          shape = RoundedCornerShape(8.dp),
          modifier = Modifier.testTag("summary_ai_button"),
          colors = ButtonDefaults.buttonColors(containerColor = BrandPurple, contentColor = Color.White)
        ) {
          Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
            Icon(imageVector = Icons.Default.AutoAwesome, contentDescription = null, modifier = Modifier.size(14.dp))
            Text(text = "AI Summary", fontSize = 11.sp, fontWeight = FontWeight.Bold)
          }
        }
      }
    }

    OutlinedTextField(
      value = uiState.professionalSummary,
      onValueChange = { viewModel.updateProfessionalSummary(it) },
      modifier = Modifier
        .fillMaxWidth()
        .height(180.dp)
        .testTag("summary_input_text"),
      placeholder = { Text("Brief 3-4 sentence elevator pitch showcasing your top achievements and core value proposition...", color = TextMuted, fontSize = 12.sp) },
      shape = RoundedCornerShape(12.dp),
      colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = BrandPrimary, unfocusedBorderColor = SurfaceCardBorder, focusedTextColor = TextPrimary, unfocusedTextColor = TextPrimary)
    )

    Text(
      text = "Tip: High-scoring summaries include target job keywords, quantifiable metrics (e.g. 'improved latency by 40%'), and total years of experience.",
      color = TextMuted,
      fontSize = 11.sp,
      lineHeight = 15.sp
    )
  }
}

// -----------------------------------------------------------------------------
// STEP 6: EDUCATION
// -----------------------------------------------------------------------------
@Composable
fun EducationStepContent(uiState: ResumeBuilderUiState, viewModel: ResumeBuilderViewModel) {
  var showAddDialog by remember { mutableStateOf(false) }

  Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
    Row(
      modifier = Modifier.fillMaxWidth(),
      horizontalArrangement = Arrangement.SpaceBetween,
      verticalAlignment = Alignment.CenterVertically
    ) {
      Text(text = "Academic Background (${uiState.educationList.size})", color = TextPrimary, fontSize = 14.sp, fontWeight = FontWeight.Bold)
      Button(
        onClick = { showAddDialog = true },
        shape = RoundedCornerShape(8.dp),
        modifier = Modifier.testTag("education_add_button"),
        colors = ButtonDefaults.buttonColors(containerColor = BrandPrimary, contentColor = Color.White)
      ) {
        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
          Icon(imageVector = Icons.Default.Add, contentDescription = null, modifier = Modifier.size(14.dp))
          Text(text = "Add Education", fontSize = 11.sp, fontWeight = FontWeight.Bold)
        }
      }
    }

    if (uiState.educationList.isEmpty()) {
      EmptySectionCard(title = "No Education Added Yet", subtitle = "Add your university degrees or academic programs.")
    } else {
      uiState.educationList.forEach { edu ->
        Card(
          modifier = Modifier.fillMaxWidth(),
          shape = RoundedCornerShape(12.dp),
          colors = CardDefaults.cardColors(containerColor = SurfaceCard),
          border = CardDefaults.outlinedCardBorder().copy(brush = Brush.verticalGradient(listOf(BrandPrimary.copy(alpha = 0.3f), SurfaceCardBorder)))
        ) {
          Row(
            modifier = Modifier
              .fillMaxWidth()
              .padding(14.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Column(modifier = Modifier.weight(1f)) {
              Text(text = edu.degree, color = TextPrimary, fontSize = 14.sp, fontWeight = FontWeight.Bold)
              Text(text = "${edu.school} • ${edu.startDate} - ${edu.endDate}", color = TextSecondary, fontSize = 12.sp)
              if (edu.fieldOfStudy.isNotBlank()) {
                Text(text = "Major: ${edu.fieldOfStudy}", color = TextMuted, fontSize = 11.sp)
              }
            }

            IconButton(onClick = { viewModel.removeEducation(edu.id) }) {
              Icon(imageVector = Icons.Default.Delete, contentDescription = "Remove", tint = Color(0xFFEF4444), modifier = Modifier.size(18.dp))
            }
          }
        }
      }
    }
  }

  if (showAddDialog) {
    AddEducationDialog(
      onAdd = { viewModel.addEducation(it) },
      onDismiss = { showAddDialog = false }
    )
  }
}

// -----------------------------------------------------------------------------
// STEP 7: WORK EXPERIENCE
// -----------------------------------------------------------------------------
@Composable
fun WorkExperienceStepContent(uiState: ResumeBuilderUiState, viewModel: ResumeBuilderViewModel) {
  var showAddDialog by remember { mutableStateOf(false) }

  Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
    Row(
      modifier = Modifier.fillMaxWidth(),
      horizontalArrangement = Arrangement.SpaceBetween,
      verticalAlignment = Alignment.CenterVertically
    ) {
      Text(text = "Employment History (${uiState.workExperienceList.size})", color = TextPrimary, fontSize = 14.sp, fontWeight = FontWeight.Bold)
      Button(
        onClick = { showAddDialog = true },
        shape = RoundedCornerShape(8.dp),
        modifier = Modifier.testTag("work_add_button"),
        colors = ButtonDefaults.buttonColors(containerColor = BrandPrimary, contentColor = Color.White)
      ) {
        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
          Icon(imageVector = Icons.Default.Add, contentDescription = null, modifier = Modifier.size(14.dp))
          Text(text = "Add Position", fontSize = 11.sp, fontWeight = FontWeight.Bold)
        }
      }
    }

    if (uiState.workExperienceList.isEmpty()) {
      EmptySectionCard(title = "No Work Experience Added", subtitle = "Add your past and current professional roles.")
    } else {
      uiState.workExperienceList.forEach { work ->
        Card(
          modifier = Modifier.fillMaxWidth(),
          shape = RoundedCornerShape(12.dp),
          colors = CardDefaults.cardColors(containerColor = SurfaceCard),
          border = CardDefaults.outlinedCardBorder().copy(brush = Brush.verticalGradient(listOf(BrandSecondary.copy(alpha = 0.3f), SurfaceCardBorder)))
        ) {
          Column(
            modifier = Modifier.padding(14.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp)
          ) {
            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.SpaceBetween,
              verticalAlignment = Alignment.CenterVertically
            ) {
              Text(text = work.jobTitle, color = TextPrimary, fontSize = 14.sp, fontWeight = FontWeight.Bold)
              IconButton(onClick = { viewModel.removeWorkExperience(work.id) }) {
                Icon(imageVector = Icons.Default.Delete, contentDescription = "Remove", tint = Color(0xFFEF4444), modifier = Modifier.size(18.dp))
              }
            }
            Text(text = "${work.company} • ${work.startDate} - ${if (work.isCurrent) "Present" else work.endDate}", color = BrandSecondary, fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
            if (work.description.isNotBlank()) {
              Text(text = work.description, color = TextSecondary, fontSize = 11.sp, lineHeight = 15.sp)
            }

            Spacer(modifier = Modifier.height(2.dp))
            OutlinedButton(
              onClick = { viewModel.aiRewriteWorkExperience(work) },
              shape = RoundedCornerShape(8.dp),
              modifier = Modifier.fillMaxWidth(),
              colors = ButtonDefaults.outlinedButtonColors(contentColor = BrandPurple)
            ) {
              Row(
                horizontalArrangement = Arrangement.spacedBy(6.dp),
                verticalAlignment = Alignment.CenterVertically
              ) {
                Icon(imageVector = Icons.Default.AutoAwesome, contentDescription = null, modifier = Modifier.size(14.dp))
                Text(text = "✨ AI Rewrite Experience & Metrics", fontSize = 11.sp, fontWeight = FontWeight.Bold)
              }
            }
          }
        }
      }
    }
  }

  if (showAddDialog) {
    AddWorkExperienceDialog(
      onAdd = { viewModel.addWorkExperience(it) },
      onDismiss = { showAddDialog = false }
    )
  }
}

// -----------------------------------------------------------------------------
// STEP 8: SKILLS
// -----------------------------------------------------------------------------
@Composable
fun SkillsStepContent(uiState: ResumeBuilderUiState, viewModel: ResumeBuilderViewModel) {
  var newSkillName by remember { mutableStateOf("") }
  var selectedCategory by remember { mutableStateOf("Technical") }
  var selectedLevel by remember { mutableStateOf("Advanced") }

  Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
    Row(
      modifier = Modifier.fillMaxWidth(),
      horizontalArrangement = Arrangement.SpaceBetween,
      verticalAlignment = Alignment.CenterVertically
    ) {
      Text(text = "Skills & Competencies", color = TextPrimary, fontSize = 14.sp, fontWeight = FontWeight.Bold)
      Button(
        onClick = { viewModel.aiSuggestMissingSkills() },
        shape = RoundedCornerShape(8.dp),
        colors = ButtonDefaults.buttonColors(containerColor = BrandPurple, contentColor = Color.White)
      ) {
        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
          Icon(imageVector = Icons.Default.AutoAwesome, contentDescription = null, modifier = Modifier.size(14.dp))
          Text(text = "✨ AI Suggest Skills", fontSize = 11.sp, fontWeight = FontWeight.Bold)
        }
      }
    }

    Card(
      modifier = Modifier.fillMaxWidth(),
      shape = RoundedCornerShape(12.dp),
      colors = CardDefaults.cardColors(containerColor = SurfaceCard)
    ) {
      Column(
        modifier = Modifier.padding(14.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
      ) {
        OutlinedTextField(
          value = newSkillName,
          onValueChange = { newSkillName = it },
          label = { Text("Add New Skill", color = TextMuted) },
          placeholder = { Text("e.g. Jetpack Compose, Kotlin, Microservices", color = TextMuted) },
          modifier = Modifier
            .fillMaxWidth()
            .testTag("skill_input_name"),
          shape = RoundedCornerShape(10.dp),
          colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = BrandPrimary, unfocusedBorderColor = SurfaceCardBorder, focusedTextColor = TextPrimary, unfocusedTextColor = TextPrimary)
        )

        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
          listOf("Technical", "Soft", "Tools").forEach { cat ->
            val isSel = selectedCategory == cat
            Box(
              modifier = Modifier
                .clip(RoundedCornerShape(8.dp))
                .background(if (isSel) BrandPrimary else SurfaceDark)
                .clickable { selectedCategory = cat }
                .padding(horizontal = 10.dp, vertical = 6.dp)
            ) {
              Text(text = cat, color = if (isSel) Color.White else TextMuted, fontSize = 11.sp, fontWeight = FontWeight.Bold)
            }
          }
        }

        Button(
          onClick = {
            if (newSkillName.isNotBlank()) {
              viewModel.addSkill(SkillItem(name = newSkillName.trim(), category = selectedCategory, level = selectedLevel))
              newSkillName = ""
            }
          },
          modifier = Modifier
            .fillMaxWidth()
            .testTag("skill_add_button"),
          shape = RoundedCornerShape(8.dp),
          colors = ButtonDefaults.buttonColors(containerColor = BrandPrimary, contentColor = Color.White)
        ) {
          Text(text = "Add Skill", fontSize = 12.sp, fontWeight = FontWeight.Bold)
        }
      }
    }

    // Display skills pills
    Row(
      modifier = Modifier.fillMaxWidth(),
      horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
      uiState.skillsList.forEach { skill ->
        Box(
          modifier = Modifier
            .clip(RoundedCornerShape(10.dp))
            .background(SurfaceCard)
            .border(1.dp, BrandPrimary.copy(alpha = 0.4f), RoundedCornerShape(10.dp))
            .padding(horizontal = 10.dp, vertical = 6.dp)
        ) {
          Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(6.dp)
          ) {
            Text(text = skill.name, color = TextPrimary, fontSize = 12.sp, fontWeight = FontWeight.Medium)
            Text(text = "(${skill.level})", color = BrandAccent, fontSize = 10.sp)
            Icon(
              imageVector = Icons.Default.Close,
              contentDescription = "Remove",
              tint = TextMuted,
              modifier = Modifier
                .size(14.dp)
                .clickable { viewModel.removeSkill(skill.id) }
            )
          }
        }
      }
    }
  }
}

// -----------------------------------------------------------------------------
// STEP 9: LANGUAGES
// -----------------------------------------------------------------------------
@Composable
fun LanguagesStepContent(uiState: ResumeBuilderUiState, viewModel: ResumeBuilderViewModel) {
  var langName by remember { mutableStateOf("") }
  var fluency by remember { mutableStateOf("Fluent") }

  Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
    Text(text = "Spoken Languages & Proficiency", color = TextPrimary, fontSize = 14.sp, fontWeight = FontWeight.Bold)

    Row(
      modifier = Modifier.fillMaxWidth(),
      horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
      OutlinedTextField(
        value = langName,
        onValueChange = { langName = it },
        label = { Text("Language", color = TextMuted) },
        placeholder = { Text("e.g. French", color = TextMuted) },
        modifier = Modifier
          .weight(1f)
          .testTag("language_input_name"),
        shape = RoundedCornerShape(10.dp),
        colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = BrandPrimary, unfocusedBorderColor = SurfaceCardBorder, focusedTextColor = TextPrimary, unfocusedTextColor = TextPrimary)
      )

      Button(
        onClick = {
          if (langName.isNotBlank()) {
            viewModel.addLanguage(LanguageItem(language = langName.trim(), proficiency = fluency))
            langName = ""
          }
        },
        modifier = Modifier
          .height(56.dp)
          .testTag("language_add_button"),
        shape = RoundedCornerShape(10.dp),
        colors = ButtonDefaults.buttonColors(containerColor = BrandPrimary, contentColor = Color.White)
      ) {
        Text(text = "Add", fontSize = 12.sp, fontWeight = FontWeight.Bold)
      }
    }

    uiState.languagesList.forEach { lang ->
      Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(10.dp),
        colors = CardDefaults.cardColors(containerColor = SurfaceCard)
      ) {
        Row(
          modifier = Modifier
            .fillMaxWidth()
            .padding(12.dp),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Icon(imageVector = Icons.Default.Language, contentDescription = null, tint = BrandSecondary, modifier = Modifier.size(16.dp))
            Text(text = lang.language, color = TextPrimary, fontSize = 13.sp, fontWeight = FontWeight.Bold)
            Text(text = "• ${lang.proficiency}", color = TextSecondary, fontSize = 12.sp)
          }

          IconButton(onClick = { viewModel.removeLanguage(lang.id) }) {
            Icon(imageVector = Icons.Default.Delete, contentDescription = "Remove", tint = Color(0xFFEF4444), modifier = Modifier.size(16.dp))
          }
        }
      }
    }
  }
}

// -----------------------------------------------------------------------------
// STEP 10: PROJECTS
// -----------------------------------------------------------------------------
@Composable
fun ProjectsStepContent(uiState: ResumeBuilderUiState, viewModel: ResumeBuilderViewModel) {
  var showAddDialog by remember { mutableStateOf(false) }

  Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
    Row(
      modifier = Modifier.fillMaxWidth(),
      horizontalArrangement = Arrangement.SpaceBetween,
      verticalAlignment = Alignment.CenterVertically
    ) {
      Text(text = "Key Projects & Portfolio (${uiState.projectsList.size})", color = TextPrimary, fontSize = 14.sp, fontWeight = FontWeight.Bold)
      Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
        OutlinedButton(
          onClick = { viewModel.aiGenerateProjects() },
          shape = RoundedCornerShape(8.dp),
          colors = ButtonDefaults.outlinedButtonColors(contentColor = BrandPurple)
        ) {
          Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
            Icon(imageVector = Icons.Default.AutoAwesome, contentDescription = null, modifier = Modifier.size(14.dp))
            Text(text = "✨ AI Projects", fontSize = 11.sp, fontWeight = FontWeight.Bold)
          }
        }
        Button(
          onClick = { showAddDialog = true },
          shape = RoundedCornerShape(8.dp),
          modifier = Modifier.testTag("projects_add_button"),
          colors = ButtonDefaults.buttonColors(containerColor = BrandPrimary, contentColor = Color.White)
        ) {
          Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
            Icon(imageVector = Icons.Default.Add, contentDescription = null, modifier = Modifier.size(14.dp))
            Text(text = "Add", fontSize = 11.sp, fontWeight = FontWeight.Bold)
          }
        }
      }
    }

    if (uiState.projectsList.isEmpty()) {
      EmptySectionCard(title = "No Projects Added", subtitle = "Add open-source, client or personal tech projects.")
    } else {
      uiState.projectsList.forEach { proj ->
        Card(
          modifier = Modifier.fillMaxWidth(),
          shape = RoundedCornerShape(12.dp),
          colors = CardDefaults.cardColors(containerColor = SurfaceCard)
        ) {
          Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.SpaceBetween,
              verticalAlignment = Alignment.CenterVertically
            ) {
              Text(text = proj.title, color = TextPrimary, fontSize = 14.sp, fontWeight = FontWeight.Bold)
              IconButton(onClick = { viewModel.removeProject(proj.id) }) {
                Icon(imageVector = Icons.Default.Delete, contentDescription = "Remove", tint = Color(0xFFEF4444), modifier = Modifier.size(18.dp))
              }
            }
            Text(text = proj.description, color = TextSecondary, fontSize = 11.sp)
            if (proj.techStack.isNotBlank()) {
              Text(text = "Tech Stack: ${proj.techStack}", color = BrandAccent, fontSize = 10.sp)
            }
          }
        }
      }
    }
  }

  if (showAddDialog) {
    AddProjectDialog(
      onAdd = { viewModel.addProject(it) },
      onDismiss = { showAddDialog = false }
    )
  }
}

// -----------------------------------------------------------------------------
// STEP 11: CERTIFICATES
// -----------------------------------------------------------------------------
@Composable
fun CertificatesStepContent(uiState: ResumeBuilderUiState, viewModel: ResumeBuilderViewModel) {
  var certName by remember { mutableStateOf("") }
  var certIssuer by remember { mutableStateOf("") }

  Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
    Text(text = "Certifications & Professional Badges", color = TextPrimary, fontSize = 14.sp, fontWeight = FontWeight.Bold)

    OutlinedTextField(
      value = certName,
      onValueChange = { certName = it },
      label = { Text("Certification Name", color = TextMuted) },
      placeholder = { Text("e.g. AWS Certified Solutions Architect", color = TextMuted) },
      modifier = Modifier
        .fillMaxWidth()
        .testTag("cert_input_name"),
      shape = RoundedCornerShape(10.dp),
      colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = BrandPrimary, unfocusedBorderColor = SurfaceCardBorder, focusedTextColor = TextPrimary, unfocusedTextColor = TextPrimary)
    )

    OutlinedTextField(
      value = certIssuer,
      onValueChange = { certIssuer = it },
      label = { Text("Issuing Organization", color = TextMuted) },
      placeholder = { Text("e.g. Amazon Web Services / Google Cloud", color = TextMuted) },
      modifier = Modifier
        .fillMaxWidth()
        .testTag("cert_input_issuer"),
      shape = RoundedCornerShape(10.dp),
      colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = BrandPrimary, unfocusedBorderColor = SurfaceCardBorder, focusedTextColor = TextPrimary, unfocusedTextColor = TextPrimary)
    )

    Button(
      onClick = {
        if (certName.isNotBlank()) {
          viewModel.addCertificate(CertificateItem(name = certName.trim(), issuer = certIssuer.trim(), issueDate = "2024"))
          certName = ""
          certIssuer = ""
        }
      },
      modifier = Modifier
        .fillMaxWidth()
        .testTag("cert_add_button"),
      shape = RoundedCornerShape(10.dp),
      colors = ButtonDefaults.buttonColors(containerColor = BrandPrimary, contentColor = Color.White)
    ) {
      Text(text = "Add Certificate", fontSize = 12.sp, fontWeight = FontWeight.Bold)
    }

    uiState.certificatesList.forEach { cert ->
      Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(10.dp),
        colors = CardDefaults.cardColors(containerColor = SurfaceCard)
      ) {
        Row(
          modifier = Modifier
            .fillMaxWidth()
            .padding(12.dp),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Column {
            Text(text = cert.name, color = TextPrimary, fontSize = 13.sp, fontWeight = FontWeight.Bold)
            Text(text = cert.issuer, color = TextSecondary, fontSize = 11.sp)
          }

          IconButton(onClick = { viewModel.removeCertificate(cert.id) }) {
            Icon(imageVector = Icons.Default.Delete, contentDescription = "Remove", tint = Color(0xFFEF4444), modifier = Modifier.size(16.dp))
          }
        }
      }
    }
  }
}

// -----------------------------------------------------------------------------
// STEP 12: REFERENCES
// -----------------------------------------------------------------------------
@Composable
fun ReferencesStepContent(uiState: ResumeBuilderUiState, viewModel: ResumeBuilderViewModel) {
  var refName by remember { mutableStateOf("") }
  var refTitle by remember { mutableStateOf("") }
  var refCompany by remember { mutableStateOf("") }

  Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
    Text(text = "Professional References", color = TextPrimary, fontSize = 14.sp, fontWeight = FontWeight.Bold)

    OutlinedTextField(
      value = refName,
      onValueChange = { refName = it },
      label = { Text("Reference Full Name", color = TextMuted) },
      modifier = Modifier
        .fillMaxWidth()
        .testTag("ref_input_name"),
      shape = RoundedCornerShape(10.dp),
      colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = BrandPrimary, unfocusedBorderColor = SurfaceCardBorder, focusedTextColor = TextPrimary, unfocusedTextColor = TextPrimary)
    )

    Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
      OutlinedTextField(
        value = refTitle,
        onValueChange = { refTitle = it },
        label = { Text("Title", color = TextMuted) },
        modifier = Modifier
          .weight(1f)
          .testTag("ref_input_title"),
        shape = RoundedCornerShape(10.dp),
        colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = BrandPrimary, unfocusedBorderColor = SurfaceCardBorder, focusedTextColor = TextPrimary, unfocusedTextColor = TextPrimary)
      )

      OutlinedTextField(
        value = refCompany,
        onValueChange = { refCompany = it },
        label = { Text("Company", color = TextMuted) },
        modifier = Modifier
          .weight(1f)
          .testTag("ref_input_company"),
        shape = RoundedCornerShape(10.dp),
        colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = BrandPrimary, unfocusedBorderColor = SurfaceCardBorder, focusedTextColor = TextPrimary, unfocusedTextColor = TextPrimary)
      )
    }

    Button(
      onClick = {
        if (refName.isNotBlank()) {
          viewModel.addReference(ReferenceItem(name = refName.trim(), title = refTitle.trim(), company = refCompany.trim()))
          refName = ""
          refTitle = ""
          refCompany = ""
        }
      },
      modifier = Modifier
        .fillMaxWidth()
        .testTag("ref_add_button"),
      shape = RoundedCornerShape(10.dp),
      colors = ButtonDefaults.buttonColors(containerColor = BrandPrimary, contentColor = Color.White)
    ) {
      Text(text = "Add Reference", fontSize = 12.sp, fontWeight = FontWeight.Bold)
    }

    uiState.referencesList.forEach { ref ->
      Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(10.dp),
        colors = CardDefaults.cardColors(containerColor = SurfaceCard)
      ) {
        Row(
          modifier = Modifier
            .fillMaxWidth()
            .padding(12.dp),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Column {
            Text(text = ref.name, color = TextPrimary, fontSize = 13.sp, fontWeight = FontWeight.Bold)
            Text(text = "${ref.title} • ${ref.company}", color = TextSecondary, fontSize = 11.sp)
          }

          IconButton(onClick = { viewModel.removeReference(ref.id) }) {
            Icon(imageVector = Icons.Default.Delete, contentDescription = "Remove", tint = Color(0xFFEF4444), modifier = Modifier.size(16.dp))
          }
        }
      }
    }
  }
}

// -----------------------------------------------------------------------------
// STEP 13: SOCIAL LINKS
// -----------------------------------------------------------------------------
@Composable
fun SocialLinksStepContent(uiState: ResumeBuilderUiState, viewModel: ResumeBuilderViewModel) {
  var selectedPlatform by remember { mutableStateOf("LinkedIn") }
  var urlText by remember { mutableStateOf("") }

  val platforms = listOf("LinkedIn", "GitHub", "Portfolio", "Twitter/X", "LeetCode", "Dribbble")

  Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
    Text(text = "Online Profiles & Social Links", color = TextPrimary, fontSize = 14.sp, fontWeight = FontWeight.Bold)

    Row(
      modifier = Modifier.horizontalScroll(rememberScrollState()),
      horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
      platforms.forEach { plt ->
        val isSel = selectedPlatform == plt
        Box(
          modifier = Modifier
            .clip(RoundedCornerShape(8.dp))
            .background(if (isSel) BrandPrimary else SurfaceCard)
            .clickable { selectedPlatform = plt }
            .padding(horizontal = 10.dp, vertical = 6.dp)
        ) {
          Text(text = plt, color = if (isSel) Color.White else TextMuted, fontSize = 11.sp, fontWeight = FontWeight.Bold)
        }
      }
    }

    OutlinedTextField(
      value = urlText,
      onValueChange = { urlText = it },
      label = { Text("$selectedPlatform URL", color = TextMuted) },
      placeholder = { Text("https://...", color = TextMuted) },
      modifier = Modifier
        .fillMaxWidth()
        .testTag("social_input_url"),
      shape = RoundedCornerShape(10.dp),
      colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = BrandPrimary, unfocusedBorderColor = SurfaceCardBorder, focusedTextColor = TextPrimary, unfocusedTextColor = TextPrimary)
    )

    Button(
      onClick = {
        if (urlText.isNotBlank()) {
          viewModel.addSocialLink(SocialLinkItem(platform = selectedPlatform, url = urlText.trim()))
          urlText = ""
        }
      },
      modifier = Modifier
        .fillMaxWidth()
        .testTag("social_add_button"),
      shape = RoundedCornerShape(10.dp),
      colors = ButtonDefaults.buttonColors(containerColor = BrandPrimary, contentColor = Color.White)
    ) {
      Text(text = "Add Social Link", fontSize = 12.sp, fontWeight = FontWeight.Bold)
    }

    uiState.socialLinksList.forEach { social ->
      Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(10.dp),
        colors = CardDefaults.cardColors(containerColor = SurfaceCard)
      ) {
        Row(
          modifier = Modifier
            .fillMaxWidth()
            .padding(12.dp),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Column {
            Text(text = social.platform, color = TextPrimary, fontSize = 13.sp, fontWeight = FontWeight.Bold)
            Text(text = social.url, color = BrandSecondary, fontSize = 11.sp)
          }

          IconButton(onClick = { viewModel.removeSocialLink(social.id) }) {
            Icon(imageVector = Icons.Default.Delete, contentDescription = "Remove", tint = Color(0xFFEF4444), modifier = Modifier.size(16.dp))
          }
        }
      }
    }
  }
}

// -----------------------------------------------------------------------------
// HELPER COMPONENT: EMPTY CARD
// -----------------------------------------------------------------------------
@Composable
fun EmptySectionCard(title: String, subtitle: String) {
  Box(
    modifier = Modifier
      .fillMaxWidth()
      .clip(RoundedCornerShape(12.dp))
      .background(SurfaceCard)
      .border(1.dp, SurfaceCardBorder, RoundedCornerShape(12.dp))
      .padding(24.dp),
    contentAlignment = Alignment.Center
  ) {
    Column(
      horizontalAlignment = Alignment.CenterHorizontally,
      verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
      Text(text = title, color = TextPrimary, fontSize = 14.sp, fontWeight = FontWeight.Bold)
      Text(text = subtitle, color = TextMuted, fontSize = 11.sp, textAlign = TextAlign.Center)
    }
  }
}

// -----------------------------------------------------------------------------
// BOTTOM NAV BUTTONS
// -----------------------------------------------------------------------------
@Composable
fun BuilderBottomNavButtons(
  uiState: ResumeBuilderUiState,
  onPrev: () -> Unit,
  onNext: () -> Unit
) {
  Row(
    modifier = Modifier
      .fillMaxWidth()
      .padding(top = 10.dp, bottom = 10.dp),
    horizontalArrangement = Arrangement.SpaceBetween,
    verticalAlignment = Alignment.CenterVertically
  ) {
    if (uiState.currentStep.stepIndex > 1) {
      OutlinedButton(
        onClick = onPrev,
        modifier = Modifier
          .height(48.dp)
          .testTag("builder_prev_step_button"),
        shape = RoundedCornerShape(12.dp),
        colors = ButtonDefaults.outlinedButtonColors(contentColor = TextPrimary)
      ) {
        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
          Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = null, modifier = Modifier.size(16.dp))
          Text(text = "Previous Step", fontSize = 13.sp, fontWeight = FontWeight.Bold)
        }
      }
    } else {
      Spacer(modifier = Modifier.width(1.dp))
    }

    Button(
      onClick = onNext,
      modifier = Modifier
        .height(48.dp)
        .testTag("builder_next_step_button"),
      shape = RoundedCornerShape(12.dp),
      colors = ButtonDefaults.buttonColors(
        containerColor = if (uiState.currentStep == BuilderStep.SOCIAL_LINKS) BrandAccent else BrandPrimary,
        contentColor = Color.White
      )
    ) {
      Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
        Text(
          text = if (uiState.currentStep == BuilderStep.SOCIAL_LINKS) "Finish & Preview Resume" else "Next Step",
          fontSize = 13.sp,
          fontWeight = FontWeight.Bold
        )
        Icon(imageVector = Icons.AutoMirrored.Filled.ArrowForward, contentDescription = null, modifier = Modifier.size(16.dp))
      }
    }
  }
}

// -----------------------------------------------------------------------------
// DIALOGS FOR ADDING ITEMS
// -----------------------------------------------------------------------------
@Composable
fun AddEducationDialog(onAdd: (EducationItem) -> Unit, onDismiss: () -> Unit) {
  var school by remember { mutableStateOf("") }
  var degree by remember { mutableStateOf("") }
  var field by remember { mutableStateOf("") }
  var start by remember { mutableStateOf("") }
  var end by remember { mutableStateOf("") }

  Dialog(onDismissRequest = onDismiss) {
    Surface(
      shape = RoundedCornerShape(20.dp),
      color = SurfaceCard,
      modifier = Modifier.padding(16.dp)
    ) {
      Column(
        modifier = Modifier.padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
      ) {
        Text(text = "Add Education", color = TextPrimary, fontSize = 16.sp, fontWeight = FontWeight.Bold)

        OutlinedTextField(value = school, onValueChange = { school = it }, label = { Text("University / School", color = TextMuted) }, modifier = Modifier.fillMaxWidth())
        OutlinedTextField(value = degree, onValueChange = { degree = it }, label = { Text("Degree (e.g. B.S. CS)", color = TextMuted) }, modifier = Modifier.fillMaxWidth())
        OutlinedTextField(value = field, onValueChange = { field = it }, label = { Text("Major / Field of Study", color = TextMuted) }, modifier = Modifier.fillMaxWidth())

        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
          OutlinedTextField(value = start, onValueChange = { start = it }, label = { Text("Start Year", color = TextMuted) }, modifier = Modifier.weight(1f))
          OutlinedTextField(value = end, onValueChange = { end = it }, label = { Text("End Year", color = TextMuted) }, modifier = Modifier.weight(1f))
        }

        Button(
          onClick = {
            if (school.isNotBlank() && degree.isNotBlank()) {
              onAdd(EducationItem(school = school, degree = degree, fieldOfStudy = field, startDate = start, endDate = end))
              onDismiss()
            }
          },
          modifier = Modifier.fillMaxWidth(),
          shape = RoundedCornerShape(10.dp),
          colors = ButtonDefaults.buttonColors(containerColor = BrandPrimary)
        ) {
          Text("Save Education", color = Color.White, fontWeight = FontWeight.Bold)
        }
      }
    }
  }
}

@Composable
fun AddWorkExperienceDialog(onAdd: (WorkExperienceItem) -> Unit, onDismiss: () -> Unit) {
  var company by remember { mutableStateOf("") }
  var title by remember { mutableStateOf("") }
  var start by remember { mutableStateOf("") }
  var end by remember { mutableStateOf("") }
  var isCurrent by remember { mutableStateOf(false) }
  var desc by remember { mutableStateOf("") }

  Dialog(onDismissRequest = onDismiss) {
    Surface(
      shape = RoundedCornerShape(20.dp),
      color = SurfaceCard,
      modifier = Modifier.padding(16.dp)
    ) {
      Column(
        modifier = Modifier.padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
      ) {
        Text(text = "Add Work Experience", color = TextPrimary, fontSize = 16.sp, fontWeight = FontWeight.Bold)

        OutlinedTextField(value = title, onValueChange = { title = it }, label = { Text("Job Title", color = TextMuted) }, modifier = Modifier.fillMaxWidth())
        OutlinedTextField(value = company, onValueChange = { company = it }, label = { Text("Company Name", color = TextMuted) }, modifier = Modifier.fillMaxWidth())

        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
          OutlinedTextField(value = start, onValueChange = { start = it }, label = { Text("Start Date", color = TextMuted) }, modifier = Modifier.weight(1f))
          OutlinedTextField(value = end, onValueChange = { end = it }, label = { Text("End Date", color = TextMuted) }, enabled = !isCurrent, modifier = Modifier.weight(1f))
        }

        Row(verticalAlignment = Alignment.CenterVertically) {
          Checkbox(checked = isCurrent, onCheckedChange = { isCurrent = it }, colors = CheckboxDefaults.colors(checkedColor = BrandPrimary))
          Text(text = "I currently work here", color = TextPrimary, fontSize = 12.sp)
        }

        OutlinedTextField(value = desc, onValueChange = { desc = it }, label = { Text("Achievements & Bullet points", color = TextMuted) }, modifier = Modifier.fillMaxWidth().height(100.dp))

        Button(
          onClick = {
            if (company.isNotBlank() && title.isNotBlank()) {
              onAdd(WorkExperienceItem(company = company, jobTitle = title, startDate = start, endDate = end, isCurrent = isCurrent, description = desc))
              onDismiss()
            }
          },
          modifier = Modifier.fillMaxWidth(),
          shape = RoundedCornerShape(10.dp),
          colors = ButtonDefaults.buttonColors(containerColor = BrandPrimary)
        ) {
          Text("Save Position", color = Color.White, fontWeight = FontWeight.Bold)
        }
      }
    }
  }
}

@Composable
fun AddProjectDialog(onAdd: (ProjectItem) -> Unit, onDismiss: () -> Unit) {
  var title by remember { mutableStateOf("") }
  var desc by remember { mutableStateOf("") }
  var tech by remember { mutableStateOf("") }
  var url by remember { mutableStateOf("") }

  Dialog(onDismissRequest = onDismiss) {
    Surface(
      shape = RoundedCornerShape(20.dp),
      color = SurfaceCard,
      modifier = Modifier.padding(16.dp)
    ) {
      Column(
        modifier = Modifier.padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
      ) {
        Text(text = "Add Project", color = TextPrimary, fontSize = 16.sp, fontWeight = FontWeight.Bold)

        OutlinedTextField(value = title, onValueChange = { title = it }, label = { Text("Project Title", color = TextMuted) }, modifier = Modifier.fillMaxWidth())
        OutlinedTextField(value = desc, onValueChange = { desc = it }, label = { Text("Description", color = TextMuted) }, modifier = Modifier.fillMaxWidth())
        OutlinedTextField(value = tech, onValueChange = { tech = it }, label = { Text("Technologies Used", color = TextMuted) }, modifier = Modifier.fillMaxWidth())
        OutlinedTextField(value = url, onValueChange = { url = it }, label = { Text("URL / Repository Link", color = TextMuted) }, modifier = Modifier.fillMaxWidth())

        Button(
          onClick = {
            if (title.isNotBlank()) {
              onAdd(ProjectItem(title = title, description = desc, techStack = tech, projectUrl = url))
              onDismiss()
            }
          },
          modifier = Modifier.fillMaxWidth(),
          shape = RoundedCornerShape(10.dp),
          colors = ButtonDefaults.buttonColors(containerColor = BrandPrimary)
        ) {
          Text("Save Project", color = Color.White, fontWeight = FontWeight.Bold)
        }
      }
    }
  }
}


