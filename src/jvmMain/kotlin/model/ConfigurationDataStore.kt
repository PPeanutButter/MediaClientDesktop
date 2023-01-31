package model

import com.alibaba.fastjson.JSON
import data.Configuration
import kotlinx.coroutines.*
import net.harawata.appdirs.AppDirsFactory
import java.io.File


class ConfigurationDataStore {
    private lateinit var file: File

    companion object{
        private const val TAG = "Configuration"
    }

    init {
        val appDirs = AppDirsFactory.getInstance()
        val root = appDirs.getUserConfigDir("NAS", "release", "PeanutButter")
        File(root).mkdirs()
        file = File(root, "settings.cfg")
        MainScope().launch(Dispatchers.IO){
            file.apply { if (!this.exists()) saveConfiguration(Configuration.Empty) }
        }
    }

    suspend fun getConfiguration(): Configuration{
        return withContext(Dispatchers.IO){
            return@withContext JSON.parseObject(file.readText(), Configuration::class.java)
        }
    }

    suspend fun saveConfiguration(configuration: Configuration){
        withContext(Dispatchers.IO){
            file.writeText(text = JSON.toJSONString(configuration))
        }
    }
}