package entity;
// Generated Nov 20, 2016 9:40:14 PM by Hibernate Tools 4.3.1


import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

import static javax.persistence.GenerationType.IDENTITY;

/**
 * Category generated by hbm2java
 */
@Entity
@Table(name="CATEGORY"
    ,catalog="musicweb"
    , uniqueConstraints = @UniqueConstraint(columnNames="slugCategory") 
)
public class Category  implements java.io.Serializable {


     private Integer idCategory;
     private Country country;
     private String category;
     private String slugCategory;
     private String infoCategory;
     private Set<Album> albums = new HashSet<Album>(0);
     private Set<Playlist> playlists = new HashSet<Playlist>(0);
     private Set<Singer> singers = new HashSet<Singer>(0);

    public Category() {
    }

    public Category(Integer idCategory, Country country, String category, String slugCategory, String infoCategory, Set<Album> albums, Set<Playlist> playlists, Set<Singer> singers) {
        this.idCategory = idCategory;
        this.country = country;
        this.category = category;
        this.slugCategory = slugCategory;
        this.infoCategory = infoCategory;
        this.albums = albums;
        this.playlists = playlists;
        this.singers = singers;
    }

    public Category(String category, String slugCategory) {
        this.category = category;
        this.slugCategory = slugCategory;
    }
    public Category(Country country, String category, String slugCategory, String infoCategory, Set<Album> albums, Set<Playlist> playlists, Set<Singer> singers) {
       this.country = country;
       this.category = category;
       this.slugCategory = slugCategory;
       this.infoCategory = infoCategory;
       this.albums = albums;
       this.playlists = playlists;
       this.singers = singers;
    }
   
     @Id @GeneratedValue(strategy=IDENTITY)

    
    @Column(name="idCategory", unique=true, nullable=false)
    public Integer getIdCategory() {
        return this.idCategory;
    }
    
    public void setIdCategory(Integer idCategory) {
        this.idCategory = idCategory;
    }

@ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="country")
    public Country getCountry() {
        return this.country;
    }
    
    public void setCountry(Country country) {
        this.country = country;
    }

    
    @Column(name="category", nullable=false, length=100)
    public String getCategory() {
        return this.category;
    }
    
    public void setCategory(String category) {
        this.category = category;
    }

    
    @Column(name="slugCategory", unique=true, nullable=false, length=100)
    public String getSlugCategory() {
        return this.slugCategory;
    }
    
    public void setSlugCategory(String slugCategory) {
        this.slugCategory = slugCategory;
    }

    
    @Column(name="infoCategory", length=4000)
    public String getInfoCategory() {
        return this.infoCategory;
    }
    
    public void setInfoCategory(String infoCategory) {
        this.infoCategory = infoCategory;
    }

@OneToMany(fetch=FetchType.LAZY, mappedBy="category")
    public Set<Album> getAlbums() {
        return this.albums;
    }
    
    public void setAlbums(Set<Album> albums) {
        this.albums = albums;
    }

@OneToMany(fetch=FetchType.LAZY, mappedBy="category")
    public Set<Playlist> getPlaylists() {
        return this.playlists;
    }
    
    public void setPlaylists(Set<Playlist> playlists) {
        this.playlists = playlists;
    }

@OneToMany(fetch=FetchType.LAZY, mappedBy="category")
    public Set<Singer> getSingers() {
        return this.singers;
    }
    
    public void setSingers(Set<Singer> singers) {
        this.singers = singers;
    }




}


