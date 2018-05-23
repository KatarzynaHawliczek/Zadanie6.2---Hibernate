package rest;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import domain.Actor;
import domain.Comment;
import domain.Film;
import domain.Rate;

@Path("/films")
@Stateless
public class FilmsResources
{
	@PersistenceContext
	EntityManager em;

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public List<Film> getAllFilms()
	{
		return em.createNamedQuery("film.all", Film.class).getResultList();
	}
	
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	public Response addFilm(Film film)
	{
		em.persist(film);
		em.flush();
		return Response.ok(film.getId()).build();
	}
	
	@GET
	@Path("/{filmId}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getFilm(@PathParam("filmId") int filmId)
	{
		Film result = em.createNamedQuery("film.id", Film.class)
				.setParameter("filmId", filmId)
				.getSingleResult();
		if(result==null)
		{
			return Response.status(404).build();
		}
		return Response.ok(result).build();
	}
	
	@PUT
	@Path("/{filmId}")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response updateFilm(@PathParam("filmId") int filmId, Film f)
	{
		Film result = em.createNamedQuery("film.id", Film.class)
				.setParameter("filmId", filmId)
				.getSingleResult();
		if(result==null)
		{
			return Response.status(404).build();
		}
		result.setTitle(f.getTitle());
		em.persist(result);
		return Response.ok().build();
	}
	
	@DELETE
	@Path("/{filmId}")
	public Response deleteFilm(@PathParam("filmId") int filmId)
	{
		Film result = em.createNamedQuery("film.id", Film.class)
				.setParameter("filmId", filmId)
				.getSingleResult();
		if(result==null)
		{
			return Response.status(404).build();
		}
		em.remove(result);
		return Response.ok().build();
	}
	
	@GET
	@Path("/{filmId}/rate")
	@Produces(MediaType.APPLICATION_JSON)
	public Rate getRate(@PathParam("filmId") int filmId)
	{
		Film result = em.createNamedQuery("film.id", Film.class)
				.setParameter("filmId", filmId)
				.getSingleResult();
		if(result==null)
		{
			return null;
		}
		Rate rate = new Rate();
		rate.setRate(result.getRate());
		return rate;
	}
	
	@POST
	@Path("/{filmId}/rate")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response addRate(@PathParam("filmId") int filmId, Rate rate)
	{
		Film result = em.createNamedQuery("film.id", Film.class)
				.setParameter("filmId", filmId)
				.getSingleResult();
		if(result==null)
		{
			return Response.status(404).build();
		}
		int sum = result.getRateSum() + rate.getRate();
		int count = result.getRateCount() + 1;
		result.setRateSum(sum);
		result.setRateCount(count);
		return Response.ok().build();
	}
	
	@GET
	@Path("/{filmId}/comments")
	@Produces(MediaType.APPLICATION_JSON)
	public List<Comment> getComments(@PathParam("filmId") int filmId)
	{
		Film result = em.createNamedQuery("film.id", Film.class)
				.setParameter("filmId", filmId)
				.getSingleResult();
		if(result==null)
		{
			return null;
		}
		return result.getComments();
	}
	
	@POST
	@Path("/{filmId}/comments")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response addComment(@PathParam("filmId") int filmId, Comment comment)
	{
		Film result = em.createNamedQuery("film.id", Film.class)
				.setParameter("filmId", filmId)
				.getSingleResult();
		if(result==null)
		{
			return Response.status(404).build();
		}
		comment.setFilm(result);
		result.getComments().add(comment);
		em.persist(comment);
		return Response.ok().build();
	}
	
	@DELETE
	@Path("/{filmId}/comments/{commentId}")
	public Response deleteComment(@PathParam("filmId") int filmId, @PathParam("commentId") int commentId) 
	{
		Film result = em.createNamedQuery("film.id", Film.class)
				.setParameter("filmId", filmId)
				.getSingleResult();
		if(result==null) 
		{
			return Response.status(404).build();
		}
		int i=0;
		for(Comment comment : result.getComments())
		{
			if(comment.getId()==commentId)
			{
				result.getComments().remove(i);
				return Response.ok().build();
			}
			i++;
		}
		return Response.status(404).build();
	}

	@GET
	@Path("{filmId}/actors")
	@Produces(MediaType.APPLICATION_JSON)
	public List<Actor> getActors(@PathParam("filmId") int filmId)
	{
		Film result = em.createNamedQuery("film.id", Film.class)
				.setParameter("filmId", filmId)
				.getSingleResult();
		if(result==null) 
		{
			return null;
		}
		return result.getActors();
	}
	
}
