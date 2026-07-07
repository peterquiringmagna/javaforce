struct WLToolkit {
  void* wl_surface;
  void* wl_view;
};

static jlong getWaylandID(JNIEnv *e, jobject c) {
  JAWT_DrawingSurface* ds;
  JAWT_DrawingSurfaceInfo* dsi;
  jint lock;
  JAWT awt;

  if (jawt == NULL) return 0;
  if (_JAWT_GetAWT == NULL) return 0;

  awt.version = JAWT_VERSION_1_4;
  if (!(*_JAWT_GetAWT)(e, &awt)) {
    printf("JAWT_GetAWT() failed\n");
    return 0;
  }

  ds = awt.GetDrawingSurface(e, c);
  if (ds == NULL) {
    printf("JAWT.GetDrawingSurface() failed\n");
    return 0;
  }
  lock = ds->Lock(ds);
  if ((lock & JAWT_LOCK_ERROR) != 0) {
    awt.FreeDrawingSurface(ds);
    printf("JAWT.Lock() failed\n");
    return 0;
  }
  dsi = ds->GetDrawingSurfaceInfo(ds);
  if (dsi == NULL) {
    printf("JAWT.GetDrawingSurfaceInfo() failed\n");
    return 0;
  }
  WLToolkit* xdsi = (WLToolkit*)dsi->platformInfo;
  printf("xdsi=%p\n", xdsi);
  if (xdsi == NULL) {
    printf("JAWT.platformInfo == NULL\n");
    return 0;
  }
  jlong handle = (jlong)xdsi->wl_surface;
  ds->FreeDrawingSurfaceInfo(dsi);
  ds->Unlock(ds);
  awt.FreeDrawingSurface(ds);

  return handle;
}

void* wl_server = NULL;
void* wl_client = NULL;
void* wl_roots = NULL;

extern "C" {
  JNIEXPORT void* _wl_display_create;
  JNIEXPORT bool WaylandAPIinit(const char* libwayland_server, const char* libwayland_client, const char* libwlroots) {
    if (wl_server == NULL && libwayland_server != NULL) {
      wl_server = dlopen(libwayland_server, RTLD_LAZY | RTLD_GLOBAL);
      if (wl_server == NULL) {
        printf("Warning:dlopen(wayland_server.so) unsuccessful\n");
      } else {
        getFunction(wl_server, (void**)&_wl_display_create, "wl_display_create");
        getFunction(wl_server, (void**)&_wl_create_loop_create, "wl_create_loop_create");
        getFunction(wl_server, (void**)&_wl_display_add_socket_auto, "wl_display_add_socket_auto");
        getFunction(wl_server, (void**)&_wl_display_run, "wl_display_run");
        getFunction(wl_server, (void**)&_wl_display_destroy, "wl_display_destroy");

      }
    }
    if (wl_client == NULL && libwayland_client != NULL) {
      wl_client = dlopen(libwayland_client, RTLD_LAZY | RTLD_GLOBAL);
      if (wl_client == NULL) {
        printf("Warning:dlopen(wayland_client.so) unsuccessful\n");
      } else {
        //TODO
      }
    }
    if (wl_roots == NULL && libwayland_roots != NULL) {
      wl_roots = dlopen(libwayland_roots, RTLD_LAZY | RTLD_GLOBAL);
      if (wl_roots == NULL) {
        printf("Warning:dlopen(wlroots.so) unsuccessful\n");
      } else {
        getFunction(wl_roots, (void**)&_wlr_session_create, "wlr_session_create");
        getFunction(wl_roots, (void**)&_wlr_backend_autocreate, "wlr_backend_autocreate");
        getFunction(wl_roots, (void**)&_wlr_backend_start, "wlr_backend_start");
        getFunction(wl_roots, (void**)&_wlr_renderer_autocreate, "wlr_renderer_autocreate");
        getFunction(wl_roots, (void**)&_wlr_compositor_create, "wlr_compositor_create");
        getFunction(wl_roots, (void**)&_wlr_xwayland_create, "wlr_xwayland_create");
        getFunction(wl_roots, (void**)&_wlr_xwayland_destroy, "wlr_xwayland_destroy");
        getFunction(wl_roots, (void**)&_wlr_backend_destroy, "wlr_backend_destroy");
      }
    }
    return JNI_TRUE;
  }
}
