package com.eighttoten.community.event.post;

import com.eighttoten.community.domain.post.Post;

public class PostScrapAddEvent extends PostStatsUpdateEvent {

    public PostScrapAddEvent(Long postId) {
        super(postId);
    }

    @Override
    public void execute(Post post) {
        post.addScrap();
    }
}
