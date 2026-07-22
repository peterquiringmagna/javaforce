package javaforce.net;

/** COTP - Connection-Oriented Transport Protocol (ISO 8073/X.224)
 *
 * @author pquiring
 */

public class COTP implements SubPacket {
  public byte length;
  public byte PDU_type;
  public byte[] pdata;

  private short src_ref;

  private static short next_id = 0x0f00;

  public static final byte type_data = (byte)0xf0;
  public static final byte type_connect = (byte)0xe0;
  public static final byte type_connect_ack = (byte)0xd0;

  public COTP() {
  }
  public COTP(byte type) {
    create(type);
  }
  private synchronized static short get_next_id() {
    return next_id++;
  }
  public int getSize() {
    return length + 1;
  }
  public int getDataSize() {
    return -1;
  }
  public void write(Packet packet) throws Exception {
    packet.writeByte(length);
    packet.writeByte(PDU_type);
    switch (PDU_type) {
      case type_data:
        packet.writeByte((byte)0x80);  //dest ref
        break;
      case type_connect:
        packet.writeByte((byte)0x00);
        packet.writeByte((byte)0x00);  //dest ref
        packet.writeByte((byte)(src_ref >>> 8));
        packet.writeByte((byte)(src_ref & 0xff));  //src ref
        packet.writeByte((byte)0x00);  //flags
        packet.writeByte((byte)0xc0);  //param code : TPDU size
        packet.writeByte((byte)1);  //param length
        packet.writeByte((byte)0x0a);  //0x09=512 , 0x0a=1024
        packet.writeByte((byte)0xc1);  //param code : src-tsap
        packet.writeByte((byte)2);  //param length
        packet.writeByte((byte)0x01);
        packet.writeByte((byte)0x00);
        packet.writeByte((byte)0xc2);  //param code : dst-tsap
        packet.writeByte((byte)2);  //param length
        packet.writeByte((byte)0x01);
        packet.writeByte((byte)0x02);
        break;
    }
  }
  public void read(Packet packet) throws Exception {
    length = packet.readByte();
    PDU_type = packet.readByte();
    pdata = new byte[length - 1];
    packet.read(pdata);
  }

  public void create(byte pdu_type) {
    PDU_type = pdu_type;
    src_ref = get_next_id();
    switch (pdu_type) {
      case type_data:
        length = 2;
        break;
      case type_connect:
        length = 17;
        break;
      default:
        System.out.println("Error:Unknown COTP type!!!");
        break;
    }
  }
}
