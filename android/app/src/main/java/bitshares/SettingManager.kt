package bitshares

import com.fowallet.walletcore.bts.ChainObjectManager
import org.json.JSONObject

class SettingManager {

    companion object {
        private var _spInstanceAppCacheMgr = SettingManager()
        fun sharedSettingManager(): SettingManager {
            return _spInstanceAppCacheMgr
        }
    }

    /**
     * 服务器配置(version.json)
     */
    //    "version" => "1.0",
    //    "force" => "0",
    //    "appURL" => "",
    //    "newVersionInfo" => "",
    //    "newVersionInfoEn" => "",
    //
    //    # btspp水龙头地址
    //    "faucetURL" => "",
    //
    //    # => 动态配置wss节点（可以根据语言分别设置）
    //    "wssNodes" => {
    //        # => 默认
    //        "default" => [
    //        "wss://btsapi.magicw.net/ws",
    //        "wss://api.bts.mobi/ws",
    //        ],
    //        # => 中文单独配置
    //        "cn" => [],
    //        # => 英文单独配置
    //        "en" => [],
    //    },
    var serverConfig: JSONObject? = null

    constructor()

    /**
     * 获取记账单位 CNY、USD 等
     */
    fun getEstimateAssetSymbol(): String {
        val settings = _load_setting_hash()
        val value = settings.optString(kSettingKey_EstimateAssetSymbol)
        //  初始化默认值（CNY）
        if (value == null || value == "") {
            settings.put(kSettingKey_EstimateAssetSymbol, "CNY")
            _save_setting_hash(settings)
            return "CNY"
        }

        //  REMARK：如果设置界面保存的计价货币 symbol 在配置的计价列表移除了，则恢复默认值。
        val currency = ChainObjectManager.sharedChainObjectManager().getEstimateUnitBySymbol(value)
        if (currency == null) {
            settings.put(kSettingKey_EstimateAssetSymbol, "CNY")
            _save_setting_hash(settings)
            return "CNY"
        }
        assert(currency.getString("symbol") == value)
        return value
    }

    /**
     * 获取当前主题风格
     */
    fun getThemeInfo(): JSONObject {
        //  TODO:暂不支持
        return JSONObject()
    }


    /**
     *  获取K线指标参数配置信息
     */

    fun getKLineIndexInfos(): JSONObject {
        val settings = _load_setting_hash()
        val value = settings.optJSONObject(kSettingKey_KLineIndexInfo)
        if (value == null) {
            val default_kline_index = ChainObjectManager.sharedChainObjectManager().getDefaultParameters().getJSONObject("default_kline_index")
            settings.put(kSettingKey_KLineIndexInfo, default_kline_index)
            _save_setting_hash(settings)
            return default_kline_index
        }
        return value
    }

    fun setUseConfig(key: String, value: Any) {
        val settings = _load_setting_hash()
        settings.put(key, value)
        _save_setting_hash(settings)
    }

    private fun _load_setting_hash(): JSONObject {
        var fullname = OrgUtils.makeFullPathByAppStorage(kAppCacheNameUserSettingByApp)
        var settings = OrgUtils.load_file_as_json(fullname)
        if (settings == null) {
            settings = JSONObject()
        }
        return settings
    }

    private fun _save_setting_hash(setting: JSONObject) {
        val fullname = OrgUtils.makeFullPathByAppStorage(kAppCacheNameUserSettingByApp)
        OrgUtils.write_file_from_json(fullname, setting)
    }
}