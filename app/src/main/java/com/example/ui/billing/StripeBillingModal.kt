package com.example.ui.billing

import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
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
import androidx.compose.material.icons.filled.CreditCard
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Star
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.data.auth.SubscriptionPlan
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
fun StripeBillingModal(
  currentPlan: SubscriptionPlan,
  onSelectPlan: (SubscriptionPlan) -> Unit,
  onDismiss: () -> Unit
) {
  val context = LocalContext.current
  var isAnnual by remember { mutableStateOf(true) }
  var selectedPlan by remember { mutableStateOf(if (currentPlan == SubscriptionPlan.FREE) SubscriptionPlan.PRO else currentPlan) }
  var showPaymentForm by remember { mutableStateOf(false) }

  // Payment Form Fields
  var cardHolderName by remember { mutableStateOf("Alexander Wright") }
  var cardNumber by remember { mutableStateOf("4242 •••• •••• 4242") }
  var expiryDate by remember { mutableStateOf("12/28") }
  var cvcCode by remember { mutableStateOf("888") }
  var postalCode by remember { mutableStateOf("94103") }
  var promoCode by remember { mutableStateOf("") }
  var discountApplied by remember { mutableStateOf(false) }
  var isProcessing by remember { mutableStateOf(false) }

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
              Icon(imageVector = Icons.Default.Lock, contentDescription = null, tint = Color.White, modifier = Modifier.size(18.dp))
            }
            Column {
              Text(text = "Stripe Payment & Subscriptions", color = TextPrimary, fontSize = 16.sp, fontWeight = FontWeight.Bold)
              Text(text = "256-bit SSL Secure Checkout", color = BrandAccent, fontSize = 11.sp, fontWeight = FontWeight.SemiBold)
            }
          }

          IconButton(onClick = onDismiss, modifier = Modifier.testTag("close_billing_modal")) {
            Icon(imageVector = Icons.Default.Close, contentDescription = "Close", tint = TextMuted)
          }
        }

        HorizontalDivider(color = SurfaceCardBorder)

        if (!showPaymentForm) {
          // Billing Interval Toggle (Monthly / Annual)
          Row(
            modifier = Modifier
              .fillMaxWidth()
              .clip(RoundedCornerShape(12.dp))
              .background(SurfaceDark)
              .padding(4.dp),
            horizontalArrangement = Arrangement.spacedBy(4.dp)
          ) {
            Box(
              modifier = Modifier
                .weight(1f)
                .clip(RoundedCornerShape(10.dp))
                .background(if (!isAnnual) BrandPrimary else Color.Transparent)
                .clickable { isAnnual = false }
                .padding(vertical = 8.dp),
              contentAlignment = Alignment.Center
            ) {
              Text(text = "Monthly Billing", color = if (!isAnnual) Color.White else TextMuted, fontSize = 12.sp, fontWeight = FontWeight.Bold)
            }

            Box(
              modifier = Modifier
                .weight(1f)
                .clip(RoundedCornerShape(10.dp))
                .background(if (isAnnual) BrandPrimary else Color.Transparent)
                .clickable { isAnnual = true }
                .padding(vertical = 8.dp),
              contentAlignment = Alignment.Center
            ) {
              Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                Text(text = "Annual Billing", color = if (isAnnual) Color.White else TextMuted, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                Box(
                  modifier = Modifier
                    .clip(RoundedCornerShape(6.dp))
                    .background(BrandAmber)
                    .padding(horizontal = 6.dp, vertical = 2.dp)
                ) {
                  Text(text = "SAVE 35%", color = Color.Black, fontSize = 9.sp, fontWeight = FontWeight.Bold)
                }
              }
            }
          }

          // PLAN CARDS
          Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
            SubscriptionPlan.values().forEach { plan ->
              val isSelected = selectedPlan == plan
              val priceText = if (plan == SubscriptionPlan.FREE) "$0" else if (isAnnual) "\$${plan.yearlyPrice}/yr" else "\$${plan.monthlyPrice}/mo"

              Card(
                modifier = Modifier
                  .fillMaxWidth()
                  .clip(RoundedCornerShape(16.dp))
                  .border(
                    width = if (isSelected) 2.dp else 1.dp,
                    color = if (isSelected) BrandPurple else SurfaceCardBorder,
                    shape = RoundedCornerShape(16.dp)
                  )
                  .clickable { selectedPlan = plan },
                colors = CardDefaults.cardColors(containerColor = if (isSelected) SurfaceDark else SurfaceCard)
              ) {
                Row(
                  modifier = Modifier
                    .fillMaxWidth()
                    .padding(14.dp),
                  horizontalArrangement = Arrangement.SpaceBetween,
                  verticalAlignment = Alignment.CenterVertically
                ) {
                  Column(modifier = Modifier.weight(1f)) {
                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                      Text(text = plan.displayName, color = TextPrimary, fontSize = 15.sp, fontWeight = FontWeight.Bold)
                      if (plan == SubscriptionPlan.PRO) {
                        Box(
                          modifier = Modifier
                            .clip(RoundedCornerShape(6.dp))
                            .background(BrandPurple)
                            .padding(horizontal = 6.dp, vertical = 2.dp)
                        ) {
                          Text(text = "MOST POPULAR", color = Color.White, fontSize = 9.sp, fontWeight = FontWeight.Bold)
                        }
                      } else if (plan == SubscriptionPlan.PREMIUM) {
                        Box(
                          modifier = Modifier
                            .clip(RoundedCornerShape(6.dp))
                            .background(BrandAmber)
                            .padding(horizontal = 6.dp, vertical = 2.dp)
                        ) {
                          Text(text = "ULTIMATE", color = Color.Black, fontSize = 9.sp, fontWeight = FontWeight.Bold)
                        }
                      }
                    }

                    Text(
                      text = when (plan) {
                        SubscriptionPlan.FREE -> "1 Resume • Standard Templates • Standard PDF Export"
                        SubscriptionPlan.PRO -> "10 Resumes • All Premium Templates • AI Auto-Writer • Cover Letters"
                        SubscriptionPlan.PREMIUM -> "Unlimited Resumes • 1-on-1 AI Coach • Priority PDF • DOCX Export"
                      },
                      color = TextMuted,
                      fontSize = 11.sp,
                      modifier = Modifier.padding(top = 4.dp)
                    )
                  }

                  Column(horizontalAlignment = Alignment.End) {
                    Text(text = priceText, color = TextPrimary, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                    if (currentPlan == plan) {
                      Text(text = "Current Plan", color = BrandAccent, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                    }
                  }
                }
              }
            }
          }

          // Checkout Action Button
          Button(
            onClick = {
              if (selectedPlan == SubscriptionPlan.FREE) {
                onSelectPlan(SubscriptionPlan.FREE)
                Toast.makeText(context, "Switched to Free Starter Plan", Toast.LENGTH_SHORT).show()
                onDismiss()
              } else {
                showPaymentForm = true
              }
            },
            modifier = Modifier
              .fillMaxWidth()
              .height(48.dp)
              .testTag("proceed_to_payment_button"),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(containerColor = BrandPrimary)
          ) {
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
              Icon(imageVector = Icons.Default.CreditCard, contentDescription = null, modifier = Modifier.size(18.dp))
              Text(
                text = if (selectedPlan == SubscriptionPlan.FREE) "Select Free Starter" else "Continue to Stripe Payment Sheet",
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold
              )
            }
          }
        } else {
          // STRIPE PAYMENT FORM SHEET
          Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            // Selected Summary Banner
            Row(
              modifier = Modifier
                .fillMaxWidth()
                .background(SurfaceDark, RoundedCornerShape(12.dp))
                .padding(12.dp),
              horizontalArrangement = Arrangement.SpaceBetween,
              verticalAlignment = Alignment.CenterVertically
            ) {
              Column {
                Text(text = "Plan: ${selectedPlan.displayName}", color = TextPrimary, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                Text(text = if (isAnnual) "Billed annually (\$" + selectedPlan.yearlyPrice + "/yr)" else "Billed monthly (\$" + selectedPlan.monthlyPrice + "/mo)", color = TextMuted, fontSize = 11.sp)
              }
              OutlinedButton(
                onClick = { showPaymentForm = false },
                shape = RoundedCornerShape(8.dp)
              ) {
                Text("Change Plan", fontSize = 11.sp, fontWeight = FontWeight.Bold)
              }
            }

            OutlinedTextField(
              value = cardHolderName,
              onValueChange = { cardHolderName = it },
              label = { Text("Cardholder Name", color = TextMuted, fontSize = 11.sp) },
              modifier = Modifier.fillMaxWidth(),
              shape = RoundedCornerShape(10.dp),
              colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = BrandPrimary, unfocusedBorderColor = SurfaceCardBorder, focusedTextColor = TextPrimary)
            )

            OutlinedTextField(
              value = cardNumber,
              onValueChange = { cardNumber = it },
              label = { Text("Card Number (Stripe Test Cards)", color = TextMuted, fontSize = 11.sp) },
              leadingIcon = { Icon(imageVector = Icons.Default.CreditCard, contentDescription = null, tint = BrandPurple) },
              modifier = Modifier.fillMaxWidth(),
              shape = RoundedCornerShape(10.dp),
              colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = BrandPrimary, unfocusedBorderColor = SurfaceCardBorder, focusedTextColor = TextPrimary)
            )

            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
              OutlinedTextField(
                value = expiryDate,
                onValueChange = { expiryDate = it },
                label = { Text("MM/YY", color = TextMuted, fontSize = 11.sp) },
                modifier = Modifier.weight(1f),
                shape = RoundedCornerShape(10.dp),
                colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = BrandPrimary, unfocusedBorderColor = SurfaceCardBorder, focusedTextColor = TextPrimary)
              )

              OutlinedTextField(
                value = cvcCode,
                onValueChange = { cvcCode = it },
                label = { Text("CVC", color = TextMuted, fontSize = 11.sp) },
                modifier = Modifier.weight(1f),
                shape = RoundedCornerShape(10.dp),
                colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = BrandPrimary, unfocusedBorderColor = SurfaceCardBorder, focusedTextColor = TextPrimary)
              )

              OutlinedTextField(
                value = postalCode,
                onValueChange = { postalCode = it },
                label = { Text("ZIP Code", color = TextMuted, fontSize = 11.sp) },
                modifier = Modifier.weight(1f),
                shape = RoundedCornerShape(10.dp),
                colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = BrandPrimary, unfocusedBorderColor = SurfaceCardBorder, focusedTextColor = TextPrimary)
              )
            }

            // Promo Coupon Code
            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.spacedBy(8.dp),
              verticalAlignment = Alignment.CenterVertically
            ) {
              OutlinedTextField(
                value = promoCode,
                onValueChange = { promoCode = it },
                placeholder = { Text("Promo Code (e.g. PRO2026)", color = TextMuted, fontSize = 11.sp) },
                modifier = Modifier.weight(1f),
                shape = RoundedCornerShape(10.dp),
                colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = BrandPrimary, unfocusedBorderColor = SurfaceCardBorder, focusedTextColor = TextPrimary)
              )

              Button(
                onClick = {
                  if (promoCode.trim().equals("PRO2026", ignoreCase = true)) {
                    discountApplied = true
                    Toast.makeText(context, "20% Promo Discount Applied!", Toast.LENGTH_SHORT).show()
                  } else {
                    Toast.makeText(context, "Invalid Coupon Code", Toast.LENGTH_SHORT).show()
                  }
                },
                shape = RoundedCornerShape(10.dp),
                colors = ButtonDefaults.buttonColors(containerColor = BrandPurple)
              ) {
                Text("Apply", fontSize = 12.sp, fontWeight = FontWeight.Bold)
              }
            }

            if (discountApplied) {
              Row(
                modifier = Modifier
                  .fillMaxWidth()
                  .background(BrandAccent.copy(alpha = 0.15f), RoundedCornerShape(8.dp))
                  .padding(8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp)
              ) {
                Icon(imageVector = Icons.Default.Check, contentDescription = null, tint = BrandAccent, modifier = Modifier.size(16.dp))
                Text(text = "20% Off Discount Active!", color = BrandAccent, fontSize = 11.sp, fontWeight = FontWeight.Bold)
              }
            }

            // Stripe Pay Button
            Button(
              onClick = {
                isProcessing = true
                onSelectPlan(selectedPlan)
                Toast.makeText(context, "Stripe Payment Authorized! Welcome to ${selectedPlan.displayName}", Toast.LENGTH_LONG).show()
                onDismiss()
              },
              modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
                .testTag("stripe_pay_confirm_button"),
              shape = RoundedCornerShape(12.dp),
              colors = ButtonDefaults.buttonColors(containerColor = BrandPrimary),
              enabled = !isProcessing
            ) {
              if (isProcessing) {
                CircularProgressIndicator(modifier = Modifier.size(20.dp), color = Color.White)
              } else {
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                  Icon(imageVector = Icons.Default.Security, contentDescription = null, modifier = Modifier.size(18.dp))
                  Text(
                    text = "Pay \$" + (if (discountApplied) (if (isAnnual) selectedPlan.yearlyPrice * 0.8 else selectedPlan.monthlyPrice * 0.8) else (if (isAnnual) selectedPlan.yearlyPrice else selectedPlan.monthlyPrice)) + " via Stripe",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold
                  )
                }
              }
            }

            Text(
              text = "Powered by Stripe Payments. You can cancel your subscription at any time from your Account Settings.",
              color = TextMuted,
              fontSize = 10.sp,
              modifier = Modifier.padding(top = 4.dp)
            )
          }
        }
      }
    }
  }
}
