package org.NettyServer.MyServlet;

import org.NettyServer.MyPrintWriter;
import org.NettyServer.HttpDecode.MyHttpRequest;
import org.NettyServer.HttpDecode.MyHttpResponse;

public class MyServlet implements IServlet {
    @Override
    public void doGet(MyHttpRequest request, MyHttpResponse response) {
        //doPost(request,response);
        String mess = (String) request.getGetParameter("name");
        MyPrintWriter mptr = response.getWriter();
        mptr.printlnAndFlush(mess);
    }

    @Override
    public void doPost(MyHttpRequest request, MyHttpResponse response) {
        String a = (String) request.getPostParameter("name");
        MyPrintWriter ptr = response.getWriter();
        ptr.printlnAndFlush(a);
    }
}
