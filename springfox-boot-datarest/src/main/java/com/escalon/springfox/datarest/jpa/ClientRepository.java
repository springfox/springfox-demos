package com.escalon.springfox.datarest.jpa;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.Optional;

/**
 * @author amutsch
 * @since 12/18/18
 */
@RepositoryRestResource(path = "clients")
public interface ClientRepository extends PagingAndSortingRepository<Client, Long> {
    Optional<Client> findByClientName(@Param("clientName") String clientName);
}
