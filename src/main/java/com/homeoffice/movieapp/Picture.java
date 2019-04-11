package com.homeoffice.movieapp;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * @author mbojanec
 *
 */
@Entity
@Table(name = "picture")

public class Picture implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4426185701753735549L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer picture_id;
	@ManyToOne
	@JoinColumn(name = "imdb_id")
	Movie movie;

	@Lob
	private byte[] picture;

	public Integer getPicture_id() {
		return picture_id;
	}

	public void setPicture_id(Integer picture_id) {
		this.picture_id = picture_id;
	}

	public Movie getMovie() {
		return movie;
	}

	public void setMovie(Movie movie) {
		this.movie = movie;
	}

	public byte[] getPicture() {
		return picture;
	}

	public void setPicture(byte[] picture) {
		this.picture = picture;
	}
}
