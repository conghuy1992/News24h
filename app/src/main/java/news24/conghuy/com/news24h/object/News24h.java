package news24.conghuy.com.news24h.object;

/**
 * Created by maidinh on 20/7/2016.
 */
public class News24h {
    String link;
    String title;

    public News24h(String link, String title ) {
        this.link = link;
        this.title = title;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
