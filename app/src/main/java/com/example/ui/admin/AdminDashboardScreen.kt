package com.example.ui.admin

import android.widget.Toast
import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.tween
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
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Help
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Analytics
import androidx.compose.material.icons.filled.Article
import androidx.compose.material.icons.filled.Assessment
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.MonetizationOn
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Style
import androidx.compose.material.icons.filled.SupportAgent
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.data.admin.AdminDashboardViewModel
import com.example.data.admin.AdminSystemSettings
import com.example.data.admin.AdminTab
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
fun AdminDashboardScreen(
  viewModel: AdminDashboardViewModel,
  onExitAdmin: () -> Unit
) {
  val uiState by viewModel.uiState.collectAsState()
  val context = LocalContext.current
  val configuration = LocalConfiguration.current
  val isWideScreen = configuration.screenWidthDp >= 768

  LaunchedEffect(uiState.toastMessage) {
    uiState.toastMessage?.let {
      Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
      viewModel.clearToast()
    }
  }

  Box(
    modifier = Modifier
      .fillMaxSize()
      .background(BackgroundDark)
  ) {
    Row(modifier = Modifier.fillMaxSize()) {
      // 1. LEFT SIDEBAR NAVIGATION RAIL
      AdminSidebarRail(
        activeTab = uiState.activeTab,
        onSelectTab = { viewModel.setTab(it) },
        onExitAdmin = onExitAdmin,
        isWideScreen = isWideScreen
      )

      // 2. MAIN ADMIN CONTENT CONTAINER
      Column(
        modifier = Modifier
          .weight(1f)
          .fillMaxHeight()
      ) {
        // TOP ADMIN HEADER BAR
        AdminTopHeader(
          activeTab = uiState.activeTab,
          searchQuery = uiState.searchQuery,
          onSearchChange = { viewModel.setSearchQuery(it) },
          onExitAdmin = onExitAdmin
        )

        HorizontalDivider(color = SurfaceCardBorder)

        // CONTENT CROSSFADE
        Box(
          modifier = Modifier
            .weight(1f)
            .fillMaxWidth()
        ) {
          Crossfade(
            targetState = uiState.activeTab,
            animationSpec = tween(250),
            label = "admin_tab_crossfade"
          ) { tab ->
            when (tab) {
              AdminTab.ANALYTICS -> AdminAnalyticsTab(uiState = uiState)
              AdminTab.USERS -> AdminUsersTab(uiState = uiState, viewModel = viewModel)
              AdminTab.RESUMES -> AdminResumesTab(uiState = uiState, viewModel = viewModel)
              AdminTab.TEMPLATES -> AdminTemplatesTab(uiState = uiState, viewModel = viewModel)
              AdminTab.SUBSCRIPTIONS -> AdminSubscriptionsTab(uiState = uiState)
              AdminTab.REPORTS -> AdminReportsTab(uiState = uiState)
              AdminTab.BLOG -> AdminBlogTab(uiState = uiState, viewModel = viewModel)
              AdminTab.SUPPORT -> AdminSupportTab(uiState = uiState, viewModel = viewModel)
              AdminTab.SETTINGS -> AdminSettingsTab(uiState = uiState, viewModel = viewModel)
            }
          }
        }
      }
    }
  }
}

