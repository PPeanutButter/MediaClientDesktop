package utils

import java.security.MessageDigest

/**
 * MD5加密
 * @author a
 */
object MD5Utils {
    /***
     * MD5加码 生成32位md5码
     */
    fun string2MD5(inStr: String): String {
        var md5: MessageDigest? = null
        md5 = try {
            MessageDigest.getInstance("MD5")
        } catch (e: Exception) {
            println(e.toString())
            e.printStackTrace()
            return ""
        }
        val charArray = inStr.toCharArray()
        val byteArray = ByteArray(charArray.size)
        for (i in charArray.indices) {
            byteArray[i] = charArray[i].code.toByte()
        }
        val md5Bytes = md5!!.digest(byteArray)
        val hexValue = StringBuffer()
        for (md5Byte in md5Bytes) {
            val `val` = md5Byte.toInt() and 0xff
            if (`val` < 16) {
                hexValue.append("0")
            }
            hexValue.append(Integer.toHexString(`val`))
        }
        return hexValue.toString()
    }

    /**
     * 加密解密算法 执行一次加密，两次解密
     */
    fun convertMD5(inStr: String): String {
        val a = inStr.toCharArray()
        for (i in a.indices) {
            a[i] = (a[i].code xor 't'.code).toChar()
        }
        return String(a)
    }

}