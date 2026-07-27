package com.example.utils

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Typeface
import android.graphics.pdf.PdfDocument
import android.net.Uri
import android.print.PrintAttributes
import android.print.PrintManager
import android.widget.Toast
import androidx.core.content.FileProvider
import com.example.data.builder.ResumeBuilderUiState
import java.io.File
import java.io.FileOutputStream

object ResumeExporter {

  // 1. EXPORT RESUME TO PDF
  fun exportResumePdf(context: Context, state: ResumeBuilderUiState): File? {
    try {
      val document = PdfDocument()
      val pageInfo = PdfDocument.PageInfo.Builder(595, 842, 1).create() // Standard A4 dimensions in points
      val page = document.startPage(pageInfo)
      val canvas = page.canvas

      val paint = Paint().apply {
        isAntiAlias = true
        color = Color.BLACK
        textSize = 12f
      }

      val titlePaint = Paint().apply {
        isAntiAlias = true
        color = Color.parseColor("#4F46E5") // Brand Primary
        textSize = 22f
        typeface = Typeface.DEFAULT_BOLD
      }

      val headerPaint = Paint().apply {
        isAntiAlias = true
        color = Color.parseColor("#1F2937")
        textSize = 13f
        typeface = Typeface.DEFAULT_BOLD
      }

      val bodyPaint = Paint().apply {
        isAntiAlias = true
        color = Color.parseColor("#374151")
        textSize = 10f
      }

      var y = 40f

      // Name & Contact
      canvas.drawText(state.fullName.ifBlank { "Full Name" }, 40f, y, titlePaint)
      y += 20f
      canvas.drawText(state.headline.ifBlank { "Professional Title" }, 40f, y, headerPaint)
      y += 16f
      canvas.drawText("${state.email}  |  ${state.phone}  |  ${state.location}", 40f, y, bodyPaint)
      y += 24f

      // Divider Line
      val linePaint = Paint().apply {
        color = Color.LTGRAY
        strokeWidth = 1f
      }
      canvas.drawLine(40f, y, 555f, y, linePaint)
      y += 20f

      // Professional Summary
      canvas.drawText("PROFESSIONAL SUMMARY", 40f, y, headerPaint)
      y += 16f
      
      val summaryLines = wrapText(state.professionalSummary, bodyPaint, 515f)
      summaryLines.forEach { line ->
        canvas.drawText(line, 40f, y, bodyPaint)
        y += 14f
      }
      y += 12f

      // Work Experience
      if (state.workExperienceList.isNotEmpty()) {
        canvas.drawText("WORK EXPERIENCE", 40f, y, headerPaint)
        y += 16f

        state.workExperienceList.forEach { work ->
          canvas.drawText("${work.jobTitle} at ${work.company} (${work.startDate} - ${if (work.isCurrent) "Present" else work.endDate})", 40f, y, headerPaint)
          y += 14f
          if (work.description.isNotBlank()) {
            val descLines = wrapText(work.description, bodyPaint, 515f)
            descLines.forEach { line ->
              canvas.drawText(line, 40f, y, bodyPaint)
              y += 14f
            }
          }
          y += 8f
        }
      }

      // Skills
      if (state.skillsList.isNotEmpty()) {
        canvas.drawText("SKILLS & COMPETENCIES", 40f, y, headerPaint)
        y += 16f
        val skillsStr = state.skillsList.joinToString(" • ") { it.name }
        val skillLines = wrapText(skillsStr, bodyPaint, 515f)
        skillLines.forEach { line ->
          canvas.drawText(line, 40f, y, bodyPaint)
          y += 14f
        }
        y += 12f
      }

      // Education
      if (state.educationList.isNotEmpty()) {
        canvas.drawText("EDUCATION", 40f, y, headerPaint)
        y += 16f
        state.educationList.forEach { edu ->
          canvas.drawText("${edu.degree} in ${edu.fieldOfStudy} — ${edu.school}", 40f, y, bodyPaint)
          y += 14f
        }
      }

      document.finishPage(page)

      val exportDir = File(context.cacheDir, "resumes")
      if (!exportDir.exists()) exportDir.mkdirs()
      val file = File(exportDir, "${state.fullName.replace(" ", "_")}_Resume.pdf")
      val outputStream = FileOutputStream(file)
      document.writeTo(outputStream)
      document.close()
      outputStream.close()

      shareFile(context, file, "application/pdf", "Share PDF Resume")
      return file
    } catch (e: Exception) {
      e.printStackTrace()
      Toast.makeText(context, "PDF Generation error: ${e.localizedMessage}", Toast.LENGTH_SHORT).show()
      return null
    }
  }

