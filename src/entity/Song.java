package entity;
// Generated Nov 20, 2016 9:40:14 PM by Hibernate Tools 4.3.1

import org.hibernate.search.annotations.*;
import org.hibernate.search.annotations.Index;

import javax.persistence.*;
import java.util.*;

import static javax.persistence.GenerationType.IDENTITY;

/**
 * Song generated by hbm2java
 */
@Entity
@Indexed
@Table(name = "SONG", catalog = "musicweb", uniqueConstraints = @UniqueConstraint(columnNames = "slugSong"))
public class Song implements java.io.Serializable {
	private static final long serialVersionUID = 1L;
	private Integer idSong;
	private Album album;
	private Country country;
	private String nameSong;
	private String keyWord;
	private String slugSong;
	private Date postDate;
	private Integer viewcount;
	private Integer likes;
	private String linkSong;
	private List<Singer> singers = new ArrayList<Singer>(0);
	private List<Users> userses = new ArrayList<Users>(0);
	private Lyric lyric;
	private Set<Comments> commentses = new HashSet<Comments>(0);
	private Set<Playlist> playlists = new HashSet<Playlist>(0);

	public Song() {
	}

	public Song(Integer idSong, Album album, Country country, String nameSong, String keyWord, String slugSong,
			Date postDate, Integer viewcount, Integer likes, String linkSong, List<Singer> singers, List<Users> userses,
			Lyric lyric, Set<Comments> commentses, Set<Playlist> playlists) {
		this.idSong = idSong;
		this.album = album;
		this.country = country;
		this.nameSong = nameSong;
		this.keyWord = keyWord;
		this.slugSong = slugSong;
		this.postDate = postDate;
		this.viewcount = viewcount;
		this.likes = likes;
		this.linkSong = linkSong;
		this.singers = singers;
		this.userses = userses;
		this.lyric = lyric;
		this.commentses = commentses;
		this.playlists = playlists;
	}

	public Song(Date postDate) {
		this.postDate = postDate;
	}

	public Song(Album album, Country country, String nameSong, String keyWord, String slugSong, Date postDate,
			Integer viewcount, Integer likes, String linkSong, List<Singer> singers, List<Users> userses, Lyric lyric,
			Set<Comments> commentses, Set<Playlist> playlists) {
		this.album = album;
		this.country = country;
		this.nameSong = nameSong;
		this.keyWord = keyWord;
		this.slugSong = slugSong;
		this.postDate = postDate;
		this.viewcount = viewcount;
		this.likes = likes;
		this.linkSong = linkSong;
		this.singers = singers;
		this.userses = userses;
		this.lyric = lyric;
		this.commentses = commentses;
		this.playlists = playlists;
	}

	@Id
	@GeneratedValue(strategy = IDENTITY)

	@Column(name = "idSong", unique = true, nullable = false)
	public Integer getIdSong() {
		return this.idSong;
	}

	public void setIdSong(Integer idSong) {
		this.idSong = idSong;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "idAlbum")
	public Album getAlbum() {
		return this.album;
	}

	public void setAlbum(Album album) {
		this.album = album;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "country")
	public Country getCountry() {
		return this.country;
	}

	public void setCountry(Country country) {
		this.country = country;
	}

	@Field(index = Index.YES, analyze = Analyze.YES, store = Store.NO)
	@Column(name = "nameSong", length = 500)
	public String getNameSong() {
		return this.nameSong;
	}

	public void setNameSong(String nameSong) {
		this.nameSong = nameSong;
	}

	@Field(index = Index.YES, analyze = Analyze.YES, store = Store.NO)
	@Column(name = "keyWord", length = 500)
	public String getKeyWord() {
		return this.keyWord;
	}

	public void setKeyWord(String keyWord) {
		this.keyWord = keyWord;
	}

	@Column(name = "slugSong", unique = true, length = 600)
	public String getSlugSong() {
		return this.slugSong;
	}

	public void setSlugSong(String slugSong) {
		this.slugSong = slugSong;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "postDate", nullable = false, length = 19)
	public Date getPostDate() {
		return this.postDate;
	}

	public void setPostDate(Date postDate) {
		this.postDate = postDate;
	}

	@Column(name = "viewcount")
	public Integer getViewcount() {
		return this.viewcount;
	}

	public void setViewcount(Integer viewcount) {
		this.viewcount = viewcount;
	}

	@Column(name = "likes")
	public Integer getLikes() {
		return this.likes;
	}

	public void setLikes(Integer likes) {
		this.likes = likes;
	}

	@Column(name = "linkSong", length = 500)
	public String getLinkSong() {
		return this.linkSong;
	}

	public void setLinkSong(String linkSong) {
		this.linkSong = linkSong;
	}

	@IndexedEmbedded
	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(name = "SONG_AUTHOR", catalog = "musicweb", joinColumns = {
			@JoinColumn(name = "idSong", nullable = false, updatable = false) }, inverseJoinColumns = {
					@JoinColumn(name = "idSinger", nullable = false, updatable = false) })
	public List<Singer> getSingers() {
		return this.singers;
	}

	public void setSingers(List<Singer> singers) {
		this.singers = singers;
	}

	@ManyToMany(fetch = FetchType.LAZY, mappedBy = "songs")
	public List<Users> getUserses() {
		return this.userses;
	}

	public void setUserses(List<Users> userses) {
		this.userses = userses;
	}

	@OneToOne(fetch = FetchType.LAZY, mappedBy = "song")
	public Lyric getLyric() {
		return this.lyric;
	}

	public void setLyric(Lyric lyric) {
		this.lyric = lyric;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "song")
	public Set<Comments> getCommentses() {
		return this.commentses;
	}

	public void setCommentses(Set<Comments> commentses) {
		this.commentses = commentses;
	}

	@ManyToMany(fetch = FetchType.LAZY, mappedBy = "songs")
	public Set<Playlist> getPlaylists() {
		return this.playlists;
	}

	public void setPlaylists(Set<Playlist> playlists) {
		this.playlists = playlists;
	}

	@Override
	public String toString() {
		return "Song [idSong=" + idSong + ", album=" + album + ", nameSong=" + nameSong + ", keyWord=" + keyWord
				+ ", slugSong=" + slugSong + "]";
	}

	public String toJson() {
		return "{ \"nameSong\":\"" + nameSong + "\" , \"slugSong\":\"" + slugSong + "\"}";
	}

}