package dev.olshevski.safeargs

import android.os.Bundle
import androidx.lifecycle.SavedStateHandle
import androidx.navigation.NamedNavArgument
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavGraphBuilder

/**
 * A single navigation route that contains all info needed for route declaration and for reading
 * arguments from [Bundle] or [SavedStateHandle].
 */
abstract class Route<ArgsT>(

    /**
     * Route pattern with argument placeholders in the form
     * `routeName/{requiredArgument}?optionalArgument={optionalArgument}`
     *
     * See [Navigate with arguments](https://developer.android.com/jetpack/compose/navigation#nav-with-args)
     * for more information on the routes format.
     */
    val pattern: String,

    /**
     * [NamedNavArguments][NamedNavArgument] that should be used in pair with the [pattern] when
     * declaring a route in [NavGraphBuilder].
     */
    val namedNavArguments: List<NamedNavArgument>
) {
    /**
     * Construct arguments data class from [NavBackStackEntry.arguments].
     */
    abstract fun argsFrom(bundle: Bundle): ArgsT

    /**
     * Construct arguments data class from savedStateHandle in a corresponding ViewModel.
     *
     * Please don't use [NavBackStackEntry.savedStateHandle] as it gets created and populated with
     * values only after ViewModel with SavedStateHandle parameter is acquired.
     */
    abstract fun argsFrom(savedStateHandle: SavedStateHandle): ArgsT
}

/**
 * Routes container. All generated classes marked with [GenerateRoutes] annotation implement this
 * interface.
 */
interface RouteGroup {

    /**
     * Return the list of all declared routes.
     */
    val routes: List<Route<*>>
}

/**
 * Build optional arguments part of a route. This method is used internally in generated classes.
 */
fun buildOptionalArguments(vararg namedArguments: Pair<String, Any?>): String {
    val nonNullNamedArguments = namedArguments.mapNotNull { namedArgument ->
        namedArgument.second?.let { namedArgument.first to it }
    }
    return if (nonNullNamedArguments.isEmpty()) {
        ""
    } else {
        StringBuilder("?").apply {
            nonNullNamedArguments.forEachIndexed { index, namedArgument ->
                if (index > 0) {
                    append('&')
                }
                append(namedArgument.first)
                append('=')
                append(namedArgument.second.toString())
            }
        }.toString()
    }
}
