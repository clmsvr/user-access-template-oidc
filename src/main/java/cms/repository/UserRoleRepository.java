package cms.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import cms.domain.model.UserRole;

@Repository
public interface UserRoleRepository extends JpaRepository<UserRole, Long> {
	
    //Query com Multi bag problem
	//@Query("from UserRole u left join fetch u.roles r left join fetch r.permissions where u.oidcId = :oidcId ")
	UserRole findByOidcId(@Param("oidcId") String oidcId);

}
