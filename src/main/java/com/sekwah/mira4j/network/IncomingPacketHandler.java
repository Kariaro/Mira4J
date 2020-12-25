package com.sekwah.mira4j.network;

import java.util.Arrays;

import com.sekwah.mira4j.Mira4J;
import com.sekwah.mira4j.network.Packets.PacketType;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.socket.DatagramPacket;

public class IncomingPacketHandler extends SimpleChannelInboundHandler<DatagramPacket> {
    private final ClientConnectionManager manager;
    
    public IncomingPacketHandler(ClientConnectionManager manager) {
        this.manager = manager;
    }
    
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, DatagramPacket msg) throws Exception {
        if (!manager.hasRemote()) {
            // Because the manager is not connected by
            // default we need to set the remote address
            manager.setRemote(msg.sender());
        }
        
        ByteBuf buf = msg.content();
        
        buf.markReaderIndex();
        final int readableBytes = buf.readableBytes();
        final byte[] packetBuffer = new byte[readableBytes];
        buf.readBytes(packetBuffer);
        buf.resetReaderIndex();
        
        PacketType type = Packets.PacketType.fromId(packetBuffer[0]);
        if (type != PacketType.PING
         && type != PacketType.ACKNOWLEDGEMENT) {
            Mira4J.LOGGER.info("Recieved Packet {} {}", type.toString(), Arrays.toString(packetBuffer));
        }
        
        decode(ctx, buf);
    }
    
    private void decode(ChannelHandlerContext ctx, ByteBuf buf) throws Exception {
        PacketType packetType = Packets.PacketType.fromId(buf.readUnsignedByte());
        
        Packet<?> packet = Packets.getPacketFromType(packetType);
        if (packet == null) return;
        packet.readData(PacketBuf.wrap(buf));
        
        ctx.fireChannelRead(packet);
    }
}
