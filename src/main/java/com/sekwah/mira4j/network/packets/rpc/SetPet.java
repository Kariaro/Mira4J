package com.sekwah.mira4j.network.packets.rpc;

import com.sekwah.mira4j.network.PacketBuf;
import com.sekwah.mira4j.network.Packets.RPCType;
import com.sekwah.mira4j.network.decoder.RPCListener;

public class SetPet implements RPCMessage {
    private int petId;
    
    public SetPet() {
        
    }
    
    public SetPet(int petId) {
        this.petId = petId;
    }
    
    @Override
    public void read(PacketBuf reader) {
        petId = reader.readUnsignedPackedInt();
    }
    
    @Override
    public void write(PacketBuf writer) {
        writer.writeUnsignedPackedInt(petId);
    }
    
    @Override
    public int id() {
        return RPCType.SetPet.getId();
    }
    
    @Override
    public void forwardPacket(RPC rpc, RPCListener listener) {
        listener.onSetPet(rpc, this);
    }
    
    public int getPedId() {
        return petId;
    }
    
    @Override
    public String toString() {
        return String.format("SetPet[ petId=%d ]", petId);
    }
}