// -----------------------------------------------------------------------------
// SIDEBAR RAIL
// -----------------------------------------------------------------------------
@Composable
fun AdminSidebarRail(
  activeTab: AdminTab,
  onSelectTab: (AdminTab) -> Unit,
  onExitAdmin: () -> Unit,
  isWideScreen: Boolean
) {
  val tabs = listOf(
    AdminTab.ANALYTICS to ("Analytics" to Icons.Default.Analytics),
    AdminTab.USERS to ("Users" to Icons.Default.People),
    AdminTab.RESUMES to ("Resumes" to Icons.Default.Description),
    AdminTab.TEMPLATES to ("Templates" to Icons.Default.Style),
    AdminTab.SUBSCRIPTIONS to ("Subscriptions" to Icons.Default.MonetizationOn),
    AdminTab.REPORTS to ("Reports" to Icons.Default.Assessment),
    AdminTab.BLOG to ("Blog CMS" to Icons.Default.Article),
    AdminTab.SUPPORT to ("Support" to Icons.Default.SupportAgent),
    AdminTab.SETTINGS to ("Settings" to Icons.Default.Settings)
  )

  Surface(
    color = SurfaceDark,
    modifier = Modifier
      .fillMaxHeight()
      .width(if (isWideScreen) 220.dp else 72.dp),
    border = CardDefaults.outlinedCardBorder().copy(brush = Brush.verticalGradient(listOf(SurfaceCardBorder, SurfaceCardBorder)))
  ) {
    Column(
      modifier = Modifier
        .fillMaxSize()
        .statusBarsPadding()
        .padding(vertical = 16.dp, horizontal = 10.dp),
      verticalArrangement = Arrangement.SpaceBetween
    ) {
      Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
        // SaaS Admin Logo Header
        Row(
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.spacedBy(10.dp),
          modifier = Modifier.padding(horizontal = 6.dp)
        ) {
          Box(
            modifier = Modifier
              .size(38.dp)
              .clip(RoundedCornerShape(10.dp))
              .background(Brush.linearGradient(listOf(BrandPurple, BrandPrimary))),
            contentAlignment = Alignment.Center
          ) {
            Icon(imageVector = Icons.Default.Security, contentDescription = null, tint = Color.White, modifier = Modifier.size(20.dp))
          }

          if (isWideScreen) {
            Column {
              Text(text = "SaaS Admin", color = TextPrimary, fontSize = 15.sp, fontWeight = FontWeight.Bold)
              Text(text = "Control Center v2.6", color = BrandAccent, fontSize = 10.sp, fontWeight = FontWeight.Bold)
            }
          }
        }

        HorizontalDivider(color = SurfaceCardBorder)

        // Navigation Items
        Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
          tabs.forEach { (tab, info) ->
            val isSelected = activeTab == tab
            val (label, icon) = info

            Box(
              modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(12.dp))
                .background(if (isSelected) BrandPurple.copy(alpha = 0.25f) else Color.Transparent)
                .clickable { onSelectTab(tab) }
                .padding(horizontal = 12.dp, vertical = 10.dp)
            ) {
              Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
              ) {
                Icon(
                  imageVector = icon,
                  contentDescription = label,
                  tint = if (isSelected) BrandPurple else TextMuted,
                  modifier = Modifier.size(18.dp)
                )
                if (isWideScreen) {
                  Text(
                    text = label,
                    color = if (isSelected) Color.White else TextSecondary,
                    fontSize = 13.sp,
                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
                  )
                }
              }
            }
          }
        }
      }

      // Exit Admin Button at bottom
      OutlinedButton(
        onClick = onExitAdmin,
        modifier = Modifier
          .fillMaxWidth()
          .testTag("admin_exit_button"),
        shape = RoundedCornerShape(10.dp),
        colors = ButtonDefaults.outlinedButtonColors(contentColor = TextMuted)
      ) {
        Row(
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
          Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = null, modifier = Modifier.size(14.dp))
          if (isWideScreen) {
            Text("Exit Admin", fontSize = 12.sp, fontWeight = FontWeight.Bold)
          }
        }
      }
    }
  }
}

