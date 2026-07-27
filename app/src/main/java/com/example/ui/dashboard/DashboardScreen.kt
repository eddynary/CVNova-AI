package com.example.ui.dashboard

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.Crossfade
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
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
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Analytics
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Dashboard
import androidx.compose.material.icons.filled.Dataset
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.RocketLaunch
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.NavigationRail
import androidx.compose.material3.NavigationRailItem
import androidx.compose.material3.NavigationRailItemDefaults
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
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.data.auth.UserSession
import com.example.data.dashboard.DashboardNavTab
import com.example.data.dashboard.DashboardUiState
import com.example.data.dashboard.DashboardViewModel
import com.example.data.dashboard.ResumeItem
import com.example.data.dashboard.UserSettingsState
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
import com.example.data.auth.SubscriptionPlan
import com.example.ui.billing.StripeBillingModal
import com.example.ui.database.FirestoreDatabaseInspectorModal
import com.example.ui.deployment.DeploymentInspectorModal
import com.example.ui.seo.SeoInspectorModal
import com.example.ui.theme.TextSecondary

@Composable
fun DashboardScreen(
  viewModel: DashboardViewModel,
  userSession: UserSession?,
  onLogout: () -> Unit = {},
  onNavigateHome: () -> Unit = {},
  onOpenResumeBuilder: () -> Unit = {},
  onOpenAdmin: () -> Unit = {},
  onUpdateSubscription: (SubscriptionPlan) -> Unit = {}
) {
  val uiState by viewModel.uiState.collectAsState()
  val resumes by viewModel.resumes.collectAsState()
  val stats by viewModel.stats.collectAsState()
  val settings by viewModel.settings.collectAsState()

  var showBillingModal by remember { mutableStateOf(false) }
  var showSeoModal by remember { mutableStateOf(false) }
  var showFirestoreModal by remember { mutableStateOf(false) }
  var showDeployModal by remember { mutableStateOf(false) }

  val configuration = LocalConfiguration.current
  val isWideScreen = configuration.screenWidthDp >= 768

  Box(
    modifier = Modifier
      .fillMaxSize()
      .background(BackgroundDark)
  ) {
    Row(modifier = Modifier.fillMaxSize()) {
      // 1. SIDEBAR NAVIGATION RAIL (ON TABLET/DESKTOP WIDE SCREENS)
      if (isWideScreen) {
        DashboardSidebarRail(
          activeTab = uiState.activeTab,
          onSelectTab = { viewModel.setTab(it) },
          userSession = userSession,
          onLogout = onLogout,
          onNavigateHome = onNavigateHome
        )
      }

      // 2. MAIN CONTENT AREA
      Column(
        modifier = Modifier
          .weight(1f)
          .fillMaxHeight()
      ) {
        // Top Dashboard Bar
        DashboardTopBar(
          activeTab = uiState.activeTab,
          userSession = userSession,
          onOpenCreateModal = { viewModel.openCreateModal() },
          onLogout = onLogout,
          onNavigateHome = onNavigateHome,
          onOpenAdmin = onOpenAdmin,
          onOpenBilling = { showBillingModal = true },
          onOpenSeo = { showSeoModal = true },
          onOpenFirestore = { showFirestoreModal = true },
          onOpenDeploy = { showDeployModal = true },
          showNavControls = !isWideScreen
        )

        // Tab Content Crossfade
        Box(
          modifier = Modifier
            .weight(1f)
            .fillMaxWidth()
        ) {
          Crossfade(
            targetState = uiState.activeTab,
            animationSpec = tween(250),
            label = "tab_crossfade"
          ) { tab ->
            when (tab) {
              DashboardNavTab.OVERVIEW -> OverviewTabContent(
                viewModel = viewModel,
                userSession = userSession,
                resumes = resumes,
                stats = stats
              )
              DashboardNavTab.MY_RESUMES -> MyResumesTabContent(
                viewModel = viewModel,
                resumes = resumes,
                searchQuery = uiState.searchQuery
              )
              DashboardNavTab.ANALYTICS -> AnalyticsTabContent(stats = stats)
              DashboardNavTab.SUBSCRIPTION -> SubscriptionTabContent(
                userSession = userSession,
                onOpenBilling = { showBillingModal = true }
              )
              DashboardNavTab.SETTINGS -> SettingsTabContent(
                currentSettings = settings,
                onSave = { viewModel.saveSettings(it) }
              )
            }
          }
        }

        // Bottom Nav Bar on Compact Screens
        if (!isWideScreen) {
          DashboardBottomBar(
            activeTab = uiState.activeTab,
            onSelectTab = { viewModel.setTab(it) }
          )
        }
      }
    }

    // CREATE RESUME MODAL DIALOG
    if (uiState.isCreateModalOpen) {
      CreateResumeDialog(
        viewModel = viewModel,
        uiState = uiState,
        onDismiss = { viewModel.closeCreateModal() },
        onCreated = {
          viewModel.closeCreateModal()
          onOpenResumeBuilder()
        }
      )
    }

    if (showBillingModal) {
      StripeBillingModal(
        currentPlan = userSession?.subscriptionPlan ?: SubscriptionPlan.FREE,
        onSelectPlan = { plan ->
          onUpdateSubscription(plan)
        },
        onDismiss = { showBillingModal = false }
      )
    }

    if (showSeoModal) {
      SeoInspectorModal(
        onDismiss = { showSeoModal = false }
      )
    }

    if (showFirestoreModal) {
      FirestoreDatabaseInspectorModal(
        onDismiss = { showFirestoreModal = false }
      )
    }

    if (showDeployModal) {
      DeploymentInspectorModal(
        onDismiss = { showDeployModal = false }
      )
    }
  }
}

