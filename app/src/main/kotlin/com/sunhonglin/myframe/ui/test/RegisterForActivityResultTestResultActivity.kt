package com.sunhonglin.myframe.ui.test

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContract
import com.sunhonglin.base.activity.DefaultToolbarActivity
import com.sunhonglin.base.databinding.ActivityBaseContentBinding
import com.sunhonglin.core.util.setDebounceOnClickListener
import com.sunhonglin.myframe.databinding.ActivityRegisterForActivityResult2Binding

class RegisterForActivityResultTestResultActivity : DefaultToolbarActivity() {
    private lateinit var binding: ActivityRegisterForActivityResult2Binding
    override fun inflateContent(
        parentBinding: ActivityBaseContentBinding,
        savedInstanceState: Bundle?
    ) {
        binding =
            ActivityRegisterForActivityResult2Binding.inflate(
                layoutInflater,
                parentBinding.content,
                true
            )

        binding.tvSetResult.setDebounceOnClickListener {
            setResult(RESULT_OK, Intent().putExtra("KEY_RESULT", "SUCCESS "))
            finish()
        }
    }

    class Contract : ActivityResultContract<String, String>() {
        override fun createIntent(context: Context, input: String?): Intent =
            Intent(context, RegisterForActivityResultTestResultActivity::class.java).apply {
                putExtra("KEY_NAME", input + "Message from main activity ")
            }

        override fun parseResult(resultCode: Int, intent: Intent?): String {
            return if (resultCode == RESULT_OK) {
                intent?.getStringExtra("KEY_RESULT") + "Data has been updated successfully"
            } else {
                intent?.getStringExtra("KEY_RESULT") + "Failed to update data"
            }
        }
    }
}