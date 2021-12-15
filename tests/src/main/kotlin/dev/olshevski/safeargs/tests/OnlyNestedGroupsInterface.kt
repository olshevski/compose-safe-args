package dev.olshevski.safeargs.tests

import dev.olshevski.safeargs.GenerateRoutes

@GenerateRoutes("OnlyNestedGroups")
interface OnlyNestedGroupsInterface {

    @GenerateRoutes("NestedGroup1")
    interface NestedGroup1Interface {
    }

    @GenerateRoutes("NestedGroup2")
    interface NestedGroup2Interface {
    }

    @GenerateRoutes("NestedGroup3")
    interface NestedGroup3Interface {
    }

}