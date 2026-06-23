package br.com.riverfy.dto.dashboard;

import br.com.riverfy.model.User;

import java.time.LocalDateTime;

public record RecentMemberDTO(
        Long id,
        String name,
        LocalDateTime createdAt
) {
    public static RecentMemberDTO fromEntity(User user) {
        return new RecentMemberDTO(
                user.getId(),
                user.getName(),
                user.getCreatedAt()
        );
    }
}
