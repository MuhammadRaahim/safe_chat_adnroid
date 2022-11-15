package com.instances.safechat.utils

import android.annotation.SuppressLint
import android.util.Patterns
import android.view.View
import com.google.android.material.snackbar.Snackbar
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.instances.safechat.db.Chat
import com.instances.safechat.utils.Constants.Companion.SALTVALUE
import com.instances.safechat.utils.Constants.Companion.SECRET_KEY
import java.lang.reflect.Type
import java.nio.charset.StandardCharsets
import java.security.InvalidAlgorithmParameterException
import java.security.InvalidKeyException
import java.security.NoSuchAlgorithmException
import java.security.spec.InvalidKeySpecException
import java.security.spec.KeySpec
import java.util.*
import javax.crypto.*
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.PBEKeySpec
import javax.crypto.spec.SecretKeySpec
import kotlin.collections.ArrayList

class BaseUtils {

    companion object{

        /* Encryption Method */
        @SuppressLint("NewApi")
        fun encrypt(strToEncrypt: String): String? {
            try {
                /* Declare a byte array. */
                val iv = byteArrayOf(0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0)
                val ivspec = IvParameterSpec(iv)
                /* Create factory for secret keys. */
                val factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256")
                /* PBEKeySpec class implements KeySpec interface. */
                val spec: KeySpec =
                    PBEKeySpec(SECRET_KEY.toCharArray(), SALTVALUE.toByteArray(), 65536, 256)
                val tmp = factory.generateSecret(spec)
                val secretKey = SecretKeySpec(tmp.encoded, "AES")
                val cipher = Cipher.getInstance("AES/CBC/PKCS5Padding")
                cipher.init(Cipher.ENCRYPT_MODE, secretKey, ivspec)
                /* Retruns encrypted value. */return Base64.getEncoder()
                    .encodeToString(cipher.doFinal(strToEncrypt.toByteArray(StandardCharsets.UTF_8)))
            } catch (e: InvalidAlgorithmParameterException) {
                println("Error occured during encryption: $e")
            } catch (e: InvalidKeyException) {
                println("Error occured during encryption: $e")
            } catch (e: NoSuchAlgorithmException) {
                println("Error occured during encryption: $e")
            } catch (e: InvalidKeySpecException) {
                println("Error occured during encryption: $e")
            } catch (e: BadPaddingException) {
                println("Error occured during encryption: $e")
            } catch (e: IllegalBlockSizeException) {
                println("Error occured during encryption: $e")
            } catch (e: NoSuchPaddingException) {
                println("Error occured during encryption: $e")
            }
            return null
        }

        /* Decryption Method */
        @SuppressLint("NewApi")
        fun decrypt(strToDecrypt: String?): String? {
            try {
                /* Declare a byte array. */
                val iv = byteArrayOf(0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0)
                val ivspec = IvParameterSpec(iv)
                /* Create factory for secret keys. */
                val factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256")
                /* PBEKeySpec class implements KeySpec interface. */
                val spec: KeySpec =
                    PBEKeySpec(SECRET_KEY.toCharArray(), SALTVALUE.toByteArray(), 65536, 256)
                val tmp = factory.generateSecret(spec)
                val secretKey = SecretKeySpec(tmp.encoded, "AES")
                val cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING")
                cipher.init(Cipher.DECRYPT_MODE, secretKey, ivspec)
                /* Retruns decrypted value. */return String(cipher.doFinal(Base64.getDecoder()
                    .decode(strToDecrypt)))
            } catch (e: InvalidAlgorithmParameterException) {
                println("Error occured during decryption: $e")
            } catch (e: InvalidKeyException) {
                println("Error occured during decryption: $e")
            } catch (e: NoSuchAlgorithmException) {
                println("Error occured during decryption: $e")
            } catch (e: InvalidKeySpecException) {
                println("Error occured during decryption: $e")
            } catch (e: BadPaddingException) {
                println("Error occured during decryption: $e")
            } catch (e: IllegalBlockSizeException) {
                println("Error occured during decryption: $e")
            } catch (e: NoSuchPaddingException) {
                println("Error occured during decryption: $e")
            }
            return null
        }

        fun showMessage(view: View, message: String, isError: Boolean = false) {
            val snackBar = Snackbar.make(view, message, Snackbar.LENGTH_LONG)
            val snackBarView = snackBar.view
            val context = view.context

            if (isError) {
                snackBarView.setBackgroundColor(context.resources.getColor(android.R.color.holo_red_light))
            } else {
                snackBarView.setBackgroundColor(context.resources.getColor(android.R.color.holo_green_light))
            }

            snackBar.show()
        }

        fun isValidEmail(str: String): Boolean {
            return Patterns.EMAIL_ADDRESS.matcher(str).matches()
        }

        // covert chat list into json string
        fun fromGsonToJson(chatList: ArrayList<Chat?>?): String? {
            if (chatList == null) {
                return null
            }
            val gson = Gson()
            val type: Type =
                object : TypeToken<ArrayList<Chat?>?>() {}.type
            return gson.toJson(chatList, type)
        }

        fun jsonToGson(jsonList: String?): ArrayList<Chat>? {
            if (jsonList == null) {
                return null
            }
            val gson = Gson()
            val type =
                object : TypeToken<ArrayList<Chat?>?>() {}.type
            return gson.fromJson<ArrayList<Chat>>(jsonList, type)
        }


    }
}