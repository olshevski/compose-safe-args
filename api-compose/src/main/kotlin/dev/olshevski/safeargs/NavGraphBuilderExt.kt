package dev.olshevski.safeargs

import android.os.Bundle
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.window.DialogProperties
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavDeepLink
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.compose.dialog
import androidx.navigation.compose.navigation

/**
 * Construct a nested [NavGraph].
 *
 * @param startDestination the starting destination's route for this NavGraph
 * @param route the destination's unique [Route]
 * @param deepLinks list of deep links to associate with the destinations
 * @param builder the builder used to construct the graph
 */
fun NavGraphBuilder.navigation(
    startDestination: String,
    route: Route<*>,
    deepLinks: List<NavDeepLink> = emptyList(),
    builder: NavGraphBuilder.() -> Unit
) = navigation(
    startDestination = startDestination,
    route = route.pattern,
    arguments = route.namedNavArguments,
    deepLinks = deepLinks,
    builder = builder
)

/**
 * Add the [Composable] to the [NavGraphBuilder]
 *
 * @param route [Route] for the destination
 * @param deepLinks list of deep links to associate with the destinations
 * @param content composable for the destination
 */
fun NavGraphBuilder.composable(
    route: Route<*>,
    deepLinks: List<NavDeepLink> = emptyList(),
    content: @Composable (NavBackStackEntry) -> Unit
) = composable(
    route = route.pattern,
    arguments = route.namedNavArguments,
    deepLinks = deepLinks,
    content = content
)

/**
 * Add the [Composable] to the [NavGraphBuilder] that will be hosted within a
 * [androidx.compose.ui.window.Dialog]
 *
 * @param route [Route] for the destination
 * @param deepLinks list of deep links to associate with the destinations
 * @param dialogProperties properties that should be passed to [androidx.compose.ui.window.Dialog].
 * @param content composable content for the destination that will be hosted within the Dialog
 */
fun NavGraphBuilder.dialog(
    route: Route<*>,
    deepLinks: List<NavDeepLink> = emptyList(),
    dialogProperties: DialogProperties = DialogProperties(),
    content: @Composable (NavBackStackEntry) -> Unit
) = dialog(
    route = route.pattern,
    arguments = route.namedNavArguments,
    deepLinks = deepLinks,
    dialogProperties = dialogProperties,
    content = content
)

/**
 * Add the [Composable] to the [NavGraphBuilder]. The Composable will receive [Route]'s
 * arguments data class as a lambda parameter in [content].
 *
 * @param route [Route] for the destination
 * @param deepLinks list of deep links to associate with the destinations
 * @param content composable for the destination
 */
fun <ArgsT> NavGraphBuilder.composableWithArgs(
    route: Route<ArgsT>,
    deepLinks: List<NavDeepLink> = emptyList(),
    content: @Composable (ArgsT) -> Unit
) = composable(
    route = route.pattern,
    arguments = route.namedNavArguments,
    deepLinks = deepLinks,
    content = adaptContent(route, content)
)

/**
 * Add the [Composable] to the [NavGraphBuilder] that will be hosted within a
 * [androidx.compose.ui.window.Dialog].  The Composable will receive [Route]'s
 * arguments data class as a lambda parameter in [content].
 *
 * @param route [Route] for the destination
 * @param deepLinks list of deep links to associate with the destinations
 * @param dialogProperties properties that should be passed to [androidx.compose.ui.window.Dialog].
 * @param content composable content for the destination that will be hosted within the Dialog
 */
fun <ArgsT> NavGraphBuilder.dialogWithArgs(
    route: Route<ArgsT>,
    deepLinks: List<NavDeepLink> = emptyList(),
    dialogProperties: DialogProperties = DialogProperties(),
    content: @Composable (ArgsT) -> Unit
) = dialog(
    route = route.pattern,
    arguments = route.namedNavArguments,
    deepLinks = deepLinks,
    dialogProperties = dialogProperties,
    content = adaptContent(route, content)
)

private inline fun <ArgsT> adaptContent(
    route: Route<ArgsT>,
    crossinline content: @Composable (ArgsT) -> Unit
): @Composable (NavBackStackEntry) -> Unit = {
    val arguments = it.arguments ?: Bundle.EMPTY
    content(remember(arguments) { route.argsFrom(arguments) })
}