package org1.download;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.http.HttpClientCodec;
import io.netty.handler.codec.http.HttpContentDecompressor;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.handler.stream.ChunkedWriteHandler;
import io.netty.util.CharsetUtil;

public class FileClient {
    public static void connect(String host, int port) throws Exception {
        EventLoopGroup workerGroup = new NioEventLoopGroup();

        try {
            Bootstrap b = new Bootstrap();
            b.group(workerGroup);
            b.channel(NioSocketChannel.class);
            b.option(ChannelOption.SO_KEEPALIVE, true).option(ChannelOption.TCP_NODELAY,true);
            b.handler(new ChannelInitializer() {
                @Override
                protected void initChannel(Channel ch) throws Exception {
//                    ch.pipeline().addLast("encoder", new StringEncoder(CharsetUtil.UTF_8));
//                    ch.pipeline().addLast("decoder", new StringDecoder(CharsetUtil.UTF_8));

                    ch.pipeline().addLast(new HttpClientCodec());
                    ch.pipeline().addLast(new HttpContentDecompressor());
                    ch.pipeline().addLast(new HttpObjectAggregator(65535));
                    ch.pipeline().addLast("chunk", new ChunkedWriteHandler());
                    ch.pipeline().addLast(new FileClientHandler());
                }
            });

            // Start the client.
            ChannelFuture f = b.connect(host, port).sync();
            f.channel().closeFuture().sync();
        } finally {
            workerGroup.shutdownGracefully();
        }
    }

    public static void main(String[] args) throws Exception {
        FileClient.connect("localhost",9999);
    }
}
