package dev.olshevski.safeargs

/**
 * Mark interfaces with this annotation in order to generate all route patterns, argument
 * constants, list of [NamedNavArguments][NamedNavArgument] and most importantly data classes
 * containing all arguments for each route.
 *
 * Such interfaces must declare methods starting with "to" prefix and returning a String value. The
 * name of every method (excluding the "to" prefix) will effectively become the name of a route.
 * Because of this, all methods must not contain duplicate names, even when they have different
 * parameters.
 *
 * All these methods may contain parameters of type Int, Long, Float, Boolean and String. String
 * parameter type may be nullable. Default values are also allowed. All the parameters will become
 * arguments of each corresponding route and their names will be used for properties of generated
 * arguments data classes.
 *
 * @param name the name of a generated class. This class will implement the interface marked with
 * the [GenerateRoutes] annotation and generate implementation for every declared method. Each
 * method will return a route with values of passed in parameters. This way the methods can be
 * used for navigation.
 */
@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.SOURCE)
annotation class GenerateRoutes(val name: String)
