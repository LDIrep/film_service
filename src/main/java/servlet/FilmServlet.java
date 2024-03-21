package servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import dto.ActorDTO;
import dto.FilmDTO;
import entity.Film;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import service.FilmService;
import service.impl.FilmServiceImpl;
import utils.ConnectionUtil;


import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

@WebServlet("/film")
public class FilmServlet extends HttpServlet {

    private FilmService filmService;

    public FilmServlet() throws SQLException {
        filmService = new FilmServiceImpl(ConnectionUtil.getConnection());
    }


    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");

        String idParameter = req.getParameter("id");

        if (idParameter != null && !idParameter.isEmpty()) {
            int id;
            try {
                id = Integer.parseInt(idParameter);
            } catch (NumberFormatException e) {
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                return;
            }
            FilmDTO film = filmService.getById(id);
            if (film == null) {
                resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
                return;
            }
            ObjectMapper objectMapper = new ObjectMapper();
            String json = objectMapper.writeValueAsString(film);

            resp.getWriter().write(json);
        } else {
            List<FilmDTO> films = filmService.getAll();

            ObjectMapper objectMapper = new ObjectMapper();
            String json = objectMapper.writeValueAsString(films);

            resp.getWriter().write(json);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("application/json");
        ObjectMapper objectMapper = new ObjectMapper();
        FilmDTO filmDTO = objectMapper.readValue(req.getReader(), FilmDTO.class);

        filmService.save(filmDTO);

        resp.setStatus(HttpServletResponse.SC_CREATED);
        resp.getWriter().write("Film has been saved successfully");
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("application/json");
        String idParameter = req.getParameter("id");
        if (idParameter == null || idParameter.isEmpty()) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        int id;
        try {
            id = Integer.parseInt(idParameter);
        } catch (NumberFormatException e) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        ObjectMapper objectMapper = new ObjectMapper();
        FilmDTO film = objectMapper.readValue(req.getReader(), FilmDTO.class);

        filmService.update(id, film);

        resp.setStatus(HttpServletResponse.SC_OK);
        resp.getWriter().write("Film has been updated successfully");
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        String idParameter = req.getParameter("id");
        if (idParameter == null || idParameter.isEmpty()) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        int id;
        try {
            id = Integer.parseInt(idParameter);
        } catch (NumberFormatException e) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        filmService.delete(id);

        resp.getWriter().write("Film has been deleted successfully");
    }
}
