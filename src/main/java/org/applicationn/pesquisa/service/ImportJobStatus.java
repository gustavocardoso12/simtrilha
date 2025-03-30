package org.applicationn.pesquisa.service;

import java.io.Serializable;
import java.time.LocalDateTime;

public class ImportJobStatus implements Serializable {
    private static final long serialVersionUID = 1L;

    private String jobId;
    private String status; // e.g., PENDING, RUNNING, COMPLETED, FAILED
    private String resultMessage;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private String fileName;
    // Opcional: int progress;

    public ImportJobStatus(String jobId, String fileName) {
        this.jobId = jobId;
        this.fileName = fileName;
        this.status = "PENDING";
        this.startTime = LocalDateTime.now();
    }

    // Getters and Setters
    public String getJobId() {
        return jobId;
    }

    public void setJobId(String jobId) {
        this.jobId = jobId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getResultMessage() {
        return resultMessage;
    }

    public void setResultMessage(String resultMessage) {
        this.resultMessage = resultMessage;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public boolean isRunning() {
        return "PENDING".equals(status) || "RUNNING".equals(status);
    }

    public boolean isCompleted() {
        return "COMPLETED".equals(status);
    }

    public boolean isFailed() {
        return "FAILED".equals(status);
    }
}