// -----------------------------------------------------------------------------
// TOP BAR
// -----------------------------------------------------------------------------
@Composable
fun DashboardTopBar(
  activeTab: DashboardNavTab,
  userSession: UserSession?,
  onOpenCreateModal: () -> Unit,
  onLogout: () -> Unit,
  onNavigateHome: () -> Unit,
  onOpenAdmin: () -> Unit,
  onOpenBilling: () -> Unit,
  onOpenSeo: () -> Unit = {},
  onOpenFirestore: () -> Unit = {},
  onOpenDeploy: () -> Unit = {},
  showNavControls: Boolean
) {
  val plan = userSession?.subscriptionPlan ?: SubscriptionPlan.FREE

  Row(
    modifier = Modifier
      .fillMaxWidth()
      .statusBarsPadding()
      .background(SurfaceDark)
      .border(1.dp, SurfaceCardBorder, RoundedCornerShape(0.dp))
      .padding(horizontal = 20.dp, vertical = 12.dp),
    horizontalArrangement = Arrangement.SpaceBetween,
    verticalAlignment = Alignment.CenterVertically
  ) {
    Row(
      verticalAlignment = Alignment.CenterVertically,
      horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
      if (showNavControls) {
        IconButton(
          onClick = onNavigateHome,
          modifier = Modifier
            .size(36.dp)
            .clip(CircleShape)
            .background(SurfaceCard)
            .testTag("dashboard_top_home_button")
        ) {
          Icon(
            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
            contentDescription = "Home",
            tint = TextPrimary
          )
        }
      }

      Box(
        modifier = Modifier
          .size(34.dp)
          .clip(RoundedCornerShape(10.dp))
          .background(
            Brush.linearGradient(colors = listOf(BrandPrimary, BrandSecondary))
          ),
        contentAlignment = Alignment.Center
      ) {
        Icon(
          imageVector = Icons.Default.Description,
          contentDescription = null,
          tint = Color.White,
          modifier = Modifier.size(18.dp)
        )
      }

      Column {
        Text(
          text = when (activeTab) {
            DashboardNavTab.OVERVIEW -> "Workspace Overview"
            DashboardNavTab.MY_RESUMES -> "Resume Library"
            DashboardNavTab.ANALYTICS -> "ATS Analytics"
            DashboardNavTab.SUBSCRIPTION -> "Subscription & Plan"
            DashboardNavTab.SETTINGS -> "Account Settings"
          },
          color = TextPrimary,
          fontSize = 16.sp,
          fontWeight = FontWeight.Bold
        )
        Text(
          text = "Plan: ${plan.displayName}",
          color = BrandAccent,
          fontSize = 11.sp,
          fontWeight = FontWeight.SemiBold
        )
      }
    }

    Row(
      verticalAlignment = Alignment.CenterVertically,
      horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
      // SEO Engine Pill
      Box(
        modifier = Modifier
          .clip(RoundedCornerShape(8.dp))
          .background(SurfaceCard)
          .border(1.dp, SurfaceCardBorder, RoundedCornerShape(8.dp))
          .clickable { onOpenSeo() }
          .padding(horizontal = 10.dp, vertical = 6.dp)
          .testTag("topbar_seo_pill")
      ) {
        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
          Icon(
            imageVector = Icons.Default.Search,
            contentDescription = "SEO Audit",
            tint = BrandAccent,
            modifier = Modifier.size(14.dp)
          )
          Text(
            text = "SEO Engine",
            color = Color.White,
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold
          )
        }
      }

      // Firestore DB Pill
      Box(
        modifier = Modifier
          .clip(RoundedCornerShape(8.dp))
          .background(SurfaceCard)
          .border(1.dp, SurfaceCardBorder, RoundedCornerShape(8.dp))
          .clickable { onOpenFirestore() }
          .padding(horizontal = 10.dp, vertical = 6.dp)
          .testTag("topbar_firestore_pill")
      ) {
        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
          Icon(
            imageVector = Icons.Default.Dataset,
            contentDescription = "Firestore DB",
            tint = BrandPurple,
            modifier = Modifier.size(14.dp)
          )
          Text(
            text = "Firestore DB",
            color = Color.White,
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold
          )
        }
      }

      // Deploy & Hosting Pill
      Box(
        modifier = Modifier
          .clip(RoundedCornerShape(8.dp))
          .background(SurfaceCard)
          .border(1.dp, SurfaceCardBorder, RoundedCornerShape(8.dp))
          .clickable { onOpenDeploy() }
          .padding(horizontal = 10.dp, vertical = 6.dp)
          .testTag("topbar_deploy_pill")
      ) {
        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
          Icon(
            imageVector = Icons.Default.RocketLaunch,
            contentDescription = "Deploy App",
            tint = BrandAccent,
            modifier = Modifier.size(14.dp)
          )
          Text(
            text = "Deploy App",
            color = Color.White,
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold
          )
        }
      }

      // Stripe Plan Pill
      Box(
        modifier = Modifier
          .clip(RoundedCornerShape(8.dp))
          .background(if (plan == SubscriptionPlan.FREE) SurfaceCard else BrandPurple)
          .border(1.dp, SurfaceCardBorder, RoundedCornerShape(8.dp))
          .clickable { onOpenBilling() }
          .padding(horizontal = 10.dp, vertical = 6.dp)
          .testTag("topbar_subscription_pill")
      ) {
        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
          Icon(
            imageVector = Icons.Default.Star,
            contentDescription = null,
            tint = if (plan == SubscriptionPlan.FREE) TextMuted else BrandAmber,
            modifier = Modifier.size(14.dp)
          )
          Text(
            text = if (plan == SubscriptionPlan.FREE) "Upgrade Pro" else plan.displayName,
            color = Color.White,
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold
          )
        }
      }

      Button(
        onClick = onOpenCreateModal,
        modifier = Modifier
          .height(38.dp)
          .testTag("topbar_create_resume_button"),
        shape = RoundedCornerShape(10.dp),
        colors = ButtonDefaults.buttonColors(containerColor = BrandPrimary, contentColor = Color.White)
      ) {
        Row(
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
          Icon(imageVector = Icons.Default.Add, contentDescription = null, modifier = Modifier.size(16.dp))
          Text(text = "New Resume", fontSize = 12.sp, fontWeight = FontWeight.Bold)
        }
      }

      var showMenu by remember { mutableStateOf(false) }
      Box {
        Box(
          modifier = Modifier
            .size(38.dp)
            .clip(CircleShape)
            .background(
              Brush.linearGradient(colors = listOf(BrandPurple, BrandPrimary))
            )
            .clickable { showMenu = true }
            .testTag("topbar_profile_avatar"),
          contentAlignment = Alignment.Center
        ) {
          Text(
            text = (userSession?.displayName?.firstOrNull() ?: 'A').toString(),
            color = Color.White,
            fontWeight = FontWeight.Bold,
            fontSize = 14.sp
          )
        }

        DropdownMenu(
          expanded = showMenu,
          onDismissRequest = { showMenu = false },
          modifier = Modifier.background(SurfaceCard)
        ) {
          DropdownMenuItem(
            text = { Text("Home Page", color = TextPrimary) },
            onClick = {
              showMenu = false
              onNavigateHome()
            },
            leadingIcon = { Icon(Icons.Default.Dashboard, contentDescription = null, tint = TextMuted) }
          )
          DropdownMenuItem(
            text = { Text("Admin Dashboard", color = BrandPurple) },
            onClick = {
              showMenu = false
              onOpenAdmin()
            },
            leadingIcon = { Icon(Icons.Default.Security, contentDescription = null, tint = BrandPurple) }
          )
          DropdownMenuItem(
            text = { Text("Sign Out", color = Color(0xFFEF4444)) },
            onClick = {
              showMenu = false
              onLogout()
            },
            leadingIcon = { Icon(Icons.AutoMirrored.Filled.Logout, contentDescription = null, tint = Color(0xFFEF4444)) }
          )
        }
      }
    }
  }
}

