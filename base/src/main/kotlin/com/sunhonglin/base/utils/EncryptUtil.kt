package com.sunhonglin.base.utils

import android.util.Base64
import com.sunhonglin.base.utils.DataUtil.hexDigits
import java.io.File
import java.io.FileInputStream
import java.io.IOException
import java.nio.channels.FileChannel
import java.security.KeyFactory
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException
import java.security.spec.X509EncodedKeySpec
import javax.crypto.Cipher
import kotlin.experimental.and

class EncryptUtil {

    companion object {
        private const val RSA_Transformation = "RSA/None/PKCS1Padding"


        /**
         * RSA公钥加密
         */
        fun encryptByPublicKey(str: String, publicKey: String): String {
            val x509EncodedKeySpec = X509EncodedKeySpec(Base64.decode(publicKey, Base64.DEFAULT))
            val cipher: Cipher = Cipher.getInstance(RSA_Transformation)
            cipher.init(
                Cipher.ENCRYPT_MODE,
                KeyFactory.getInstance("RSA").generatePublic(x509EncodedKeySpec)
            )
            return Base64.encodeToString(cipher.doFinal(str.toByteArray()), Base64.DEFAULT)
        }

        /**
         * MD5加密文件
         *
         * @param filePath 文件路径
         * @return 文件的16进制密文
         */
        fun encryptMD5File2String(filePath: String): String {
            return encryptMD5File2String(File(filePath))
        }

        /**
         * MD5加密文件
         *
         * @param file 文件
         * @return 文件的16进制密文
         */
        private fun encryptMD5File2String(file: File): String {
            return when {
                encryptMD5File(file) != null -> bytes2HexString(encryptMD5File(file))
                else -> ""
            }
        }

        /**
         * MD5加密文件
         *
         * @param file 文件
         * @return 文件的MD5校验码
         */
        private fun encryptMD5File(file: File): ByteArray? {
            var fis: FileInputStream? = null
            try {
                fis = FileInputStream(file)
                val channel = fis.channel
                val buffer = channel.map(FileChannel.MapMode.READ_ONLY, 0, file.length())
                val md = MessageDigest.getInstance("MD5")
                md.update(buffer)
                return md.digest()
            } catch (e: NoSuchAlgorithmException) {
                e.printStackTrace()
            } catch (e: IOException) {
                e.printStackTrace()
            } finally {
                fis?.apply {
                    close()
                }
            }
            return null
        }

        /**
         * byteArr转hexString
         *
         * 例如：
         * bytes2HexString(new byte[] { 0, (byte) 0xa8 }) returns 00A8
         *
         * @param bytes byte数组
         * @return 16进制大写字符串
         */
        private fun bytes2HexString(bytes: ByteArray?): String {
            if (bytes == null) return ""
            val ret = CharArray(bytes.size shl 1)
            var i = 0
            var j = 0
            while (i < bytes.size) {
                ret[j++] = hexDigits[bytes[i].toInt().ushr(4) and 0x0f]
                ret[j++] = hexDigits[(bytes[i] and 0x0f).toInt()]
                i++
            }
            return String(ret)
        }
    }
}