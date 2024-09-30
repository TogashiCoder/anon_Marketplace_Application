package com.marketplace.controller;

import com.marketplace.dto.CommentDto;
import com.marketplace.service.ICommentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/comments")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class CommentController {


    private final ICommentService commentService;

    @PostMapping("/new")
    public ResponseEntity<CommentDto> createComment(@Valid @RequestBody CommentDto commentDto) {
        CommentDto createdComment = commentService.createComment(commentDto);
        return ResponseEntity.status(201).body(createdComment);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CommentDto> getCommentById(@PathVariable Long id) {
        CommentDto commentDto = commentService.getCommentById(id);
        return ResponseEntity.ok(commentDto);
    }

    @GetMapping("/all")
    public ResponseEntity<List<CommentDto>> getAllComments() {
        List<CommentDto> comments = commentService.getAllComments();
        return ResponseEntity.ok(comments);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<CommentDto> updateComment(
            @PathVariable Long id,
            @RequestBody CommentDto commentDto) {
        CommentDto updatedComment = commentService.updateComment(id, commentDto);
        return ResponseEntity.ok(updatedComment);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteComment(@PathVariable Long id) {
        commentService.deleteComment(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/product/{id}")
    public ResponseEntity<List<CommentDto>> getCommentsByProductId(@PathVariable Long id) {
        List<CommentDto> comments = commentService.getCommentsByProductId(id);
        return ResponseEntity.ok(comments);
    }


}