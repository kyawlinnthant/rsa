package com.kyawlinnthant.rsa


fun isPhoneNumberValid(ph: String): Boolean = android.util.Patterns.PHONE.matcher(ph).matches()


