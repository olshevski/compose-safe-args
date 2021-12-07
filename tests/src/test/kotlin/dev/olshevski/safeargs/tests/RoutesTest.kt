package dev.olshevski.safeargs.tests

import io.kotest.core.spec.style.FunSpec
import io.kotest.datatest.withData
import io.kotest.matchers.collections.shouldContainAll
import io.kotest.matchers.shouldBe

class RoutesTest : FunSpec({

    context("correct routes list") {
        test("Routes") {
            Routes.routes shouldContainAll listOf(
                Routes.MainScreen,
                Routes.PrimitiveValuesScreen,
                Routes.StringValueScreen,
                Routes.Subgraph
            )
        }

        test("Routes.Subgraph") {
            Routes.Subgraph.routes shouldContainAll listOf(
                Routes.Subgraph.FirstSubScreen,
                Routes.Subgraph.SecondSubScreen
            )
        }

    }

    context("correct generated pattern") {
        withData(
            nameFn = { it.first::class.simpleName.toString() },
            Routes.MainScreen to "Routes_MainScreen",
            Routes.PrimitiveValuesScreen to "Routes_PrimitiveValuesScreen/{intValue}/{longValue}/{floatValue}/{booleanValue}",
            Routes.StringValueScreen to "Routes_StringValueScreen/{stringValue}?nullableStringValue={nullableStringValue}",
            Routes.Subgraph to "Routes_Subgraph",
            Routes.Subgraph.FirstSubScreen to "Routes_Subgraph_FirstSubScreen",
            Routes.Subgraph.SecondSubScreen to "Routes_Subgraph_SecondSubScreen?nullableStringValue={nullableStringValue}",
        ) { (route, expectedPattern) ->
            route.pattern shouldBe expectedPattern
        }
    }

})