package com.xently.payment.utils

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import com.xently.payment.BuildConfig
import com.xently.payment.utils.Log.Type.*
import java.util.*

object Log {
    enum class Type {
        ASSERT,
        DEBUG,
        INFO,
        WARNING,
        ERROR,
        VERBOSE
    }

    /**
     * Shows logs only when build type is debug
     * @param tag: Log TAG
     * @param message: Log message
     * @param throwable: Exception to accompany the log
     * @see BuildConfig.BUILD_TYPE
     */
    @JvmOverloads
    @JvmStatic
    fun show(
        tag: String,
        message: String?,
        throwable: Throwable? = null,
        type: Type = DEBUG,
        logRelease: Boolean = false
    ) {
        if ((!isReleaseBuild() || logRelease) && message != null) {
            when (type) {
                DEBUG -> {
                    if (throwable == null) {
                        Log.d(tag, message)
                        return
                    }
                    Log.d(tag, message, throwable)
                }
                INFO -> {
                    if (throwable == null) {
                        Log.i(tag, message)
                        return
                    }
                    Log.i(tag, message, throwable)
                }
                WARNING -> {
                    if (throwable == null) {
                        Log.w(tag, message)
                        return
                    }
                    Log.w(tag, message, throwable)
                }
                ERROR -> {
                    if (throwable == null) {
                        Log.e(tag, message)
                        return
                    }
                    Log.e(tag, message, throwable)
                }
                VERBOSE -> {
                    if (throwable == null) {
                        Log.v(tag, message)
                        return
                    }
                    Log.v(tag, message, throwable)
                }
                ASSERT -> {
                    if (throwable == null) {
                        Log.wtf(tag, message)
                        return
                    }
                    Log.wtf(tag, message, throwable)
                }
            }
        }
    }

    /**
     * @see show
     */
    @JvmOverloads
    @JvmStatic
    fun show(
        tag: String, message: Any?, throwable: Throwable? = null, type: Type = DEBUG,
        logRelease: Boolean = false
    ) {
        show(tag, "$message", throwable, type, logRelease)
    }
}

object Constants {
    val LOG_TAG = Constants::class.java.simpleName

    const val connectTO = 60L // Seconds
    const val readTO = 30L // Seconds
    const val writeTO = 15L // Seconds

    /*@UseExperimental(kotlin.contracts.ExperimentalContracts::class)
    private fun <T> T?.notNull(): Boolean {
        kotlin.contracts.contract {
            returns(true) implies (this@notNull != null)
        }
        return this != null
    }*/

    /**
     * Creates and or Returns a [SharedPreferences] by the name [name] in [Context.MODE_PRIVATE]
     */
    fun getSharedPref(context: Context, name: String): SharedPreferences =
        context.getSharedPreferences(name, Context.MODE_PRIVATE)

}

/**
 * Sets a number of decimal places to a number
 * @receiver this@inFixedDecimalPoints Number that decimal points / places is to be added to
 * @param points The number of decimal places to add to [this@inFixedDecimalPoints]
 * @param thousandSeparated if **true** then resulting amount's thousands is separated
 * by [thousandSeparator] e.g 8,582.20 or 8,596,159.85
 * @param thousandSeparator a character for separating thousands. Default <code>,</code>
 * @return a string of a number with [points] number of decimal places appended to it
 */
@JvmOverloads
fun Number.inFixedDPs(
    points: Int = 2,
    thousandSeparated: Boolean = true,
    thousandSeparator: Char = ','
): String = if (thousandSeparated) {
    "%,.${points}f".format(this.toDouble())
} else {
    "%.${points}f".format(this.toDouble())
}.replace(',', thousandSeparator)

@JvmOverloads
fun Number.inFixedDPsOrNull(points: Int = 2): Double? {
    return try {
        this.inFixedDPs(points, false).cleanseToNumber
    } catch (ex: Exception) {
        null
    }
}

@JvmOverloads
fun CharSequence?.inFixedDPsOrNull(points: Int = 2): Double? {
    return try {
        this?.cleanseToNumber?.inFixedDPsOrNull(points)
    } catch (ex: Exception) {
        return null
    }
}

@JvmOverloads
fun Number.inFixedDPsOrDefault(points: Int = 2, default: Double = 0.0): Double {
    val fb = default.inFixedDPs(points, false).toDouble()
    return try {
        this.inFixedDPsOrNull(points) ?: fb
    } catch (ex: Exception) {
        fb
    }
}

@JvmOverloads
fun CharSequence?.inFixedDPsOrDefault(points: Int = 2, default: Double = 0.0): Double {
    return (this.inFixedDPsOrNull(points) ?: default).inFixedDPsOrDefault(points, default)
}

operator fun CharSequence.times(no: Int): String {
    val sb = StringBuilder()
    for (i in 0 until no) sb.append(this)
    return sb.toString()
}

/**
 * @receiver [String] with white spaces at its left or right side
 * @return [String] without white spaces at the ends
 */
fun String.spaceTrimmedEndAndStart(): String = this.trimStart().trimEnd()

@Suppress("UNCHECKED_CAST")
fun <K, V, T : Map<K, V>> T?.append(vararg pairs: Pair<K, V>): T {
    return (this?.plus(mapOf(*pairs)) ?: mapOf(*pairs)) as T
}

/**
 * Removes ($,,,/,=) from amount string then converts it to double
 */
inline val CharSequence.cleanseToNumber
    get() = try {
        this.toString().replace(Regex("[$,/=]"), "").toDoubleOrNull()
    } catch (ex: NumberFormatException) {
        null
    }

fun isReleaseBuild() = BuildConfig.BUILD_TYPE.toLowerCase(Locale.ROOT).contains(Regex("^release$"))