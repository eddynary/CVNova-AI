package com.example.ui.landing

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
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
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.EditNote
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.Extension
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.RocketLaunch
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.SupportAgent
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material.icons.filled.Work
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.R
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

/**
 * Premium Homepage / Landing Page inspired by Stripe, Linear, Notion, and Resume.io.
 */
@Composable
fun LandingScreen(
  onStartBuilding: () -> Unit = {},
  onOpenAuth: () -> Unit = {}
) {
  var showDemoModal by remember { mutableStateOf(false) }
  var selectedTemplateForModal by remember { mutableStateOf<TemplateItem?>(null) }
  var selectedPricingTier by remember { mutableStateOf<PricingTier?>(null) }
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
      // 1. Navigation Header
      HeaderNavigation(
        onStartClick = { showDemoModal = true },
        onAuthClick = onOpenAuth
      )

      // 2. Hero Section
      HeroSection(
        onPrimaryCta = { showDemoModal = true },
        onSecondaryCta = {
          // Scroll or trigger template preview modal
          selectedTemplateForModal = sampleTemplates.first()
        }
      )

      Spacer(modifier = Modifier.height(48.dp))

      // 3. Statistics Section
      StatisticsSection()

      Spacer(modifier = Modifier.height(64.dp))

      // 4. Features Bento Grid Section (Linear & Notion style)
      FeaturesBentoSection()

      Spacer(modifier = Modifier.height(64.dp))

      // 5. Resume Templates Showcase Section (Resume.io style)
      TemplatesShowcaseSection(
        onSelectTemplate = { template ->
          selectedTemplateForModal = template
        }
      )

      Spacer(modifier = Modifier.height(64.dp))

      // 6. Testimonials Section (Stripe style)
      TestimonialsSection()

      Spacer(modifier = Modifier.height(64.dp))

      // 7. Pricing Section
      PricingSection(
        onSelectPlan = { tier ->
          selectedPricingTier = tier
        }
      )

      Spacer(modifier = Modifier.height(64.dp))

      // 8. FAQ Section
      FaqSection()

      Spacer(modifier = Modifier.height(64.dp))

      // 9. Bottom CTA Banner
      BottomCtaBanner(
        onStartClick = { showDemoModal = true }
      )

      Spacer(modifier = Modifier.height(48.dp))

      // 10. Footer Section
      FooterSection()

      Spacer(modifier = Modifier.navigationBarsPadding())
    }

    // Modal for Interactive Preview or Template Quick-Start
    if (showDemoModal) {
      QuickStartDialog(
        onDismiss = { showDemoModal = false },
        onConfirm = {
          showDemoModal = false
          onStartBuilding()
        }
      )
    }

    if (selectedTemplateForModal != null) {
      TemplatePreviewDialog(
        template = selectedTemplateForModal!!,
        onDismiss = { selectedTemplateForModal = null },
        onUseTemplate = {
          selectedTemplateForModal = null
          onStartBuilding()
        }
      )
    }

    if (selectedPricingTier != null) {
      PlanCheckoutDialog(
        plan = selectedPricingTier!!,
        onDismiss = { selectedPricingTier = null }
      )
    }
  }
}

// -----------------------------------------------------------------------------
// 1. HEADER NAVIGATION
// -----------------------------------------------------------------------------
@Composable
fun HeaderNavigation(
  onStartClick: () -> Unit,
  onAuthClick: () -> Unit = {}
) {
  Row(
    modifier = Modifier
      .fillMaxWidth()
      .statusBarsPadding()
      .padding(horizontal = 20.dp, vertical = 16.dp),
    horizontalArrangement = Arrangement.SpaceBetween,
    verticalAlignment = Alignment.CenterVertically
  ) {
    // Brand Logo
    Row(
      verticalAlignment = Alignment.CenterVertically,
      horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
      Box(
        modifier = Modifier
          .size(38.dp)
          .clip(RoundedCornerShape(10.dp))
          .background(
            Brush.linearGradient(
              colors = listOf(BrandPrimary, BrandSecondary)
            )
          ),
        contentAlignment = Alignment.Center
      ) {
        Icon(
          imageVector = Icons.Default.Description,
          contentDescription = "Logo",
          tint = Color.White,
          modifier = Modifier.size(20.dp)
        )
      }

      Column {
        Text(
          text = "Resume Studio",
          color = TextPrimary,
          fontSize = 18.sp,
          fontWeight = FontWeight.Bold,
          letterSpacing = (-0.5).sp
        )
        Text(
          text = "AI CAREER ENGINE",
          color = BrandSecondary,
          fontSize = 9.sp,
          fontWeight = FontWeight.Bold,
          letterSpacing = 1.sp
        )
      }
    }

    // Header CTAs Row
    Row(
      verticalAlignment = Alignment.CenterVertically,
      horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
      OutlinedButton(
        onClick = onAuthClick,
        modifier = Modifier
          .height(38.dp)
          .testTag("header_signin_button"),
        shape = RoundedCornerShape(20.dp),
        border = ButtonDefaults.outlinedButtonBorder.copy(
          brush = Brush.horizontalGradient(listOf(SurfaceCardBorder, SurfaceCardBorder))
        ),
        colors = ButtonDefaults.outlinedButtonColors(
          contentColor = TextPrimary
        )
      ) {
        Text(
          text = "Sign In",
          fontSize = 12.sp,
          fontWeight = FontWeight.SemiBold
        )
      }

      Button(
        onClick = onStartClick,
        modifier = Modifier
          .height(38.dp)
          .testTag("header_cta_build_button"),
        shape = RoundedCornerShape(20.dp),
        colors = ButtonDefaults.buttonColors(
          containerColor = BrandPrimary,
          contentColor = Color.White
        )
      ) {
        Row(
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
          Icon(
            imageVector = Icons.Default.AutoAwesome,
            contentDescription = null,
            modifier = Modifier.size(14.dp)
          )
          Text(
            text = "Build Resume",
            fontSize = 12.sp,
            fontWeight = FontWeight.SemiBold
          )
        }
      }
    }
  }
}

