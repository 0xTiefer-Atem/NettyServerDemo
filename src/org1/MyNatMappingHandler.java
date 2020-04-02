package org1;

import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.*;
import static io.netty.handler.codec.http.HttpHeaderNames.CONTENT_LENGTH;
import static io.netty.handler.codec.http.HttpHeaderNames.CONTENT_TYPE;
import static io.netty.handler.codec.http.HttpResponseStatus.OK;
import static io.netty.handler.codec.http.HttpVersion.HTTP_1_1;


public class MyNatMappingHandler extends ChannelInboundHandlerAdapter {
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        FullHttpRequest fullrequest = (FullHttpRequest) msg;
        String[] ipdress = fullrequest.uri().split("/");
        String webmess = null;
        //System.out.println(ipdress[1]);
        if (ipdress[1].contains("baidu")) {
            fullrequest.setUri("www.baidu.com");
            MyHttpHandler myHttpHandler = new MyHttpHandler(fullrequest);
            webmess = myHttpHandler.getmess();
        }
        System.out.println(webmess);
        System.out.println("-------=======----------------============:"+fullrequest.getUri() );
        HttpResponse response = new DefaultFullHttpResponse(HTTP_1_1, OK, Unpooled.wrappedBuffer(webmess.getBytes("utf-8")));
        //response.headers().set(CONTENT_TYPE, "text/html");
        //response.headers().setInt(CONTENT_LENGTH, ((DefaultFullHttpResponse) response).content().readableBytes());
        response.headers().set(CONTENT_TYPE,"text/html");
        response.headers().setInt(CONTENT_LENGTH,((DefaultFullHttpResponse)response).content().readableBytes());
        ctx.writeAndFlush(response);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        super.exceptionCaught(ctx, cause);
        Channel channel = ctx.channel();
        if(channel.isActive()){
            ctx.close();
        }
    }
}