// -----------------------------------------------------------------------------
// TOP HEADER
// -----------------------------------------------------------------------------
@Composable
fun AdminTopHeader(
  activeTab: AdminTab,
  searchQuery: String,
  onSearchChange: (String) -> Unit,
  onExitAdmin: () -> Unit
) {
  Row(
    modifier = Modifier
      .fillMaxWidth()
      .statusBarsPadding()
      .background(SurfaceDark)
      .padding(horizontal = 20.dp, vertical = 12.dp),
    horizontalArrangement = Arrangement.SpaceBetween,
    verticalAlignment = Alignment.CenterVertically
  ) {
    Column {
      Text(
        text = when (activeTab) {
          AdminTab.ANALYTICS -> "Platform Telemetry & Metrics"
          AdminTab.USERS -> "User Accounts & Permissions"
          AdminTab.RESUMES -> "Global Resume Management"
          AdminTab.TEMPLATES -> "Resume Template Catalog"
          AdminTab.SUBSCRIPTIONS -> "Subscriptions & MRR Billing"
          AdminTab.REPORTS -> "System Audit & Abuse Reports"
          AdminTab.BLOG -> "Career Advice Blog CMS"
          AdminTab.SUPPORT -> "Helpdesk Support Tickets"
          AdminTab.SETTINGS -> "Platform & Gateway Settings"
        },
        color = TextPrimary,
        fontSize = 17.sp,
        fontWeight = FontWeight.Bold
      )
      Text(text = "System Operational • 99.98% Uptime", color = BrandAccent, fontSize = 11.sp)
    }

    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(10.dp)) {
      OutlinedTextField(
        value = searchQuery,
        onValueChange = onSearchChange,
        placeholder = { Text("Filter table records...", color = TextMuted, fontSize = 12.sp) },
        leadingIcon = { Icon(imageVector = Icons.Default.Search, contentDescription = null, tint = TextMuted, modifier = Modifier.size(16.dp)) },
        singleLine = true,
        modifier = Modifier
          .width(220.dp)
          .height(42.dp),
        shape = RoundedCornerShape(10.dp),
        colors = OutlinedTextFieldDefaults.colors(
          focusedBorderColor = BrandPurple,
          unfocusedBorderColor = SurfaceCardBorder,
          focusedContainerColor = SurfaceCard,
          unfocusedContainerColor = SurfaceCard,
          focusedTextColor = TextPrimary,
          unfocusedTextColor = TextPrimary
        )
      )

      Box(
        modifier = Modifier
          .clip(RoundedCornerShape(8.dp))
          .background(BrandAmber.copy(alpha = 0.2f))
          .padding(horizontal = 10.dp, vertical = 6.dp)
      ) {
        Text(text = "SUPER ADMIN", color = BrandAmber, fontSize = 10.sp, fontWeight = FontWeight.Bold)
      }
    }
  }
}

// -----------------------------------------------------------------------------
// TAB 1: ANALYTICS TAB
// -----------------------------------------------------------------------------
@Composable
fun AdminAnalyticsTab(uiState: com.example.data.admin.AdminDashboardUiState) {
  val scrollState = rememberScrollState()

  Column(
    modifier = Modifier
      .fillMaxSize()
      .verticalScroll(scrollState)
      .padding(20.dp),
    verticalArrangement = Arrangement.spacedBy(16.dp)
  ) {
    // STATS METRIC CARDS
    Row(
      modifier = Modifier.fillMaxWidth(),
      horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
      AdminStatCard("Monthly Revenue", "$12,840.00", "+14.2% MRR", Icons.Default.MonetizationOn, BrandAccent, Modifier.weight(1f))
      AdminStatCard("Active Users", "${uiState.activeUsersCount}", "+88 this week", Icons.Default.People, BrandPrimary, Modifier.weight(1f))
      AdminStatCard("Total Resumes", "${uiState.totalResumesCount}", "+320 created", Icons.Default.Description, BrandPurple, Modifier.weight(1f))
      AdminStatCard("Server Health", "99.98%", "All APIs Healthy", Icons.Default.CheckCircle, BrandAccent, Modifier.weight(1f))
    }

    // SAAS PERFORMANCE SUMMARY
    Card(
      modifier = Modifier.fillMaxWidth(),
      shape = RoundedCornerShape(16.dp),
      colors = CardDefaults.cardColors(containerColor = SurfaceCard),
      border = CardDefaults.outlinedCardBorder()
    ) {
      Column(
        modifier = Modifier.padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
      ) {
        Text(text = "SaaS Platform Overview", color = TextPrimary, fontSize = 16.sp, fontWeight = FontWeight.Bold)
        Text(
          text = "Platform performance is operating smoothly across OpenAI Gemini integration, Stripe webhook processing, and PDF/DOCX exporting pipelines.",
          color = TextSecondary,
          fontSize = 13.sp
        )

        HorizontalDivider(color = SurfaceCardBorder)

        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween
        ) {
          Column {
            Text(text = "Monthly AI Calls", color = TextMuted, fontSize = 11.sp)
            Text(text = "142,500 requests", color = TextPrimary, fontSize = 15.sp, fontWeight = FontWeight.Bold)
          }

          Column {
            Text(text = "Conversion Rate", color = TextMuted, fontSize = 11.sp)
            Text(text = "6.8% Free -> Pro", color = BrandAccent, fontSize = 15.sp, fontWeight = FontWeight.Bold)
          }

          Column {
            Text(text = "Avg Generation Time", color = TextMuted, fontSize = 11.sp)
            Text(text = "1.2 seconds", color = BrandSecondary, fontSize = 15.sp, fontWeight = FontWeight.Bold)
          }
        }
      }
    }
  }
}

