package dev.olshevski.safeargs

import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe

class BuildOptionalArgumentsTest : FunSpec({
    test("no arguments") {
        buildOptionalArguments() shouldBe ""
    }

    test("one argument") {
        buildOptionalArguments(
            "argName" to "argValue"
        ) shouldBe "?argName=argValue"
    }

    test("two arguments") {
        buildOptionalArguments(
            "argName" to "argValue",
            "anotherName" to "anotherValue"
        ) shouldBe "?argName=argValue&anotherName=anotherValue"
    }

    test("all supported types") {
        buildOptionalArguments(
            "string" to "stringValue",
            "int" to 123456789,
            "long" to 123456789010L,
            "short" to 12345.679F,
            "boolean" to true
        ) shouldBe "?string=stringValue&int=123456789&long=123456789010&short=12345.679&boolean=true"
    }
})