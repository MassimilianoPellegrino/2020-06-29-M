package it.polito.tdp.imdb.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import it.polito.tdp.imdb.model.Actor;
import it.polito.tdp.imdb.model.Director;
import it.polito.tdp.imdb.model.Movie;

public class ImdbDAO {
	
	public List<Actor> listAllActors(){
		String sql = "SELECT * FROM actors";
		List<Actor> result = new ArrayList<Actor>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();
			while (res.next()) {

				Actor actor = new Actor(res.getInt("id"), res.getString("first_name"), res.getString("last_name"),
						res.getString("gender"));
				
				result.add(actor);
			}
			conn.close();
			return result;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public List<Movie> listAllMovies(){
		String sql = "SELECT * FROM movies";
		List<Movie> result = new ArrayList<Movie>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();
			while (res.next()) {

				Movie movie = new Movie(res.getInt("id"), res.getString("name"), 
						res.getInt("year"), res.getDouble("rank"));
				
				result.add(movie);
			}
			conn.close();
			return result;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	
	public List<Director> listAllDirectors(){
		String sql = "SELECT * FROM directors";
		List<Director> result = new ArrayList<Director>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();
			while (res.next()) {

				Director director = new Director(res.getInt("id"), res.getString("first_name"), res.getString("last_name"));
				
				result.add(director);
			}
			conn.close();
			return result;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public List<Director> getYearDirectors(Map<Integer, Director> idMap, int anno){
		String sql = "SELECT distinct d.* "
				+ "FROM directors d, movies_directors md, movies m "
				+ "WHERE d.id = md.director_id AND md.movie_id = m.id AND m.year = ?";
		
		List<Director> result = new ArrayList<Director>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			st.setInt(1, anno);
			ResultSet res = st.executeQuery();
			while (res.next()) {

				Director director = new Director(res.getInt("id"), res.getString("first_name"), res.getString("last_name"));
				
				if(!idMap.containsKey(res.getInt("id")))
					idMap.put(res.getInt("id"), director);
				
				result.add(director);
			}
			conn.close();
			return result;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	
	public List<Arco> getEdges(int anno){
		
		List<Arco> result = new ArrayList<>();
		
		String sql1 = "SELECT md1.director_id AS d1, md2.director_id AS d2, COUNT(DISTINCT r.actor_id) AS w "
			+ "FROM movies_directors md1, movies_directors md2, roles r, movies m "
			+ "WHERE md1.director_id > md2.director_id AND md1.movie_id = m.id AND m.year = ? "
			+ "AND r.movie_id = md1.movie_id AND md1.movie_id = md2.movie_id "
			+ "GROUP BY md1.director_id, md2.director_id";
	
		String sql2 = "SELECT md1.director_id AS d1, md2.director_id AS d2, COUNT(DISTINCT r1.actor_id) AS w "
			+ "FROM movies_directors md1, movies_directors md2, roles r1, roles r2, movies m1, movies m2 "
			+ "WHERE md1.director_id > md2.director_id "
			+ "AND md1.movie_id = r1.movie_id AND md2.movie_id = r2.movie_id AND r1.actor_id = r2.actor_id "
			+ "AND m1.id = md1.movie_id AND m1.year = ? AND m2.id = md2.movie_id AND m2.year = ? AND r1.movie_id != r2.movie_id "
			+ "GROUP BY md1.director_id, md2.director_id";
		
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st1 = conn.prepareStatement(sql1);
			st1.setInt(1, anno);
			ResultSet res1 = st1.executeQuery();
			while (res1.next()) {

				Arco arco = new Arco(res1.getInt("d1"), res1.getInt("d2"), res1.getInt("w"));
				
				result.add(arco);
			}
			
			
			PreparedStatement st2 = conn.prepareStatement(sql2);
			st2.setInt(1, anno);
			st2.setInt(2, anno);
			ResultSet res2 = st2.executeQuery();
			while (res2.next()) {

				Arco arco = new Arco(res2.getInt("d1"), res2.getInt("d2"), res2.getInt("w"));
				
				result.add(arco);
			}
		
			conn.close();
			
			Map<Arco, Integer> map = new HashMap<>();
			
			for(Arco a: result) {
				if(map.containsKey(a))
					map.put(a, map.get(a)+a.getW());
				else
					map.put(a, a.getW());
			}
			
			result.clear();
			
			for(Arco a: map.keySet()) {
				a.setW(map.get(a));
				result.add(a);
			}
			
			/*for(Arco a: result)
				System.out.println(a+"\n");*/
			
			return result;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	
	
	
	
	
}
