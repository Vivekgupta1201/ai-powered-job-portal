package com.jobportal.user.service;

import com.jobportal.user.dto.CreateJobSeekerProfileRequest;
import com.jobportal.user.dto.CreateRecruiterProfileRequest;
import com.jobportal.user.dto.ProfileResponse;
import com.jobportal.user.dto.UpdateJobSeekerProfileRequest;
import com.jobportal.user.dto.UpdateRecruiterProfileRequest;
import com.jobportal.user.dto.UserProfileResponse;

public interface UserProfileService {

    ProfileResponse createJobSeekerProfile(String email, CreateJobSeekerProfileRequest request);

    ProfileResponse createRecruiterProfile(String email, CreateRecruiterProfileRequest request);

    UserProfileResponse getMyProfile(String email);
    
    ProfileResponse updateJobSeekerProfile(String email, UpdateJobSeekerProfileRequest request);

    ProfileResponse updateRecruiterProfile(String email, UpdateRecruiterProfileRequest request);
}
