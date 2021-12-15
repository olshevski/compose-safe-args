package dev.olshevski.safeargs.tests

import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.collections.shouldContainAll
import io.kotest.matchers.shouldBe

class OnlyNestedGroupsTest : FunSpec({

    test("routes set should be empty") {
        OnlyNestedGroups.routes.isEmpty() shouldBe true
    }

    test("groups set should contain all nested groups") {
        OnlyNestedGroups.groups shouldContainAll setOf(
            OnlyNestedGroups.NestedGroup1,
            OnlyNestedGroups.NestedGroup2,
            OnlyNestedGroups.NestedGroup3
        )
    }

})