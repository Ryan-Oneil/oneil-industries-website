package biz.oneilindustries.website.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "service_token")
public class ServiceToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "token_uuid")
    private String tokenUUID;

    @Column(name = "service_uuid")
    private String servicUUID;

    private String service;

    public ServiceToken(String tokenUUID, String servicUUID, String service) {
        this.tokenUUID = tokenUUID;
        this.servicUUID = servicUUID;
        this.service = service;
    }

    public ServiceToken() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTokenUUID() {
        return tokenUUID;
    }

    public void setTokenUUID(String tokenUUID) {
        this.tokenUUID = tokenUUID;
    }

    public String getServicUUID() {
        return servicUUID;
    }

    public void setServicUUID(String servicUUID) {
        this.servicUUID = servicUUID;
    }

    public String getService() {
        return service;
    }

    public void setService(String service) {
        this.service = service;
    }
}
