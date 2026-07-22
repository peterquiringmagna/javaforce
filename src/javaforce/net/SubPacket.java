package javaforce.net;

/** Sub Packet
 *
 * @author pquiring
 */

public interface SubPacket {
  /** Get header size. */
  public int getSize();
  /** Get payload size. */
  public int getDataSize();
  /** Read packet header. */
  public void read(Packet packet) throws Exception;
  /** Write packet header. */
  public void write(Packet packet) throws Exception;
}
