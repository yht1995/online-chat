package cn.yaoht.onlinechat;

import junit.framework.Assert;
import junit.framework.TestCase;

import cn.yaoht.onlinechat.midware.ServerMidware;
import cn.yaoht.onlinechat.model.Friend;

/**
 * Created by yaoht on 2015/12/11.
 * Project: OnlineChat
 */
public class ServerMidwareTest extends TestCase {


    public void testLogin() throws Exception {
        final String ip_address = "166.111.140.14";
        final int port = 8000;
        ServerMidware serverMidware = new ServerMidware(ip_address, port);
    }

    public void testLogout() throws Exception {
        final String ip_address = "166.111.140.14";
        final int port = 8000;
        ServerMidware serverMidware = new ServerMidware(ip_address, port);
    }

    public void testQueryOnlineState() throws Exception {
        final String ip_address = "166.111.140.14";
        final int port = 8000;
        ServerMidware serverMidware = new ServerMidware(ip_address, port);
        Friend friend = new Friend();
        friend.setUser_id("2013011515");
        Assert.assertEquals("n", serverMidware.QueryOnlineState(friend));
    }
}