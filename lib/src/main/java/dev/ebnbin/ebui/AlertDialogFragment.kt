package dev.ebnbin.ebui

import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatDialogFragment
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.commit
import androidx.fragment.app.setFragmentResult
import androidx.lifecycle.LifecycleOwner
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import dev.ebnbin.eb.getValue

fun FragmentManager.openAlertDialog(
    fragmentClass: Class<out AlertDialogFragment> = AlertDialogFragment::class.java,
    title: CharSequence? = null,
    message: CharSequence? = null,
    positiveText: CharSequence? = null,
    negativeText: CharSequence? = null,
    neutralText: CharSequence? = null,
    dialogCancelable: DialogCancelable = DialogCancelable.CANCELABLE,
    fragmentArgs: Bundle = Bundle.EMPTY,
    fragmentTag: String? = null,
    fragmentResultListener: AlertDialogFragment.ResultListener? = null,
) {
    fragmentResultListener?.let {
        setFragmentResultListener(it.requestKey, it.lifecycleOwner) { requestKey, result ->
            if (it.requestKey == requestKey) {
                it.onFragmentResult(result, result.getValue(KEY_RESULT_TYPE))
            }
        }
    }
    commit(allowStateLoss = true) {
        add(
            fragmentClass,
            bundleOf().also {
                it.putAll(fragmentArgs)
                it.putAll(
                    bundleOf(
                        KEY_TITLE to title,
                        KEY_MESSAGE to message,
                        KEY_POSITIVE_TEXT to positiveText,
                        KEY_NEGATIVE_TEXT to negativeText,
                        KEY_NEUTRAL_TEXT to neutralText,
                        KEY_DIALOG_CANCELABLE to dialogCancelable,
                        KEY_REQUEST_KEY to fragmentResultListener?.requestKey,
                    ),
                )
            },
            fragmentTag,
        )
    }
}

private const val KEY_TITLE = "title"
private const val KEY_MESSAGE = "message"
private const val KEY_POSITIVE_TEXT = "positive_text"
private const val KEY_NEGATIVE_TEXT = "negative_text"
private const val KEY_NEUTRAL_TEXT = "neutral_text"
private const val KEY_DIALOG_CANCELABLE = "dialog_cancelable"
private const val KEY_REQUEST_KEY = "request_key"

private const val KEY_RESULT_TYPE = "result_type"

open class AlertDialogFragment : AppCompatDialogFragment() {
    protected open val title: CharSequence?
        get() = getArg(KEY_TITLE)

    protected open val message: CharSequence?
        get() = getArg(KEY_MESSAGE)

    protected open val positiveText: CharSequence?
        get() = getArg(KEY_POSITIVE_TEXT)

    protected open val negativeText: CharSequence?
        get() = getArg(KEY_NEGATIVE_TEXT)

    protected open val neutralText: CharSequence?
        get() = getArg(KEY_NEUTRAL_TEXT)

    protected open val dialogCancelable: DialogCancelable
        get() = getArgOrDefault(KEY_DIALOG_CANCELABLE, DialogCancelable.CANCELABLE)

