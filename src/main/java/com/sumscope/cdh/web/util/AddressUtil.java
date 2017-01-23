package com.sumscope.cdh.web.util;

import javax.mail.internet.InternetAddress;
import java.io.UnsupportedEncodingException;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

/**
 * Created by Roidder on 2016/11/7.
 */
public class AddressUtil {

    public static String getIp() {
        //inetAddress.getLocalHost().getHostAddress() 如果本地localhost没有增加ip配置直接获取ip的话返回127.0.0.1
        try {
            for (Enumeration en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements(); ) {
                NetworkInterface intf = (NetworkInterface) en.nextElement();
                for (Enumeration enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements(); ) {
                    InetAddress inetAddress = (InetAddress) enumIpAddr.nextElement();
                    if (!inetAddress.isLoopbackAddress() && inetAddress instanceof Inet4Address) {
                        String ipAddress = inetAddress.getHostAddress().toString();
                        return ipAddress;
                    }
                }
            }
        } catch (SocketException ex) {
            System.out.print("Socket exception in GetIP Address of Utilities-----" + ex.toString());
        }
        return null;
    }

    public static String diyTitle(String yourIp, String diyName) {
        String diyIpName = null;
        try {
            diyIpName = new InternetAddress(yourIp, diyName).toString();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return diyIpName;
    }
}
