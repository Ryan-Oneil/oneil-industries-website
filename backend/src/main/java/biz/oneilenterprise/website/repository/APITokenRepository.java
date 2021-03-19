package biz.oneilenterprise.website.repository;

import biz.oneilenterprise.website.entity.ApiToken;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface APITokenRepository extends CrudRepository<ApiToken, String> {

}
