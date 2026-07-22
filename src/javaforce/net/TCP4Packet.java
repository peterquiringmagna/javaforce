package javaforce.net;

/** TCP4 Packet
 *
 * @author pquiring
 */

public class TCP4Packet implements SubPacket {
  private int header_length;

  public short src_port;
  public short dst_port;
  public int seq;
  public int ack;

  public TCP4Packet() {}

  public TCP4Packet(short src_port, short dst_port) {
    create(src_port, dst_port);
  }

  public int getSize() {
    return header_length * 4;
  }

  public int getDataSize() {
    return -1;
  }

  public void read(Packet packet) throws Exception {
    src_port = packet.readShort();
    dst_port = packet.readShort();
    seq = packet.readInt();
    ack = packet.readInt();
    short header = packet.readShort();
    header_length = (header & 0xf000) >> 12;
    if (header_length < 5) throw new Exception("TCP4 : Invalid header length");
    short window = packet.readShort();
    short checksum = packet.readShort();
    short urgent = packet.readShort();
    if (header_length > 5) {
      packet.offset += (header_length - 5) * 4;
    }
  }

  public void write(Packet packet) throws Exception {
    packet.writeShort(src_port);
    packet.writeShort(dst_port);
    packet.writeInt(seq);
    packet.writeInt(ack);
    packet.writeShort((short)0x5000);  //header_length = 5
    packet.writeShort((byte)0x0000);  //window
    packet.writeShort((byte)0x0000);  //checksum
    packet.writeShort((byte)0x0000);  //urgent
  }

  /** Create TCP Packet with supplied parameters. */
  public void create(short src_port, short dst_port) {
    this.src_port = src_port;
    this.dst_port = dst_port;
  }
}
