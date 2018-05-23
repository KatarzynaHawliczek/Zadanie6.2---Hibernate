package domain;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

@XmlRootElement
@Entity
@NamedQueries
({
	@NamedQuery(name="film.all", query="SELECT f FROM Film f"),
	@NamedQuery(name="film.id", query="FROM Film f WHERE f.id=:filmId")
})
public class Film
{
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	private String title;
	private int rateSum = 0;
	private int rateCount = 0;
	private List<Comment> comments = new ArrayList<Comment>();
	private List<Actor> actors = new ArrayList<Actor>();
	
	public int getId()
	{
		return id;
	}
	
	public void setId(int id)
	{
		this.id = id;
	}
	
	public String getTitle()
	{
		return title;
	}
	
	public void setTitle(String title)
	{
		this.title = title;
	}
	
	public int getRateSum()
	{
		return rateSum;
	}

	public void setRateSum(int rateSum)
	{
		this.rateSum = rateSum;
	}

	public int getRateCount()
	{
		return rateCount;
	}

	public void setRateCount(int rateCount)
	{
		this.rateCount = rateCount;
	}
	
	public int getRate()
	{
		if(this.rateCount>0)
		{
			return Math.round(this.rateSum/this.rateCount);
		}
		else
		{
			return 0;
		}
	}
	
	@XmlTransient
	@OneToMany(mappedBy="film")
	public List<Comment> getComments()
	{
		return comments;
	}
	
	public void setComments(List<Comment> comments)
	{
		this.comments = comments;
	}
	
	@XmlTransient
	@ManyToMany(targetEntity=Actor.class, mappedBy = "films")
	public List<Actor> getActors()
	{
		return actors;
	}

	public void setActors(List<Actor> actors)
	{
		this.actors = actors;
		
	}

}
