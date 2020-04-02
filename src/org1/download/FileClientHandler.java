package org1.download;


import java.io.*;
import java.net.URI;
import java.util.Scanner;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.*;

import static io.netty.handler.codec.http.HttpVersion.HTTP_1_1;

public class FileClientHandler extends ChannelInboundHandlerAdapter {
    private long file_length = 0;
    private float current_length = 0;
    private String file_path = null;
    private boolean flag = false;

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        Scanner sc = new Scanner(System.in);
        System.out.println("请输入想要下载文件的地址:");
        file_path = sc.nextLine();
        URI uri = new URI(file_path+"?num=1");
//        String file = "/home/wq/IdeServer.jar";
        HttpRequest request = new DefaultFullHttpRequest(HTTP_1_1, HttpMethod.GET, uri.toASCIIString());
        request.headers().set(HttpHeaderNames.CONNECTION, HttpHeaderNames.CONNECTION);
        request.headers().set(HttpHeaderNames.CONTENT_LENGTH, ((DefaultFullHttpRequest) request).content().readableBytes());
        request.headers().set("messageType", "normal");
        ctx.writeAndFlush(request);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        FullHttpResponse fullHttpResponse = (FullHttpResponse) msg;
//        System.out.println("==================");
//        System.out.println(fullHttpResponse.headers().toString());
        ByteBuf byteBuf = fullHttpResponse.content();
        byte[] bytes = new byte[byteBuf.readableBytes()];
        byteBuf.readBytes(bytes);
        byteBuf.release();
        String msg1 = new String(bytes, "utf-8");
        if (msg1.contains("text-length")) {
            URI uri = new URI("file_path?num=1");
            file_length = Long.parseLong(msg1.split("=")[1]);
            HttpRequest request = new DefaultFullHttpRequest(HTTP_1_1, HttpMethod.POST, uri.toASCIIString(), Unpooled.wrappedBuffer("status=ok".getBytes("utf-8")));
            request.headers().set(HttpHeaderNames.CONNECTION, HttpHeaderNames.CONNECTION);
            request.headers().set(HttpHeaderNames.CONTENT_LENGTH, ((DefaultFullHttpRequest) request).content().readableBytes());
            request.headers().set("messageType", "normal");
            ctx.writeAndFlush(request);
        }
//        System.out.println(file_length);
        else {
            String file_name = file_path.split("/")[3];
            RandomAccessFile raf = new RandomAccessFile("/home/wq/test/"+file_name, "rw");
            if(!flag){
                System.out.println("开始文件下载...");
                flag = true;
            }
            if (current_length < file_length) {
//                System.out.println(file_name);
                raf.seek(raf.length());
                raf.write(bytes);
                current_length += bytes.length;
                System.out.println();
                System.out.println("接受数据的长度" + bytes.length + "\t当前文件长度: " + current_length + "\t" + "文件总长度: " + file_length);
                System.out.println("下载文件进度:  " + (current_length / file_length) * 100 + "%");
                HttpRequest request = new DefaultFullHttpRequest(HTTP_1_1, HttpMethod.POST, file_path, Unpooled.wrappedBuffer("status=ok".getBytes("utf-8")));
                request.headers().set(HttpHeaderNames.CONNECTION, HttpHeaderNames.CONNECTION);
                request.headers().set(HttpHeaderNames.CONTENT_LENGTH, ((DefaultFullHttpRequest) request).content().readableBytes());
                request.headers().set("messageType", "normal");
                ctx.writeAndFlush(request);
            }else {
                raf.close();
                System.out.println("文件下载完成");
            }
        }
    }
}
