/*
 * Copyright (C) 2020 The Pixel Experience Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.android.internal.util.xdroid;

import android.app.ActivityTaskManager;
import android.app.Application;
import android.app.TaskStackListener;
import android.content.ComponentName;
import android.content.Context;
import android.os.Environment;
import android.os.Binder;
import android.os.Build;
import android.os.Process;
import android.os.SystemProperties;
import android.util.Log;

import java.util.Arrays;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

public class PixelPropsUtils {

    private static final String TAG = PixelPropsUtils.class.getSimpleName();
    private static final String PROP_HOOKS = "persist.sys.pihooks_";
    private static final boolean DEBUG = SystemProperties.getBoolean(PROP_HOOKS + "DEBUG", false);

    private static final Map<String, Object> propsToChange;
    private static final Map<String, Object> propsToChangePixel3XL;

    private static final String[] packagesToChange = {
            "com.android.vending",
            "com.breel.wallpapers20",
            "com.google.android.apps.customization.pixel",
            "com.google.android.apps.fitness",
            "com.google.android.apps.photos",
            "com.google.android.apps.recorder",
            "com.google.android.apps.subscriptions.red",
            "com.google.android.apps.tachyon",
            "com.google.android.apps.turboadapter",
            "com.google.android.apps.wallpaper.pixel",
            "com.google.android.as",
            "com.google.android.dialer",
            "com.google.android.gms.location.history",
            "com.google.android.inputmethod.latin",
            "com.google.android.soundpicker",
            "com.google.pixel.dynamicwallpapers",
            "com.google.pixel.livewallpaper",
            "com.google.android.apps.safetyhub",
            "com.google.android.apps.turbo",
            "com.google.android.apps.wallpaper",
            "com.google.android.apps.maps",
            "com.google.android.gms",
            "com.google.android.apps.nexuslauncher"
    };

    private static final Map<String, String> DEFAULT_VALUES = Map.of(
        "BRAND", "google",
        "MANUFACTURER", "Google",
        "DEVICE", "husky",
        "FINGERPRINT", "google/husky_beta/husky:15/AP31.240517.022/11948202:user/release-keys",
        "MODEL", "Pixel 8 Pro",
        "PRODUCT", "husky_beta",
        "DEVICE_INITIAL_SDK_INT", "21",
        "SECURITY_PATCH", "2024-07-05",
        "ID", "AP31.240617.009"
    );

    private static final String[] packagesToChangePixel3XL = {
            "com.google.android.googlequicksearchbox"
    };

    private static final Map<String, Object> propsToChangeROG1;
    private static final String[] packagesToChangeROG1 = {
            "com.dts.freefireth",
            "com.dts.freefiremax",
            "com.madfingergames.legends"
    };

    private static final Map<String, Object> propsToChangeXP5;
    private static final String[] packagesToChangeXP5 = {
            "com.activision.callofduty.shooter",
            "com.tencent.tmgp.kr.codm",
            "com.garena.game.codm",
            "com.vng.codmvn"
    };

    private static final Map<String, Object> propsToChangeOP8P;
    private static final String[] packagesToChangeOP8P = {
            "com.tencent.ig",
            "com.pubg.imobile",
            "com.pubg.krmobile",
            "com.pubg.newstate",
            "com.vng.pubgmobile",
            "com.rekoo.pubgm",
            "com.tencent.tmgp.pubgmhd",
            "com.riotgames.league.wildrift",
            "com.riotgames.league.wildrifttw",
            "com.riotgames.league.wildriftvn",
            "com.netease.lztgglobal",
            "com.epicgames.fortnite",
            "com.epicgames.portal"
    };

    static {
        propsToChange = new HashMap<>();
        propsToChange.put("BRAND", "google");
        propsToChange.put("MANUFACTURER", "Google");
        propsToChange.put("DEVICE", "redfin");
        propsToChange.put("PRODUCT", "redfin");
        propsToChange.put("MODEL", "Pixel 5");
        propsToChange.put("FINGERPRINT", "google/redfin/redfin:11/RQ3A.211001.001/7641976:user/release-keys");
        propsToChangePixel3XL = new HashMap<>();
        propsToChangePixel3XL.put("BRAND", "google");
        propsToChangePixel3XL.put("MANUFACTURER", "Google");
        propsToChangePixel3XL.put("DEVICE", "crosshatch");
        propsToChangePixel3XL.put("PRODUCT", "crosshatch");
        propsToChangePixel3XL.put("MODEL", "Pixel 3 XL");
        propsToChangePixel3XL.put("FINGERPRINT", "google/crosshatch/crosshatch:11/RQ3A.211001.001/7641976:user/release-keys");
        propsToChangeROG1 = new HashMap<>();
        propsToChangeROG1.put("MODEL", "ASUS_Z01QD");
        propsToChangeROG1.put("MANUFACTURER", "asus");
        propsToChangeXP5 = new HashMap<>();
        propsToChangeXP5.put("MODEL", "SO-52A");
        propsToChangeOP8P = new HashMap<>();
        propsToChangeOP8P.put("MODEL", "IN2020");
        propsToChangeOP8P.put("MANUFACTURER", "OnePlus");
    }

    public static void setProps(String packageName) {
        if (packageName == null){
            return;
        }
        if (Arrays.asList(packagesToChange).contains(packageName)){
            if (DEBUG){
                Log.d(TAG, "Defining props for: " + packageName);
            }
            for (Map.Entry<String, Object> prop : propsToChange.entrySet()) {
                String key = prop.getKey();
                Object value = prop.getValue();
                if (packageName.equals("com.google.android.gms") && key.equals("MODEL")){
                     value = value + "\u200b";
                }
                setPropValue(key, value);
            }
        }
        
        if (Arrays.asList(packagesToChangePixel3XL).contains(packageName)){
            if (DEBUG){
                Log.d(TAG, "Defining props for: " + packageName);
            }
            for (Map.Entry<String, Object> prop : propsToChangePixel3XL.entrySet()) {
                String key = prop.getKey();
                Object value = prop.getValue();
                setPropValue(key, value);
            }
        }
        
        if (Arrays.asList(packagesToChangeROG1).contains(packageName)) {
            if (DEBUG) Log.d(TAG, "Defining props for: " + packageName);
            for (Map.Entry<String, Object> prop : propsToChangeROG1.entrySet()) {
                String key = prop.getKey();
                Object value = prop.getValue();
                setPropValue(key, value);
            }
        }
        
        if (Arrays.asList(packagesToChangeXP5).contains(packageName)) {
            if (DEBUG) Log.d(TAG, "Defining props for: " + packageName);
            for (Map.Entry<String, Object> prop : propsToChangeXP5.entrySet()) {
                String key = prop.getKey();
                Object value = prop.getValue();
                setPropValue(key, value);
            }
        }
        
        if (Arrays.asList(packagesToChangeOP8P).contains(packageName)) {
            if (DEBUG) Log.d(TAG, "Defining props for: " + packageName);
            for (Map.Entry<String, Object> prop : propsToChangeOP8P.entrySet()) {
                String key = prop.getKey();
                Object value = prop.getValue();
                setPropValue(key, value);
            }
        }
    
        // Set proper indexing fingerprint
        if (packageName.equals("com.google.android.settings.intelligence")){
            setPropValue("FINGERPRINT", Build.XDROID_FINGERPRINT);
        }
    }

    private static void setPropValue(String key, Object value){
        try {
          Field field = getBuildClassField(key);
            if (field != null) {
                field.setAccessible(true);
                if (field.getType() == int.class) {
                    if (value instanceof String) {
                        field.set(null, Integer.parseInt((String) value));
                    } else if (value instanceof Integer) {
                        field.set(null, (Integer) value);
                    }
                } else if (field.getType() == long.class) {
                    if (value instanceof String) {
                        field.set(null, Long.parseLong((String) value));
                    } else if (value instanceof Long) {
                        field.set(null, (Long) value);
                    }
                } else {
                    field.set(null, value.toString());
                }
                field.setAccessible(false);
                dlog("Set prop " + key + " to " + value);
            } else {
                Log.e(TAG, "Field " + key + " not found in Build or Build.VERSION classes");
            }
        } catch (NoSuchFieldException | IllegalAccessException | IllegalArgumentException e) {
            Log.e(TAG, "Failed to set prop " + key, e);
        }
    }

    private static Field getBuildClassField(String key) throws NoSuchFieldException {
        try {
            Field field = Build.class.getDeclaredField(key);
            dlog("Field " + key + " found in Build.class");
            return field;
        } catch (NoSuchFieldException e) {
            Field field = Build.VERSION.class.getDeclaredField(key);
            dlog("Field " + key + " found in Build.VERSION.class");
            return field;
        }
    }

    private static void spoofBuildGms() {
        for (Map.Entry<String, String> entry : DEFAULT_VALUES.entrySet()) {
            String propKey = PROP_HOOKS + entry.getKey();
            String value = SystemProperties.get(propKey);
            setPropValue(entry.getKey(), value != null ? value : entry.getValue());
        }
    }

    private static boolean isCallerSafetyNet() {
        return sIsGms && Arrays.stream(Thread.currentThread().getStackTrace())
                .anyMatch(elem -> elem.getClassName().contains("DroidGuard"));
    }

    public static void onEngineGetCertificateChain() {
        // Check stack for SafetyNet or Play Integrity
        if (isCallerSafetyNet() || sIsFinsky) {
            Log.i(TAG, "Blocked key attestation sIsGms=" + sIsGms + " sIsFinsky=" + sIsFinsky);
            throw new UnsupportedOperationException();
        }
    }
    static void dlog(String msg) {
        if (DEBUG) Log.d(TAG, msg);
    }
}
