package net.dstc.crawlertest.data;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

/**
 * Spring Data JPA repository
 *
 * @author Patrizio Bruno (desertconsulting@gmail.com)
 */
public interface DataRepository extends JpaRepository<Optimizable, String> {

    /**
     * Finds all the URLs crawled by a specific client request
     * @param uuid request ID
     * @return a list of optimizable URLs and related info
     */
    @Query("select m from Optimizable m where m.uuid = ?1")
    public List<Optimizable> findByUuid(String uuid);

    /**
     * Lists all the crawled URLs
     * @return a list of optimizable URLs and related info
     */
    @Query("select m from Optimizable m")
    public List<Optimizable> list();
}
