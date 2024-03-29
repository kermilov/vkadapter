package ru.kermilov.targetaudience.vkadapter.domain;

import javax.persistence.*;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.NaturalId;
import org.hibernate.annotations.Type;

@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "vk_posttemplate")
public class PostTemplateEntity {
    public PostTemplateEntity(String message, String attachment) {
        this.message = message;
        this.attachment = attachment;
    }
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    @NaturalId
    @Column(name = "externalid")
    private String externalId;
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
}
