package com.grishko188.pointofinterest.core.validator

import android.content.Context
import android.util.Patterns
import com.grishko188.pointofinterest.R

abstract class Validator(private val context: Context) {

    var isValid = true

    abstract val errorMessageRes: Int

    abstract fun validateLogic(input: String): Boolean

    fun validate(input: String): Boolean {
        isValid = input.isEmpty() || validateLogic(input)
        return isValid
    }

    fun getErrorMessage() = context.getString(errorMessageRes)
}


class UrlValidator(context: Context) : Validator(context) {
    override val errorMessageRes: Int = R.string.error_url_not_valid
    override fun validateLogic(input: String): Boolean =
        Patterns.WEB_URL.matcher(input).matches()
}