package com.homeoffice.movieapp;



import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

/**
 * @author mbojanec
 *
 */
@Entity
@Table(name = "movie")
@NamedQueries({
        @NamedQuery(
                name = "Movie.findMovies",
                query = "SELECT m " +
                        "FROM Movie m order by m.imdb_id"
        )
})

@JsonSerialize(using = MovieSerializer.class)
@JsonDeserialize(using = MovieDeserializer.class)
public class Movie implements Serializable {

   
	/**
	 * 
	 */
	private static final long serialVersionUID = -8582331915504501787L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
    private Integer imdb_id;
    @Column(name = "title")
    private String title;
    @Column(name = "description")
    private String description;
    @Column(name = "year")
    private Integer year;
    @Column(name = "last_modified")
    private Date lastModified;
    
    @ManyToMany()
    @JoinTable(
    	name = "movie_actor",
    	joinColumns = @JoinColumn(name = "imdb_id"), 
    	inverseJoinColumns = @JoinColumn(name = "actor_id"))
    Set<Actor> movieActors = new HashSet<Actor>();
    
    @PrePersist
    public void prePersist() {
    	lastModified = new Date();
    }
 
    @PreUpdate
    public void preUpdate() {
    	lastModified = new Date();
    }

	public Integer getImdb_id() {
		return imdb_id;
	}

	public void setImdb_id(Integer imdb_id) {
		this.imdb_id = imdb_id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Integer getYear() {
		return year;
	}

	public void setYear(Integer year) {
		this.year = year;
	}

	public Set<Actor> getMovieActors() {
		return movieActors;
	}

	public void setMovieActors(Set<Actor> movieActors) {
		this.movieActors = movieActors;
	}
}

