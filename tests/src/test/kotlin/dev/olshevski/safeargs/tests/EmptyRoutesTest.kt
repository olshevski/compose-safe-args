package dev.olshevski.safeargs.tests

import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe

class EmptyRoutesTest : FunSpec({
    test("routes set should be empty") {
        EmptyRoutes.routes.isEmpty() shouldBe true
    }

    test("groups set should be empty") {
        EmptyRoutes.groups.isEmpty() shouldBe true
    }
})