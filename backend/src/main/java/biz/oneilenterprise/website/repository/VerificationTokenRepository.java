package biz.oneilenterprise.website.repository;

import biz.oneilenterprise.website.entity.User;
import biz.oneilenterprise.website.entity.VerificationToken;
import java.util.Optional;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VerificationTokenRepository extends CrudRepository<VerificationToken, Integer> {
    Optional<VerificationToken> findByToken(String token);
    Optional<VerificationToken> findByUser(User user);
}
