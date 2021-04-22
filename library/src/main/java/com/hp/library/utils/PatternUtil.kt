package com.hp.library.utils

import android.annotation.SuppressLint
import android.text.TextUtils
import java.text.SimpleDateFormat
import java.util.*
import java.util.regex.Pattern

/**
 * 字符串匹配工具类
 *
 */
object PatternUtil {
    private val TAG = PatternUtil::class.java.simpleName

    /*
	 * 正则验证邮箱
	 */
    fun patternEmail(email: String?): Boolean {
        // 电子邮件
        val check = "^\\s*\\w+(?:\\.{0,1}[\\w-]+)*@[a-zA-Z0-9]+(?:[-.][a-zA-Z0-9]+)*\\.[a-zA-Z]+\\s*$"
        // String check1 =
        // "^[a-zA-Z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-zA-Z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-zA-Z0-9](?:[a-zA-Z0-9-]*[a-zA-Z0-9])?\\.)+[a-zA-Z0-9](?:[a-zA-Z0-9-]*[a-zA-Z0-9])?";
        val regex = Pattern.compile(check)
        val matcher = regex.matcher(email)
        return matcher.matches()
    }

    /*
	 * 正则匹配测量数据
	 */
    fun patter(data: String?): Boolean {
        val reg = "^\\d+$"
        val regex = Pattern.compile(reg)
        val matcher = regex.matcher(data)
        return matcher.matches()
    }

    /*
	 * 正则匹配年龄在0-120岁之间的数字
	 */
    fun patterAge(data: String?): Boolean {
        val reg = "^([0-9]|[0-9]{2}|120)$"
        val regex = Pattern.compile(reg)
        val matcher = regex.matcher(data)
        return matcher.matches()
    }

    fun patterHeight(data: String?): Boolean {
        val reg = "^([0-9]|[0-9]{1-3}|250)$"
        val regex = Pattern.compile(reg)
        val matcher = regex.matcher(data)
        return matcher.matches()
    }

    /*
	 * 正则匹配double类型的数据
	 */
    fun patterDouble(data: String?): Boolean {
        val reg = "^\\-?[0-9]*\\.[0-9]*$"
        val regex = Pattern.compile(reg)
        val matcher = regex.matcher(data)
        return matcher.find()
    }

    /**
     * 匹配int
     * @param data
     * @return
     */
    fun patterInt(data: String?): Boolean {
        if (null == data || "" == data) {
            return false
        }
        val pattern = Pattern.compile("^[-\\+]?[\\d]*$")
        return pattern.matcher(data).matches()
    }

    /**
     * 匹配double、float和int型
     * @param data
     * @return
     */
    fun patterDoubleOrInt(data: String?): Boolean {
        return patterDouble(data) || patterInt(data)
    }

    /*
	 * 匹配手机号码
	 */
    fun patterPhone(num: String?): Boolean {
//		String regExp = "^((13[0-9])|(15[^4,\\D])|(17[0,6,7,8])|(18[0-9])|(14[5,7]))\\d{8}$";
        val regExp = "^(1)\\d{10}$"
        val p = Pattern.compile(regExp)
        val m = p.matcher(num)
        return m.find()
    }

    /**
     * 匹配短信验证码
     * @param code
     * @return
     */
    fun patterCertificationCode(code: String): Boolean {
        return isNumeric(code) && code.length == 6
    }

    /*
	 * 正在匹配注册密码
	 */
    fun patterPasswrod(password: String?): Boolean {
//		String regExp = "^[\\da-zA-Z]{6,20}$";
//		String regExp = "^[^\\da-zA-Z]+${6,20}$";
//		String regExp = "^[\\da-zA-Z~`!@#$%^&*()_+=-]{6,20}$";
        val regExp = "^[\\da-zA-Z~！@#%“”*？（）\\-_：；/`=+$…\"\"^\\[\\]|~!'&?():;-]{6,20}$"
        val p = Pattern.compile(regExp)
        val m = p.matcher(password)
        return m.find()
    }

