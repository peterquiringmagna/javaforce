package javaforce.net;

/** Sub Packet
 *
 * @author pquiring
 */

public interface SubPacket {
  public int getSize();
  public void read(Packet packet) throws Exception;  
  public void write(Packet packet) throws Exception;  
}
