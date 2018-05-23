package rest;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import domain.Actor;
import domain.Film;

@Path("/actors")
@Stateless
public class ActorsResources
{

	@PersistenceContext
	EntityManager em;
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public List<Actor> getAllActors()
	{
		return em.createNamedQuery("actor.all", Actor.class).getResultList();
	}
	
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	public Response addActor(Actor actor)
	{
		em.persist(actor);
		em.flush();
		return Response.ok(actor.getId()).build();
	}
	
	@GET
	@Path("/{actorId}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getActor(@PathParam("actorId") int actorId)
	{
		Actor result = em.createNamedQuery("actor.id", Actor.class)
				.setParameter("actorId", actorId)
				.getSingleResult();
		if(result==null)
		{
			return Response.status(404).build();
		}
		return Response.ok(result).build();
	}
	
	@GET
	@Path("/{actorId}/films")
	@Produces(MediaType.APPLICATION_JSON)
	public List<Film> getFilms(@PathParam("actorId") int actorId)
	{
		Actor result = em.createNamedQuery("actor.id", Actor.class)
				.setParameter("actorId", actorId)
				.getSingleResult();
		if(result==null)
		{
			return null;
		}
		return result.getFilms();
	}
	
	@PUT
	@Path("/{actorId}/films/{filmId}")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response update(@PathParam("actorId") int actorId, @PathParam("filmId") int filmId, Film f)
	{
		Actor resulta = em.createNamedQuery("actor.id", Actor.class)
				.setParameter("actorId", actorId)
				.getSingleResult();
		if(resulta==null)
		{
			return Response.status(404).build();
		}
		
		Film resultf = em.createNamedQuery("film.id", Film.class)
				.setParameter("filmId", filmId)
				.getSingleResult();
		if(resultf==null)
		{
			return Response.status(404).build();
		}
		resultf.setTitle(f.getTitle());
		
		boolean exists = false;
		for(Film film : resulta.getFilms())
		{
			if(film.getId()==filmId)
			{
				exists = true;
				break;
			}
		}
		
		if(!exists)
		{
			resulta.getFilms().add(resultf);
			resultf.getActors().add(resulta);
		}
		
		em.persist(resultf);
		return Response.ok().build();
	}
}
