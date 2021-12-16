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

    fun toSingleIdScreen(id: Long): String

    fun toNullableIdsScreen(id1: Long?, id2: Long?): String

    fun toSubgraph(): String

    @GenerateRoutes("Subgraph")
    interface SubgraphActions {

        fun toFirstSubScreen(): String

        fun toSecondSubScreen(nullableStringValue: String?): String

    }

    fun toAllSupportedValuesScreen(
        stringValue: String,
        booleanValue: Boolean,
        byteValue: Byte,
        shortValue: Short,
        intValue: Int,
        longValue: Long,
        floatValue: Float,
        doubleValue: Double
    ): String

    fun toAllNullableValuesScreen(
        stringValue: String?,
        booleanValue: Boolean?,
        byteValue: Byte?,
        shortValue: Short?,
        intValue: Int?,
        longValue: Long?,
        floatValue: Float?,
        doubleValue: Double?
    ): String

    fun toAllNullValuesScreen(
        stringValue: String?,
        booleanValue: Boolean?,
        byteValue: Byte?,
        shortValue: Short?,
        intValue: Int?,
        longValue: Long?,
        floatValue: Float?,
        doubleValue: Double?
    ): String

    fun toEncodedCharactersScreen(
        encodedCharactersString: String,
        nullableEncodedCharactersString: String?
    ): String

}