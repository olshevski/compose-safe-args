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

class SingleIdViewModel(savedStateHandle: SavedStateHandle) : ViewModel() {

    private val args = Routes.SingleIdScreen.argsFrom(savedStateHandle)

    init {
        check(args.id == SampleValues.LongValue)
        Log.v(TAG, args.toString())
    }

}

class NullableIdsViewModel(savedStateHandle: SavedStateHandle) : ViewModel() {

    private val args = Routes.NullableIdsScreen.argsFrom(savedStateHandle)

    init {
        check(args.id1 == SampleValues.LongValue)
        check(args.id2 == null)
        Log.v(TAG, args.toString())
    }

}

class AllSupportedTypesViewModel(savedStateHandle: SavedStateHandle) : ViewModel() {

    private val args = Routes.AllSupportedValuesScreen.argsFrom(savedStateHandle)

    init {
        check(args.stringValue == SampleValues.StringValue)
        check(args.booleanValue == SampleValues.BooleanValue)
        check(args.byteValue == SampleValues.ByteValue)
        check(args.shortValue == SampleValues.ShortValue)
        check(args.intValue == SampleValues.IntValue)
        check(args.longValue == SampleValues.LongValue)
        check(args.floatValue == SampleValues.FloatValue)
        check(args.doubleValue == SampleValues.DoubleValue)
        Log.v(TAG, args.toString())
    }

}

class AllNullableTypesViewModel(savedStateHandle: SavedStateHandle) : ViewModel() {

    private val args = Routes.AllNullableValuesScreen.argsFrom(savedStateHandle)

    init {
        check(args.stringValue == SampleValues.StringValue)
        check(args.booleanValue == SampleValues.BooleanValue)
        check(args.byteValue == SampleValues.ByteValue)
        check(args.shortValue == SampleValues.ShortValue)
        check(args.intValue == SampleValues.IntValue)
        check(args.longValue == SampleValues.LongValue)
        check(args.floatValue == SampleValues.FloatValue)
        check(args.doubleValue == SampleValues.DoubleValue)
        Log.v(TAG, args.toString())
    }

}

class AllNullTypesViewModel(savedStateHandle: SavedStateHandle) : ViewModel() {

    private val args = Routes.AllNullValuesScreen.argsFrom(savedStateHandle)

    init {
        check(args.stringValue == null)
        check(args.booleanValue == null)
        check(args.byteValue == null)
        check(args.shortValue == null)
        check(args.intValue == null)
        check(args.longValue == null)
        check(args.floatValue == null)
        check(args.doubleValue == null)
        Log.v(TAG, args.toString())
    }

}