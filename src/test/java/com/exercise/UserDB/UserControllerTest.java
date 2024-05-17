package com.exercise.UserDB;

import com.exercise.UserDB.aop.TrackInfo;
import com.exercise.UserDB.model.Address;
import com.exercise.UserDB.model.UserMongo;
import com.exercise.UserDB.repository.MUserRepo;
import com.exercise.UserDB.rolesvalidator.ValidatorUser;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.LinkedHashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@Testcontainers
@AutoConfigureMockMvc
public class UserControllerTest {

    @Container
    public static final MongoDBContainer mongoDBContainer = new MongoDBContainer("mongo:5.0")
            .withExposedPorts(27017);
    @Autowired
    private ValidatorUser user;

    @BeforeAll
    public static void beforeAll() {
        mongoDBContainer.start();
    }

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private MUserRepo repo;

    private static UserMongo userTest;

    private static final String GET_USER_ID="/userm/{id}";
    private static final String GET_ALL_USERS="/usersm";
    private static final String ADD_USER="/addUserm";
    private static final String UPDATE_USER="/userm/update/{id}";
    private static final String DELETE_USER="/userm/delete/{id}";
    private static final String CURRENT_USER="/usernamem";
    private static final String GET_STATS="/statsm";
    private static final String RESET_STATS="/statsm/reset";
    public static final String GET_USER_EMAIL="/userm/email/{email}";

    @BeforeAll
    static void setUp() {
        userTest = new UserMongo("da","Alex","alex@email.com","pw",new Address("Turda",4,100000,"Medias"));
    }

    @Test
    void getAllUsers() throws Exception {
        var response = this.mockMvc.perform(get(GET_ALL_USERS)
                        .header("Authorization", "Basic YWRtaW46YWRtaW5ib3Nz")
                .accept(MediaType.APPLICATION_JSON))
                .andReturn()
                .getResponse();
        assertThat(response.getStatus()).isEqualTo(200);
    }

    @Test
    void getAnExistingUserById() throws Exception {

        repo.save(userTest);

        var response = this.mockMvc.perform(get(GET_USER_ID,"da")
                .header("Authorization", "Basic YWRtaW46YWRtaW5ib3Nz")
                .accept(MediaType.APPLICATION_JSON))
                .andReturn()
                .getResponse();
        assertThat(response.getStatus()).isEqualTo(200);

        UserMongo user = new ObjectMapper().readValue(response.getContentAsString(), UserMongo.class);
        assertThat(user.getName()).isEqualTo("Alex");
    }

