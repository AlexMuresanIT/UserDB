package com.exercise.UserDB;

import com.exercise.UserDB.exception.InvalidData;
import com.exercise.UserDB.exception.NoUserFoundException;
import com.exercise.UserDB.model.Address;
import com.exercise.UserDB.model.UserMongo;
import com.exercise.UserDB.repository.MUserRepo;
import com.exercise.UserDB.service.UserMongoService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private MUserRepo repo;

    @InjectMocks
    private UserMongoService userMongoService;

    private static UserMongo user;

    @BeforeAll
    static void setUp() {
        user = new UserMongo("dadada","Alexa","alexa@email.com","dada",new Address("Turda",4,100000,"Medias"));
    }

    @Test
    void createUserAndSavesItAndThenReturnTrue() {
        Mockito.when(repo.save(user)).thenReturn(user);
        assertThat(userMongoService.save(user)).isEqualTo(true);
    }

    @Test
    void createUserWithInvalidData() {
        UserMongo userTest = new UserMongo("dadada","Alexa","alexa@email.com","dada",new Address("Turda",4,10,"Medias"));
        assertThrows(InvalidData.class, () -> userMongoService.save(userTest));
    }

    @Test
    void createUsersAndThenGetTheUserById() {
        Mockito.when(repo.findById("dadada")).thenReturn(Optional.of(user));
        assertThat(userMongoService.findById("dadada")).isEqualTo(user);
    }

    @Test
    void createUsersAndThenVerifyThatWereSaved() {
        UserMongo user1 = new UserMongo("dada","Mihai","mihai@email.com","pw2",new Address("Papa",4,100000,"Papa"));
        UserMongo user2 = new UserMongo("dadada1","Dragos","dragos@email.com","pw3",new Address("Liberate",4,100000,"Sighet"));

        Mockito.when(repo.save(user1)).thenReturn(user1);
        Mockito.when(repo.save(user2)).thenReturn(user2);
        Mockito.when(repo.save(user)).thenReturn(user);

        assertTrue(userMongoService.save(user));
        assertTrue(userMongoService.save(user1));
        assertTrue(userMongoService.save(user2));

        Mockito.verify(repo, Mockito.times(1)).save(user);
        Mockito.verify(repo, Mockito.times(1)).save(user1);
        Mockito.verify(repo, Mockito.times(1)).save(user2);
    }

    @Test
    void testGetAllUsers() {
        List<UserMongo> users = new ArrayList<>();
        users.add(user);
        Mockito.when(repo.findAll()).thenReturn(users);
        assertThat(userMongoService.findAll()).isEqualTo(users);
    }

    @Test
    void getUserByIdAndThenThrowNoUserFoundException() {
        Mockito.when(repo.findById("da")).thenReturn(Optional.empty());
        assertThrows(NoUserFoundException.class, ()->userMongoService.findById("da"));
    }

    @Test
    void getUserByIdAndThenVerifyTheName() {
        Mockito.when(repo.findById("dadada")).thenReturn(Optional.of(user));
        assertThat(userMongoService.findById("dadada").getName()).isEqualTo("Alexa");
    }

    @Test
    void deleteUserByIdAndThenVerifyThatHasBeenDeleted() {
        Mockito.when(repo.findById("dadada")).thenReturn(Optional.of(user));
        assertTrue(userMongoService.deleteById("dadada"));
    }

    @Test
    void tryToDeleteUserByIdThatDoesNotExist() {
        Mockito.when(repo.findById("1")).thenReturn(Optional.empty());
        assertThrows(NoUserFoundException.class, ()->userMongoService.deleteById("1"));
    }

    @Test
    void findUserByIdAndThenDeleteTheUser() {
        Mockito.when(repo.findById("dadada")).thenReturn(Optional.of(user));
        assertThat(userMongoService.findById("dadada")).isEqualTo(user);
        assertThat(userMongoService.deleteById("dadada")).isTrue();
    }

    @Test
    void findUserByIdAndThenUpdateTheUser() {
        Mockito.when(repo.findById("dadada")).thenReturn(Optional.of(user));
        assertTrue(userMongoService.update("dadada",user));
    }

    @Test
    void getUnexistedUserByIdAndThenThrowNoUserFoundException() {
        Mockito.when(repo.findById("dad")).thenReturn(Optional.empty());
        assertThrows(NoUserFoundException.class, ()->userMongoService.update("dad",user));
    }

    @Test
    void getUserByEmailAndThenVerifyThatHasBeenFound() {
        Mockito.when(repo.findByEmail("dadada")).thenReturn(Optional.of(user));
        assertThat(userMongoService.findByEmail("dadada")).isEqualTo(user);
    }

    @Test
    void getAnUserByEmailThatDoesNotExist() {
        Mockito.when(repo.findByEmail("da")).thenReturn(Optional.empty());
        assertThrows(NoUserFoundException.class, ()->userMongoService.findByEmail("da"));
    }

}
