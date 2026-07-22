package javaforce.voip;

import javaforce.net.*;

/** PacketReceiver
 *
 * @author pquiring
 */

public interface PacketReceiver {
  public void onPacket(Packet packet);
}
