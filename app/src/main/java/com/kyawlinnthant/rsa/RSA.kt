package com.kyawlinnthant.rsa

import android.util.Base64
import java.io.BufferedReader
import java.io.IOException
import java.io.StringReader
import java.nio.charset.StandardCharsets
import java.security.*
import java.security.spec.InvalidKeySpecException
import java.security.spec.PKCS8EncodedKeySpec
import java.security.spec.X509EncodedKeySpec
import javax.crypto.BadPaddingException
import javax.crypto.Cipher
import javax.crypto.IllegalBlockSizeException
import javax.crypto.NoSuchPaddingException


class RSA @Throws(
    NoSuchAlgorithmException::class,
    NoSuchPaddingException::class,
    InvalidKeyException::class,
    IllegalBlockSizeException::class,
    BadPaddingException::class
)
constructor() {

    private var publicKey: PublicKey? = null
    private var privateKey: PrivateKey? = null
    private val encrypted: String? = null

    init {
        generateKeyPair()
    }

    companion object {

        private val CRYPTO_METHOD = "RSA"
        private val CRYPTO_BITS = 2048

        @Throws(
            NoSuchAlgorithmException::class,
            NoSuchPaddingException::class,
            InvalidKeyException::class,
            IllegalBlockSizeException::class,
            BadPaddingException::class
        )

        fun stringToPublicKey(publicKeyString: String): PublicKey? {
            var pk = publicKeyString

            try {
                if (pk.contains("-----BEGIN PUBLIC KEY-----") ||
                    pk.contains("-----END PUBLIC KEY-----")
                )

                    pk = pk.replace("-----BEGIN PUBLIC KEY-----", "")
                        .replace("-----END PUBLIC KEY-----", "")


                val keyBytes = Base64.decode(pk, Base64.DEFAULT)
                val spec = X509EncodedKeySpec(keyBytes)
                val keyFactory = KeyFactory.getInstance("RSA")

                return keyFactory.generatePublic(spec)

            } catch (e: NoSuchAlgorithmException) {
                e.printStackTrace()
                return null
            } catch (e: InvalidKeySpecException) {
                e.printStackTrace()
                return null
            }

        }

        @Throws(
            NoSuchAlgorithmException::class,
            NoSuchPaddingException::class,
            InvalidKeyException::class,
            IllegalBlockSizeException::class,
            BadPaddingException::class
        )
        fun stringToPrivate(private_key: String): PrivateKey? {

            try {
                // Read in the key into a String
                val pkcs8Lines = StringBuilder()
                val rdr = BufferedReader(StringReader(private_key))
                val line: String = rdr.readLine()
                while ((rdr.readLine()) != null) {
                    pkcs8Lines.append(line)
                }

                // Remove the "BEGIN" and "END" lines, as well as any whitespace

                var pkcs8Pem = pkcs8Lines.toString()
                pkcs8Pem = pkcs8Pem.replace("-----BEGIN PRIVATE KEY-----", "")
                pkcs8Pem = pkcs8Pem.replace("-----END PRIVATE KEY-----", "")
                pkcs8Pem = pkcs8Pem.replace("\\s+".toRegex(), "")

                // Base64 decode the result

                val pkcs8EncodedBytes = Base64.decode(pkcs8Pem, Base64.DEFAULT)

                // extract the private key

                val keySpec = PKCS8EncodedKeySpec(pkcs8EncodedBytes)
                val kf = KeyFactory.getInstance("RSA")
                return kf.generatePrivate(keySpec)

            } catch (e: NoSuchAlgorithmException) {
                e.printStackTrace()

                return null
            } catch (e: InvalidKeySpecException) {
                e.printStackTrace()
                return null
            } catch (e: IOException) {
                e.printStackTrace()
            }

            return null


        }
    }

    /**
     * rsaEncrypt plain text to RSA encrypted and Base64 encoded string
     *
     * @param args args[0] should be plain text that will be encrypted
     * If args[1] is be, it should be RSA public key to be used as rsaEncrypt public key
     * @return a encrypted string that Base64 encoded
     * @throws NoSuchAlgorithmException
     * @throws NoSuchPaddingException
     * @throws InvalidKeyException
     * @throws IllegalBlockSizeException
     * @throws BadPaddingException
     */
    @Throws(
        NoSuchAlgorithmException::class,
        NoSuchPaddingException::class,
        InvalidKeyException::class,
        IllegalBlockSizeException::class,
        BadPaddingException::class
    )
    fun rsaEncrypt(vararg args: Any): String {

        val plain = args[0] as String
        val rsaPublicKey: PublicKey? = if (args.size == 1) this.publicKey else args[1] as PublicKey

        val cipher = Cipher.getInstance("RSA/ECB/OAEPWithSHA1AndMGF1Padding")
        cipher.init(Cipher.ENCRYPT_MODE, rsaPublicKey)
        val encryptedBytes = cipher.doFinal(plain.toByteArray(StandardCharsets.UTF_8))
        return Base64.encodeToString(encryptedBytes, Base64.DEFAULT)
    }

    @Throws(
        NoSuchAlgorithmException::class,
        NoSuchPaddingException::class,
        InvalidKeyException::class,
        IllegalBlockSizeException::class,
        BadPaddingException::class
    )
    fun rsaDecrypt(result: String): String {

        val cipher1 = Cipher.getInstance("RSA/ECB/OAEPWithSHA1AndMGF1Padding")
        cipher1.init(Cipher.DECRYPT_MODE, privateKey)
        val decryptedBytes = cipher1.doFinal(Base64.decode(result, Base64.DEFAULT))

        return String(decryptedBytes)
    }

    @Throws(
        NoSuchAlgorithmException::class,
        NoSuchPaddingException::class,
        InvalidKeyException::class,
        IllegalBlockSizeException::class,
        BadPaddingException::class
    )
    fun getPublicKey(option: String): String? {

        when (option) {

            "pkcs1-pem" -> {
                var pkcs1pem = "-----BEGIN RSA PUBLIC KEY-----\n"
                pkcs1pem += Base64.encodeToString(publicKey!!.encoded, Base64.DEFAULT)
                pkcs1pem += "-----END RSA PUBLIC KEY-----"

                return pkcs1pem
            }

            "pkcs8-pem" -> {
                var pkcs8pem = "-----BEGIN PUBLIC KEY-----\n"
                pkcs8pem += Base64.encodeToString(publicKey!!.encoded, Base64.DEFAULT)
                pkcs8pem += "-----END PUBLIC KEY-----"

                return pkcs8pem
            }

            "base64" -> return Base64.encodeToString(publicKey!!.encoded, Base64.DEFAULT)

            else -> return null
        }

    }

    @Throws(
        NoSuchAlgorithmException::class,
        NoSuchPaddingException::class,
        InvalidKeyException::class,
        IllegalBlockSizeException::class,
        BadPaddingException::class
    )
    private fun generateKeyPair() {

        val kpg = KeyPairGenerator.getInstance(CRYPTO_METHOD)
        kpg.initialize(CRYPTO_BITS)
        val kp = kpg.genKeyPair()
        publicKey = kp.public
        privateKey = kp.private
    }

}


