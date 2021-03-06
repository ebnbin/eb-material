package dev.ebnbin.ebui

import android.os.Bundle
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.FragmentManager
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import dev.ebnbin.eb.layoutInflater
import dev.ebnbin.ebui.databinding.EbuiLogDialogViewBinding

fun FragmentManager.openLogDialog(
    title: CharSequence? = null,
    log: CharSequence,
    logHorizontalScrollable: Boolean = true,
    dialogCancelable: DialogCancelable = DialogCancelable.CANCELABLE,
    fragmentTag: String? = null,
) {
    openAlertDialog(
        fragmentClass = LogDialogFragment::class.java,
        title = title,
        dialogCancelable = dialogCancelable,
        fragmentArgs = bundleOf(
            KEY_LOG to log,
            KEY_LOG_HORIZONTAL_SCROLLABLE to logHorizontalScrollable,
        ),
        fragmentTag = fragmentTag,
    )
}

private const val KEY_LOG = "log"
private const val KEY_LOG_HORIZONTAL_SCROLLABLE = "log_horizontal_scrollable"

internal class LogDialogFragment : AlertDialogFragment() {
    override val positiveText: CharSequence?
        get() = requireContext().getText(android.R.string.ok)

    private val log: CharSequence
        get() = requireArg(KEY_LOG)

    /**
     * 是否支持水平滚动.
     */
    private val logHorizontalScrollable: Boolean
        get() = requireArg(KEY_LOG_HORIZONTAL_SCROLLABLE)

    override fun onAlertDialogBuilderCreated(builder: MaterialAlertDialogBuilder, savedInstanceState: Bundle?) {
        super.onAlertDialogBuilderCreated(builder, savedInstanceState)
        val binding = EbuiLogDialogViewBinding.inflate(requireContext().layoutInflater)
        if (logHorizontalScrollable) {
            binding.ebuiLog.isVisible = false
            binding.ebuiHorizontalScrollView.isVisible = true
            binding.ebuiHorizontalScrollableLog.text = log
        } else {
            binding.ebuiLog.isVisible = true
            binding.ebuiHorizontalScrollView.isVisible = false
            binding.ebuiLog.text = log
        }
        builder.setView(binding.root)
    }
}
