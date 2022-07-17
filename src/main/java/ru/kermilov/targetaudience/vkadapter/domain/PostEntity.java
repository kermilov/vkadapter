package ru.kermilov.targetaudience.vkadapter.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.*;
import org.hibernate.annotations.NaturalId;
import org.hibernate.annotations.Type;

import java.nio.charset.StandardCharsets;
import java.util.UUID;

@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "vk_post")
public class PostEntity {

    private static final String URL_PREFIX = "https://vk.com/wall";

    public PostEntity(GroupEntity groupEntity, String message, String attachment, Integer externalId, String url,
            PostStatus status) {
        this.groupEntity = groupEntity;
        this.message = message;
        this.attachment = attachment;
        this.externalId = externalId;
        this.url = url;
        this.status = status;
    }

    public PostEntity(GroupEntity groupEntity, String message, String attachment, Integer externalId) {
        this(groupEntity, message, attachment);
        setExternalIdAndUrl(externalId);
    }

    public PostEntity(GroupEntity groupEntity, String message, String attachment) {
        this.groupEntity = groupEntity;
        this.message = message;
        this.attachment = attachment;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @SneakyThrows
    public void calcHash() {
        String s = "G" + this.groupEntity.getId() + "M" + this.getMessage() + "A" + this.getAttachment();
        setHash(UUID.nameUUIDFromBytes(s.getBytes(StandardCharsets.UTF_8)).toString());
    }

    @Column(name = "hash")
    private String hash;
    @ManyToOne
    @JoinColumn(name = "groupid")
    private GroupEntity groupEntity;
    @Lob
    @Type(type = "org.hibernate.type.TextType")
    @Column(name = "message")
    private String message;
    @Lob
    @Type(type = "org.hibernate.type.TextType")
    @Column(name = "attachment")
    private String attachment;
    @Column(name = "comment")
    private String comment;
    @NaturalId
    @Column(name = "externalid")
    private Integer externalId;
    @Column(name = "url")
    private String url;
    @Enumerated(EnumType.ORDINAL)
    @Column(name = "status")
    private PostStatus status = PostStatus.SUGGEST;

    public void setExternalIdAndUrl(Integer externalId) {
        setExternalId(externalId);
        setUrl(URL_PREFIX+getGroupEntity().getExternalId()*-1+"_"+getExternalId());
    }

    public String getVKApiClientId() {
        return getUrl().replaceFirst(URL_PREFIX, "");
    }
}
