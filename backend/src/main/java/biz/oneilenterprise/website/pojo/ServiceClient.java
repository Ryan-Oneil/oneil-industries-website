package biz.oneilenterprise.website.pojo;

public class ServiceClient {

    private String name;
    private String uuid;

    public ServiceClient() {
    }

    public ServiceClient(String name, String uuid) {
        this.name = name;
        this.uuid = uuid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }
}
