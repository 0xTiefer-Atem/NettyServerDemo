package org.NettyServer.MyServlet;

import org.NettyServer.HttpDecode.MyHttpRequest;
import org.NettyServer.HttpDecode.MyHttpResponse;


public interface IServlet {
    void doGet(MyHttpRequest request, MyHttpResponse response);
    void doPost(MyHttpRequest request, MyHttpResponse response);

}
