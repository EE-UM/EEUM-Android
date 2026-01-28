package com.example.eeum.data

data class ReportCommentRequest(
    val commentId: Long,
    val reportedUserId: Long,
    val reportReason: String
)

data class ReportCommentResponse(
    val reporterUserId: Long,
    val reportedUserId: Long,
    val reportedCommentId: Long,
    val reportReason: String,
    val reportTime: String
)
