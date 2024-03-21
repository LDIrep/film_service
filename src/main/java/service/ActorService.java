package service;

import dto.ActorDTO;


import java.util.List;

public interface ActorService {

    ActorDTO getById(int id);
    List<ActorDTO> getAll();
    void save(ActorDTO actorDTO);
    void update(int id, ActorDTO ActorDTO);
    void delete(int id);
}
