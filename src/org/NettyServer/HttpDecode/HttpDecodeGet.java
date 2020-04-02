package org.NettyServer.HttpDecode;

import org.NettyServer.MyPrintWriter;

import java.util.HashMap;
import java.util.Map;

public class HttpDecodeGet  {
    private String uri = null;
    private static MyPrintWriter myGetPrintWriter = new MyPrintWriter();
    public static Map<String, String> parametermap = new HashMap<>();

    public HttpDecodeGet(String uri1) {
        this.uri = uri1;
        decodeuri();
    }


    private void decodeuri() {
        String[] parameter_string = uri.split("\\?");
        System.out.println(parameter_string[0] + "======" + parameter_string[1]);
        String[] get_param_kv = parameter_string[1].split("&");
        for (String tempara : get_param_kv) {
            String[] param_k_v = tempara.split("=");
            parametermap.put(param_k_v[0], param_k_v[1]);
        }
        System.out.println(parametermap);
    }

    public String getMess(){
        String mess = myGetPrintWriter.getMess();
        System.out.println(mess+"=-=-=-=-=-=-=-=-=-=-=-");
        return mess;
    }
}
