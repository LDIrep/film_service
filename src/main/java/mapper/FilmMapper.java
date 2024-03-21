package mapper;

import dto.FilmDTO;
import entity.Film;

public class FilmMapper {

    public Film DTOtoEntity(FilmDTO directorDTO) {
        return utils.ModelMapper.getModelMapper().map(directorDTO, Film.class);
    }

    public FilmDTO EntityToDTO(Film film) {
        return utils.ModelMapper.getModelMapper().map(film, FilmDTO.class);
    }
}