    /**
     * 匹配身份证号码合法性
     *
     * @param IDStr
     * @return
     */
    @SuppressLint("SimpleDateFormat")
    fun patternIDCard(IDStr: String): Boolean {
        var IDStr = IDStr
        if (TextUtils.isEmpty(IDStr)) {
            return false
        }
        val ValCodeArr = arrayOf("1", "0", "x", "9", "8", "7", "6", "5", "4", "3", "2")
        val Wi = arrayOf("7", "9", "10", "5", "8", "4", "2", "1", "6", "3", "7", "9", "10", "5", "8", "4", "2")
        var Ai = ""
        // ================ 号码的长度 15位或18位 ================
        if (IDStr.length != 15 && IDStr.length != 18) {
            return false
        }
        IDStr = IDStr.toLowerCase()

        // ================ 数字 除最后以为都为数字 ================
        if (IDStr.length == 18) {
            Ai = IDStr.substring(0, 17)
        } else if (IDStr.length == 15) {
            Ai = IDStr.substring(0, 6) + "19" + IDStr.substring(6, 15)
        }
        if (isNumeric(Ai) == false) {
            // errorInfo = "身份证15位号码都应为数字 ; 18位号码除最后一位外，都应为数字。";
            return false
        }

        // ================ 出生年月是否有效 ================
        val strYear = Ai.substring(6, 10) // 年份
        val strMonth = Ai.substring(10, 12) // 月份
        val strDay = Ai.substring(12, 14) // 月份
        if (isDate("$strYear-$strMonth-$strDay") == false) {
            // errorInfo = "身份证生日无效。";
            return false
        }
        val gc = GregorianCalendar()
        val s = SimpleDateFormat("yyyy-MM-dd")
        try {
            if (gc[Calendar.YEAR] - strYear.toInt() > 150
                    || gc.time.time - s.parse("$strYear-$strMonth-$strDay").time < 0) {
                // errorInfo = "身份证生日不在有效范围。";
                return false
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        if (strMonth.toInt() > 12 || strMonth.toInt() == 0) {
            // errorInfo = "身份证月份无效";
            return false
        }
        if (strDay.toInt() > 31 || strDay.toInt() == 0) {
            // errorInfo = "身份证日期无效";
            return false
        }

        // ================ 地区码时候有效 ================
        val h = GetAreaCode()
        if (h[Ai.substring(0, 2)] == null) {
            // errorInfo = "身份证地区编码错误。";
            return false
        }

        // ================ 判断最后一位的值 ================
        var TotalmulAiWi = 0
        for (i in 0..16) {
            TotalmulAiWi = TotalmulAiWi + Ai[i].toString().toInt() * Wi[i].toInt()
        }
        val modValue = TotalmulAiWi % 11
        val strVerifyCode = ValCodeArr[modValue]
        Ai = Ai + strVerifyCode
        if (IDStr.length == 18) {
            if (Ai == IDStr == false) {
                // errorInfo = "身份证无效，不是合法的身份证号码";
                return false
            }
        }
        return true
    }

    /**
     * 功能：判断字符串是否为数字
     *
     * @param str
     * @return
     */
    private fun isNumeric(str: String): Boolean {
        val pattern = Pattern.compile("[0-9]*")
        val isNum = pattern.matcher(str)
        return if (isNum.matches()) {
            true
        } else {
            false
        }
    }

    /**
     * 功能：判断字符串是否为日期格式
     *
     * @return
     */
    private fun isDate(strDate: String): Boolean {
        val pattern = Pattern.compile(
                "^((\\d{2}(([02468][048])|([13579][26]))[\\-\\/\\s]?((((0?[13578])|(1[02]))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])"
                        + "|(3[01])))|(((0?[469])|(11))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(30)))|(0?2[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])))))"
                        + "|(\\d{2}(([02468][1235679])|([13579][01345789]))[\\-\\/\\s]?((((0?[13578])|(1[02]))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])"
                        + "|(3[01])))|(((0?[469])|(11))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(30)))|(0?2[\\-\\/\\s]?((0?[1-9])|(1[0-9])|(2[0-8]))))))"
                        + "(\\s(((0?[0-9])|([1-2][0-3]))\\:([0-5]?[0-9])((\\s)|(\\:([0-5]?[0-9])))))?$")
        val m = pattern.matcher(strDate)
        return if (m.matches()) {
            true
        } else {
            false
        }
    }

    /**
     * 功能：设置地区编码
     *
     * @return Hashtable 对象
     */
    private fun GetAreaCode(): Hashtable<*, *> {
        val hashtable = Hashtable<Any?, Any?>()
        hashtable["11"] = "北京"
        hashtable["12"] = "天津"
        hashtable["13"] = "河北"
        hashtable["14"] = "山西"
        hashtable["15"] = "内蒙古"
        hashtable["21"] = "辽宁"
        hashtable["22"] = "吉林"
        hashtable["23"] = "黑龙江"
        hashtable["31"] = "上海"
        hashtable["32"] = "江苏"
        hashtable["33"] = "浙江"
        hashtable["34"] = "安徽"
        hashtable["35"] = "福建"
        hashtable["36"] = "江西"
        hashtable["37"] = "山东"
        hashtable["41"] = "河南"
        hashtable["42"] = "湖北"
        hashtable["43"] = "湖南"
        hashtable["44"] = "广东"
        hashtable["45"] = "广西"
        hashtable["46"] = "海南"
        hashtable["50"] = "重庆"
        hashtable["51"] = "四川"
        hashtable["52"] = "贵州"
        hashtable["53"] = "云南"
        hashtable["54"] = "西藏"
        hashtable["61"] = "陕西"
        hashtable["62"] = "甘肃"
        hashtable["63"] = "青海"
        hashtable["64"] = "宁夏"
        hashtable["65"] = "新疆"
        hashtable["71"] = "台湾"
        hashtable["81"] = "香港"
        hashtable["82"] = "澳门"
        hashtable["91"] = "国外"
        return hashtable
    }

    fun isEmpty(s: String?): Boolean {
        if (s == null) return true
        if (s.length == 0) return true
        return if (s.trim { it <= ' ' }.length == 0) true else false
    }

    // 校验Tag Alias 只能是数字,英文字母和中文
    fun isValidTagAndAlias(s: String?): Boolean {
        val p = Pattern.compile("^[\u4E00-\u9FA50-9a-zA-Z_-]{0,}$")
        val m = p.matcher(s)
        return m.matches()
    }

    /**
     * 校验用户注册密码，密码只能包含数字和字母
     */
    fun patternPassword(pwd: String): Boolean {
        val regex = "^[0-9A-Za-z]{6,16}$"
        return pwd.matches(Regex(regex))
    }

    // 根据Unicode编码完美的判断中文汉字和符号
    private fun patternChinese(c: Char): Boolean {
        val ub = Character.UnicodeBlock.of(c)
        return (ub === Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS
                || ub === Character.UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS
                || ub === Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A
                || ub === Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_B
                || ub === Character.UnicodeBlock.CJK_SYMBOLS_AND_PUNCTUATION
                || ub === Character.UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS
                || ub === Character.UnicodeBlock.GENERAL_PUNCTUATION)
    }

    /**
     * 判断字符串是否为全中文
     * @param strName
     * @return
     */
    fun patternChinese(strName: String): Boolean {
        val ch = strName.toCharArray()
        for (i in ch.indices) {
            val c = ch[i]
            if (!patternChinese(c)) {
                return false
            }
        }
        return true
    }
}