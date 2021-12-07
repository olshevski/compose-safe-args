package dev.olshevski.safeargs.processor

import com.google.devtools.ksp.symbol.KSNode
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.contract

class GenerateRoutesException(message: String, val nodes: List<KSNode>) : Exception(message) {
    constructor(message: String, node: KSNode) : this(message, listOf(node))
}

/**
 * Throws a [GenerateRoutesException] with the given [message].
 */
fun error(message: Any, node: KSNode): Nothing =
    throw GenerateRoutesException(message.toString(), node)

/**
 * Throws a [GenerateRoutesException] with the given [message].
 */
fun error(message: Any, nodes: List<KSNode>): Nothing =
    throw GenerateRoutesException(message.toString(), nodes)

/**
 * Throws a [GenerateRoutesException] with the result of calling [lazyMessage] if the [value] is false.
 */
@OptIn(ExperimentalContracts::class)
inline fun check(value: Boolean, node: KSNode, lazyMessage: () -> Any) {
    contract {
        returns() implies value
    }
    if (!value) {
        throw GenerateRoutesException(lazyMessage().toString(), node)
    }
}

/**
 * Throws a [GenerateRoutesException] with the result of calling [lazyMessage] if the [value] is false.
 */
@OptIn(ExperimentalContracts::class)
inline fun check(value: Boolean, nodes: List<KSNode>, lazyMessage: () -> Any) {
    contract {
        returns() implies value
    }
    if (!value) {
        throw GenerateRoutesException(lazyMessage().toString(), nodes)
    }
}