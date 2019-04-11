package com.homeoffice.movieapp;

import java.io.IOException;
import java.util.stream.Collectors;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

/**
 * @author mbojanec
 *
 */
public class MovieSerializer extends StdSerializer<Movie> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2074783085076640236L;

	public MovieSerializer() {
		this(null);
	}

	public MovieSerializer(Class<Movie> t) {
		super(t);
	}

	public void serialize(Movie value, JsonGenerator jgen, SerializerProvider provider) throws IOException {
		jgen.writeStartObject();
		jgen.writeNumberField("imdb_id", value.getImdb_id().intValue());
		jgen.writeStringField("title", value.getTitle());
		jgen.writeStringField("description", value.getDescription());
		jgen.writeObjectField("year", value.getYear());
		jgen.writeFieldName("movieActors");
		jgen.writeObject(value.getMovieActors().stream().map(Actor::getActor_id).collect(Collectors.toList()));
		jgen.writeEndObject();

	}
}