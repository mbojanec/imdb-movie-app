package com.homeoffice.movieapp;

import java.util.Date;
import java.util.List;

import javax.enterprise.context.RequestScoped;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;
import javax.transaction.Transactional;

/**
 * @author mbojanec
 *
 */
@RequestScoped
public class MovieService {

	private static final EntityManagerFactory emFactory;
	private static final String PERSISTENCE_UNIT_NAME = "moviedb";

	static {
		emFactory = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT_NAME);
	}

	public static EntityManager getEntityManager() {
		return emFactory.createEntityManager();
	}

	private EntityManager em = getEntityManager();

	public Movie getMovie(Integer imdbId) {
		try {
			return (Movie) em.find(Movie.class, imdbId);

		} catch (Exception e) {

			e.printStackTrace();
			return null;
		}
	}

	public List<Movie> getMovies() {
		List<Movie> movies = em.createNamedQuery("Movie.findMovies", Movie.class).getResultList();

		return movies;
	}

	@SuppressWarnings("unchecked")
	public List<Movie> searchMovies(String searchText) {
		List<Movie> movies = em.createQuery(
				"select m from Movie m where cast(m.imdb_id as text) like :searchText or m.title like :searchText or m.description like :searchText or cast(m.year as text) like :searchText")
				.setParameter("searchText", "%" + searchText + "%").getResultList();

		return movies;
	}

	@SuppressWarnings("unchecked")
	public List<Movie> getMovies(int limit, int offset) {
		Query query = em.createNamedQuery("Movie.findMovies", Movie.class);
		query.setFirstResult(offset);
		query.setMaxResults(limit);
		List<Movie> movies = query.getResultList();

		return movies;
	}

	@Transactional
	public Boolean saveMovie(Movie movie) {
		Boolean success = true;
		if (movie != null) {
			em.getTransaction().begin();
			try {
				em.persist(movie);
				em.getTransaction().commit();
			} catch (Exception e) {
				success = false;
				em.getTransaction().rollback();
			}
		}
		return success;
	}

	@Transactional
	public Boolean updateMovie(Movie movie) {
		Boolean success = true;
		if (movie != null) {
			em.getTransaction().begin();
			try {
				em.merge(movie);
				em.getTransaction().commit();
			} catch (Exception e) {
				success = false;
				em.getTransaction().rollback();
			}
		}
		return success;
	}

	@Transactional
	public void deleteMovie(Integer imdbId) {
		Movie movie = em.find(Movie.class, imdbId);
		if (movie != null) {
			em.getTransaction().begin();
			em.remove(movie);
			em.getTransaction().commit();
		}
	}

	public Date getLastModified() {
		return (Date) em.createNativeQuery("select max(last_modified) from movie").getResultList().get(0);

	}

	public Date getLastModified(Integer imdbId) {
		return (Date) em.createQuery("select m.lastModified from Movie m where m.imdb_id = :imdbId")
				.setParameter("imdbId", imdbId).getResultList().get(0);

	}
}