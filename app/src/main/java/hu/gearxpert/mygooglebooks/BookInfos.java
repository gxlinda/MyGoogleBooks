package hu.gearxpert.mygooglebooks;

/**
 * Created by melinda.kostenszki on 2017.05.24..
 */

public class BookInfos {

    private String mTitle;
    private String mAuthors;
    private  String mPublisher;
    private String mPublishedDate;
    private int mPageCount;
    private String mSmallThumbnailUrl;
    private String mInfoLinkUrl;

    public BookInfos (String title, String authors, String publisher, String publishedDate, int pageCount, String thumbnailUrl, String infoLinkUrl) {
        mTitle = title;
        mAuthors = authors;
        mPublisher = publisher;
        mPublishedDate = publishedDate;
        mPageCount = pageCount;
        mSmallThumbnailUrl = thumbnailUrl;
        mInfoLinkUrl = infoLinkUrl;
    }

    public String getTitle() {
        return mTitle;
    }

    public String getAuthors() {
        return mAuthors;
    }

    public String getPublisher() {
        return mPublisher;
    }

    public String getPublishedDate() {
        return mPublishedDate;
    }

    public int getPageCount() {
        return mPageCount;
    }

    public String getThumbnailUrl() {
        return mSmallThumbnailUrl;
    }

    public String getInfoLinkUrl() {
        return mInfoLinkUrl;
    }
}
