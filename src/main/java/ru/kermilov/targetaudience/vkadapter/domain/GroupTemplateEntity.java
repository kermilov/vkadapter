package ru.kermilov.targetaudience.vkadapter.domain;

import lombok.*;
import org.hibernate.annotations.NaturalId;
import org.hibernate.annotations.Type;

import javax.persistence.*;

@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "vk_grouptemplate")
public class GroupTemplateEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    @NaturalId
    @ManyToOne
    @JoinColumn(name = "groupid")
    private GroupEntity groupEntity;
    @NaturalId
    @Enumerated(EnumType.ORDINAL)
    @Column(name = "type")
    private ContentType type;
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
