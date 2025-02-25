package cms.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import cms.domain.model.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    
	//@Query("from User u left join fetch u.roles r left join fetch r.permissions where u.oidcId = :oidcId ")
	User findByOidcId(@Param("oidcId") String oidcId);
}