    protected open val requestKey: String?
        get() = getArg(KEY_REQUEST_KEY)

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = MaterialAlertDialogBuilder(requireContext(), theme)
            .setTitle(title)
            .setMessage(message)
            .setPositiveButton(positiveText, null)
            .setNegativeButton(negativeText, null)
            .setNeutralButton(neutralText, null)
        onAlertDialogBuilderCreated(builder, savedInstanceState)
        val alertDialog = builder.create()
        alertDialog.setCanceledOnTouchOutside(dialogCancelable == DialogCancelable.CANCELABLE)
        alertDialog.setOnShowListener {
            it as AlertDialog
            it.findViewById<View>(R.id.titleDividerNoCustom)?.isVisible = true
            it.getButton(DialogInterface.BUTTON_POSITIVE)?.setOnClickListener { _ ->
                onAlertDialogPositive(it)?.let { result ->
                    setFragmentResult(result, ResultType.POSITIVE)
                    dismissAllowingStateLoss()
                }
            }
            it.getButton(DialogInterface.BUTTON_NEGATIVE)?.setOnClickListener { _ ->
                onAlertDialogNegative(it)?.let { result ->
                    setFragmentResult(result, ResultType.NEGATIVE)
                    dismissAllowingStateLoss()
                }
            }
            it.getButton(DialogInterface.BUTTON_NEUTRAL)?.setOnClickListener { _ ->
                onAlertDialogNeutral(it)?.let { result ->
                    setFragmentResult(result, ResultType.NEUTRAL)
                    dismissAllowingStateLoss()
                }
            }
            onAlertDialogShow(it)
        }
        isCancelable = dialogCancelable != DialogCancelable.NOT_CANCELABLE
        onAlertDialogCreated(alertDialog, savedInstanceState)
        return alertDialog
    }

    override fun onCancel(dialog: DialogInterface) {
        super.onCancel(dialog)
        setFragmentResult(onAlertDialogCancel(dialog as AlertDialog), ResultType.CANCEL)
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        onAlertDialogDismiss(dialog as AlertDialog)
    }

    protected open fun onAlertDialogBuilderCreated(builder: MaterialAlertDialogBuilder, savedInstanceState: Bundle?) {
    }

    protected open fun onAlertDialogCreated(alertDialog: AlertDialog, savedInstanceState: Bundle?) {
    }

    protected open fun onAlertDialogShow(alertDialog: AlertDialog) {
    }

    /**
     * @return 如果点击按钮后不需要关闭对话框则返回 null, 否则返回关闭对话框后要设置给 [setFragmentResult] 的数据,
     * 如果需要关闭对话框且不需要设置数据, 返回 [Bundle.EMPTY].
     */
    protected open fun onAlertDialogPositive(alertDialog: AlertDialog): Bundle? {
        return Bundle.EMPTY
    }

    /**
     * @return 如果点击按钮后不需要关闭对话框则返回 null, 否则返回关闭对话框后要设置给 [setFragmentResult] 的数据,
     * 如果需要关闭对话框且不需要设置数据, 返回 [Bundle.EMPTY].
     */
    protected open fun onAlertDialogNegative(alertDialog: AlertDialog): Bundle? {
        return Bundle.EMPTY
    }

    /**
     * @return 如果点击按钮后不需要关闭对话框则返回 null, 否则返回关闭对话框后要设置给 [setFragmentResult] 的数据,
     * 如果需要关闭对话框且不需要设置数据, 返回 [Bundle.EMPTY].
     */
    protected open fun onAlertDialogNeutral(alertDialog: AlertDialog): Bundle? {
        return Bundle.EMPTY
    }

    /**
     * @return 返回关闭对话框后要设置给 [setFragmentResult] 的数据, 如果不需要设置数据, 返回 [Bundle.EMPTY].
     */
    protected open fun onAlertDialogCancel(alertDialog: AlertDialog): Bundle {
        return Bundle.EMPTY
    }

    protected open fun onAlertDialogDismiss(alertDialog: AlertDialog) {
    }

    private fun setFragmentResult(bundle: Bundle, resultType: ResultType) {
        val requestKey = requestKey ?: return
        val result = bundleOf().also {
            it.putAll(bundle)
            it.putAll(
                bundleOf(
                    KEY_RESULT_TYPE to resultType,
                ),
            )
        }
        setFragmentResult(requestKey, result)
    }

    enum class ResultType {
        POSITIVE,
        NEGATIVE,
        NEUTRAL,
        CANCEL,
    }

    data class ResultListener(
        val requestKey: String,
        val lifecycleOwner: LifecycleOwner,
        val onFragmentResult: (result: Bundle, resultType: ResultType?) -> Unit,
    )
}
