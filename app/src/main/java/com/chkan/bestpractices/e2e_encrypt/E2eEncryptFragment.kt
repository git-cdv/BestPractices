package com.chkan.bestpractices.e2e_encrypt

import android.os.Build
import android.os.Bundle
import android.security.keystore.KeyProperties
import android.util.Base64
import android.util.Log
import android.view.View
import androidx.annotation.RequiresApi
import com.chkan.bestpractices.core.BaseFragment
import com.chkan.bestpractices.core.extensions.onClick
import com.chkan.bestpractices.databinding.FragmentEncryptBinding
import com.google.crypto.tink.aead.subtle.AesGcmSiv
import com.google.crypto.tink.subtle.ChaCha20Poly1305
import com.google.crypto.tink.subtle.Hkdf
import com.google.crypto.tink.subtle.X25519
import dagger.hilt.android.AndroidEntryPoint
import java.io.ByteArrayOutputStream
import java.nio.charset.StandardCharsets
import java.security.KeyFactory
import java.security.KeyPair
import java.security.KeyPairGenerator
import java.security.spec.X509EncodedKeySpec
import javax.crypto.KeyAgreement


@AndroidEntryPoint
class E2eEncryptFragment : BaseFragment<FragmentEncryptBinding>(FragmentEncryptBinding::inflate) {

    @RequiresApi(Build.VERSION_CODES.S)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnGenerateKeys.onClick {
            //VERSION 1 - FIT FOR IOS with Tink library
            // This example uses the Tink library and the HKDF key derivation function.
            val privateKey = X25519.generatePrivateKey()
            val publicKey = X25519.publicFromPrivate(privateKey)

            binding.tvPrivateKeyResult.text = privateKey.toString()
            binding.tvPublicKeyResult.text = publicKey.toString()

            //If you want to share your public key to a server
            val pubKeyString = String(Base64.encode(publicKey, 0))
            Log.d("CHKAN", "pubKeyString - $pubKeyString")
            val privateKeyString = String(Base64.encode(privateKey, 0))
            Log.d("CHKAN", "privateKeyString - $privateKeyString")

            val decodedPublicKey = Base64.decode("4B1rgdgeaxFB+Ch0R5uGVt49+lvz6WmaHMS8tmg9Vwc=", 0)
            val decodedPrivateKey = Base64.decode("30DunXNTb6+zEWB9R4f1cp3pospgbJV4MfhcAzQtR68=", 0)

            val decodedOtherPublicKey =
                Base64.decode("t390EUVino5elU7PRJe4rBaNLCKleAH3SsGGBK8ri3o=", 0)

            //суть EСDH алгоритма: можно создать общий симметричный ключ из своего приватного и чужого публичного (ассиметричные ключи
            // т.е он будет одинаков если другая сторона сделает также с моим публичным ключом
            // для этого ключи должны быть созданы подходящим алгоритмом X25519 или EC
            val sharedSecretBytes =
                X25519.computeSharedSecret(decodedPrivateKey, decodedOtherPublicKey)

            val salt = "1111".toByteArray()
            val info = ByteArrayOutputStream()

            // через Tink library создаем обертку над ключем, которая шифрует/дешифрует через алгоритм ChaChaPoly (можно менять выбрав другую обертку)
            val keyChaCha = ChaCha20Poly1305(
                Hkdf.computeHkdf(
                    "HMACSHA256", sharedSecretBytes, salt, info.toByteArray(), 32
                )
            )

            val decodedServerText = Base64.decode(
                "Z9z7DZb2zJ5JIDfKAjhatQvZJeb5/wQPLudDA6x+WBJykZHm++YkkY/fp6TeJelEzbL4xSEg3muVWf9VPoy4naYRE18bJT5HEsT2lOtx0Ao/gWOeiScMOvI1b8V1noHjp3VBCQ==",
                0
            )

            val encrypted = keyChaCha.encrypt("Evrika!".toByteArray(), null)
            val encryptedString = String(Base64.encode(encrypted, 0))
            val decrypted = keyChaCha.decrypt(decodedServerText, null)
            val text = String(decrypted)
            Log.d("CHKAN", "text - $text")
            Log.d("CHKAN", "encryptedString - $encryptedString")

            // VERSION 2 with Tink library & standart KeyPairGenerator
            /*val keyPairGenerator = KeyPairGenerator.getInstance(KeyProperties.KEY_ALGORITHM_EC)
            val myKeyPair: KeyPair = keyPairGenerator.generateKeyPair()
            binding.tvPrivateKeyResult.text = myKeyPair.private.toString()
            binding.tvPublicKeyResult.text = myKeyPair.public.toString()

            //If you want to share your public key to a server
            val pubKeyString = String(Base64.encode(myKeyPair.public.encoded, 0))
            Log.d("CHKAN", "pubKeyString - $pubKeyString")

            val decodedKey = Base64.decode(pubKeyString,0)

            // generate public key from server's key with string format
            val serverPublicKey = KeyFactory.getInstance(myKeyPair.public.algorithm).generatePublic(
                X509EncodedKeySpec(decodedKey)
            )
            binding.tvSecretResult.text = serverPublicKey.toString()

            // Create a shared secret based on our private key and the other party's public key.
            val keyAgreement: KeyAgreement = KeyAgreement.getInstance("ECDH")
            keyAgreement.init(myKeyPair.private)
            keyAgreement.doPhase(serverPublicKey, true)
            val sharedSecret: ByteArray = keyAgreement.generateSecret()

            // SharedSecret пока нельзя безопасно использовать в качестве ключа. Мы должны запустить его через вывод ключа
            // функция с некоторыми другими данными: "соль" и "информация". Соль является необязательным случайным значением,
            // в этом примере опущено. Рекомендуется включать как открытые ключи, так и любые другие
            // ключевые данные переговоров в info. Здесь мы используем открытые ключи и метку, указывающую
            // сообщения, зашифрованные этим ключом, приходят с сервера.
            //https://developer.android.com/reference/android/security/keystore/KeyGenParameterSpec
            val salt = byteArrayOf()
            val info = ByteArrayOutputStream()
            info.write("ECDH secp256r1 AES-256-GCM-SIV\u0000".toByteArray(StandardCharsets.UTF_8))
            info.write(myKeyPair.public.encoded)
            info.write(serverPublicKey.encoded)

            // This example uses the Tink library and the HKDF key derivation function.
            val key = AesGcmSiv(
                Hkdf.computeHkdf(
                    "HMACSHA256", sharedSecret, salt, info.toByteArray(), 32
                )
            )
            val keyChaCha = ChaCha20Poly1305(
                Hkdf.computeHkdf(
                    "HMACSHA256", sharedSecret, salt, info.toByteArray(), 32
                )
            )

            //binding.tvSecretResult.text = key.toString()
            val encrypted = keyChaCha.encrypt("Evrika!".toByteArray(),null)
            val decrypted = keyChaCha.decrypt(encrypted,null)
            val text = String(decrypted)
            Log.d("CHKAN", "text - $text")*/
        }
    }
}