package org1;

import io.netty.handler.codec.http.DefaultFullHttpRequest;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.HttpRequest;

import java.io.*;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.Channel;
import java.nio.channels.FileChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;

public class MyHttpHandler {
    private static int PORT = 80;
    private Socket mysocket = null;
    private FullHttpRequest request = null;
    //    private SocketChannel inchannel = null;
//    private Charset charset = Charset.forName("utf-8");
//    private ByteBuffer buffer = ByteBuffer.allocate(1024);
    private BufferedReader bfr = null;
    private PrintStream out = null;
    private StringBuffer sb = new StringBuffer();

    public MyHttpHandler(FullHttpRequest request1) {
        this.request = request1;
        System.out.println(request.toString());
        System.out.println("\n");
        try {
            mysocket = new Socket("www.baidu.com", PORT);
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            mappingHandler();
        } catch (IOException e) {
            System.out.println("handler方法出错了");
        }
    }

    private void mappingHandler() throws IOException {
//        Channel channel = mysocket.getChannel();
//        while ((((SocketChannel) channel).read(buffer))!=-1){
//            buffer.flip();
//            CharsetDecoder decoder = charset.newDecoder();
//            CharBuffer charBuffer = decoder.decode(buffer);
//            System.out.println(String.valueOf(charBuffer));
//            buffer.clear();
//        }

        String address = "www.baidu.com";
        String line = "\r\n";
        System.out.println("收到请求");
        StringBuffer requestheader = new StringBuffer();
        requestheader.append("GET / HTTP/1.1" + line);
        requestheader.append("Host: " + address + line + line);
        requestheader.append("Accept-Charset: UTF-8" + line);
        requestheader.append("Accept: text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8" + line);
        requestheader.append("Accept-Language: zh-CN,zh;q=0.8" + line);
        requestheader.append("User-Agent: Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/53.0.2785.116 Safari/537.36" + line);


        out = new PrintStream(mysocket.getOutputStream());
        out.println(requestheader.toString());
        out.flush();


        System.out.println("=======================");


        System.out.println("正在请求百度...\n");
        System.out.println("请求成功\n");


        bfr = new BufferedReader(new InputStreamReader(mysocket.getInputStream()));
        String mess = null;
        boolean flag = false;
        while ((mess = bfr.readLine()) != null) {
            if (mess.equals("</body></html>")) {
                sb.append(mess);
            }
            sb.append(mess);
        }
    }

    public String getmess() {
        System.out.println("------------------------");
        System.out.println(sb.toString());
        return sb.toString();
    }
}