// -----------------------------------------------------------------------------
// 2. HERO SECTION
// -----------------------------------------------------------------------------
@Composable
fun HeroSection(
  onPrimaryCta: () -> Unit,
  onSecondaryCta: () -> Unit
) {
  val infiniteTransition = rememberInfiniteTransition(label = "HeroGlow")
  val alphaGlow by infiniteTransition.animateFloat(
    initialValue = 0.4f,
    targetValue = 0.8f,
    animationSpec = infiniteRepeatable(
      animation = tween(2000, easing = FastOutSlowInEasing),
      repeatMode = RepeatMode.Reverse
    ),
    label = "alphaGlow"
  )

  Column(
    modifier = Modifier
      .fillMaxWidth()
      .padding(horizontal = 20.dp, vertical = 24.dp),
    horizontalAlignment = Alignment.CenterHorizontally
  ) {
    // Shimmer Pill Badge
    Box(
      modifier = Modifier
        .clip(RoundedCornerShape(30.dp))
        .background(BrandPrimary.copy(alpha = 0.15f))
        .border(
          width = 1.dp,
          brush = Brush.horizontalGradient(
            colors = listOf(BrandPrimary.copy(alpha = 0.6f), BrandSecondary.copy(alpha = 0.6f))
          ),
          shape = RoundedCornerShape(30.dp)
        )
        .padding(horizontal = 14.dp, vertical = 6.dp)
    ) {
      Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
      ) {
        Icon(
          imageVector = Icons.Default.AutoAwesome,
          contentDescription = null,
          tint = BrandSecondary,
          modifier = Modifier.size(14.dp)
        )
        Text(
          text = "Gemini 1.5 Pro • 2026 AI Resume Engine",
          color = TextPrimary,
          fontSize = 12.sp,
          fontWeight = FontWeight.Medium
        )
      }
    }

    Spacer(modifier = Modifier.height(20.dp))

    // Title
    Text(
      text = "Craft Resumes That Land High-Paying Tech & Design Roles",
      color = TextPrimary,
      fontSize = 32.sp,
      fontWeight = FontWeight.ExtraBold,
      textAlign = TextAlign.Center,
      lineHeight = 40.sp,
      letterSpacing = (-0.8).sp,
      modifier = Modifier.widthIn(max = 600.dp)
    )

    Spacer(modifier = Modifier.height(16.dp))

    // Subtitle
    Text(
      text = "Linear-speed editor. Notion-style blocks. Resume.io aesthetics. Instant real-time ATS optimization that guarantees top recruiter visibility.",
      color = TextSecondary,
      fontSize = 15.sp,
      fontWeight = FontWeight.Normal,
      textAlign = TextAlign.Center,
      lineHeight = 22.sp,
      modifier = Modifier.widthIn(max = 520.dp)
    )

    Spacer(modifier = Modifier.height(28.dp))

    // CTAs
    Column(
      modifier = Modifier.fillMaxWidth(),
      horizontalAlignment = Alignment.CenterHorizontally,
      verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
      Button(
        onClick = onPrimaryCta,
        modifier = Modifier
          .fillMaxWidth()
          .height(52.dp)
          .widthIn(max = 340.dp)
          .testTag("cta_start_free"),
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
          Text(
            text = "Start Building Free",
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold
          )
          Spacer(modifier = Modifier.width(8.dp))
          Icon(
            imageVector = Icons.AutoMirrored.Filled.ArrowForward,
            contentDescription = null,
            modifier = Modifier.size(18.dp)
          )
        }
      }

      OutlinedButton(
        onClick = onSecondaryCta,
        modifier = Modifier
          .fillMaxWidth()
          .height(52.dp)
          .widthIn(max = 340.dp)
          .testTag("cta_explore_templates"),
        shape = RoundedCornerShape(12.dp),
        border = ButtonDefaults.outlinedButtonBorder.copy(
          brush = Brush.horizontalGradient(
            colors = listOf(SurfaceCardBorder, SurfaceCardBorder)
          )
        ),
        colors = ButtonDefaults.outlinedButtonColors(
          contentColor = TextPrimary
        )
      ) {
        Row(
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.Center
        ) {
          Icon(
            imageVector = Icons.Default.Description,
            contentDescription = null,
            tint = BrandSecondary,
            modifier = Modifier.size(18.dp)
          )
          Spacer(modifier = Modifier.width(8.dp))
          Text(
            text = "Explore 24+ Templates",
            fontSize = 15.sp,
            fontWeight = FontWeight.SemiBold
          )
        }
      }
    }

    Spacer(modifier = Modifier.height(36.dp))

    // Interactive Showcase Card Preview with Hero Graphic
    Card(
      modifier = Modifier
        .fillMaxWidth()
        .widthIn(max = 680.dp)
        .shadow(24.dp, RoundedCornerShape(20.dp), spotColor = BrandPrimary.copy(alpha = alphaGlow)),
      shape = RoundedCornerShape(20.dp),
      colors = CardDefaults.cardColors(containerColor = SurfaceCard),
      border = CardDefaults.outlinedCardBorder().copy(
        brush = Brush.linearGradient(
          colors = listOf(BrandPrimary.copy(alpha = 0.5f), SurfaceCardBorder)
        )
      )
    ) {
      Column {
        // Window bar mockup
        Row(
          modifier = Modifier
            .fillMaxWidth()
            .background(SurfaceDark)
            .padding(horizontal = 16.dp, vertical = 12.dp),
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.SpaceBetween
        ) {
          Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
            Box(
              modifier = Modifier
                .size(10.dp)
                .clip(CircleShape)
                .background(Color(0xFFFF5F56))
            )
            Box(
              modifier = Modifier
                .size(10.dp)
                .clip(CircleShape)
                .background(Color(0xFFFFBD2E))
            )
            Box(
              modifier = Modifier
                .size(10.dp)
                .clip(CircleShape)
                .background(Color(0xFF27C93F))
            )
          }

          Text(
            text = "resumestudio.app/editor/live-preview",
            color = TextMuted,
            fontSize = 11.sp,
            fontWeight = FontWeight.Medium
          )

          Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp)
          ) {
            Box(
              modifier = Modifier
                .size(8.dp)
                .clip(CircleShape)
                .background(BrandAccent)
            )
            Text(
              text = "ATS Live 98%",
              color = BrandAccent,
              fontSize = 10.sp,
              fontWeight = FontWeight.Bold
            )
          }
        }

        // Hero Showcase Image
        Image(
          painter = painterResource(id = R.drawable.resume_showcase_hero_1785163177361),
          contentDescription = "Resume Studio Live Showcase Interface",
          modifier = Modifier
            .fillMaxWidth()
            .height(200.dp),
          contentScale = ContentScale.Crop
        )

        // Floating Micro Metrics Row
        Row(
          modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
          horizontalArrangement = Arrangement.SpaceAround
        ) {
          HeroMicroMetric(title = "ATS Pass Rate", value = "98.7%", icon = Icons.Default.Verified)
          HeroMicroMetric(title = "Salary Lift", value = "+34%", icon = Icons.Default.RocketLaunch)
          HeroMicroMetric(title = "AI Gen Speed", value = "1.2 sec", icon = Icons.Default.Speed)
        }
      }
    }
  }
}

@Composable
fun HeroMicroMetric(title: String, value: String, icon: ImageVector) {
  Row(
    verticalAlignment = Alignment.CenterVertically,
    horizontalArrangement = Arrangement.spacedBy(8.dp)
  ) {
    Box(
      modifier = Modifier
        .size(32.dp)
        .clip(RoundedCornerShape(8.dp))
        .background(BrandPrimary.copy(alpha = 0.2f)),
      contentAlignment = Alignment.Center
    ) {
      Icon(
        imageVector = icon,
        contentDescription = null,
        tint = BrandSecondary,
        modifier = Modifier.size(16.dp)
      )
    }
    Column {
      Text(
        text = value,
        color = TextPrimary,
        fontSize = 14.sp,
        fontWeight = FontWeight.Bold
      )
      Text(
        text = title,
        color = TextMuted,
        fontSize = 10.sp
      )
    }
  }
}

