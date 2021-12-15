package dev.olshevski.safeargs.tests

import io.kotest.core.spec.style.FunSpec
import io.kotest.datatest.withData
import io.kotest.matchers.collections.shouldContainAll
import io.kotest.matchers.shouldBe

class RoutesTest : FunSpec({

    context("correct routes list") {
        test("Routes") {
            Routes.routes shouldContainAll setOf(
                Routes.MainScreen,
                Routes.PrimitiveValuesScreen,
                Routes.StringValueScreen,
                Routes.Subgraph
            )
        }

        test("Routes.Subgraph") {
            Routes.Subgraph.routes shouldContainAll setOf(
                Routes.Subgraph.FirstSubScreen,
                Routes.Subgraph.SecondSubScreen
            )
        }

    }

    context("correct generated pattern") {
        withData(
            nameFn = { it.first::class.simpleName.toString() },
            Routes.MainScreen to "Routes-MainScreen",
            Routes.PrimitiveValuesScreen to "Routes-PrimitiveValuesScreen/{intValue}/{longValue}/{floatValue}/{booleanValue}",
            Routes.StringValueScreen to "Routes-StringValueScreen/{stringValue}?nullableStringValue={nullableStringValue}",
            Routes.Subgraph to "Routes-Subgraph",
            Routes.Subgraph.FirstSubScreen to "Routes-Subgraph-FirstSubScreen",
            Routes.Subgraph.SecondSubScreen to "Routes-Subgraph-SecondSubScreen?nullableStringValue={nullableStringValue}",
        ) { (route, expectedPattern) ->
            route.pattern shouldBe expectedPattern
        }
    }

})