// -----------------------------------------------------------------------------
// OVERVIEW TAB CONTENT
// -----------------------------------------------------------------------------
@Composable
fun OverviewTabContent(
  viewModel: DashboardViewModel,
  userSession: UserSession?,
  resumes: List<ResumeItem>,
  stats: com.example.data.dashboard.DashboardStats
) {
  val scrollState = rememberScrollState()

  Column(
    modifier = Modifier
      .fillMaxSize()
      .verticalScroll(scrollState)
      .padding(20.dp),
    verticalArrangement = Arrangement.spacedBy(20.dp)
  ) {
    // WELCOME HERO CARD
    WelcomeHeroCard(
      userSession = userSession,
      stats = stats,
      onOpenCreate = { viewModel.openCreateModal() }
    )

    // METRIC STATS GRID
    DashboardStatsGrid(stats = stats)

    // RECENT RESUMES SECTION
    Row(
      modifier = Modifier.fillMaxWidth(),
      horizontalArrangement = Arrangement.SpaceBetween,
      verticalAlignment = Alignment.CenterVertically
    ) {
      Column {
        Text(text = "Recent Resumes", color = TextPrimary, fontSize = 18.sp, fontWeight = FontWeight.Bold)
        Text(text = "Manage and optimize your tailored applications", color = TextSecondary, fontSize = 12.sp)
      }

      TextButton(
        onClick = { viewModel.setTab(DashboardNavTab.MY_RESUMES) },
        modifier = Modifier.testTag("overview_view_all_resumes_button")
      ) {
        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
          Text(text = "View All (${resumes.size})", color = BrandSecondary, fontSize = 13.sp, fontWeight = FontWeight.Bold)
          Icon(imageVector = Icons.AutoMirrored.Filled.ArrowForward, contentDescription = null, tint = BrandSecondary, modifier = Modifier.size(14.dp))
        }
      }
    }

    ResumesGrid(
      resumes = resumes.take(4),
      onFavoriteToggle = { viewModel.toggleFavorite(it) },
      onDuplicate = { viewModel.duplicateResume(it) },
      onDelete = { viewModel.deleteResume(it) }
    )

    // PROFILE COMPLETION & SUBSCRIPTION SUMMARY ROW
    Row(
      modifier = Modifier.fillMaxWidth(),
      horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
      ProfileCompletionCard(
        stats = stats,
        modifier = Modifier.weight(1f)
      )

      SubscriptionStatusCard(
        modifier = Modifier.weight(1f),
        onUpgrade = { viewModel.setTab(DashboardNavTab.SUBSCRIPTION) }
      )
    }

    Spacer(modifier = Modifier.height(20.dp))
  }
}

// -----------------------------------------------------------------------------
// WELCOME HERO CARD
// -----------------------------------------------------------------------------
@Composable
fun WelcomeHeroCard(
  userSession: UserSession?,
  stats: com.example.data.dashboard.DashboardStats,
  onOpenCreate: () -> Unit
) {
  Card(
    modifier = Modifier.fillMaxWidth(),
    shape = RoundedCornerShape(20.dp),
    colors = CardDefaults.cardColors(containerColor = SurfaceCard),
    border = CardDefaults.outlinedCardBorder().copy(
      brush = Brush.horizontalGradient(listOf(BrandPrimary, BrandSecondary.copy(alpha = 0.5f)))
    )
  ) {
    Box(
      modifier = Modifier
        .fillMaxWidth()
        .background(
          Brush.horizontalGradient(
            colors = listOf(BrandPrimary.copy(alpha = 0.25f), Color.Transparent)
          )
        )
        .padding(24.dp)
    ) {
      Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
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
                .clip(RoundedCornerShape(20.dp))
                .background(BrandAccent.copy(alpha = 0.2f))
                .border(1.dp, BrandAccent, RoundedCornerShape(20.dp))
                .padding(horizontal = 10.dp, vertical = 4.dp)
            ) {
              Text(
                text = "Pro Pass Active",
                color = BrandAccent,
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold
              )
            }

            Box(
              modifier = Modifier
                .clip(RoundedCornerShape(20.dp))
                .background(BrandAmber.copy(alpha = 0.2f))
                .border(1.dp, BrandAmber, RoundedCornerShape(20.dp))
                .padding(horizontal = 10.dp, vertical = 4.dp)
            ) {
              Text(
                text = "${stats.aiCreditsRemaining} AI Credits",
                color = BrandAmber,
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold
              )
            }
          }

          Text(
            text = "Top 2% ATS Score",
            color = TextMuted,
            fontSize = 12.sp,
            fontWeight = FontWeight.Medium
          )
        }

        Text(
          text = "Welcome back, ${userSession?.displayName ?: "Alexander"}! 👋",
          color = TextPrimary,
          fontSize = 22.sp,
          fontWeight = FontWeight.Bold
        )

        Text(
          text = "Your tailored resumes generated 18 recruiter views this week. You have 85 AI bullet points left for instant ATS enhancement.",
          color = TextSecondary,
          fontSize = 13.sp,
          lineHeight = 18.sp,
          modifier = Modifier.widthIn(max = 600.dp)
        )

        Row(
          horizontalArrangement = Arrangement.spacedBy(12.dp),
          modifier = Modifier.padding(top = 4.dp)
        ) {
          Button(
            onClick = onOpenCreate,
            modifier = Modifier.testTag("hero_create_resume_button"),
            shape = RoundedCornerShape(10.dp),
            colors = ButtonDefaults.buttonColors(containerColor = BrandPrimary, contentColor = Color.White)
          ) {
            Row(
              verticalAlignment = Alignment.CenterVertically,
              horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
              Icon(imageVector = Icons.Default.Add, contentDescription = null, modifier = Modifier.size(16.dp))
              Text(text = "Create Tailored Resume", fontSize = 13.sp, fontWeight = FontWeight.Bold)
            }
          }
        }
      }
    }
  }
}

// -----------------------------------------------------------------------------
// METRIC STATS GRID
// -----------------------------------------------------------------------------
@Composable
fun DashboardStatsGrid(stats: com.example.data.dashboard.DashboardStats) {
  Row(
    modifier = Modifier.fillMaxWidth(),
    horizontalArrangement = Arrangement.spacedBy(12.dp)
  ) {
    StatItemCard(
      title = "Total Resumes",
      value = "${stats.totalResumes}",
      subtitle = "4 tailored variants",
      icon = Icons.Default.Description,
      accentColor = BrandPrimary,
      modifier = Modifier.weight(1f)
    )

    StatItemCard(
      title = "Avg ATS Match",
      value = "${stats.avgAtsScore}%",
      subtitle = "+4% vs industry",
      icon = Icons.Default.CheckCircle,
      accentColor = BrandAccent,
      modifier = Modifier.weight(1f)
    )

    StatItemCard(
      title = "Recruiter Views",
      value = "${stats.totalViews}",
      subtitle = "18 this week",
      icon = Icons.Default.Visibility,
      accentColor = BrandSecondary,
      modifier = Modifier.weight(1f)
    )

    StatItemCard(
      title = "Applications Sent",
      value = "${stats.applicationsSent}",
      subtitle = "12 interviews",
      icon = Icons.Default.TrendingUp,
      accentColor = BrandPurple,
      modifier = Modifier.weight(1f)
    )
  }
}