// -----------------------------------------------------------------------------
// 3. STATISTICS SECTION
// -----------------------------------------------------------------------------
@Composable
fun StatisticsSection() {
  Column(
    modifier = Modifier
      .fillMaxWidth()
      .padding(horizontal = 20.dp),
    horizontalAlignment = Alignment.CenterHorizontally
  ) {
    Text(
      text = "PROVEN CAREER RESULTS",
      color = BrandSecondary,
      fontSize = 11.sp,
      fontWeight = FontWeight.Bold,
      letterSpacing = 1.5.sp
    )

    Spacer(modifier = Modifier.height(8.dp))

    Text(
      text = "Built for Professionals Demanding Maximum Impact",
      color = TextPrimary,
      fontSize = 20.sp,
      fontWeight = FontWeight.Bold,
      textAlign = TextAlign.Center
    )

    Spacer(modifier = Modifier.height(20.dp))

    Row(
      modifier = Modifier.fillMaxWidth(),
      horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
      StatCard(
        modifier = Modifier.weight(1f),
        metric = "3.4x",
        label = "More Interview Invitations",
        accent = BrandPrimary
      )
      StatCard(
        modifier = Modifier.weight(1f),
        metric = "98.7%",
        label = "Fortune 500 ATS Pass Rate",
        accent = BrandAccent
      )
    }

    Spacer(modifier = Modifier.height(12.dp))

    Row(
      modifier = Modifier.fillMaxWidth(),
      horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
      StatCard(
        modifier = Modifier.weight(1f),
        metric = "120K+",
        label = "Resumes Created This Year",
        accent = BrandSecondary
      )
      StatCard(
        modifier = Modifier.weight(1f),
        metric = "4.9 ★",
        label = "Based on 15k+ Reviews",
        accent = BrandAmber
      )
    }
  }
}

@Composable
fun StatCard(
  modifier: Modifier = Modifier,
  metric: String,
  label: String,
  accent: Color
) {
  Card(
    modifier = modifier,
    shape = RoundedCornerShape(16.dp),
    colors = CardDefaults.cardColors(containerColor = SurfaceCard),
    border = CardDefaults.outlinedCardBorder().copy(
      brush = Brush.verticalGradient(
        colors = listOf(accent.copy(alpha = 0.4f), SurfaceCardBorder)
      )
    )
  ) {
    Column(
      modifier = Modifier.padding(16.dp),
      horizontalAlignment = Alignment.Start
    ) {
      Text(
        text = metric,
        color = accent,
        fontSize = 24.sp,
        fontWeight = FontWeight.ExtraBold,
        letterSpacing = (-0.5).sp
      )
      Spacer(modifier = Modifier.height(4.dp))
      Text(
        text = label,
        color = TextSecondary,
        fontSize = 11.sp,
        lineHeight = 15.sp,
        fontWeight = FontWeight.Medium
      )
    }
  }
}

// -----------------------------------------------------------------------------
// 4. FEATURES BENTO GRID SECTION (Linear & Notion style)
// -----------------------------------------------------------------------------
@Composable
fun FeaturesBentoSection() {
  Column(
    modifier = Modifier
      .fillMaxWidth()
      .padding(horizontal = 20.dp)
  ) {
    Column(modifier = Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
      Text(
        text = "POWERFUL FEATURE SUITE",
        color = BrandPrimary,
        fontSize = 11.sp,
        fontWeight = FontWeight.Bold,
        letterSpacing = 1.5.sp
      )

      Spacer(modifier = Modifier.height(8.dp))

      Text(
        text = "Engineered Like Linear. Flexible Like Notion.",
        color = TextPrimary,
        fontSize = 22.sp,
        fontWeight = FontWeight.Bold,
        textAlign = TextAlign.Center
      )

      Spacer(modifier = Modifier.height(8.dp))

      Text(
        text = "Everything you need to write, score, style, and export industry-winning resumes.",
        color = TextSecondary,
        fontSize = 13.sp,
        textAlign = TextAlign.Center
      )
    }

    Spacer(modifier = Modifier.height(24.dp))

    // Bento Item 1: Large Featured Card
    BentoCard(
      title = "AI Action-Bullet Optimizer",
      description = "Transforms basic bullet points into high-impact, quantified achievement statements using deep career semantics.",
      icon = Icons.Default.Psychology,
      tag = "GEMINI 1.5 PRO",
      tagColor = BrandPurple,
      extraContent = {
        Column(
          modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(10.dp))
            .background(SurfaceDark)
            .padding(12.dp)
        ) {
          Text(
            text = "Original: \"Built a web dashboard for team metrics.\"",
            color = TextMuted,
            fontSize = 11.sp
          )
          Spacer(modifier = Modifier.height(6.dp))
          Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
              imageVector = Icons.Default.AutoAwesome,
              contentDescription = null,
              tint = BrandAccent,
              modifier = Modifier.size(14.dp)
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text(
              text = "Optimized: \"Architected high-throughput dashboard tracking 50K daily active users, boosting dev velocity by 34%.\"",
              color = TextPrimary,
              fontSize = 11.sp,
              fontWeight = FontWeight.SemiBold
            )
          }
        }
      }
    )

    Spacer(modifier = Modifier.height(12.dp))

    // Bento Row 2: 2 Column
    Row(
      modifier = Modifier.fillMaxWidth(),
      horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
      BentoCardSmall(
        modifier = Modifier.weight(1f),
        title = "Live ATS Diagnostic",
        description = "Real-time keyphrase matching against target job descriptions.",
        icon = Icons.Default.Speed,
        badgeText = "98% Pass"
      )

      BentoCardSmall(
        modifier = Modifier.weight(1f),
        title = "Notion Block Editor",
        description = "Seamless reordering with keyboard shortcuts & clean markdown.",
        icon = Icons.Default.EditNote,
        badgeText = "Drag & Drop"
      )
    }

    Spacer(modifier = Modifier.height(12.dp))

    // Bento Row 3: 2 Column
    Row(
      modifier = Modifier.fillMaxWidth(),
      horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
      BentoCardSmall(
        modifier = Modifier.weight(1f),
        title = "Stripe-Grade Security",
        description = "Encrypted local drafts, zero data selling, private cloud backups.",
        icon = Icons.Default.Lock,
        badgeText = "256-bit AES"
      )

      BentoCardSmall(
        modifier = Modifier.weight(1f),
        title = "Pixel-Perfect Export",
        description = "PDF vector rendering with zero line wrapping bugs.",
        icon = Icons.Default.Download,
        badgeText = "Vector PDF"
      )
    }
  }
}

