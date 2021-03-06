package com.student.techapp.ui

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.student.techapp.R
import com.student.techapp.databinding.FragmentPdfViewBinding


class PdfViewFragment : Fragment(R.layout.fragment_pdf_view) {

    private lateinit var binding: FragmentPdfViewBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentPdfViewBinding.bind(view)
        checkPdfAction()

    }

    private fun checkPdfAction() {

        showPdfFromAssets(com.student.techapp.utils.FileUtils.getPdfNameFromAssets())

    }


    private fun showPdfFromAssets(pdfName: String) {

        binding.pdfView.fromAsset(pdfName)
            .password(null) // if password protected, then write password
            .defaultPage(0) // set the default page to open
            .onPageError { page, _ ->
                Toast.makeText(
                    context,
                    "Error at page: $page", Toast.LENGTH_LONG
                ).show()
            }
            .load()
    }
}