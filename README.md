# Safe Arguments Generator

Yet another attemp to add safe arguments to [Compose Navigation](https://developer.android.com/jetpack/compose/navigation).

## Why

Since routes in Navigation Component don't support safe arguments out of the box as well as require a lot of boilerplate code, this library was meant to be made.

The main focus of the library is a simplified approach for declaring routes and arguments. What's more, this library *doesn't force you* to declare your screen composables in any particular way, which was a deal-breaker in several other existing safe-args libraries.

## Ok, show me the code

You declare all routes within a single interface like this:

```kotlin
@GenerateRoutes("Routes")
interface RouteActions {

    fun toMainScreen(): String
    fun toSecondScreen(id: Int): String
    ...

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

    ...
}
```

And in order to navigate between destinations you just call:

```kotlin
navController.navigate(Routes.toSecondScreen(id = 123))
```

As simple as that.

There is a of course a bunch of other useful features that will be explained further. But first...

## Add the library to your project

First of all add [Kotlin Symbol Processing](https://github.com/google/ksp) plugin to the module of your project where you are going to declare routes:

```kotlin
plugins {
    ...
    id("com.google.devtools.ksp") version "1.5.31-1.0.1"
}
```

Then add dependencies:

```kotlin
dependencies {
    ...
    ksp("dev.olshevski.safeargs:ksp:1.0.0")
    implementation("dev.olshevski.safeargs:api-compose:1.0.0")
}
```

In order for the project to discover newly generated files add `build/generated/ksp/...` folders to source sets like this:

```kotlin
android {
    ...
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

If you for some reason want to use this library in a non-Compose application or you just want to write you own custom `NavGraphBuilder` extensions you can use:

```kotlin
implementation("dev.olshevski.safeargs:api:1.0.0")
```
instead of `api-compose`. The `api` artifact contains only essential declarations without any Compose dependencies.

## API

### Declaring routes

You must declare routes inside an interface. The name of the interface is arbitrary. As it declares navigation actions a good choise for the name may be `RouteActions` or similar one.

Inside the interface you declare methods starting with `to` prefix and returning `String`. The names of the methods without `to` prefix will become the names of the routes. For example, `fun toMainScreen(): String` will be interpreted as `MainScreen` route.

Every method may contain parameters of types `Int`, `Long`, `Float`, `Boolean`, `String` or `String?`. This is the limitation of Navigation Component, see [Supported Argument Types](https://developer.android.com/guide/navigation/navigation-pass-data#supported_argument_types).

Then you annotate the interface with `@GenerateRoutes` annotation specifying the name of the generated class, e.g. `"Routes"`. This class will inherit the interface and provide implementations for every declared method. Every method will then be able to build a `String` representation of a route with arguments applied. Thus the required `String` return value.

Note that there is no limitation on the number of `GenerateRoutes`-annotated interfaces. Feel free to group and organize your routes however you want. The only requirement: no duplicate names for generated classes within the same package.

### What's generated

As it was said above, navigations actions are implemented. Besides that route declarations are constructed. They all inherit the base `Route` class and provide the `pattern` property and the list of `NamedNavArguments` which are both required to declare routes within `NavHost`.

And of course, every generated route with parameters contains `Args` data class.

Let's see what a single declaration of `fun toSecondScreen(id: Int): String` generates:

```kotlin
public override fun toSecondScreen(id: Int): String = "Routes_SecondScreen/$id"

public object Second : Route<Second.Args>(
    "Routes_Second/{id}", listOf(
        navArgument(Args.Id) {
            type = NavType.IntType
        },
    )
) {
    public override fun argsFrom(bundle: Bundle) = Args.from(bundle)

    public override fun argsFrom(savedStateHandle: SavedStateHandle) =
        Args.from(savedStateHandle)

    public data class Args(
        public val id: Int
    ) {
        public companion object {
            public const val Id: String = "id"

            public fun from(bundle: Bundle) = Args(
                bundle.getInt(Id),
            )

            public fun from(savedStateHandle: SavedStateHandle) = Args(
                savedStateHandle[Id]!!,
            )
        }
    }
}
```

As you can see a bunch of useful `from` methods are generated and constants for arguments. You may use them as you want. For example, argument constants may be usefull for declaring deep-links.

### Nested navigation

*this section is under construction*