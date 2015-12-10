package cn.yaoht.onlinechat;

import junit.framework.TestCase;

/**
 * Created by yaoht on 2015/12/11.
 * Project: OnlineChat
 */
public class ServerBackendTest extends TestCase {


    public void testLogin() throws Exception {
        final String ip_address = "166.111.140.14";
        final int port = 8000;
        ServerBackend serverBackend = new ServerBackend(ip_address, port);
        serverBackend.Login("2013011521", "net2015");
        Thread.sleep(1 * 1000);
        assertEquals(Boolean.TRUE, serverBackend.getIs_online());
    }

    public void testLogout() throws Exception {
        final String ip_address = "166.111.140.14";
        final int port = 8000;
        ServerBackend serverBackend = new ServerBackend(ip_address, port);
        serverBackend.Logout("2013011521");
        Thread.sleep(1 * 1000);
        assertEquals(Boolean.FALSE, serverBackend.getIs_online());
    }
}