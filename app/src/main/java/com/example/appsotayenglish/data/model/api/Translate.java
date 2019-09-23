package com.example.appsotayenglish.data.model.api;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Translate {
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("translate")
    @Expose
    private String translate;
    @SerializedName("language_code")
    @Expose
    private String languageCode;
    @SerializedName("sound")
    @Expose
    private String sound;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTranslate() {
        return translate;
    }

    public void setTranslate(String translate) {
        this.translate = translate;
    }

    public String getLanguageCode() {
        return languageCode;
    }

    public void setLanguageCode(String languageCode) {
        this.languageCode = languageCode;
    }

    public String getSound() {
        return sound;
    }

    public void setSound(String sound) {
        this.sound = sound;
    }
}
