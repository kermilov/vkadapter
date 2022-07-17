package ru.kermilov.targetaudience.vkadapter.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import ru.kermilov.targetaudience.vkadapter.domain.CompanyEntity;
import ru.kermilov.targetaudience.vkadapter.domain.GroupEntity;
import ru.kermilov.targetaudience.vkadapter.domain.PostEntity;
import ru.kermilov.targetaudience.vkadapter.domain.PostTemplateEntity;
import ru.kermilov.targetaudience.vkadapter.service.CompanyService;
import ru.kermilov.targetaudience.vkadapter.service.GroupService;
import ru.kermilov.targetaudience.vkadapter.service.PostService;
import ru.kermilov.targetaudience.vkadapter.service.PostTemplateService;

import static java.util.stream.Collectors.toList;

@RestController
@RequiredArgsConstructor
public class QuickInsertController {
    private final CompanyService companyService;
    private final GroupService groupService;
    private final PostTemplateService postTemplateService;
    private final PostService postService;

    @PostMapping("/quickinsert/company")
    public ResponseEntity<CompanyEntity> quickInsertCompany(@RequestParam(name = "url") String url) {
        return ResponseEntity.of(Optional.of(companyService.quickInsert(url)));
    }

    @PostMapping("/quickinsert/group")
    public ResponseEntity<GroupEntity> quickInsertGroup(@RequestParam(name = "url") String url) {
        return ResponseEntity.of(Optional.of(groupService.quickInsert(url)));
    }

    @PostMapping("/quickinsert/groups")
    public ResponseEntity<List<GroupEntity>> quickInsertGroups(@RequestParam(name = "urls") String urls) {
        return ResponseEntity.of(Optional.of(Arrays.stream(urls.split(" "))
            .map(groupService::quickInsert)
            .filter(Objects::nonNull)
            .collect(toList())));
    }

    @PostMapping("/quickinsert/posttemplate")
    public ResponseEntity<PostTemplateEntity> quickInsertPostTemplate(@RequestParam(name = "url") String url) {
        return ResponseEntity.of(Optional.of(postTemplateService.quickInsert(url)));
    }

    @PostMapping("/quickinsert/posttemplates")
    public ResponseEntity<List<PostTemplateEntity>> quickInsertPostTemplates(@RequestParam(name = "urls") String urls) {
        return ResponseEntity.of(Optional.of(Arrays.stream(urls.split(" "))
            .map(postTemplateService::quickInsert)
            .filter(Objects::nonNull)
            .collect(toList())));
    }

    @PostMapping("/quickinsert/post")
    public ResponseEntity<PostEntity> quickInsertPost(@RequestParam(name = "url") String url) {
        return ResponseEntity.of(Optional.of(postService.quickInsert(url)));
    }

    @PostMapping("/quickinsert/posts")
    public ResponseEntity<List<PostEntity>> quickInsertPosts(@RequestParam(name = "urls") String urls) {
        return ResponseEntity.of(Optional.of(Arrays.stream(urls.split(" "))
            .map(postService::quickInsert)
            .filter(Objects::nonNull)
            .collect(toList())));
    }

    @PostMapping("/refresh/post")
    public ResponseEntity<List<PostEntity>> refreshPost() {
        return ResponseEntity.of(Optional.of(postService.refresh()));
    }

    @DeleteMapping("/rollback/post")
    public ResponseEntity<Void> rollbackPost() {
        postService.rollback();
        return ResponseEntity.ok().build();
    }

}
