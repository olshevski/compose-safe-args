package dev.olshevski.safeargs.processor

import com.google.devtools.ksp.processing.Dependencies
import com.google.devtools.ksp.processing.KSPLogger
import com.google.devtools.ksp.symbol.ClassKind
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.google.devtools.ksp.symbol.KSDeclaration
import com.google.devtools.ksp.symbol.KSFunctionDeclaration
import com.google.devtools.ksp.symbol.KSNode

class RoutesStructureReader(val logger: KSPLogger) {

    private companion object {
        val AllowedParameterTypes = listOf(
            String::class.qualifiedName,
            Boolean::class.qualifiedName,
            Byte::class.qualifiedName,
            Short::class.qualifiedName,
            Int::class.qualifiedName,
            Long::class.qualifiedName,
            Float::class.qualifiedName,
            Double::class.qualifiedName,
        )

        val WordRegex = Regex("[\\w]+")
    }

    fun read(annotatedDeclaration: KSNode): RoutesStructure {
        check(
            annotatedDeclaration is KSClassDeclaration &&
                    annotatedDeclaration.classKind == ClassKind.INTERFACE,
            annotatedDeclaration
        ) {
            "Only interfaces can be annotated with $GenerateRoutes"
        }
        val groupNodeWithDeclaration = readInterfaceStructure(annotatedDeclaration)
        return RoutesStructure(
            packageName = annotatedDeclaration.packageName.asString(),
            dependencies = Dependencies(
                false,
                groupNodeWithDeclaration.declaration.containingFile!!
            ),
            rootNode = groupNodeWithDeclaration.node
        )
    }

    private fun readInterfaceStructure(interfaceDeclaration: KSClassDeclaration): NodeWithDeclaration<GroupNode> {
        val annotation = interfaceDeclaration.annotations
            .find { it.annotationType.resolve().declaration.qualifiedName?.asString() == GenerateRoutes }
        check(annotation != null, interfaceDeclaration) {
            "Nested interfaces must be annotated with $GenerateRoutes"
        }

        val annotationArgument =
            annotation.arguments.find { it.name?.getShortName() == GenerateRoutesName }
        val name = annotationArgument?.value as String

        check(name.isNotBlank(), annotationArgument) {
            "The \"${GenerateRoutesName}\" value in $GenerateRoutes annotation must not be blank"
        }
        check(name.matches(WordRegex), annotationArgument) {
            """ The "$GenerateRoutesName" value in $GenerateRoutes annotation must contain only
                alphanumeric characters and underscore: [A-Za-z0-9_]
            """.singleLine()
        }

        val interfaceType = interfaceDeclaration.asType(emptyList())
        check(name != interfaceType.declaration.simpleName.asString(), annotationArgument) {
            """ The "$GenerateRoutesName" value in $GenerateRoutes annotation must not be the same as
                the name of the annotated interface
            """.singleLine()
        }

        val childrenNodes = interfaceDeclaration.declarations
            .map { declaration ->
                when {
                    declaration is KSFunctionDeclaration -> {
                        readFunctionStructure(declaration)
                    }

                    declaration is KSClassDeclaration &&
                            declaration.classKind == ClassKind.INTERFACE -> {
                        readInterfaceStructure(declaration)
                    }

                    else -> error(
                        """ Only functions and interfaces must be declared inside the interface
                            annotated with $GenerateRoutes
                        """.singleLine(),
                        declaration
                    )
                }

            }
            .mergeRouteAndGroupNodes()

        if (childrenNodes.isEmpty()) {
            logger.warn(
                "The interface doesn't specify any methods. An empty object will be generated.",
                interfaceDeclaration
            )
        }

        return NodeWithDeclaration(
            GroupNode(
                name = name,
                interfaceType = interfaceType,
                childNodes = childrenNodes,
            ),
            interfaceDeclaration
        )
    }

    private fun Sequence<NodeWithDeclaration<out Node>>.mergeRouteAndGroupNodes() =
        groupBy { it.node.name }
            .values
            .map { group ->
                if (group.size == 1) {
                    group[0].node
                } else {
                    val routeNode = group
                        .filter { it.node is RouteNode }
                        .let { routeNodes ->
                            check(routeNodes.size == 1, routeNodes.map { it.declaration }) {
                                "There cannot be more than one method with the same name"
                            }
                            routeNodes[0].node as RouteNode
                        }

                    val groupNode = group
                        .filter { it.node is GroupNode }
                        .let { groupNodes ->
                            check(groupNodes.size == 1, groupNodes.map { it.declaration }) {
                                """ There cannot be more than one nested interface with the same 
                                    "$GenerateRoutesName" value of $GenerateRoutes annotation
                                """.singleLine()
                            }
                            groupNodes[0].node as GroupNode
                        }

                    MergedNode.create(routeNode, groupNode)
                }
            }

    private fun readFunctionStructure(functionDeclaration: KSFunctionDeclaration): NodeWithDeclaration<RouteNode> {
        val functionName = functionDeclaration.simpleName.asString()
        check(functionName.startsWith(ToFunctionPrefix), functionDeclaration) {
            "All functions must start with prefix \"${ToFunctionPrefix}\""
        }
        check(functionName.matches(WordRegex), functionDeclaration) {
            """ All function must be named only with alphanumeric characters and underscore: 
                [A-Za-z0-9_]
            """.singleLine()
        }

        val returnType = functionDeclaration.returnType?.resolve()
            ?: error("Return type cannot be resolved", functionDeclaration)
        check(
            returnType.declaration.qualifiedName?.asString() == String::class.qualifiedName
                    && !returnType.isMarkedNullable,
            functionDeclaration.returnType!!
        ) {
            "Return type can only be non-null String"
        }

        val parameters = functionDeclaration.parameters.map { parameter ->
            val type = parameter.type.resolve()
            val qualifiedName = type.declaration.qualifiedName?.asString()

            check(qualifiedName in AllowedParameterTypes && !parameter.isVararg, parameter.type) {
                """ Parameter type can only be Boolean, Byte, Short, Int, Long, Float, Double or
                    String. They all may be nullable. No vararg.
                """.singleLine()
            }
            
            RouteNode.Parameter(
                parameter.name?.asString() ?: error("Cannot get parameter name", parameter),
                type
            )
        }

        return NodeWithDeclaration(
            RouteNode(
                name = functionName.removePrefix(ToFunctionPrefix),
                returnType = returnType,
                parameters = parameters
            ),
            functionDeclaration
        )
    }

    /**
     * Attach additional declaration information to nodes for logging reasons.
     */
    private class NodeWithDeclaration<T : Node>(
        val node: T,
        val declaration: KSDeclaration
    )

}

