package com.chkan.bestpractices.luep_translater

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.chkan.bestpractices.core.SingleLiveEvent
import com.chkan.bestpractices.luep_translater.data.LuepService
import com.chkan.bestpractices.luep_translater.data.models.AuthResponse
import com.chkan.bestpractices.luep_translater.data.models.ClientData
import com.chkan.bestpractices.luep_translater.data.models.LoginRequest
import com.chkan.bestpractices.luep_translater.data.models.TranslateRequest
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.serialization.SerialName
import java.lang.StringBuilder
import javax.inject.Inject

@HiltViewModel
class LuepViewModel @Inject constructor(
    private val luepService: LuepService,
) : ViewModel() {

    private val tokenResources = SingleLiveEvent<AuthResponse>()
    fun token() = tokenResources

    private val errorResource = SingleLiveEvent<String>()
    fun error() = errorResource

    private val translatedResource = SingleLiveEvent<String>()
    fun translated() = translatedResource

    private var token: String? = null

    private val listLocales = listOf("bn_IN","es_ES","fa_IR","fr_FR","hi_IN","ne_NP","or_IN","pa_IN","ps_AR","pt_PT","ur_IN")

    fun login(){
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = luepService.loginUser(LoginRequest(
                    email = "bengolson1@gmail.com", password = "abcd1234", clientData = ClientData(
                        os = "Android",
                        osVersion = "12",
                        fcmToken = "crqd0Uv2R8Oft40dMukTeP:APA91bHTBpMruFh6c1Tm_8uK4M7fnAS6t7W8EnFv0HeLhWfR6pVK52v0P0mNE-h-Ilvj-VK79Jvk9OyIyKOwHnld6vxleyccuE6M3MWD7Kiaop_hwvoifPBKpoV1OMxGC76-xqoLe3Ef",
                        mfaDeviceId = "gfdsgfdsgfdsgfdsgfdsgfdsgfdgsfsdgfd",
                        appName = "luep",
                        deviceId = "sefekjf4398tui4838734u"
                    )
                ))
                token = response.data?.token
                tokenResources.postValue(response.data)
            } catch (e:Exception){
                errorResource.postValue(e.message)
            }
        }
    }

    fun translate(text: String) {
        viewModelScope.launch(Dispatchers.IO) {
            token?.let {
                try {
                    val bearer = "Bearer $it"
                    val resultText = StringBuilder()
                    listLocales.onEach {locale ->
                        val translated = luepService.translate(
                            bearer,
                            TranslateRequest(translatedLocale = locale, originalText = text)
                        ).data?.translatedText
                        resultText.append("$locale : $translated \n")
                    }
                    translatedResource.postValue(resultText.toString())
                } catch (e:Exception){
                    errorResource.postValue(e.message)
                }
            }
        }
    }
}