@Composable
fun AdminStatCard(
  title: String,
  value: String,
  subtitle: String,
  icon: androidx.compose.ui.graphics.vector.ImageVector,
  accentColor: Color,
  modifier: Modifier = Modifier
) {
  Card(
    modifier = modifier,
    shape = RoundedCornerShape(14.dp),
    colors = CardDefaults.cardColors(containerColor = SurfaceCard),
    border = CardDefaults.outlinedCardBorder().copy(brush = Brush.verticalGradient(listOf(accentColor.copy(alpha = 0.3f), SurfaceCardBorder)))
  ) {
    Column(
      modifier = Modifier.padding(16.dp),
      verticalArrangement = Arrangement.spacedBy(6.dp)
    ) {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Text(text = title, color = TextMuted, fontSize = 11.sp)
        Icon(imageVector = icon, contentDescription = null, tint = accentColor, modifier = Modifier.size(16.dp))
      }
      Text(text = value, color = TextPrimary, fontSize = 20.sp, fontWeight = FontWeight.Bold)
      Text(text = subtitle, color = accentColor, fontSize = 10.sp, fontWeight = FontWeight.Bold)
    }
  }
}

// -----------------------------------------------------------------------------
// TAB 2: USERS TAB
// -----------------------------------------------------------------------------
@Composable
fun AdminUsersTab(
  uiState: com.example.data.admin.AdminDashboardUiState,
  viewModel: AdminDashboardViewModel
) {
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
      shape = RoundedCornerShape(16.dp),
      colors = CardDefaults.cardColors(containerColor = SurfaceCard),
      border = CardDefaults.outlinedCardBorder()
    ) {
      Column(
        modifier = Modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
      ) {
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Text(text = "User Accounts (${uiState.usersList.size})", color = TextPrimary, fontSize = 16.sp, fontWeight = FontWeight.Bold)
        }

        HorizontalDivider(color = SurfaceCardBorder)

        uiState.usersList.forEach { user ->
          Row(
            modifier = Modifier
              .fillMaxWidth()
              .background(SurfaceDark, RoundedCornerShape(10.dp))
              .padding(12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Column {
              Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(text = user.fullName, color = TextPrimary, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                Box(
                  modifier = Modifier
                    .clip(RoundedCornerShape(6.dp))
                    .background(if (user.role == "Admin") BrandPurple.copy(alpha = 0.2f) else SurfaceCardBorder)
                    .padding(horizontal = 6.dp, vertical = 2.dp)
                ) {
                  Text(text = user.role, color = if (user.role == "Admin") BrandPurple else TextMuted, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                }
              }
              Text(text = "${user.email} • Plan: ${user.plan} • Joined: ${user.joinedDate}", color = TextMuted, fontSize = 11.sp)
            }

            Row(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalAlignment = Alignment.CenterVertically) {
              OutlinedButton(
                onClick = { viewModel.toggleUserStatus(user.id) },
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.outlinedButtonColors(
                  contentColor = if (user.status == "Active") Color(0xFFEF4444) else BrandAccent
                )
              ) {
                Text(text = if (user.status == "Active") "Suspend" else "Activate", fontSize = 11.sp, fontWeight = FontWeight.Bold)
              }
            }
          }
        }
      }
    }
  }
}

