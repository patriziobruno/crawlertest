package net.dstc.crawlertest.data;

import java.net.URL;
import java.util.List;
import java.util.UUID;
import org.jsoup.helper.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Data access logic. It wraps calls to the repository to add parameter validation, etc.
 *
 * @author Patrizio Bruno (desertconsulting@gmail.com)
 */
@Service
public class DataImpl implements Data {

    /**
     * Spring Data JPA repository
     */
    @Autowired
    private DataRepository repository;

    @Override
    public void save(URL url, String title, long downloadTime, UUID uuid) {
        Optimizable entry = new Optimizable(url.toString(), title,
                downloadTime, uuid.toString());
        repository.save(entry);
    }

    @Override
    public List<Optimizable> list(String uuid) {

        return StringUtil.isBlank(uuid) ? repository.list() : repository.findByUuid(uuid);
    }
}
