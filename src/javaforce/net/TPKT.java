package javaforce.net;

/** TPKT Header
 *
 * @author pquiring
 */

public class TPKT implements SubPacket {
  public byte version = 3;  //always 3
  public byte res;  //always 0
  public short length;  //length of data including this header

  public TPKT() {
  }
  public TPKT(short length) {
    create(length);
  }

  public int getSize() {
    return 4;
  }
  public int getDataSize() {
    return length - 4;
  }
  public void write(Packet packet) throws Exception {
    packet.writeByte(version);
    packet.writeByte(res);
    packet.writeShort(length);
  }
  public void read(Packet packet) throws Exception {
    version = packet.readByte();
    if (version != 3) throw new Exception("TPKT : unknown version");
    res = packet.readByte();
    if (res != 0) throw new Exception("TPKT : unknown res");
    length = packet.readShort();
  }

  /** Creates header.
   * @param length = total length (including 4 bytes for header)
   */
  public void create(short length) {
    this.length = length;
  }
}
