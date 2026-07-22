package javaforce.controls.s7;

import java.util.*;

import javaforce.net.*;

/** S7 Data Packet.
 *
 * Reference : snap7.sf.net
 *
 * Example Tags:
 *   DB100.DBX1.0
 *   DB100.DBB1
 *   DB100.DBW2
 *   DB100.DBD4
 *   DB700.DBB0 BYTE 16
 *   M1.0
 *   MB2
 *   I3.0
 *   IW4
 *   Q5.0
 *   QD6
 *   etc.
 *
 * Not Supported (direct peripheral I/O bits):
 *   PI1.0
 *   PQ2.0
 *   etc.
 *
 * @author pquiring
 */

public class S7Packet {

  /** Creates a packet to connect at COTP level (connect step1). */
  public static byte[] makeConnectPacket1() throws Exception {
    Packet packet = new Packet();
    TPKT tpkt = new TPKT();
    COTP cotp = new COTP();

    int size = tpkt.getSize() + cotp.getSize();
    tpkt.create((short)size);
    tpkt.write(packet);
    cotp.create(COTP.type_connect);
    cotp.write(packet);
    return packet.toByteArray();
  }

  /** Creates a packet to connect at S7 level (connect step2). */
  public static byte[] makeConnectPacket2() throws Exception {
    Packet packet = new Packet();
    TPKT tpkt = new TPKT();
    COTP cotp = new COTP();
    S7Header header = new S7Header();
    S7Params params = new S7Params();

    params.makeConnect();
    int size = tpkt.getSize() + cotp.getSize() + header.getSize() + params.getSize();
    tpkt.create((short)size);
    tpkt.write(packet);
    cotp.create(COTP.type_data);
    cotp.write(packet);
    header.create((short)params.getSize(), (short)0);
    header.write(packet);
    params.write(packet);
    return packet.toByteArray();
  }

  /** Creates a packet to read data from S7. */
  public static byte[] makeReadPacket(S7Data s7) throws Exception {
    Packet packet = new Packet();
    TPKT tpkt = new TPKT();
    COTP cotp = new COTP();
    S7Header header = new S7Header();
    S7Params params = new S7Params();

    params.makeRead(s7);
    int size = tpkt.getSize() + cotp.getSize() + header.getSize() + params.getSize();
    tpkt.create((short)size);
    tpkt.write(packet);
    cotp.create(COTP.type_data);
    cotp.write(packet);
    header.create((short)params.getSize(), (short)0);
    header.write(packet);
    params.write(packet);
    return packet.toByteArray();
  }

  /** Creates a packet to read data from S7. */
  public static byte[] makeReadPacket(S7Data[] s7) throws Exception {
    Packet packet = new Packet();
    TPKT tpkt = new TPKT();
    COTP cotp = new COTP();
    S7Header header = new S7Header();
    S7Params params = new S7Params();

    params.makeRead(s7);
    int size = tpkt.getSize() + cotp.getSize() + header.getSize() + params.getSize();
    tpkt.create((short)size);
    tpkt.write(packet);
    cotp.create(COTP.type_data);
    cotp.write(packet);
    header.create((short)params.getSize(), (short)0);
    header.write(packet);
    params.write(packet);
    return packet.toByteArray();
  }

  public static byte[] makeReadTimePacket() throws Exception {
    Packet packet = new Packet();
    TPKT tpkt = new TPKT();
    COTP cotp = new COTP();
    S7Header header = new S7Header();
    S7Params params = new S7Params();

    header.rosctr = S7Header.ROSCTR_USERDATA;

    params.makeReadTime();
    int size = tpkt.getSize() + cotp.getSize() + header.getSize() + params.getSize();
    tpkt.create((short)size);
    tpkt.write(packet);
    cotp.create(COTP.type_data);
    cotp.write(packet);
    header.create((short)(params.getSize() - 4), (short)4);
    header.write(packet);
    params.write(packet);
    return packet.toByteArray();
  }

  /** Creates a packet to write data to S7. */
  public static byte[] makeWritePacket(S7Data type) throws Exception {
    Packet packet = new Packet();
    TPKT tpkt = new TPKT();
    COTP cotp = new COTP();
    S7Header header = new S7Header();
    S7Params params = new S7Params();

    params.makeWrite(type.block_type, type.block_number, type.data_type, type.offset, type.length, type.data);
    int size = tpkt.getSize() + cotp.getSize() + header.getSize() + params.getSize();
    tpkt.create((short)size);
    tpkt.write(packet);
    cotp.create(COTP.type_data);
    cotp.write(packet);
    header.create((short)(params.getSize() - 4 - type.data.length), (short)(4 + type.data.length));
    header.write(packet);
    params.write(packet);
    return packet.toByteArray();
  }

  public static byte[] makeWriteTimePacket(Calendar dt) throws Exception {
    Packet packet = new Packet();
    TPKT tpkt = new TPKT();
    COTP cotp = new COTP();
    S7Header header = new S7Header();
    S7Params params = new S7Params();

    header.rosctr = S7Header.ROSCTR_USERDATA;

    params.makeWriteTime(dt);
    int size = tpkt.getSize() + cotp.getSize() + header.getSize() + params.getSize();
    tpkt.create((short)size);
    tpkt.write(packet);
    cotp.create(COTP.type_data);
    cotp.write(packet);
    header.create((short)(params.getSize() - 14), (short)14);
    header.write(packet);
    params.write(packet);
    return packet.toByteArray();
  }