@Composable
fun StatItemCard(
  title: String,
  value: String,
  subtitle: String,
  icon: androidx.compose.ui.graphics.vector.ImageVector,
  accentColor: Color,
  modifier: Modifier = Modifier
) {
  Card(
    modifier = modifier,
    shape = RoundedCornerShape(16.dp),
    colors = CardDefaults.cardColors(containerColor = SurfaceCard),
    border = CardDefaults.outlinedCardBorder().copy(
      brush = Brush.verticalGradient(listOf(accentColor.copy(alpha = 0.3f), SurfaceCardBorder))
    )
  ) {
    Column(
      modifier = Modifier.padding(16.dp),
      verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Text(text = title, color = TextMuted, fontSize = 11.sp, fontWeight = FontWeight.Medium)
        Box(
          modifier = Modifier
            .size(28.dp)
            .clip(CircleShape)
            .background(accentColor.copy(alpha = 0.15f)),
          contentAlignment = Alignment.Center
        ) {
          Icon(imageVector = icon, contentDescription = null, tint = accentColor, modifier = Modifier.size(14.dp))
        }
      }

      Text(text = value, color = TextPrimary, fontSize = 22.sp, fontWeight = FontWeight.Bold)
      Text(text = subtitle, color = accentColor, fontSize = 11.sp, fontWeight = FontWeight.SemiBold)
    }
  }
}

// -----------------------------------------------------------------------------
// RESUMES GRID
// -----------------------------------------------------------------------------
@Composable
fun ResumesGrid(
  resumes: List<ResumeItem>,
  onFavoriteToggle: (String) -> Unit,
  onDuplicate: (String) -> Unit,
  onDelete: (String) -> Unit
) {
  Column(
    verticalArrangement = Arrangement.spacedBy(12.dp)
  ) {
    resumes.chunked(2).forEach { rowItems ->
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
      ) {
        rowItems.forEach { resume ->
          ResumeCardItem(
            resume = resume,
            onFavoriteToggle = { onFavoriteToggle(resume.id) },
            onDuplicate = { onDuplicate(resume.id) },
            onDelete = { onDelete(resume.id) },
            modifier = Modifier.weight(1f)
          )
        }
        if (rowItems.size == 1) {
          Spacer(modifier = Modifier.weight(1f))
        }
      }
    }
  }
}

@Composable
fun ResumeCardItem(
  resume: ResumeItem,
  onFavoriteToggle: () -> Unit,
  onDuplicate: () -> Unit,
  onDelete: () -> Unit,
  modifier: Modifier = Modifier
) {
  var showMenu by remember { mutableStateOf(false) }

  Card(
    modifier = modifier,
    shape = RoundedCornerShape(16.dp),
    colors = CardDefaults.cardColors(containerColor = SurfaceCard),
    border = CardDefaults.outlinedCardBorder().copy(
      brush = Brush.verticalGradient(
        listOf(Color(resume.accentColorHex).copy(alpha = 0.4f), SurfaceCardBorder)
      )
    )
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
        Box(
          modifier = Modifier
            .clip(RoundedCornerShape(6.dp))
            .background(Color(resume.accentColorHex).copy(alpha = 0.15f))
            .border(1.dp, Color(resume.accentColorHex).copy(alpha = 0.4f), RoundedCornerShape(6.dp))
            .padding(horizontal = 8.dp, vertical = 3.dp)
        ) {
          Text(text = resume.templateName, color = Color(resume.accentColorHex), fontSize = 10.sp, fontWeight = FontWeight.Bold)
        }

        Row(verticalAlignment = Alignment.CenterVertically) {
          IconButton(onClick = onFavoriteToggle, modifier = Modifier.size(28.dp)) {
            Icon(
              imageVector = if (resume.isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
              contentDescription = "Favorite",
              tint = if (resume.isFavorite) Color(0xFFEF4444) else TextMuted,
              modifier = Modifier.size(16.dp)
            )
          }

          Box {
            IconButton(onClick = { showMenu = true }, modifier = Modifier.size(28.dp)) {
              Icon(imageVector = Icons.Default.MoreVert, contentDescription = "Menu", tint = TextMuted, modifier = Modifier.size(16.dp))
            }

            DropdownMenu(
              expanded = showMenu,
              onDismissRequest = { showMenu = false },
              modifier = Modifier.background(SurfaceCard)
            ) {
              DropdownMenuItem(
                text = { Text("Duplicate", color = TextPrimary) },
                onClick = {
                  showMenu = false
                  onDuplicate()
                },
                leadingIcon = { Icon(Icons.Default.ContentCopy, contentDescription = null, tint = TextMuted) }
              )
              DropdownMenuItem(
                text = { Text("Delete", color = Color(0xFFEF4444)) },
                onClick = {
                  showMenu = false
                  onDelete()
                },
                leadingIcon = { Icon(Icons.Default.Delete, contentDescription = null, tint = Color(0xFFEF4444)) }
              )
            }
          }
        }
      }

      Text(
        text = resume.title,
        color = TextPrimary,
        fontSize = 15.sp,
        fontWeight = FontWeight.Bold,
        maxLines = 1,
        overflow = TextOverflow.Ellipsis
      )

      Text(
        text = "Target: ${resume.targetRole}",
        color = TextSecondary,
        fontSize = 12.sp,
        maxLines = 1,
        overflow = TextOverflow.Ellipsis
      )

      HorizontalDivider(color = SurfaceCardBorder, thickness = 1.dp)

      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Row(
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
          Icon(imageVector = Icons.Default.CheckCircle, contentDescription = null, tint = BrandAccent, modifier = Modifier.size(14.dp))
          Text(text = "ATS Score: ${resume.atsScore}%", color = BrandAccent, fontSize = 11.sp, fontWeight = FontWeight.Bold)
        }

        Text(text = resume.lastModified, color = TextMuted, fontSize = 11.sp)
      }
    }
  }
}

// -----------------------------------------------------------------------------
// PROFILE COMPLETION CARD
// -----------------------------------------------------------------------------
@Composable
fun ProfileCompletionCard(
  stats: com.example.data.dashboard.DashboardStats,
  modifier: Modifier = Modifier
) {
  Card(
    modifier = modifier,
    shape = RoundedCornerShape(16.dp),
    colors = CardDefaults.cardColors(containerColor = SurfaceCard),
    border = CardDefaults.outlinedCardBorder().copy(
      brush = Brush.verticalGradient(listOf(BrandPrimary.copy(alpha = 0.3f), SurfaceCardBorder))
    )
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
        Text(text = "Profile Strength", color = TextPrimary, fontSize = 14.sp, fontWeight = FontWeight.Bold)
        Text(text = "${stats.profileCompletionPercentage}%", color = BrandPrimary, fontSize = 14.sp, fontWeight = FontWeight.Bold)
      }

      LinearProgressIndicator(
        progress = { stats.profileCompletionPercentage / 100f },
        modifier = Modifier
          .fillMaxWidth()
          .height(6.dp)
          .clip(RoundedCornerShape(3.dp)),
        color = BrandPrimary,
        trackColor = SurfaceDark
      )

      Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
        ChecklistItem(text = "Work History & Impact Metrics", isDone = true)
        ChecklistItem(text = "Education & Tech Certifications", isDone = true)
        ChecklistItem(text = "LinkedIn & GitHub Integration", isDone = false)
      }
    }
  }
}

@Composable
fun ChecklistItem(text: String, isDone: Boolean) {
  Row(
    verticalAlignment = Alignment.CenterVertically,
    horizontalArrangement = Arrangement.spacedBy(8.dp)
  ) {
    Icon(
      imageVector = if (isDone) Icons.Default.CheckCircle else Icons.Default.Check,
      contentDescription = null,
      tint = if (isDone) BrandAccent else TextMuted,
      modifier = Modifier.size(14.dp)
    )
    Text(
      text = text,
      color = if (isDone) TextSecondary else TextMuted,
      fontSize = 11.sp
    )
  }
}

