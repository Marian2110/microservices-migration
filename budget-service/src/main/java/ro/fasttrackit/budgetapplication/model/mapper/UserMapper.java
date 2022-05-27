package ro.fasttrackit.budgetapplication.model.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import ro.fasttrackit.budgetapplication.model.dto.UserDTO;
import ro.fasttrackit.budgetapplication.model.entity.Role;
import ro.fasttrackit.budgetapplication.model.entity.User;

import java.util.List;
import java.util.Set;

@Mapper(uses = {RoleMapper.class})
public interface UserMapper {
    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);
    @Mapping(target= "roles", ignore = true)
    UserDTO mapToDTO(User user);

    List<UserDTO> mapToDTOs(List<User> users);

    User mapToEntity(UserDTO userDTO);

    List<User> mapToEntities(List<UserDTO> userDTOs);
}
