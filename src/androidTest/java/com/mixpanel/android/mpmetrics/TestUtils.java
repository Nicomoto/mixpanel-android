package com.mixpanel.android.mpmetrics;

import android.content.Context;
import android.content.SharedPreferences;

import java.io.UnsupportedEncodingException;
import java.util.Map;
import java.util.concurrent.Future;

public class TestUtils {
    public static byte[] bytes(String s) {
        try {
            return s.getBytes("UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("This is not an android device, or a compatible java. WHO ARE YOU?");
        }
    }

    public static class CleanMixpanelAPI extends MixpanelAPI {
        public CleanMixpanelAPI(final Context context, final Future<SharedPreferences> referrerPreferences, final String token) {
            super(context, referrerPreferences, token);
        }

        @Override
        /* package */ PersistentIdentity getPersistentIdentity(final Context context, final Future<SharedPreferences> referrerPreferences, final String token) {
            final String prefsName = "com.mixpanel.android.mpmetrics.MixpanelAPI_" + token;
            final SharedPreferences ret = context.getSharedPreferences(prefsName, Context.MODE_PRIVATE);
            ret.edit().clear().commit();

            return super.getPersistentIdentity(context, referrerPreferences, token);
        }

        @Override
        /* package */ boolean sendAppOpen() {
            return false;
        }
    }

    public static class TestResourceIds implements ResourceIds {
        public TestResourceIds(final Map<String, Integer> anIdMap) {
            mIdMap = anIdMap;
        }

        @Override
        public boolean knownIdName(String name) {
            return mIdMap.containsKey(name);
        }

        @Override
        public int idFromName(String name) {
            return mIdMap.get(name);
        }

        @Override
        public String nameForId(int id) {
            for (Map.Entry<String, Integer> entry : mIdMap.entrySet()) {
                if (entry.getValue() == id) {
                    return entry.getKey();
                }
            }

            return null;
        }

        private final Map<String, Integer> mIdMap;
    }
}
