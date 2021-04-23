package top.pippen.xncms.constant;

import java.io.File;

/**
 * @author Pippen
 * @since 2021/01/20 15:37
 */
public class XnCMSConst {


    /**
     * User home directory.
     */
    public static final String USER_HOME = System.getProperties().getProperty("user.home");


    public static final String PROTOCOL_HTTPS = "https://";

    public static final String PROTOCOL_HTTP = "http://";

    public static final String URL_SEPARATOR = "/";

    /**
     * Default error path.
     */
    public static final String DEFAULT_ERROR_PATH = "common/info/error";

    /**
     * info
     */
    public static final String DEFAULT_INFO_TEMPLATE = "common/info/info";

    /**
     * 微信授权跳板页
     */
    public static final String JUMP_PAGE_TEMPLATE = "common/wx/jump-page";

    /**
     * 微信绑定页面
     */
    public static final String WX_BIND_TEMPLATE = "common/wx/wx-bind";




    /**
     * Path separator.
     */
    public static final String FILE_SEPARATOR = File.separator;

    /**
     * Post password template name.
     */
    public static final String POST_PASSWORD_TEMPLATE = "post_password";
    /**
     * Suffix of freemarker template file
     */
    public static final String SUFFIX_FTL = ".ftl";
    /**
     * Custom freemarker tag method key.
     */
    public static final String METHOD_KEY = "method";
    /**
     * 网易云音乐短代码前缀
     */
    public static final String NETEASE_MUSIC_PREFIX = "[music:";
    /**
     * 网易云音乐 iframe 代码
     */
    public static final String NETEASE_MUSIC_IFRAME =
            "<iframe frameborder=\"no\" border=\"0\" marginwidth=\"0\" marginheight=\"0\" width=330 "
                    + "height=86 src=\"//music.163.com/outchain/player?type=2&id=$1&auto=1&height=66"
                    + "\"></iframe>";
    /**
     * 网易云音乐短代码正则表达式
     */
    public static final String NETEASE_MUSIC_REG_PATTERN = "\\[music:(\\d+)\\]";
    /**
     * 哔哩哔哩视频短代码前缀
     */
    public static final String BILIBILI_VIDEO_PREFIX = "[bilibili:";
    /**
     * 哔哩哔哩视频 iframe 代码
     */
    public static final String BILIBILI_VIDEO_IFRAME =
            "<iframe height=$3 width=$2 src=\"//player.bilibili.com/player.html?aid=$1\" "
                    + "scrolling=\"no\" border=\"0\" frameborder=\"no\" framespacing=\"0\" "
                    + "allowfullscreen=\"true\"> </iframe>";
    /**
     * 哔哩哔哩视频正则表达式
     */
    public static final String BILIBILI_VIDEO_REG_PATTERN =
            "\\[bilibili:(\\d+)\\,(\\d+)\\,(\\d+)\\]";
    /**
     * YouTube 视频短代码前缀
     */
    public static final String YOUTUBE_VIDEO_PREFIX = "[youtube:";
    /**
     * YouTube 视频 iframe 代码
     */
    public static final String YOUTUBE_VIDEO_IFRAME =
            "<iframe width=$2 height=$3 src=\"https://www.youtube.com/embed/$1\" frameborder=\"0\" "
                    + "allow=\"accelerometer; autoplay; encrypted-media; gyroscope; picture-in-picture\" "
                    + "allowfullscreen></iframe>";
    /**
     * YouTube 视频正则表达式
     */
    public static final String YOUTUBE_VIDEO_REG_PATTERN = "\\[youtube:(\\w+)\\,(\\d+)\\,(\\d+)\\]";
    /**
     * Unknown version: unknown
     */
    public static final String UNKNOWN_VERSION = "unknown";

}
