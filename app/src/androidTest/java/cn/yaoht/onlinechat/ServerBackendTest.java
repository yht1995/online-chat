package cn.yaoht.onlinechat;

import junit.framework.Assert;
import junit.framework.TestCase;

import cn.yaoht.onlinechat.model.Friend;

/**
 * Created by yaoht on 2015/12/11.
 * Project: OnlineChat
 */
public class ServerBackendTest extends TestCase {


    public void testLogin() throws Exception {
        final String ip_address = "166.111.140.14";
        final int port = 8000;
        ServerBackend serverBackend = new ServerBackend(ip_address, port);
        Assert.assertEquals(true, serverBackend.Login("2013011515", "net2015"));
    }

    public void testLogout() throws Exception {
        final String ip_address = "166.111.140.14";
        final int port = 8000;
        ServerBackend serverBackend = new ServerBackend(ip_address, port);
        Assert.assertEquals(true, serverBackend.Logout("2013011515"));
    }

    public void testQueryOnlineState() throws Exception {
        final String ip_address = "166.111.140.14";
        final int port = 8000;
        ServerBackend serverBackend = new ServerBackend(ip_address, port);
        Friend friend = new Friend();
        friend.setUser_id("2013011515");
        Assert.assertEquals("n", serverBackend.QueryOnlineState(friend));
    }
}