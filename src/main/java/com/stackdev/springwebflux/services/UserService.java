package com.stackdev.springwebflux.services;

import com.stackdev.springwebflux.models.Users;
import com.stackdev.springwebflux.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.Objects;

@Service
public class UserService {

    @Autowired
    UserRepository userRepository;

    public Flux<Users>  getAllUsers(){
        return userRepository.findAll()
                .delayElements(
                        Duration.ofSeconds(2)
                );
    }

    public Mono<Users> getUserById(Long id){
        return userRepository.findById(id);
    }

    public void addUser(Users users){
        //Subscribe - We are subscribing to  the Stream of data, that is returned on save
        //subscribe() returns the Disposable that helps cancel the subscription
        //this is just a comment to the view the data
         userRepository.save(users).subscribe();
    }

    public Mono<Users> updateUser(Users user){
        return userRepository.findById(user.getId())
                .switchIfEmpty(Mono.error(new Exception("NOT_FOUND")))
                .map(oldUser ->{
                    if(user.getEmail() != null) oldUser.setEmail(user.getEmail());
                    if(user.getUsername()!=null) oldUser.setUsername(user.getUsername());
                    if(user.getName()!=null) oldUser.setName(user.getName());
                    if(user.getSurname()!=null) oldUser.setSurname(user.getSurname());
                    return oldUser;
                })
                .flatMap(userRepository::save);
    }

    public Mono<Void> deleteUser(Long id){
        return userRepository.deleteById(id).switchIfEmpty(Mono.error(new Exception("NOT FOUND")));
    }

}
