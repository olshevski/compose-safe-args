package dev.olshevski.safeargs.sample

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import org.junit.Rule
import org.junit.Test

class RoutesNavigationTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    @Test
    fun mainScreenDisplayed() {
        composeTestRule
            .onNodeWithText(MainScreenTitle)
            .assertIsDisplayed()
    }

    @Test
    fun singleIdScreenArgsPassed() {
        composeTestRule
            .onNodeWithText(SingleIdScreenTitle.withToPrefix())
            .performClick()

        composeTestRule
            .onNodeWithText(SingleIdScreenTitle)
            .assertIsDisplayed()

        composeTestRule
            .onNodeWithText("Args(id=123456789010)")
            .assertIsDisplayed()
    }

    @Test
    fun nullableIdsScreenArgsPassed() {
        composeTestRule
            .onNodeWithText(NullableIdsScreenTitle.withToPrefix())
            .performClick()

        composeTestRule
            .onNodeWithText(NullableIdsScreenTitle)
            .assertIsDisplayed()

        composeTestRule
            .onNodeWithText("Args(id1=123456789010, id2=null)")
            .assertIsDisplayed()
    }

    @Test
    fun subgraphOpened() {
        composeTestRule
            .onNodeWithText(SubgraphTitle.withToPrefix())
            .performClick()

        composeTestRule
            .onNodeWithText(FirstSubScreenTitle)
            .assertIsDisplayed()
    }

    @Test
    fun subSecondScreenArgsPassed() {
        composeTestRule
            .onNodeWithText(SubgraphTitle.withToPrefix())
            .performClick()

        composeTestRule
            .onNodeWithText(SecondSubScreenTitle.withToPrefix())
            .performClick()

        composeTestRule
            .onNodeWithText(SecondSubScreenTitle)
            .assertIsDisplayed()

        composeTestRule
            .onNodeWithText("Args(nullableStringValue=Hello, routes!)")
            .assertIsDisplayed()
    }

    @Test
    fun allSupportedValuesScreenTitleArgsPassed() {
        composeTestRule
            .onNodeWithText(AllSupportedValuesScreenTitle.withToPrefix())
            .performClick()

        composeTestRule
            .onNodeWithText(AllSupportedValuesScreenTitle)
            .assertIsDisplayed()

        composeTestRule
            .onNodeWithText("Args(stringValue=Hello, routes!, booleanValue=true, byteValue=123, shortValue=12345, intValue=1234567890, longValue=123456789010, floatValue=12345.679, doubleValue=12345.67891)")
            .assertIsDisplayed()
    }

    @Test
    fun allNullableValuesScreenTitleArgsPassed() {
        composeTestRule
            .onNodeWithText(AllNullableValuesScreenTitle.withToPrefix())
            .performClick()

        composeTestRule
            .onNodeWithText(AllNullableValuesScreenTitle)
            .assertIsDisplayed()

        composeTestRule
            .onNodeWithText("Args(stringValue=Hello, routes!, booleanValue=true, byteValue=123, shortValue=12345, intValue=1234567890, longValue=123456789010, floatValue=12345.679, doubleValue=12345.67891)")
            .assertIsDisplayed()
    }

    @Test
    fun allNullValuesScreenTitleArgsPassed() {
        composeTestRule
            .onNodeWithText(AllNullValuesScreenTitle.withToPrefix())
            .performClick()

        composeTestRule
            .onNodeWithText(AllNullValuesScreenTitle)
            .assertIsDisplayed()

        composeTestRule
            .onNodeWithText("Args(stringValue=null, booleanValue=null, byteValue=null, shortValue=null, intValue=null, longValue=null, floatValue=null, doubleValue=null)")
            .assertIsDisplayed()
    }

    @Test
    fun encodedCharactersScreenTitleArgsPassed() {
        composeTestRule
            .onNodeWithText(EncodedCharactersScreenTitle.withToPrefix())
            .performClick()

        composeTestRule
            .onNodeWithText(EncodedCharactersScreenTitle)
            .assertIsDisplayed()

        composeTestRule
            .onNodeWithText("Args(encodedCharactersString=path1/part1?value1=arg1, nullableEncodedCharactersString=path2/part2?value2=arg2)")
            .assertIsDisplayed()
    }

}