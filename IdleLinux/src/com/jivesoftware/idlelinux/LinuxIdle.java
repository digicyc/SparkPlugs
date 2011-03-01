package com.jivesoftware.idlelinux;
/**
 * So we can get idle time of user in Linux.
 *
 * @author Aaron Allred
 */

public class LinuxIdle {


    private static native long idleTime();

    private LinuxIdle() { }

    public static long getIdleTime() {
        return idleTime();
    }

    static {
        try {
            StringBuffer libPath = new StringBuffer();
            libPath.append(System.getProperty("user.home")+"/.Spark/");
            libPath.append("plugins/idlelinux/lib/");

            Runtime.getRuntime().load(libPath.toString()+"libLinuxIdle.so");
        } catch (UnsatisfiedLinkError linkError) {
            linkError.printStackTrace();           
        }
    }
}
