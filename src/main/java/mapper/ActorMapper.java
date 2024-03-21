package mapper;

import dto.ActorDTO;
import entity.Actor;

public class ActorMapper {

    public Actor DTOtoEntity(ActorDTO actorDTO) {
        return utils.ModelMapper.getModelMapper().map(actorDTO, Actor.class);
    }

    public ActorDTO EntityToDTO(Actor actor) {
        return utils.ModelMapper.getModelMapper().map(actor, ActorDTO.class);
    }
}
