package org.NettyServer.HttpDecode;

public class MyHttpRequest {
    public Object getPostParameter(String para){
        return HttpDecoderPost.parmMap.get(para);
    }

    public Object getGetParameter(String para){
        return HttpDecodeGet.parametermap.get(para);
    }
}
