package dev.olshevski.safeargs.sample

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel

/*
 * This file shows how arguments can be obtained in ViewModels from SavedStateHandle. Of course
 * there are ways to pass arguments directly into ViewModels' constructors, but that is beyond the
 * scope of this sample.
 */

private const val TAG = "ViewModels"

class PrimitiveValuesViewModel(savedStateHandle: SavedStateHandle) : ViewModel() {

    private val args = Routes.PrimitiveValuesScreen.argsFrom(savedStateHandle)

    init {
        check(args.intValue == SampleValues.IntValue)
        check(args.longValue == SampleValues.LongValue)
        check(args.floatValue == SampleValues.FloatValue)
        check(args.booleanValue == SampleValues.BooleanValue)
        Log.v(TAG, args.toString())
    }

}

class PrimitiveDefaultValuesViewModel(savedStateHandle: SavedStateHandle) : ViewModel() {

    private val args = Routes.PrimitiveDefaultValuesScreen.argsFrom(savedStateHandle)

    init {
        check(args.intValue == SampleValues.IntValue)
        check(args.longValue == SampleValues.LongValue)
        check(args.floatValue == SampleValues.FloatValue)
        check(args.booleanValue == SampleValues.BooleanValue)
        Log.v(TAG, args.toString())
    }

}

class StringValueViewModel(savedStateHandle: SavedStateHandle) : ViewModel() {

    private val args = Routes.StringValueScreen.argsFrom(savedStateHandle)

    init {
        check(args.stringValue == SampleValues.StringValue)
        check(args.nullableStringValue == null)
        check(args.anotherNullableStringValue == SampleValues.AnotherStringValue)
        Log.v(TAG, args.toString())
    }

}

class StringDefaultValueViewModel(savedStateHandle: SavedStateHandle) : ViewModel() {

    private val args = Routes.StringDefaultValueScreen.argsFrom(savedStateHandle)

    init {
        check(args.stringValue == SampleValues.StringValue)
        check(args.nullableStringValue == null)
        check(args.anotherNullableStringValue == SampleValues.AnotherStringValue)
        Log.v(TAG, args.toString())
    }

}