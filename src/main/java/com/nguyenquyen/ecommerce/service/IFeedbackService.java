package com.nguyenquyen.ecommerce.service;

import com.nguyenquyen.ecommerce.dto.request.FeedbackCreateRequest;
import com.nguyenquyen.ecommerce.dto.response.FeedbackResponse;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface IFeedbackService {
    List<FeedbackResponse> getAllFeedbacks(Long productId, Pageable pageable);
    FeedbackResponse getFeedbackById(Long id);
    FeedbackResponse createFeedback(FeedbackCreateRequest request);
}
