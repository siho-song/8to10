package com.eighttoten.community.event.post;

import com.eighttoten.community.domain.post.Post;

public class PostHeartAddEvent extends PostStatsUpdateEvent {

    public PostHeartAddEvent(Long boardId) {
        super(boardId);
    }

    @Override
    public void execute(Post post) {
        post.addLike();
    }
}
