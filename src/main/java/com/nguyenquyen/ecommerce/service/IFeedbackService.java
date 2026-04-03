package com.nguyenquyen.ecommerce.service;

import com.nguyenquyen.ecommerce.dto.PaginationResponse;
import com.nguyenquyen.ecommerce.dto.request.FeedbackCreateRequest;
import com.nguyenquyen.ecommerce.dto.response.FeedbackResponse;
import org.springframework.data.domain.Page;

public interface IFeedbackService {
    PaginationResponse<FeedbackResponse> getAllFeedbacks(Long productId, int page, int size);
    FeedbackResponse getFeedbackById(Long id);
    FeedbackResponse createFeedback(FeedbackCreateRequest request);
}
