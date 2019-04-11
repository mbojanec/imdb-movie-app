package com.homeoffice.movieapp;

import java.io.IOException;

import javax.enterprise.context.RequestScoped;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.IntNode;

/**
 * @author mbojanec
 *
 */
@RequestScoped
public class MovieDeserializer extends StdDeserializer<Movie> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6980812524068697405L;

	public MovieDeserializer() {
		this(null);
	}

	public MovieDeserializer(Class<?> vc) {
		super(vc);
	}

	@Override
	public Movie deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JsonProcessingException {

		JsonNode node = p.getCodec().readTree(p);
		Integer imdbId = null;
		if (node.get("imdb_id") != null) {
			imdbId = node.get("imdb_id").asInt();
		}
		String title = node.get("title").asText();
		String description = node.get("description").asText();
		Integer year = (Integer) ((IntNode) node.get("year")).numberValue();
		ArrayNode actorIds = ((ArrayNode) node.get("movieActors"));

		Movie movie = new Movie();
		movie.setImdb_id(imdbId);
		movie.setTitle(title);
		movie.setDescription(description);
		movie.setYear(year);

		for (JsonNode jsonNode : actorIds) {
			Actor a = new Actor();
			a.setActor_id(jsonNode.asInt());
			movie.getMovieActors().add(a);
		}

		return movie;
	}
}