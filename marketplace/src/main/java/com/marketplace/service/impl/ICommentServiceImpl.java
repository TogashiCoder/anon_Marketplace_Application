package com.marketplace.service.impl;

import com.marketplace.dto.CommentDto;
import com.marketplace.exception.ResourceNotFoundException;
import com.marketplace.mapper.CommentMapper;
import com.marketplace.model.Buyer;
import com.marketplace.model.Comment;
import com.marketplace.model.Product;
import com.marketplace.repository.BuyerRepository;
import com.marketplace.repository.CommentRepository;
import com.marketplace.repository.ProductRepository;
import com.marketplace.service.ICommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ICommentServiceImpl implements ICommentService {



    private final CommentRepository commentRepository;
    private final CommentMapper commentMapper;
    private final BuyerRepository buyerRepository;
    private final ProductRepository productRepository;

    @Override
    public CommentDto createComment(CommentDto commentDto) {
        // Find Buyer and Product by ID
        Buyer buyer = buyerRepository.findById(commentDto.getBuyerId())
                .orElseThrow(() -> new ResourceNotFoundException("Buyer", "id", commentDto.getBuyerId().toString()));

        Product product = productRepository.findById(commentDto.getProductId())
                .orElseThrow(() -> new ResourceNotFoundException("Product", "id", commentDto.getProductId().toString()));

        // Map DTO to Entity
        Comment comment = commentMapper.toEntity(commentDto);
        comment.setBuyer(buyer); // Set Buyer
        comment.setProduct(product); // Set Product

        // Save Comment
        Comment savedComment = commentRepository.save(comment);
        return commentMapper.toDto(savedComment);
    }

    @Override
    public CommentDto getCommentById(Long id) {
        Comment comment = commentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Comment", "id", id.toString()));
        return commentMapper.toDto(comment);
    }

    @Override
    public List<CommentDto> getAllComments() {
        return commentRepository.findAll().stream()
                .map(commentMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public CommentDto updateComment(Long id, CommentDto commentDto) {
        Comment existingComment = commentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Comment", "id", id.toString()));

        // Find Buyer and Product by ID
        Buyer buyer = buyerRepository.findById(commentDto.getBuyerId())
                .orElseThrow(() -> new ResourceNotFoundException("Buyer", "id", commentDto.getBuyerId().toString()));

        Product product = productRepository.findById(commentDto.getProductId())
                .orElseThrow(() -> new ResourceNotFoundException("Product", "id", commentDto.getProductId().toString()));

        // Update fields
        existingComment.setContent(commentDto.getContent());
        existingComment.setRating(commentDto.getRating());
        existingComment.setBuyer(buyer); // Set updated Buyer
        existingComment.setProduct(product); // Set updated Product

        Comment updatedComment = commentRepository.save(existingComment);
        return commentMapper.toDto(updatedComment);
    }

    @Override
    public void deleteComment(Long id) {
        Comment comment = commentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Comment", "id", id.toString()));
        commentRepository.delete(comment);
    }


    @Override
    public List<CommentDto> getCommentsByProductId(Long productId) {
        List<Comment> comments = commentRepository.findAllByProductId(productId);
        return comments.stream()
                .map(commentMapper::toDto)
                .sorted((c1, c2) -> c2.getCreatedAt().compareTo(c1.getCreatedAt())) // Sort by createdAt from new to old
                .collect(Collectors.toList());
    }



}