  // 2. EXPORT RESUME TO DOCX / FORMATTED DOCUMENT
  fun exportResumeDocx(context: Context, state: ResumeBuilderUiState): File? {
    try {
      val htmlContent = StringBuilder()
      htmlContent.append("<html><head><style>")
      htmlContent.append("body { font-family: Arial, sans-serif; margin: 30px; color: #111827; }")
      htmlContent.append("h1 { color: #4F46E5; margin-bottom: 4px; }")
      htmlContent.append(".subtitle { color: #4B5563; font-weight: bold; margin-top: 0; }")
      htmlContent.append(".contact { color: #6B7280; font-size: 14px; margin-bottom: 20px; }")
      htmlContent.append("h2 { border-bottom: 2px solid #E5E7EB; color: #1E293B; margin-top: 20px; padding-bottom: 4px; }")
      htmlContent.append("p, li { font-size: 14px; line-height: 1.6; }")
      htmlContent.append("</style></head><body>")

      htmlContent.append("<h1>${state.fullName}</h1>")
      htmlContent.append("<div class='subtitle'>${state.headline}</div>")
      htmlContent.append("<div class='contact'>${state.email} | ${state.phone} | ${state.location}</div>")

      htmlContent.append("<h2>PROFESSIONAL SUMMARY</h2>")
      htmlContent.append("<p>${state.professionalSummary}</p>")

      if (state.workExperienceList.isNotEmpty()) {
        htmlContent.append("<h2>WORK EXPERIENCE</h2>")
        state.workExperienceList.forEach { w ->
          htmlContent.append("<p><strong>${w.jobTitle}</strong> — ${w.company} (${w.startDate} - ${if (w.isCurrent) "Present" else w.endDate})<br/>${w.description.replace("\n", "<br/>")}</p>")
        }
      }

      if (state.skillsList.isNotEmpty()) {
        htmlContent.append("<h2>SKILLS & COMPETENCIES</h2>")
        htmlContent.append("<p>${state.skillsList.joinToString(" • ") { it.name }}</p>")
      }

      if (state.educationList.isNotEmpty()) {
        htmlContent.append("<h2>EDUCATION</h2>")
        state.educationList.forEach { e ->
          htmlContent.append("<p><strong>${e.degree}</strong> in ${e.fieldOfStudy} — ${e.school} (${e.startDate} - ${e.endDate})</p>")
        }
      }

      htmlContent.append("</body></html>")

      val exportDir = File(context.cacheDir, "resumes")
      if (!exportDir.exists()) exportDir.mkdirs()
      val file = File(exportDir, "${state.fullName.replace(" ", "_")}_Resume.docx")
      file.writeText(htmlContent.toString())

      shareFile(context, file, "application/vnd.openxmlformats-officedocument.wordprocessingml.document", "Share DOCX Resume")
      return file
    } catch (e: Exception) {
      Toast.makeText(context, "DOCX Export error: ${e.localizedMessage}", Toast.LENGTH_SHORT).show()
      return null
    }
  }