@Composable
fun BentoCard(
  title: String,
  description: String,
  icon: ImageVector,
  tag: String,
  tagColor: Color,
  extraContent: @Composable () -> Unit
) {
  Card(
    modifier = Modifier.fillMaxWidth(),
    shape = RoundedCornerShape(18.dp),
    colors = CardDefaults.cardColors(containerColor = SurfaceCard),
    border = CardDefaults.outlinedCardBorder().copy(
      brush = Brush.linearGradient(colors = listOf(tagColor.copy(alpha = 0.5f), SurfaceCardBorder))
    )
  ) {
    Column(modifier = Modifier.padding(18.dp)) {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Box(
          modifier = Modifier
            .size(36.dp)
            .clip(RoundedCornerShape(10.dp))
            .background(tagColor.copy(alpha = 0.2f)),
          contentAlignment = Alignment.Center
        ) {
          Icon(
            imageVector = icon,
            contentDescription = null,
            tint = tagColor,
            modifier = Modifier.size(20.dp)
          )
        }

        Box(
          modifier = Modifier
            .clip(RoundedCornerShape(12.dp))
            .background(tagColor.copy(alpha = 0.15f))
            .border(1.dp, tagColor.copy(alpha = 0.3f), RoundedCornerShape(12.dp))
            .padding(horizontal = 10.dp, vertical = 4.dp)
        ) {
          Text(text = tag, color = tagColor, fontSize = 10.sp, fontWeight = FontWeight.Bold)
        }
      }

      Spacer(modifier = Modifier.height(14.dp))

      Text(text = title, color = TextPrimary, fontSize = 18.sp, fontWeight = FontWeight.Bold)
      Spacer(modifier = Modifier.height(6.dp))
      Text(text = description, color = TextSecondary, fontSize = 13.sp, lineHeight = 18.sp)

      Spacer(modifier = Modifier.height(14.dp))
      extraContent()
    }
  }
}

@Composable
fun BentoCardSmall(
  modifier: Modifier = Modifier,
  title: String,
  description: String,
  icon: ImageVector,
  badgeText: String
) {
  Card(
    modifier = modifier,
    shape = RoundedCornerShape(16.dp),
    colors = CardDefaults.cardColors(containerColor = SurfaceCard),
    border = CardDefaults.outlinedCardBorder().copy(
      brush = Brush.verticalGradient(colors = listOf(SurfaceCardBorder, SurfaceCardBorder))
    )
  ) {
    Column(
      modifier = Modifier
        .fillMaxHeight()
        .padding(14.dp),
      verticalArrangement = Arrangement.SpaceBetween
    ) {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Box(
          modifier = Modifier
            .size(32.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(BrandPrimary.copy(alpha = 0.2f)),
          contentAlignment = Alignment.Center
        ) {
          Icon(
            imageVector = icon,
            contentDescription = null,
            tint = BrandSecondary,
            modifier = Modifier.size(16.dp)
          )
        }

        Text(
          text = badgeText,
          color = BrandAccent,
          fontSize = 10.sp,
          fontWeight = FontWeight.Bold
        )
      }

      Spacer(modifier = Modifier.height(12.dp))

      Text(text = title, color = TextPrimary, fontSize = 14.sp, fontWeight = FontWeight.Bold)
      Spacer(modifier = Modifier.height(4.dp))
      Text(text = description, color = TextMuted, fontSize = 11.sp, lineHeight = 15.sp)
    }
  }
}

// -----------------------------------------------------------------------------
// 5. RESUME TEMPLATES SHOWCASE SECTION (Resume.io style)
// -----------------------------------------------------------------------------
data class TemplateItem(
  val id: String,
  val name: String,
  val category: String,
  val atsScore: String,
  val accentColor: Color,
  val description: String,
  val isPopular: Boolean = false
)

val sampleTemplates = listOf(
  TemplateItem(
    id = "executive_mono",
    name = "Executive Linear",
    category = "Tech Lead & Manager",
    atsScore = "99% ATS",
    accentColor = BrandPrimary,
    description = "Clean minimalist hierarchy with high-contrast section headers and quantified achievement focus.",
    isPopular = true
  ),
  TemplateItem(
    id = "notion_clean",
    name = "Notion Minimalist",
    category = "Design & Product",
    atsScore = "98% ATS",
    accentColor = BrandSecondary,
    description = "Clean typography layout with elegant column grid spacing and modern skill badges.",
    isPopular = false
  ),
  TemplateItem(
    id = "stripe_modern",
    name = "Stripe Gradient",
    category = "Software Engineer",
    atsScore = "97% ATS",
    accentColor = BrandPurple,
    description = "Sleek dark/light top banner accent with prominent tech stack highlights and compact project lists."
  ),
  TemplateItem(
    id = "academic_pro",
    name = "Stanford Classic",
    category = "Data & Research",
    atsScore = "100% ATS",
    accentColor = BrandAccent,
    description = "Traditional single-column layout optimized for automated corporate filtering systems."
  )
)

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun TemplatesShowcaseSection(
  onSelectTemplate: (TemplateItem) -> Unit
) {
  var selectedCategoryFilter by remember { mutableStateOf("All") }
  val filters = listOf("All", "Tech Lead & Manager", "Software Engineer", "Design & Product", "Data & Research")

  Column(
    modifier = Modifier
      .fillMaxWidth()
      .padding(horizontal = 20.dp)
  ) {
    Column(modifier = Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
      Text(
        text = "RESUME.IO INSPIRED CATALOG",
        color = BrandSecondary,
        fontSize = 11.sp,
        fontWeight = FontWeight.Bold,
        letterSpacing = 1.5.sp
      )

      Spacer(modifier = Modifier.height(8.dp))

      Text(
        text = "Battle-Tested Templates Built for Recruiter Scanning",
        color = TextPrimary,
        fontSize = 22.sp,
        fontWeight = FontWeight.Bold,
        textAlign = TextAlign.Center
      )

      Spacer(modifier = Modifier.height(8.dp))

      Text(
        text = "Tested against Workday, Greenhouse, Lever, and Taleo ATS parsers.",
        color = TextSecondary,
        fontSize = 13.sp,
        textAlign = TextAlign.Center
      )
    }

    Spacer(modifier = Modifier.height(20.dp))

    // Filter Chips Row
    FlowRow(
      modifier = Modifier.fillMaxWidth(),
      horizontalArrangement = Arrangement.spacedBy(8.dp),
      verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
      filters.forEach { filter ->
        FilterChip(
          selected = (selectedCategoryFilter == filter),
          onClick = { selectedCategoryFilter = filter },
          label = { Text(filter, fontSize = 12.sp) },
          colors = FilterChipDefaults.filterChipColors(
            selectedContainerColor = BrandPrimary,
            selectedLabelColor = Color.White,
            containerColor = SurfaceCard,
            labelColor = TextSecondary
          ),
          border = FilterChipDefaults.filterChipBorder(
            enabled = true,
            selected = selectedCategoryFilter == filter,
            borderColor = SurfaceCardBorder,
            selectedBorderColor = BrandPrimary
          )
        )
      }
    }

    Spacer(modifier = Modifier.height(20.dp))

    val filteredTemplates = if (selectedCategoryFilter == "All") {
      sampleTemplates
    } else {
      sampleTemplates.filter { it.category == selectedCategoryFilter }
    }

    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
      filteredTemplates.forEach { template ->
        TemplateShowcaseCard(
          template = template,
          onUseClick = { onSelectTemplate(template) }
        )
      }
    }
  }
}

