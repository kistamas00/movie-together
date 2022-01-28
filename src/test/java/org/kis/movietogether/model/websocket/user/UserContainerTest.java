package org.kis.movietogether.model.websocket.user;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.adapter.standard.StandardWebSocketSession;

import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class UserContainerTest {

    private UserContainer userContainer;

    @BeforeEach
    void setUp() {
        this.userContainer = new UserContainer();
    }

    @Test
    void testCurrentUserName() {
        // GIVEN

        // WHEN
        userContainer.updateCurrentUserName("newName");

        // THEN
        assertThat(userContainer.getCurrentUserName())
                .isEqualTo("newName");

    }


    @Test
    void testAddUser() {
        // GIVEN
        final User user = new User();

        // WHEN
        userContainer.addUser(user);

        // THEN
        assertThat(userContainer.getUsers())
                .containsExactly(user);
    }

    @Test
    void testAddUsers() {
        // GIVEN
        final Set<User> users = Set.of(new User("User1"), new User("User2"));

        // WHEN
        userContainer.addUsers(users);

        // THEN
        assertThat(userContainer.getUsers())
                .isEqualTo(users);
    }

    @Test
    void testRemoveUser() {
        // GIVEN
        final User user1 = new User("User1");
        final User user2 = new User("User2");
        userContainer.addUser(user1);
        userContainer.addUser(user2);

        // WHEN
        userContainer.removeUser(user1);

        // THEN
        assertThat(userContainer.getUsers())
                .containsExactly(user2);
    }

    @Test
    void testGetUserBySession() {
        // GIVEN
        final WebSocketSession session =
                new StandardWebSocketSession(null, null, null, null);
        final User user1 = new User(session);
        final User user2 = new User();
        userContainer.addUser(user1);
        userContainer.addUser(user2);

        // WHEN
        final Optional<User> optionalUser = userContainer.getUserBy(session);

        // THEN
        assertThat(optionalUser)
                .isPresent()
                .get()
                .isEqualTo(user1);
    }

    @Test
    void testGetUsersWithSession() {
        // GIVEN
        final WebSocketSession session =
                new StandardWebSocketSession(null, null, null, null);
        final User user1 = new User(session);
        final User user2 = new User();
        userContainer.addUser(user1);
        userContainer.addUser(user2);

        // WHEN
        final Set<User> usersWithSession = userContainer.getUsersWithSession();

        // THEN
        assertThat(usersWithSession)
                .containsExactly(user1);
    }

    @Test
    void testClear() {
        // GIVEN
        final User user1 = new User("User1");
        final User user2 = new User("User2");
        userContainer.addUser(user1);
        userContainer.addUser(user2);

        // WHEN
        userContainer.clear();

        // THEN
        assertThat(userContainer.getUsers())
                .isEmpty();
    }
}