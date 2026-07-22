package javaforce.net;

/** UDP4 Packet
 *
 * @author pquiring
 */

public class UDP4Packet implements SubPacket {
  private int length;

  public short src_port;
  public short dst_port;

  public UDP4Packet() {}

  public UDP4Packet(short src_port, short dst_port, int length) {
    create(src_port, dst_port, length);
  }

  public int getSize() {
    return 8;
  }

  public int getDataSize() {
    return length;
  }

  public void read(Packet packet) throws Exception {
    src_port = packet.readShort();
    dst_port = packet.readShort();
    length = packet.readShort();
    int checksum = packet.readShort();
  }

  public void write(Packet packet) throws Exception {
    packet.writeShort(src_port);
    packet.writeShort(dst_port);
    packet.writeShort((short)length);
    packet.writeShort((short)0x0000);  //checksum
  }

  /** Create TCP Packet with supplied parameters. */
  public void create(short src_port, short dst_port, int length) {
    this.length = length;
    this.src_port = src_port;
    this.dst_port = dst_port;
  }
}
