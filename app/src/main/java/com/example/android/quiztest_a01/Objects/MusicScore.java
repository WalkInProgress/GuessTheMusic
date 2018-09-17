package com.example.android.quiztest_a01.Objects;

public class MusicScore {
    private String userName;
    private String score;
    private String playListId;
    private String playListName;
    private String playListIcon;
    
    public MusicScore(String userName, String score, String playListId, String playListName, String playListIcon){
        this.userName = userName;
        this.score = score;
        this.playListId = playListId;
        this.playListName = playListName;
        this.playListIcon = playListIcon;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }

    public String getPlaylistId() {
        return playListId;
    }

    public void setPlaylistId(String playlistId) {
        this.playListId = playlistId;
    }

    public String getPlaylistName() {
        return playListName;
    }

    public void setPlaylistName(String playlistName) {
        this.playListName = playlistName;
    }

    public String getPlayListIcon() {
        return playListIcon;
    }

    public void setPlayListIcon(String playListIcon) {
        this.playListIcon = playListIcon;
    }
}
