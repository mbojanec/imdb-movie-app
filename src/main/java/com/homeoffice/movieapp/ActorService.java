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
public class ActorService {

	private static final EntityManagerFactory emFactory;
    private static final String PERSISTENCE_UNIT_NAME = "moviedb";  
 
    static {
        emFactory = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT_NAME);
    }
 
    public static EntityManager getEntityManager() {
        return emFactory.createEntityManager();
    }
    
    private EntityManager em = getEntityManager();

    public Actor getActor(Integer actorId) {
        return em.find(Actor.class, actorId);
    }

    public List<Actor> getActors() {
        List<Actor> actors = em
                .createNamedQuery("Actor.findActors", Actor.class)
                .getResultList();

        return actors;
    }
    
    @SuppressWarnings("unchecked")
   	public List<Actor> getActors(int limit, int offset) {
       	Query query = em.createNamedQuery("Actor.findActors", Actor.class);
       	query.setFirstResult(offset); 
       	query.setMaxResults(limit);
           List<Actor> actors = query.getResultList();

           return actors;
       }

    @Transactional
    public void saveActor(Actor actor) {
        if (actor != null) {
        	em.getTransaction().begin();
            em.persist(actor);
            
            for (Movie movie : actor.getActorMovies()) {
				Movie tempMovie = em.find(Movie.class, movie.getImdb_id());
				tempMovie.getMovieActors().add(actor);
				em.persist(tempMovie);
			}
            em.getTransaction().commit();
        }

    }
    
    @Transactional
    public void updateActor(Actor actor) {
        if (actor != null) {
        	em.getTransaction().begin();
            em.merge(actor);
            for (Movie movie : actor.getActorMovies()) {
				Movie tempMovie = em.find(Movie.class, movie.getImdb_id());
				tempMovie.getMovieActors().remove(actor);
				em.merge(tempMovie);
			}
            em.getTransaction().commit();
        }
    }

    @Transactional
    public void deleteActor(Integer actorId) {
        Actor actor = em.find(Actor.class, actorId);
        if (actor != null) {
        	em.getTransaction().begin();
        	for (Movie movie : actor.getActorMovies()) {
				Movie tempMovie = em.find(Movie.class, movie.getImdb_id());
				tempMovie.getMovieActors().remove(actor);
				em.merge(tempMovie);
			}
            em.remove(actor);
            em.getTransaction().commit();
        }
    }
    
    public Date getLastModified() {
    	return (Date) em.createNativeQuery("select max(last_modified) from actor").getResultList().get(0);
    }
    
    public Date getLastModified(Integer actorId) {
    	return (Date) em.createQuery("select a.lastModified from Actor a where a.actor_id = :actorId").setParameter("actorId", actorId).getResultList().get(0);
    }
    
}