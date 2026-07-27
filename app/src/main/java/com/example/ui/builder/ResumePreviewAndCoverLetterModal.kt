package com.example.ui.builder

import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.FontDownload
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material.icons.filled.Print
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.FormatSize
import androidx.compose.material.icons.filled.ZoomIn
import androidx.compose.material.icons.filled.ZoomOut
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.data.auth.SubscriptionPlan
import com.example.data.auth.UserSession
import com.example.data.builder.ResumeBuilderUiState
import com.example.data.builder.ResumeBuilderViewModel
import com.example.ui.theme.BackgroundDark
import com.example.ui.theme.BrandAccent
import com.example.ui.theme.BrandAmber
import com.example.ui.theme.BrandPrimary
import com.example.ui.theme.BrandPurple
import com.example.ui.theme.SurfaceCard
import com.example.ui.theme.SurfaceCardBorder
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import com.example.utils.ResumeExporter

@Composable
fun ResumePreviewAndCoverLetterModal(
  uiState: ResumeBuilderUiState,
  viewModel: ResumeBuilderViewModel,
  userSession: UserSession? = null,
  onOpenBilling: () -> Unit = {},
  onDismiss: () -> Unit
) {
  val context = LocalContext.current
  var selectedTab by remember { mutableStateOf(0) } // 0 = Live Preview, 1 = Cover Letter

  Dialog(
    onDismissRequest = onDismiss,
    properties = DialogProperties(usePlatformDefaultWidth = false)
  ) {
    Surface(
      modifier = Modifier
        .fillMaxSize()
        .padding(8.dp),
      shape = RoundedCornerShape(20.dp),
      color = BackgroundDark
    ) {
      Column(
        modifier = Modifier.fillMaxSize()
      ) {
        // TOP MODAL HEADER
        Row(
          modifier = Modifier
            .fillMaxWidth()
            .background(SurfaceCard)
            .padding(horizontal = 16.dp, vertical = 12.dp),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
          ) {
            Icon(
              imageVector = if (selectedTab == 0) Icons.Default.Description else Icons.Default.Email,
              contentDescription = null,
              tint = BrandPrimary,
              modifier = Modifier.size(22.dp)
            )
            Column {
              Text(
                text = if (selectedTab == 0) "Real-Time Resume Live Preview" else "AI Cover Letter Generator",
                color = TextPrimary,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
              )
              Text(
                text = "${uiState.fullName} • ${uiState.country} Standard",
                color = TextMuted,
                fontSize = 11.sp
              )
            }
          }

          IconButton(onClick = onDismiss, modifier = Modifier.testTag("close_preview_modal")) {
            Icon(imageVector = Icons.Default.Close, contentDescription = "Close", tint = TextMuted)
          }
        }

        // TAB SELECTOR BAR
        TabRow(
          selectedTabIndex = selectedTab,
          containerColor = SurfaceCard,
          contentColor = BrandPrimary,
          indicator = { tabPositions ->
            if (selectedTab < tabPositions.size) {
              TabRowDefaults.SecondaryIndicator(
                Modifier.tabIndicatorOffset(tabPositions[selectedTab]),
                color = BrandPrimary
              )
            }
          }
        ) {
          Tab(
            selected = selectedTab == 0,
            onClick = { selectedTab = 0 },
            text = {
              Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                Icon(imageVector = Icons.Default.Description, contentDescription = null, modifier = Modifier.size(16.dp))
                Text("Resume Studio Preview", fontWeight = FontWeight.Bold, fontSize = 13.sp)
              }
            }
          )

          Tab(
            selected = selectedTab == 1,
            onClick = { selectedTab = 1 },
            text = {
              Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                Icon(imageVector = Icons.Default.AutoAwesome, contentDescription = null, modifier = Modifier.size(16.dp), tint = BrandPurple)
                Text("AI Cover Letter", fontWeight = FontWeight.Bold, fontSize = 13.sp, color = if (selectedTab == 1) BrandPurple else TextSecondary)
              }
            }
          )
        }

        HorizontalDivider(color = SurfaceCardBorder)

        // TAB BODY
        Box(
          modifier = Modifier
            .weight(1f)
            .fillMaxWidth()
        ) {
          if (selectedTab == 0) {
            ResumeStudioPreviewContent(
              uiState = uiState,
              viewModel = viewModel,
              userSession = userSession,
              onOpenBilling = onOpenBilling
            )
          } else {
            CoverLetterGeneratorContent(
              uiState = uiState,
              viewModel = viewModel,
              userSession = userSession,
              onOpenBilling = onOpenBilling
            )
          }
        }
      }
    }
  }
}

