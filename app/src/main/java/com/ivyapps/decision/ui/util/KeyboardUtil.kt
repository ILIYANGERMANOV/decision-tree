package com.ivyapps.decision.ui.util

import android.view.View
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.unit.Dp
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.doOnLayout

@Composable
fun animatedKeyboardPadding(): Dp {
    val systemBottomPadding = systemPaddingBottom()
    val keyboardShown by keyboardShownState()
    val keyboardShownInset = keyboardPaddingBottom()
    return animateDpAsState(
        targetValue = if (keyboardShown)
            keyboardShownInset else systemBottomPadding,
    ).value
}

// region Insets
/**
 * @return system's bottom inset (nav buttons or bottom nav)
 */
@Composable
private fun systemPaddingBottom(): Dp {
    val rootView = LocalView.current
    val densityScope = LocalDensity.current
    return remember(rootView) {
        val insetPx =
            WindowInsetsCompat.toWindowInsetsCompat(rootView.rootWindowInsets, rootView)
                .getInsets(WindowInsetsCompat.Type.navigationBars())
                .bottom
        with(densityScope) { insetPx.toDp() }
    }
}

@Composable
private fun keyboardPaddingBottom(): Dp {
    val rootView = LocalView.current
    val insetPx =
        WindowInsetsCompat.toWindowInsetsCompat(rootView.rootWindowInsets, rootView)
            .getInsets(
                WindowInsetsCompat.Type.ime() or
                        WindowInsetsCompat.Type.navigationBars()
            )
            .bottom
    return with(LocalDensity.current) { insetPx.toDp() }
}
// endregion

// region Keyboard shown
@Composable
fun keyboardShownState(): MutableState<Boolean> {
    val keyboardOpen = remember { mutableStateOf(false) }
    val rootView = LocalView.current

    DisposableEffect(Unit) {
        val keyboardListener = {
            // check keyboard state after this layout
            val isOpenNew = isKeyboardOpen(rootView)

            // since the observer is hit quite often, only callback when there is a change.
            if (isOpenNew != keyboardOpen.value) {
                keyboardOpen.value = isOpenNew
            }
        }

        rootView.doOnLayout {
            // get initial state of keyboard
            keyboardOpen.value = isKeyboardOpen(rootView)

            // whenever the layout resizes/changes, callback with the state of the keyboard.
            rootView.viewTreeObserver.addOnGlobalLayoutListener(keyboardListener)
        }

        onDispose {
            // stop keyboard updates
            rootView.viewTreeObserver.removeOnGlobalLayoutListener(keyboardListener)
        }
    }

    return keyboardOpen
}

fun isKeyboardOpen(rootView: View): Boolean {
    return try {
        WindowInsetsCompat.toWindowInsetsCompat(rootView.rootWindowInsets, rootView)
            .isVisible(WindowInsetsCompat.Type.ime())
    } catch (e: Exception) {
        e.printStackTrace()
        false
    }
}
// endregion
