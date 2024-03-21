package service;

import dto.FilmDTO;

import java.util.List;

public interface FilmService {

    FilmDTO getById(int id);
    List<FilmDTO> getAll();
    void save(FilmDTO filmDTO);
    void update(int id, FilmDTO filmDTO);
    void delete(int id);
}