// -----------------------------------------------------------------------------
// TAB 1: RESUME STUDIO PREVIEW CONTENT
// -----------------------------------------------------------------------------
@Composable
fun ResumeStudioPreviewContent(
  uiState: ResumeBuilderUiState,
  viewModel: ResumeBuilderViewModel,
  userSession: UserSession? = null,
  onOpenBilling: () -> Unit = {}
) {
  val context = LocalContext.current
  val scrollState = rememberScrollState()
  val plan = userSession?.subscriptionPlan ?: SubscriptionPlan.FREE

  // Accent Colors list
  val colorPalette = listOf(
    "#6366F1" to "Indigo",
    "#10B981" to "Emerald",
    "#0284C7" to "Ocean",
    "#8B5CF6" to "Purple",
    "#E11D48" to "Crimson",
    "#D97706" to "Amber"
  )

  val parsedAccentColor = try {
    Color(android.graphics.Color.parseColor(uiState.accentColorHex))
  } catch (e: Exception) {
    BrandPrimary
  }

  val selectedFontFamily = when (uiState.fontStyle) {
    "Classic Serif" -> FontFamily.Serif
    "Monospace" -> FontFamily.Monospace
    "Display Bold" -> FontFamily.SansSerif
    else -> FontFamily.Default
  }

  Column(
    modifier = Modifier.fillMaxSize()
  ) {
    // CUSTOMIZER TOOLBAR
    Column(
      modifier = Modifier
        .fillMaxWidth()
        .background(SurfaceCard)
        .padding(horizontal = 16.dp, vertical = 10.dp),
      verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
      // Templates Row
      Row(
        modifier = Modifier.horizontalScroll(rememberScrollState()),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
      ) {
        val templates = listOf("Executive Linear", "Modern Tech", "Minimalist Elegant", "Creative Hybrid", "European Standard", "Executive Grid")
        val premiumTemplates = setOf("Creative Hybrid", "European Standard", "Executive Grid")

        templates.forEach { tmpl ->
          val isSelected = tmpl == uiState.selectedTemplate
          val isPremium = tmpl in premiumTemplates

          Surface(
            shape = RoundedCornerShape(20.dp),
            color = if (isSelected) BrandPrimary else SurfaceCardBorder,
            modifier = Modifier.clickable {
              if (isPremium && plan == SubscriptionPlan.FREE) {
                Toast.makeText(context, "$tmpl is a Pro/Premium template. Please upgrade!", Toast.LENGTH_SHORT).show()
                onOpenBilling()
              } else {
                viewModel.updateSelectedTemplate(tmpl)
              }
            }
          ) {
            Row(
              verticalAlignment = Alignment.CenterVertically,
              horizontalArrangement = Arrangement.spacedBy(4.dp),
              modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
            ) {
              Text(
                text = tmpl,
                color = Color.White,
                fontSize = 11.sp,
                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
              )
              if (isPremium && plan == SubscriptionPlan.FREE) {
                Icon(
                  imageVector = Icons.Default.AutoAwesome,
                  contentDescription = "Pro",
                  tint = BrandAmber,
                  modifier = Modifier.size(12.dp)
                )
              }
            }
          }
        }
      }

      // Font & Color Customization Controls
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        // Colors
        Row(
          horizontalArrangement = Arrangement.spacedBy(6.dp),
          verticalAlignment = Alignment.CenterVertically
        ) {
          Text(text = "Color:", color = TextMuted, fontSize = 11.sp)
          colorPalette.forEach { (hex, _) ->
            val c = Color(android.graphics.Color.parseColor(hex))
            Box(
              modifier = Modifier
                .size(20.dp)
                .clip(CircleShape)
                .background(c)
                .border(
                  width = if (uiState.accentColorHex == hex) 2.dp else 0.dp,
                  color = Color.White,
                  shape = CircleShape
                )
                .clickable { viewModel.updateAccentColorHex(hex) }
            )
          }
        }

        // Fonts
        Row(
          horizontalArrangement = Arrangement.spacedBy(6.dp),
          verticalAlignment = Alignment.CenterVertically
        ) {
          Text(text = "Font:", color = TextMuted, fontSize = 11.sp)
          val fonts = listOf("Modern Sans", "Classic Serif", "Monospace")
          fonts.forEach { fontName ->
            val isSel = fontName == uiState.fontStyle
            Text(
              text = fontName.take(6),
              color = if (isSel) BrandPrimary else TextSecondary,
              fontSize = 11.sp,
              fontWeight = if (isSel) FontWeight.Bold else FontWeight.Normal,
              modifier = Modifier
                .clickable { viewModel.updateFontStyle(fontName) }
                .padding(horizontal = 4.dp, vertical = 2.dp)
            )
          }
        }

        // Zoom Scale
        Row(
          horizontalArrangement = Arrangement.spacedBy(4.dp),
          verticalAlignment = Alignment.CenterVertically
        ) {
          IconButton(
            onClick = { viewModel.updateZoomScale((uiState.zoomScale - 0.15f).coerceAtLeast(0.6f)) },
            modifier = Modifier.size(24.dp)
          ) {
            Icon(imageVector = Icons.Default.ZoomOut, contentDescription = "Zoom Out", tint = TextMuted, modifier = Modifier.size(16.dp))
          }
          Text(text = "${(uiState.zoomScale * 100).toInt()}%", color = TextPrimary, fontSize = 11.sp, fontWeight = FontWeight.Bold)
          IconButton(
            onClick = { viewModel.updateZoomScale((uiState.zoomScale + 0.15f).coerceAtMost(1.4f)) },
            modifier = Modifier.size(24.dp)
          ) {
            Icon(imageVector = Icons.Default.ZoomIn, contentDescription = "Zoom In", tint = TextMuted, modifier = Modifier.size(16.dp))
          }
        }
      }
    }

    HorizontalDivider(color = SurfaceCardBorder)

    // CANVAS RESUME SHEET
    Box(
      modifier = Modifier
        .weight(1f)
        .fillMaxWidth()
        .padding(12.dp),
      contentAlignment = Alignment.TopCenter
    ) {
      Surface(
        shape = RoundedCornerShape(12.dp),
        color = Color(0xFF18181B),
        modifier = Modifier
          .fillMaxWidth()
          .verticalScroll(scrollState),
        border = CardDefaults.outlinedCardBorder().copy(brush = Brush.verticalGradient(listOf(parsedAccentColor, SurfaceCardBorder)))
      ) {
        Column(
          modifier = Modifier
            .padding(20.dp)
            .fillMaxWidth(),
          verticalArrangement = Arrangement.spacedBy(if (uiState.spacingDensity == "Compact") 8.dp else 14.dp)
        ) {
          // HEADER BASED ON TEMPLATE
          when (uiState.selectedTemplate) {
            "Modern Tech" -> {
              Surface(
                shape = RoundedCornerShape(12.dp),
                color = parsedAccentColor.copy(alpha = 0.15f),
                modifier = Modifier.fillMaxWidth()
              ) {
                Column(modifier = Modifier.padding(14.dp)) {
                  Text(text = uiState.fullName.ifBlank { "Full Name" }, color = Color.White, fontSize = (22 * uiState.zoomScale).sp, fontWeight = FontWeight.Bold, fontFamily = selectedFontFamily)
                  Text(text = uiState.headline.ifBlank { "Professional Headline" }, color = parsedAccentColor, fontSize = (14 * uiState.zoomScale).sp, fontWeight = FontWeight.SemiBold, fontFamily = selectedFontFamily)
                  Spacer(modifier = Modifier.height(4.dp))
                  Text(text = "${uiState.email} • ${uiState.phone} • ${uiState.location}", color = TextMuted, fontSize = (11 * uiState.zoomScale).sp)
                }
              }
            }
            "Minimalist Elegant" -> {
              Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
              ) {
                Text(text = uiState.fullName.ifBlank { "Full Name" }.uppercase(), color = Color.White, fontSize = (20 * uiState.zoomScale).sp, fontWeight = FontWeight.Bold, fontFamily = selectedFontFamily)
                Text(text = uiState.headline.ifBlank { "Professional Title" }, color = parsedAccentColor, fontSize = (12 * uiState.zoomScale).sp)
                Text(text = "${uiState.email}  |  ${uiState.phone}  |  ${uiState.location}", color = TextMuted, fontSize = (10 * uiState.zoomScale).sp)
                HorizontalDivider(modifier = Modifier.padding(top = 10.dp), color = parsedAccentColor)
              }
            }
            else -> {
              // Executive Linear default
              Column {
                Text(text = uiState.fullName.ifBlank { "Full Name" }, color = Color.White, fontSize = (20 * uiState.zoomScale).sp, fontWeight = FontWeight.Bold, fontFamily = selectedFontFamily)
                Text(text = uiState.headline.ifBlank { "Professional Title" }, color = parsedAccentColor, fontSize = (13 * uiState.zoomScale).sp, fontWeight = FontWeight.SemiBold, fontFamily = selectedFontFamily)
                Text(text = "${uiState.email}  •  ${uiState.phone}  •  ${uiState.location}", color = TextMuted, fontSize = (11 * uiState.zoomScale).sp)
                HorizontalDivider(modifier = Modifier.padding(top = 8.dp), color = Color(0xFF3F3F46))
              }
            }
          }

          // SUMMARY
          Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
            Text(text = "PROFESSIONAL SUMMARY", color = parsedAccentColor, fontSize = (11 * uiState.zoomScale).sp, fontWeight = FontWeight.Bold, fontFamily = selectedFontFamily)
            Text(
              text = uiState.professionalSummary.ifBlank { "Your executive summary will render here..." },
              color = Color(0xFFD4D4D8),
              fontSize = (11 * uiState.zoomScale).sp,
              lineHeight = (15 * uiState.zoomScale).sp,
              fontFamily = selectedFontFamily
            )
          }

          // WORK EXPERIENCE
          if (uiState.workExperienceList.isNotEmpty()) {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
              Text(text = "WORK EXPERIENCE", color = parsedAccentColor, fontSize = (11 * uiState.zoomScale).sp, fontWeight = FontWeight.Bold, fontFamily = selectedFontFamily)
              uiState.workExperienceList.forEach { work ->
                Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                  Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text(text = "${work.jobTitle} — ${work.company}", color = Color.White, fontSize = (11 * uiState.zoomScale).sp, fontWeight = FontWeight.Bold, fontFamily = selectedFontFamily)
                    Text(text = "${work.startDate} - ${if (work.isCurrent) "Present" else work.endDate}", color = TextMuted, fontSize = (10 * uiState.zoomScale).sp)
                  }
                  if (work.description.isNotBlank()) {
                    Text(text = work.description, color = Color(0xFFA1A1AA), fontSize = (10 * uiState.zoomScale).sp, lineHeight = (14 * uiState.zoomScale).sp, fontFamily = selectedFontFamily)
                  }
                }
              }
            }
          }

          // SKILLS
          if (uiState.skillsList.isNotEmpty()) {
            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
              Text(text = "SKILLS & COMPETENCIES", color = parsedAccentColor, fontSize = (11 * uiState.zoomScale).sp, fontWeight = FontWeight.Bold, fontFamily = selectedFontFamily)
              Text(
                text = uiState.skillsList.joinToString(" • ") { it.name },
                color = Color(0xFFD4D4D8),
                fontSize = (11 * uiState.zoomScale).sp,
                fontFamily = selectedFontFamily
              )
            }
          }

          // EDUCATION
          if (uiState.educationList.isNotEmpty()) {
            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
              Text(text = "EDUCATION", color = parsedAccentColor, fontSize = (11 * uiState.zoomScale).sp, fontWeight = FontWeight.Bold, fontFamily = selectedFontFamily)
              uiState.educationList.forEach { edu ->
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                  Text(text = "${edu.degree} in ${edu.fieldOfStudy} — ${edu.school}", color = Color.White, fontSize = (11 * uiState.zoomScale).sp, fontFamily = selectedFontFamily)
                  Text(text = "${edu.startDate} - ${edu.endDate}", color = TextMuted, fontSize = (10 * uiState.zoomScale).sp)
                }
              }
            }
          }

          // PROJECTS
          if (uiState.projectsList.isNotEmpty()) {
            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
              Text(text = "KEY PROJECTS", color = parsedAccentColor, fontSize = (11 * uiState.zoomScale).sp, fontWeight = FontWeight.Bold, fontFamily = selectedFontFamily)
              uiState.projectsList.forEach { proj ->
                Column {
                  Text(text = proj.title, color = Color.White, fontSize = (11 * uiState.zoomScale).sp, fontWeight = FontWeight.Bold, fontFamily = selectedFontFamily)
                  Text(text = proj.description, color = Color(0xFFA1A1AA), fontSize = (10 * uiState.zoomScale).sp)
                }
              }
            }
          }
        }
      }
    }

    // BOTTOM EXPORT ACTIONS BAR
    Surface(
      color = SurfaceCard,
      modifier = Modifier.fillMaxWidth()
    ) {
      Row(
        modifier = Modifier
          .fillMaxWidth()
          .padding(12.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
      ) {
        Button(
          onClick = { ResumeExporter.printResume(context, uiState) },
          modifier = Modifier.weight(1f),
          shape = RoundedCornerShape(10.dp),
          colors = ButtonDefaults.buttonColors(containerColor = BrandPrimary)
        ) {
          Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
            Icon(imageVector = Icons.Default.Print, contentDescription = null, modifier = Modifier.size(16.dp))
            Text("Print", fontSize = 12.sp, fontWeight = FontWeight.Bold)
          }
        }

        Button(
          onClick = { ResumeExporter.exportResumePdf(context, uiState) },
          modifier = Modifier.weight(1.2f),
          shape = RoundedCornerShape(10.dp),
          colors = ButtonDefaults.buttonColors(containerColor = BrandPurple)
        ) {
          Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
            Icon(imageVector = Icons.Default.Download, contentDescription = null, modifier = Modifier.size(16.dp))
            Text("Export PDF", fontSize = 12.sp, fontWeight = FontWeight.Bold)
          }
        }

        OutlinedButton(
          onClick = {
            if (plan == SubscriptionPlan.FREE) {
              Toast.makeText(context, "DOCX Export is a Pro/Premium feature. Please upgrade!", Toast.LENGTH_SHORT).show()
              onOpenBilling()
            } else {
              ResumeExporter.exportResumeDocx(context, uiState)
            }
          },
          modifier = Modifier.weight(1.2f),
          shape = RoundedCornerShape(10.dp),
          colors = ButtonDefaults.outlinedButtonColors(contentColor = TextPrimary)
        ) {
          Text("Export DOCX", fontSize = 12.sp, fontWeight = FontWeight.Bold)
        }
      }
    }
  }
}

