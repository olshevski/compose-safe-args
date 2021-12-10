# Safe Arguments Generator

Yet another attemp to add safe arguments to [Compose Navigation](https://developer.android.com/jetpack/compose/navigation).

## Why

Since routes in Navigation Component don't support safe arguments out of the box, as well as require a lot of boilerplate code this library was meant to be made.

The main focus of the library is the simplified approach for declaring routes and arguments. What's more, this library **doesn't force you** to declare your screen composables in any particular way, which was a deal-breaker in other existing safe-args libraries.

## Ok, show me the code

You declare all routes within a single interface like this:

```
@GenerateRoutes("Routes")
interface RouteActions {

    fun toMainScreen(): String
    fun toSecondScreen(id: Int): String
    ...
}
```

The processor then generates a class with the name `Routes` as specified in the annotation, which can be used to declare your navigation within `NavHost`:

```
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

```
navController.navigate(Routes.toSecondScreen(id = 123))
```

As simple as that.

There is a of course a bunch of other useful features that will be explained further. But first...

## Add the library to your project

First of all add **Kotlin Symbol Processing** plugin to the module of your project where you are going to declare routes:

```
plugins {
    id("com.google.devtools.ksp") version "1.5.31-1.0.1"
}
```

Then add dependencies:

```
dependencies {
    ksp("dev.olshevski.safeargs:ksp:1.0.0")
    implementation("dev.olshevski.safeargs:api-compose:1.0.0")
}
```

In order for the project to discover a newly generated files add the `build/generated/ksp/…` folders to source sets like this:

```
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

```
implementation("dev.olshevski.safeargs:api:1.0.0")
```
instead of `api-compose`. This artifact contains only the essential declarations without any Compose dependencies.

## Nested navigation

*this section is under construction*