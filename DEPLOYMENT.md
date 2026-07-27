# Resume Studio - Production Deployment & Hosting Manual

Welcome to the **Resume Studio** production deployment guide. This document provides step-by-step instructions for deploying to **Firebase Hosting**, **Vercel**, configuring **Cloud Firestore Security Rules & Indexes**, setting up **Android App Links**, and optimizing production security and performance.

---

## 🚀 Quick Deployment Options

### Option 1: Firebase Hosting (Recommended)

Firebase Hosting provides zero-configuration SSL, global CDN edge caching, and seamless integration with Cloud Firestore and Firebase Authentication.

#### 1. Install Firebase CLI
```bash
npm install -g firebase-tools
firebase login
```

#### 2. Initialize & Select Project
```bash
firebase use --add resume-studio-prod
```

#### 3. Deploy Firestore Rules, Indexes & Hosting Assets
```bash
# Deploy security rules and indexes
firebase deploy --only firestore

# Deploy web assets (sitemap, robots.txt, assetlinks.json)
firebase deploy --only hosting
```

---

### Option 2: Vercel Deployment

Vercel provides edge network deployment for asset links, SEO sitemaps, and web previews.

#### 1. Install Vercel CLI
```bash
npm install -g vercel
vercel login
```

#### 2. Deploy to Production
```bash
vercel --prod
```

---

## 🔒 Security & Performance Optimizations

### 1. HTTP Security Headers (Configured in `firebase.json` & `vercel.json`)
- **Strict-Transport-Security (HSTS):** `max-age=31536000; includeSubDomains; preload`
- **X-Content-Type-Options:** `nosniff` (Prevents MIME-sniffing attacks)
- **X-Frame-Options:** `DENY` (Prevents clickjacking)
- **Content Security Policy (CSP):** Restricts script and asset origins to trusted domains.

### 2. Android App Links Verification (`assetlinks.json`)
Located at `app/src/main/assets/assetlinks.json`:
- Domain: `https://resumestudio.app/.well-known/assetlinks.json`
- Allows seamless opening of web URLs directly in the Resume Studio Android app without browser prompts.

---

## 📦 Production Android Build & Signing

### Build Release APK / App Bundle (AAB)
```bash
# Generate signed Android App Bundle (AAB) for Google Play
gradle :app:bundleRelease

# Generate Universal Release APK
gradle :app:assembleRelease
```

---

## 🛠 Environment Variables & Secrets Setup
In Google AI Studio or Production CI/CD pipelines, set:
- `OPENAI_API_KEY`: Secrets panel -> `OPENAI_API_KEY`
- `GEMINI_API_KEY`: Secrets panel -> `GEMINI_API_KEY`
- `STRIPE_PUBLISHABLE_KEY`: Secrets panel -> `STRIPE_PUBLISHABLE_KEY`