// -----------------------------------------------------------------------------
// SUBSCRIPTION STATUS CARD
// -----------------------------------------------------------------------------
@Composable
fun SubscriptionStatusCard(
  modifier: Modifier = Modifier,
  onUpgrade: () -> Unit
) {
  Card(
    modifier = modifier,
    shape = RoundedCornerShape(16.dp),
    colors = CardDefaults.cardColors(containerColor = SurfaceCard),
    border = CardDefaults.outlinedCardBorder().copy(
      brush = Brush.verticalGradient(listOf(BrandSecondary.copy(alpha = 0.3f), SurfaceCardBorder))
    )
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
        Text(text = "Subscription Plan", color = TextPrimary, fontSize = 14.sp, fontWeight = FontWeight.Bold)
        Box(
          modifier = Modifier
            .clip(RoundedCornerShape(6.dp))
            .background(BrandSecondary.copy(alpha = 0.2f))
            .padding(horizontal = 8.dp, vertical = 2.dp)
        ) {
          Text(text = "PRO PASS", color = BrandSecondary, fontSize = 10.sp, fontWeight = FontWeight.Bold)
        }
      }

      Text(
        text = "Renews automatically on Aug 27, 2026. Includes unlimited PDF/JSON exports & Gemini AI tailoring.",
        color = TextSecondary,
        fontSize = 11.sp,
        lineHeight = 15.sp
      )

      OutlinedButton(
        onClick = onUpgrade,
        modifier = Modifier
          .fillMaxWidth()
          .height(36.dp)
          .testTag("sub_manage_plan_button"),
        shape = RoundedCornerShape(8.dp),
        colors = ButtonDefaults.outlinedButtonColors(contentColor = BrandSecondary)
      ) {
        Text(text = "Manage Plan & Billing", fontSize = 11.sp, fontWeight = FontWeight.Bold)
      }
    }
  }
}

// -----------------------------------------------------------------------------
// MY RESUMES TAB
// -----------------------------------------------------------------------------
@Composable
fun MyResumesTabContent(
  viewModel: DashboardViewModel,
  resumes: List<ResumeItem>,
  searchQuery: String
) {
  val scrollState = rememberScrollState()

  Column(
    modifier = Modifier
      .fillMaxSize()
      .verticalScroll(scrollState)
      .padding(20.dp),
    verticalArrangement = Arrangement.spacedBy(16.dp)
  ) {
    // Search & Filter Header
    Row(
      modifier = Modifier.fillMaxWidth(),
      horizontalArrangement = Arrangement.SpaceBetween,
      verticalAlignment = Alignment.CenterVertically
    ) {
      OutlinedTextField(
        value = searchQuery,
        onValueChange = { viewModel.setSearchQuery(it) },
        modifier = Modifier
          .weight(1f)
          .testTag("resumes_search_input"),
        placeholder = { Text("Search by title or target role...", color = TextMuted, fontSize = 13.sp) },
        leadingIcon = { Icon(imageVector = Icons.Default.Search, contentDescription = null, tint = TextMuted) },
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

      Spacer(modifier = Modifier.width(12.dp))

      Button(
        onClick = { viewModel.openCreateModal() },
        modifier = Modifier
          .height(48.dp)
          .testTag("resumes_tab_create_button"),
        shape = RoundedCornerShape(12.dp),
        colors = ButtonDefaults.buttonColors(containerColor = BrandPrimary, contentColor = Color.White)
      ) {
        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
          Icon(imageVector = Icons.Default.Add, contentDescription = null, modifier = Modifier.size(16.dp))
          Text(text = "Create Resume", fontSize = 13.sp, fontWeight = FontWeight.Bold)
        }
      }
    }

    val filtered = resumes.filter {
      it.title.contains(searchQuery, ignoreCase = true) || it.targetRole.contains(searchQuery, ignoreCase = true)
    }

    ResumesGrid(
      resumes = filtered,
      onFavoriteToggle = { viewModel.toggleFavorite(it) },
      onDuplicate = { viewModel.duplicateResume(it) },
      onDelete = { viewModel.deleteResume(it) }
    )
  }
}

// -----------------------------------------------------------------------------
// ANALYTICS TAB CONTENT
// -----------------------------------------------------------------------------
@Composable
fun AnalyticsTabContent(stats: com.example.data.dashboard.DashboardStats) {
  val scrollState = rememberScrollState()

  Column(
    modifier = Modifier
      .fillMaxSize()
      .verticalScroll(scrollState)
      .padding(20.dp),
    verticalArrangement = Arrangement.spacedBy(16.dp)
  ) {
    Card(
      modifier = Modifier.fillMaxWidth(),
      shape = RoundedCornerShape(20.dp),
      colors = CardDefaults.cardColors(containerColor = SurfaceCard),
      border = CardDefaults.outlinedCardBorder().copy(
        brush = Brush.verticalGradient(listOf(BrandSecondary, SurfaceCardBorder))
      )
    ) {
      Column(
        modifier = Modifier.padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
      ) {
        Row(
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
          Icon(imageVector = Icons.Default.Analytics, contentDescription = null, tint = BrandSecondary)
          Text(text = "Live Recruiter Analytics", color = TextPrimary, fontSize = 18.sp, fontWeight = FontWeight.Bold)
        }

        Text(
          text = "Your Stanford Classic resume template generated the highest conversion rate (14 recruiter views per application).",
          color = TextSecondary,
          fontSize = 13.sp
        )

        HorizontalDivider(color = SurfaceCardBorder)

        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween
        ) {
          Column {
            Text(text = "Total PDF Downloads", color = TextMuted, fontSize = 11.sp)
            Text(text = "45 Downloads", color = TextPrimary, fontSize = 16.sp, fontWeight = FontWeight.Bold)
          }

          Column {
            Text(text = "ATS Parse Rate", color = TextMuted, fontSize = 11.sp)
            Text(text = "99.4%", color = BrandAccent, fontSize = 16.sp, fontWeight = FontWeight.Bold)
          }

          Column {
            Text(text = "Interview Callback Ratio", color = TextMuted, fontSize = 11.sp)
            Text(text = "1 out of 2.1", color = BrandSecondary, fontSize = 16.sp, fontWeight = FontWeight.Bold)
          }
        }
      }
    }
  }
}