// -----------------------------------------------------------------------------
// TAB 3: RESUMES TAB
// -----------------------------------------------------------------------------
@Composable
fun AdminResumesTab(
  uiState: com.example.data.admin.AdminDashboardUiState,
  viewModel: AdminDashboardViewModel
) {
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
      shape = RoundedCornerShape(16.dp),
      colors = CardDefaults.cardColors(containerColor = SurfaceCard),
      border = CardDefaults.outlinedCardBorder()
    ) {
      Column(
        modifier = Modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
      ) {
        Text(text = "Global Resumes Directory (${uiState.resumesList.size})", color = TextPrimary, fontSize = 16.sp, fontWeight = FontWeight.Bold)
        HorizontalDivider(color = SurfaceCardBorder)

        uiState.resumesList.forEach { r ->
          Row(
            modifier = Modifier
              .fillMaxWidth()
              .background(SurfaceDark, RoundedCornerShape(10.dp))
              .padding(12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Column {
              Text(text = r.title, color = TextPrimary, fontSize = 14.sp, fontWeight = FontWeight.Bold)
              Text(text = "Owner: ${r.ownerEmail} • Template: ${r.templateName} • ATS: ${r.atsScore}%", color = TextMuted, fontSize = 11.sp)
            }

            Row(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalAlignment = Alignment.CenterVertically) {
              IconButton(onClick = { viewModel.toggleResumeFeatured(r.id) }) {
                Icon(
                  imageVector = Icons.Default.Star,
                  contentDescription = "Feature",
                  tint = if (r.isFeatured) BrandAmber else TextMuted,
                  modifier = Modifier.size(18.dp)
                )
              }
              IconButton(onClick = { viewModel.deleteResume(r.id) }) {
                Icon(imageVector = Icons.Default.Delete, contentDescription = "Delete", tint = Color(0xFFEF4444), modifier = Modifier.size(18.dp))
              }
            }
          }
        }
      }
    }
  }
}

