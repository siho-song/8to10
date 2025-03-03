package com.eighttoten.community.event.post;

import com.eighttoten.community.domain.post.Post;
import lombok.Getter;

@Getter
public abstract class PostStatsUpdateEvent {
    protected final Long postId;

    public PostStatsUpdateEvent(Long id) {
        this.postId = id;
    }

    public abstract void execute(Post post);
}
