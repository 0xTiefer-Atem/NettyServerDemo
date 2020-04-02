package org1.download;

import java.io.*;
import java.util.HashMap;
import java.util.List;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.DecoderResult;
import io.netty.handler.codec.http.*;
import io.netty.handler.codec.http.multipart.Attribute;
import io.netty.handler.codec.http.multipart.HttpPostRequestDecoder;
import io.netty.handler.codec.http.multipart.InterfaceHttpData;
import io.netty.handler.codec.http.multipart.InterfaceHttpPostRequestDecoder;

import static io.netty.handler.codec.http.HttpHeaderNames.CONTENT_LENGTH;
import static io.netty.handler.codec.http.HttpHeaderNames.CONTENT_TYPE;
import static io.netty.handler.codec.http.HttpResponseStatus.OK;
import static io.netty.handler.codec.http.HttpVersion.HTTP_1_1;
import static org.NettyServer.HttpDecode.HttpDecoderPost.parmMap;

public class FileServerHandler extends ChannelInboundHandlerAdapter {
    private int position = 0;
    private String file_path = null;
    private static final int SIZE = 10240;
    private int file_size = 0;

    //42,271,872
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        FullHttpRequest fullHttpRequest = (FullHttpRequest) msg;
        System.out.println(fullHttpRequest.method() + " =====> " + fullHttpRequest.uri());
        String url = fullHttpRequest.uri();
        HttpMethod method = fullHttpRequest.method();

        /**
         *第一次连接判断文件是否存在,存在的话返回文件大小
         * 第二次开始进行文件传输
         */
        if (method.equals(HttpMethod.GET)) {
            if (url.contains("num=1")) {
                File file = new File(url.split("\\?")[0]);
                if (!file.exists()) {
                    //文件不存在
                    HttpResponse response = new DefaultFullHttpResponse(HTTP_1_1, OK, Unpooled.wrappedBuffer(("文件不存在").getBytes("utf-8")));
                    response.headers().setInt(CONTENT_LENGTH, ((DefaultFullHttpResponse) response).content().readableBytes());
                    response.headers().set(CONTENT_TYPE, "text/plain");
                    ctx.writeAndFlush(response);
                } else {
                    //返回下载文件大小
                    //确认文件存在存储文件路径
                    file_path = url.split("\\?")[0];
                    RandomAccessFile raf = new RandomAccessFile(file, "r");
                    file_size =(int) raf.length();
                    HttpResponse response = new DefaultFullHttpResponse(HTTP_1_1, OK, Unpooled.wrappedBuffer(("text-length=" + raf.length()).getBytes("utf-8")));
                    response.headers().setInt(CONTENT_LENGTH, ((DefaultFullHttpResponse) response).content().readableBytes());
                    ctx.writeAndFlush(response);
                }
            }
        } else {
            ByteBuf byteBuf1 = fullHttpRequest.content();
            byte[] bytes1 = new byte[byteBuf1.readableBytes()];
            byteBuf1.readBytes(bytes1);
            byteBuf1.release();
            String status = new String(bytes1, "utf-8").split("=")[1];
//            System.out.println(file_path);
            if (status.equals("ok")) {
                RandomAccessFile raf = new RandomAccessFile(file_path, "r");
                raf.seek(position);
                System.out.println("RandomAccessFile的文件指针的初始位置：" + raf.getFilePointer());
                byte[] file_bytes = null;
                if(file_size-raf.getFilePointer()<SIZE){
                    System.out.println("剩余文件不够规定大小");
                    file_bytes = new byte[file_size-(int) raf.getFilePointer()];
                    raf.read(file_bytes);
                    System.out.println("文件读取完毕");
                }else {
                    file_bytes = new byte[SIZE];
                    System.out.println("下次读取位置 "+(position+=SIZE));
                    raf.read(file_bytes);
                }
//                Thread.currentThread().sleep(1000);
                System.out.println("发送数据长度: "+file_bytes.length);
                System.out.println("_+_+_+_+_+_+_+_+");
                HttpResponse response = new DefaultFullHttpResponse(HTTP_1_1, OK, Unpooled.wrappedBuffer(file_bytes));
                response.headers().setInt(CONTENT_LENGTH, ((DefaultFullHttpResponse) response).content().readableBytes());
                ctx.writeAndFlush(response);
                System.out.println("发送成功");
                System.out.println();
            }
        }
    }
}
