package com.portafolio.zomtg.salesflow.users.repository;


import com.portafolio.zomtg.salesflow.users.entity.User;
import com.portafolio.zomtg.salesflow.users.enums.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {

    boolean existsByUsernameAndEmail(String username, String email);
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);
    //boolean existsOwnerByStoreId(UUID storeId);//select * from user where role=="OWNER" && storeID=storeid;
//    @Query("""
//    SELECT COUNT(u) > 0
//    FROM User u
//    WHERE u.role = 'OWNER'
//    AND u.storeId = :storeId
//""")
//    boolean existsOwnerByStoreId(@Param("storeId") UUID storeId);

    @Query("""
            SELECT COUNT(u)>0
            FROM User u
            WHERE u.role='OWNER'
            AND u.ownerId= :ownerId
            """)
    boolean existsOwnerByOwner(@Param("ownerId") UUID ownerId);


    Optional<User> findUserById(UUID id);
    Optional<User> findUserByNameAndSurname(String name, String surname);
    List<User> findByOwnerIdAndRole(UUID ownerId,Role role);
    List<User> findByStoreIdAndRole(UUID storeId, Role role);
   Optional<User>findUserByUsername(String username);



}
