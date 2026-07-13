package javaforce.net;

/** Access Point.
 *
 * @author pquiring
 */

public class AccessPoint {
  public String ssid;

  public AccessPoint(String ssid) {
    this.ssid = ssid;
  }

  public String toString() {
    return "AccessPoint:" + ssid;
  }
}
