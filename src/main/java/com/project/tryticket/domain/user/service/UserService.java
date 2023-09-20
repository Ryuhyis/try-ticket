package com.project.tryticket.domain.user.service;

import com.project.tryticket.domain.user.entity.User;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {
   public List<User> getAllUsers() {
      return null;
   }

   public User getUserById(Long id) {
      return null;
   }

   public User createUser(User user) {
      return user;
   }

   public User updateUser(Long id, User user) {
      return user;
   }

   public void deleteUser(Long id) {
   }
}