@Composable
fun TemplateShowcaseCard(
  template: TemplateItem,
  onUseClick: () -> Unit
) {
  Card(
    modifier = Modifier.fillMaxWidth(),
    shape = RoundedCornerShape(16.dp),
    colors = CardDefaults.cardColors(containerColor = SurfaceCard),
    border = CardDefaults.outlinedCardBorder().copy(
      brush = Brush.horizontalGradient(
        colors = listOf(template.accentColor.copy(alpha = 0.4f), SurfaceCardBorder)
      )
    )
  ) {
    Column(modifier = Modifier.padding(18.dp)) {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
          Box(
            modifier = Modifier
              .size(12.dp)
              .clip(CircleShape)
              .background(template.accentColor)
          )
          Text(
            text = template.name,
            color = TextPrimary,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold
          )
        }

        if (template.isPopular) {
          Box(
            modifier = Modifier
              .clip(RoundedCornerShape(12.dp))
              .background(BrandAmber.copy(alpha = 0.2f))
              .padding(horizontal = 8.dp, vertical = 3.dp)
          ) {
            Text("★ MOST POPULAR", color = BrandAmber, fontSize = 9.sp, fontWeight = FontWeight.Bold)
          }
        }
      }

      Spacer(modifier = Modifier.height(8.dp))

      Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
      ) {
        Box(
          modifier = Modifier
            .clip(RoundedCornerShape(6.dp))
            .background(SurfaceDark)
            .padding(horizontal = 8.dp, vertical = 2.dp)
        ) {
          Text(template.category, color = TextMuted, fontSize = 10.sp)
        }

        Box(
          modifier = Modifier
            .clip(RoundedCornerShape(6.dp))
            .background(BrandAccent.copy(alpha = 0.15f))
            .padding(horizontal = 8.dp, vertical = 2.dp)
        ) {
          Text(template.atsScore, color = BrandAccent, fontSize = 10.sp, fontWeight = FontWeight.Bold)
        }
      }

      Spacer(modifier = Modifier.height(10.dp))

      Text(
        text = template.description,
        color = TextSecondary,
        fontSize = 12.sp,
        lineHeight = 16.sp
      )

      Spacer(modifier = Modifier.height(16.dp))

      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.End
      ) {
        Button(
          onClick = onUseClick,
          shape = RoundedCornerShape(10.dp),
          colors = ButtonDefaults.buttonColors(
            containerColor = template.accentColor,
            contentColor = Color.White
          ),
          modifier = Modifier.testTag("template_card_use_${template.id}")
        ) {
          Text("Use Template", fontSize = 12.sp, fontWeight = FontWeight.Bold)
          Spacer(modifier = Modifier.width(6.dp))
          Icon(
            imageVector = Icons.AutoMirrored.Filled.ArrowForward,
            contentDescription = null,
            modifier = Modifier.size(14.dp)
          )
        }
      }
    }
  }
}

// -----------------------------------------------------------------------------
// 6. TESTIMONIALS SECTION (Stripe style)
// -----------------------------------------------------------------------------
data class TestimonialItem(
  val name: String,
  val role: String,
  val company: String,
  val quote: String,
  val avatarInitials: String,
  val highlightBadge: String
)

val testimonialsList = listOf(
  TestimonialItem(
    name = "Alexander Wright",
    role = "Senior Staff Engineer",
    company = "Google",
    quote = "Resume Studio redesigned my decade of experience into a clean Linear-style layout. Within 4 days, I received calls from Stripe and Meta.",
    avatarInitials = "AW",
    highlightBadge = "+ $65k Salary Increase"
  ),
  TestimonialItem(
    name = "Sophia Chen",
    role = "Lead Product Designer",
    company = "Airbnb",
    quote = "The Notion-style block editor made tailoring my portfolio and experience effortlessly fast. The ATS score checker is insanely accurate.",
    avatarInitials = "SC",
    highlightBadge = "3 Offers in 1 Week"
  )
)

@Composable
fun TestimonialsSection() {
  Column(
    modifier = Modifier
      .fillMaxWidth()
      .padding(horizontal = 20.dp)
  ) {
    Column(modifier = Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
      Text(
        text = "TRUSTED BY INDUSTRY LEADS",
        color = BrandPrimary,
        fontSize = 11.sp,
        fontWeight = FontWeight.Bold,
        letterSpacing = 1.5.sp
      )

      Spacer(modifier = Modifier.height(8.dp))

      Text(
        text = "Loved by Engineers, Designers, & PMs",
        color = TextPrimary,
        fontSize = 22.sp,
        fontWeight = FontWeight.Bold,
        textAlign = TextAlign.Center
      )
    }

    Spacer(modifier = Modifier.height(24.dp))

    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
      testimonialsList.forEach { item ->
        Card(
          modifier = Modifier.fillMaxWidth(),
          shape = RoundedCornerShape(18.dp),
          colors = CardDefaults.cardColors(containerColor = SurfaceCard),
          border = CardDefaults.outlinedCardBorder().copy(
            brush = Brush.verticalGradient(colors = listOf(SurfaceCardBorder, SurfaceCardBorder))
          )
        ) {
          Column(modifier = Modifier.padding(18.dp)) {
            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.SpaceBetween,
              verticalAlignment = Alignment.CenterVertically
            ) {
              Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                Box(
                  modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(BrandPrimary),
                  contentAlignment = Alignment.Center
                ) {
                  Text(item.avatarInitials, color = Color.White, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                }

                Column {
                  Text(item.name, color = TextPrimary, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                  Text("${item.role} @ ${item.company}", color = TextMuted, fontSize = 11.sp)
                }
              }

              Box(
                modifier = Modifier
                  .clip(RoundedCornerShape(10.dp))
                  .background(BrandAccent.copy(alpha = 0.15f))
                  .padding(horizontal = 8.dp, vertical = 4.dp)
              ) {
                Text(item.highlightBadge, color = BrandAccent, fontSize = 10.sp, fontWeight = FontWeight.Bold)
              }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Rating Stars
            Row(horizontalArrangement = Arrangement.spacedBy(2.dp)) {
              repeat(5) {
                Icon(
                  imageVector = Icons.Default.Star,
                  contentDescription = null,
                  tint = BrandAmber,
                  modifier = Modifier.size(14.dp)
                )
              }
            }

            Spacer(modifier = Modifier.height(10.dp))

            Text(
              text = "\"${item.quote}\"",
              color = TextSecondary,
              fontSize = 13.sp,
              lineHeight = 18.sp
            )
          }
        }
      }
    }
  }
}

