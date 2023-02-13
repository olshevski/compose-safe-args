# Safe Arguments Generator (Discontinued). Check my type-safe navigation library instead: [Compose Navigation Reimagined](https://github.com/olshevski/compose-navigation-reimagined)

[Navigation Component for Compose](https://developer.android.com/jetpack/compose/navigation) doesn't support safe arguments out of the box as well as requires a lot of boilerplate code. This library fills in this missing part. 

The main focus of the library is a simplified approach for declaring routes and arguments. What's more, this library *doesn't force you* to declare your screen composables in any particular way, which is in my opinion an overkill in several other existing safe-args projects.

## Usage

You declare all routes within a single interface like this:

```kotlin
@GenerateRoutes("Routes")
interface RouteActions {

    fun toMainScreen(): String
    fun toSecondScreen(id: Int): String
    // ...

}
```

The processor then generates a class with the name `Routes` as specified in the annotation, which can be used to declare your navigation within `NavHost`:

```kotlin
NavHost(navController, startDestination = Routes.toMainScreen()) {

    composable(Routes.MainScreen) {
        // your composable UI goes here
    }
    
    composableWithArgs(Routes.SecondScreen) { args ->
        // here you can use args.id
    }

    // ...
}
```

And in order to navigate between destinations, you just call:

```kotlin
navController.navigate(Routes.toSecondScreen(id = 123))
```

As simple as that.

## Setup

The library uses [Kotlin Symbol Processing](https://github.com/google/ksp) for annotation processing. Here is what you need to add to the module of your project where you are going to declare routes:

```kotlin
plugins {
    // ...
    id("com.google.devtools.ksp") version "1.6.10-1.0.4"
}

android {
    // ...
    applicationVariants.all {
            val variantName = name
            sourceSets {
                named(variantName) {
                    kotlin.srcDir(file("build/generated/ksp/$variantName/kotlin"))
                }
            }
        }
    }
}

dependencies {
    // ...
    ksp("dev.olshevski.safeargs:ksp:1.2.1")
    implementation("dev.olshevski.safeargs:api-compose:1.2.1")
}
```

### Alternative dependencies

If you for some reason want to use this library in a non-Compose application, or you just want to write your own custom `NavGraphBuilder` extensions you can use:

```kotlin
implementation("dev.olshevski.safeargs:api:1.2.1")
```
instead of `api-compose`. The `api` artifact contains only essential declarations without any Compose dependencies.

## API

### Declaring routes

You must declare routes inside an interface. The name of the interface is arbitrary. As it declares navigation actions, a good choice for the name may be `RouteActions` or similar one.

Inside the interface you declare methods starting with `to` prefix and returning `String`. The names of methods minus `to` prefix will become the names of routes. For example, `fun toMainScreen(): String` will be interpreted as `MainScreen` route.

Every method may contain parameters of **supported types** `Boolean`, `Byte`, `Short`, `Int`, `Long`, `Float`, `Double` or `String`. They all *may be nullable*.

**Default values** for parameters may be specified as well. 

Then you annotate the interface with `@GenerateRoutes` specifying the name of the generated class, e.g. `"Routes"`. This class will inherit the interface and provide implementations for every declared method. Every method will then be able to build a `String` representation of a route with arguments applied. Thus the required `String` return value.

Note that there is no limitation on the number of `GenerateRoutes`-annotated interfaces. Feel free to group and organize your routes however you want. The only requirement: no duplicate names for generated classes within the same namespace.

### Important notices on String type

- URL-encoding of special characters such as `/`, `?`, `=` and various other symbols is handled in this library by default.

- Do not pass empty Strings as required argument values. They lead to invalid routes and Navigation Component crashes.

- Empty strings passed as optional argument values will become `null` when received.

### What else is generated

The route declarations themselves are constructed. They all inherit the base `Route` class and provide the `pattern` property and the list of `NamedNavArguments` which are both required to declare navigation routes within `NavHost`.

And of course, every generated route with parameters contains `Args` data class.

Let's see what a single declaration of `fun toSecondScreen(id: Int): String` generates:

```kotlin
object Second : Route<Second.Args>(
    "Routes_Second/{id}", listOf(
        navArgument(Args.Id) {
            type = NavType.IntType
        },
    )
) {
    override fun argsFrom(bundle: Bundle) = Args.from(bundle)

    override fun argsFrom(savedStateHandle: SavedStateHandle) =
        Args.from(savedStateHandle)

    data class Args(
        val id: Int
    ) {
        companion object {
            const val Id: String = "id"

            fun from(bundle: Bundle) = Args(
                bundle.getInt(Id),
            )

            fun from(savedStateHandle: SavedStateHandle) = Args(
                savedStateHandle[Id]!!,
            )
        }
    }
}
```

As you can see, a bunch of useful `from` methods are generated and constants for arguments. You may use them as you want. For example, argument constants may be useful for declaring deep-links.

**Note:** constructing arguments from `SavedStateHandle` is useful in `ViewModels`. For acquiring arguments from `NavBackStackEntry` use `argsFrom(navBackStackEntry.arguments)`. `navBackStackEntry.savedStateHandle` may simply be `null`.

### Nested navigation

This library was created with nested navigation in mind. To organize your routes hierarchically, you can simply declare more nested interfaces with `@GenerateRoutes` annotation:

```kotlin
@GenerateRoutes("Routes")
interface RouteActions {

    // ...

    @GenerateRoutes("Subroutes")
    interface SubrouteActions {

        fun toFirstScreen(): String
        fun toSecondScreen(): String

    }
    
}
```

This declaration will simply be treated as another group of routes, which are placed inside `Routes.Subroutes` object. In order for `Routes.Subroutes` to be a route itself, you need to add a navigation declaration with the same name:

```kotlin
@GenerateRoutes("Routes")
interface RouteActions {

    // ...

    fun toSubroutes(): String

    @GenerateRoutes("Subroutes")
    interface SubrouteActions {

        fun toFirstScreen(): String
        fun toSecondScreen(): String

    }
    
}
```

Now `Routes.Subroutes` is both a `Route` and a container for nested routes.

### Extension methods

`api` artifacts adds a single extension method for now:

```kotlin
fun NavController.getBackStackEntry(route: Route<*>)
```

`api-compose` artifact adds convenient `NavGraphBuilder` extensions for using generated routes and easy acquiring of `Args` classes:

```kotlin
NavHost(navController, startDestination = Routes.toMainScreen()) {

    composable(Routes.MainScreen) {
        // you may get args manually here or in ViewModel
    }
    
    composableWithArgs(Routes.SecondScreen) { args ->
        // Args are acquired. 
        // Works well even if route doesn't have any parameters.
    }

    // same for Dialogs
    dialog(Routes.SomeDialog) { /* ... */ }
    dialogWithArgs(Routes.AnotherDialog) { args -> /* ... */ }

    // nested navigation
    navigation(Routes.Subroutes) { /* ... */ }

}
```

## Sample

Please explore the `sample` module within the project for better understanding of capabilities of the library.
