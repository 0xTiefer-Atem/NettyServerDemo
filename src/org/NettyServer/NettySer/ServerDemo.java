package org.NettyServer.NettySer;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.epoll.EpollEventLoopGroup;
import io.netty.channel.epoll.EpollServerSocketChannel;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.*;
import io.netty.handler.stream.ChunkedWriteHandler;
import java.util.concurrent.Executors;

public class ServerDemo {
    public void init(int port) throws Exception {
        EventLoopGroup bossGroup = new EpollEventLoopGroup(0x1, Executors.newCachedThreadPool()); //mainReactor    1个线程
        EventLoopGroup workerGroup = new EpollEventLoopGroup(Runtime.getRuntime().availableProcessors() * 0x3, Executors.newCachedThreadPool());   //subReactor       线程数量等价于cpu个数+1
        try {
            ServerBootstrap b = new ServerBootstrap();
            ServerBootstrap bs = b.group(bossGroup, workerGroup);
            ServerBootstrap bs1 = bs.channel(EpollServerSocketChannel.class);
            bs1.childHandler(new ChannelInitializer<SocketChannel>() {
                @Override
                protected void initChannel(SocketChannel socketChannel) throws Exception {
                    socketChannel.pipeline().addLast("encoder", new HttpResponseEncoder());//server端发送的是httpResponse,要进行编码

                    socketChannel.pipeline().addLast("decoder", new HttpRequestDecoder());//server端接收的是httpRequest,要进行解码

                    socketChannel.pipeline().addLast("full_data", new HttpObjectAggregator(65535));

                    socketChannel.pipeline().addLast("chunk", new ChunkedWriteHandler());

                    socketChannel.pipeline().addLast("server", new HttpServerInboundHandler());

                }
            }).option(ChannelOption.SO_BACKLOG, 128)
                    .childOption(ChannelOption.SO_KEEPALIVE, true);
            ChannelFuture f = bs1.bind("0.0.0.0",port);

            f.channel().closeFuture().sync();
            /*在对应的future对象上阻塞用户线程，例如future.channel().closeFuture().sync()
            而最终触发future对象的notify动作都是通过eventLoop线程轮询任务完成的，例如对关闭的sync，
            因为不论是用户直接关闭或者eventLoop的轮询状态关闭，都会在eventLoop的线程内完成notify动作，
            所以不要在IO线程内调用future对象的sync或者await方法，因为应用程序代码都是编写的channelHandler，
            而channelHandler是在eventLoop的线程内执行的，所以是不能在channelHandler中调用sync或者await方法的*/
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            workerGroup.shutdownGracefully();
            bossGroup.shutdownGracefully();
        }
    }

    public static void main(String[] args) {
        try {
            new ServerDemo().init(9999);
        } catch (Exception e) {
            System.out.println("绑定失败");
        }
    }
}
