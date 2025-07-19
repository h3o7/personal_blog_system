package blogsystem.utils;

public enum UploadType {
    USER_AVATAR("users/", "用户头像", "user-image"),
    AD_POSTER("ad/", "广告海报", "ad-poster"),
    MOVIE_POST("movies/", "电影海报", "movie-post"),
    ARTICLE_COVER("articles/", "文章封面", "article-cover"),
    ARTICLE_CONTENT("articles/content/", "文章内容图片", "article-content"),
    DOCUMENT("documents/", "文档", "document");

    private final String path;
    private final String description;
    private final String code; // 用于URL路径

    UploadType(String path, String description, String code) {
        this.path = path;
        this.description = description;
        this.code = code;
    }

    public String getPath() { return path; }
    public String getDescription() { return description; }
    public String getCode() { return code; }

    /**
     * 根据代码获取上传类型
     */
    public static UploadType fromString(String code) {
        for (UploadType type : UploadType.values()) {
            if (type.code.equals(code)) {
                return type;
            }
        }
        throw new IllegalArgumentException("不支持的上传类型: " + code);
    }
}