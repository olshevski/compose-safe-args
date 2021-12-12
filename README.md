# Safe Arguments Generator

Yet another attempt to add safe arguments to [Compose Navigation](https://developer.android.com/jetpack/compose/navigation).

## Why

Since routes in Navigation Component don't support safe arguments out of the box as well as require a lot of boilerplate code, this library was meant to be made.

The main focus of the library is a simplified approach for declaring routes and arguments. What's more, this library *doesn't force you* to declare your screen composables in any particular way, which was a deal-breaker in several other existing safe-args projects.

## Ok, show me the code

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

There is of course a bunch of other useful features that will be explained further. But first...

## Add the library to your project

First, add [Kotlin Symbol Processing](https://github.com/google/ksp) plugin to the module of your project where you are going to declare routes:

```kotlin
plugins {
    // ...
    id("com.google.devtools.ksp") version "1.5.31-1.0.1"
}
```

Then add dependencies:

```kotlin
dependencies {
    // ...
    ksp("dev.olshevski.safeargs:ksp:1.0.0")
    implementation("dev.olshevski.safeargs:api-compose:1.0.0")
}
```

In order for the project to discover newly generated files, add `build/generated/ksp/...` folders to the source sets like this:

```kotlin
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
```
And that's it.

### Alternative dependencies

If you for some reason want to use this library in a non-Compose application, or you just want to write your own custom `NavGraphBuilder` extensions you can use:

```kotlin
implementation("dev.olshevski.safeargs:api:1.0.0")
```
instead of `api-compose`. The `api` artifact contains only essential declarations without any Compose dependencies.

## API

### Declaring routes

You must declare routes inside an interface. The name of the interface is arbitrary. As it declares navigation actions, a good choice for the name may be `RouteActions` or similar one.

Inside the interface you declare methods starting with `to` prefix and returning `String`. Names of methods minus `to` prefix will become names of routes. For example, `fun toMainScreen(): String` will be interpreted as `MainScreen` route.

Every method may contain parameters of **types** `Int`, `Long`, `Float`, `Boolean`, `String` or `String?`. This is the limitation of Navigation Component, see [Supported Argument Types](https://developer.android.com/guide/navigation/navigation-pass-data#supported_argument_types).

**Default values** for parameters may be specified as well. 

Then you annotate the interface with `@GenerateRoutes` specifying the name of the generated class, e.g. `"Routes"`. This class will inherit the interface and provide implementations for every declared method. Every method will then be able to build a `String` representation of a route with arguments applied. Thus the required `String` return value.

For method `toSecondScreen` from the example above, the generated implementation will be:

```kotlin
override fun toSecondScreen(id: Int): String = "Routes_SecondScreen/$id"
```

Note that there is no limitation on the number of `GenerateRoutes`-annotated interfaces. Feel free to group and organize your routes however you want. The only requirement: no duplicate names for generated classes within the same namespace.

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
