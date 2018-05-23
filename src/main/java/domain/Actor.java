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
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

@XmlRootElement
@Entity
@NamedQueries
({
	@NamedQuery(name="actor.all", query="SELECT a FROM Actor a"),
	@NamedQuery(name="actor.id", query="FROM Actor a WHERE a.id=:actorId")
})
public class Actor
{
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	private String name;
	private String surname;
	private List<Film> films = new ArrayList<Film>();
	
	public int getId()
	{
		return id;
	}
	
	public void setId(int id)
	{
		this.id = id;
	}
	
	public String getName()
	{
		return name;
	}
	
	public void setName(String name)
	{
		this.name = name;
	}
	
	public String getSurname()
	{
		return surname;
	}
	
	public void setSurname(String surname)
	{
		this.surname = surname;
	}
	
	@XmlTransient
	@ManyToMany(targetEntity=Film.class, mappedBy="actors")
	public List<Film> getFilms()
	{
		return films;
	}
	
	public void setFilms(List<Film> films)
	{
		this.films = films;
	}  
	
}
