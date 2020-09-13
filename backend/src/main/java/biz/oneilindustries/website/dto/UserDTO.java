package biz.oneilindustries.website.dto;

public class UserDTO {

    private int id;
    private String name;
    private String email;
    private String role;
    private boolean enabled;
    private QuotaDTO quota = new QuotaDTO(0, 0, false);

    public UserDTO(int id, String name, String email, String role, boolean enabled) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.role = role;
        this.enabled = enabled;
    }

    public UserDTO(int id, String name, String email, String role, boolean enabled, QuotaDTO quota) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.role = role;
        this.enabled = enabled;
        this.quota = quota;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getRole() {
        return role;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public QuotaDTO getQuota() {
        return quota;
    }

    public void setQuota(QuotaDTO quota) {
        this.quota = quota;
    }
}
