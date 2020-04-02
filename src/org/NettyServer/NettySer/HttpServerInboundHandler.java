package org.NettyServer.NettySer;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelPromise;
import io.netty.handler.codec.http.*;
import org.NettyServer.HttpDecode.HttpDecodeGet;
import org.NettyServer.HttpDecode.HttpDecoderPost;
import org.NettyServer.HttpDecode.MyHttpRequest;
import org.NettyServer.HttpDecode.MyHttpResponse;
import org.NettyServer.MyServlet.IServlet;
import java.io.FileInputStream;
import java.lang.reflect.Method;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.util.Base64;

import static io.netty.handler.codec.http.HttpHeaderNames.*;
import static io.netty.handler.codec.http.HttpHeaderNames.CONTENT_TYPE;
import static io.netty.handler.codec.http.HttpResponseStatus.OK;
import static io.netty.handler.codec.http.HttpVersion.HTTP_1_1;


public class HttpServerInboundHandler extends ChannelInboundHandlerAdapter {

    private FileInputStream fin = null;
    private FileChannel fchannel = null;

    @Override
    public void channelRead(ChannelHandlerContext cxt, Object msg) throws Exception {
        FullHttpRequest request = (FullHttpRequest) msg;
        Charset charset = Charset.forName("utf-8");
        CharsetDecoder fileDecoder = charset.newDecoder();
        if (!request.uri().equals("/favicon.ico")) {
            String uri = request.uri();
            //默认登录界面
            if (uri.equals("/")) {
                System.out.println("当前请求路径: " + uri);
//                QueryStringDecoder queryStringDecoder = new QueryStringDecoder(uri);
//                String name = "";
//                Map mymap = queryStringDecoder.parameters();
//                System.out.println(mymap);
//                if (queryStringDecoder.parameters().get("name") != null) {
//                    name = queryStringDecoder.parameters().get("name").get(0);
//                }
//                System.out.println(name + "-------1");

                fin = new FileInputStream("/home/wq/WebSocket/web/html/websocket.html");
                fchannel = fin.getChannel();
                FileChannel fileRead = fin.getChannel();
                MappedByteBuffer fileReadBuffer = fileRead.map(FileChannel.MapMode.READ_ONLY, 0, fin.available());
                CharBuffer fileCharBuffer = fileDecoder.decode(fileReadBuffer);
                String file = fileCharBuffer.toString();
                HttpResponse response = new DefaultFullHttpResponse(HTTP_1_1, OK, Unpooled.wrappedBuffer(file.getBytes("utf-8")));
                response.headers().set(CONTENT_TYPE, "text/html");
                response.headers().setInt(CONTENT_LENGTH, ((DefaultFullHttpResponse) response).content().readableBytes());
                cxt.writeAndFlush(response);
            } else {
                // 是POST请求
                if (request.method() == HttpMethod.POST) {
                    System.out.println("POST方法");
                    HttpDecoderPost httpDecoderPost = new HttpDecoderPost(request);
                    String mess;
                    Class servletclass = Class.forName("org.NettyServer.MyServlet.MyServlet");
                    IServlet mysetvlet =(IServlet) servletclass.newInstance();
                    Method postMethod = servletclass.getMethod("doPost", MyHttpRequest.class, MyHttpResponse.class);
                    postMethod.invoke(mysetvlet,new MyHttpRequest(),new MyHttpResponse());
                    mess = httpDecoderPost.getMess();
                    String json = "{\"url\":\"" + "test.html" + "\"}";
                    fin = new FileInputStream("/home/wq/IdeaProjects/NettyServerDemo/src/webApp/test.html");
                    fchannel = fin.getChannel();
                    ByteBuffer bfr = ByteBuffer.allocate(1024);
                    String mess1 = "";
                    while (fchannel.read(bfr) !=-1){
                        bfr.flip();
                        CharsetDecoder post_decoder = charset.newDecoder();
                        CharBuffer charBuffer = post_decoder.decode(bfr);
                        bfr.clear();
                        mess1 += String.valueOf(charBuffer);
                    }
                    System.out.println(json);
                    HttpResponse response = new DefaultFullHttpResponse(HTTP_1_1, OK, Unpooled.wrappedBuffer(json.getBytes("utf-8")));
                    response.headers().set(CONTENT_TYPE, "text/html");
                    response.headers().setInt(CONTENT_LENGTH, ((DefaultFullHttpResponse) response).content().readableBytes());
                    cxt.writeAndFlush(response);

                } else if (request.method() == HttpMethod.GET) {
                    HttpResponse response=null;
                    System.out.println("GET方法");
                    System.out.println(request.uri() + "=============2");
                    if(request.uri().contains("?")) {
                        HttpDecodeGet httpDecodeGet = new HttpDecodeGet(request.uri());
                        String user = "";
                        Class setvletclass = Class.forName("org.NettyServer.MyServlet.MyServlet");
                        IServlet myservlet = (IServlet) setvletclass.newInstance();
                        Method getMethod = setvletclass.getMethod("doGet", MyHttpRequest.class, MyHttpResponse.class);
                        getMethod.invoke(myservlet, new MyHttpRequest(), new MyHttpResponse());
                        user = httpDecodeGet.getMess();

                        String json = "{\"name\":\"" + user + "\"}";
                        System.out.println(json);

                        response = new DefaultFullHttpResponse(HTTP_1_1, OK, Unpooled.wrappedBuffer(json.getBytes("utf-8")));
                        response.headers().set(CONTENT_TYPE, "text/html");
                        response.headers().setInt(CONTENT_LENGTH, ((DefaultFullHttpResponse) response).content().readableBytes());
                        cxt.writeAndFlush(response);
                    }else {
                        //get请求里面没参数,直接返回给他页面
                        fin = new FileInputStream("/home/wq/IdeaProjects/NettyServerDemo/src/webApp"+request.uri());
                        ByteBuffer bfr = ByteBuffer.allocate(1024);
                        fchannel = fin.getChannel();
                        String mess = "";
                        while ((fchannel.read(bfr)!=-1)) {
                            bfr.flip();
                            CharsetDecoder decoder = charset.newDecoder();
                            CharBuffer cb = decoder.decode(bfr);
                            mess+=String.valueOf(cb);
                            bfr.clear();
                        }
                        System.out.println(mess+"9090909090");
                        response = new DefaultFullHttpResponse(HTTP_1_1, OK, Unpooled.wrappedBuffer(mess.getBytes("utf-8")));
                        response.headers().set(CONTENT_TYPE, "text/html");
                        response.headers().setInt(CONTENT_LENGTH, ((DefaultFullHttpResponse) response).content().readableBytes());
                        cxt.writeAndFlush(response);
                    }
                }
            }
        }
    }


    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        System.out.println("扑获异常时触发");
        super.exceptionCaught(ctx, cause);
    }
}
