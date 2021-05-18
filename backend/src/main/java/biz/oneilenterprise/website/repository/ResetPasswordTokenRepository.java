package biz.oneilenterprise.website.repository;


import biz.oneilenterprise.website.entity.PasswordResetToken;
import biz.oneilenterprise.website.entity.User;
import java.util.Optional;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ResetPasswordTokenRepository extends CrudRepository<PasswordResetToken, Integer> {
    Optional<PasswordResetToken> getByUser(User name);
    Optional<PasswordResetToken> getByToken(String token);
}
