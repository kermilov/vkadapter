package ru.kermilov.targetaudience.vkadapter.service;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Objects;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.vk.api.sdk.objects.base.City;
import com.vk.api.sdk.objects.friends.responses.SearchResponse;
import com.vk.api.sdk.objects.likes.responses.GetListResponse;
import com.vk.api.sdk.objects.users.responses.GetResponse;

import org.apache.commons.io.IOUtils;

import ru.kermilov.targetaudience.vkadapter.domain.GroupEntity;
import ru.kermilov.targetaudience.vkadapter.domain.PostEntity;
import ru.kermilov.targetaudience.vkadapter.domain.PostTemplateEntity;

public class TestConstants {

    static final Integer VKMUSICIANS_ID = 50695130;
	static final String VKMUSICIANS_SCREEN_NAME = "vkmusicians";
	static final String VKMUSICIANS_NAME = "VK Musicians";
    static final String VKMUSICIANS_URL_ID = "https://vk.com/public"+VKMUSICIANS_ID;
	static final String VKMUSICIANS_URL_SCREEN_NAME = "https://vk.com/"+VKMUSICIANS_SCREEN_NAME;

    static final Integer VKMUSICIANS_POST_ID = 68913;
    static final String VKMUSICIANS_POST_URL = "https://vk.com/wall-50695130_68913";
    static final String VKMUSICIANS_POST_DESC = "[club149151284|Помехи] — «Камнем На Шее» (2017) \n\n#rock@vkmusicians\n#post@vkmusicians \n#indie@vkmusicians";
	static final String VKMUSICIANS_POST_ATTACH = "audio_playlist-149151284_1";

    static final Integer E_MUSIC_YAR_ID = 42514821;
	static final String E_MUSIC_YAR_SCREEN_NAME = "e_music_yar";
	static final String E_MUSIC_YAR_NAME = "E:\\music\\Ярославль";
    static final String E_MUSIC_YAR_URL_ID = "https://vk.com/public"+E_MUSIC_YAR_ID;
	static final String E_MUSIC_YAR_URL_SCREEN_NAME = "https://vk.com/"+E_MUSIC_YAR_SCREEN_NAME;

    static final Integer E_MUSIC_YAR_POST_ID = 471;
	static final String E_MUSIC_YAR_POST_URL = "https://vk.com/wall-42514821_471";
	static final String E_MUSIC_YAR_POST_DESC = "[club149151284|Помехи] — Камнем На Шее (2017) \n[club55413494|indie rock] | [club30653307|post-punk] \n\n\"Гимн напрасным усилиям и всему не произошедшему\" - именно так описывает создатель студийного проекта [club149151284|Помехи] альбом \"Камнем На Шее\", увидевший свет ещё в июле 2017-го года. На выходе мы имеем довольно неплохой инди-рок, приправленный загадочными и мрачноватыми мотивами пост-панка. Танцевально, однако.\n\n#indie_rock #post_punk";
	static final String E_MUSIC_YAR_POST_ATTACH = "photo-42514821_456239104,audio2000104287_456241909,audio2000104406_456241970,audio2000102498_456242295,audio2000101982_456242285,audio2000103568_456242099,audio2000102218_456242260,audio2000103905_456241942,audio2000102790_456242274,audio2000102463_456242304";

    static final int AUTHOR_ID = 5217060;
    static final String FIRST_NAME = "Кирилл";
    static final String LAST_NAME = "Ермилов";
    static final String CITY = "Ярославль";

    static GroupEntity getGroupEntityVkMusicians() {
        return new GroupEntity(VKMUSICIANS_ID,
            1,
            VKMUSICIANS_NAME,
            VKMUSICIANS_URL_ID);
    }

    static PostTemplateEntity getPostTemplateEntityVkMusicians() {
        return new PostTemplateEntity(TestConstants.VKMUSICIANS_POST_DESC,
            TestConstants.VKMUSICIANS_POST_ATTACH);
    }

    static PostEntity getPostEntityVkMusicians() {
        return new PostEntity(getGroupEntityVkMusicians(),
            VKMUSICIANS_POST_DESC,
            VKMUSICIANS_POST_ATTACH,
            VKMUSICIANS_POST_ID);
    }

    static GroupEntity getGroupEntityEMusicYar() {
        return new GroupEntity(E_MUSIC_YAR_ID,
            1,
            E_MUSIC_YAR_NAME,
            E_MUSIC_YAR_URL_ID);
    }

