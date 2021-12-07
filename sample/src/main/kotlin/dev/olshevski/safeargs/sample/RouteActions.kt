package dev.olshevski.safeargs.sample

import dev.olshevski.safeargs.GenerateRoutes

/**
 * Class with name "Routes" will be generated implementing RouteActions interface and creating all
 * useful objects, methods and data structures.
 */
@GenerateRoutes("Routes")
interface RouteActions {

    /**
     * Main screen.
     *
     * Note that these comments will actually be inherited in the generated "Routes" class, so you
     * may leave reasonable explanation of your destinations and their parameters right here.
     */
    fun toMainScreen(): String

    fun toPrimitiveValuesScreen(
        intValue: Int,
        longValue: Long,
        floatValue: Float,
        booleanValue: Boolean
    ): String

    fun toPrimitiveDefaultValuesScreen(
        intValue: Int = SampleValues.IntValue,
        longValue: Long = SampleValues.LongValue,
        floatValue: Float = SampleValues.FloatValue,
        booleanValue: Boolean = SampleValues.BooleanValue
    ): String

    fun toStringValueScreen(
        stringValue: String,
        nullableStringValue: String?,
        anotherNullableStringValue: String?
    ): String

    fun toStringDefaultValueScreen(
        stringValue: String = SampleValues.StringValue,
        nullableStringValue: String? = null,
        anotherNullableStringValue: String? = SampleValues.AnotherStringValue
    ): String

    fun toSubgraph(): String

    @GenerateRoutes("Subgraph")
    interface SubgraphActions {

        fun toFirstSubScreen(): String

        fun toSecondSubScreen(nullableStringValue: String?): String

    }

}