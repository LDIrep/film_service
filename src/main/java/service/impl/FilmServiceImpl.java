package service.impl;

import dto.FilmDTO;
import entity.Film;
import mapper.FilmMapper;
import repository.FilmDAO;
import repository.impl.FilmDAOImpl;
import service.FilmService;
import utils.ConnectionUtil;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;

public class FilmServiceImpl implements FilmService {

    FilmMapper filmMapper = new FilmMapper();
    FilmDAO filmDAO = new FilmDAOImpl(ConnectionUtil.getConnection());

    public FilmServiceImpl(Connection connection) throws SQLException {
    }

    @Override
    public FilmDTO getById(int id) {
        Film film = filmDAO.getById(id);
        if (film == null) {
            return null;
        }
        return filmMapper.EntityToDTO(film);
    }

    @Override
    public List<FilmDTO> getAll() {
        return filmDAO.getAll()
                .stream()
                .map(filmMapper::EntityToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public void save(FilmDTO filmDTO) {
        filmDAO.save(filmMapper.DTOtoEntity(filmDTO));
    }

    @Override
    public void update(int id, FilmDTO filmDTO) {
        filmDAO.update(id, filmMapper.DTOtoEntity(filmDTO));
    }

    @Override
    public void delete(int id) {
        filmDAO.delete(id);
    }

    public void setFilmDAO(FilmDAO filmDAO) {
        this.filmDAO = filmDAO;
    }
}
