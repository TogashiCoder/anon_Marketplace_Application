package com.marketplace.service;

import com.marketplace.dto.CommentDto;

import java.util.List;

public interface ICommentService {

    CommentDto createComment(CommentDto commentDto);
    CommentDto getCommentById(Long id);
    List<CommentDto> getAllComments();
    CommentDto updateComment(Long id, CommentDto commentDto);
    void deleteComment(Long id);
    List<CommentDto> getCommentsByProductId(Long productId);

}
