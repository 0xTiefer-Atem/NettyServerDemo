package org.NettyServer;

public class MyPrintWriter {
    private static String mess=null;
    public void printlnAndFlush(String mess1){
        this.mess = mess1;

    }


    public String getMess() {
        System.out.println(mess+"   _+_+_+_+_+_+_+_+_+_+_+_+_+_");
        return mess;
    }
}
