package dto;

import entity.Film;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Objects;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class ActorDTO {
    private int id;
    private String name;
    private List<FilmDTO> films;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ActorDTO actorDTO = (ActorDTO) o;
        return id == actorDTO.id && Objects.equals(name, actorDTO.name) && Objects.equals(films, actorDTO.films);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, films);
    }
}
