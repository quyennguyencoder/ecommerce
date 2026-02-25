package com.nguyenquyen.ecommerce.service.intf;

import com.nguyenquyen.ecommerce.dtos.UserDTO;
import com.nguyenquyen.ecommerce.exception.DataNotFoundException;
import com.nguyenquyen.ecommerce.models.User;

public interface IUserService {
    User createUser(UserDTO userDTO) throws DataNotFoundException;
    String login(String phoneNumber, String password);
}
