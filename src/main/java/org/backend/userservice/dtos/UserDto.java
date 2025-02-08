package org.backend.userservice.dtos;

import lombok.Getter;
import lombok.Setter;
import org.backend.userservice.Models.Role;
import org.backend.userservice.Models.User;

import java.util.List;

@Getter
@Setter
public class UserDto {
    private String name;
    private String email;
    private List<Role> roles;

    public static UserDto from(User user){
        if(user == null){
            return null;
        }
        UserDto userDto = new UserDto();
        userDto.setEmail(user.getEmail());
        userDto.setName(user.getName());
        userDto.setRoles(user.getRoles());
        return userDto;
    }


}
