package biz.oneilindustries.website.repository;

import biz.oneilindustries.website.entity.User;
import biz.oneilindustries.website.entity.VerificationToken;
import java.util.Optional;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VerificationTokenRepository extends CrudRepository<VerificationToken, Integer> {
    Optional<VerificationToken> findByToken(String token);
    Optional<VerificationToken> findByUsername(User user);
}
