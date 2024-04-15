package com.example.lucky7postservice.src.comment.api;

import com.example.lucky7postservice.src.comment.application.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/posts")
public class CommentController {
    private final CommentService commentService;


}