// -----------------------------------------------------------------------------
// SUBSCRIPTION TAB CONTENT
// -----------------------------------------------------------------------------
@Composable
fun SubscriptionTabContent(
  userSession: UserSession?,
  onOpenBilling: () -> Unit
) {
  val currentPlan = userSession?.subscriptionPlan ?: SubscriptionPlan.FREE
  val scrollState = rememberScrollState()

  Column(
    modifier = Modifier
      .fillMaxSize()
      .verticalScroll(scrollState)
      .padding(20.dp),
    verticalArrangement = Arrangement.spacedBy(16.dp)
  ) {
    Card(
      modifier = Modifier.fillMaxWidth(),
      shape = RoundedCornerShape(20.dp),
      colors = CardDefaults.cardColors(containerColor = SurfaceCard),
      border = CardDefaults.outlinedCardBorder().copy(
        brush = Brush.verticalGradient(listOf(BrandPrimary, SurfaceCardBorder))
      )
    ) {
      Column(
        modifier = Modifier.padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
      ) {
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Column {
            Text(text = "Current Plan: ${currentPlan.displayName}", color = TextPrimary, fontSize = 20.sp, fontWeight = FontWeight.Bold)
            Text(
              text = if (currentPlan == SubscriptionPlan.FREE) "Upgrade to Pro or Premium to unlock AI Auto-Writer, Executive templates, DOCX export, and unlimited resumes." else "You have full access to AI resume builder features, ATS score optimizer, and premium templates.",
              color = TextSecondary,
              fontSize = 12.sp,
              modifier = Modifier.padding(top = 4.dp)
            )
          }
        }

        Row(
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
          Box(
            modifier = Modifier
              .clip(RoundedCornerShape(8.dp))
              .background(BrandAccent.copy(alpha = 0.2f))
              .padding(horizontal = 10.dp, vertical = 4.dp)
          ) {
            Text(
              text = if (currentPlan == SubscriptionPlan.FREE) "STARTER PLAN" else "ACTIVE SUBSCRIBER",
              color = BrandAccent,
              fontSize = 11.sp,
              fontWeight = FontWeight.Bold
            )
          }

          Text(
            text = if (currentPlan == SubscriptionPlan.FREE) "\$0 / forever" else if (currentPlan == SubscriptionPlan.PRO) "\$19.99 / mo" else "\$39.99 / mo",
            color = TextPrimary,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold
          )
        }

        Button(
          onClick = onOpenBilling,
          modifier = Modifier
            .fillMaxWidth()
            .height(44.dp)
            .testTag("open_stripe_checkout_button"),
          shape = RoundedCornerShape(12.dp),
          colors = ButtonDefaults.buttonColors(containerColor = BrandPrimary)
        ) {
          Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Icon(imageVector = Icons.Default.Star, contentDescription = null, modifier = Modifier.size(18.dp))
            Text(
              text = if (currentPlan == SubscriptionPlan.FREE) "Upgrade with Stripe Payments" else "Manage / Change Subscription Plan",
              fontSize = 13.sp,
              fontWeight = FontWeight.Bold
            )
          }
        }
      }
    }

    // FEATURES COMPARISON TABLE
    Text(text = "Plan Features Comparison", color = TextPrimary, fontSize = 16.sp, fontWeight = FontWeight.Bold)

    Card(
      modifier = Modifier.fillMaxWidth(),
      shape = RoundedCornerShape(16.dp),
      colors = CardDefaults.cardColors(containerColor = SurfaceCard)
    ) {
      Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        PlanFeatureRow("Max Resume Documents", "1 Document", "10 Documents", "Unlimited")
        HorizontalDivider(color = SurfaceCardBorder)
        PlanFeatureRow("ATS Templates Access", "Basic Only", "All Premium", "All Premium + Custom")
        HorizontalDivider(color = SurfaceCardBorder)
        PlanFeatureRow("OpenAI Auto-Writer", "Disabled", "Included", "Included + 1-on-1 Coach")
        HorizontalDivider(color = SurfaceCardBorder)
        PlanFeatureRow("DOCX & JSON Export", "Disabled", "Included", "Included")
        HorizontalDivider(color = SurfaceCardBorder)
        PlanFeatureRow("AI Cover Letter Generator", "Disabled", "Unlimited", "Unlimited Multi-Lang")
      }
    }
  }
}

@Composable
fun PlanFeatureRow(label: String, freeVal: String, proVal: String, premiumVal: String) {
  Row(
    modifier = Modifier.fillMaxWidth(),
    horizontalArrangement = Arrangement.SpaceBetween,
    verticalAlignment = Alignment.CenterVertically
  ) {
    Text(text = label, color = TextPrimary, fontSize = 12.sp, fontWeight = FontWeight.SemiBold, modifier = Modifier.weight(1.2f))
    Row(modifier = Modifier.weight(1.8f), horizontalArrangement = Arrangement.SpaceBetween) {
      Text(text = freeVal, color = TextMuted, fontSize = 11.sp, modifier = Modifier.weight(1f))
      Text(text = proVal, color = BrandPurple, fontSize = 11.sp, fontWeight = FontWeight.Bold, modifier = Modifier.weight(1f))
      Text(text = premiumVal, color = BrandAmber, fontSize = 11.sp, fontWeight = FontWeight.Bold, modifier = Modifier.weight(1f))
    }
  }
}

