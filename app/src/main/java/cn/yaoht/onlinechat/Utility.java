package cn.yaoht.onlinechat;

import java.util.regex.Pattern;

/**
 * Created by yaoht on 2015/12/26.
 * Project: OnlineChat
 */

/**
 * @author yaoht
 *         Utilities
 */
public class Utility {
    /**
     * 校验IP地址
     *
     * @param ip_address IP地址
     * @return 是否合法
     */
    static public boolean CheckIPAddress(String ip_address) {
        return Pattern.matches("^(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.)" +
                "{3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)$", ip_address);
    }

    /**
     * 校验用户账号
     *
     * @param user_id 用户帐户
     * @return 是否合法
     */
    static public boolean CheckUserID(String user_id) {
        return Pattern.matches("^2013[0-9]{6}$", user_id);
    }


}
