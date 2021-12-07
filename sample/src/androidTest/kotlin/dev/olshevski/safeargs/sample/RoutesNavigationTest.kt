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
            .onNodeWithText(MainScreen)
            .assertIsDisplayed()
    }

    @Test
    fun primitiveValuesScreenArgsPassed() {
        composeTestRule
            .onNodeWithText("To $PrimitiveValuesScreen")
            .performClick()

        composeTestRule
            .onNodeWithText(PrimitiveValuesScreen)
            .assertIsDisplayed()

        composeTestRule
            .onNodeWithText("Args(intValue=1234567890, longValue=123456789010, floatValue=12345.679, booleanValue=true)")
            .assertIsDisplayed()
    }

    @Test
    fun primitiveDefaultValuesScreenArgsPassed() {
        composeTestRule
            .onNodeWithText("To $PrimitiveDefaultValuesScreen")
            .performClick()

        composeTestRule
            .onNodeWithText(PrimitiveDefaultValuesScreen)
            .assertIsDisplayed()

        composeTestRule
            .onNodeWithText("Args(intValue=1234567890, longValue=123456789010, floatValue=12345.679, booleanValue=true)")
            .assertIsDisplayed()
    }

    @Test
    fun stringValueScreenArgsPassed() {
        composeTestRule
            .onNodeWithText("To $StringValueScreen")
            .performClick()

        composeTestRule
            .onNodeWithText(StringValueScreen)
            .assertIsDisplayed()

        composeTestRule
            .onNodeWithText("Args(stringValue=Hello, routes!, nullableStringValue=null, anotherNullableStringValue=Hello to you too!)")
            .assertIsDisplayed()
    }

    @Test
    fun stringDefaultValueScreenArgsPassed() {
        composeTestRule
            .onNodeWithText("To $StringDefaultValueScreen")
            .performClick()

        composeTestRule
            .onNodeWithText(StringDefaultValueScreen)
            .assertIsDisplayed()

        composeTestRule
            .onNodeWithText("Args(stringValue=Hello, routes!, nullableStringValue=null, anotherNullableStringValue=Hello to you too!)")
            .assertIsDisplayed()
    }

    @Test
    fun subgraphOpened() {
        composeTestRule
            .onNodeWithText("To $Subgraph")
            .performClick()

        composeTestRule
            .onNodeWithText(FirstSubScreen)
            .assertIsDisplayed()
    }

    @Test
    fun subSecondScreenArgsPassed() {
        composeTestRule
            .onNodeWithText("To $Subgraph")
            .performClick()

        composeTestRule
            .onNodeWithText("To $SecondSubScreen")
            .performClick()

        composeTestRule
            .onNodeWithText(SecondSubScreen)
            .assertIsDisplayed()

        composeTestRule
            .onNodeWithText("Args(nullableStringValue=Hello, routes!)")
            .assertIsDisplayed()
    }
}