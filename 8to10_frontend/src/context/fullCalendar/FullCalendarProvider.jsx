import {useEffect, useState} from "react";
import {formatFixedSchedule, formatNormalSchedule, formatVariableSchedule} from "@/helpers/ScheduleFormatter.js";
import authenticatedApi from "@/api/AuthenticatedApi.js";
import {API_ENDPOINT_NAMES} from "@/constants/ApiEndPoints.js";
import {CalendarContext} from "@/context/fullCalendar/FullCalendarContext.jsx";
import {isSuccess} from "@/helpers/AxiosHelper.js";
import {LOAD_SCHEDULES} from "@/constants/ErrorMessage.js";

export const FullCalendarProvider = ({children}) => {
    const date = new Date();
    const year = date.getFullYear();
    const month = date.getMonth() + 1;

    const [events, setEvents] = useState([]);
    const [ym, setYm] = useState({year: year, month: month});

    // 연, 월로 조회하도록 수정 필요
    const loadCalendarEvents = async () => {
        const url = `/schedule/${ym.year}/${ym.month}`;
        const response = await authenticatedApi.get(
            url,
            {
                apiEndPoint: API_ENDPOINT_NAMES.GET_EVENTS,
            });
        if (await isSuccess(response)) {
            const data = response.data;

            const formattedEvents = data.items.map((event) => {
                if (event.type === "normal") {
                    return formatNormalSchedule(event);
                } else if (event.type === "variable") {
                    return formatVariableSchedule(event);
                } else if (event.type === "fixed") {
                    return formatFixedSchedule(event);
                }
            });

            setEvents(formattedEvents);
        } else {
            alert(LOAD_SCHEDULES.FAIL);
        }
    }


    useEffect(() => {
        loadCalendarEvents();
    }, []);

    const addEvent = (event) => {
        setEvents((prevEvents) => [...prevEvents, event]);
    };

    const deleteEvent = (id) => {
        setEvents((prevEvents) =>
            prevEvents.filter((event) => event.id !== id)
        );
    };

    const deleteEventsByGroupId = (groupId) => {
        setEvents((prevEvents) =>
            prevEvents.filter((event) => event.groupId !== groupId)
        );
    }

    const deleteEventsAfterDateByGroupId = (groupId, date) => {
        setEvents((prevEvents) =>
            prevEvents.filter((event) => {
                if (event.groupId !== groupId) return true;
                const eventDate = new Date(event.start);
                return eventDate < date;
            })
        );
    };

    const updateExtendedProps = (id, keys, values) => {
        if (keys.length !== values.length) {
            console.error("Keys and values must have the same length.");
            return;
        }

        setEvents((prevEvents) =>
            prevEvents.map((event) =>
                id === event.id
                    ? {
                        ...event,
                        extendedProps: {
                            ...event.extendedProps,
                            ...keys.reduce((acc, key, index) => {
                                acc[key] = values[index];
                                return acc;
                            }, {}),
                        },
                    }
                    : event
            )
        );
    };

    const updateProps = (id, keys, values) => {
        setEvents((prevEvents) =>
            prevEvents.map((event) =>
                id === event.id
                    ? {
                        ...event,
                        ...keys.reduce((acc, key, index) => {
                            acc[key] = values[index];
                            return acc;
                        }, {}),
                    }
                    : event
            )
        )
    }

    const updatePropsByGroupId = (groupId, keys, values) => {
        setEvents((prevEvents) =>
            prevEvents.map((event) =>
                event.groupId === groupId
                    ? {
                        ...event,
                        ...keys.reduce((acc, key, index) => {
                            acc[key] = values[index];
                            return acc;
                        }, {}),
                    }
                    : event
            )
        );
    };

    const updateExtendedPropsByGroupId = (groupId, keys, values) => {
        setEvents((prevEvents) =>
            prevEvents.map((event) =>
                event.groupId === groupId
                    ? {
                        ...event,
                        extendedProps: {
                            ...event.extendedProps,
                            ...keys.reduce((acc, key, index) => {
                                acc[key] = values[index];
                                return acc;
                            }, {}),
                        },
                    }
                    : event
            )
        );
    };

    const countEventsByGroupId = (groupId) => {
        return events.filter(event => event.groupId === groupId).length;
    };

    const getEarliestStartByGroupId = (groupId) => {
        const groupEvents = events.filter(event => event.groupId === groupId);
        if (groupEvents.length === 0) {
            console.warn(`No events found for groupId: ${groupId}`);
            return null;
        }
        const earliestEvent = groupEvents.reduce((earliest, event) => {
            const eventStart = new Date(event.start);
            const earliestStart = new Date(earliest.start);
            return eventStart < earliestStart ? event : earliest;
        });
        return earliestEvent.start;
    };


    return (
        <CalendarContext.Provider value={
            {events,
                loadCalendarEvents,
                addEvent,
                updateExtendedProps,
                updateProps,
                deleteEvent,
                deleteEventsByGroupId,
                deleteEventsAfterDateByGroupId,
                updatePropsByGroupId,
                updateExtendedPropsByGroupId,
                countEventsByGroupId,
                getEarliestStartByGroupId,
            }}>
            {children}
        </CalendarContext.Provider>
    );
};