// -----------------------------------------------------------------------------
// SETTINGS TAB CONTENT
// -----------------------------------------------------------------------------
@Composable
fun SettingsTabContent(
  currentSettings: UserSettingsState,
  onSave: (UserSettingsState) -> Unit
) {
  var name by remember { mutableStateOf(currentSettings.fullName) }
  var email by remember { mutableStateOf(currentSettings.email) }
  var jobTitle by remember { mutableStateOf(currentSettings.jobTitle) }
  var phone by remember { mutableStateOf(currentSettings.phone) }
  var location by remember { mutableStateOf(currentSettings.location) }
  var portfolio by remember { mutableStateOf(currentSettings.portfolioUrl) }

  var notifications by remember { mutableStateOf(currentSettings.emailNotifications) }
  var autoEnhance by remember { mutableStateOf(currentSettings.aiAutoEnhance) }

  val scrollState = rememberScrollState()

  Column(
    modifier = Modifier
      .fillMaxSize()
      .verticalScroll(scrollState)
      .padding(20.dp),
    verticalArrangement = Arrangement.spacedBy(16.dp)
  ) {
    Card(
      modifier = Modifier.fillMaxWidth(),
      shape = RoundedCornerShape(20.dp),
      colors = CardDefaults.cardColors(containerColor = SurfaceCard),
      border = CardDefaults.outlinedCardBorder().copy(
        brush = Brush.verticalGradient(listOf(BrandPrimary, SurfaceCardBorder))
      )
    ) {
      Column(
        modifier = Modifier.padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
      ) {
        Text(text = "Personal & Profile Details", color = TextPrimary, fontSize = 18.sp, fontWeight = FontWeight.Bold)

        OutlinedTextField(
          value = name,
          onValueChange = { name = it },
          label = { Text("Full Name", color = TextMuted) },
          modifier = Modifier.fillMaxWidth(),
          shape = RoundedCornerShape(10.dp),
          colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = BrandPrimary, unfocusedBorderColor = SurfaceCardBorder, focusedTextColor = TextPrimary, unfocusedTextColor = TextPrimary)
        )

        OutlinedTextField(
          value = email,
          onValueChange = { email = it },
          label = { Text("Email Address", color = TextMuted) },
          modifier = Modifier.fillMaxWidth(),
          shape = RoundedCornerShape(10.dp),
          colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = BrandPrimary, unfocusedBorderColor = SurfaceCardBorder, focusedTextColor = TextPrimary, unfocusedTextColor = TextPrimary)
        )

        OutlinedTextField(
          value = jobTitle,
          onValueChange = { jobTitle = it },
          label = { Text("Current Job Title", color = TextMuted) },
          modifier = Modifier.fillMaxWidth(),
          shape = RoundedCornerShape(10.dp),
          colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = BrandPrimary, unfocusedBorderColor = SurfaceCardBorder, focusedTextColor = TextPrimary, unfocusedTextColor = TextPrimary)
        )

        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
          OutlinedTextField(
            value = phone,
            onValueChange = { phone = it },
            label = { Text("Phone", color = TextMuted) },
            modifier = Modifier.weight(1f),
            shape = RoundedCornerShape(10.dp),
            colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = BrandPrimary, unfocusedBorderColor = SurfaceCardBorder, focusedTextColor = TextPrimary, unfocusedTextColor = TextPrimary)
          )

          OutlinedTextField(
            value = location,
            onValueChange = { location = it },
            label = { Text("Location", color = TextMuted) },
            modifier = Modifier.weight(1f),
            shape = RoundedCornerShape(10.dp),
            colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = BrandPrimary, unfocusedBorderColor = SurfaceCardBorder, focusedTextColor = TextPrimary, unfocusedTextColor = TextPrimary)
          )
        }

        OutlinedTextField(
          value = portfolio,
          onValueChange = { portfolio = it },
          label = { Text("Portfolio Website / GitHub", color = TextMuted) },
          modifier = Modifier.fillMaxWidth(),
          shape = RoundedCornerShape(10.dp),
          colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = BrandPrimary, unfocusedBorderColor = SurfaceCardBorder, focusedTextColor = TextPrimary, unfocusedTextColor = TextPrimary)
        )

        Spacer(modifier = Modifier.height(10.dp))

        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Text(text = "Email Notifications", color = TextPrimary, fontSize = 14.sp)
          Switch(
            checked = notifications,
            onCheckedChange = { notifications = it },
            colors = SwitchDefaults.colors(checkedTrackColor = BrandPrimary)
          )
        }

        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Text(text = "AI Bullet Point Auto-Enhance", color = TextPrimary, fontSize = 14.sp)
          Switch(
            checked = autoEnhance,
            onCheckedChange = { autoEnhance = it },
            colors = SwitchDefaults.colors(checkedTrackColor = BrandPrimary)
          )
        }

        Spacer(modifier = Modifier.height(10.dp))

        Button(
          onClick = {
            onSave(
              UserSettingsState(
                fullName = name,
                email = email,
                jobTitle = jobTitle,
                phone = phone,
                location = location,
                portfolioUrl = portfolio,
                emailNotifications = notifications,
                aiAutoEnhance = autoEnhance
              )
            )
          },
          modifier = Modifier
            .fillMaxWidth()
            .height(48.dp)
            .testTag("settings_save_button"),
          shape = RoundedCornerShape(10.dp),
          colors = ButtonDefaults.buttonColors(containerColor = BrandPrimary, contentColor = Color.White)
        ) {
          Text(text = "Save Profile Settings", fontSize = 14.sp, fontWeight = FontWeight.Bold)
        }
      }
    }
  }
}

// -----------------------------------------------------------------------------
// SIDEBAR RAIL (WIDE SCREENS)
// -----------------------------------------------------------------------------
@Composable
fun DashboardSidebarRail(
  activeTab: DashboardNavTab,
  onSelectTab: (DashboardNavTab) -> Unit,
  userSession: UserSession?,
  onLogout: () -> Unit,
  onNavigateHome: () -> Unit
) {
  NavigationRail(
    modifier = Modifier
      .fillMaxHeight()
      .width(220.dp)
      .background(SurfaceDark)
      .border(1.dp, SurfaceCardBorder, RoundedCornerShape(0.dp)),
    containerColor = SurfaceDark
  ) {
    Column(
      modifier = Modifier
        .fillMaxSize()
        .padding(16.dp),
      verticalArrangement = Arrangement.SpaceBetween
    ) {
      Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
        // App Logo
        Row(
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.spacedBy(10.dp),
          modifier = Modifier
            .clickable { onNavigateHome() }
            .padding(vertical = 12.dp)
        ) {
          Box(
            modifier = Modifier
              .size(36.dp)
              .clip(RoundedCornerShape(10.dp))
              .background(
                Brush.linearGradient(colors = listOf(BrandPrimary, BrandSecondary))
              ),
            contentAlignment = Alignment.Center
          ) {
            Icon(imageVector = Icons.Default.Description, contentDescription = null, tint = Color.White, modifier = Modifier.size(20.dp))
          }

          Text(text = "Resume Studio", color = TextPrimary, fontSize = 16.sp, fontWeight = FontWeight.Bold)
        }

        HorizontalDivider(color = SurfaceCardBorder)

        // Navigation Menu Items
        SidebarNavItem(
          title = "Overview",
          icon = Icons.Default.Dashboard,
          isSelected = activeTab == DashboardNavTab.OVERVIEW,
          onClick = { onSelectTab(DashboardNavTab.OVERVIEW) },
          testTag = "sidebar_nav_overview"
        )

        SidebarNavItem(
          title = "My Resumes",
          icon = Icons.Default.Description,
          isSelected = activeTab == DashboardNavTab.MY_RESUMES,
          onClick = { onSelectTab(DashboardNavTab.MY_RESUMES) },
          testTag = "sidebar_nav_resumes"
        )

        SidebarNavItem(
          title = "ATS Analytics",
          icon = Icons.Default.Analytics,
          isSelected = activeTab == DashboardNavTab.ANALYTICS,
          onClick = { onSelectTab(DashboardNavTab.ANALYTICS) },
          testTag = "sidebar_nav_analytics"
        )

        SidebarNavItem(
          title = "Subscription",
          icon = Icons.Default.Star,
          isSelected = activeTab == DashboardNavTab.SUBSCRIPTION,
          onClick = { onSelectTab(DashboardNavTab.SUBSCRIPTION) },
          testTag = "sidebar_nav_subscription"
        )

        SidebarNavItem(
          title = "Settings",
          icon = Icons.Default.Settings,
          isSelected = activeTab == DashboardNavTab.SETTINGS,
          onClick = { onSelectTab(DashboardNavTab.SETTINGS) },
          testTag = "sidebar_nav_settings"
        )
      }

      Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        HorizontalDivider(color = SurfaceCardBorder)
        SidebarNavItem(
          title = "Sign Out",
          icon = Icons.AutoMirrored.Filled.Logout,
          isSelected = false,
          onClick = onLogout,
          testTag = "sidebar_nav_logout"
        )
      }
    }
  }
}

@Composable
fun SidebarNavItem(
  title: String,
  icon: androidx.compose.ui.graphics.vector.ImageVector,
  isSelected: Boolean,
  onClick: () -> Unit,
  testTag: String
) {
  val bg by animateColorAsState(
    targetValue = if (isSelected) BrandPrimary else Color.Transparent,
    animationSpec = tween(150),
    label = "navBg"
  )

  Row(
    modifier = Modifier
      .fillMaxWidth()
      .height(42.dp)
      .clip(RoundedCornerShape(10.dp))
      .background(bg)
      .clickable(onClick = onClick)
      .testTag(testTag)
      .padding(horizontal = 12.dp),
    verticalAlignment = Alignment.CenterVertically,
    horizontalArrangement = Arrangement.spacedBy(10.dp)
  ) {
    Icon(
      imageVector = icon,
      contentDescription = title,
      tint = if (isSelected) Color.White else TextMuted,
      modifier = Modifier.size(18.dp)
    )
    Text(
      text = title,
      color = if (isSelected) Color.White else TextSecondary,
      fontSize = 13.sp,
      fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
    )
  }
}

