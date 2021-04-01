package dev.ebnbin.ebui

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.lifecycle.ViewTreeLifecycleOwner
import androidx.lifecycle.ViewTreeViewModelStoreOwner
import androidx.savedstate.ViewTreeSavedStateRegistryOwner

inline fun <reified T : Fragment> Activity.openFragment(
    fragmentIsView: Boolean = true,
    fragmentArgs: Bundle = Bundle.EMPTY,
    fragmentTag: String? = null,
    activityClass: Class<out FragmentWrapperActivity> = FragmentWrapperActivity::class.java,
    activityIntent: Intent = Intent(),
) {
    startActivity(
        FragmentWrapperActivity.createIntent(
            context = this,
            fragmentClass = T::class.java,
            fragmentIsView,
            fragmentArgs,
            fragmentTag,
            activityClass,
            activityIntent,
        ),
    )
}

inline fun <reified T : Fragment> Fragment.openFragment(
    fragmentIsView: Boolean = true,
    fragmentArgs: Bundle = Bundle.EMPTY,
    fragmentTag: String? = null,
    activityClass: Class<out FragmentWrapperActivity> = FragmentWrapperActivity::class.java,
    activityIntent: Intent = Intent(),
) {
    startActivity(
        FragmentWrapperActivity.createIntent(
            context = requireContext(),
            fragmentClass = T::class.java,
            fragmentIsView,
            fragmentArgs,
            fragmentTag,
            activityClass,
            activityIntent,
        ),
    )
}

open class FragmentWrapperActivity : AppCompatActivity() {
    protected open val fragmentClass: Class<out Fragment>
        get() = requireExtra(KEY_FRAGMENT_CLASS)

    protected open val fragmentIsView: Boolean
        get() = getExtraOrDefault(KEY_FRAGMENT_IS_VIEW, true)

    protected open val fragmentArgs: Bundle
        get() = getExtraOrDefault(KEY_FRAGMENT_ARGS, Bundle.EMPTY)

    protected open val fragmentTag: String?
        get() = getExtra(KEY_FRAGMENT_TAG)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setOwners()
        if (savedInstanceState == null) {
            supportFragmentManager.commit(allowStateLoss = true) {
                val containerViewId = if (fragmentIsView) android.R.id.content else 0
                add(containerViewId, fragmentClass, fragmentArgs, fragmentTag)
            }
        }
    }

    private fun setOwners() {
        val decorView = window.decorView
        if (ViewTreeLifecycleOwner.get(decorView) == null) {
            ViewTreeLifecycleOwner.set(decorView, this)
        }
        if (ViewTreeViewModelStoreOwner.get(decorView) == null) {
            ViewTreeViewModelStoreOwner.set(decorView, this)
        }
        if (ViewTreeSavedStateRegistryOwner.get(decorView) == null) {
            ViewTreeSavedStateRegistryOwner.set(decorView, this)
        }
    }

    companion object {
        private const val KEY_FRAGMENT_CLASS = "fragment_class"
        private const val KEY_FRAGMENT_IS_VIEW = "fragment_is_view"
        private const val KEY_FRAGMENT_ARGS = "fragment_args"
        private const val KEY_FRAGMENT_TAG = "fragment_tag"

        fun createIntent(
            context: Context,
            fragmentClass: Class<out Fragment>,
            fragmentIsView: Boolean = true,
            fragmentArgs: Bundle = Bundle.EMPTY,
            fragmentTag: String? = null,
            activityClass: Class<out FragmentWrapperActivity> = FragmentWrapperActivity::class.java,
            activityIntent: Intent = Intent(),
        ): Intent {
            return activityIntent
                .setClass(context, activityClass)
                .putExtras(
                    bundleOf(
                        KEY_FRAGMENT_CLASS to fragmentClass,
                        KEY_FRAGMENT_IS_VIEW to fragmentIsView,
                        KEY_FRAGMENT_ARGS to fragmentArgs,
                        KEY_FRAGMENT_TAG to fragmentTag,
                    ),
                )
        }
    }
}
