@file:Suppress("UnstableApiUsage")

package dev.olshevski.safeargs.lint

import com.android.tools.lint.detector.api.Category
import com.android.tools.lint.detector.api.Implementation
import com.android.tools.lint.detector.api.Issue
import com.android.tools.lint.detector.api.Scope
import com.android.tools.lint.detector.api.Severity

private val GenerateRoutesCategory = Category.create(Category.CORRECTNESS, "GenerateRoutes", 50)

val MissingPrefixIssue: Issue = Issue.create(
    id = "GenerateRoutesMissingPrefix",
    briefDescription = "Method name must start with \"to\" prefix",
    explanation = """
        Every method of interface marked with `@GenerateRoutes` annotation must start \
        with "to" prefix, e.g. `fun toFirstScreen(): String`.
        """,
    category = GenerateRoutesCategory,
    severity = Severity.ERROR,
    implementation = Implementation(
        GenerateRoutesIssueDetector::class.java,
        Scope.JAVA_FILE_SCOPE
    )
)

val WrongReturnTypeIssue: Issue = Issue.create(
    id = "GenerateRoutesWrongReturnType",
    briefDescription = "Method must return String",
    explanation = """
        Every method of interface marked with `@GenerateRoutes` annotation must return String.
        """,
    category = GenerateRoutesCategory,
    severity = Severity.ERROR,
    implementation = Implementation(
        GenerateRoutesIssueDetector::class.java,
        Scope.JAVA_FILE_SCOPE
    )
)

val WrongParameterTypeIssue: Issue = Issue.create(
    id = "GenerateRoutesWrongParameterType",
    briefDescription = "Method must use only supported parameter types",
    explanation = """
        Every method of interface marked with `@GenerateRoutes` annotation must use \
        only supported parameter types: `Boolean`, `Byte`, `Short`, `Int`, `Long`, `Float`, \
        `Boolean` or `String`. All of them may be nullable.
        """,
    category = GenerateRoutesCategory,
    severity = Severity.ERROR,
    implementation = Implementation(
        GenerateRoutesIssueDetector::class.java,
        Scope.JAVA_FILE_SCOPE
    )
)