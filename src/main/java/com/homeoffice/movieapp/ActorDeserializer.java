package com.homeoffice.movieapp;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.node.ArrayNode;

/**
 * @author mbojanec
 *
 */
public class ActorDeserializer extends StdDeserializer<Actor> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6980812524068697405L;

	public ActorDeserializer() {
		this(null);
	}

	public ActorDeserializer(Class<?> vc) {
		super(vc);
	}

	@Override
	public Actor deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JsonProcessingException {

		JsonNode node = p.getCodec().readTree(p);
		Integer actorId = null;
		if (node.get("id") != null) {
			actorId = node.get("id").asInt();
		}
		String firstName = node.get("firstName").asText();
		String lastName = node.get("lastName").asText();

		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

		Date born;
		try {
			born = format.parse(node.get("born").asText());
		} catch (ParseException e) {
			born = new Date();
			e.printStackTrace();
		}
		ArrayNode movieIds = ((ArrayNode) node.get("actorMovies"));

		Actor actor = new Actor();
		actor.setActor_id(actorId);
		actor.setFirstName(firstName);
		actor.setLastName(lastName);
		actor.setBorn(born);

		for (JsonNode jsonNode : movieIds) {
			Movie m = new Movie();
			m.setImdb_id(jsonNode.asInt());
			actor.getActorMovies().add(m);

		}

		return actor;
	}
}