// -----------------------------------------------------------------------------
// 7. PRICING SECTION
// -----------------------------------------------------------------------------
data class PricingTier(
  val name: String,
  val priceMonthly: String,
  val priceAnnual: String,
  val description: String,
  val features: List<String>,
  val isPopular: Boolean = false,
  val buttonText: String = "Select Plan"
)

val pricingTiers = listOf(
  PricingTier(
    name = "Starter Free",
    priceMonthly = "$0",
    priceAnnual = "$0",
    description = "Essential features for crafting a single resume.",
    features = listOf(
      "1 AI-optimized Resume Draft",
      "Standard ATS Diagnostic",
      "Vector PDF Export",
      "Basic Notion Editor"
    ),
    buttonText = "Start Free"
  ),
  PricingTier(
    name = "Pro Career",
    priceMonthly = "$19/mo",
    priceAnnual = "$12/mo",
    description = "Full Gemini AI superpowers for active job hunters.",
    features = listOf(
      "Unlimited AI Resume Generations",
      "Unlimited Real-time ATS Checks",
      "All 24+ Premium Resume Templates",
      "Matching Cover Letter Creator",
      "1-Click PDF & JSON Exports"
    ),
    isPopular = true,
    buttonText = "Get Pro Pass"
  )
)

@Composable
fun PricingSection(
  onSelectPlan: (PricingTier) -> Unit
) {
  var isAnnual by remember { mutableStateOf(true) }

  Column(
    modifier = Modifier
      .fillMaxWidth()
      .padding(horizontal = 20.dp)
  ) {
    Column(modifier = Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
      Text(
        text = "SIMPLE TRANSPARENT PRICING",
        color = BrandSecondary,
        fontSize = 11.sp,
        fontWeight = FontWeight.Bold,
        letterSpacing = 1.5.sp
      )

      Spacer(modifier = Modifier.height(8.dp))

      Text(
        text = "Invest in Your Next Career Leap",
        color = TextPrimary,
        fontSize = 22.sp,
        fontWeight = FontWeight.Bold,
        textAlign = TextAlign.Center
      )

      Spacer(modifier = Modifier.height(16.dp))

      // Billing Switcher
      Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(10.dp)
      ) {
        Text(
          text = "Monthly",
          color = if (!isAnnual) TextPrimary else TextMuted,
          fontSize = 13.sp,
          fontWeight = FontWeight.Medium
        )

        Switch(
          checked = isAnnual,
          onCheckedChange = { isAnnual = it },
          colors = SwitchDefaults.colors(
            checkedThumbColor = Color.White,
            checkedTrackColor = BrandPrimary,
            uncheckedThumbColor = TextMuted,
            uncheckedTrackColor = SurfaceCard
          )
        )

        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
          Text(
            text = "Annual",
            color = if (isAnnual) TextPrimary else TextMuted,
            fontSize = 13.sp,
            fontWeight = FontWeight.Medium
          )
          Box(
            modifier = Modifier
              .clip(RoundedCornerShape(10.dp))
              .background(BrandAccent.copy(alpha = 0.2f))
              .padding(horizontal = 6.dp, vertical = 2.dp)
          ) {
            Text("SAVE 40%", color = BrandAccent, fontSize = 9.sp, fontWeight = FontWeight.Bold)
          }
        }
      }
    }

    Spacer(modifier = Modifier.height(24.dp))

    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
      pricingTiers.forEach { tier ->
        val price = if (isAnnual) tier.priceAnnual else tier.priceMonthly

        Card(
          modifier = Modifier.fillMaxWidth(),
          shape = RoundedCornerShape(18.dp),
          colors = CardDefaults.cardColors(
            containerColor = if (tier.isPopular) SurfaceCard else SurfaceDark
          ),
          border = CardDefaults.outlinedCardBorder().copy(
            brush = Brush.verticalGradient(
              colors = if (tier.isPopular) listOf(BrandPrimary, BrandSecondary) else listOf(SurfaceCardBorder, SurfaceCardBorder)
            )
          )
        ) {
          Column(modifier = Modifier.padding(20.dp)) {
            if (tier.isPopular) {
              Box(
                modifier = Modifier
                  .clip(RoundedCornerShape(12.dp))
                  .background(BrandPrimary)
                  .padding(horizontal = 10.dp, vertical = 4.dp)
              ) {
                Text("RECOMMENDED FOR JOB SEARCH", color = Color.White, fontSize = 9.sp, fontWeight = FontWeight.Bold)
              }
              Spacer(modifier = Modifier.height(10.dp))
            }

            Text(text = tier.name, color = TextPrimary, fontSize = 18.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = tier.description, color = TextMuted, fontSize = 12.sp)

            Spacer(modifier = Modifier.height(14.dp))

            Row(verticalAlignment = Alignment.Bottom) {
              Text(
                text = price,
                color = TextPrimary,
                fontSize = 32.sp,
                fontWeight = FontWeight.ExtraBold,
                letterSpacing = (-1).sp
              )
              if (price != "$0") {
                Text(
                  text = if (isAnnual) " / mo billed annually" else " / month",
                  color = TextMuted,
                  fontSize = 12.sp,
                  modifier = Modifier.padding(bottom = 6.dp, start = 4.dp)
                )
              }
            }

            Spacer(modifier = Modifier.height(16.dp))
            HorizontalDivider(color = SurfaceCardBorder)
            Spacer(modifier = Modifier.height(16.dp))

            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
              tier.features.forEach { feature ->
                Row(
                  verticalAlignment = Alignment.CenterVertically,
                  horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                  Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = null,
                    tint = BrandAccent,
                    modifier = Modifier.size(16.dp)
                  )
                  Text(text = feature, color = TextSecondary, fontSize = 12.sp)
                }
              }
            }

            Spacer(modifier = Modifier.height(20.dp))

            Button(
              onClick = { onSelectPlan(tier) },
              modifier = Modifier
                .fillMaxWidth()
                .height(48.dp)
                .testTag("pricing_plan_select_${tier.name.lowercase().replace(" ", "_")}"),
              shape = RoundedCornerShape(10.dp),
              colors = ButtonDefaults.buttonColors(
                containerColor = if (tier.isPopular) BrandPrimary else SurfaceCardBorder,
                contentColor = Color.White
              )
            ) {
              Text(text = tier.buttonText, fontSize = 14.sp, fontWeight = FontWeight.Bold)
            }
          }
        }
      }
    }
  }
}

// -----------------------------------------------------------------------------
// 8. FAQ SECTION
// -----------------------------------------------------------------------------
data class FaqItem(
  val question: String,
  val answer: String
)

