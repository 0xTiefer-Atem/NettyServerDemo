package org.NettyServer.HttpDecode;

import io.netty.buffer.ByteBuf;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.multipart.Attribute;
import io.netty.handler.codec.http.multipart.HttpPostRequestDecoder;
import io.netty.handler.codec.http.multipart.InterfaceHttpData;
import io.netty.handler.codec.http.multipart.InterfaceHttpPostRequestDecoder;
import org.NettyServer.MyPrintWriter;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HttpDecoderPost {
    private FullHttpRequest request = null;
    private HttpPostRequestDecoder decoder = null;
    public static Map parmMap = new HashMap();
    private List<InterfaceHttpData> parmList = null;
    private MyPrintWriter myPostPrintWriter= new MyPrintWriter();

    public HttpDecoderPost(FullHttpRequest request1) throws UnsupportedEncodingException {
        this.request = request1;
        System.out.println(request.uri()+"===========1");

        ByteBuf bf = request.content();
        byte[] bytes = new byte[bf.capacity()];
        bf.readBytes(bytes);

        String s = new String(bytes,"utf-8");
        System.out.println("===========222222");
        System.out.println(s);
        System.out.println("===========222222");
        //        System.out.println(request.content());
//        this.decoder = new HttpPostRequestDecoder(request);//对http包进行解码
        try {
           // decodeData();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void decodeData() throws Exception {
//        System.out.println(request.duplicate()+"====================2");
        InterfaceHttpPostRequestDecoder httpdecoder = decoder.offer(request);
        parmList = httpdecoder.getBodyHttpDatas();
        for (InterfaceHttpData parm : parmList) {
            Attribute data = (Attribute) parm;
            parmMap.put(data.getName(), data.getValue());
        }
        System.out.println(parmMap);
        String name = (String) parmMap.get("name");
        System.out.println(name);
        String mess = "{\"name\":\"" + name + "\"}";
        System.out.println(mess);
    }

    public String getMess(){
        String mess = myPostPrintWriter.getMess();
        System.out.println(mess+"=-=-=-=-=-=-=-=-=-=-=-");
        return mess;
    }
}
