package com.nguyenquyen.ecommerce.controller;

import com.nguyenquyen.ecommerce.dto.ApiResponse;
import com.nguyenquyen.ecommerce.dto.request.feedback.FeedbackCreateRequest;
import com.nguyenquyen.ecommerce.dto.request.feedback.FeedbackUpdateRequest;
import com.nguyenquyen.ecommerce.dto.response.FeedbackResponse;
import com.nguyenquyen.ecommerce.service.IFeedbackService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("${api.prefix}/feedbacks")
@RequiredArgsConstructor
public class FeedbackController {

    private final IFeedbackService feedbackService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<FeedbackResponse>>> getAllFeedbacks(
            @RequestParam(required = false) Long productId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Pageable pageable = PageRequest.of(page, size);
        List<FeedbackResponse> feedbacks = feedbackService.getAllFeedbacks(productId, pageable);

        ApiResponse<List<FeedbackResponse>> response = ApiResponse.<List<FeedbackResponse>>builder()
                .status(HttpStatus.OK)
                .message("Lấy danh sách đánh giá thành công")
                .data(feedbacks)
                .build();

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<FeedbackResponse>> getFeedbackById(@PathVariable Long id) {
        FeedbackResponse feedback = feedbackService.getFeedbackById(id);

        ApiResponse<FeedbackResponse> response = ApiResponse.<FeedbackResponse>builder()
                .status(HttpStatus.OK)
                .message("Lấy thông tin đánh giá thành công")
                .data(feedback)
                .build();

        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<ApiResponse<FeedbackResponse>> createFeedback(
            @Valid @RequestBody FeedbackCreateRequest request) {

        FeedbackResponse feedback = feedbackService.createFeedback(request);

        ApiResponse<FeedbackResponse> response = ApiResponse.<FeedbackResponse>builder()
                .status(HttpStatus.CREATED)
                .message("Tạo đánh giá thành công")
                .data(feedback)
                .build();

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<FeedbackResponse>> updateFeedback(
            @PathVariable Long id,
            @Valid @RequestBody FeedbackUpdateRequest request) {

        FeedbackResponse feedback = feedbackService.updateFeedback(id, request);

        ApiResponse<FeedbackResponse> response = ApiResponse.<FeedbackResponse>builder()
                .status(HttpStatus.OK)
                .message("Cập nhật đánh giá thành công")
                .data(feedback)
                .build();

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteFeedback(@PathVariable Long id) {
        feedbackService.deleteFeedback(id);

        ApiResponse<Void> response = ApiResponse.<Void>builder()
                .status(HttpStatus.OK)
                .message("Xóa đánh giá thành công")
                .build();

        return ResponseEntity.ok(response);
    }
}
