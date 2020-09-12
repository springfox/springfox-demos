package com.escalon.springfox.datarest.jpa;


import com.escalon.springfox.datarest.ClientApplication;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.core.AutoConfigureCache;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureDataJpa;
import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureTestEntityManager;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
public class ClientRepositoryDSTEST {

    @Autowired
    private ClientRepository clientRepository;
    private static List<Long> addedIds = new ArrayList<>();

    @Test
    @Rollback(false)
    void addValidClient(TestInfo testInfo) {
        final LocalDateTime testStartTime = LocalDateTime.now();
        final Client client = clientRepository.save(ClientTestHelper.createValidClient(testInfo));
        final String dynamicData = ClientTestHelper.getDynamicDataFromTestInfo(testInfo);
        addedIds.add(client.getClientId());
        Assertions.assertAll(() -> Assertions.assertEquals(dynamicData, client.getClientName()),
            () -> Assertions.assertEquals(dynamicData, client.getCreatedBy()),
            () -> Assertions.assertEquals(dynamicData, client.getUpdatedBy()),
            () -> Assertions.assertNotNull(client.getClientId()),
            () -> Assertions.assertTrue(client.getCreated().compareTo(testStartTime) >= 0
                && client.getCreated().compareTo(LocalDateTime.now()) <= 0, "Creation time check"),
            () -> Assertions.assertTrue(client.getUpdated().compareTo(testStartTime) >= 0
                && client.getUpdated().compareTo(LocalDateTime.now()) <= 0, "Update time check"),
            () -> Assertions.assertTrue(client.isActive()));
    }

    @Test
    @Rollback(false)
    void addInactiveClient(TestInfo testInfo) {
        final LocalDateTime testStartTime = LocalDateTime.now();
        Client preAddClient = ClientTestHelper.createValidClient(testInfo);
        preAddClient.setActive(false);
        final Client client = clientRepository.save(preAddClient);
        final String dynamicData = ClientTestHelper.getDynamicDataFromTestInfo(testInfo);
        addedIds.add(client.getClientId());
        Assertions.assertAll(() -> Assertions.assertEquals(dynamicData, client.getClientName()),
            () -> Assertions.assertEquals(dynamicData, client.getCreatedBy()),
            () -> Assertions.assertEquals(dynamicData, client.getUpdatedBy()),
            () -> Assertions.assertNotNull(client.getClientId()),
            () -> Assertions.assertTrue(client.getCreated().compareTo(testStartTime) >= 0
                && client.getCreated().compareTo(LocalDateTime.now()) <= 0, "Creation time check"),
            () -> Assertions.assertTrue(client.getUpdated().compareTo(testStartTime) >= 0
                && client.getUpdated().compareTo(LocalDateTime.now()) <= 0, "Update time check"),
            () -> Assertions.assertFalse(client.isActive(), "Active Check"));
    }

    @Nested
    class ClientPostCreateOperationsDSTEST {

        @Test
        void findAll() {
            List<Long> clientIds = new ArrayList<>(addedIds);
            long count = 0;
            long matchCount = 0;
            Iterable<Client> dbClients = clientRepository.findAll();
            long dbCount = clientRepository.count();
            for (Client client : dbClients) {
                count++;
                if (clientIds.contains(client.getClientId())) {
                    matchCount++;
                }
            }
            Assertions.assertEquals(dbCount, count);
            Assertions.assertEquals(clientIds.size(), matchCount);
        }

        @Test
        void findById() {
            Optional<Client> client = clientRepository.findById(addedIds.get(0));
            Assertions.assertTrue(client.isPresent());
        }

        @Test
        void findByName() {
            Optional<Client> client = clientRepository.findById(addedIds.get(addedIds.size() - 1));
            Optional<Client> fetchedClient = clientRepository.findByClientName(client.get().getClientName());
            Assertions.assertEquals(client.get().getClientId(), fetchedClient.get().getClientId());
        }

        @Test
        void updateName(TestInfo testInfo) {
            Optional<Client> client = clientRepository.findById(addedIds.get(0));
            Client returnedClient = client.get();
            String name = ClientTestHelper.getDynamicDataFromTestInfo(testInfo);
            returnedClient.setClientName(name);
            clientRepository.save(returnedClient);
            Assertions.assertEquals(name, clientRepository.findById(returnedClient.getClientId()).get().getClientName());
        }

        @Nested
        class ClientDeleteOperations {

            @Test
            @Rollback(false)
            void deleteOperations() {
                Long id = addedIds.remove(0);
                clientRepository.deleteById(id);

                clientRepository.deleteAll(clientRepository.findAllById(addedIds));

                addedIds.add(id);
                Assertions.assertFalse(clientRepository.findAllById(addedIds).iterator().hasNext());
            }
        }
    }

}
