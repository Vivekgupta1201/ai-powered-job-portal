package com.jobportal.user.service.impl;

import com.jobportal.auth.entity.User;
import com.jobportal.auth.repository.UserRepository;
import com.jobportal.company.entity.Company;
import com.jobportal.company.repository.CompanyRepository;
import com.jobportal.common.exception.ConflictException;
import com.jobportal.common.exception.NotFoundException;
import com.jobportal.user.dto.CreateJobSeekerProfileRequest;
import com.jobportal.user.dto.CreateRecruiterProfileRequest;
import com.jobportal.user.dto.ProfileResponse;
import com.jobportal.user.dto.UpdateJobSeekerProfileRequest;
import com.jobportal.user.dto.UpdateRecruiterProfileRequest;
import com.jobportal.user.dto.UserProfileResponse;
import com.jobportal.user.entity.JobSeekerProfile;
import com.jobportal.user.entity.RecruiterProfile;
import com.jobportal.user.repository.JobSeekerProfileRepository;
import com.jobportal.user.repository.RecruiterProfileRepository;
import com.jobportal.user.service.UserProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserProfileServiceImpl implements UserProfileService {

    private final UserRepository userRepository;
    private final JobSeekerProfileRepository jobSeekerProfileRepository;
    private final RecruiterProfileRepository recruiterProfileRepository;
    private final CompanyRepository companyRepository;

    @Override
    public ProfileResponse createJobSeekerProfile(String email, CreateJobSeekerProfileRequest request) {
        User user = findUserByEmail(email);

        if (jobSeekerProfileRepository.existsByUserId(user.getId())) {
            throw new ConflictException("Job seeker profile already exists");
        }

        JobSeekerProfile profile = new JobSeekerProfile();
        profile.setUser(user);
        profile.setFirstName(request.getFirstName());
        profile.setLastName(request.getLastName());
        profile.setPhone(request.getPhone());
        profile.setExperienceYears(request.getExperienceYears());
        profile.setCurrentLocation(request.getCurrentLocation());
        profile.setHeadline(request.getHeadline());
        profile.setSummary(request.getSummary());

        jobSeekerProfileRepository.save(profile);

        return new ProfileResponse(
                "Job seeker profile created successfully",
                user.getEmail(),
                "JOB_SEEKER"
        );
    }

    @Override
    public ProfileResponse createRecruiterProfile(String email, CreateRecruiterProfileRequest request) {
        User user = findUserByEmail(email);

        if (recruiterProfileRepository.existsByUserId(user.getId())) {
            throw new ConflictException("Recruiter profile already exists");
        }

        Company company = new Company();
        company.setName(request.getCompanyName());
        company.setWebsite(request.getCompanyWebsite());
        company.setDescription(request.getCompanyDescription());

        Company savedCompany = companyRepository.save(company);

        RecruiterProfile profile = new RecruiterProfile();
        profile.setUser(user);
        profile.setCompany(savedCompany);
        profile.setFirstName(request.getFirstName());
        profile.setLastName(request.getLastName());
        profile.setDesignation(request.getDesignation());

        recruiterProfileRepository.save(profile);

        return new ProfileResponse(
                "Recruiter profile created successfully",
                user.getEmail(),
                "RECRUITER"
        );
    }

    @Override
    public UserProfileResponse getMyProfile(String email) {
        User user = findUserByEmail(email);

        return jobSeekerProfileRepository.findByUserId(user.getId())
                .map(profile -> buildJobSeekerResponse(user, profile))
                .orElseGet(() -> recruiterProfileRepository.findByUserId(user.getId())
                        .map(profile -> buildRecruiterResponse(user, profile))
                        .orElseThrow(() -> new NotFoundException("Profile not found for this user")));
    }

    private User findUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new NotFoundException("User not found"));
    }

    private UserProfileResponse buildJobSeekerResponse(User user, JobSeekerProfile profile) {
        return new UserProfileResponse(
                "JOB_SEEKER",
                user.getEmail(),
                profile.getFirstName(),
                profile.getLastName(),
                profile.getPhone(),
                profile.getExperienceYears(),
                profile.getCurrentLocation(),
                profile.getHeadline(),
                profile.getSummary(),
                null,
                null,
                null,
                null
        );
    }

    private UserProfileResponse buildRecruiterResponse(User user, RecruiterProfile profile) {
        return new UserProfileResponse(
                "RECRUITER",
                user.getEmail(),
                profile.getFirstName(),
                profile.getLastName(),
                null,
                null,
                null,
                null,
                null,
                profile.getDesignation(),
                profile.getCompany().getName(),
                profile.getCompany().getWebsite(),
                profile.getCompany().getDescription()
        );
    }

	@Override
	public ProfileResponse updateJobSeekerProfile(String email, UpdateJobSeekerProfileRequest request) {
		
		 User user = findUserByEmail(email);

		    JobSeekerProfile profile = jobSeekerProfileRepository.findByUserId(user.getId())
		            .orElseThrow(() -> new NotFoundException("Job seeker profile not found"));

		    profile.setFirstName(request.getFirstName());
		    profile.setLastName(request.getLastName());
		    profile.setPhone(request.getPhone());
		    profile.setExperienceYears(request.getExperienceYears());
		    profile.setCurrentLocation(request.getCurrentLocation());
		    profile.setHeadline(request.getHeadline());
		    profile.setSummary(request.getSummary());

		    jobSeekerProfileRepository.save(profile);

		    return new ProfileResponse(
		            "Job seeker profile updated successfully",
		            user.getEmail(),
		            "JOB_SEEKER"
		    );
		
	}

	@Override
	public ProfileResponse updateRecruiterProfile(String email, UpdateRecruiterProfileRequest request) {
		User user = findUserByEmail(email);

	    RecruiterProfile profile = recruiterProfileRepository.findByUserId(user.getId())
	            .orElseThrow(() -> new NotFoundException("Recruiter profile not found"));

	    profile.setFirstName(request.getFirstName());
	    profile.setLastName(request.getLastName());
	    profile.setDesignation(request.getDesignation());

	    Company company = profile.getCompany();
	    company.setName(request.getCompanyName());
	    company.setWebsite(request.getCompanyWebsite());
	    company.setDescription(request.getCompanyDescription());

	    companyRepository.save(company);
	    recruiterProfileRepository.save(profile);

	    return new ProfileResponse(
	            "Recruiter profile updated successfully",
	            user.getEmail(),
	            "RECRUITER"
	    );
	}
}