// -----------------------------------------------------------------------------
// TAB 4: TEMPLATES TAB
// -----------------------------------------------------------------------------
@Composable
fun AdminTemplatesTab(
  uiState: com.example.data.admin.AdminDashboardUiState,
  viewModel: AdminDashboardViewModel
) {
  var showAddDialog by remember { mutableStateOf(false) }
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
      shape = RoundedCornerShape(16.dp),
      colors = CardDefaults.cardColors(containerColor = SurfaceCard),
      border = CardDefaults.outlinedCardBorder()
    ) {
      Column(
        modifier = Modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
      ) {
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Text(text = "Resume Templates Catalog (${uiState.templatesList.size})", color = TextPrimary, fontSize = 16.sp, fontWeight = FontWeight.Bold)
          Button(
            onClick = { showAddDialog = true },
            shape = RoundedCornerShape(8.dp),
            colors = ButtonDefaults.buttonColors(containerColor = BrandPrimary)
          ) {
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
              Icon(imageVector = Icons.Default.Add, contentDescription = null, modifier = Modifier.size(14.dp))
              Text("Add Template", fontSize = 11.sp, fontWeight = FontWeight.Bold)
            }
          }
        }

        HorizontalDivider(color = SurfaceCardBorder)

        uiState.templatesList.forEach { t ->
          Row(
            modifier = Modifier
              .fillMaxWidth()
              .background(SurfaceDark, RoundedCornerShape(10.dp))
              .padding(12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Column {
              Text(text = t.name, color = TextPrimary, fontSize = 14.sp, fontWeight = FontWeight.Bold)
              Text(text = "Category: ${t.category} • Used ${t.usageCount} times • ${if (t.isPremium) "Pro Only" else "Free Tier"}", color = TextMuted, fontSize = 11.sp)
            }

            Switch(
              checked = t.isActive,
              onCheckedChange = { viewModel.toggleTemplateActive(t.id) },
              colors = SwitchDefaults.colors(checkedThumbColor = BrandPrimary, checkedTrackColor = BrandPurple.copy(alpha = 0.5f))
            )
          }
        }
      }
    }
  }

  if (showAddDialog) {
    var name by remember { mutableStateOf("") }
    var category by remember { mutableStateOf("Tech / Engineering") }
    var isPremium by remember { mutableStateOf(true) }

    Dialog(onDismissRequest = { showAddDialog = false }) {
      Surface(
        shape = RoundedCornerShape(16.dp),
        color = SurfaceCard,
        modifier = Modifier.padding(16.dp)
      ) {
        Column(
          modifier = Modifier.padding(20.dp),
          verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
          Text(text = "Add New Resume Template", color = TextPrimary, fontSize = 16.sp, fontWeight = FontWeight.Bold)
          OutlinedTextField(
            value = name,
            onValueChange = { name = it },
            label = { Text("Template Name", color = TextMuted) },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(8.dp),
            colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = BrandPrimary, unfocusedBorderColor = SurfaceCardBorder, focusedTextColor = TextPrimary)
          )
          Button(
            onClick = {
              if (name.isNotBlank()) {
                viewModel.addNewTemplate(name, category, isPremium, "#6366F1")
                showAddDialog = false
              }
            },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(8.dp),
            colors = ButtonDefaults.buttonColors(containerColor = BrandPrimary)
          ) {
            Text("Create Template", fontWeight = FontWeight.Bold)
          }
        }
      }
    }
  }
}

// -----------------------------------------------------------------------------
// TAB 5: SUBSCRIPTIONS TAB
// -----------------------------------------------------------------------------
@Composable
fun AdminSubscriptionsTab(uiState: com.example.data.admin.AdminDashboardUiState) {
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
      shape = RoundedCornerShape(16.dp),
      colors = CardDefaults.cardColors(containerColor = SurfaceCard),
      border = CardDefaults.outlinedCardBorder()
    ) {
      Column(
        modifier = Modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
      ) {
        Text(text = "Stripe Active Subscriptions", color = TextPrimary, fontSize = 16.sp, fontWeight = FontWeight.Bold)
        HorizontalDivider(color = SurfaceCardBorder)

        uiState.subscriptionsList.forEach { s ->
          Row(
            modifier = Modifier
              .fillMaxWidth()
              .background(SurfaceDark, RoundedCornerShape(10.dp))
              .padding(12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Column {
              Text(text = s.userEmail, color = TextPrimary, fontSize = 13.sp, fontWeight = FontWeight.Bold)
              Text(text = "${s.planName} • \$${s.amountMonthly}/mo • Renewal: ${s.renewalDate}", color = TextMuted, fontSize = 11.sp)
            }

            Box(
              modifier = Modifier
                .clip(RoundedCornerShape(6.dp))
                .background(if (s.status == "Active") BrandAccent.copy(alpha = 0.2f) else SurfaceCardBorder)
                .padding(horizontal = 8.dp, vertical = 4.dp)
            ) {
              Text(text = s.status, color = if (s.status == "Active") BrandAccent else TextMuted, fontSize = 10.sp, fontWeight = FontWeight.Bold)
            }
          }
        }
      }
    }
  }
}