// -----------------------------------------------------------------------------
// TAB 2: AI COVER LETTER GENERATOR CONTENT
// -----------------------------------------------------------------------------
@Composable
fun CoverLetterGeneratorContent(
  uiState: ResumeBuilderUiState,
  viewModel: ResumeBuilderViewModel,
  userSession: UserSession? = null,
  onOpenBilling: () -> Unit = {}
) {
  val context = LocalContext.current
  val scrollState = rememberScrollState()
  val plan = userSession?.subscriptionPlan ?: SubscriptionPlan.FREE

  Column(
    modifier = Modifier
      .fillMaxSize()
      .padding(16.dp)
      .verticalScroll(scrollState),
    verticalArrangement = Arrangement.spacedBy(14.dp)
  ) {
    // TARGET JOB PARAMETERS CARD
    Card(
      modifier = Modifier.fillMaxWidth(),
      shape = RoundedCornerShape(16.dp),
      colors = CardDefaults.cardColors(containerColor = SurfaceCard),
      border = CardDefaults.outlinedCardBorder().copy(brush = Brush.horizontalGradient(listOf(BrandPurple, BrandPrimary)))
    ) {
      Column(
        modifier = Modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
      ) {
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Text(text = "Target Job & Country Specifications", color = TextPrimary, fontSize = 14.sp, fontWeight = FontWeight.Bold)
          Text(text = "✨ OpenAI Tailored", color = BrandPurple, fontSize = 11.sp, fontWeight = FontWeight.Bold)
        }

        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
          OutlinedTextField(
            value = uiState.targetJobTitle,
            onValueChange = { viewModel.updateCoverLetterTarget(targetTitle = it) },
            label = { Text("Target Job Title", color = TextMuted) },
            modifier = Modifier.weight(1f),
            shape = RoundedCornerShape(10.dp),
            colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = BrandPurple, unfocusedBorderColor = SurfaceCardBorder, focusedTextColor = TextPrimary)
          )

          OutlinedTextField(
            value = uiState.targetCompany,
            onValueChange = { viewModel.updateCoverLetterTarget(targetCompany = it) },
            label = { Text("Company Name", color = TextMuted) },
            modifier = Modifier.weight(1f),
            shape = RoundedCornerShape(10.dp),
            colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = BrandPurple, unfocusedBorderColor = SurfaceCardBorder, focusedTextColor = TextPrimary)
          )
        }

        // Country & Language Selectors
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
          Column(modifier = Modifier.weight(1f)) {
            Text(text = "Target Country Format:", color = TextMuted, fontSize = 11.sp)
            Row(modifier = Modifier.horizontalScroll(rememberScrollState()), horizontalArrangement = Arrangement.spacedBy(6.dp)) {
              val countries = listOf("United States", "Canada", "France", "Germany", "Spain")
              countries.forEach { c ->
                val isSel = c == uiState.coverLetterCountry
                Surface(
                  shape = RoundedCornerShape(16.dp),
                  color = if (isSel) BrandPurple else SurfaceCardBorder,
                  modifier = Modifier.clickable { viewModel.updateCoverLetterTarget(country = c) }
                ) {
                  Text(text = c, color = Color.White, fontSize = 10.sp, fontWeight = if (isSel) FontWeight.Bold else FontWeight.Normal, modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp))
                }
              }
            }
          }
        }

        // Action Generate Button
        Button(
          onClick = {
            if (!plan.allowCoverLetters) {
              Toast.makeText(context, "AI Cover Letter Generator is available on Pro and Premium plans. Please upgrade!", Toast.LENGTH_SHORT).show()
              onOpenBilling()
            } else {
              viewModel.aiGenerateCoverLetter()
            }
          },
          modifier = Modifier
            .fillMaxWidth()
            .height(48.dp)
            .testTag("generate_cover_letter_button"),
          shape = RoundedCornerShape(12.dp),
          colors = ButtonDefaults.buttonColors(containerColor = BrandPurple)
        ) {
          if (uiState.isCoverLetterLoading) {
            CircularProgressIndicator(modifier = Modifier.size(20.dp), color = Color.White, strokeWidth = 2.dp)
            Spacer(modifier = Modifier.width(10.dp))
            Text("Writing human ATS cover letter...", color = Color.White, fontWeight = FontWeight.Bold)
          } else {
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
              Icon(imageVector = Icons.Default.AutoAwesome, contentDescription = null, modifier = Modifier.size(18.dp))
              Text("Generate Tailored Cover Letter with OpenAI", fontSize = 13.sp, fontWeight = FontWeight.Bold)
            }
          }
        }
      }
    }

    // VERSIONS SELECTOR
    if (uiState.coverLetterVersions.isNotEmpty()) {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically
      ) {
        Text(text = "Versions:", color = TextMuted, fontSize = 12.sp)
        uiState.coverLetterVersions.forEachIndexed { idx, _ ->
          val isSel = uiState.coverLetterText == uiState.coverLetterVersions[idx]
          Surface(
            shape = RoundedCornerShape(12.dp),
            color = if (isSel) BrandPrimary else SurfaceCardBorder,
            modifier = Modifier.clickable { viewModel.selectCoverLetterVersion(idx) }
          ) {
            Text(text = "Version ${idx + 1}", color = Color.White, fontSize = 11.sp, fontWeight = if (isSel) FontWeight.Bold else FontWeight.Normal, modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp))
          }
        }
      }
    }

    // EDITABLE COVER LETTER TEXT SHEET
    Card(
      modifier = Modifier.fillMaxWidth(),
      shape = RoundedCornerShape(16.dp),
      colors = CardDefaults.cardColors(containerColor = SurfaceCard),
      border = CardDefaults.outlinedCardBorder()
    ) {
      Column(
        modifier = Modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
      ) {
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Text(text = "Letter Draft Editor", color = TextPrimary, fontSize = 14.sp, fontWeight = FontWeight.Bold)
          if (uiState.coverLetterText.isNotBlank()) {
            Text(text = "${uiState.coverLetterText.length} chars", color = TextMuted, fontSize = 11.sp)
          }
        }

        OutlinedTextField(
          value = uiState.coverLetterText,
          onValueChange = { viewModel.updateCoverLetterText(it) },
          modifier = Modifier
            .fillMaxWidth()
            .height(280.dp),
          placeholder = { Text("Click 'Generate Tailored Cover Letter' above to create an ATS-optimized letter...", color = TextMuted) },
          shape = RoundedCornerShape(12.dp),
          colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = BrandPrimary,
            unfocusedBorderColor = SurfaceCardBorder,
            focusedTextColor = TextPrimary,
            unfocusedTextColor = TextSecondary
          )
        )

        // EXPORT BUTTONS FOR COVER LETTER
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
          Button(
            onClick = {
              if (uiState.coverLetterText.isNotBlank()) {
                ResumeExporter.exportCoverLetterPdf(context, uiState.fullName, uiState.targetCompany, uiState.coverLetterText)
              } else {
                Toast.makeText(context, "Please generate or write a cover letter first!", Toast.LENGTH_SHORT).show()
              }
            },
            modifier = Modifier.weight(1f),
            shape = RoundedCornerShape(10.dp),
            colors = ButtonDefaults.buttonColors(containerColor = BrandPurple)
          ) {
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
              Icon(imageVector = Icons.Default.Download, contentDescription = null, modifier = Modifier.size(16.dp))
              Text("Download Letter PDF", fontSize = 12.sp, fontWeight = FontWeight.Bold)
            }
          }

          OutlinedButton(
            onClick = {
              if (uiState.coverLetterText.isNotBlank()) {
                ResumeExporter.exportResumeDocx(context, uiState)
              } else {
                Toast.makeText(context, "Please generate a letter first!", Toast.LENGTH_SHORT).show()
              }
            },
            modifier = Modifier.weight(1f),
            shape = RoundedCornerShape(10.dp),
            colors = ButtonDefaults.outlinedButtonColors(contentColor = TextPrimary)
          ) {
            Text("Export DOCX", fontSize = 12.sp, fontWeight = FontWeight.Bold)
          }
        }
      }
    }
  }
}
