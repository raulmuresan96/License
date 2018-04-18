package service;

import model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import repo.UserRepository;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public Iterable<User> getUsers(){
        return userRepository.findByActive(true);
    }

    public User addUser(User user){
        user.setActive(true);
        return userRepository.save(user);
    }

    public User deleteUser(Integer id){
        User userToDisable = userRepository.findOne(id);
        userToDisable.setActive(false);
        return userRepository.save(userToDisable);
    }

    public User verifyUser(User user){
        return userRepository.findByEmailAndPasswordAndActive(user.getEmail(), user.getPassword(), true);
    }

}