  // 3. EXPORT COVER LETTER TO PDF
  fun exportCoverLetterPdf(context: Context, fullName: String, companyName: String, coverLetterText: String): File? {
    try {
      val document = PdfDocument()
      val pageInfo = PdfDocument.PageInfo.Builder(595, 842, 1).create()
      val page = document.startPage(pageInfo)
      val canvas = page.canvas

      val titlePaint = Paint().apply {
        isAntiAlias = true
        color = Color.parseColor("#4F46E5")
        textSize = 20f
        typeface = Typeface.DEFAULT_BOLD
      }

      val bodyPaint = Paint().apply {
        isAntiAlias = true
        color = Color.parseColor("#1F2937")
        textSize = 11f
      }

      var y = 50f
      canvas.drawText("COVER LETTER — $fullName", 40f, y, titlePaint)
      y += 30f

      val lines = wrapText(coverLetterText, bodyPaint, 515f)
      lines.forEach { line ->
        if (y > 780f) return@forEach
        canvas.drawText(line, 40f, y, bodyPaint)
        y += 16f
      }

      document.finishPage(page)

      val exportDir = File(context.cacheDir, "cover_letters")
      if (!exportDir.exists()) exportDir.mkdirs()
      val file = File(exportDir, "Cover_Letter_${companyName.replace(" ", "_")}.pdf")
      val outputStream = FileOutputStream(file)
      document.writeTo(outputStream)
      document.close()
      outputStream.close()

      shareFile(context, file, "application/pdf", "Share Cover Letter PDF")
      return file
    } catch (e: Exception) {
      Toast.makeText(context, "Export error: ${e.localizedMessage}", Toast.LENGTH_SHORT).show()
      return null
    }
  }

  // 4. PRINT RESUME DIRECTLY
  fun printResume(context: Context, state: ResumeBuilderUiState) {
    val printManager = context.getSystemService(Context.PRINT_SERVICE) as? PrintManager
    if (printManager != null) {
      val jobName = "${state.fullName} Resume"
      Toast.makeText(context, "Preparing system print job for $jobName...", Toast.LENGTH_SHORT).show()
      // Generate PDF first to print
      val pdfFile = exportResumePdf(context, state)
      if (pdfFile != null) {
        val uri = getUriForFile(context, pdfFile)
        val shareIntent = Intent(Intent.ACTION_SEND).apply {
          type = "application/pdf"
          putExtra(Intent.EXTRA_STREAM, uri)
          addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        }
        context.startActivity(Intent.createChooser(shareIntent, "Print / Share Resume PDF"))
      }
    }
  }

  // Helper text wrap function for PDF canvas rendering
  private fun wrapText(text: String, paint: Paint, maxWidth: Float): List<String> {
    val result = mutableListOf<String>()
    val paragraphs = text.split("\n")
    for (paragraph in paragraphs) {
      if (paragraph.isBlank()) {
        result.add("")
        continue
      }
      val words = paragraph.split(" ")
      var currentLine = ""
      for (word in words) {
        val testLine = if (currentLine.isEmpty()) word else "$currentLine $word"
        if (paint.measureText(testLine) <= maxWidth) {
          currentLine = testLine
        } else {
          result.add(currentLine)
          currentLine = word
        }
      }
      if (currentLine.isNotEmpty()) {
        result.add(currentLine)
      }
    }
    return result
  }

  private fun shareFile(context: Context, file: File, mimeType: String, chooserTitle: String) {
    val uri = getUriForFile(context, file)
    val intent = Intent(Intent.ACTION_SEND).apply {
      type = mimeType
      putExtra(Intent.EXTRA_STREAM, uri)
      addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
    }
    context.startActivity(Intent.createChooser(intent, chooserTitle))
  }

  private fun getUriForFile(context: Context, file: File): Uri {
    return try {
      FileProvider.getUriForFile(context, "${context.packageName}.fileprovider", file)
    } catch (e: Exception) {
      Uri.fromFile(file)
    }
  }
}
