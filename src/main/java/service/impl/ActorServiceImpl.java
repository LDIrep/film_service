package service.impl;

import dto.ActorDTO;
import mapper.ActorMapper;
import repository.ActorDAO;
import repository.impl.ActorDAOImpl;
import service.ActorService;
import utils.ConnectionUtil;

import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;

public class ActorServiceImpl implements ActorService {

    ActorDAO actorDAO = new ActorDAOImpl(ConnectionUtil.getConnection());
    ActorMapper actorMapper = new ActorMapper();

    public ActorServiceImpl() throws SQLException {
    }

    @Override
    public ActorDTO getById(int id) {
        return actorMapper.EntityToDTO(actorDAO.getById(id));
    }

    @Override
    public List<ActorDTO> getAll() {
        return actorDAO.getAll()
                .stream()
                .map(actorMapper::EntityToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public void save(ActorDTO actorDTO) {
        actorDAO.save(actorMapper.DTOtoEntity(actorDTO));
    }

    @Override
    public void update(int id, ActorDTO actorDTO) {
        actorDAO.update(id, actorMapper.DTOtoEntity(actorDTO));
    }

    @Override
    public void delete(int id) {
        actorDAO.delete(id);
    }
}
