package com.escalon.springfox.datarest;

import com.escalon.springfox.datarest.jpa.Client;
import com.escalon.springfox.datarest.jpa.ClientTestHelper;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.hamcrest.core.StringContains;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.core.AutoConfigureCache;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureDataJpa;
import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureTestEntityManager;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureWebMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * @author amutsch
 * @since 12/18/2018
 */
@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = ClientApplication.class)
//We are going all the way to db for full integration
@Transactional
@AutoConfigureCache
@AutoConfigureDataJpa
@AutoConfigureTestDatabase
@AutoConfigureTestEntityManager
@AutoConfigureWebMvc
@AutoConfigureMockMvc
public class ClientServiceITEST {

    private MockMvc mockMvc;
    private ObjectMapper mapper = new ObjectMapper();

    @Autowired
    private WebApplicationContext webAppContext;

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webAppContext).build();
        mapper.configure(JsonParser.Feature.AUTO_CLOSE_SOURCE, true);
        mapper.registerModule(new JavaTimeModule());
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    @Test
    @Rollback(false)
    void addValidClient(TestInfo testInfo) throws Exception {
        String jsonRequest = mapper.writeValueAsString(ClientTestHelper.createValidClient(testInfo));
        mockMvc.perform(MockMvcRequestBuilders
            .post("/clients")
            .content(jsonRequest)
            .contentType(MediaType.APPLICATION_JSON)
        )
            .andDo(MockMvcResultHandlers.print())
            .andExpect(status().is2xxSuccessful())
            .andExpect(header().string("Location", new StringContains("http://localhost/clients/")));
    }

    @Test
    @Rollback(false)
    void addInactiveClient(TestInfo testInfo) throws Exception {
        Client client = ClientTestHelper.createValidClient(testInfo);
        client.setActive(false);
        String jsonRequest = mapper.writeValueAsString(client);
        mockMvc.perform(MockMvcRequestBuilders
            .post("/clients")
            .content(jsonRequest)
            .contentType(MediaType.APPLICATION_JSON)
        )
            .andDo(MockMvcResultHandlers.print())
            .andExpect(status().is2xxSuccessful())
            .andExpect(header().string("Location", new StringContains("http://localhost/clients/")));

    }

    @Nested
    class ClientPostCreateOperations {

        @Test
        void findAll() throws Exception {
            mockMvc.perform(MockMvcRequestBuilders
                .get("/clients")
                .contentType(MediaType.APPLICATION_JSON)
            )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk());
        }

        @Test
        void findById() throws Exception {
            mockMvc.perform(MockMvcRequestBuilders
                .get("/clients/1")
                .contentType(MediaType.APPLICATION_JSON)
            )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk());
        }

        @Test
        void findByName(TestInfo testInfo) throws Exception {
            Client client = ClientTestHelper.createValidClient(testInfo);
            String name = client.getClientName();
            String jsonRequest = mapper.writeValueAsString(client);
            mockMvc.perform(MockMvcRequestBuilders
                .post("/clients")
                .content(jsonRequest)
                .contentType(MediaType.APPLICATION_JSON)
            )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().is2xxSuccessful())
                .andExpect(header().string("Location", new StringContains("http://localhost/clients/")));

            mockMvc.perform(MockMvcRequestBuilders
                .get("/clients")
                .contentType(MediaType.APPLICATION_JSON)
                .param("clientName", name)
            )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk());
        }

        @Test
        void updateName(TestInfo testInfo) throws Exception {
            //Create our data for this test
            Client client = ClientTestHelper.createValidClient(testInfo);
            String jsonRequest = mapper.writeValueAsString(client);
            MvcResult result = mockMvc.perform(MockMvcRequestBuilders
                .post("/clients")
                .content(jsonRequest)
                .contentType(MediaType.APPLICATION_JSON)
            )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().is2xxSuccessful())
                .andExpect(header().string("Location", new StringContains("http://localhost/clients/")))
                .andReturn();

            //Use the returned location to get the data
            MvcResult clientData = mockMvc.perform(MockMvcRequestBuilders
                .get(result.getResponse().getRedirectedUrl())
                .contentType(MediaType.APPLICATION_JSON)
            )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andReturn();

            //Extract the context and update the name
            Client returnedClient = mapper.readValue(clientData.getResponse().getContentAsString(), Client.class);
            returnedClient.setClientName("UpdatedByITEST");

            //Push Update to server
            mockMvc.perform(MockMvcRequestBuilders
                .put(result.getResponse().getRedirectedUrl())
                .content(mapper.writeValueAsString(returnedClient))
                .contentType(MediaType.APPLICATION_JSON)
            )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().is2xxSuccessful())
                .andExpect(header().string("Location", new StringContains("http://localhost/clients/")));

            //Validate the Update
            mockMvc.perform(MockMvcRequestBuilders
                .get("/clients")
                .contentType(MediaType.APPLICATION_JSON)
                .param("clientName", "UpdatedByITEST")
            )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk());
        }

        @Nested
        class ClientDeleteOperations {

            @Test
            @Rollback(false)
            void deleteOperations() throws Exception {
                MvcResult clientData = mockMvc.perform(MockMvcRequestBuilders
                    .get("/clients")
                    .contentType(MediaType.APPLICATION_JSON)
                )
                    .andDo(MockMvcResultHandlers.print())
                    .andExpect(status().isOk())
                    .andReturn();

                //We need to do some parsing as this information is under and embedded section
                JsonObject json = new Gson().fromJson(clientData.getResponse().getContentAsString(), JsonObject.class);
                JsonArray jsonArray = json.getAsJsonObject("_embedded").getAsJsonArray("clients");
                for (JsonElement clientItem : jsonArray) {
                    String hrefUrl =
                        clientItem.getAsJsonObject().getAsJsonObject("_links").getAsJsonObject("self").getAsJsonPrimitive(
                            "href").getAsString();
                    mockMvc.perform(MockMvcRequestBuilders.delete(hrefUrl))
                        .andDo(MockMvcResultHandlers.print())
                        .andExpect(status().isNoContent());
                }

                clientData = mockMvc.perform(MockMvcRequestBuilders
                    .get("/clients")
                    .contentType(MediaType.APPLICATION_JSON)
                )
                    .andDo(MockMvcResultHandlers.print())
                    .andExpect(status().isOk())
                    .andReturn();

                json = new Gson().fromJson(clientData.getResponse().getContentAsString(), JsonObject.class);
                long count = json.getAsJsonObject("page").getAsJsonPrimitive("totalElements").getAsLong();
                Assertions.assertEquals(0L, count);
            }
        }
    }
}
