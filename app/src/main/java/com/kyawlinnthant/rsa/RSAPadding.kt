package com.kyawlinnthant.rsa

import android.annotation.SuppressLint
import android.content.Context
import java.math.BigInteger
import java.security.InvalidKeyException
import java.security.KeyFactory
import java.security.NoSuchAlgorithmException
import java.security.spec.InvalidKeySpecException
import java.security.spec.PKCS8EncodedKeySpec
import java.security.spec.X509EncodedKeySpec
import java.util.*
import javax.crypto.BadPaddingException
import javax.crypto.Cipher
import javax.crypto.IllegalBlockSizeException
import javax.crypto.NoSuchPaddingException

class RSAPadding(private val context: Context) {

    @Throws(
        NoSuchAlgorithmException::class,
        NoSuchPaddingException::class,
        InvalidKeyException::class,
        IllegalBlockSizeException::class,
        BadPaddingException::class,
        NumberFormatException::class,
        InvalidKeySpecException::class
    )
    fun encrypt(plain: String): String {

        val encrypted: String
        val encryptedBytes: ByteArray

        val pk = context.resources.getString(R.string.public_key)
        val i = plain.length

        if (plain.length >= 100) {
            encrypted = "Message Length longer than expected"
            return encrypted
        } else {

            val publicBytes = stringToBytes(pk)
            val keySpec = X509EncodedKeySpec(publicBytes)
            val keyFactory = KeyFactory.getInstance("RSA")
            val publicKey = keyFactory.generatePublic(keySpec)
            val cipher = Cipher.getInstance("RSA")

            cipher.init(Cipher.ENCRYPT_MODE, publicKey)
            encryptedBytes = cipher.doFinal(plain.toByteArray())

            encrypted = bytesToString(encryptedBytes)

            return encrypted

        }

    }

    @Throws(
        NoSuchAlgorithmException::class,
        NoSuchPaddingException::class,
        InvalidKeyException::class,
        IllegalBlockSizeException::class,
        BadPaddingException::class,
        NumberFormatException::class,
        InvalidKeySpecException::class
    )
    fun decrypt(result: String): String {

        val decryptedBytes: ByteArray

        val pk = context.resources.getString(R.string.private_key)

        val clear = stringToBytes(pk)
        val keySpec = PKCS8EncodedKeySpec(clear)
        val fact = KeyFactory.getInstance("RSA")
        val privateKey = fact.generatePrivate(keySpec)
        val decrypted: String
        val cipher = Cipher.getInstance("RSA")
        cipher.init(Cipher.DECRYPT_MODE, privateKey)
        decryptedBytes = cipher.doFinal(stringToBytes(result))
        decrypted = run { String(decryptedBytes) }

        return decrypted

    }

    private fun bytesToString(b: ByteArray): String {
        val b2 = ByteArray(b.size + 1)
        b2[0] = 1
        System.arraycopy(b, 0, b2, 1, b.size)
        return BigInteger(b2).toString(36)
    }


    @SuppressLint("NewApi")
    @Throws(NumberFormatException::class)
    fun stringToBytes(s: String): ByteArray {
        val b2 = BigInteger(s, 36).toByteArray()
        return Arrays.copyOfRange(b2, 1, b2.size)
    }

}