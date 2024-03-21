package repository;

import dto.ActorDTO;
import dto.FilmDTO;
import entity.Actor;
import entity.Film;

import java.util.List;

public interface FilmDAO {
    Film getById(int id);
    List<Film> getAll();
    void save(Film film);
    void update(int id, Film filmDTO);
    void delete(int id);
}
