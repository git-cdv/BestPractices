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
            val keyPairGenerator = KeyPairGenerator.getInstance(KeyProperties.KEY_ALGORITHM_EC)
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

            //FOR IOS https://github.com/Blackjacx/Playgrounds/blob/master/playgrounds/CryptoKit.playground/Contents.swift
            // https://stackoverflow.com/questions/34855741/how-to-encrypt-using-aes-gcm-on-ios

            //binding.tvSecretResult.text = key.toString()
            val encrypted = keyChaCha.encrypt("Evrika!".toByteArray(),null)
            val decrypted = keyChaCha.decrypt(encrypted,null)
            val text = String(decrypted)
            Log.d("CHKAN", "text - $text")
        }
    }
}