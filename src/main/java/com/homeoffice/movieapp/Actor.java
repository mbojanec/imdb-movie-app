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
import javax.persistence.ManyToMany;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

/**
 * @author mbojanec
 *
 */
@Entity
@Table(name = "actor")
@NamedQueries({ @NamedQuery(name = "Actor.findActors", query = "SELECT a " + "FROM Actor a order by a.actor_id") })
@JsonSerialize(using = ActorSerializer.class)
@JsonDeserialize(using = ActorDeserializer.class)
public class Actor implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4426185701753735549L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer actor_id;
	@Column(name = "first_name")
	private String firstName;
	@Column(name = "last_name")
	private String lastName;
	@Column(name = "born")
	@Temporal(TemporalType.DATE)
	private Date born;
	@Column(name = "last_modified")
	private Date lastModified;

	@JsonIgnore
	@ManyToMany(mappedBy = "movieActors")
	Set<Movie> actorMovies = new HashSet<Movie>();

	@PrePersist
	public void prePersist() {
		lastModified = new Date();
	}

	@PreUpdate
	public void preUpdate() {
		lastModified = new Date();
	}

	public Integer getActor_id() {
		return actor_id;
	}

	public void setActor_id(Integer actor_id) {
		this.actor_id = actor_id;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public Date getBorn() {
		return born;
	}

	public void setBorn(Date born) {
		this.born = born;
	}

	public Set<Movie> getActorMovies() {
		return actorMovies;
	}

	public void setActorMovies(Set<Movie> actorMovies) {
		this.actorMovies = actorMovies;
	}
}
