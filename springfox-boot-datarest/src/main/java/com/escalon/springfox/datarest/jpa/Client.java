package com.escalon.springfox.datarest.jpa;

import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * @author amutsch
 * @since 12/18/18
 */
@Entity
@Table(name = "CLIENT")
public class Client {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "CLIENT_ID", updatable = false, nullable = false)
    private Long clientId;

    @NotNull
    @Size(min = 1, max = 200)
    @Column(name = "CLIENT_NAME")
    private String clientName;

    @Column(name = "ACTIVE")
    private boolean active;

    @Column(name = "CREATED")
    private LocalDateTime created;

    @NotNull
    @Size(min = 1, max = 100)
    @Column(name = "CREATED_BY")
    private String createdBy;

    @Column(name = "UPDATED")
    private LocalDateTime updated;

    @NotNull
    @Size(min = 1, max = 100)
    @Column(name = "UPDATED_BY")
    private String updatedBy;

    public Long getClientId() {
        return clientId;
    }

    public void setClientId(Long clientId) {
        this.clientId = clientId;
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public LocalDateTime getCreated() {
        return created;
    }

    public void setCreated(LocalDateTime created) {
        this.created = created;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public LocalDateTime getUpdated() {
        return updated;
    }

    public void setUpdated(LocalDateTime updated) {
        this.updated = updated;
    }

    public String getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
    }

    @PrePersist
    private void setInsertTimestamp() {
        this.setCreated(LocalDateTime.now());
        this.setUpdated(LocalDateTime.now());
    }

    @PreUpdate
    private void setUpdateTimestamp() {
        this.setUpdated(LocalDateTime.now());
    }
}
