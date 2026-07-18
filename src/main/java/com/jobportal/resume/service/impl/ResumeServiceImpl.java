package com.jobportal.resume.service.impl;

import com.jobportal.auth.entity.User;
import com.jobportal.auth.repository.UserRepository;
import com.jobportal.common.exception.ConflictException;
import com.jobportal.common.exception.NotFoundException;
import com.jobportal.resume.dto.ResumeResponse;
import com.jobportal.resume.dto.ResumeUploadResponse;
import com.jobportal.resume.dto.ResumeUploadUrlResponse;
import com.jobportal.resume.entity.Resume;
import com.jobportal.resume.repository.ResumeRepository;
import com.jobportal.resume.service.ResumeService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ResumeServiceImpl implements ResumeService {

    private final ResumeRepository resumeRepository;
    private final UserRepository userRepository;

    @Value("${app.resume.upload-dir:uploads/resumes}")
    private String uploadDir;

    @Override
    public ResumeUploadUrlResponse createUploadTarget(String email, String fileName) {
        User user = findUser(email);
        String storageKey = user.getId() + "/" + UUID.randomUUID() + "-" + sanitize(fileName);
        return new ResumeUploadUrlResponse(uploadDir + "/" + storageKey, storageKey);
    }

    @Override
    @Transactional
    public ResumeUploadResponse saveResume(String email, MultipartFile file, String storageKey) {
        User user = findUser(email);
        try {
            Path path = Paths.get(uploadDir, storageKey);
            Files.createDirectories(path.getParent());
            Files.write(path, file.getBytes());
        } catch (IOException e) {
            throw new IllegalStateException("Unable to store resume file", e);
        }

        Resume resume = new Resume();
        resume.setJobSeeker(user);
        resume.setFileName(file.getOriginalFilename());
        resume.setStorageKey(storageKey);
        resume.setContentType(file.getContentType());
        resume.setFileSize(file.getSize());

        Resume saved = resumeRepository.save(resume);
        return new ResumeUploadResponse(saved.getId(), "Resume uploaded successfully", saved.getFileName(), saved.getStorageKey());
    }

    @Override
    public List<ResumeResponse> listMyResumes(String email) {
        User user = findUser(email);
        return resumeRepository.findByJobSeekerId(user.getId()).stream().map(this::toResponse).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public ResumeResponse activateResume(String email, UUID resumeId) {
        User user = findUser(email);
        Resume resume = resumeRepository.findByIdAndJobSeekerId(resumeId, user.getId())
                .orElseThrow(() -> new NotFoundException("Resume not found"));
        resumeRepository.findByJobSeekerIdAndActiveTrue(user.getId()).ifPresent(active -> {
            active.setActive(false);
            resumeRepository.save(active);
        });
        resume.setActive(true);
        return toResponse(resumeRepository.save(resume));
    }

    @Override
    public ResumeResponse getResume(String email, UUID resumeId) {
        User user = findUser(email);
        Resume resume = resumeRepository.findByIdAndJobSeekerId(resumeId, user.getId())
                .orElseThrow(() -> new NotFoundException("Resume not found"));
        return toResponse(resume);
    }

    private User findUser(String email) {
        return userRepository.findByEmail(email).orElseThrow(() -> new NotFoundException("User not found"));
    }

    private ResumeResponse toResponse(Resume resume) {
        return new ResumeResponse(
                resume.getId(),
                resume.getFileName(),
                resume.getContentType(),
                resume.getFileSize(),
                resume.getActive(),
                resume.getUploadedAt().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME),
                resume.getStorageKey()
        );
    }

    private String sanitize(String fileName) {
        return fileName == null ? "resume" : fileName.replaceAll("[^a-zA-Z0-9._-]", "_");
    }
}
