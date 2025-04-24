package com.eighttoten.community.event.reply;

import com.eighttoten.community.domain.reply.Reply;
import lombok.Getter;

@Getter
public abstract class ReplyStatsUpdateEvent {
    protected final Long replyId;

    protected ReplyStatsUpdateEvent(Long replyId) {
        this.replyId = replyId;
    }

    public abstract void execute(Reply reply);
}
