package com.chkan.bestpractices.luep_translater

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import com.chkan.bestpractices.core.BaseFragment
import com.chkan.bestpractices.core.observe
import com.chkan.bestpractices.databinding.FragmentLuepTranslatorBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LuepTranslatorFragment : BaseFragment<FragmentLuepTranslatorBinding>(FragmentLuepTranslatorBinding::inflate) {

    private val viewModel: LuepViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnLogin.setOnClickListener {
            binding.progressBar.isVisible = true
            viewModel.login()
        }
        binding.btnTranslate.setOnClickListener {
            val text = binding.textEditInput.text.toString()
            if (text.isNotEmpty()) {
                viewModel.translate(text)
                binding.progressBar.isVisible = true
            }
        }
        binding.btnCopy.setOnClickListener {
            val text = binding.textOutput.text.toString()
            if (text.isNotEmpty()) {
                val clipboard = requireActivity().getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                val clip: ClipData = ClipData.newPlainText("translate", text)
                clipboard.setPrimaryClip(clip)
                Toast.makeText(context, "Copied",Toast.LENGTH_SHORT).show()
            }
        }
        initViewModel()
    }

    private fun initViewModel() = with(viewModel) {
        observe(error()) {
            it?.let {
                binding.progressBar.isVisible = false
                binding.btnLogin.text = "ERROR"
            }
        }
        observe(token()) {
            it?.let {
                binding.btnLogin.text = "OK"
                binding.btnLogin.isEnabled = false
                binding.progressBar.isVisible = false
            }
        }
        observe(translated()) {
            it?.let { text ->
                binding.progressBar.isVisible = false
                binding.textOutput.text = text
                Log.d("CHKAN", "$text")
            }
        }
    }
}