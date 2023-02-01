package utils

import com.alibaba.fastjson.JSON
import com.alibaba.fastjson.JSONObject
import data.Configuration
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import net.harawata.appdirs.AppDirsFactory
import java.io.File

object SettingManager {
    private var sharedPreferences: File? = null

    var token: String
        get() = getValue("token", "")
        set(value) = setValue("token", value)

    fun init() {
        if (null == sharedPreferences){
            synchronized("SettingManager.sharedPreferences"){
                if (null == sharedPreferences) {
                    val appDirs = AppDirsFactory.getInstance()
                    val root = appDirs.getUserConfigDir("NAS", "release", "PeanutButter")
                    File(root).mkdirs()
                    sharedPreferences = File(root, "com.peanut.pc.nas_preferences")
                    sharedPreferences?.apply { if (!this.exists()) this.createNewFile() }
                }
            }
        }

    }

    /**
     * 封装一些方法
     */
    operator fun<T> set(key: String, value: T) = setValue(key, value)
    private fun map(key: String):Set<String> = getValue(key, emptySet())
    fun plus(key: String,newValue:String){
        this@SettingManager[key] = map(key).plus(newValue)
    }
    operator fun plusAssign(pair: Pair<String,String>) {
        this@SettingManager[pair.first] = map(pair.first).plus(pair.second)
    }

    /**
     * 原始方法
     */
    @Suppress("UNCHECKED_CAST")
    fun <T> getValue(key: String, defaultValue: T): T {
        return when (defaultValue) {
            is Boolean -> sharedPreferences!!.getBoolean(key, defaultValue) as T
            is String -> sharedPreferences!!.getString(key, defaultValue) as T
            is Int -> sharedPreferences!!.getInt(key, defaultValue) as T
            is Float -> sharedPreferences!!.getFloat(key, defaultValue) as T
            is Long -> sharedPreferences!!.getLong(key, defaultValue) as T
            else -> sharedPreferences!!.getStringSet(key, defaultValue as Set<String>) as T
        }
    }

    private fun <T> setValue(key: String, value: T) {
        val sharedPreferences = readAll(sharedPreferences)
        sharedPreferences?.put(key, value)
        this.sharedPreferences?.writeText(JSON.toJSONString(sharedPreferences))
    }

    /**
     * 代替
     */
    private fun File.getBoolean(key: String, defaultValue: Boolean):Boolean{
        val j = readAll(this)
        return j?.getBoolean(key)?:defaultValue
    }
    private fun File.getString(key: String, defaultValue: String):String{
        val j = readAll(this)
        return j?.getString(key)?:defaultValue
    }
    private fun File.getInt(key: String, defaultValue: Int):Int{
        val j = readAll(this)
        return j?.getInteger(key)?:defaultValue
    }
    private fun File.getFloat(key: String, defaultValue: Float):Float{
        val j = readAll(this)
        return j?.getFloat(key)?:defaultValue
    }
    private fun File.getLong(key: String, defaultValue: Long):Long{
        val j = readAll(this)
        return j?.getLong(key)?:defaultValue
    }
    private fun File.getStringSet(key: String, defaultValue: Set<String>):Set<String>{
        val j = readAll(this)
        val r = j?.getJSONArray(key) ?: return defaultValue
        return mutableSetOf<String>().apply {
            r.forEach {
                if (it is String){
                    this.add(it)
                }
            }
        }
    }
    private fun readAll(file: File?):JSONObject?{
        val a = file?.readText()
        return JSON.parseObject(if (a.isNullOrEmpty()) "{}" else a)
    }
}