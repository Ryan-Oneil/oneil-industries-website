package biz.oneilenterprise.website.repository;

import biz.oneilenterprise.website.entity.Role;
import java.util.List;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends CrudRepository<Role, Integer> {
    @Query("select r from Role r")
    List<Role> getAllRoles();
}
