package ru.yandex.practicum.filmorate.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Service
@Slf4j

public class UserService {
    @Autowired
    @Qualifier("userDao")
    private final UserStorage userStorage;

    public UserService(@Qualifier("userDao")UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public User getUserById (int id) {
        return userStorage.getUserById(id);
    }

    public void addFriend (int userId, int friendId){
        idValidator(userId, friendId);
            for (User user : userStorage.getAll()) {
                if (user.getId() == userId) {
                    for (User friend : userStorage.getAll()){
                        if (friend.getId() == friendId){
                            user.addFriend(friendId);
                            friend.addFriend(userId);
                            log.info("Пользователи добавлены в друзья");
                            break;
                        }
                    }
                }
            }
    }

    public void deleteFriend (int userId, int friendId){
        idValidator(userId, friendId);
            for (User user: userStorage.getAll()){
                if(user.getId() == userId){
                    if (user.getFriends().contains(friendId)){
                        user.getFriends().remove(friendId);
                        log.info("Пользователь удален");
                    } else {
                        log.info("Friend not found");
                        throw new NotFoundException("Пользователя нет в друзьях, удаление невозможно!");
                    }
                }
            }
    }

    public Collection<User> getAllFriends (int id){
        ArrayList<User> temporaryList = new ArrayList<>();
        for (User user : userStorage.getAll()){
            if(user.getId() == id){
                for (Integer friendId : user.getFriends()){
                    for (User friend : userStorage.getAll()){
                        if (friend.getId() == friendId){
                            temporaryList.add(friend);
                        }
                    }
                }
                log.info("Список друзей передан");
                return temporaryList;
            }
        }
            return null;
    }

    public List<User> mutualFriends(int userId, int otherId){
        idValidator(userId,otherId);
        User user1 = null;
        User user2 = null;
        List<Integer> mutualFriendsId = new ArrayList<>();

        for (User user : userStorage.getAll()){
            if (user.getId() == userId){
                user1 = user;
            } else if (user.getId() == otherId){
                user2 = user;
            }
        }

        for (Integer user1friend : user1.getFriends()){
            for (Integer user2friend : user2.getFriends()){
                if (user1friend == user2friend){
                    mutualFriendsId.add(user2friend);
                }
            }
        }

        List<User> mutualFriends = new ArrayList<>();
        for (Integer fId : mutualFriendsId){
            for (User friend : userStorage.getAll()){
                if (friend.getId() == fId){
                    mutualFriends.add(friend);
                }
            }
        }
        return mutualFriends;
    }

    private static void idValidator (int userId, int friendId) {
        if (userId < 0 || friendId < 0){
            throw new NotFoundException("Negative id");
        }


    }
}
