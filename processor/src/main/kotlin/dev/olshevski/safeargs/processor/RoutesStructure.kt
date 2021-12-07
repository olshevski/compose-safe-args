package dev.olshevski.safeargs.processor

import com.google.devtools.ksp.processing.Dependencies

/**
 * Full structure of a single top-level interface declaration marked with GenerateRoutes annotation.
 *
 * This represents the minimum data required to generate output for this declaration.
 */
class RoutesStructure(
    val packageName: String,
    val dependencies: Dependencies,
    val rootNode: GroupNode
)