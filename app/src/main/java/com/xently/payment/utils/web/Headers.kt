package com.xently.payment.utils.web

object Headers {
    /*AUTHORIZATION("Authorization"),
    AUTHENTICATION("WWW-Authenticate"),
    DEVICE_TOKEN("x-device-token"),
    DEVICE_TYPE("x-device-type"),
    APP_FLAVOR("x-app-flavor"),
    ACCEPT_LANGUAGE("Accept-Language"),
    COOKIE("Cookie")*/

    const val AUTHORIZATION: String = "Authorization"
    const val DEVICE_IP_ADDRESS: String = "X-Forwarded-For"
    const val DEVICE_TOKEN: String = "x-device-token"
    const val DEVICE_ID: String = "x-device-id"
    const val ACCEPT_LANGUAGE: String = "Accept-Language"
    const val COOKIE: String = "Cookie"
}