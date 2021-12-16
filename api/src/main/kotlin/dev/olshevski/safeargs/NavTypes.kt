package dev.olshevski.safeargs

import android.net.Uri
import android.os.Bundle
import androidx.navigation.NavType

object EncodedStringType : NavType<String?>(isNullableAllowed = true) {

    override fun put(bundle: Bundle, key: String, value: String?) {
        if (value != null) {
            bundle.putString(key, value)
        }
    }

    override fun get(bundle: Bundle, key: String): String? = bundle.getString(key)

    override fun parseValue(value: String): String = Uri.decode(value)

}

object NullableBooleanType : NavType<Boolean?>(isNullableAllowed = true) {

    override fun put(bundle: Bundle, key: String, value: Boolean?) {
        if (value != null) {
            bundle.putBoolean(key, value)
        }
    }

    override fun get(bundle: Bundle, key: String): Boolean? = if (bundle.containsKey(key)) {
        bundle.getBoolean(key)
    } else {
        null
    }

    override fun parseValue(value: String): Boolean = value.toBoolean()

}

object NullableByteType : NavType<Byte?>(isNullableAllowed = true) {

    override fun put(bundle: Bundle, key: String, value: Byte?) {
        if (value != null) {
            bundle.putByte(key, value)
        }
    }

    override fun get(bundle: Bundle, key: String): Byte? = if (bundle.containsKey(key)) {
        bundle.getByte(key)
    } else {
        null
    }

    override fun parseValue(value: String): Byte {
        return if (value.startsWith("0x")) {
            value.substring(2).toByte(16)
        } else {
            value.toByte()
        }
    }

}

object NullableShortType : NavType<Short?>(isNullableAllowed = true) {

    override fun put(bundle: Bundle, key: String, value: Short?) {
        if (value != null) {
            bundle.putShort(key, value)
        }
    }

    override fun get(bundle: Bundle, key: String): Short? = if (bundle.containsKey(key)) {
        bundle.getShort(key)
    } else {
        null
    }

    override fun parseValue(value: String): Short {
        return if (value.startsWith("0x")) {
            value.substring(2).toShort(16)
        } else {
            value.toShort()
        }
    }

}

object NullableIntType : NavType<Int?>(isNullableAllowed = true) {

    override fun put(bundle: Bundle, key: String, value: Int?) {
        if (value != null) {
            bundle.putInt(key, value)
        }
    }

    override fun get(bundle: Bundle, key: String): Int? = if (bundle.containsKey(key)) {
        bundle.getInt(key)
    } else {
        null
    }

    override fun parseValue(value: String): Int {
        return if (value.startsWith("0x")) {
            value.substring(2).toInt(16)
        } else {
            value.toInt()
        }
    }

}

object NullableLongType : NavType<Long?>(isNullableAllowed = true) {

    override fun put(bundle: Bundle, key: String, value: Long?) {
        if (value != null) {
            bundle.putLong(key, value)
        }
    }

    override fun get(bundle: Bundle, key: String): Long? = if (bundle.containsKey(key)) {
        bundle.getLong(key)
    } else {
        null
    }

    override fun parseValue(value: String): Long {
        // At runtime the L suffix is optional, contrary to the Safe Args plugin.
        // This is in order to be able to parse long numbers passed as deep link URL
        // parameters
        var localValue = value
        if (value.endsWith("L")) {
            localValue = localValue.substring(0, value.length - 1)
        }
        return if (value.startsWith("0x")) {
            localValue.substring(2).toLong(16)
        } else {
            localValue.toLong()
        }
    }

}

object NullableFloatType : NavType<Float?>(isNullableAllowed = true) {

    override fun put(bundle: Bundle, key: String, value: Float?) {
        if (value != null) {
            bundle.putFloat(key, value)
        }
    }

    override fun get(bundle: Bundle, key: String): Float? = if (bundle.containsKey(key)) {
        bundle.getFloat(key)
    } else {
        null
    }

    override fun parseValue(value: String): Float = value.toFloat()

}

object NullableDoubleType : NavType<Double?>(isNullableAllowed = true) {

    override fun put(bundle: Bundle, key: String, value: Double?) {
        if (value != null) {
            bundle.putDouble(key, value)
        }
    }

    override fun get(bundle: Bundle, key: String): Double? = if (bundle.containsKey(key)) {
        bundle.getDouble(key)
    } else {
        null
    }

    override fun parseValue(value: String): Double = value.toDouble()

}

