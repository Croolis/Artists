package givorenon.artists;

import java.util.ArrayList;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class Artist extends RealmObject {
    @PrimaryKey
    private Integer id;
    private String name;
    private String genres;
    private Integer tracks;
    private Integer albums;
    private String link;
    private String description;
    private String cover;

    public Artist() {
    }

    public Artist(Integer id, String name, String genres, Integer tracks, Integer albums,
                  String link, String description, String cover) {
        this.id = id;
        this.name = name;
        this.genres = genres;
        this.tracks = tracks;
        this.albums = albums;
        this.link = link;
        this.description = description;
        this.cover = cover;
    }

    public Artist(Artist anotherArtist) {
        this.id = anotherArtist.getId();
        this.name = anotherArtist.getName();
        this.genres = anotherArtist.getGenres();
        this.tracks = anotherArtist.getTracks();
        this.albums = anotherArtist.getAlbums();
        this.link = anotherArtist.getLink();
        this.description = anotherArtist.getDescription();
        this.cover = anotherArtist.getCover();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGenres() {
        return genres;
    }

    public void setGenres(String genres) {
        this.genres = genres;
    }

    public Integer getTracks() {
        return tracks;
    }

    public void setTracks(Integer tracks) {
        this.tracks = tracks;
    }

    public Integer getAlbums() {
        return albums;
    }

    public void setAlbums(Integer albums) {
        this.albums = albums;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }
}
