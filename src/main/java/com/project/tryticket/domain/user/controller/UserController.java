package com.project.tryticket.domain.user.controller;

import com.project.tryticket.domain.user.entity.User;
import com.project.tryticket.domain.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("user")
@RequiredArgsConstructor
public class UserController {
   private final UserService userService;

   @GetMapping
   public List<User> getAllUsers() {
      return userService.getAllUsers();
   }

   @GetMapping("/{id}")
   public User getUserById(@PathVariable Long id) {
      return userService.getUserById(id);
   }

   @PostMapping
   public User createUser(@RequestBody User user) {
      return userService.createUser(user);
   }

   @PutMapping("/{id}")
   public User updateUser(@PathVariable Long id, @RequestBody User user) {
      return userService.updateUser(id, user);
   }

   @DeleteMapping("/{id}")
   public void deleteUser(@PathVariable Long id) {
      userService.deleteUser(id);
   }
}