val faqList = listOf(
  FaqItem(
    question = "How does the ATS score checker work?",
    answer = "Our Gemini 1.5 Pro integration parses target job descriptions, extracts key skills, keywords, and action verbs, and compares them against your resume blocks to generate a live 0-100% compatibility score."
  ),
  FaqItem(
    question = "Can I export my resume without watermarks?",
    answer = "Yes! All resume exports (both Free and Pro) are rendered as clean, high-resolution vector PDFs with zero watermarks or forced branding."
  ),
  FaqItem(
    question = "Is my career data kept private and secure?",
    answer = "100%. Your drafts are stored locally on your device with 256-bit AES encryption. We never sell user career data to recruiters or third-party advertisers."
  ),
  FaqItem(
    question = "How are templates inspired by Resume.io and Stripe created?",
    answer = "Our design team meticulously crafted layout grids following Notion's spacing, Stripe's typography hierarchy, and Resume.io's ATS-tested document structures."
  )
)

@Composable
fun FaqSection() {
  var expandedIndex by remember { mutableIntStateOf(-1) }

  Column(
    modifier = Modifier
      .fillMaxWidth()
      .padding(horizontal = 20.dp)
  ) {
    Column(modifier = Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
      Text(
        text = "FREQUENTLY ASKED QUESTIONS",
        color = BrandPrimary,
        fontSize = 11.sp,
        fontWeight = FontWeight.Bold,
        letterSpacing = 1.5.sp
      )

      Spacer(modifier = Modifier.height(8.dp))

      Text(
        text = "Everything You Need to Know",
        color = TextPrimary,
        fontSize = 22.sp,
        fontWeight = FontWeight.Bold,
        textAlign = TextAlign.Center
      )
    }

    Spacer(modifier = Modifier.height(24.dp))

    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
      faqList.forEachIndexed { index, item ->
        val isExpanded = (expandedIndex == index)

        Card(
          modifier = Modifier
            .fillMaxWidth()
            .clickable { expandedIndex = if (isExpanded) -1 else index }
            .testTag("faq_item_$index"),
          shape = RoundedCornerShape(14.dp),
          colors = CardDefaults.cardColors(containerColor = SurfaceCard),
          border = CardDefaults.outlinedCardBorder().copy(
            brush = Brush.verticalGradient(colors = listOf(SurfaceCardBorder, SurfaceCardBorder))
          )
        ) {
          Column(
            modifier = Modifier
              .animateContentSize()
              .padding(16.dp)
          ) {
            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.SpaceBetween,
              verticalAlignment = Alignment.CenterVertically
            ) {
              Text(
                text = item.question,
                color = TextPrimary,
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.weight(1f)
              )

              Icon(
                imageVector = if (isExpanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                contentDescription = null,
                tint = BrandSecondary
              )
            }

            AnimatedVisibility(
              visible = isExpanded,
              enter = fadeIn() + expandVertically(),
              exit = fadeOut() + shrinkVertically()
            ) {
              Column {
                Spacer(modifier = Modifier.height(10.dp))
                HorizontalDivider(color = SurfaceCardBorder)
                Spacer(modifier = Modifier.height(10.dp))
                Text(
                  text = item.answer,
                  color = TextSecondary,
                  fontSize = 12.sp,
                  lineHeight = 17.sp
                )
              }
            }
          }
        }
      }
    }
  }
}

// -----------------------------------------------------------------------------
// 9. BOTTOM CTA BANNER
// -----------------------------------------------------------------------------
@Composable
fun BottomCtaBanner(
  onStartClick: () -> Unit
) {
  Box(
    modifier = Modifier
      .fillMaxWidth()
      .padding(horizontal = 20.dp)
      .clip(RoundedCornerShape(20.dp))
      .background(
        Brush.linearGradient(
          colors = listOf(BrandPrimary, BrandPurple, BrandSecondary)
        )
      )
      .padding(28.dp),
    contentAlignment = Alignment.Center
  ) {
    Column(
      horizontalAlignment = Alignment.CenterHorizontally
    ) {
      Text(
        text = "Ready to Land Your Dream Tech Role?",
        color = Color.White,
        fontSize = 22.sp,
        fontWeight = FontWeight.ExtraBold,
        textAlign = TextAlign.Center
      )

      Spacer(modifier = Modifier.height(8.dp))

      Text(
        text = "Build your high-converting resume in under 5 minutes.",
        color = Color.White.copy(alpha = 0.9f),
        fontSize = 13.sp,
        textAlign = TextAlign.Center
      )

      Spacer(modifier = Modifier.height(20.dp))

      Button(
        onClick = onStartClick,
        modifier = Modifier
          .height(48.dp)
          .testTag("bottom_cta_banner_button"),
        shape = RoundedCornerShape(24.dp),
        colors = ButtonDefaults.buttonColors(
          containerColor = Color.White,
          contentColor = BackgroundDark
        )
      ) {
        Row(
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
          Icon(
            imageVector = Icons.Default.AutoAwesome,
            contentDescription = null,
            tint = BrandPrimary,
            modifier = Modifier.size(18.dp)
          )
          Text(
            text = "Create Free Resume",
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold
          )
        }
      }
    }
  }
}

// -----------------------------------------------------------------------------
// 10. FOOTER SECTION
// -----------------------------------------------------------------------------
@Composable
fun FooterSection() {
  Column(
    modifier = Modifier
      .fillMaxWidth()
      .background(SurfaceDark)
      .padding(24.dp)
  ) {
    Row(
      modifier = Modifier.fillMaxWidth(),
      horizontalArrangement = Arrangement.SpaceBetween,
      verticalAlignment = Alignment.CenterVertically
    ) {
      Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
      ) {
        Box(
          modifier = Modifier
            .size(28.dp)
            .clip(RoundedCornerShape(6.dp))
            .background(BrandPrimary),
          contentAlignment = Alignment.Center
        ) {
          Icon(
            imageVector = Icons.Default.Description,
            contentDescription = null,
            tint = Color.White,
            modifier = Modifier.size(16.dp)
          )
        }
        Text(text = "Resume Studio", color = TextPrimary, fontSize = 14.sp, fontWeight = FontWeight.Bold)
      }

      // Operational status pill
      Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(6.dp)
      ) {
        Box(
          modifier = Modifier
            .size(8.dp)
            .clip(CircleShape)
            .background(BrandAccent)
        )
        Text("All Systems Operational", color = TextMuted, fontSize = 10.sp)
      }
    }

    Spacer(modifier = Modifier.height(16.dp))
    HorizontalDivider(color = SurfaceCardBorder)
    Spacer(modifier = Modifier.height(16.dp))

    Text(
      text = "Inspired by Stripe, Linear, Notion & Resume.io aesthetics.",
      color = TextMuted,
      fontSize = 11.sp,
      textAlign = TextAlign.Center,
      modifier = Modifier.fillMaxWidth()
    )

    Spacer(modifier = Modifier.height(4.dp))

    Text(
      text = "© 2026 Resume Studio Inc. All rights reserved.",
      color = TextMuted,
      fontSize = 10.sp,
      textAlign = TextAlign.Center,
      modifier = Modifier.fillMaxWidth()
    )
  }
}

