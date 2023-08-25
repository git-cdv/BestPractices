package com.chkan.bestpractices.luep_translater.data.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class LoginRequest(
    val email: String,
    val password: String,
    @SerialName("client_data") val clientData: ClientData,
)

@Serializable
data class ClientData(
    val os: String,
    @SerialName("os_version") val osVersion: String,
    @SerialName("fcm_token") val fcmToken: String,
    @SerialName("mfa_device_id") val mfaDeviceId: String? = null,
    @SerialName("app_name") val appName: String,
    @SerialName("device_id") val deviceId: String
)