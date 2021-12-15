package dev.olshevski.safeargs.tests

import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.collections.shouldContainAll
import io.kotest.matchers.shouldBe

class OnlyNestedRoutesTest : FunSpec({

    test("routes set should contain all nested routes") {
        OnlyNestedRoutes.routes shouldContainAll setOf(
            OnlyNestedRoutes.Route1,
            OnlyNestedRoutes.Route2,
            OnlyNestedRoutes.Route3
        )
    }

    test("groups set should be empty") {
        OnlyNestedRoutes.groups.isEmpty() shouldBe true
    }

})