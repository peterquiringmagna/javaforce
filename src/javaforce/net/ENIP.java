package javaforce.net;

import javaforce.controls.ab.*;

/** EtherNet/IP (Industrial Protocol).
 *
 * The ill named packet:
 *   EtherNet/IP is a sub packet of the Ethernet/IP/TCP packets.
 *   Namer deserves the Darwin award.
 *
 * @author pquiring
 */

public class ENIP implements SubPacket {
  //header (24 bytes)
  public short cmd;  //cmd type
  public short len;  //following command data below
  public int session;  //random ID for connection
  public int status;  //0 = success
  public long context;  //increments per packet
  public int options;  //0
  //CMD_RR_DATA (16 bytes)
  public int ihandle;  //0
  public short timeout;  //0
  public short count = 2;
  public short type_1 = 0x0000;
  public short len_1 = 0x0000;
  public short type_2 = 0x00b2;
  public short len_2 = 0x0000;  //length of CIP packet in bytes
  //CMD_GET_SESSION (4 bytes)
  public short protocol = 0x0001;
  public short flags = 0x0000;


  public static final short CMD_RR_DATA = 0x6f;
  public static final short CMD_GET_SESSION = 0x65;
  public static final short CMD_SEND_UNIT_DATA = 0x70;

  public ENIP() {}

  public ENIP(short _cmd) {
    cmd = _cmd;
    if (cmd == CMD_SEND_UNIT_DATA) {
      timeout = 1;
    }
  }

  public int getSize() {
    switch (cmd) {
      case CMD_RR_DATA: return 24 + 16;  //40
      case CMD_GET_SESSION: return 24 + 4;  //28
      case CMD_SEND_UNIT_DATA: return 24 + 22 - 4;  //46
    }
    return -1;
  }

  public int getDataSize() {
    return -1;
  }

  public void read(Packet packet) throws Exception {
    cmd = packet.readShort();
    len = packet.readShort();
    session = packet.readInt();
    status = packet.readInt();
    context = packet.readLong();
    options = packet.readInt();
    switch (cmd) {
      case CMD_RR_DATA:
        ihandle = packet.readInt();
        timeout = packet.readShort();
        count = packet.readShort();
        if (count != 2) throw new Exception("ab:bad ip packet");
        type_1 = packet.readShort();
        len_1 = packet.readShort();
        type_2 = packet.readShort();
        len_2 = packet.readShort();
        break;
      case CMD_GET_SESSION:
        protocol = packet.readShort();
        flags = packet.readShort();
        break;
      case CMD_SEND_UNIT_DATA:
        //TODO
        break;
    }
  }

  public void setSizes(int size) {
    switch (cmd) {
      case CMD_RR_DATA:
        len = (short)(16 + size);
        len_2 = (short)size;
        break;
      case CMD_GET_SESSION:
        len = 4;
        break;
      case CMD_SEND_UNIT_DATA:
        len = (short)(22 + size - 4);
        len_2 = (short)(2 + size);
        break;
    }
  }

  public void write(Packet packet) throws Exception {
    write(packet, null);
  }

  public void write(Packet packet, ABContext abcontext) throws Exception {
    if (abcontext != null) {
      session = abcontext.session;
      context = abcontext.context;
    }
    //24 bytes
    packet.writeShort(cmd);
    packet.writeShort(len);
    packet.writeInt(session);
    packet.writeInt(status);
    packet.writeLong(context);
    packet.writeInt(options);
    switch (cmd) {
      case CMD_RR_DATA:
        //16 bytes
        packet.writeInt(ihandle);
        packet.writeShort(timeout);
        packet.writeShort(count);
        packet.writeShort(type_1);
        packet.writeShort(len_1);
        packet.writeShort(type_2);
        packet.writeShort(len_2);
        if (abcontext != null) {
          abcontext.increment();
        }
        break;
      case CMD_GET_SESSION:
        //4
        packet.writeShort(protocol);
        packet.writeShort(flags);
        break;
      case CMD_SEND_UNIT_DATA:
        //22 bytes
        packet.writeInt(ihandle);
        packet.writeShort(timeout);
        packet.writeShort(count);
        packet.writeShort(type_1);
        packet.writeShort(len_1);
        packet.writeShort(type_2);
        packet.writeShort(len_2);
        break;
    }
  }
}
