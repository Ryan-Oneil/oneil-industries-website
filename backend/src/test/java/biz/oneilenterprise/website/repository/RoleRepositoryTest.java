package biz.oneilenterprise.website.repository;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import biz.oneilenterprise.website.entity.Role;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;


public class RoleRepositoryTest extends BaseRepository {

    @Autowired
    private RoleRepository roleRepository;

    @Test
    public void getAllRolesTest() {
        List<Role> roles = roleRepository.getAllRoles();

        assertThat(roles.size()).isEqualTo(1);
    }

}
