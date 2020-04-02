package org.NettyServer.HttpDecode;

import org.NettyServer.MyPrintWriter;

public class MyHttpResponse {
    private static MyPrintWriter myPrintWriter = new MyPrintWriter();
    public MyPrintWriter getWriter(){
        if (myPrintWriter == null){
            myPrintWriter = new MyPrintWriter();
            return myPrintWriter;
        }
        return myPrintWriter;
    }
}
