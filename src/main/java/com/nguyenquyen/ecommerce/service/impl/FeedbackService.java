package com.nguyenquyen.ecommerce.service.impl;

import com.nguyenquyen.ecommerce.dto.request.FeedbackCreateRequest;
import com.nguyenquyen.ecommerce.dto.response.FeedbackResponse;
import com.nguyenquyen.ecommerce.mapper.FeedbackMapper;
import com.nguyenquyen.ecommerce.model.Feedback;
import com.nguyenquyen.ecommerce.model.Product;
import com.nguyenquyen.ecommerce.model.User;
import com.nguyenquyen.ecommerce.repository.FeedbackRepository;
import com.nguyenquyen.ecommerce.repository.ProductRepository;
import com.nguyenquyen.ecommerce.service.IFeedbackService;
import com.nguyenquyen.ecommerce.util.SecurityUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FeedbackService implements IFeedbackService {

    private final FeedbackRepository feedbackRepository;
    private final FeedbackMapper feedbackMapper;
    private final ProductRepository productRepository;
    private final SecurityUtil securityUtil;

    @Override
    public List<FeedbackResponse> getAllFeedbacks(Long productId, Pageable pageable) {
        List<Feedback> feedbacks;
        if (productId != null) {
            feedbacks = feedbackRepository.findByProductId(productId, pageable).getContent();
        } else {
            feedbacks = feedbackRepository.findAll(pageable).getContent();
        }
        return feedbacks.stream()
                .map(feedbackMapper::feedbackToFeedbackResponse)
                .toList();
    }

    @Override
    public FeedbackResponse getFeedbackById(Long id) {
        Feedback feedback = feedbackRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Đánh giá không tồn tại với id: " + id));
        return feedbackMapper.feedbackToFeedbackResponse(feedback);
    }

    @Override
    public FeedbackResponse createFeedback(FeedbackCreateRequest request) {
        Product product = productRepository.findById(request.getProductId())
                .orElseThrow(() -> new RuntimeException("Sản phẩm không tồn tại với id: " + request.getProductId()));

        User user = securityUtil.getCurrentUser();

        Feedback feedback = Feedback.builder()
                .star(request.getStar())
                .content(request.getContent())
                .product(product)
                .user(user)
                .build();

        Feedback savedFeedback = feedbackRepository.save(feedback);
        return feedbackMapper.feedbackToFeedbackResponse(savedFeedback);
    }
}
