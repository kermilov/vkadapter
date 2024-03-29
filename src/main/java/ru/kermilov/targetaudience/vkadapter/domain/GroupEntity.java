package ru.kermilov.targetaudience.vkadapter.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.*;
import org.hibernate.annotations.NaturalId;

@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "vk_group")
public class GroupEntity {
    public GroupEntity(Integer externalId, Integer membersCount, String name, String url) {
        this.externalId = externalId;
        this.membersCount = membersCount;
        this.name = name;
        this.url = url;
    }
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    @NaturalId
    @Column(name = "externalid")
    private Integer externalId;
    @Column(name = "memberscount")
    private Integer membersCount;
    @Column(name = "name")
    private String name;
    @Column(name = "url")
    private String url;
    @Column(name = "comment")
    private String comment;
}
