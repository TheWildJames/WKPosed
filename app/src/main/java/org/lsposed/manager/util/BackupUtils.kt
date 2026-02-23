package org.lsposed.manager.util

import android.net.Uri
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import org.lsposed.manager.App
import org.lsposed.manager.ConfigManager
import org.lsposed.manager.adapters.ScopeAdapter
import java.io.IOException
import java.util.zip.GZIPInputStream
import java.util.zip.GZIPOutputStream

object BackupUtils {
    private const val VERSION = 2

    @Throws(JSONException::class, IOException::class)
    @JvmOverloads
    fun backup(uri: Uri, packageName: String? = null) {
        val rootObject = JSONObject()
        rootObject.put("version", VERSION)
        val modulesArray = JSONArray()
        val modules = ModuleUtil.getInstance().modules ?: return
        for (module in modules.values) {
            if (packageName != null && module.packageName != packageName) continue
            val moduleObject = JSONObject()
            moduleObject.put("enable", ModuleUtil.getInstance().isModuleEnabled(module.packageName))
            moduleObject.put("package", module.packageName)
            val scope = ConfigManager.getModuleScope(module.packageName)
            val scopeArray = JSONArray()
            for (s in scope) {
                val app = JSONObject()
                app.put("package", s.packageName)
                app.put("userId", s.userId)
                scopeArray.put(app)
            }
            moduleObject.put("scope", scopeArray)
            modulesArray.put(moduleObject)
        }
        rootObject.put("modules", modulesArray)
        App.getInstance().contentResolver.openOutputStream(uri)?.use { output ->
            GZIPOutputStream(output).use { gzipOutputStream ->
                gzipOutputStream.write(rootObject.toString().toByteArray())
            }
        }
    }

    @Throws(JSONException::class, IOException::class)
    @JvmOverloads
    fun restore(uri: Uri, packageName: String? = null) {
        // ...existing code...
    }
}