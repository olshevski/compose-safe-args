package dev.olshevski.safeargs.tests

import dev.olshevski.safeargs.GenerateRoutes

@GenerateRoutes("OnlyNestedRoutes")
interface OnlyNestedRoutesInterface {

    fun toRoute1(): String
    fun toRoute2(): String
    fun toRoute3(): String

}