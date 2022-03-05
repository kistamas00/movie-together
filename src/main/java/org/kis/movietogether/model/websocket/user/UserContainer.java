package org.kis.movietogether.model.websocket.user;

import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketSession;

import java.util.Collections;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Component
public class UserContainer {

    private final User currentUser;
    private final Set<User> users;

    public UserContainer() {
        this.currentUser = new User();
        this.users = new CopyOnWriteArraySet<>();
    }

    public void updateCurrentUserName(final String userName) {
        this.currentUser.setUserName(userName);
    }

    public String getCurrentUserName() {
        return this.currentUser.getUserName();
    }

    public void addUser(final User user) {
        users.add(user);
    }

    public void addUsers(final Set<User> user) {
        users.addAll(user);
    }

    public Set<User> getUsers() {
        return Collections.unmodifiableSet(users);
    }

    public void removeUser(final User user) {
        users.remove(user);
    }

    public Optional<User> getUserBy(final WebSocketSession userSession) {
        return findAny(user -> Objects.equals(user.getSession(), userSession));
    }

    public Set<User> getUsersWithSession() {
        return findAll(user -> Objects.nonNull(user.getSession()));
    }

    public void clear() {
        users.clear();
    }

    private Optional<User> findAny(final Predicate<User> predicate) {
        return users.stream().filter(predicate).findAny();
    }

    private Set<User> findAll(final Predicate<User> predicate) {
        return users.stream().filter(predicate).collect(Collectors.toSet());
    }
}
