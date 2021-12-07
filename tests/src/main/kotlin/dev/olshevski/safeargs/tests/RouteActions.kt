package dev.olshevski.safeargs.tests

import dev.olshevski.safeargs.GenerateRoutes

@GenerateRoutes("Routes")
interface RouteActions {

    fun toMainScreen(): String

    fun toPrimitiveValuesScreen(
        intValue: Int,
        longValue: Long,
        floatValue: Float,
        booleanValue: Boolean
    ): String

    fun toStringValueScreen(stringValue: String, nullableStringValue: String?): String

    fun toSubgraph(): String

    @GenerateRoutes("Subgraph")
    interface SubgraphActions {

        fun toFirstSubScreen(): String

        fun toSecondSubScreen(nullableStringValue: String?): String

    }

}