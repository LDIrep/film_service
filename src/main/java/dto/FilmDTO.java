package dto;

import lombok.*;

import java.util.List;
import java.util.Objects;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class FilmDTO {

    private String title;
    private int director;
    private List<ActorDTO> actors;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FilmDTO filmDTO = (FilmDTO) o;
        return director == filmDTO.director && Objects.equals(title, filmDTO.title) && Objects.equals(actors, filmDTO.actors);
    }

    @Override
    public int hashCode() {
        return Objects.hash(title, director, actors);
    }
}
