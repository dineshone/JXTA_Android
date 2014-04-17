package net.jxta.impl.endpoint.netty.http;

import java.util.concurrent.Executors;

import net.jxta.impl.endpoint.netty.NettyTransport;

import org.jboss.netty.channel.socket.ClientSocketChannelFactory;
import org.jboss.netty.channel.socket.ServerSocketChannelFactory;
import org.jboss.netty.channel.socket.http.HttpTunnelingClientSocketChannelFactory;
import org.jboss.netty.channel.socket.httptunnel.HttpTunnelClientChannelFactory;
import org.jboss.netty.channel.socket.httptunnel.HttpTunnelServerChannelFactory;
import org.jboss.netty.channel.socket.nio.NioClientSocketChannelFactory;
import org.jboss.netty.channel.socket.nio.NioServerSocketChannelFactory;
// d0d0 05/21/2013 The call to HttpTunnelServerChannelFactory was throwing an error. Hence I added the below line to solve it.

import org.jboss.netty.channel.socket.httptunnel.*;
/**
 * Netty based transport which uses a full duplex HTTP tunnel rather than a raw TCP
 * connection to send messages between client and server. This is intended to allow
 * negotiation of restrictive transparent firewalls and proxies, typically in corporate
 * environments.
 * 
 * @author iain.mcginniss@onedrum.com
 */


/*
* d0d0 The method signature for the method NioClientSocketChannelFactory & NioServerSocketChannelFactory 
* has changed with the latest version of netty.
* In the new version the parameters Executors.newCachedThreadPool() are unnecessary. 
* A call to the constructor with no parameters does the same trick. 
* 
* */     	


@SuppressWarnings("unused")
public class NettyHttpTunnelTransport extends NettyTransport {

    @Override
    protected ClientSocketChannelFactory createClientSocketChannelFactory() {
//    	NioClientSocketChannelFactory nioFactory = new NioClientSocketChannelFactory();
    	NioClientSocketChannelFactory nioFactory = new NioClientSocketChannelFactory(Executors.newCachedThreadPool(), Executors.newCachedThreadPool());
        return new HttpTunnelingClientSocketChannelFactory(nioFactory);
    }

    @Override
    protected ServerSocketChannelFactory createServerSocketChannelFactory() {
//    	NioServerSocketChannelFactory nioFactory = new NioServerSocketChannelFactory();
    	NioServerSocketChannelFactory nioFactory = new NioServerSocketChannelFactory(Executors.newCachedThreadPool(), Executors.newCachedThreadPool());
        return new HttpTunnelServerChannelFactory(nioFactory);
    }

    @Override
    protected String getDefaultProtocolName() {
        return "http2";
    }

    @Override
    protected int getDefaultPort() {
        return 8080;
    }

    @Override
    protected int getDefaultPortRangeLowerBound() {
    	return 8081;
    }

    @Override
    protected int getDefaultPortRangeUpperBound() {
    	return 8099;
    }

    @Override
    protected String getTransportDescriptiveName() {
        return "HTTP Tunnel";
    }

}
