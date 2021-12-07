package dev.olshevski.safeargs.sample

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import dev.olshevski.safeargs.composable
import dev.olshevski.safeargs.composableWithArgs
import dev.olshevski.safeargs.navigation
import dev.olshevski.safeargs.sample.ui.theme.ComposeSafeArgsTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ComposeSafeArgsTheme {
                // A surface container using the 'background' color from the theme
                Surface(color = MaterialTheme.colors.background) {
                    MainNavHost()
                }
            }
        }
    }
}

@Composable
fun MainNavHost() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = Routes.toMainScreen()) {

        composable(Routes.MainScreen) {
            ScreenWithTitle(title = MainScreen) {
                Column(
                    Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Button(onClick = {
                        navController.navigate(
                            Routes.toPrimitiveValuesScreen(
                                intValue = SampleValues.IntValue,
                                longValue = SampleValues.LongValue,
                                floatValue = SampleValues.FloatValue,
                                booleanValue = SampleValues.BooleanValue
                            )
                        )
                    }) {
                        Text("To $PrimitiveValuesScreen")
                    }

                    Button(onClick = {
                        navController.navigate(Routes.toPrimitiveDefaultValuesScreen())
                    }) {
                        Text("To $PrimitiveDefaultValuesScreen")
                    }

                    Button(onClick = {
                        navController.navigate(
                            Routes.toStringValueScreen(
                                stringValue = SampleValues.StringValue,
                                nullableStringValue = null,
                                anotherNullableStringValue = SampleValues.AnotherStringValue
                            )
                        )
                    }) {
                        Text("To $StringValueScreen")
                    }

                    Button(onClick = {
                        navController.navigate(Routes.toStringDefaultValueScreen())
                    }) {
                        Text("To $StringDefaultValueScreen")
                    }

                    Button(onClick = {
                        navController.navigate(Routes.toSubgraph())
                    }) {
                        Text("To $Subgraph")
                    }
                }
            }
        }

        composableWithArgs(Routes.PrimitiveValuesScreen) { args ->
            ScreenWithTitle(title = PrimitiveValuesScreen) {
                check(args.intValue == SampleValues.IntValue)
                check(args.longValue == SampleValues.LongValue)
                check(args.floatValue == SampleValues.FloatValue)
                check(args.booleanValue == SampleValues.BooleanValue)
                Text(text = args.toString())

                /*
                 * Explore ViewModels.kt for the simplest way to get arguments in ViewModels. Here
                 * I request ViewModel simply just to instantiate it.
                 */
                viewModel<PrimitiveValuesViewModel>()
            }
        }

        composableWithArgs(Routes.PrimitiveDefaultValuesScreen) { args ->
            ScreenWithTitle(title = PrimitiveDefaultValuesScreen) {
                check(args.intValue == SampleValues.IntValue)
                check(args.longValue == SampleValues.LongValue)
                check(args.floatValue == SampleValues.FloatValue)
                check(args.booleanValue == SampleValues.BooleanValue)
                Text(text = args.toString())

                viewModel<PrimitiveDefaultValuesViewModel>()
            }
        }

        composableWithArgs(Routes.StringValueScreen) { args ->
            ScreenWithTitle(title = StringValueScreen) {
                check(args.stringValue == SampleValues.StringValue)
                check(args.nullableStringValue == null)
                check(args.anotherNullableStringValue == SampleValues.AnotherStringValue)
                Text(text = args.toString())

                viewModel<StringValueViewModel>()
            }
        }

        composableWithArgs(Routes.StringDefaultValueScreen) { args ->
            ScreenWithTitle(title = StringDefaultValueScreen) {
                check(args.stringValue == SampleValues.StringValue)
                check(args.nullableStringValue == null)
                check(args.anotherNullableStringValue == SampleValues.AnotherStringValue)
                Text(text = args.toString())

                viewModel<StringDefaultValueViewModel>()
            }
        }

        navigation(route = Routes.Subgraph, startDestination = Routes.Subgraph.toFirstSubScreen()) {

            composable(Routes.Subgraph.FirstSubScreen) {
                ScreenWithTitle(title = FirstSubScreen) {
                    Button(
                        modifier = Modifier.align(Alignment.CenterHorizontally),
                        onClick = {
                            navController.navigate(Routes.Subgraph.toSecondSubScreen(SampleValues.StringValue))
                        }
                    ) {
                        Text("To $SecondSubScreen")
                    }
                }
            }

            composableWithArgs(Routes.Subgraph.SecondSubScreen) { args ->
                ScreenWithTitle(title = SecondSubScreen) {
                    Text(text = args.toString())
                }
            }

        }
    }
}

@Composable
fun ScreenWithTitle(title: String, content: @Composable ColumnScope.() -> Unit = {}) {
    Column(Modifier.fillMaxSize()) {
        Text(
            text = title,
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(16.dp),
            style = MaterialTheme.typography.h5
        )
        content()
    }
}