// -----------------------------------------------------------------------------
// BOTTOM BAR (MOBILE SCREENS)
// -----------------------------------------------------------------------------
@Composable
fun DashboardBottomBar(
  activeTab: DashboardNavTab,
  onSelectTab: (DashboardNavTab) -> Unit
) {
  NavigationBar(
    containerColor = SurfaceDark,
    contentColor = TextPrimary,
    modifier = Modifier
      .fillMaxWidth()
      .navigationBarsPadding()
      .border(1.dp, SurfaceCardBorder, RoundedCornerShape(0.dp))
  ) {
    NavigationBarItem(
      selected = activeTab == DashboardNavTab.OVERVIEW,
      onClick = { onSelectTab(DashboardNavTab.OVERVIEW) },
      icon = { Icon(Icons.Default.Dashboard, contentDescription = "Overview") },
      label = { Text("Overview", fontSize = 10.sp) },
      colors = NavigationBarItemDefaults.colors(selectedIconColor = BrandPrimary, indicatorColor = SurfaceCard)
    )

    NavigationBarItem(
      selected = activeTab == DashboardNavTab.MY_RESUMES,
      onClick = { onSelectTab(DashboardNavTab.MY_RESUMES) },
      icon = { Icon(Icons.Default.Description, contentDescription = "Resumes") },
      label = { Text("Resumes", fontSize = 10.sp) },
      colors = NavigationBarItemDefaults.colors(selectedIconColor = BrandPrimary, indicatorColor = SurfaceCard)
    )

    NavigationBarItem(
      selected = activeTab == DashboardNavTab.ANALYTICS,
      onClick = { onSelectTab(DashboardNavTab.ANALYTICS) },
      icon = { Icon(Icons.Default.Analytics, contentDescription = "Analytics") },
      label = { Text("Analytics", fontSize = 10.sp) },
      colors = NavigationBarItemDefaults.colors(selectedIconColor = BrandPrimary, indicatorColor = SurfaceCard)
    )

    NavigationBarItem(
      selected = activeTab == DashboardNavTab.SETTINGS,
      onClick = { onSelectTab(DashboardNavTab.SETTINGS) },
      icon = { Icon(Icons.Default.Settings, contentDescription = "Settings") },
      label = { Text("Settings", fontSize = 10.sp) },
      colors = NavigationBarItemDefaults.colors(selectedIconColor = BrandPrimary, indicatorColor = SurfaceCard)
    )
  }
}

// -----------------------------------------------------------------------------
// CREATE RESUME MODAL DIALOG
// -----------------------------------------------------------------------------
@Composable
fun CreateResumeDialog(
  viewModel: DashboardViewModel,
  uiState: DashboardUiState,
  onDismiss: () -> Unit,
  onCreated: () -> Unit = {}
) {
  val templates = listOf(
    "Executive Linear",
    "Notion Minimalist",
    "Stripe Gradient",
    "Stanford Classic",
    "Vercel Clean Tech"
  )

  Dialog(onDismissRequest = onDismiss) {
    Surface(
      shape = RoundedCornerShape(24.dp),
      color = SurfaceCard,
      modifier = Modifier.padding(16.dp),
      border = CardDefaults.outlinedCardBorder().copy(
        brush = Brush.verticalGradient(listOf(BrandPrimary, SurfaceCardBorder))
      )
    ) {
      Column(
        modifier = Modifier.padding(22.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
      ) {
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Text(text = "Create New Resume", color = TextPrimary, fontSize = 18.sp, fontWeight = FontWeight.Bold)
          IconButton(onClick = onDismiss) {
            Icon(imageVector = Icons.Default.Close, contentDescription = "Close", tint = TextMuted)
          }
        }

        OutlinedTextField(
          value = uiState.newResumeTitle,
          onValueChange = { viewModel.updateNewResumeTitle(it) },
          label = { Text("Resume Title", color = TextMuted) },
          placeholder = { Text("e.g. Senior Backend Engineer - Google 2026", color = TextMuted, fontSize = 13.sp) },
          modifier = Modifier
            .fillMaxWidth()
            .testTag("create_resume_input_title"),
          shape = RoundedCornerShape(10.dp),
          colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = BrandPrimary, unfocusedBorderColor = SurfaceCardBorder, focusedTextColor = TextPrimary, unfocusedTextColor = TextPrimary)
        )

        OutlinedTextField(
          value = uiState.newResumeTargetRole,
          onValueChange = { viewModel.updateNewResumeTargetRole(it) },
          label = { Text("Target Job Title", color = TextMuted) },
          placeholder = { Text("Staff Distributed Systems Engineer", color = TextMuted, fontSize = 13.sp) },
          modifier = Modifier
            .fillMaxWidth()
            .testTag("create_resume_input_role"),
          shape = RoundedCornerShape(10.dp),
          colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = BrandPrimary, unfocusedBorderColor = SurfaceCardBorder, focusedTextColor = TextPrimary, unfocusedTextColor = TextPrimary)
        )

        Text(text = "CHOOSE TEMPLATE", color = TextSecondary, fontSize = 10.sp, fontWeight = FontWeight.Bold)

        Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
          templates.forEach { tmpl ->
            val isSelected = uiState.selectedTemplate == tmpl
            Box(
              modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(10.dp))
                .background(if (isSelected) BrandPrimary.copy(alpha = 0.2f) else SurfaceDark)
                .border(
                  1.dp,
                  if (isSelected) BrandPrimary else SurfaceCardBorder,
                  RoundedCornerShape(10.dp)
                )
                .clickable { viewModel.selectTemplate(tmpl) }
                .padding(horizontal = 14.dp, vertical = 10.dp)
            ) {
              Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
              ) {
                Text(
                  text = tmpl,
                  color = if (isSelected) Color.White else TextPrimary,
                  fontSize = 13.sp,
                  fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
                )
                if (isSelected) {
                  Icon(imageVector = Icons.Default.CheckCircle, contentDescription = null, tint = BrandPrimary, modifier = Modifier.size(16.dp))
                }
              }
            }
          }
        }

        Spacer(modifier = Modifier.height(6.dp))

        Button(
          onClick = {
            viewModel.createResume()
            onCreated()
          },
          modifier = Modifier
            .fillMaxWidth()
            .height(48.dp)
            .testTag("create_resume_submit_button"),
          shape = RoundedCornerShape(12.dp),
          colors = ButtonDefaults.buttonColors(containerColor = BrandPrimary, contentColor = Color.White)
        ) {
          Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Icon(imageVector = Icons.Default.AutoAwesome, contentDescription = null, modifier = Modifier.size(16.dp))
            Text(text = "Generate Resume & Open Editor", fontSize = 14.sp, fontWeight = FontWeight.Bold)
          }
        }
      }
    }
  }
}
