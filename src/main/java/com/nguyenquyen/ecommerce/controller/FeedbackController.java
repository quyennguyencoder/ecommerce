package com.nguyenquyen.ecommerce.controller;

import com.nguyenquyen.ecommerce.dto.ApiResponse;
import com.nguyenquyen.ecommerce.dto.PaginationResponse;
import com.nguyenquyen.ecommerce.dto.request.FeedbackCreateRequest;
import com.nguyenquyen.ecommerce.dto.response.FeedbackResponse;
import com.nguyenquyen.ecommerce.service.IFeedbackService;
import com.nguyenquyen.ecommerce.util.PaginationUtil;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("${api.prefix}/feedbacks")
@RequiredArgsConstructor
public class FeedbackController {

    private final IFeedbackService feedbackService;

    @GetMapping
    public ResponseEntity<ApiResponse<PaginationResponse<FeedbackResponse>>> getAllFeedbacks(
            @RequestParam(required = false) Long productId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Pageable pageable = PageRequest.of(page, size);
        var feedbackPage = feedbackService.getAllFeedbacks(productId, pageable);
        PaginationResponse<FeedbackResponse> paginationResponse = PaginationUtil.toPaginationResponse(feedbackPage);

        ApiResponse<PaginationResponse<FeedbackResponse>> response = ApiResponse.<PaginationResponse<FeedbackResponse>>builder()
                .status(HttpStatus.OK)
                .message("Lấy danh sách đánh giá thành công")
                .data(paginationResponse)
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
}
