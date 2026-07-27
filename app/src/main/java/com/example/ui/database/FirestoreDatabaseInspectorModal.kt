package com.example.ui.database

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
import androidx.compose.material.icons.filled.Code
import androidx.compose.material.icons.filled.Dataset
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Storage
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
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
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
import com.example.data.firestore.FirestoreRepository
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
import kotlinx.coroutines.launch

@Composable
fun FirestoreDatabaseInspectorModal(
  onDismiss: () -> Unit
) {
  val context = LocalContext.current
  val clipboardManager = LocalClipboardManager.current
  val coroutineScope = rememberCoroutineScope()
  var selectedTab by remember { mutableStateOf(0) } // 0: Schema, 1: Security Rules, 2: Indexes, 3: Sample Collections

  var isSeeding by remember { mutableStateOf(false) }

  val rulesText = remember {
    """
rules_version = '2';
service cloud.firestore {
  match /databases/{database}/documents {
    function isAuthenticated() { return request.auth != null; }
    function isOwner(userId) { return isAuthenticated() && request.auth.uid == userId; }
    function isAdmin() { return isAuthenticated() && request.auth.token.admin == true; }

    match /users/{userId} {
      allow read, update: if isOwner(userId) || isAdmin();
      allow create: if isAuthenticated() && request.auth.uid == userId;
    }

    match /resumes/{resumeId} {
      allow read: if isAuthenticated() && (resource.data.userId == request.auth.uid || resource.data.isPublic == true || isAdmin());
      allow create, update, delete: if isAuthenticated() && (resource.data.userId == request.auth.uid || isAdmin());
    }

    match /cover_letters/{letterId} {
      allow read, write: if isAuthenticated() && (resource.data.userId == request.auth.uid || isAdmin());
    }

    match /templates/{templateId} {
      allow read: if true;
      allow write: if isAdmin();
    }

    match /subscriptions/{subscriptionId} {
      allow read, write: if isAuthenticated() && (resource.data.userId == request.auth.uid || isAdmin());
    }
  }
}
    """.trimIndent()
  }

  val indexesText = remember {
    """
{
  "indexes": [
    {
      "collectionGroup": "resumes",
      "fields": [
        { "fieldPath": "userId", "order": "ASCENDING" },
        { "fieldPath": "updatedAt", "order": "DESCENDING" }
      ]
    },
    {
      "collectionGroup": "cover_letters",
      "fields": [
        { "fieldPath": "userId", "order": "ASCENDING" },
        { "fieldPath": "createdAt", "order": "DESCENDING" }
      ]
    },
    {
      "collectionGroup": "subscriptions",
      "fields": [
        { "fieldPath": "userId", "order": "ASCENDING" },
        { "fieldPath": "status", "order": "ASCENDING" }
      ]
    }
  ]
}
    """.trimIndent()
  }

  Dialog(onDismissRequest = onDismiss) {
    Surface(
      shape = RoundedCornerShape(24.dp),
      color = SurfaceCard,
      modifier = Modifier
        .fillMaxWidth()
        .padding(10.dp),
      border = CardDefaults.outlinedCardBorder().copy(brush = Brush.verticalGradient(listOf(BrandPurple, SurfaceCardBorder)))
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
                .background(Brush.linearGradient(listOf(BrandPurple, BrandPrimary))),
              contentAlignment = Alignment.Center
            ) {
              Icon(imageVector = Icons.Default.Storage, contentDescription = null, tint = Color.White, modifier = Modifier.size(18.dp))
            }
            Column {
              Text(text = "Cloud Firestore Database", color = TextPrimary, fontSize = 16.sp, fontWeight = FontWeight.Bold)
              Text(text = "Structure, Rules, Indexes & Live Collections", color = BrandAccent, fontSize = 11.sp, fontWeight = FontWeight.SemiBold)
            }
          }

          IconButton(onClick = onDismiss, modifier = Modifier.testTag("close_firestore_modal")) {
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
          val tabs = listOf("Schema", "Rules", "Indexes", "Collections")
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
                fontSize = 11.sp,
                fontWeight = if (isSel) FontWeight.Bold else FontWeight.Normal
              )
            }
          }
        }

        when (selectedTab) {
          0 -> {
            // SCHEMA OVERVIEW
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
              Text(text = "Firestore Data Architecture", color = TextPrimary, fontSize = 14.sp, fontWeight = FontWeight.Bold)

              CollectionSchemaCard("users", "User Profiles & Accounts", "users/{userId}", listOf("uid", "email", "displayName", "role", "subscriptionPlan"))
              CollectionSchemaCard("resumes", "Resume Documents", "resumes/{resumeId}", listOf("id", "userId", "title", "fullName", "atsScore", "workExperienceJson", "skillsJson"))
              CollectionSchemaCard("cover_letters", "AI Cover Letters", "cover_letters/{letterId}", listOf("id", "userId", "resumeId", "targetJobTitle", "companyName", "letterText"))
              CollectionSchemaCard("templates", "Resume Templates Catalog", "templates/{templateId}", listOf("id", "name", "category", "isPremium", "primaryColorHex"))
              CollectionSchemaCard("subscriptions", "Billing & Plans", "subscriptions/{subscriptionId}", listOf("id", "userId", "planId", "status", "stripeCustomerId", "currentPeriodEnd"))

              Button(
                onClick = {
                  coroutineScope.launch {
                    isSeeding = true
                    FirestoreRepository.seedDefaultTemplatesIfEmpty()
                    isSeeding = false
                    Toast.makeText(context, "Default template documents seeded successfully!", Toast.LENGTH_SHORT).show()
                  }
                },
                modifier = Modifier
                  .fillMaxWidth()
                  .height(44.dp)
                  .testTag("seed_firestore_button"),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = BrandPurple)
              ) {
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                  Icon(imageVector = Icons.Default.Dataset, contentDescription = null, modifier = Modifier.size(16.dp))
                  Text(text = if (isSeeding) "Seeding Collections..." else "Seed Default Templates Collection", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                }
              }
            }
          }

          1 -> CodeViewerCard("firestore.rules", rulesText, clipboardManager, context)
          2 -> CodeViewerCard("firestore.indexes.json", indexesText, clipboardManager, context)
          3 -> {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
              Text(text = "Sample Collection Payloads", color = TextPrimary, fontSize = 14.sp, fontWeight = FontWeight.Bold)

              Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = SurfaceDark)
              ) {
                Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                  Text(text = "Sample /resumes Document", color = BrandAccent, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                  Text(
                    text = """
{
  "id": "res_884920",
  "userId": "usr_77391",
  "title": "Senior Staff Software Engineer",
  "fullName": "Alex Morgan",
  "atsScore": 94,
  "selectedTemplate": "Modern Tech",
  "isPublic": true,
  "createdAt": 1785058000000
}
                    """.trimIndent(),
                    color = TextSecondary,
                    fontSize = 11.sp,
                    fontFamily = FontFamily.Monospace
                  )
                }
              }

              Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = SurfaceDark)
              ) {
                Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                  Text(text = "Sample /subscriptions Document", color = BrandAmber, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                  Text(
                    text = """
{
  "id": "sub_99201",
  "userId": "usr_77391",
  "planId": "PRO",
  "status": "active",
  "stripeCustomerId": "cus_Nks88129",
  "currentPeriodEnd": 1787650000000
}
                    """.trimIndent(),
                    color = TextSecondary,
                    fontSize = 11.sp,
                    fontFamily = FontFamily.Monospace
                  )
                }
              }
            }
          }
        }
      }
    }
  }
}

