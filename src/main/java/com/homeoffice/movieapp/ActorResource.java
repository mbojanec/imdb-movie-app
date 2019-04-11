package com.homeoffice.movieapp;

import java.util.Date;
import java.util.List;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.Response;

import org.eclipse.microprofile.metrics.annotation.Counted;

/**
 * @author mbojanec
 *
 */
@RequestScoped
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@Path("actors")
public class ActorResource {

	@Inject
	private ActorService actorBean;

	@GET
	@Counted(name = "get_all_actors_counter", monotonic = true)
	public Response getAllActors(@Context Request request, @QueryParam("limit") int limit,
			@QueryParam("offset") int offset) {
		List<Actor> actors;
		if (limit > 0) {
			actors = actorBean.getActors(limit, offset);
		} else {
			actors = actorBean.getActors();
		}
		Date lastModifiedDate;
		if (actors.size() > 0) {
			lastModifiedDate = actorBean.getLastModified();

		} else {
			lastModifiedDate = new Date(Long.MIN_VALUE);
		}
		Response.ResponseBuilder responseBuilder = request.evaluatePreconditions(lastModifiedDate);

		if (responseBuilder == null) {
			return Response.ok(actors).lastModified(lastModifiedDate).build();
		} else {
			return responseBuilder.build();
		}
	}

	@GET
	@Path("{actorId}")
	@Counted(name = "get_actor_counter", monotonic = true)
	public Response getActor(@PathParam("actorId") Integer actorId, @Context Request request) {
		Actor actor = actorBean.getActor(actorId);

		if (actor != null) {
			Date lastModifiedDate = actorBean.getLastModified(actorId);
			Response.ResponseBuilder responseBuilder = request.evaluatePreconditions(lastModifiedDate);
			if (responseBuilder == null) {
				return Response.ok(actor).lastModified(lastModifiedDate).build();
			} else {
				return responseBuilder.build();
			}
		} else {
			return Response.status(Response.Status.NOT_FOUND).build();
		}
	}

	@POST
	@Counted(name = "post_actor_counter", monotonic = true)
	public Response addNewActor(Actor actor) {
		actorBean.saveActor(actor);
		return Response.noContent().build();
	}

	@PUT
	@Counted(name = "put_actor_counter", monotonic = true)
	public Response updateActor(Actor actor) {
		actorBean.updateActor(actor);
		return Response.noContent().build();
	}

	@DELETE
	@Path("{actorId}")
	@Counted(name = "delete_actor_counter", monotonic = true)
	public Response deleteActor(@PathParam("actorId") Integer actorId) {
		actorBean.deleteActor(actorId);
		return Response.noContent().build();
	}
}