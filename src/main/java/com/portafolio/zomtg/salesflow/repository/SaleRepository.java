package com.portafolio.zomtg.salesflow.repository;

import com.portafolio.zomtg.salesflow.model.entities.Sale;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
@Repository
public interface SaleRepository extends JpaRepository<Sale, UUID> {
    List<Sale> findByStoreId(UUID storeId);
    @Query("""
SELECT s 
FROM Sale s 
WHERE s.storeId = :storeId 
AND s.datetime BETWEEN :start AND :end
""")
    List<Sale> findSalesByStoreAndDate(
            @Param("storeId") UUID storeId,
            @Param("start") LocalDateTime start,
            @Param("end") LocalDateTime end
    );
    @Query(value = """
SELECT DATE(datetime) as day, SUM(total) as total
FROM sale
WHERE storeId = :storeId
GROUP BY day
ORDER BY day
""", nativeQuery = true)
    List<Object[]> getDailySales(UUID storeId);

    @Query(value = """
SELECT date_trunc('week', datetime) as week, SUM(total) as total
FROM sale
WHERE store_id = :storeId
GROUP BY week
ORDER BY week
""", nativeQuery = true)
    List<Object[]> getWeeklySale(UUID storeId);

    List<Sale> findByClientId(UUID clientId);

    @Query(value= """
            SELECT DATE_TRUNC('week',datetime) as week, SUM (total) as total
            FROM sale 
            WHERE store_id=:storeId and employee_id=:employeeId
            GROUP BY week
            ORDER BY week
            """,nativeQuery = true
    )
    List<Object[]> getWeeklySaleByEmployee(UUID storeId, UUID employeeId);

    List<Sale> findByEmployeeIdAndStoreId(UUID employeeId, UUID storeId);
//    @Query("select from sales where storeId= :storeId and datetime.dayofYear= :day")
//    List <Sale> findByStoreIdAndDayofYear(@Param("storeId") UUID storeId, @Param("day") Integer day);

}
