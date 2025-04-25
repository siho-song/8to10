import {formatDate} from "@/helpers/TimeFormatter.js";

export const generateEventIdWithParentId = (event) => {
    if (event.parentId) {
        return `${event.type}-${event.parentId}-${event.id}`;
    }
    return `${event.type}-${event.id}`;
};

export const parseEventIdWithParentId = (event) => {
    if (event.extendedProps.parentId) {
        return Number(event.id.split("-").pop());
    }
    return event.id;
}

export const formatNormalSchedule = (event) => {
    let achievedAmount;

    if(event.dailyAmount) {
        if (event.completeStatus) {
            achievedAmount = event.dailyAmount;
        } else {
            achievedAmount = 0;
        }
    } else {
        achievedAmount = NaN;
    }
    return {
        id: generateEventIdWithParentId(event),
        groupId: `n${event.parentId}`,
        title: event.title,
        start: event.startDateTime,
        end: event.endDateTime,
        // color: event.color,
        color: "#b1d0a9",
        extendedProps: {
            type: event.type,
            parentId: event.parentId,
            commonDescription: event.commonDescription,
            detailDescription: event.detailDescription || "",
            bufferTime : event.bufferTime,
            completeStatus: event.completeStatus,
            isComplete: event.completeStatus,
            dailyAmount : event.dailyAmount,
            achievedAmount: event.achievedAmount,
            originId: event.id,
        }
    };
}

export const formatVariableSchedule = (event) => {
    return {
        id: generateEventIdWithParentId(event),
        groupId: `v${event.id}`,
        title: event.title,
        start: event.startDateTime,
        end: event.endDateTime,
        color: "#f5d0cc",
        extendedProps: {
            type: event.type,
            commonDescription: event.commonDescription,
            originId: event.id,
        },
    };
}

export const formatFixedSchedule = (event) => {
    return {
        id: generateEventIdWithParentId(event),
        groupId: `f${event.parentId}`,
        title: event.title,
        start: event.startDateTime,
        end: event.endDateTime,
        // color: event.color,
        color: "#cad9fa",
        extendedProps: {
            type: event.type,
            commonDescription: event.commonDescription,
            detailDescription: event.detailDescription || "",
            originId: event.id,
            parentId: event.parentId,
        },
    };
}

export const formatTodoEvent = (event) => {
    if (!event) return {};

    return {
        scheduleDetailId: event.extendedProps.originId,
        completeStatus: event.extendedProps.isComplete,
        ...(event.extendedProps.dailyAmount && {
            achievedAmount: event.extendedProps.achievedAmount,
        }),
    };
};

export const formatTodoEventsSubmit = (date, events) => {
    if (!events) return {};
    const data = {
        date: formatDate(date),
        progressUpdates: [],
    };

    for (let event of events) {
        data.progressUpdates.push(formatTodoEvent(event));
    }
    return data;
};
