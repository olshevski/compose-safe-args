package dev.olshevski.safeargs.processor

import com.google.devtools.ksp.symbol.KSType

/**
 * Base class for internal tree structure representation of all info required for generating output.
 */
sealed class Node(val name: String)

/**
 * A single route data.
 */
class RouteNode(
    name: String,
    val returnType: KSType,
    val parameters: List<Parameter>
) : Node(name) {
    data class Parameter(val name: String, val type: KSType)
}

/**
 * Group of nested [Nodes][Node]. Note that this class is in itself a Node, meaning it may contain
 * other nested GroupNodes.
 */
class GroupNode(
    name: String,
    val interfaceType: KSType,
    val childNodes: List<Node>
) : Node(name)

/**
 * Denotes that a route and a group of routes with same name will become a single generated entity.
 */
class MergedNode private constructor(
    val routeNode: RouteNode,
    val groupNode: GroupNode
) : Node(routeNode.name) {
    companion object {
        fun create(routeNode: RouteNode, groupNode: GroupNode): MergedNode {
            check(routeNode.name == groupNode.name) {
                "Merged RouteNode and GroupNode names must be the same"
            }
            return MergedNode(routeNode, groupNode)
        }
    }
}

fun RouteNode.partitionParameters() = parameters.partition { !it.type.isMarkedNullable }

val RouteNode.Parameter.capitalizedName get() = name.replaceFirstChar { it.uppercase() }