package com.nguyenquyen.ecommerce.service;

import com.nguyenquyen.ecommerce.dto.request.FeedbackCreateRequest;
import com.nguyenquyen.ecommerce.dto.response.FeedbackResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface IFeedbackService {
    Page<FeedbackResponse> getAllFeedbacks(Long productId, Pageable pageable);
    FeedbackResponse getFeedbackById(Long id);
    FeedbackResponse createFeedback(FeedbackCreateRequest request);
}
