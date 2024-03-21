package repository.impl;

import dto.ActorDTO;
import entity.Actor;
import entity.Film;
import repository.FilmDAO;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FilmDAOImpl implements FilmDAO {

    private Connection connection;

    public FilmDAOImpl(Connection connection) {
        this.connection = connection;
    }

    @Override
    public Film getById(int id) {
        Film film = null;
        String sql = "SELECT * FROM film WHERE id = ?";
        String actorsQuery = """
                SELECT f.id AS film_id, fa.actor_id AS actor_id, a.name AS actor_name FROM film f
                JOIN film_actor fa on f.id = fa.film_id
                JOIN actor a on a.id = fa.actor_id
                WHERE film_id = ?;
                """;

        try (PreparedStatement filmStatement = connection.prepareStatement(sql);
             PreparedStatement actorsStratement = connection.prepareStatement(actorsQuery)) {
            filmStatement.setInt(1, id);
            ResultSet rs = filmStatement.executeQuery();

            if (rs.next()) {
                film = Film.builder()
                        .id(rs.getInt("id"))
                        .title(rs.getString("title"))
                        .director(rs.getInt("director_id"))
                        .build();
            }
            if (film != null) {
                actorsStratement.setInt(1, id);
                ResultSet actorsResultSet = actorsStratement.executeQuery();
                while (actorsResultSet.next()) {
                    Actor actor = Actor.builder()
                            .name(actorsResultSet.getString("actor_name"))
                            .build();
                    film.addActor(actor);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return film;
    }

    @Override
    public List<Film> getAll() {
        List<Film> films = new ArrayList<>();
        String filmQuery = """
                        SELECT f.id AS film_id, f.title AS film_title,
                        d.id AS director_id, d.name AS director_name,
                        a.id AS actor_id, a.name AS actor_name 
                        FROM film f 
                        LEFT JOIN director d ON f.director_id = d.id 
                        LEFT JOIN film_actor fa ON f.id = fa.film_id 
                        LEFT JOIN actor a ON fa.actor_id = a.id
                """;

        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(filmQuery)) {

            Map<Integer, Film> filmMap = new HashMap<>();

            while (resultSet.next()) {
                int filmId = resultSet.getInt("film_id");
                Film film = filmMap.get(filmId);
                if (film == null) {
                    film = Film.builder()
                            .title(resultSet.getString("film_title"))
                            .director(resultSet.getInt("director_id"))
                            .build();
                    filmMap.put(filmId, film);
                    films.add(film);
                }

                int actorId = resultSet.getInt("actor_id");
                if (actorId != 0) {
                    Actor actor = new Actor();
                    actor.setId(actorId);
                    actor.setName(resultSet.getString("actor_name"));
                    film.addActor(actor);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return films;
    }

    @Override
    public void save(Film film) {
        String insertFilmQuery = "INSERT INTO film (title, director_id) VALUES (?, ?)";

        try (PreparedStatement statement = connection.prepareStatement(insertFilmQuery, Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, film.getTitle());
            statement.setInt(2, film.getDirector());
            statement.executeUpdate();

            ResultSet generatedKeys = statement.getGeneratedKeys();
            int filmId = -1;
            if (generatedKeys.next()) {
                filmId = generatedKeys.getInt(1);
            }

            if (filmId != -1) {
                for (Actor actor : film.getActors()) {
                    String insertFilmActorQuery = "INSERT INTO film_actor (film_id, actor_id) VALUES (?, ?)";
                    try (PreparedStatement filmActorStatement = connection.prepareStatement(insertFilmActorQuery)) {
                        filmActorStatement.setInt(1, filmId);
                        filmActorStatement.setInt(2, actor.getId());
                        filmActorStatement.executeUpdate();
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void update(int id, Film film) {
        String query = "UPDATE film SET title = ? WHERE id = ?";

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, film.getTitle());
            statement.setInt(2, id);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void delete(int id) {
        String deleteFilmActorQuery = "DELETE FROM film_actor WHERE film_id = ?";
        try (PreparedStatement statement = connection.prepareStatement(deleteFilmActorQuery)) {
            statement.setInt(1, id);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        String deleteFilmQuery = "DELETE FROM film WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(deleteFilmQuery)) {
            statement.setInt(1, id);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

