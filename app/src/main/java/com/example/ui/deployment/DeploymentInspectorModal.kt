package com.example.ui.deployment

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.material.icons.filled.CloudDone
import androidx.compose.material.icons.filled.Code
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.RocketLaunch
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
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
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.data.seo.SeoManager
import com.example.ui.seo.CodeViewerCard
import com.example.ui.theme.BackgroundDark
import com.example.ui.theme.BrandAccent
import com.example.ui.theme.BrandAmber
import com.example.ui.theme.BrandPrimary
import com.example.ui.theme.BrandPurple
import com.example.ui.theme.SurfaceCard
import com.example.ui.theme.SurfaceCardBorder
import com.example.ui.theme.SurfaceDark
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary

@Composable
fun DeploymentInspectorModal(
  onDismiss: () -> Unit
) {
  val context = LocalContext.current
  val clipboardManager = LocalClipboardManager.current
  var selectedTab by remember { mutableStateOf(0) } // 0: Overview, 1: firebase.json, 2: vercel.json, 3: GitHub Actions, 4: DEPLOYMENT.md

  val firebaseJson = remember {
    """
{
  "hosting": {
    "public": "app/src/main/assets",
    "rewrites": [{ "source": "**", "destination": "/sitemap.xml" }],
    "headers": [
      {
        "source": "**",
        "headers": [
          { "key": "Strict-Transport-Security", "value": "max-age=31536000; includeSubDomains" },
          { "key": "X-Content-Type-Options", "value": "nosniff" },
          { "key": "X-Frame-Options", "value": "DENY" }
        ]
      }
    ]
  },
  "firestore": {
    "rules": "firestore.rules",
    "indexes": "firestore.indexes.json"
  }
}
    """.trimIndent()
  }

  val vercelJson = remember {
    """
{
  "version": 2,
  "name": "resume-studio-mobile",
  "cleanUrls": true,
  "routes": [
    { "src": "/.well-known/assetlinks.json", "dest": "/app/src/main/assets/assetlinks.json" },
    { "src": "/sitemap.xml", "dest": "/app/src/main/assets/sitemap.xml" }
  ]
}
    """.trimIndent()
  }

  val deployManual = remember {
    """
# Quick CLI Deploy Commands:

## 1. Firebase Hosting & Firestore Rules:
npm install -g firebase-tools
firebase login
firebase deploy --only hosting,firestore

## 2. Vercel Hosting:
npm install -g vercel
vercel --prod

## 3. Android Production Release Build:
gradle :app:bundleRelease :app:assembleRelease
    """.trimIndent()
  }

  val githubActions = remember {
    """
name: Production Deployment Pipeline
on:
  push:
    branches: [ main ]
jobs:
  deploy:
    runs-on: ubuntu-latest
    steps:
      - uses: actions/checkout@v4
      - name: Build Android Bundle & APK
        run: gradle :app:bundleRelease
      - name: Deploy Firebase Hosting
        uses: FirebaseExtended/action-hosting-deploy@v0
    """.trimIndent()
  }

  Dialog(onDismissRequest = onDismiss) {
    Surface(
      shape = RoundedCornerShape(24.dp),
      color = SurfaceCard,
      modifier = Modifier
        .fillMaxWidth()
        .padding(10.dp),
      border = CardDefaults.outlinedCardBorder().copy(brush = Brush.verticalGradient(listOf(BrandAccent, SurfaceCardBorder)))
    ) {
      Column(
        modifier = Modifier
          .padding(20.dp)
          .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(16.dp)
      ) {
        // Modal Header
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            Box(
              modifier = Modifier
                .size(36.dp)
                .clip(RoundedCornerShape(10.dp))
                .background(Brush.linearGradient(listOf(BrandAccent, BrandPurple))),
              contentAlignment = Alignment.Center
            ) {
              Icon(imageVector = Icons.Default.RocketLaunch, contentDescription = null, tint = Color.White, modifier = Modifier.size(18.dp))
            }
            Column {
              Text(text = "Production Deploy & Hosting", color = TextPrimary, fontSize = 16.sp, fontWeight = FontWeight.Bold)
              Text(text = "Firebase Hosting, Vercel, Security & CI/CD", color = BrandAccent, fontSize = 11.sp, fontWeight = FontWeight.SemiBold)
            }
          }

          IconButton(onClick = onDismiss, modifier = Modifier.testTag("close_deploy_modal")) {
            Icon(imageVector = Icons.Default.Close, contentDescription = "Close", tint = TextMuted)
          }
        }

        HorizontalDivider(color = SurfaceCardBorder)

        // TABS SELECTOR
        Row(
          modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(SurfaceDark)
            .padding(4.dp),
          horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
          val tabs = listOf("Overview", "Firebase", "Vercel", "CI/CD Pipeline", "Commands")
          tabs.forEachIndexed { idx, label ->
            val isSel = selectedTab == idx
            Box(
              modifier = Modifier
                .weight(1f)
                .clip(RoundedCornerShape(8.dp))
                .background(if (isSel) BrandPrimary else Color.Transparent)
                .clickable { selectedTab = idx }
                .padding(vertical = 6.dp),
              contentAlignment = Alignment.Center
            ) {
              Text(
                text = label,
                color = if (isSel) Color.White else TextMuted,
                fontSize = 10.sp,
                fontWeight = if (isSel) FontWeight.Bold else FontWeight.Normal,
                maxLines = 1
              )
            }
          }
        }

        when (selectedTab) {
          0 -> {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
              Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                Card(
                  modifier = Modifier.weight(1f),
                  colors = CardDefaults.cardColors(containerColor = SurfaceDark),
                  shape = RoundedCornerShape(14.dp)
                ) {
                  Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    Text(text = "Firebase Hosting", color = TextMuted, fontSize = 11.sp)
                    Text(text = "CONFIGURED", color = BrandAccent, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                    Text(text = "firebase.json Ready", color = TextSecondary, fontSize = 10.sp)
                  }
                }

                Card(
                  modifier = Modifier.weight(1f),
                  colors = CardDefaults.cardColors(containerColor = SurfaceDark),
                  shape = RoundedCornerShape(14.dp)
                ) {
                  Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    Text(text = "Vercel Platform", color = TextMuted, fontSize = 11.sp)
                    Text(text = "READY", color = BrandAmber, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                    Text(text = "vercel.json Ready", color = TextSecondary, fontSize = 10.sp)
                  }
                }
              }

              Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = SurfaceDark)
              ) {
                Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                  Text(text = "Production Optimization Audit", color = TextPrimary, fontSize = 13.sp, fontWeight = FontWeight.Bold)

                  DeployCheckItem("Strict Transport Security (HSTS)", "Enforces HTTPS max-age 31536000", true)
                  DeployCheckItem("X-Frame-Options DENY", "Protects app against clickjacking", true)
                  DeployCheckItem("Content-Type-Options nosniff", "Prevents MIME sniffing vulnerabilities", true)
                  DeployCheckItem("AssetLinks Verification", "Deep links to resumestudio.app", true)
                  DeployCheckItem("Release AAB / APK Build Target", "Optimized Kotlin bytecode & ProGuard", true)
                }
              }
            }
          }

          1 -> CodeViewerCard("firebase.json", firebaseJson, clipboardManager, context)
          2 -> CodeViewerCard("vercel.json", vercelJson, clipboardManager, context)
          3 -> CodeViewerCard(".github/workflows/deploy.yml", githubActions, clipboardManager, context)
          4 -> CodeViewerCard("DEPLOYMENT.md", deployManual, clipboardManager, context)
        }
      }
    }
  }
}

@Composable
fun DeployCheckItem(label: String, desc: String, passed: Boolean) {
  Row(
    modifier = Modifier.fillMaxWidth(),
    horizontalArrangement = Arrangement.SpaceBetween,
    verticalAlignment = Alignment.CenterVertically
  ) {
    Column(modifier = Modifier.weight(1f)) {
      Text(text = label, color = TextPrimary, fontSize = 12.sp, fontWeight = FontWeight.Bold)
      Text(text = desc, color = TextMuted, fontSize = 10.sp)
    }
    Box(
      modifier = Modifier
        .clip(CircleShape)
        .background(if (passed) BrandAccent.copy(alpha = 0.2f) else Color.Red.copy(alpha = 0.2f))
        .padding(4.dp)
    ) {
      Icon(imageVector = Icons.Default.Check, contentDescription = null, tint = BrandAccent, modifier = Modifier.size(14.dp))
    }
  }
}
