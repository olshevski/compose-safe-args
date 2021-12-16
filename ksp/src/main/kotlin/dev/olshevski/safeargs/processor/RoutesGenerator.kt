package dev.olshevski.safeargs.processor

import com.google.devtools.ksp.processing.CodeGenerator
import com.google.devtools.ksp.symbol.KSType
import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.KModifier
import com.squareup.kotlinpoet.MemberName
import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.parameterizedBy
import com.squareup.kotlinpoet.PropertySpec
import com.squareup.kotlinpoet.STAR
import com.squareup.kotlinpoet.TypeSpec
import com.squareup.kotlinpoet.asTypeName
import com.squareup.kotlinpoet.buildCodeBlock
import com.squareup.kotlinpoet.ksp.KotlinPoetKspPreview
import com.squareup.kotlinpoet.ksp.toTypeName
import com.squareup.kotlinpoet.ksp.writeTo
import com.squareup.kotlinpoet.withIndent

@OptIn(KotlinPoetKspPreview::class)
class RoutesGenerator(
    private val codeGenerator: CodeGenerator,
) {

    /**
     * Referenced types that would be imported.
     */
    private object Types {
        val RouteClass = ClassName(LibPackage, "Route")
        val RouteGroupClass = ClassName(LibPackage, "RouteGroup")
        val UriClass = ClassName("android.net", "Uri")
        val NavArgumentMethod = MemberName("androidx.navigation", "navArgument")
        val BundleClass = ClassName("android.os", "Bundle")
        val SavedStateHandleClass = ClassName("androidx.lifecycle", "SavedStateHandle")
    }

    /**
     * All other names that are not actually imported types.
     */
    private object Names {
        const val ArgsClass = "Args"
        const val FromMethod = "from"
        const val ArgsFromMethod = "argsFrom"
        const val RoutesProperty = "routes"
        const val GroupsProperty = "groups"
        const val BundleParameter = "bundle"
        const val SavedStateHandleParameter = "savedStateHandle"
    }

    private val parameterTypeMappings = mapOf(
        String::class.qualifiedName to ParameterTypeMapping(
            navType = ClassName(LibPackage, "EncodedStringType"),
            buildParameterToStringCode = { it }
        ),
        Boolean::class.qualifiedName to ParameterTypeMapping(
            navType = ClassName(LibPackage, "NullableBooleanType"),
        ),
        Byte::class.qualifiedName to ParameterTypeMapping(
            navType = ClassName(LibPackage, "NullableByteType"),
        ),
        Short::class.qualifiedName to ParameterTypeMapping(
            navType = ClassName(LibPackage, "NullableShortType"),
        ),
        Int::class.qualifiedName to ParameterTypeMapping(
            navType = ClassName(LibPackage, "NullableIntType"),
        ),
        Long::class.qualifiedName to ParameterTypeMapping(
            navType = ClassName(LibPackage, "NullableLongType"),
        ),
        Float::class.qualifiedName to ParameterTypeMapping(
            navType = ClassName(LibPackage, "NullableFloatType"),
        ),
        Double::class.qualifiedName to ParameterTypeMapping(
            navType = ClassName(LibPackage, "NullableDoubleType"),
        ),
    )

    private fun getParameterTypeMapping(type: KSType) =
        parameterTypeMappings[type.declaration.qualifiedName?.asString()]
            ?: error("Unexpected parameter type")

    fun generateRoutesFile(routesStructure: RoutesStructure) {
        val fileSpec = FileSpec
            .builder(
                routesStructure.packageName,
                routesStructure.rootNode.name
            )
            .addType(generateRouteClass(emptyList(), routesStructure.rootNode))
            .build()
        fileSpec.writeTo(codeGenerator, routesStructure.dependencies)
    }

    private fun generateRouteClass(
        parents: List<GroupNode>,
        elementNode: Node
    ): TypeSpec {
        val (routeNode, groupNode) = when (elementNode) {
            is RouteNode -> elementNode to null
            is GroupNode -> null to elementNode
            is MergedNode -> elementNode.routeNode to elementNode.groupNode
        }
        return TypeSpec
            .objectBuilder(elementNode.name)
            .apply {
                if (routeNode != null) {
                    superclass(
                        Types.RouteClass.parameterizedBy(
                            if (routeNode.parameters.isEmpty()) {
                                Unit::class.asTypeName()
                            } else {
                                ClassName("", routeNode.name, Names.ArgsClass)
                            }
                        )
                    )
                    addSuperclassConstructorParameter(
                        "%S",
                        generateRoutePattern(parents, routeNode)
                    )
                    addSuperclassConstructorParameter(generateNavArguments(routeNode.parameters))
                    addFunction(
                        generateFromArgsMethod(
                            routeNode,
                            Names.BundleParameter, Types.BundleClass
                        )
                    )
                    addFunction(
                        generateFromArgsMethod(
                            routeNode,
                            Names.SavedStateHandleParameter,
                            Types.SavedStateHandleClass
                        )
                    )
                    if (routeNode.parameters.isNotEmpty()) {
                        addType(generateArgsClass(routeNode.parameters))
                    }
                }
                if (groupNode != null) {
                    addSuperinterface(groupNode.interfaceType.toTypeName())
                    addSuperinterface(Types.RouteGroupClass)
                    val newParents = parents + groupNode
                    groupNode.childNodes.forEach { childNode ->
                        when (childNode) {
                            is RouteNode -> addFunction(
                                generateRouteMethod(newParents, childNode)
                            )
                            is MergedNode -> addFunction(
                                generateRouteMethod(newParents, childNode.routeNode)
                            )
                            is GroupNode -> {}
                        }
                        addType(generateRouteClass(newParents, childNode))
                    }
                    addProperty(generateRoutesProperty(groupNode))
                    addProperty(generateGroupsProperty(groupNode))
                }
            }
            .build()
    }

    private fun generateRoutesProperty(groupNode: GroupNode) = PropertySpec
        .builder(
            Names.RoutesProperty, Set::class.asTypeName().parameterizedBy(
                Types.RouteClass.parameterizedBy(STAR)
            )
        )
        .addModifiers(KModifier.OVERRIDE)
        .initializer(buildCodeBlock {
            val childRouteNodes = groupNode.childNodes.filter { it !is GroupNode }
            if (childRouteNodes.isEmpty()) {
                add("emptySet()")
            } else {
                addLine("setOf(")
                withIndent {
                    childRouteNodes.forEach { childNode ->
                        addLine("${childNode.name},")
                    }
                }
                add(")")
            }
        })
        .build()

    private fun generateGroupsProperty(groupNode: GroupNode) = PropertySpec
        .builder(
            Names.GroupsProperty, Set::class.asTypeName().parameterizedBy(Types.RouteGroupClass)
        )
        .addModifiers(KModifier.OVERRIDE)
        .initializer(buildCodeBlock {
            val childGroupNodes = groupNode.childNodes.filter { it !is RouteNode }
            if (childGroupNodes.isEmpty()) {
                add("emptySet()")
            } else {
                addLine("setOf(")
                withIndent {
                    childGroupNodes.forEach { childNode ->
                        addLine("${childNode.name},")
                    }
                }
                add(")")
            }
        })
        .build()

    private fun generateFromArgsMethod(
        routeNode: RouteNode,
        parameterName: String,
        parameterClass: ClassName
    ) = FunSpec.builder(Names.ArgsFromMethod)
        .addModifiers(KModifier.OVERRIDE)
        .addParameter(parameterName, parameterClass)
        .apply {
            if (routeNode.parameters.isEmpty()) {
                addStatement("return Unit")
            } else {
                addStatement("return ${Names.ArgsClass}.${Names.FromMethod}(${parameterName})")
            }
        }
        .build()

    private fun generateRoutePattern(
        parents: List<GroupNode>,
        routeNode: RouteNode
    ) = StringBuilder(buildUniqueRouteName(parents, routeNode)).apply {
        val (requiredParameters, optionalParameters) = routeNode.partitionParameters()
        requiredParameters.forEach { parameter ->
            append("/{")
            append(parameter.name)
            append("}")
        }
        if (optionalParameters.isNotEmpty()) {
            append("?")
            optionalParameters.forEachIndexed { index, parameter ->
                if (index > 0) {
                    append("&")
                }
                append(parameter.name)
                append("={")
                append(parameter.name)
                append("}")
            }
        }
    }.toString()

    private fun generateNavArguments(parameters: List<RouteNode.Parameter>) = buildCodeBlock {
        if (parameters.isEmpty()) {
            add("emptyList()")
        } else {
            addLine("listOf(")
            withIndent {
                parameters.forEach { parameter ->
                    addLine(
                        "%M(${Names.ArgsClass}.${parameter.capitalizedName})·{",
                        Types.NavArgumentMethod
                    )
                    withIndent {
                        val navType = getParameterTypeMapping(parameter.type).navType
                        addLine("type = %T", navType)
                        if (parameter.type.isMarkedNullable) {
                            addLine("nullable = true")
                        }
                    }
                    addLine("},")
                }
            }
            add(")")
        }
    }

    private fun generateArgsClass(parameters: List<RouteNode.Parameter>) = TypeSpec
        .classBuilder(Names.ArgsClass)
        .addModifiers(KModifier.DATA)
        .primaryConstructor(FunSpec.constructorBuilder()
            .apply {
                parameters.forEach { parameter ->
                    addParameter(parameter.name, parameter.type.toTypeName())
                }
            }
            .build()
        )
        .apply {
            parameters.forEach { parameter ->
                addProperty(
                    PropertySpec.builder(parameter.name, parameter.type.toTypeName())
                        .initializer(parameter.name)
                        .build()
                )
            }
        }
        .addType(generateArgsCompanionObject(parameters))
        .build()

    private fun generateArgsCompanionObject(parameters: List<RouteNode.Parameter>) = TypeSpec
        .companionObjectBuilder()
        .apply {
            parameters.forEach { parameter ->
                addProperty(
                    PropertySpec
                        .builder(
                            parameter.capitalizedName,
                            String::class.asTypeName(),
                            KModifier.CONST
                        )
                        .initializer("%S", parameter.name)
                        .build()
                )
            }
        }
        .addFunction(
            FunSpec.builder(Names.FromMethod)
                .addParameter(Names.BundleParameter, Types.BundleClass)
                .addCode(buildCodeBlock {
                    addLine("return ${Names.ArgsClass}(")
                    withIndent {
                        parameters.forEach { parameter ->
                            val mapping = getParameterTypeMapping(parameter.type)
                            add(
                                "%T.get(${Names.BundleParameter}, ${parameter.capitalizedName})",
                                mapping.navType
                            )
                            if (!parameter.type.isMarkedNullable) {
                                add("!!")
                            }
                            addLine(",")
                        }
                    }
                    add(")")
                })
                .build()
        )
        .addFunction(
            FunSpec.builder(Names.FromMethod)
                .addParameter(
                    Names.SavedStateHandleParameter,
                    Types.SavedStateHandleClass
                )
                .addCode(buildCodeBlock {
                    addLine("return ${Names.ArgsClass}(")
                    withIndent {
                        parameters.forEach { parameter ->
                            add("${Names.SavedStateHandleParameter}[${parameter.capitalizedName}]")
                            if (!parameter.type.isMarkedNullable) {
                                add("!!")
                            }
                            addLine(",")
                        }
                    }
                    add(")")
                })
                .build()
        )
        .build()

    private fun generateRouteMethod(
        parents: List<GroupNode>,
        routeNode: RouteNode
    ): FunSpec = FunSpec
        .builder("$ToFunctionPrefix${routeNode.name}")
        .apply {
            routeNode.parameters.forEach { parameter ->
                addParameter(parameter.name, parameter.type.toTypeName())
            }
        }
        .returns(routeNode.returnType.toTypeName())
        .addModifiers(KModifier.OVERRIDE)
        .addCode(buildCodeBlock {
            val uniqueRouteName = buildUniqueRouteName(parents, routeNode)
            add("return ")
            if (routeNode.parameters.isEmpty()) {
                add("%S", uniqueRouteName)
            } else {
                addLine("%T.Builder().apply·{", Types.UriClass)
                withIndent {
                    addLine("path(%S)", uniqueRouteName)
                    routeNode.parameters.forEach { parameter ->
                        val parameterToStringCode = getParameterTypeMapping(parameter.type)
                            .buildParameterToStringCode(parameter.name)
                        if (parameter.type.isMarkedNullable) {
                            addLine("if·(${parameter.name} != null)·{")
                            withIndent {
                                addLine("appendQueryParameter(${routeNode.name}.${Names.ArgsClass}.${parameter.capitalizedName}, $parameterToStringCode)")
                            }
                            addLine("}")
                        } else {
                            addLine("appendPath($parameterToStringCode)")
                        }
                    }
                }
                add("}.toString()")
            }
        })
        .build()

    private fun buildUniqueRouteName(
        parents: List<GroupNode>,
        routeNode: RouteNode
    ) = StringBuilder().apply {
        parents.forEach {
            append(it.name)
            append("-")
        }
        append(routeNode.name)
    }.toString()

    private class ParameterTypeMapping(
        val navType: ClassName,
        val buildParameterToStringCode: (String) -> String = { "$it.toString()" }
    )

}