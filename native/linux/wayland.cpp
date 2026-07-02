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