// -----------------------------------------------------------------------------
// TAB 6: REPORTS TAB
// -----------------------------------------------------------------------------
@Composable
fun AdminReportsTab(uiState: com.example.data.admin.AdminDashboardUiState) {
  Column(
    modifier = Modifier
      .fillMaxSize()
      .padding(20.dp),
    verticalArrangement = Arrangement.spacedBy(16.dp)
  ) {
    Card(
      modifier = Modifier.fillMaxWidth(),
      shape = RoundedCornerShape(16.dp),
      colors = CardDefaults.cardColors(containerColor = SurfaceCard),
      border = CardDefaults.outlinedCardBorder()
    ) {
      Column(
        modifier = Modifier.padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
      ) {
        Text(text = "System Security & Performance Audits", color = TextPrimary, fontSize = 16.sp, fontWeight = FontWeight.Bold)
        Text(text = "No security breaches or unauthorized Gemini API key quota violations detected in the last 30 days.", color = TextSecondary, fontSize = 12.sp)
        HorizontalDivider(color = SurfaceCardBorder)
        Text(text = "• All ATS parsing engines verified against latest 2026 Workday/Greenhouse standard format.", color = TextMuted, fontSize = 11.sp)
      }
    }
  }
}

// -----------------------------------------------------------------------------
// TAB 7: BLOG CMS TAB
// -----------------------------------------------------------------------------
@Composable
fun AdminBlogTab(
  uiState: com.example.data.admin.AdminDashboardUiState,
  viewModel: AdminDashboardViewModel
) {
  var showCreateBlog by remember { mutableStateOf(false) }
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
      shape = RoundedCornerShape(16.dp),
      colors = CardDefaults.cardColors(containerColor = SurfaceCard),
      border = CardDefaults.outlinedCardBorder()
    ) {
      Column(
        modifier = Modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
      ) {
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Text(text = "Blog Articles CMS (${uiState.blogPostsList.size})", color = TextPrimary, fontSize = 16.sp, fontWeight = FontWeight.Bold)
          Button(
            onClick = { showCreateBlog = true },
            shape = RoundedCornerShape(8.dp),
            colors = ButtonDefaults.buttonColors(containerColor = BrandPurple)
          ) {
            Text("Publish New Post", fontSize = 11.sp, fontWeight = FontWeight.Bold)
          }
        }

        HorizontalDivider(color = SurfaceCardBorder)

        uiState.blogPostsList.forEach { p ->
          Row(
            modifier = Modifier
              .fillMaxWidth()
              .background(SurfaceDark, RoundedCornerShape(10.dp))
              .padding(12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Column {
              Text(text = p.title, color = TextPrimary, fontSize = 13.sp, fontWeight = FontWeight.Bold)
              Text(text = "Category: ${p.category} • Author: ${p.author} • ${p.views} views", color = TextMuted, fontSize = 11.sp)
            }

            OutlinedButton(
              onClick = { viewModel.toggleBlogPostStatus(p.id) },
              shape = RoundedCornerShape(8.dp)
            ) {
              Text(text = p.status, fontSize = 11.sp, fontWeight = FontWeight.Bold)
            }
          }
        }
      }
    }
  }

  if (showCreateBlog) {
    var title by remember { mutableStateOf("") }
    var category by remember { mutableStateOf("Resume Advice") }

    Dialog(onDismissRequest = { showCreateBlog = false }) {
      Surface(shape = RoundedCornerShape(16.dp), color = SurfaceCard, modifier = Modifier.padding(16.dp)) {
        Column(modifier = Modifier.padding(20.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
          Text("Publish Blog Post", color = TextPrimary, fontWeight = FontWeight.Bold)
          OutlinedTextField(value = title, onValueChange = { title = it }, label = { Text("Article Title", color = TextMuted) }, modifier = Modifier.fillMaxWidth())
          Button(
            onClick = {
              if (title.isNotBlank()) {
                viewModel.createBlogPost(title, category, "SaaS Editorial Team")
                showCreateBlog = false
              }
            },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(containerColor = BrandPurple)
          ) {
            Text("Publish Now", fontWeight = FontWeight.Bold)
          }
        }
      }
    }
  }
}

