package cloud_storage.client;


import cloud_storage.common.Recipient;
import cloud_storage.common.SCM;
import cloud_storage.common.Sender;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

class Client {

    private final InOut inOut = new InOut();
    private String host;
    private int port;
    private EventLoopGroup workerGroup;
    private Bootstrap bootstrap;
    private ChannelFuture f;
    private Channel channel;
    private boolean authorized;

    public boolean isAuthorized() {
        return authorized;
    }

    Client(String host, int port) {
        this.host = host;
        this.port = port;
        workerGroup = new NioEventLoopGroup();
        bootstrap = new Bootstrap();
        bootstrap.channel(NioSocketChannel.class);
        bootstrap.group(workerGroup);
        bootstrap.option(ChannelOption.SO_KEEPALIVE, true);
        bootstrap.handler(new ChannelInitializer<SocketChannel>() {
            @Override
            public void initChannel(SocketChannel ch) throws Exception {
                ch.pipeline().addLast(new Sender(), new Recipient(), inOut);
            }
        });
    }

    public void getAuthorization(){
        do {
            if (inOut.getAuthorizationMessage() != null) {
                if (inOut.getAuthorizationMessage().equals(SCM.OK)) authorized = true;
            }
        }while (inOut.getAuthorizationMessage() == null);
    }

    public void toSendServiceMessage(String message){
        channel.write(message);
    }

    public boolean connect(){
        try {
            f = bootstrap.connect(host, port).sync();
            if (f.isSuccess()){
                channel = f.channel();
                return true;
            }
        } catch (Exception e) {
            // TODO: 12.07.2018 сообщение о недоступности сервера
            System.out.println("Не удалось подключиться к серверу");
            return false;
        }
        return  false;
    }

    public void disconnect() {
        try {
            f.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }finally {
            workerGroup.shutdownGracefully();
            System.out.println("закончили работу");
        }
    }
}