  /** Decodes S7 Address.
   *
   * Supports: DB,M,I,Q
   *
   * Does not support ranges yet.
   */
  public static S7Data decodeAddress(String addr) {
    //DB##.DB?##[.#]
    //M[?]##[.#]
    //I[?]##[.#]
    //Q[?]##[.#]
    S7Data data = new S7Data();
    if (addr.startsWith("DB")) {
      data.block_type = S7Types.DB;
      int idx = addr.indexOf('.');  //.DB?##[.#]
      data.block_number = Integer.valueOf(addr.substring(2, idx));
      addr = addr.substring(idx+2);  //B?##[.#]
    } else if (addr.startsWith("M")) {
      data.block_type = S7Types.M;
    } else if (addr.startsWith("I")) {
      data.block_type = S7Types.I;
    } else if (addr.startsWith("Q")) {
      data.block_type = S7Types.Q;
    } else {
      return null;
    }
    data.data_type = S7Types.getType(addr.charAt(1));
    int offset;
    int idx = addr.indexOf('.');
    if (idx == -1) idx = addr.indexOf(' ');
    if (idx == -1) idx = addr.length();
    if (data.data_type == 0) {
      //no type present (assume bit)
      offset = Integer.valueOf(addr.substring(1, idx));
      data.data_type = S7Types.BIT;
    } else {
      offset = Integer.valueOf(addr.substring(2, idx));
    }
    data.offset = offset << 3;
    if (data.data_type == S7Types.BIT) {
      int idx2 = addr.indexOf(' ');
      if (idx2 == -1) idx2 = addr.length();
      byte bit = Byte.valueOf(addr.substring(idx+1, idx2));
      data.offset += bit;
    }
    data.length = 1;  //# of elements (not bytes)
    idx = addr.indexOf(" BIT ");
    if (idx != -1) {
      data.length = Integer.valueOf(addr.substring(idx+5));
    }
    idx = addr.indexOf(" BYTE ");
    if (idx != -1) {
      data.data_type = S7Types.BYTE;
      data.length = Integer.valueOf(addr.substring(idx+6));
    }
    idx = addr.indexOf(" CHAR ");
    if (idx != -1) {
      data.data_type = S7Types.CHAR;
      data.length = Integer.valueOf(addr.substring(idx+6));
    }
    idx = addr.indexOf(" WORD ");
    if (idx != -1) {
      data.data_type = S7Types.WORD;
      data.length = Integer.valueOf(addr.substring(idx+6));
    }
    idx = addr.indexOf(" INT ");
    if (idx != -1) {
      data.data_type = S7Types.INT;
      data.length = Integer.valueOf(addr.substring(idx+5));
    }
    idx = addr.indexOf(" DWORD ");
    if (idx != -1) {
      data.data_type = S7Types.DWORD;
      data.length = Integer.valueOf(addr.substring(idx+7));
    }
    idx = addr.indexOf(" DINT ");
    if (idx != -1) {
      data.data_type = S7Types.DINT;
      data.length = Integer.valueOf(addr.substring(idx+6));
    }
    idx = addr.indexOf(" REAL ");
    if (idx != -1) {
      data.data_type = S7Types.REAL;
      data.length = Integer.valueOf(addr.substring(idx+6));
    }
    return data;
  }

  /** Decodes a packet and returns any data returned. */
  public static S7Data decodePacket(byte[] packet_data) {
    try {
      Packet packet = new Packet(packet_data);
      S7Data data = new S7Data();
      TPKT tpkt = new TPKT();
      tpkt.read(packet);
      COTP cotp = new COTP();
      cotp.read(packet);
      if (cotp.PDU_type == COTP.type_connect) return data;
      if (cotp.PDU_type == COTP.type_connect_ack) return data;
      S7Header header = new S7Header();
      header.read(packet);
      S7Params params = new S7Params();
      params.read(packet, data);
      return data;
    } catch (Exception e) {
      e.printStackTrace();
    }
    return null;
  }

  /** Decodes a packet and returns any data returned. */
  public static S7Data[] decodeMultiPacket(byte[] packet_data, int count) {
    try {
      Packet packet = new Packet(packet_data);
      S7Data[] data = new S7Data[count];
      for(int a=0;a<count;a++) {
        data[a] = new S7Data();
      }
      TPKT tpkt = new TPKT();
      tpkt.read(packet);
      COTP cotp = new COTP();
      cotp.read(packet);
      if (cotp.PDU_type == COTP.type_connect) return data;
      if (cotp.PDU_type == COTP.type_connect_ack) return data;
      S7Header header = new S7Header();
      header.read(packet);
      S7Params params = new S7Params();
      params.read(packet, data);
      return data;
    } catch (Exception e) {
      e.printStackTrace();
    }
    return null;
  }

  /** Decodes a packet and returns any data returned. */
  public static Calendar decodeTimePacket(byte[] packet_data) {
    try {
      Packet packet = new Packet(packet_data);
      Calendar data = Calendar.getInstance();
      TPKT tpkt = new TPKT();
      tpkt.read(packet);
      COTP cotp = new COTP();
      cotp.read(packet);
      S7Header header = new S7Header();
      header.read(packet);
      S7Params params = new S7Params();
      params.read(packet, data);
      return data;
    } catch (Exception e) {
      e.printStackTrace();
    }
    return null;
  }

  public static boolean isPacketComplete(byte[] packet) {
    return decodePacket(packet) != null;
  }

  public static boolean isPacketTimeComplete(byte[] packet) {
    return decodeTimePacket(packet) != null;
  }
}
