package com.sekwah.mira4j.network.inbound.packets.rpc;

import com.sekwah.mira4j.network.PacketBuf;
import com.sekwah.mira4j.network.Packets;

public class SetPet implements RPCObject {
    private int petId;
    
    public SetPet() {
        
    }
    
    public SetPet(int petId) {
        this.petId = petId;
    }
    
    public void read(PacketBuf reader) {
        petId = reader.readUnsignedPackedInt();
    }
    
    public void write(PacketBuf writer) {
        writer.writeUnsignedPackedInt(petId);
    }
    
    public int id() {
        return Packets.RPCType.SetPet.getId();
    }
    
    public int getPedId() {
        return petId;
    }
    
    @Override
    public String toString() {
        return String.format("SetPet[ petId=%d ]", petId);
    }
}
