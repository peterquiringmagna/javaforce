package javaforce.controls;

import java.util.*;
import java.util.function.*;
import java.time.*;
import java.security.cert.*;
import java.util.concurrent.*;

import javaforce.*;

import org.eclipse.milo.opcua.stack.core.*;
import org.eclipse.milo.opcua.stack.core.transport.*;
import org.eclipse.milo.opcua.stack.core.types.builtin.*;
import org.eclipse.milo.opcua.stack.core.security.*;
import org.eclipse.milo.opcua.stack.core.types.enumerated.*;
import org.eclipse.milo.opcua.stack.core.types.structured.*;
import org.eclipse.milo.opcua.sdk.client.*;
import org.eclipse.milo.opcua.sdk.client.identity.*;
import org.eclipse.milo.opcua.sdk.server.*;
import org.eclipse.milo.opcua.sdk.server.util.*;
import static org.eclipse.milo.opcua.sdk.server.OpcUaServerConfig.*;
import org.eclipse.milo.opcua.stack.core.util.EndpointUtil;

/** OPCUA Client.
 *
 *  See : https://github.com/eclipse-milo/milo/blob/main/milo-examples/client-examples/src/main/java/org/eclipse/milo/examples/client/ClientExample.java
 *
 * @author pquiring
 */

public class OPCUAClient {
  private String host;
  private int port;
  private OpcUaClient client;

  private static boolean netty;

  public boolean connect(String host) {
    return connect(host, 4840);
  }

  public boolean connect(String host, int port) {
    this.host = host;
    this.port = port;
    try {
      if (!netty) {
        netty = true;
        JFLog.log("Disabling netty unsafe usage!");
        System.setProperty("org.jboss.netty.debug", "true");
        System.setProperty("io.netty.allocator.numDirectArenas", "0");
        System.setProperty("io.netty.noPreferDirect", "true");
        System.setProperty("io.netty.noUnsafe", "true");
      }
      client = createClient(host, port);
      println("connecting...");

      // Milo's connect() is asynchronous. If you don't wait for the returned Future,
      // the application can appear to stall or never complete the handshake because
      // the client is still trying to negotiate the connection with the server.
      client.connect().get(10, TimeUnit.SECONDS);
      return true;
    } catch (TimeoutException e) {
      JFLog.log("OPC UA connection to " + host + ":" + port + " timed out.");
      disconnect();
      return false;
    } catch (Exception e) {
      JFLog.log(e);
      disconnect();
      return false;
    }
  }

  public void disconnect() {
    if (client != null) {
      try {
        client.disconnect().get(5, TimeUnit.SECONDS);
      } catch (Exception e) {
        JFLog.log(e);
      }
    }
  }

  private IdentityProvider getIdentityProvider() {
    return new AnonymousProvider();
  }

  private OpcUaClient createClient(String host, int port) throws Exception {
    OpcUaClientConfigBuilder clientConfigBuilder =
      OpcUaClientConfig.builder()
        .setEndpoint(createEndpointDescription(host, port))
        .setApplicationName(LocalizedText.english("eclipse milo opc-ua client"))
        .setApplicationUri("urn:eclipse:milo:examples:client")
        .setIdentityProvider(getIdentityProvider());

    configureClient(clientConfigBuilder);

    return OpcUaClient.create(clientConfigBuilder.build());
  }

  void configureClient(OpcUaClientConfigBuilder builder) {}

  private EndpointDescription createEndpointDescription(String host, int port) {
    return new EndpointDescription(createUrl(host, port), null, null, null, null, null, null, null);
  }

  private String createUrl(String host, int port) {
    String url = "opc.tcp://" + host + ":" + port;
    println("URL=" + url);
    return url;
  }

  private static void println(String ln) {
    System.out.println(ln);
  }

  public static void main(String[] args) {
    try {
      if (args.length != 2) {
        println("Usage: OPCUAClient host port");
        return;
      }
      OPCUAClient client = new OPCUAClient();
      if (!client.connect(args[0], Integer.valueOf(args[1]))) {
        println("Connection failed!");
        return;
      } else {
        println("Connection successful!");
      }
    } catch (Throwable t) {
      JFLog.log(t);
    }
  }
}
