// MusicPlayerService.aidl
package com.example.audioclient;

// Declare any non-default types here with import statements

interface MusicPlayerService {
    void startMusic(int id);
    void stopMusic();
    void pauseMusic();
    void resumeMusic();
    boolean isStarted();
}