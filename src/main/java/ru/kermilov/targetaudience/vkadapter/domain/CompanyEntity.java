package ru.kermilov.targetaudience.vkadapter.domain;

import lombok.*;

import javax.persistence.*;
import java.util.List;

@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "vk_company")
public class CompanyEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    public CompanyEntity(String comment, PostTemplateEntity postTemplateEntity) {
        this.comment = comment;
        this.postTemplateEntity = postTemplateEntity;
    }

    public CompanyEntity(String comment, List<GroupEntity> groupEntities, List<GroupTemplateEntity> groupTemplateEntities, PostTemplateEntity postTemplateEntity) {
        this.comment = comment;
        this.groupEntities = groupEntities;
        this.groupTemplateEntities = groupTemplateEntities;
        this.postTemplateEntity = postTemplateEntity;
    }

    @Enumerated(EnumType.ORDINAL)
    @Column(name = "type")
    private ContentType type = ContentType.RELEASE;
    @Column(name = "comment")
    private String comment;
    @ManyToMany
    @JoinTable(name = "vk_company_groups", joinColumns = @JoinColumn(name = "companyid"), inverseJoinColumns = @JoinColumn(name = "groupid"))
    private List<GroupEntity> groupEntities;
    @ManyToMany
    @JoinTable(name = "vk_company_grouptemplates", joinColumns = @JoinColumn(name = "companyid"), inverseJoinColumns = @JoinColumn(name = "grouptemplateid"))
    private List<GroupTemplateEntity> groupTemplateEntities;
    @ManyToMany
    @JoinTable(name = "vk_company_posts", joinColumns = @JoinColumn(name = "companyid"), inverseJoinColumns = @JoinColumn(name = "postid"))
    private List<PostEntity> postEntities;
    @ManyToOne
    @JoinColumn(name = "posttemplateid", nullable = false, foreignKey = @ForeignKey(name = "fk_vk_company"))
    private PostTemplateEntity postTemplateEntity;
}
