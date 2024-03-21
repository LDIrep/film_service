package servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import dto.ActorDTO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import service.ActorService;
import service.impl.ActorServiceImpl;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

@WebServlet("/actor")
public class ActorServlet extends HttpServlet {

    ActorService actorService;

    {
        try {
            actorService = new ActorServiceImpl();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
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
            ActorDTO actor = actorService.getById(id);
            if (actor == null) {
                resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
                return;
            }
            ObjectMapper objectMapper = new ObjectMapper();
            String json = objectMapper.writeValueAsString(actor);

            resp.getWriter().write(json);
        } else {
            List<ActorDTO> actors = actorService.getAll();

            ObjectMapper objectMapper = new ObjectMapper();
            String json = objectMapper.writeValueAsString(actors);
            resp.getWriter().write(json);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        ObjectMapper objectMapper = new ObjectMapper();
        ActorDTO actor = objectMapper.readValue(req.getReader(), ActorDTO.class);

        actorService.save(actor);

        resp.setStatus(HttpServletResponse.SC_CREATED);
        resp.getWriter().write("Actor has been saved successfully");
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

        actorService.delete(id);

        resp.getWriter().write("Actor has been deleted successfully");
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
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
        ActorDTO actor = objectMapper.readValue(req.getReader(), ActorDTO.class);

        actorService.update(id, actor);

        resp.setStatus(HttpServletResponse.SC_OK);
        resp.getWriter().write("Actor has been updated successfully");
    }
}
