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
@Path("movies")
public class MovieResource {

	@Inject
	private MovieService movieBean;

	@GET
	@Counted(name = "get_all_movies_counter", monotonic = true)
	public Response getAllMovies(@Context Request request, @QueryParam("limit") int limit,
			@QueryParam("offset") int offset) {
		List<Movie> movies;
		if (limit > 0) {
			movies = movieBean.getMovies(limit, offset);
		} else {
			movies = movieBean.getMovies();
		}
		Date lastModifiedDate;
		if (movies.size() > 0) {
			lastModifiedDate = movieBean.getLastModified();

		} else {
			lastModifiedDate = new Date(Long.MIN_VALUE);
		}
		Response.ResponseBuilder responseBuilder = request.evaluatePreconditions(lastModifiedDate);
		if (responseBuilder == null) {
			return Response.ok(movies).lastModified(lastModifiedDate).build();
		} else {
			return responseBuilder.build();
		}
	}

	@GET
	@Path("/search")
	@Counted(name = "get_search_movies_counter", monotonic = true)
	public Response searchMovies(@QueryParam("searchText") String searchText) {
		List<Movie> movies = movieBean.searchMovies(searchText);

		return movies != null ? Response.ok(movies).build() : Response.status(Response.Status.NOT_FOUND).build();
	}

	@GET
	@Path("{imdbId}")
	@Counted(name = "get_movie_counter", monotonic = true)
	public Response getMovie(@PathParam("imdbId") Integer imdbId, @Context Request request) {

		Movie movie = movieBean.getMovie(imdbId);

		if (movie != null) {
			Date lastModifiedDate = movieBean.getLastModified(imdbId);
			Response.ResponseBuilder responseBuilder = request.evaluatePreconditions(lastModifiedDate);
			if (responseBuilder == null) {
				return Response.ok(movie).lastModified(lastModifiedDate).build();
			} else {
				return responseBuilder.build();
			}
		} else {
			return Response.status(Response.Status.NOT_FOUND).build();
		}
	}

	@POST
	@Counted(name = "post_movie_counter", monotonic = true)
	public Response addNewMovie(Movie movie) {

		Boolean success = movieBean.saveMovie(movie);
		if (success) {
			return Response.noContent().build();
		} else {
			return Response.status(Response.Status.CONFLICT).build();
		}
	}

	@PUT
	@Counted(name = "put_movie_counter", monotonic = true)
	public Response updateMovie(Movie movie) {

		Boolean success = movieBean.updateMovie(movie);
		if (success) {
			return Response.noContent().build();
		} else { 
			return Response.status(Response.Status.NOT_FOUND).build();
		}
	}

	@DELETE
	@Path("{imdbId}")
	@Counted(name = "delete_movie_counter", monotonic = true)
	public Response deleteMovie(@PathParam("imdbId") Integer imdbId) {
		movieBean.deleteMovie(imdbId);
		return Response.noContent().build();
	}
}