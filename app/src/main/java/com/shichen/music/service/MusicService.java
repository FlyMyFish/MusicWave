package com.shichen.music.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.AudioManager;
import android.os.Bundle;
import android.os.RemoteException;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v4.media.MediaBrowserCompat;
import android.support.v4.media.MediaBrowserServiceCompat;
import android.support.v4.media.session.MediaControllerCompat;
import android.support.v4.media.session.MediaSessionCompat;

import java.util.List;

/**
 * @author shichen 754314442@qq.com
 * Created by shichen on 2019/4/24.
 */
public class MusicService extends MediaBrowserServiceCompat {
    private BecomingNoisyReceiver becomingNoisyReceiver;
    private NotificationManagerCompat notificationManagerCompat;
    private NotificationBuilder notificationBuilder;

    @Nullable
    @Override
    public BrowserRoot onGetRoot(@NonNull String s, int i, @Nullable Bundle bundle) {
        return null;
    }

    @Override
    public void onLoadChildren(@NonNull String s, @NonNull Result<List<MediaBrowserCompat.MediaItem>> result) {

    }

    private class BecomingNoisyReceiver extends BroadcastReceiver {
        private Context context;

        private IntentFilter noisyIntentFilter;
        private MediaControllerCompat controller;
        private boolean registered = false;

        public BecomingNoisyReceiver(Context context, MediaSessionCompat.Token sessionToken) {
            this.context = context;
            noisyIntentFilter = new IntentFilter(AudioManager.ACTION_AUDIO_BECOMING_NOISY);
            try {
                controller = new MediaControllerCompat(context, sessionToken);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }

        private void register() {
            if (!registered) {
                context.registerReceiver(this, noisyIntentFilter);
                registered = true;
            }
        }

        private void unregister() {
            if (registered) {
                context.unregisterReceiver(this);
                registered = false;
            }
        }

        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction() != null) {
                if (intent.getAction().equals(AudioManager.ACTION_AUDIO_BECOMING_NOISY)) {
                    controller.getTransportControls().pause();
                }
            }
        }
    }
}
