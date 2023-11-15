package com.example.videospluscms.object;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MovieVersion {
    @SerializedName("id")
    @Expose
    private Integer versionId;

    @SerializedName("movieId")
    @Expose
    private Integer movieId;

    @SerializedName("movieFormat")
    @Expose
    private String movieFormat;

    @SerializedName("movieResolution")
    @Expose
    private String movieResolution;

    @SerializedName("movieLink")
    @Expose
    private String movieLink;

    public Integer getVersionId() {
        return versionId;
    }

    public void setVersionId(Integer versionId) {
        this.versionId = versionId;
    }

    public Integer getMovieId() {
        return movieId;
    }

    public void setMovieId(Integer movieId) {
        this.movieId = movieId;
    }

    public String getMovieFormat() {
        return movieFormat;
    }

    public void setMovieFormat(String movieFormat) {
        this.movieFormat = movieFormat;
    }

    public String getMovieResolution() {
        return movieResolution;
    }

    public void setMovieResolution(String movieResolution) {
        this.movieResolution = movieResolution;
    }

    public String getMovieLink() {
        return movieLink;
    }

    public void setMovieLink(String movieLink) {
        this.movieLink = movieLink;
    }
}