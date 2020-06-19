package com.xently.payment.utils

import com.google.gson.*
import com.xently.payment.utils.Exclude.During.*
import org.intellij.lang.annotations.Language
import java.text.DateFormat

@Retention(value = AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.FIELD)
/**
 * Excludes field(s) from json serialization and/or deserialization depending on [during]
 * @param during When to exclude field from serialization and/or deserialization
 */
annotation class Exclude(val during: During = BOTH) {
    /**
     * @see SERIALIZATION Exclude field ONLY from json serialization
     * @see DESERIALIZATION Exclude field ONLY from json deserialization
     * @see BOTH Exclude field from json serialization and deserialization
     */
    enum class During {
        /**
         * Exclude field ONLY from json serialization
         */
        SERIALIZATION,

        /**
         * Exclude field ONLY from json deserialization
         */
        DESERIALIZATION,

        /**
         * Exclude field from json serialization and deserialization
         */
        BOTH
    }
}

interface IData<T> {
    fun fromJson(@Language("JSON") json: String?): T?

    fun fromMap(map: Map<String, Any?>): T? = fromJson(JSON_CONVERTER.toJson(map))
}

inline fun <reified T> fromJson(json: String?): T? = if (json.isNullOrBlank()) null else try {
    JSON_CONVERTER.fromJson(json, T::class.java)
} catch (ex: Exception) {
    null
}

private fun getExclusionStrategy(during: Exclude.During = BOTH): ExclusionStrategy {
    return object : ExclusionStrategy {
        override fun shouldSkipClass(clazz: Class<*>?): Boolean = false

        override fun shouldSkipField(f: FieldAttributes?): Boolean {
            return if (f == null) true else {
                val annotation = f.getAnnotation(Exclude::class.java)
                if (annotation == null) {

                    false
                } else {
                    annotation.during == during
                }
            }
        }
    }
}

val JSON_CONVERTER: Gson = GsonBuilder()
    .enableComplexMapKeySerialization()
    .addSerializationExclusionStrategy(getExclusionStrategy(SERIALIZATION))
    .addDeserializationExclusionStrategy(getExclusionStrategy(DESERIALIZATION))
    .setExclusionStrategies(getExclusionStrategy())
    .serializeNulls()
    .setDateFormat(DateFormat.LONG)
    .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
    /*.registerTypeAdapter(Id::class.java, IdTypeAdapter())
    .setPrettyPrinting()
    .setVersion(1.0)*/
    .create()

data class GsonSerializable(
    val name: String?,
    val age: Int = 0,
    val sex: Sex = Sex.MALE,
    @Exclude
    val ignore: String? = "ISD",
    @Exclude(DESERIALIZATION)
    val ignoreDes: String? = "ID",
    @Exclude(SERIALIZATION)
    val ignoreSer: String? = "IS"
) {
    enum class Sex {
        MALE,
        FEMALE
    }

    @Exclude(SERIALIZATION)
            /**
             * Without annotation it'd be serialized!
             */
    val increaseAgeBy: (increment: Int) -> Int = {
        age + it
    }

    override fun toString(): String = JSON_CONVERTER.toJson(this)

    companion object : IData<GsonSerializable> {
        override fun fromJson(json: String?): GsonSerializable? =
            com.xently.payment.utils.fromJson(json)
    }
}