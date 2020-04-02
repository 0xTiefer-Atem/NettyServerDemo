package org1;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.epoll.EpollEventLoopGroup;
import io.netty.channel.epoll.EpollServerSocketChannel;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpRequestDecoder;
import io.netty.handler.codec.http.HttpResponseEncoder;
import io.netty.handler.stream.ChunkedWriteHandler;

import java.util.concurrent.Executors;

public class NatMapping {
    public void init(int port) throws Exception {
        EventLoopGroup bossGroup = new EpollEventLoopGroup(0x1, Executors.newCachedThreadPool());
        EventLoopGroup workerGroup = new EpollEventLoopGroup(Runtime.getRuntime().availableProcessors() * 0x3, Executors.newCachedThreadPool());
        try {
            ServerBootstrap b = new ServerBootstrap();
            ServerBootstrap bs = b.group(bossGroup, workerGroup);
            ServerBootstrap bs1 = bs.channel(EpollServerSocketChannel.class);
            bs1.childHandler(new ChannelInitializer<SocketChannel>() {
                @Override
                protected void initChannel(SocketChannel ch) throws Exception {

                    ch.pipeline().addLast("encoder", new HttpResponseEncoder());//server端发送的是httpResponse,要进行编码

                    ch.pipeline().addLast("decoder", new HttpRequestDecoder());//server端接收的是httpRequest,要进行解码

                    ch.pipeline().addLast("full_data", new HttpObjectAggregator(65535));

                    ch.pipeline().addLast("chunk", new ChunkedWriteHandler());

                    ch.pipeline().addLast("NatMapping", new MyNatMappingHandler());
                }
            }).option(ChannelOption.SO_BACKLOG, 128)
                    .childOption(ChannelOption.SO_KEEPALIVE, true)
                    .childOption(ChannelOption.TCP_NODELAY, true)
                    .childOption(ChannelOption.SO_REUSEADDR, true) ;
            ChannelFuture cf = bs1.bind(port);

            System.out.println("Nat开启的端口"+port);
            cf.channel().closeFuture().sync();
        } catch (Exception e) {
            System.out.println(e.toString());
        } finally {
            workerGroup.shutdownGracefully();
            bossGroup.shutdownGracefully();
        }
    }

    public static void main(String[] args) throws Exception {
        new NatMapping().init(9999);
    }
}
