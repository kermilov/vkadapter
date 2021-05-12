package ru.kermilov.targetaudience.vkadapter.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import ru.kermilov.targetaudience.vkadapter.domain.GroupEntity;
import ru.kermilov.targetaudience.vkadapter.domain.PostEntity;
import ru.kermilov.targetaudience.vkadapter.domain.PostTemplateEntity;
import ru.kermilov.targetaudience.vkadapter.service.GroupService;
import ru.kermilov.targetaudience.vkadapter.service.PostService;
import ru.kermilov.targetaudience.vkadapter.service.PostTemplateService;

@RestController
@RequiredArgsConstructor
public class QuickInsertController {
    private final GroupService groupService;
    private final PostTemplateService postTemplateService;
    private final PostService postService;

    @PostMapping("/quickinsert/group")
    public ResponseEntity<GroupEntity> quickInsertGroup(@RequestParam(name = "url") String url) {
        return ResponseEntity.of(Optional.of(groupService.quickInsert(url)));
    }

    @PostMapping("/quickinsert/posttemplate")
    public ResponseEntity<PostTemplateEntity> quickInsertPostTemplate(@RequestParam(name = "url") String url) {
        return ResponseEntity.of(Optional.of(postTemplateService.quickInsert(url)));
    }

    @PostMapping("/quickinsert/post")
    public ResponseEntity<PostEntity> quickInsertPost(@RequestParam(name = "url") String url) {
        return ResponseEntity.of(Optional.of(postService.quickInsert(url)));
    }

    @PostMapping("/refresh/post")
    public ResponseEntity<List<PostEntity>> refreshPost() {
        return ResponseEntity.of(Optional.of(postService.refresh(postService.findAll())));
    }

}
