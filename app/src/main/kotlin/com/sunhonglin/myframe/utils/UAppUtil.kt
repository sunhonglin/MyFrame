package com.sunhonglin.myframe.utils

import android.app.Application
import com.umeng.analytics.MobclickAgent
import com.umeng.commonsdk.BuildConfig
import com.umeng.commonsdk.UMConfigure

/**
 * 合规三步走
 * 1.您需要确保App有《隐私政策》，并且在用户首次启动App时就弹出《隐私政策》取得用户同意。
 * 2.您务必告知用户您选择友盟+SDK服务，请在《隐私政策》中增加如下参考条款：“我们的产品集成友盟+SDK，友盟+SDK需要收集您的设备Mac地址、唯一设备识别码（IMEI/android ID/IDFA/OPENUDID/GUID、SIM 卡 IMSI 信息）以提供统计分析服务，并通过地理位置校准报表数据准确性，提供基础反作弊能力。”
 * 3.您务必确保用户同意《隐私政策》之后，再初始化友盟+SDK。
 */
class UAppUtil {

    /**
     * 两次使用App之间间隔时间，影响到启动次数
     * interval 单位为毫秒，如果想设定为40秒，interval应为 40*1000.
     * interval最小有效值为30秒。
     */
    fun sessionContinueMillis(interval: Long) {
        MobclickAgent.setSessionContinueMillis(interval)
    }

    /**
     * 每台设备仅记录首次安装激活的渠道，在其他渠道再次安装不会重复计量。 所以在测试不同的渠道的时候，请使用不同的设备来分别测试不要改变’UMENG_CHANNEL’。
     * 可以由英文字母、阿拉伯数字、下划线、中划线、空格、括号组成，可以含汉字以及其他明文字符，但是不建议使用中文命名，会出现乱码。
     * 首尾字符不可以为空格。
     * 不要使用纯数字作为渠道ID。
     */
    fun Application.proInit() {
        UMConfigure.preInit(this, "5be10e2ff1f556a56d000128", BuildConfig.FLAVOR)
    }


}