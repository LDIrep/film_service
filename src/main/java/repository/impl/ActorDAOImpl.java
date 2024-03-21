package repository.impl;

import entity.Actor;
import entity.Film;
import repository.ActorDAO;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ActorDAOImpl implements ActorDAO {

    private Connection connection;

    public ActorDAOImpl(Connection connection) {
        this.connection = connection;
    }

    @Override
    public Actor getById(int id) {
        Actor actor = null;
        String actorQuery = "SELECT * FROM actor WHERE id = ?";
        String filmsQuery = "SELECT f.id AS film_id, f.title AS film_title, d.id AS director_id, d.name AS director_name " +
                "FROM film f " +
                "JOIN film_actor fa ON f.id = fa.film_id " +
                "LEFT JOIN director d ON f.director_id = d.id " +
                "WHERE fa.actor_id = ?";

        try (PreparedStatement actorStatement = connection.prepareStatement(actorQuery);
             PreparedStatement filmsStatement = connection.prepareStatement(filmsQuery)) {

            // Получаем актера по его идентификатору
            actorStatement.setInt(1, id);
            ResultSet actorResultSet = actorStatement.executeQuery();
            if (actorResultSet.next()) {
                actor = Actor.builder()
                        .id(actorResultSet.getInt("id"))
                        .name(actorResultSet.getString("name"))
                        .build();
            }

            if (actor != null) {
                filmsStatement.setInt(1, id);
                ResultSet filmsResultSet = filmsStatement.executeQuery();
                List<Film> films = new ArrayList<>();
                while (filmsResultSet.next()) {
                    Film film = new Film();
                    film.setId(filmsResultSet.getInt("film_id"));
                    film.setTitle(filmsResultSet.getString("film_title"));

                    films.add(film);
                }
                actor.setFilms(films);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return actor;
    }

    @Override
    public List<Actor> getAll() {
        List<Actor> actors = new ArrayList<>();
        String query = "SELECT * FROM actor";

        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {

            while (resultSet.next()) {
                Actor actor = new Actor();
                actor.setId(resultSet.getInt("id"));
                actor.setName(resultSet.getString("name"));
                actors.add(actor);
            }
        } catch (SQLException e) {
            e.printStackTrace();

        }
        return actors;
    }

    @Override
    public void save(Actor actor) {
        String query = "INSERT INTO actor (name) VALUES (?)";

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, actor.getName());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void update(int id, Actor actor) {
        String query = "UPDATE actor SET name = ? WHERE id = ?";

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, actor.getName());
            statement.setInt(2, id);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void delete(int id) {

        String deleteFilmActorQuery = "DELETE FROM film_actor WHERE actor_id = ?";
        try (PreparedStatement statement = connection.prepareStatement(deleteFilmActorQuery)) {
            statement.setInt(1, id);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        String query = "DELETE FROM actor WHERE id = ?";

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, id);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
