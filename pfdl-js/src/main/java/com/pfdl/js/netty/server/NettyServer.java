package com.pfdl.js.netty.server;

import com.pfdl.common.netty.JSConstants;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.util.CharsetUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.net.InetSocketAddress;

@Component
public class NettyServer {
    private static final Logger log = LoggerFactory.getLogger(NettyServer.class);
    public void start(InetSocketAddress address) {
        //配置服务端的NIO线程组
        EventLoopGroup bossGroup = new NioEventLoopGroup(1);
        EventLoopGroup workerGroup = new NioEventLoopGroup();

        try {
            ServerBootstrap bootstrap = new ServerBootstrap()
                    .group(bossGroup, workerGroup)  // 绑定线程池
                    .channel(NioServerSocketChannel.class)
                    .localAddress(address)
//                    .childHandler(new NettyServerChannelInitializer())//编码解码
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            //ch.pipeline().addLast(new ReadTimeoutHandler(5)); //5秒没有读事件就关掉
                            //ch.pipeline().addLast(new StringDecoder(CharsetUtil.UTF_8));
                            ch.pipeline().addLast(new MyDecoder());       //自定义的解码
                            ch.pipeline().addLast(new StringEncoder(CharsetUtil.UTF_8));
                            ch.pipeline().addLast(new NettyServerHandler());
                        }
                    })
                    .option(ChannelOption.SO_BACKLOG, 128)  //服务端接受连接的队列长度，如果队列已满，客户端连接将被拒绝
                    .childOption(ChannelOption.SO_KEEPALIVE, true);  //保持长连接，2小时无数据激活心跳机制

            // 绑定端口，开始接收进来的连接
            ChannelFuture future = bootstrap.bind(address).sync();
            log.info("Netty-服务器启动成功地址为:  "+ JSConstants.SOCKET_SERVER_IP+" - 监听端口为: "+JSConstants.SOCKET_SERVER_PROT);
            //关闭channel和块，直到它被关闭
            future.channel().closeFuture().sync();

        } catch (Exception e) {
            e.printStackTrace();
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }


}
