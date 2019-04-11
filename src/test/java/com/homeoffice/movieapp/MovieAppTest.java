package com.homeoffice.movieapp;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.util.List;

import javax.inject.Inject;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.resolver.api.maven.Maven;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(Arquillian.class)
public class MovieAppTest {

	@Deployment
	public static WebArchive createDeployment() {
		File[] lib1 = Maven.configureResolver().loadPomFromFile("pom.xml").importCompileAndRuntimeDependencies()
				.resolve().withTransitivity().asFile();

		WebArchive bla = ShrinkWrap.create(WebArchive.class).addAsLibraries(lib1).addClass(Actor.class)
				.addClass(Movie.class).addClass(MovieService.class).addAsResource("web.xml", "WEB-INF/web.xml")
				.addAsResource("test-persistence.xml", "META-INF/persistence.xml")
				.addAsResource(EmptyAsset.INSTANCE, "META-INF/beans.xml");
		return bla;
	}

	@Inject
	private MovieService movieBean;

	@Before
	public void fillDB() {
		Movie movie1 = new Movie();
		movie1.setTitle("Movie 1");
		movie1.setDescription("one great movie");
		movie1.setYear(2000);

		Movie movie2 = new Movie();
		movie2.setTitle("Movie 2");
		movie2.setDescription("another great movie");
		movie2.setYear(2001);

		Movie movie3 = new Movie();
		movie3.setTitle("Movie 3");
		movie3.setDescription("yet another great movie");
		movie3.setYear(2002);

		movieBean.saveMovie(movie1);
		movieBean.saveMovie(movie2);
		movieBean.saveMovie(movie3);
	}

	@Test
	public void testSearchMovie() {
		List<Movie> movies = movieBean.searchMovies("2000");
		assertEquals(3, movies.size());
	}

	@Test
	public void testUpdateMovie() {
		Movie movie = movieBean.searchMovies("2000").get(0);

		int imdbId = movie.getImdb_id();

		movie.setTitle(movie.getTitle() + " updated");
		movieBean.updateMovie(movie);

		Movie changedMovie = movieBean.getMovie(imdbId);
		assertEquals("update does not work.", movie.getTitle(), changedMovie.getTitle());
	}

	@Test
	public void testDeleteMovie() {
		int movieCount = movieBean.getMovies().size();

		movieBean.deleteMovie(3);

		assertEquals((movieCount - 1), movieBean.getMovies().size());
	}
}