// -----------------------------------------------------------------------------
// TAB 8: SUPPORT TAB
// -----------------------------------------------------------------------------
@Composable
fun AdminSupportTab(
  uiState: com.example.data.admin.AdminDashboardUiState,
  viewModel: AdminDashboardViewModel
) {
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
      shape = RoundedCornerShape(16.dp),
      colors = CardDefaults.cardColors(containerColor = SurfaceCard),
      border = CardDefaults.outlinedCardBorder()
    ) {
      Column(
        modifier = Modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
      ) {
        Text(text = "User Support Tickets", color = TextPrimary, fontSize = 16.sp, fontWeight = FontWeight.Bold)
        HorizontalDivider(color = SurfaceCardBorder)

        uiState.supportTicketsList.forEach { tk ->
          Row(
            modifier = Modifier
              .fillMaxWidth()
              .background(SurfaceDark, RoundedCornerShape(10.dp))
              .padding(12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Column {
              Text(text = tk.subject, color = TextPrimary, fontSize = 13.sp, fontWeight = FontWeight.Bold)
              Text(text = "From: ${tk.userEmail} • Priority: ${tk.priority} • Date: ${tk.date}", color = TextMuted, fontSize = 11.sp)
            }

            Button(
              onClick = { viewModel.updateTicketStatus(tk.id, "Resolved") },
              shape = RoundedCornerShape(8.dp),
              colors = ButtonDefaults.buttonColors(containerColor = if (tk.status == "Resolved") BrandAccent else BrandPrimary)
            ) {
              Text(text = tk.status, fontSize = 11.sp, fontWeight = FontWeight.Bold)
            }
          }
        }
      }
    }
  }
}

// -----------------------------------------------------------------------------
// TAB 9: SETTINGS TAB
// -----------------------------------------------------------------------------
@Composable
fun AdminSettingsTab(
  uiState: com.example.data.admin.AdminDashboardUiState,
  viewModel: AdminDashboardViewModel
) {
  var platformName by remember { mutableStateOf(uiState.systemSettings.platformName) }
  var maintenanceMode by remember { mutableStateOf(uiState.systemSettings.maintenanceMode) }
  var allowRegistrations by remember { mutableStateOf(uiState.systemSettings.allowNewRegistrations) }

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
      shape = RoundedCornerShape(16.dp),
      colors = CardDefaults.cardColors(containerColor = SurfaceCard),
      border = CardDefaults.outlinedCardBorder()
    ) {
      Column(
        modifier = Modifier.padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
      ) {
        Text(text = "Global SaaS System Configuration", color = TextPrimary, fontSize = 18.sp, fontWeight = FontWeight.Bold)

        OutlinedTextField(
          value = platformName,
          onValueChange = { platformName = it },
          label = { Text("Application Brand Name", color = TextMuted) },
          modifier = Modifier.fillMaxWidth(),
          shape = RoundedCornerShape(10.dp),
          colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = BrandPrimary, unfocusedBorderColor = SurfaceCardBorder, focusedTextColor = TextPrimary)
        )

        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Text(text = "Maintenance Mode", color = TextPrimary, fontSize = 14.sp)
          Switch(checked = maintenanceMode, onCheckedChange = { maintenanceMode = it })
        }

        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Text(text = "Allow New User Registrations", color = TextPrimary, fontSize = 14.sp)
          Switch(checked = allowRegistrations, onCheckedChange = { allowRegistrations = it })
        }

        Button(
          onClick = {
            viewModel.updateSystemSettings(
              AdminSystemSettings(
                platformName = platformName,
                maintenanceMode = maintenanceMode,
                allowNewRegistrations = allowRegistrations
              )
            )
          },
          modifier = Modifier.fillMaxWidth(),
          shape = RoundedCornerShape(10.dp),
          colors = ButtonDefaults.buttonColors(containerColor = BrandPrimary)
        ) {
          Text(text = "Save Settings", fontWeight = FontWeight.Bold)
        }
      }
    }
  }
}
