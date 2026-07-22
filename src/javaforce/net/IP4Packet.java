package javaforce.net;

import java.util.*;

/** IPv4 Packet
 *
 * @author pquiring
 */

public class IP4Packet implements SubPacket {
  private int length;
  private int id;
  private byte protocol;
  private IP4 src;
  private IP4 dst;

  public IP4Packet() {}

  public IP4Packet(IP4 src, IP4 dst, int length, byte protocol) {
    create(src, dst, length, protocol);
  }

  public int getSize() {
    return length;
  }

  public void read(Packet packet) throws Exception {
    byte[] ip = new byte[4];
    byte ver_length = packet.readByte();
    byte version = (byte)((ver_length & 0xf0) >> 4);
    if (version != 4) throw new Exception("IP4:Invalid Packet version");
    int header_length = (ver_length & 0xf);  //length in 32bit fields
    if (header_length < 5) throw new Exception("IP4:Invalid header length");
    short service_type = packet.readByte();
    length = packet.readShort();
    short id = packet.readShort();
    short frag = packet.readShort();
    byte ttl = packet.readByte();
    protocol = packet.readByte();
    short checksum = packet.readShort();
    packet.read(ip);
    src = new IP4(ip);
    packet.read(ip);
    dst = new IP4(ip);
    header_length -= 5;
    if (header_length > 0) {
      packet.offset += header_length * 4;
    }
  }

  public void write(Packet packet) throws Exception {
    if (src == null || dst == null) throw new Exception("IP4Packet:IPs not set");
    packet.writeByte((byte)0x45);  //IP4 : header_length=5
    packet.writeByte((byte)0);  //TOS
    packet.writeShort((short)length);
    packet.writeShort((short)id);
    packet.writeShort((byte)0);  //frag
    packet.writeByte((byte)64);  //ttl
    packet.writeByte(protocol);
    packet.writeShort((byte)0);  //checksum
    packet.write(src.ip);
    packet.write(dst.ip);
  }

  /** Create IP4 Packet with supplied parameters. */
  public void create(IP4 src, IP4 dst, int length, byte protocol) {
    this.length = length;
    this.src = src;
    this.dst = dst;
    this.protocol = protocol;
    id = (short)new Random().nextInt();
  }
}
