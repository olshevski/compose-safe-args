package dev.olshevski.safeargs.sample

import android.os.Bundle
import android.util.Log
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
            ScreenWithTitle(title = MainScreenTitle) {
                Column(
                    Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Button(onClick = {
                        navController.navigate(Routes.toSingleIdScreen(id = SampleValues.LongValue))
                    }) {
                        Text(SingleIdScreenTitle.withToPrefix())
                    }

                    Button(onClick = {
                        navController.navigate(
                            Routes.toNullableIdsScreen(
                                id1 = SampleValues.LongValue,
                                id2 = null
                            )
                        )
                    }) {
                        Text(NullableIdsScreenTitle.withToPrefix())
                    }

                    Button(onClick = {
                        navController.navigate(Routes.toSubgraph())
                    }) {
                        Text(SubgraphTitle.withToPrefix())
                    }

                    Button(onClick = {
                        navController.navigate(
                            Routes.toAllSupportedValuesScreen(
                                stringValue = SampleValues.StringValue,
                                booleanValue = SampleValues.BooleanValue,
                                byteValue = SampleValues.ByteValue,
                                shortValue = SampleValues.ShortValue,
                                intValue = SampleValues.IntValue,
                                longValue = SampleValues.LongValue,
                                floatValue = SampleValues.FloatValue,
                                doubleValue = SampleValues.DoubleValue
                            )
                        )
                    }) {
                        Text(AllSupportedValuesScreenTitle.withToPrefix())
                    }

                    Button(onClick = {
                        navController.navigate(
                            Routes.toAllNullableValuesScreen(
                                stringValue = SampleValues.StringValue,
                                booleanValue = SampleValues.BooleanValue,
                                byteValue = SampleValues.ByteValue,
                                shortValue = SampleValues.ShortValue,
                                intValue = SampleValues.IntValue,
                                longValue = SampleValues.LongValue,
                                floatValue = SampleValues.FloatValue,
                                doubleValue = SampleValues.DoubleValue
                            )
                        )
                    }) {
                        Text(AllNullableValuesScreenTitle.withToPrefix())
                    }

                    Button(onClick = {
                        navController.navigate(
                            Routes.toAllNullValuesScreen(
                                stringValue = null,
                                booleanValue = null,
                                byteValue = null,
                                shortValue = null,
                                intValue = null,
                                longValue = null,
                                floatValue = null,
                                doubleValue = null
                            )
                        )
                    }) {
                        Text(AllNullValuesScreenTitle.withToPrefix())
                    }

                    Button(onClick = {
                        navController.navigate(
                            Routes.toEncodedCharactersScreen(
                                encodedCharactersString = SampleValues.EncodedCharactersString,
                                nullableEncodedCharactersString = SampleValues.AnotherEncodedCharactersString
                            )
                        )
                    }) {
                        Text(EncodedCharactersScreenTitle.withToPrefix())
                    }

                }
            }
        }

        composableWithArgs(Routes.SingleIdScreen) { args ->
            ScreenWithTitle(title = SingleIdScreenTitle) {
                check(args.id == SampleValues.LongValue)
                Text(text = args.toString())

                /*
                 * Explore ViewModels.kt for the simplest way to get arguments in ViewModels. Here
                 * I request ViewModel simply just to instantiate it.
                 */
                viewModel<SingleIdViewModel>()
            }
        }

        composableWithArgs(Routes.NullableIdsScreen) { args ->
            ScreenWithTitle(title = NullableIdsScreenTitle) {
                check(args.id1 == SampleValues.LongValue)
                check(args.id2 == null)
                Text(text = args.toString())

                viewModel<NullableIdsViewModel>()
            }
        }

        navigation(route = Routes.Subgraph, startDestination = Routes.Subgraph.toFirstSubScreen()) {

            composable(Routes.Subgraph.FirstSubScreen) {
                ScreenWithTitle(title = FirstSubScreenTitle) {
                    Button(
                        modifier = Modifier.align(Alignment.CenterHorizontally),
                        onClick = {
                            navController.navigate(Routes.Subgraph.toSecondSubScreen(SampleValues.StringValue))
                        }
                    ) {
                        Text(SecondSubScreenTitle.withToPrefix())
                    }
                }
            }

            composableWithArgs(Routes.Subgraph.SecondSubScreen) { args ->
                ScreenWithTitle(title = SecondSubScreenTitle) {
                    Text(text = args.toString())
                }
            }

        }

        composableWithArgs(Routes.AllSupportedValuesScreen) { args ->
            ScreenWithTitle(title = AllSupportedValuesScreenTitle) {
                check(args.stringValue == SampleValues.StringValue)
                check(args.booleanValue == SampleValues.BooleanValue)
                check(args.byteValue == SampleValues.ByteValue)
                check(args.shortValue == SampleValues.ShortValue)
                check(args.intValue == SampleValues.IntValue)
                check(args.longValue == SampleValues.LongValue)
                check(args.floatValue == SampleValues.FloatValue)
                check(args.doubleValue == SampleValues.DoubleValue)
                Text(text = args.toString())

                viewModel<AllSupportedTypesViewModel>()
            }
        }

        composableWithArgs(Routes.AllNullableValuesScreen) { args ->
            ScreenWithTitle(title = AllNullableValuesScreenTitle) {
                check(args.stringValue == SampleValues.StringValue)
                check(args.booleanValue == SampleValues.BooleanValue)
                check(args.byteValue == SampleValues.ByteValue)
                check(args.shortValue == SampleValues.ShortValue)
                check(args.intValue == SampleValues.IntValue)
                check(args.longValue == SampleValues.LongValue)
                check(args.floatValue == SampleValues.FloatValue)
                check(args.doubleValue == SampleValues.DoubleValue)
                Text(text = args.toString())

                viewModel<AllNullableTypesViewModel>()
            }
        }

        composableWithArgs(Routes.AllNullValuesScreen) { args ->
            ScreenWithTitle(title = AllNullValuesScreenTitle) {
                check(args.stringValue == null)
                check(args.booleanValue == null)
                check(args.byteValue == null)
                check(args.shortValue == null)
                check(args.intValue == null)
                check(args.longValue == null)
                check(args.floatValue == null)
                check(args.doubleValue == null)
                Text(text = args.toString())

                viewModel<AllNullTypesViewModel>()
            }
        }

        composableWithArgs(Routes.EncodedCharactersScreen) { args ->
            ScreenWithTitle(title = EncodedCharactersScreenTitle) {
                check(args.encodedCharactersString == SampleValues.EncodedCharactersString)
                check(args.nullableEncodedCharactersString == SampleValues.AnotherEncodedCharactersString)
                Text(text = args.toString())
                Log.v("ERASEME", args.toString())
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
