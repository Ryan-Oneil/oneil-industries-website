package biz.oneilindustries.website.service;

import biz.oneilindustries.website.dao.RoleDAO;
import biz.oneilindustries.website.entity.Role;
import java.util.List;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RoleService {

    private final RoleDAO roleDAO;

    @Autowired
    public RoleService(RoleDAO roleDAO) {
        this.roleDAO = roleDAO;
    }

    @Transactional
    public List<Role> getRoles() {
        return roleDAO.getRoles();
    }
}