    static PostTemplateEntity getPostTemplateEntityEMusicYar() {
        return new PostTemplateEntity(TestConstants.E_MUSIC_YAR_POST_DESC,
            TestConstants.E_MUSIC_YAR_POST_ATTACH);
    }

    static PostEntity getPostEntityEMusicYar() {
        return new PostEntity(getGroupEntityEMusicYar(),
            E_MUSIC_YAR_POST_DESC,
            E_MUSIC_YAR_POST_ATTACH,
            E_MUSIC_YAR_POST_ID);
    }

    static String groupGetByIdLegacyInVkMusiciansScreenName() {
        return VKMUSICIANS_SCREEN_NAME;
    }
    static String groupGetByIdLegacyInVkMusiciansId() {
        return VKMUSICIANS_ID.toString();
    }

    static com.vk.api.sdk.objects.groups.responses.GetByIdLegacyResponse groupGetByIdLegacyOutVkMusicians() {
        var result = new com.vk.api.sdk.objects.groups.responses.GetByIdLegacyResponse();
        result.setId(VKMUSICIANS_ID);
        result.setName(VKMUSICIANS_NAME);
        return result;
    }

    static String wallGetByIdLegacyInVkMusicians() {
        return "-"+VKMUSICIANS_ID+"_"+VKMUSICIANS_POST_ID;
    }

    static com.vk.api.sdk.objects.wall.responses.GetByIdLegacyResponse wallGetByIdLegacyOutVkMusicians() {
        var result = new com.vk.api.sdk.objects.wall.responses.GetByIdLegacyResponse();
        result.setId(VKMUSICIANS_POST_ID);
        result.setText(VKMUSICIANS_POST_DESC);
        result.setOwnerId(VKMUSICIANS_ID*-1);
        return result;
    }

    static String wallGetByIdLegacyInEMusicYar() {
        return "-"+E_MUSIC_YAR_ID+"_"+E_MUSIC_YAR_POST_ID;
    }

    static com.vk.api.sdk.objects.wall.responses.GetByIdLegacyResponse wallGetByIdLegacyOutEMusicYar() {
        var result = new com.vk.api.sdk.objects.wall.responses.GetByIdLegacyResponse();
        result.setId(E_MUSIC_YAR_POST_ID);
        result.setText(E_MUSIC_YAR_POST_DESC);
        result.setOwnerId(E_MUSIC_YAR_ID*-1);
        return result;
    }

    static String executeWallInVkMusicians() {
        return "return API.wall.getById({\"posts\": \""+wallGetByIdLegacyInVkMusicians()+"\"});";
    }

    static JsonArray executeWallOutVkMusicians() throws IOException {
        return new Gson().fromJson(getJson("api-wall-getbyid-50695130_68913-answer-example"), JsonArray.class);
    }

    static String executeWallInEMusicYar() {
        return "return API.wall.getById({\"posts\": \""+wallGetByIdLegacyInEMusicYar()+"\"});";
    }

    static JsonArray executeWallOutEMusicYar() throws IOException {
        return new Gson().fromJson(getJson("api-wall-getbyid-42514821_471-answer-example"), JsonArray.class);
    }

    private static String getJson(String name) throws IOException {
        return IOUtils.toString(
                Objects.requireNonNull(TestConstants.class.getClassLoader().getResourceAsStream(
                    name+".json")),
                StandardCharsets.UTF_8.name());
    }

    static com.vk.api.sdk.objects.friends.responses.SearchResponse getFriendsSearchResponseEmpty() {
        var friendsResponse = new com.vk.api.sdk.objects.friends.responses.SearchResponse();
        friendsResponse.setCount(0);
        friendsResponse.setItems(List.of());
        return friendsResponse;
    }

    static com.vk.api.sdk.objects.likes.responses.GetListResponse getLikesResponseAuthor() {
        var likesResponse = new com.vk.api.sdk.objects.likes.responses.GetListResponse();
        likesResponse.setCount(1);
        likesResponse.setItems(List.of(AUTHOR_ID));
        return likesResponse;
    }

    static com.vk.api.sdk.objects.users.responses.GetResponse getUsersResponseAuthor() {
        var vkUser = new com.vk.api.sdk.objects.users.responses.GetResponse();
        vkUser.setId(AUTHOR_ID);
        vkUser.setFirstName(FIRST_NAME);
        vkUser.setLastName(LAST_NAME);
        var city = new City();
        city.setTitle(CITY);
        vkUser.setCity(city);
        return vkUser;
    }

}