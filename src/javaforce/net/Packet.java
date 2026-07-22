package javaforce.net;

/** SIP/RTSP Packet
 *
 * @author pquiring
 */

public class Packet {
  public Packet() {}
  public Packet(byte[] data, int offset, int length) {
    this.data = data;
    this.offset = offset;
    this.length = length;
  }
  /** Packet data. */
  public byte[] data;
  /** Packet offset. */
  public int offset;
  /** Packet length. */
  public int length;

  /** Packet source host. */
  public String host;
  /** Packet source port. */
  public int port;

  /** Packet media stream. */
  public int stream;
  /** Packet media timestamp. */
  public long ts;
  /** Packet media key frame. */
  public boolean keyFrame;

  public String toString() {
    return "Packet:{data:" + data + ",offset:" + offset + ",length:" + length + "}";
  }
}
