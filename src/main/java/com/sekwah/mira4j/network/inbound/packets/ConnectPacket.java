package com.sekwah.mira4j.network.inbound.packets;

//import com.sekwah.mira4j.network.Packet;
//import com.sekwah.mira4j.network.PacketBuf;

//public class ConnectPacket implements Packet<ClientListener> {
//    private int nonce;
//    private byte hazelVersion;
//    private int version;
//    private String username;
//    
//    @Override
//    public void readData(PacketBuf reader) {
//        nonce = reader.readUnsignedShortBE();
//        hazelVersion = reader.readByte();
//        version = reader.readInt();
//        username = reader.readString();
//    }
//
//    @Override
//    public void writeData(PacketBuf writer) {}
//
//    @Override
//    public void forwardPacket(ClientListener listener) {
//        listener.onConnectPacket(this);
//    }
//    
//    public int getNonce() {
//        return nonce;
//    }
//    
//    public byte getHazelVersion() {
//        return hazelVersion;
//    }
//    
//    public int getVersion() {
//        return version;
//    }
//    
//    public String getUsername() {
//        return username;
//    }
//}
