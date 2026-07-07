package javaforce.ffm;

import java.lang.foreign.*;
import java.lang.invoke.*;
import static java.lang.foreign.ValueLayout.*;

import javaforce.*;
import javaforce.ffm.*;
import javaforce.api.linux.*;
import javaforce.linux.*;

/** WaylandAPI FFM implementation.
 *
 * NON-AI MACHINE GENERATED CODE - DO NOT EDIT
 */


public class WaylandFFM implements WaylandAPI {

  private FFM ffm;

  private static WaylandFFM instance;
  public static synchronized WaylandFFM getInstance() {
    if (instance == null) {
      instance = new WaylandFFM();
      if (!instance.ffm_init()) {
        JFLog.log("WaylandFFM init failed!");
        instance = null;
      }
    }
    return instance;
  }

  private MethodHandle wl_display_create;
  public long wl_display_create() { try { long _ret_value_ = (long)wl_display_create.invokeExact();return _ret_value_; } catch (Throwable t) { JFLog.log(t);  return -1;} }

  private MethodHandle wl_create_loop_create;
  public long wl_create_loop_create() { try { long _ret_value_ = (long)wl_create_loop_create.invokeExact();return _ret_value_; } catch (Throwable t) { JFLog.log(t);  return -1;} }

  private MethodHandle wl_display_add_socket_auto;
  public String wl_display_add_socket_auto(long display) { try { String _ret_value_ = FFM.getString((MemorySegment)wl_display_add_socket_auto.invokeExact(display));return _ret_value_; } catch (Throwable t) { JFLog.log(t);  return null;} }

  private MethodHandle wl_display_run;
  public void wl_display_run(long display) { try { wl_display_run.invokeExact(display); } catch (Throwable t) { JFLog.log(t); } }

  private MethodHandle wl_display_destroy;
  public boolean wl_display_destroy(long display) { try { boolean _ret_value_ = (boolean)wl_display_destroy.invokeExact(display);return _ret_value_; } catch (Throwable t) { JFLog.log(t);  return false;} }

  private MethodHandle wlr_session_create;
  public long wlr_session_create(long display) { try { long _ret_value_ = (long)wlr_session_create.invokeExact(display);return _ret_value_; } catch (Throwable t) { JFLog.log(t);  return -1;} }

  private MethodHandle wlr_backend_autocreate;
  public long wlr_backend_autocreate(long event_loop,long session_ptr) { try { long _ret_value_ = (long)wlr_backend_autocreate.invokeExact(event_loop,session_ptr);return _ret_value_; } catch (Throwable t) { JFLog.log(t);  return -1;} }

  private MethodHandle wlr_backend_start;
  public void wlr_backend_start(long backend) { try { wlr_backend_start.invokeExact(backend); } catch (Throwable t) { JFLog.log(t); } }

  private MethodHandle wlr_renderer_autocreate;
  public long wlr_renderer_autocreate(long backend) { try { long _ret_value_ = (long)wlr_renderer_autocreate.invokeExact(backend);return _ret_value_; } catch (Throwable t) { JFLog.log(t);  return -1;} }

  private MethodHandle wlr_compositor_create;
  public long wlr_compositor_create(long display,long renderer) { try { long _ret_value_ = (long)wlr_compositor_create.invokeExact(display,renderer);return _ret_value_; } catch (Throwable t) { JFLog.log(t);  return -1;} }

  private MethodHandle wlr_xwayland_create;
  public long wlr_xwayland_create(long display,long compositor,boolean lazy) { try { long _ret_value_ = (long)wlr_xwayland_create.invokeExact(display,compositor,lazy);return _ret_value_; } catch (Throwable t) { JFLog.log(t);  return -1;} }

  private MethodHandle wlr_xwayland_destroy;
  public void wlr_xwayland_destroy(long xwayland) { try { wlr_xwayland_destroy.invokeExact(xwayland); } catch (Throwable t) { JFLog.log(t); } }

  private MethodHandle wlr_backend_destroy;
  public void wlr_backend_destroy(long backend) { try { wlr_backend_destroy.invokeExact(backend); } catch (Throwable t) { JFLog.log(t); } }


  private boolean ffm_init() {
    if (FFM.debug) JFLog.log("WaylandFFM init started");
    MethodHandle init;
    ffm = FFM.getInstance();
    Library[] libs = new Library[] {new Library("wayland-server"),new Library("wayland-client"),new Library("wlroots")};
    Library.findLibraries(null, libs);
    Arena arena = Arena.ofAuto();
    init = ffm.getFunction("WaylandAPIinit", ffm.getFunctionDesciptor(ValueLayout.JAVA_BOOLEAN,ADDRESS,ADDRESS,ADDRESS));
    if (init == null) return false;
    try {if (!(boolean)init.invokeExact(libs[0].getPath(arena),libs[1].getPath(arena),libs[2].getPath(arena))) return false;} catch (Throwable t) {JFLog.log(t); return false;}

    wl_display_create = ffm.getFunctionPtr("_wl_display_create", ffm.getFunctionDesciptor(JAVA_LONG));
    wl_create_loop_create = ffm.getFunctionPtr("_wl_create_loop_create", ffm.getFunctionDesciptor(JAVA_LONG));
    wl_display_add_socket_auto = ffm.getFunctionPtr("_wl_display_add_socket_auto", ffm.getFunctionDesciptor(ADDRESS,JAVA_LONG));
    wl_display_run = ffm.getFunctionPtr("_wl_display_run", ffm.getFunctionDesciptorVoid(JAVA_LONG));
    wl_display_destroy = ffm.getFunctionPtr("_wl_display_destroy", ffm.getFunctionDesciptor(JAVA_BOOLEAN,JAVA_LONG));
    wlr_session_create = ffm.getFunctionPtr("_wlr_session_create", ffm.getFunctionDesciptor(JAVA_LONG,JAVA_LONG));
    wlr_backend_autocreate = ffm.getFunctionPtr("_wlr_backend_autocreate", ffm.getFunctionDesciptor(JAVA_LONG,JAVA_LONG,JAVA_LONG));
    wlr_backend_start = ffm.getFunctionPtr("_wlr_backend_start", ffm.getFunctionDesciptorVoid(JAVA_LONG));
    wlr_renderer_autocreate = ffm.getFunctionPtr("_wlr_renderer_autocreate", ffm.getFunctionDesciptor(JAVA_LONG,JAVA_LONG));
    wlr_compositor_create = ffm.getFunctionPtr("_wlr_compositor_create", ffm.getFunctionDesciptor(JAVA_LONG,JAVA_LONG,JAVA_LONG));
    wlr_xwayland_create = ffm.getFunctionPtr("_wlr_xwayland_create", ffm.getFunctionDesciptor(JAVA_LONG,JAVA_LONG,JAVA_LONG,JAVA_BOOLEAN));
    wlr_xwayland_destroy = ffm.getFunctionPtr("_wlr_xwayland_destroy", ffm.getFunctionDesciptorVoid(JAVA_LONG));
    wlr_backend_destroy = ffm.getFunctionPtr("_wlr_backend_destroy", ffm.getFunctionDesciptorVoid(JAVA_LONG));
    if (FFM.debug) JFLog.log("WaylandFFM init complete");
    return true;
  }
}