    @Test
    void getAnUserThatNotExists() throws Exception {

        var response = this.mockMvc.perform(get(GET_USER_ID,"ap")
                .header("Authorization","Basic YWRtaW46YWRtaW5ib3Nz")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andReturn()
                .getResponse();

        assertThat(response.getStatus()).isEqualTo(404);
    }

    @Test
    void getUserByEmail() throws Exception {

        repo.save(userTest);

        var response = this.mockMvc.perform(get(GET_USER_EMAIL,"alex@email.com")
                .header("Authorization","Basic YWRtaW46YWRtaW5ib3Nz")
                .accept(MediaType.APPLICATION_JSON))
                .andReturn()
                .getResponse();

        assertThat(response.getStatus()).isEqualTo(200);
        UserMongo user = new ObjectMapper().readValue(response.getContentAsString(), UserMongo.class);
        assertThat(user.getName()).isEqualTo("Alex");
        assertThat(user.getEmail()).isEqualTo("alex@email.com");
    }

    @Test
    void getUserByEmailThatNotExists() throws Exception {

        var response = this.mockMvc.perform(get(GET_USER_EMAIL,"alex@email.com")
                .header("Authorization","Basic YWRtaW46YWRtaW5ib3Nz")
                .accept(MediaType.APPLICATION_JSON))
                .andReturn()
                .getResponse();

        assertThat(response.getStatus()).isEqualTo(404);
    }

    @Test
    void saveUser() throws Exception {

        var response = this.mockMvc.perform(MockMvcRequestBuilders.post(ADD_USER)
                        .header("Authorization","Basic YWRtaW46YWRtaW5ib3Nz")
                .content(asJsonString(userTest))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse();
        assertThat(response.getStatus()).isEqualTo(201);

        UserMongo userJSON = new ObjectMapper().readValue(response.getContentAsString(), UserMongo.class);
        assertThat(userJSON.getName()).isEqualTo("Alex");
        assertThat(userJSON.getEmail()).isEqualTo("alex@email.com");
        assertThat(userJSON.getPassword()).isEqualTo("pw");
        assertThat(userJSON.getAddress().getCity()).isEqualTo("Medias");
    }

    @Test
    void deleteAnExistingUser() throws Exception {

        repo.save(userTest);

        var response = this.mockMvc.perform(delete(DELETE_USER,"da")
                .header("Authorization","Basic YWRtaW46YWRtaW5ib3Nz")
                .content(asJsonString(userTest))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent())
                .andReturn()
                .getResponse();
        assertThat(response.getStatus()).isEqualTo(204);
    }

    @Test
    void deleteAnUserThatNotExists() throws Exception {

        var response = this.mockMvc.perform(delete(DELETE_USER,"ap")
                .header("Authorization","Basic YWRtaW46YWRtaW5ib3Nz")
                .content(asJsonString(userTest))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andReturn()
                .getResponse();
        assertThat(response.getStatus()).isEqualTo(404);
    }

    @Test
    void updateExistingUser() throws Exception {

        repo.save(userTest);

        var response = this.mockMvc.perform(put(UPDATE_USER,"da")
                .header("Authorization","Basic YWRtaW46YWRtaW5ib3Nz")
                .content(asJsonString(userTest))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse();
        assertThat(response.getStatus()).isEqualTo(200);
    }

    @Test
    void updateNotExistingUser() throws Exception {

        var response = this.mockMvc.perform(put(UPDATE_USER,"da")
                        .header("Authorization","Basic YWRtaW46YWRtaW5ib3Nz")
                        .content(asJsonString(userTest))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andReturn()
                .getResponse();
        assertThat(response.getStatus()).isEqualTo(404);
    }

    @Test
    void trackCounterTest() throws Exception {

        repo.save(userTest);

        var responseGetUserId = this.mockMvc.perform(get(GET_USER_ID,"da")
                .header("Authorization","Basic YWRtaW46YWRtaW5ib3Nz")
                .accept(MediaType.APPLICATION_JSON))
                .andReturn()
                .getResponse();

        assertThat(responseGetUserId.getStatus()).isEqualTo(200);

        var responseGetStats = this.mockMvc.perform(get(GET_STATS)
                .header("Authorization","Basic YWRtaW46YWRtaW5ib3Nz")
                .accept(MediaType.APPLICATION_JSON))
                .andReturn()
                .getResponse();

        assertThat(responseGetStats.getStatus()).isEqualTo(200);
        Map<String, TrackInfo> map = new ObjectMapper().readValue(responseGetStats.getContentAsString(), LinkedHashMap.class);
        assertThat(map.size()).isEqualTo(2);
    }

    @Test
    void resetStatsTest() throws Exception {

        repo.save(userTest);

        var responseGetUserId = this.mockMvc.perform(get(GET_USER_ID,"da")
                        .header("Authorization","Basic YWRtaW46YWRtaW5ib3Nz")
                        .accept(MediaType.APPLICATION_JSON))
                .andReturn()
                .getResponse();

        assertThat(responseGetUserId.getStatus()).isEqualTo(200);

        var responseResetStats = this.mockMvc.perform(put(RESET_STATS)
                .header("Authorization","Basic YWRtaW46YWRtaW5ib3Nz")
                .accept(MediaType.APPLICATION_JSON))
                .andReturn()
                .getResponse();

        assertThat(responseResetStats.getStatus()).isEqualTo(200);
        Map<String, TrackInfo> map = new ObjectMapper().readValue(responseResetStats.getContentAsString(), LinkedHashMap.class);
        assertThat(map).isEmpty();
    }

    @Test
    void getAdminName() throws Exception {

        var response = this.mockMvc.perform(get(CURRENT_USER)
                .header("Authorization","Basic YWRtaW46YWRtaW5ib3Nz"))
                .andReturn()
                .getResponse();
        assertThat(response.getStatus()).isEqualTo(200);
        assertThat(response.getContentAsString()).isEqualTo("Current role: admin");
    }

    private static String asJsonString(final Object obj) {
        try{
            final ObjectMapper mapper = new ObjectMapper();
            return mapper.writeValueAsString(obj);
        }catch (Exception e){
            throw new RuntimeException(e);
        }
    }

    @AfterEach
    void cleanUp() {
        repo.deleteAll();
    }

}
