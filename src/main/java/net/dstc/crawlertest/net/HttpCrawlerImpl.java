package net.dstc.crawlertest.net;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;
import net.dstc.crawlertest.data.Data;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * This class crawls and index all info about an optimizable site
 *
 * @author Patrizio Bruno (desertconsulting@gmail.com)
 */
public class HttpCrawlerImpl implements HttpCrawler {

    /**
     * This is what a optimizable page looks like.
     */
    private static final Pattern OPTIMIZABLE = Pattern.compile("(news|noticias)", Pattern.CASE_INSENSITIVE);

    /**
     * Already parsed URLs, lets skip them from future downloads
     */
    private final List<String> done;

    /**
     * Data layer
     */
    @Autowired
    private Data data;

    /**
     * Initializes {@link HttpCrawlerImpl}'s internal state
     */
    public HttpCrawlerImpl() {
        done = new ArrayList<>();
    }

    /**
     * Downloads and parsed a page pointed by {@code url} and returns all
     * relevant info about it: if it's optimizable, its title and all links to
     * pages in the same domain
     *
     * @param url URL of the page to be downloaded
     * @return relevant info about the page pointed by {@code url}
     * @throws IOException
     */
    private ParsedPage parsePage(URL url) throws IOException {
        done.add(url.toString());
        ParsedPage rv = new ParsedPage();

        long st = System.nanoTime();
        Document document = Jsoup.connect(url.toString()).timeout(20000).validateTLSCertificates(false).get();
        rv.elapsedTime = (System.nanoTime() - st) / 1000000;

        String title = document.title();
        if (title != null && OPTIMIZABLE.matcher(title).find()) {
            rv.isOptimizable = true;
            rv.title = title;
        }
        rv.subPages = getLinks(url, document);
        return rv;
    }

    /**
     * Finds all the links in {@code document} that belongs to the same domain
     * {@code document} comes from.
     *
     * @param url document's URL
     * @param document page pointed by url
     * @return a list of matching URLs
     */
    private List<URL> getLinks(URL url, Document document) {
        List<URL> rv = new ArrayList<>();
        final String baseUrl = String.format("%s://%s", url.getProtocol(), url.getAuthority());
        final String pageUrl = url.toString();
        Elements links = document.getElementsByTag("a");
        links.forEach(link -> {
            String u = link.absUrl("href");
            if (u.startsWith(baseUrl) && !pageUrl.equals(u)) {
                try {
                    if (!done.contains(u)) {
                        rv.add(new URL(u));
                    }
                } catch (MalformedURLException ex) {
                    Logger.getLogger(HttpCrawlerImpl.class.getName()).log(Level.SEVERE, pageUrl, ex);
                }
            }
        });
        return rv.size() > 0 ? rv : null;
    }

    private class ParsedPage {

        private boolean isOptimizable;
        private List<URL> subPages;
        private String title;
        private long elapsedTime;
    }

    /**
     * Runs a crawling operation over a page pointed by a specific URL and all
     * linked pages.
     *
     * @param startUrl URL to start crawling from
     * @param requestId request unique ID
     * @return a list of URLs connected to the crawled URL
     * @throws IOException error retrieving/parsing the first URL passed in as
     * parameter
     */
    @Override
    public List<URL> run(final URL startUrl, final UUID requestId) throws IOException {

        // lets run over a queue of URLs and crawl its entries one by one ...
        List<URL> rv = new ArrayList<>();
        Queue<URL> queue = new LinkedList<>();

        boolean first = true;
        // ... starting with startUrl, of course
        queue.add(startUrl);

        URL url;
        done.clear();
        while ((url = queue.poll()) != null) {
            try {
                ParsedPage page = parsePage(url);
                if (page.isOptimizable) {
                    // lets save marfelizable URLs
                    data.save(url, page.title, page.elapsedTime, requestId);
                    rv.add(url);
                }
                if (page.subPages != null) {
                    // lets add to the queue every page linked from the parsed page
                    // that belongs to the same domain
                    queue.addAll(page.subPages);
                }
                first = false;
            } catch (IOException ex) {
                Logger.getLogger(HttpCrawlerImpl.class.getName()).log(Level.SEVERE, url.toString(), ex);
                // if we can't even download the first page, no way to get on and we'll let the client know
                if (first) {
                    throw ex;
                }
            }
        }
        return rv;
    }
}
