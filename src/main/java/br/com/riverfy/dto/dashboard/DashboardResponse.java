package br.com.riverfy.dto.dashboard;

import java.util.List;

public record DashboardResponse(
        long totalMembers,
        long newMembersThisMonth,

        long activeEvents,
        long upcomingEventsCount,

        long publishedNotices,
        long noticesThisWeek,

        long totalDevotionals,
        long totalDevotionalPages,

        List<RecentEventDTO> upcomingEvents,
        List<RecentMemberDTO> recentMembers,
        List<RecentNoticeDTO> recentNotices
) {
}
