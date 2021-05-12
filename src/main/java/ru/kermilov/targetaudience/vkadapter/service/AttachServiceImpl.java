package ru.kermilov.targetaudience.vkadapter.service;

import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.vk.api.sdk.client.actors.UserActor;
import com.vk.api.sdk.exceptions.ApiException;
import com.vk.api.sdk.exceptions.ClientException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import ru.kermilov.targetaudience.vkadapter.wrapper.VkApiClientWrapper;

@Service
@RequiredArgsConstructor
public class AttachServiceImpl implements AttachService {
    @Autowired
    private final VkApiClientWrapper vkApiClientWrapper;
    @Autowired
    private final UserActor actor;

    @Override
    public String parse(String id) {
        return StreamSupport.stream(getJsonAttachments(id).getAsJsonArray().spliterator(), false)
            .map(this::mapper)
            .collect(Collectors.joining(","));
    }

    private JsonElement getJsonAttachments(String posts) {
        JsonElement result = new JsonObject();

        try {
            var request = String.format("return API.wall.getById({\"posts\": \"%s\"});",
                posts);
            result = vkApiClientWrapper.vkApiClient()
                    .execute()
                    .code(actor, request)
                    .execute()
                    .getAsJsonArray().get(0)
                    .getAsJsonObject().get("attachments");
        } catch (ClientException | ApiException e) {
            e.printStackTrace();
        }

        return result;
    }
    private String mapper(JsonElement attachment) {
        var json = attachment.getAsJsonObject();
        var type = json.get("type").getAsString();
        var value = json.get(type).getAsJsonObject();
        if (type.equals("link")) {
            var url = value.get("url").getAsJsonPrimitive();
            if (url != null) {
                return getAudioPlaylistId(url.getAsString());
            }
        }
        var ownerId = value.get("owner_id");
        var id = value.get("id");
        if (ownerId != null && id != null) {
            return type+ownerId.getAsString()+"_"+id.getAsString();
        }
        return "";
    }
    private String getAudioPlaylistId(String url) {
        int beginIndex = url.indexOf("audio_playlist");
        if (beginIndex > 0) {
            int endIndex = url.indexOf("&", beginIndex);
            if (endIndex > 0) {
                return url.substring(beginIndex, endIndex);
            }
            return url.substring(beginIndex);
        }
        return "";
    }

    /* для истории, если audio.getOwnerId() появится в VK Java SDK
    private String mapper(WallpostAttachment attachment) {
        if (attachment.getType().equals(WallpostAttachmentType.LINK)) {
            var url = attachment.getLink().getUrl().getQuery();
            return getAudioPlaylistId(url);
        } else
        if (attachment.getType().equals(WallpostAttachmentType.PHOTO)) {
            var photo = attachment.getPhoto();
            return "photo"+photo.getOwnerId()+"_"+photo.getId();
        } else
        if (attachment.getType().equals(WallpostAttachmentType.AUDIO)) {
            var audio = attachment.getAudio();
            return "audio"+"audio.getOwnerId()"+"_"+audio.getId();
        }
        return "";
    }
    */
}
