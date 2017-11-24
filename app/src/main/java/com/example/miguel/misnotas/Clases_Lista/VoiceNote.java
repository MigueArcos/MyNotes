package com.example.miguel.misnotas.Clases_Lista;

import java.io.File;

/**
 * Created by 79812 on 22/11/2017.
 */
public class VoiceNote {
    private String title;
    private String lastUpdate;
    private File audio;
    public VoiceNote(String title, String lastUpdate, File audio) {
        this.title = title;
        this.lastUpdate = lastUpdate;
        this.audio = audio;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate(String lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    public File getAudio() {
        return audio;
    }

    public void setAudio(File audio) {
        this.audio = audio;
    }
}