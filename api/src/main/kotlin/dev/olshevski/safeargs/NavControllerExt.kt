package dev.olshevski.safeargs

import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import androidx.navigation.NavDestination

/**
 * Gets the topmost [NavBackStackEntry] for a [Route].
 *
 * This is always safe to use with [the current destination][currentDestination] or
 * [its parent][NavDestination.parent] or grandparent navigation graphs as these
 * destinations are guaranteed to be on the back stack.
 *
 * @param route route of a destination that exists on the back stack
 * @throws IllegalArgumentException if the destination is not on the back stack
 */
fun NavController.getBackStackEntry(route: Route<*>) = getBackStackEntry(route.pattern)