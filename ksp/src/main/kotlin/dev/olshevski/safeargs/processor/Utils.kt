package dev.olshevski.safeargs.processor

import com.google.devtools.ksp.symbol.KSNode
import com.google.devtools.ksp.symbol.NonExistLocation
import com.squareup.kotlinpoet.CodeBlock

fun String.singleLine(separator: String = " ") = lineSequence()
    .map { it.trim() }
    .filter { it.isNotBlank() }
    .joinToString(separator)

fun CodeBlock.Builder.addLine(format: String, vararg args: Any?) {
    add(format, *args)
    add("\n")
}

tailrec fun closestNodeWithExistentLocation(node: KSNode): KSNode {
    val parent = node.parent
    return if (node.location !is NonExistLocation || parent == null) {
        node
    } else {
        closestNodeWithExistentLocation(parent)
    }
}
