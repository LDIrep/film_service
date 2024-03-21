package service.impl;

import dto.FilmDTO;
import entity.Film;
import mapper.FilmMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import repository.FilmDAO;
import repository.impl.FilmDAOImpl;
import utils.ConnectionUtil;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class FilmServiceImplTest {

    @Mock
    private FilmDAO filmDAO;
    @Mock
    private Connection connection;
    @InjectMocks
    private FilmServiceImpl filmService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetById() throws SQLException {
        FilmDTO filmDTO = new FilmDTO();
        filmDTO.setTitle("The Godfather");

        Film film = new Film();
        film.setId(1);
        film.setTitle("The Godfather");

        FilmDAO filmDAO = mock(FilmDAO.class);
        when(filmDAO.getById(1)).thenReturn(film);

        FilmServiceImpl filmService = new FilmServiceImpl(connection);

        filmService.setFilmDAO(filmDAO);

        FilmDTO result = filmService.getById(1);

        assertNotNull(result);
        assertEquals("The Godfather", result.getTitle());
    }


    @Test
    public void testGetAll() {
        FilmDTO filmDTO1 = new FilmDTO();
        filmDTO1.setTitle("The Godfather");

        FilmDTO filmDTO2 = new FilmDTO();
        filmDTO2.setTitle("Inception");

        Film film1 = new Film();
        film1.setId(1);
        film1.setTitle("The Godfather");

        Film film2 = new Film();
        film2.setId(2);
        film2.setTitle("Inception");

        List<Film> films = new ArrayList<>();
        films.add(film1);
        films.add(film2);

        FilmDAO filmDAO = mock(FilmDAO.class);
        when(filmDAO.getAll()).thenReturn(films);

        FilmServiceImpl filmService = null;
        try {
            filmService = new FilmServiceImpl(connection);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        filmService.setFilmDAO(filmDAO);

        List<FilmDTO> result = filmService.getAll();

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("The Godfather", result.get(0).getTitle());
        assertEquals("Inception", result.get(1).getTitle());
    }

    @Test
    public void testSave() {
        FilmDTO filmDTO = new FilmDTO();
        filmDTO.setTitle("The Godfather");

        Film film = new Film();
        film.setId(1);
        film.setTitle("The Godfather");

        FilmDAO filmDAO = mock(FilmDAO.class);

        FilmServiceImpl filmService = null;
        try {
            filmService = new FilmServiceImpl(connection);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        filmService.setFilmDAO(filmDAO);

        filmService.save(filmDTO);

        verify(filmDAO, times(1)).save(any(Film.class));
    }

    @Test
    public void testUpdate() {
        FilmDTO filmDTO = new FilmDTO();
        filmDTO.setTitle("The Godfather");

        Film film = new Film();
        film.setId(1);
        film.setTitle("The Godfather");

        FilmDAO filmDAO = mock(FilmDAO.class);

        FilmServiceImpl filmService = null;
        try {
            filmService = new FilmServiceImpl(connection);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        filmService.setFilmDAO(filmDAO);

        filmService.update(1, filmDTO);

        verify(filmDAO, times(1)).update(eq(1), any(Film.class));
    }

    @Test
    public void testDelete() {
        FilmDAO filmDAO = mock(FilmDAO.class);

        FilmServiceImpl filmService = null;
        try {
            filmService = new FilmServiceImpl(connection);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        filmService.setFilmDAO(filmDAO);

        filmService.delete(1);

        verify(filmDAO, times(1)).delete(1);
    }
}