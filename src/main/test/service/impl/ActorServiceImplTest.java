package service.impl;

import dto.ActorDTO;
import entity.Actor;
import mapper.ActorMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import repository.ActorDAO;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ActorServiceImplTest {

    @Mock
    private ActorDAO actorDAO;
    @Mock
    private ActorMapper actorMapper;
    @InjectMocks
    private ActorServiceImpl actorService;
    private ActorDTO actorDTO;
    private Actor actor;

    @BeforeEach
    public void setUp() {
        actorDTO = ActorDTO.builder()
                .id(1)
                .name("John Doe")
                .build();
        actor = Actor.builder()
                .id(1)
                .name("John Doe")
                .build();
    }

    @Test
    public void testGetById() {
        when(actorDAO.getById(1)).thenReturn(actor);
        when(actorMapper.EntityToDTO(actor)).thenReturn(actorDTO);

        ActorDTO result = actorService.getById(1);

        assertEquals(actorDTO, result);
    }

    @Test
    public void testGetAll() {
        List<Actor> actors = new ArrayList<>();
        actors.add(actor);

        List<ActorDTO> expectedActorsDTO = new ArrayList<>();
        expectedActorsDTO.add(actorDTO);

        when(actorDAO.getAll()).thenReturn(actors);
        when(actorMapper.EntityToDTO(actor)).thenReturn(actorDTO);

        List<ActorDTO> result = actorService.getAll();

        assertEquals(expectedActorsDTO, result);
    }

    @Test
    public void testSave() {
        actorService.save(actorDTO);

        verify(actorDAO).save(actorMapper.DTOtoEntity(actorDTO));
    }

    @Test
    public void testUpdate() {
        actorService.update(1, actorDTO);

        verify(actorDAO).update(1, actorMapper.DTOtoEntity(actorDTO));
    }

    @Test
    public void testDelete() {
        actorService.delete(1);

        verify(actorDAO).delete(1);
    }
}