// -----------------------------------------------------------------------------
// MODALS & INTERACTIVE DIALOGS
// -----------------------------------------------------------------------------
@Composable
fun QuickStartDialog(
  onDismiss: () -> Unit,
  onConfirm: () -> Unit
) {
  var selectedRole by remember { mutableStateOf("Software Engineer") }

  Dialog(onDismissRequest = onDismiss) {
    Surface(
      shape = RoundedCornerShape(20.dp),
      color = SurfaceCard,
      border = CardDefaults.outlinedCardBorder().copy(
        brush = Brush.linearGradient(listOf(BrandPrimary, SurfaceCardBorder))
      ),
      modifier = Modifier
        .fillMaxWidth()
        .padding(16.dp)
    ) {
      Column(modifier = Modifier.padding(24.dp)) {
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Icon(imageVector = Icons.Default.AutoAwesome, contentDescription = null, tint = BrandSecondary)
            Text(text = "AI Resume QuickStart", color = TextPrimary, fontSize = 18.sp, fontWeight = FontWeight.Bold)
          }

          IconButton(onClick = onDismiss) {
            Icon(imageVector = Icons.Default.Close, contentDescription = "Close", tint = TextMuted)
          }
        }

        Spacer(modifier = Modifier.height(12.dp))

        Text(
          text = "Select your target career track to preload tailored templates & Gemini bullet generators:",
          color = TextSecondary,
          fontSize = 12.sp
        )

        Spacer(modifier = Modifier.height(16.dp))

        val roles = listOf("Software Engineer", "Product Manager", "UI/UX Designer", "Data Scientist / AI")
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
          roles.forEach { role ->
            val isSelected = (selectedRole == role)
            Box(
              modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(10.dp))
                .background(if (isSelected) BrandPrimary.copy(alpha = 0.2f) else SurfaceDark)
                .border(
                  width = 1.dp,
                  color = if (isSelected) BrandPrimary else SurfaceCardBorder,
                  shape = RoundedCornerShape(10.dp)
                )
                .clickable { selectedRole = role }
                .padding(12.dp)
            ) {
              Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
              ) {
                Text(text = role, color = if (isSelected) TextPrimary else TextSecondary, fontSize = 13.sp, fontWeight = FontWeight.Medium)
                if (isSelected) {
                  Icon(imageVector = Icons.Default.CheckCircle, contentDescription = null, tint = BrandPrimary, modifier = Modifier.size(18.dp))
                }
              }
            }
          }
        }

        Spacer(modifier = Modifier.height(20.dp))

        Button(
          onClick = onConfirm,
          modifier = Modifier
            .fillMaxWidth()
            .height(48.dp)
            .testTag("dialog_confirm_launch_editor"),
          shape = RoundedCornerShape(10.dp),
          colors = ButtonDefaults.buttonColors(containerColor = BrandPrimary, contentColor = Color.White)
        ) {
          Text("Launch Editor with $selectedRole Profile", fontSize = 13.sp, fontWeight = FontWeight.Bold)
        }
      }
    }
  }
}

@Composable
fun TemplatePreviewDialog(
  template: TemplateItem,
  onDismiss: () -> Unit,
  onUseTemplate: () -> Unit
) {
  Dialog(onDismissRequest = onDismiss) {
    Surface(
      shape = RoundedCornerShape(20.dp),
      color = SurfaceCard,
      border = CardDefaults.outlinedCardBorder().copy(
        brush = Brush.linearGradient(listOf(template.accentColor, SurfaceCardBorder))
      ),
      modifier = Modifier
        .fillMaxWidth()
        .padding(16.dp)
    ) {
      Column(modifier = Modifier.padding(20.dp)) {
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Text(text = template.name, color = TextPrimary, fontSize = 18.sp, fontWeight = FontWeight.Bold)
          IconButton(onClick = onDismiss) {
            Icon(imageVector = Icons.Default.Close, contentDescription = "Close", tint = TextMuted)
          }
        }

        Spacer(modifier = Modifier.height(8.dp))

        Box(
          modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(SurfaceDark)
            .padding(16.dp)
        ) {
          Column {
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
              Box(modifier = Modifier.size(10.dp).clip(CircleShape).background(template.accentColor))
              Text("ATS Rating: ${template.atsScore}", color = BrandAccent, fontSize = 11.sp, fontWeight = FontWeight.Bold)
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(template.description, color = TextSecondary, fontSize = 12.sp)
          }
        }

        Spacer(modifier = Modifier.height(20.dp))

        Button(
          onClick = onUseTemplate,
          modifier = Modifier
            .fillMaxWidth()
            .height(48.dp)
            .testTag("dialog_use_template_${template.id}"),
          shape = RoundedCornerShape(10.dp),
          colors = ButtonDefaults.buttonColors(containerColor = template.accentColor, contentColor = Color.White)
        ) {
          Text("Apply ${template.name} Template", fontSize = 13.sp, fontWeight = FontWeight.Bold)
        }
      }
    }
  }
}

@Composable
fun PlanCheckoutDialog(
  plan: PricingTier,
  onDismiss: () -> Unit
) {
  Dialog(onDismissRequest = onDismiss) {
    Surface(
      shape = RoundedCornerShape(20.dp),
      color = SurfaceCard,
      border = CardDefaults.outlinedCardBorder().copy(
        brush = Brush.linearGradient(listOf(BrandPrimary, SurfaceCardBorder))
      ),
      modifier = Modifier
        .fillMaxWidth()
        .padding(16.dp)
    ) {
      Column(modifier = Modifier.padding(20.dp)) {
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
            Icon(imageVector = Icons.Default.Verified, contentDescription = null, tint = BrandSecondary)
            Text(text = "Selected: ${plan.name}", color = TextPrimary, fontSize = 16.sp, fontWeight = FontWeight.Bold)
          }
          IconButton(onClick = onDismiss) {
            Icon(imageVector = Icons.Default.Close, contentDescription = "Close", tint = TextMuted)
          }
        }

        Spacer(modifier = Modifier.height(12.dp))

        Text(
          text = "You are starting with the ${plan.name} plan. Includes access to Gemini AI bullet generator and all resume export formats.",
          color = TextSecondary,
          fontSize = 12.sp,
          lineHeight = 16.sp
        )

        Spacer(modifier = Modifier.height(20.dp))

        Button(
          onClick = onDismiss,
          modifier = Modifier
            .fillMaxWidth()
            .height(48.dp)
            .testTag("dialog_confirm_plan"),
          shape = RoundedCornerShape(10.dp),
          colors = ButtonDefaults.buttonColors(containerColor = BrandPrimary, contentColor = Color.White)
        ) {
          Text("Continue with ${plan.name}", fontSize = 13.sp, fontWeight = FontWeight.Bold)
        }
      }
    }
  }
}
