package javaforce.controls.s7;

import javaforce.net.*;

/**
 * S7 Header
 *
 * @author pquiring
 */

public class S7Header implements SubPacket {
  public byte id = 0x32;  //protocol id (always 0x32)
  public byte rosctr = ROSCTR_JOB;
  public short res;  //reserved
  public short pdu_ref;  //protocol data unit reference
  public short param_length;
  public short data_length;

  public static final byte ROSCTR_JOB = 1;
  public static final byte ROSCTR_ACK = 3;
  public static final byte ROSCTR_USERDATA = 7;

  public int getSize() {
    switch (rosctr) {
      case ROSCTR_JOB: return 10;
      case ROSCTR_ACK: return 12;
      case ROSCTR_USERDATA: return 10;
    }
    return 0;
  }
  public int getDataSize() {
    return data_length;
  }
  public void write(Packet packet) throws Exception {
    packet.writeByte(id);
    packet.writeByte(rosctr);
    packet.writeShort(res);
    packet.writeShort(pdu_ref);
    packet.writeShort(param_length);
    packet.writeShort(data_length);
  }
  public void read(Packet packet) throws Exception {
    id = packet.readByte();
    rosctr = packet.readByte();
    res = (short)packet.readShort();
    pdu_ref = (short)packet.readShort();
    param_length = (short)packet.readShort();
    data_length = (short)packet.readShort();
    if (rosctr == ROSCTR_ACK) {
      byte error_cls = packet.readByte();
      byte error_code = packet.readByte();
    }
  }
  public void create(short _param_length, short _data_length) {
    pdu_ref = 0x500;
    param_length = _param_length;
    data_length = _data_length;
  }
}
