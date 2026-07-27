package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.runtime.collectAsState
import com.example.data.auth.AuthViewModel
import com.example.data.builder.ResumeBuilderViewModel
import com.example.data.dashboard.DashboardViewModel
import com.example.ui.auth.AuthScreen
import com.example.ui.builder.ResumeBuilderScreen
import com.example.ui.dashboard.DashboardScreen
import com.example.data.admin.AdminDashboardViewModel
import com.example.ui.admin.AdminDashboardScreen
import com.example.ui.landing.LandingScreen
import com.example.ui.theme.BackgroundDark
import com.example.ui.theme.MyApplicationTheme

enum class AppDestination {
  LANDING,
  AUTH,
  DASHBOARD,
  BUILDER,
  ADMIN
}

class MainActivity : ComponentActivity() {

  private val authViewModel: AuthViewModel by viewModels()
  private val dashboardViewModel: DashboardViewModel by viewModels()
  private val builderViewModel: ResumeBuilderViewModel by viewModels()
  private val adminViewModel: AdminDashboardViewModel by viewModels()

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    enableEdgeToEdge()
    setContent {
      MyApplicationTheme {
        var currentDestination by remember { mutableStateOf(AppDestination.LANDING) }
        val userSession by authViewModel.userSession.collectAsState()

        Surface(
          modifier = Modifier.fillMaxSize(),
          color = BackgroundDark
        ) {
          Crossfade(
            targetState = currentDestination,
            animationSpec = tween(300),
            label = "screen_crossfade"
          ) { destination ->
            when (destination) {
              AppDestination.LANDING -> {
                LandingScreen(
                  onStartBuilding = {
                    currentDestination = AppDestination.BUILDER
                  },
                  onOpenAuth = {
                    if (userSession != null) {
                      currentDestination = AppDestination.DASHBOARD
                    } else {
                      currentDestination = AppDestination.AUTH
                    }
                  }
                )
              }
              AppDestination.AUTH -> {
                AuthScreen(
                  viewModel = authViewModel,
                  onNavigateBack = { currentDestination = AppDestination.LANDING },
                  onAuthSuccess = { currentDestination = AppDestination.DASHBOARD }
                )
              }
              AppDestination.DASHBOARD -> {
                DashboardScreen(
                  viewModel = dashboardViewModel,
                  userSession = userSession,
                  onLogout = {
                    authViewModel.logout()
                    currentDestination = AppDestination.AUTH
                  },
                  onNavigateHome = { currentDestination = AppDestination.LANDING },
                  onOpenResumeBuilder = { currentDestination = AppDestination.BUILDER },
                  onOpenAdmin = { currentDestination = AppDestination.ADMIN },
                  onUpdateSubscription = { plan -> authViewModel.upgradeSubscriptionPlan(plan) }
                )
              }
              AppDestination.BUILDER -> {
                ResumeBuilderScreen(
                  viewModel = builderViewModel,
                  userSession = userSession,
                  onNavigateBack = {
                    if (userSession != null) {
                      currentDestination = AppDestination.DASHBOARD
                    } else {
                      currentDestination = AppDestination.LANDING
                    }
                  },
                  onUpdateSubscription = { plan -> authViewModel.upgradeSubscriptionPlan(plan) }
                )
              }
              AppDestination.ADMIN -> {
                AdminDashboardScreen(
                  viewModel = adminViewModel,
                  onExitAdmin = { currentDestination = AppDestination.DASHBOARD }
                )
              }
            }
          }
        }
      }
    }
  }
}


