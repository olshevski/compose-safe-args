package dev.olshevski.safeargs.processor

import com.google.devtools.ksp.processing.CodeGenerator
import com.google.devtools.ksp.processing.KSPLogger
import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.processing.SymbolProcessor
import com.google.devtools.ksp.symbol.KSAnnotated
import com.google.devtools.ksp.symbol.KSNode
import com.google.devtools.ksp.validate

class GenerateRoutesProcessor(
    private val codeGenerator: CodeGenerator,
    private val logger: KSPLogger,
    private val options: Map<String, String>
) : SymbolProcessor {

    override fun process(resolver: Resolver): List<KSAnnotated> {
        // All nested annotated symbols will be processed as part of enclosing ones. Filter them.
        val (topmostSymbols, invalidSymbols) = resolver
            .getSymbolsWithAnnotation(GenerateRoutes)
            .filter { !it.hasAnyAnnotatedParent(GenerateRoutes) }
            .partition { it.validate() }

        try {
            // Read all structure reading and validation first, so we can fail faster.
            val routesStructureReader = RoutesStructureReader(logger)
            val routesStructureList =
                topmostSymbols.map { symbol -> routesStructureReader.read(symbol) }
            routesStructureList.forEach { logRoutesStructure(it) }

            val routesGenerator = RoutesGenerator(codeGenerator)
            routesStructureList.forEach { routesGenerator.generateRoutesFile(it) }
        } catch (exception: GenerateRoutesException) {
            logExceptionGracefully(exception)
        } catch (exception: Exception) {
            // for some reason some exception are not printed into the output, handle this manually
            logger.exception(exception)
        }

        return invalidSymbols
    }

    private tailrec fun KSNode.hasAnyAnnotatedParent(annotationName: String): Boolean {
        val parent = this.parent
        return if (parent == null) {
            false
        } else {
            if (parent is KSAnnotated &&
                parent.annotations.any {
                    it.annotationType.resolve().declaration.qualifiedName?.asString() == annotationName
                }
            ) {
                true
            } else {
                parent.hasAnyAnnotatedParent(annotationName)
            }
        }
    }

    private fun logRoutesStructure(routesStructure: RoutesStructure) {
        logger.logging("RoutesStructure(${routesStructure.packageName})")
        logNode(routesStructure.rootNode)
    }

    private fun logNode(node: Node, prefix: String = "") {
        fun log(string: String) {
            logger.logging(prefix + string)
        }

        val increasedPrefix = "    $prefix"
        when (node) {
            is MergedNode -> {
                log("MergedNode(${node.name})")
                logNode(node.routeNode, increasedPrefix)
                logNode(node.groupNode, increasedPrefix)
            }
            is GroupNode -> {
                log("GroupNode(${node.name})")
                node.childNodes.forEach { logNode(it, increasedPrefix) }
            }
            is RouteNode -> log("RouteNode(${node.name})")
        }
    }

    /**
     * Error logs provide beautiful clickable references for KSNodes, so prefer logging
     * instead of throwing exceptions out of processor.
     */
    private fun logExceptionGracefully(exception: GenerateRoutesException) {
        val message = exception.message!!
        if (exception.nodes.isEmpty()) {
            logger.error(message)
        } else {
            exception.nodes.forEach { logger.error(message, closestNodeWithExistentLocation(it)) }
        }
    }

}