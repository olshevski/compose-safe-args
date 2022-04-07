/*
 * Copyright (C) 2020 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package dev.olshevski.safeargs.lint

import com.android.tools.lint.client.api.UElementHandler
import com.android.tools.lint.detector.api.Detector
import com.android.tools.lint.detector.api.JavaContext
import com.android.tools.lint.detector.api.SourceCodeScanner
import com.intellij.psi.PsiType
import org.jetbrains.uast.UAnnotation
import org.jetbrains.uast.UClass
import org.jetbrains.uast.UMethod

@Suppress("UnstableApiUsage")
class GenerateRoutesIssueDetector : Detector(), SourceCodeScanner {

    private companion object {
        const val GenerateRoutesAnnotationName = "dev.olshevski.safeargs.GenerateRoutes"
        const val StringTypeName = "java.lang.String"
        const val MethodPrefix = "to"
        val AllowedPrimitiveParameterTypes = listOf(
            PsiType.BOOLEAN,
            PsiType.BYTE,
            PsiType.SHORT,
            PsiType.INT,
            PsiType.LONG,
            PsiType.FLOAT,
            PsiType.DOUBLE
        )
        val AllowedParameterTypes = listOf(
            "java.lang.Boolean",
            "java.lang.Byte",
            "java.lang.Short",
            "java.lang.Integer",
            "java.lang.Long",
            "java.lang.Float",
            "java.lang.Double",
            StringTypeName
        )
    }

    override fun getApplicableUastTypes() = listOf(UAnnotation::class.java)

    override fun createUastHandler(context: JavaContext) = object : UElementHandler() {

        override fun visitAnnotation(node: UAnnotation) {
            if (node.qualifiedName == GenerateRoutesAnnotationName) {
                val parent = node.uastParent
                if (node.uastParent != null && parent is UClass) {
                    parent.methods.forEach { method ->
                        checkMethodName(context, method)
                        checkReturnType(context, method)
                        checkParameterTypes(context, method)
                    }
                }
            }
        }

    }

    private fun checkMethodName(context: JavaContext, method: UMethod) {
        val nameIsCorrect =
            method.name.startsWith(MethodPrefix) && method.name.length > MethodPrefix.length
        if (!nameIsCorrect) {
            context.report(
                issue = MissingPrefixIssue,
                scopeClass = method,
                location = context.getLocation(method.nameIdentifier),
                message = "Method name (`${method.name}`) must start with \"to\" prefix",
                quickfixData = if (method.name != MethodPrefix) {
                    fix().replace()
                        .with(MethodPrefix + method.name.replaceFirstChar { it.uppercase() })
                        .build()
                } else {
                    null
                }
            )
        }
    }

    private fun checkReturnType(context: JavaContext, method: UMethod) {
        if (method.returnType?.equalsToText(StringTypeName) != true) {
            context.report(
                issue = WrongReturnTypeIssue,
                scopeClass = method,
                location = context.getLocation(method.returnTypeReference ?: method),
                message = "Method `${method.name}` must return String",
                quickfixData = if (method.returnTypeReference == null) {
                    fix().name("Return String").replace().with(method.text + ": String").build()
                } else {
                    fix().replace().with("String").build()
                }
            )
        }
    }

    private fun checkParameterTypes(context: JavaContext, method: UMethod) {
        method.uastParameters.forEach { parameter ->
            val typeIsCorrect = AllowedPrimitiveParameterTypes.any { parameter.type == it }
                    || AllowedParameterTypes.any { parameter.type.equalsToText(it) }
            if (!typeIsCorrect) {
                context.report(
                    issue = WrongParameterTypeIssue,
                    scopeClass = method,
                    location = context.getLocation(parameter.typeReference),
                    message = """Method `${method.name}` must use only allowed parameter types: \
                        `Boolean`, `Byte`, `Short`, `Int`, `Long`, `Float`,  `Boolean` or \
                        `String`. All of them may be nullable.
                        """
                )
            }
        }
    }

}
