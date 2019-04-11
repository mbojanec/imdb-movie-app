package com.homeoffice.movieapp;


import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.stream.Collectors;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

/**
 * @author mbojanec
 *
 */
public class ActorSerializer extends StdSerializer<Actor> {
    
    /**
	 * 
	 */
	private static final long serialVersionUID = 2074783085076640236L;

	public ActorSerializer() {
        this(null);
    }
   
    public ActorSerializer(Class<Actor> t) {
        super(t);
    }

	@Override
	public void serialize(Actor value, JsonGenerator jgen, SerializerProvider provider) throws IOException {
		final SimpleDateFormat FORMATTER = new SimpleDateFormat("yyyy-MM-dd");
		
		jgen.writeStartObject();
        jgen.writeNumberField("id", value.getActor_id());
        jgen.writeStringField("firstName", value.getFirstName());
        jgen.writeStringField("lastName", value.getLastName());
        jgen.writeObjectField("born", FORMATTER.format(value.getBorn()));
        jgen.writeFieldName("actorMovies");
        jgen.writeObject(value.getActorMovies().stream().map(Movie::getImdb_id).collect(Collectors.toList()));
        jgen.writeEndObject();
		
	}
}