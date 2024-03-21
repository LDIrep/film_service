package repository;

import entity.Actor;

import java.util.List;

public interface ActorDAO {
    Actor getById(int id);
    List<Actor> getAll();
    void save(Actor actor);
    void update(int id, Actor Actor);
    void delete(int id);
}
