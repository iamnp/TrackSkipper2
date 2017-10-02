package com.trackskipper2;

import android.content.Context;
import android.content.pm.PackageManager;
import android.media.session.MediaSessionManager;
import android.view.KeyEvent;

import java.lang.reflect.Method;

class VolumeKeyLongPressListener {

    private static final String PERMISSION_SET_VOLUME_KEY_LONG_PRESS_LISTENER = "android.permission.SET_VOLUME_KEY_LONG_PRESS_LISTENER";
    private Method setOnVolumeKeyLongPressListener = null;
    private MediaSessionManager mediaSessionManager;
    private Object proxy;
    private OnVolumeKeyLongPressListener listener;
    private final java.lang.reflect.InvocationHandler invocationHandler = new java.lang.reflect.InvocationHandler() {

        @Override
        public Object invoke(Object proxy, java.lang.reflect.Method method, Object[] args) throws java.lang.Throwable {
            if (method.getName().equals("onVolumeKeyLongPress")) {
                listener.onVolumeKeyLongPress((KeyEvent) args[0]);
            }
            return null;
        }
    };

    VolumeKeyLongPressListener(Context ctx, OnVolumeKeyLongPressListener listener) {
        setOnVolumeKeyLongPressListener = getSetOnVolumeKeyLongPressListener();
        mediaSessionManager = (MediaSessionManager) ctx.getSystemService(Context.MEDIA_SESSION_SERVICE);
        this.listener = listener;
        proxy = java.lang.reflect.Proxy.newProxyInstance(
                OnVolumeKeyLongPressListener.class.getClassLoader(),
                new java.lang.Class[]{getOnVolumeKeyLongPressListener()},
                invocationHandler);
    }

    static String checkRequirements(Context ctx) {
        if (ctx.checkCallingOrSelfPermission(PERMISSION_SET_VOLUME_KEY_LONG_PRESS_LISTENER)
                != PackageManager.PERMISSION_GRANTED) {
            return "Permission " + PERMISSION_SET_VOLUME_KEY_LONG_PRESS_LISTENER + " not granted!";
        }

        if (getSetOnVolumeKeyLongPressListener() == null) {
            return "setOnVolumeKeyLongPressListener method not found!";
        }

        if (getOnVolumeKeyLongPressListener() == null) {
            return "onVolumeKeyLongPressListener interface not found!";
        }

        return null;
    }

    private static Method getSetOnVolumeKeyLongPressListener() {
        Class smClass = MediaSessionManager.class;
        Method[] m = smClass.getMethods();
        for (Method mm : m) {
            if (mm.getName().equals("setOnVolumeKeyLongPressListener")) {
                return mm;
            }
        }
        return null;
    }

    private static Class<?> getOnVolumeKeyLongPressListener() {
        Class<?> onVolumeKeyLongPressListener = null;
        try {
            onVolumeKeyLongPressListener = Class.forName("android.media.session.MediaSessionManager$OnVolumeKeyLongPressListener");
        } catch (ClassNotFoundException e) {
            // omitted
        }
        return onVolumeKeyLongPressListener;
    }

    void bind() {
        try {
            setOnVolumeKeyLongPressListener.invoke(mediaSessionManager, proxy, null);
        } catch (Exception e) {
            // ommited
        }
    }

    void unbind() {
        try {
            setOnVolumeKeyLongPressListener.invoke(mediaSessionManager, null, null);
        } catch (Exception e) {
            // ommited
        }
    }

    interface OnVolumeKeyLongPressListener {
        void onVolumeKeyLongPress(KeyEvent event);
    }
}
