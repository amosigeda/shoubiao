package com.bracelet.socket;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.Delimiters;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.handler.timeout.ReadTimeoutHandler;

@Service
public class ServerChannelInitializer extends ChannelInitializer<SocketChannel> {
	


	@Autowired
	BaseChannelHandler baseChannelHandler;
	
	@Override
	protected void initChannel(SocketChannel ch) throws Exception {
		ChannelPipeline pipeline = ch.pipeline();
	   // pipeline.addLast("framer", new DelimiterBasedFrameDecoder(8192, Delimiters.lineDelimiter()));
	//	pipeline.addLast("framer", new DelimiterBasedFrameDecoder(8192, Unpooled.wrappedBuffer(new byte[] { ']'})));
	  //	pipeline.addLast("decoder", new StringDecoder());
		//pipeline.addLast(new LengthFieldBasedFrameDecoder(Integer.MAX_VALUE,0,4,0,4));
		/*
		 *  int maxFrameLength,
            int lengthFieldOffset, int lengthFieldLength,
            int lengthAdjustment, int initialBytesToStrip
		 * */
		pipeline.addLast("framer", new DelimiterBasedFrameDecoder(8192, Unpooled.wrappedBuffer(new byte[] { ']'})));
		//pipeline.addLast("decoder", new StringDecoder());
		pipeline.addLast("encoder", new StringEncoder());
        pipeline.addLast("readTimeoutHandler", new ReadTimeoutHandler(130));//单位秒  
		pipeline.addLast("handler", baseChannelHandler);
	}
	
	

	
}