@Composable
fun CollectionSchemaCard(
  collectionName: String,
  title: String,
  path: String,
  fields: List<String>
) {
  Card(
    modifier = Modifier.fillMaxWidth(),
    shape = RoundedCornerShape(14.dp),
    colors = CardDefaults.cardColors(containerColor = SurfaceDark)
  ) {
    Column(modifier = Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
          Icon(imageVector = Icons.Default.Storage, contentDescription = null, tint = BrandPrimary, modifier = Modifier.size(16.dp))
          Text(text = "/$collectionName", color = TextPrimary, fontSize = 13.sp, fontWeight = FontWeight.Bold)
        }
        Text(text = path, color = TextMuted, fontSize = 10.sp, fontFamily = FontFamily.Monospace)
      }

      Text(text = title, color = TextSecondary, fontSize = 11.sp)

      Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(4.dp)) {
        fields.take(4).forEach { field ->
          Box(
            modifier = Modifier
              .clip(RoundedCornerShape(6.dp))
              .background(SurfaceCardBorder)
              .padding(horizontal = 6.dp, vertical = 2.dp)
          ) {
            Text(text = field, color = BrandAccent, fontSize = 9.sp, fontFamily = FontFamily.Monospace)
          }
        }
        if (fields.size > 4) {
          Box(
            modifier = Modifier
              .clip(RoundedCornerShape(6.dp))
              .background(SurfaceCardBorder)
              .padding(horizontal = 6.dp, vertical = 2.dp)
          ) {
            Text(text = "+${fields.size - 4}", color = TextMuted, fontSize = 9.sp)
          }
        }
      }
    }
  }
}
