import {useEffect, useRef, useState} from "react";
import { EventSourcePolyfill } from "event-source-polyfill";

import {buildBearerToken} from "@/helpers/TokenManager.js";
import NotificationsIcon from '@mui/icons-material/Notifications';
import "@/styles/notification/Notification.css";
import NotificationPopup from "@/components/notification/NotificationPopup.jsx";
import {formatDateTimeSimple} from "@/helpers/TimeFormatter.js";
import authenticatedApi from "@/api/AuthenticatedApi.js";
import {API_ENDPOINT_NAMES} from "@/constants/ApiEndPoints.js";
import {useLocation} from "react-router-dom";

const originalConsoleError = console.error;
console.error = (...args) => {
    const shouldIgnore = args.some(arg => {
        return arg.toString().includes('No activity within') || arg.toString().includes('TypeError');
    });

    if (!shouldIgnore) {
        originalConsoleError(...args);
    }
};

const Notification = () => {

    const location = useLocation();

    const [notifications, setNotifications] = useState(JSON.parse(localStorage.getItem('notifications')) || []);
    const [unreadCount, setUnreadCount] = useState(
        notifications.filter(notification => !notification.isRead).length
    );
    const [isPopupVisible, setPopupVisible] = useState(false);
    const eventSourceRef = useRef(null);
    const retryCountRef = useRef(0);

    const API_BASE_URL = import.meta.env.VITE_API_URL;
    const DEFAULT_RETRY_MESSAGE = "No activity within";

    const startNotificationConnection = () => {
        const accessToken = localStorage.getItem('Authorization');
        const lastEventId = localStorage.getItem('Last-Event-ID') || null;
        const MAX_RETRIES = 3;
        const BACKOFF = 5000;

        eventSourceRef.current = new EventSourcePolyfill(`${API_BASE_URL}/notification/subscribe`, {
            headers: {
                Authorization: buildBearerToken(accessToken),
                ...(lastEventId && { "Last-Event-ID": lastEventId }),
            },
        });

        eventSourceRef.current.onopen = () => {
            console.log("SSE 연결 성공");
        }

        eventSourceRef.current.addEventListener("notification", (event) => {
            const newNotificationData = JSON.parse(event.data);

            const newNotification = {
                ...newNotificationData,
                receivedAt: formatDateTimeSimple(new Date()),
                isRead: false,
            };

            if (event.lastEventId) {
                localStorage.setItem("Last-Event-ID", event.lastEventId);
            }

            setNotifications((prevNotifications) => {
                const isDuplicate = prevNotifications.some(
                    (notification) => notification.entityId === newNotification.entityId
                );

                if (isDuplicate) return prevNotifications;

                const updatedNotifications = [...prevNotifications, newNotification];
                localStorage.setItem("notifications", JSON.stringify(updatedNotifications));

                return updatedNotifications;
            });

            setUnreadCount((prevCount) => prevCount + 1);
        });

        eventSourceRef.current.onerror = (event) => {
            if (eventSourceRef.current){
                eventSourceRef.current.close();
            }

            if (event.error.message.toString().includes(DEFAULT_RETRY_MESSAGE)) {
                setTimeout(() => startNotificationConnection(), BACKOFF);
            }
            else {
                retryCountRef.current += 1;

                if (retryCountRef.current > MAX_RETRIES) {
                    console.error("최대 재시도 횟수를 초과했습니다.");
                    return;
                }

                const backoffDelay = BACKOFF * Math.pow(5, retryCountRef.current);
                setTimeout(() => startNotificationConnection(), backoffDelay);
            }
        }
    }

    const handleNotificationClick = () => {
        setPopupVisible(true);
    };

    const handlePopupClose = () => {
        setPopupVisible(false);
    };

    const handleNotificationRemove = async (entityId) => {
        let isRead = false;

        const updatedNotifications = notifications.filter(
            (notification) => {
                const shouldInclude = notification.entityId !== entityId;
                if (!shouldInclude) {
                    isRead = notification.isRead;
                }
                return shouldInclude;
            }
        );


        const url = `/notification/${entityId}`
        const response = await authenticatedApi.delete(
            url,
            {apiEndPoint: API_ENDPOINT_NAMES.DELETE_NOTIFICATION,}
        )

        setNotifications(updatedNotifications);
        localStorage.setItem("notifications", JSON.stringify(updatedNotifications));

        if (!isRead){
            setUnreadCount((prevCount) => prevCount - 1);
        }
    };

    const markNotificationAsRead = (entityId) => {
        const updatedNotifications = notifications.map((notification) => {
            if (notification.entityId === entityId) {
                return { ...notification, isRead: true };
            }
            return notification;
        });

        setNotifications(updatedNotifications);
        localStorage.setItem("notifications", JSON.stringify(updatedNotifications));
    };

    useEffect(() => {
        startNotificationConnection();

        return () => {
            if (eventSourceRef.current) {
                eventSourceRef.current.close();
                console.log("SSE 연결 해제");
            }
        };
    }, []);

    useEffect(() => {
        handlePopupClose();
    }, [location]);

    return (
        <div className="image-container" data-alt="알림">
            <button
                className={`image-link notification-button ${unreadCount > 0 ? "has-unread" : ""}`}
                onClick={handleNotificationClick}
            >
                <NotificationsIcon/>
                {unreadCount > 0 && <span className="notification-badge">{unreadCount}</span>}
                {/*<p className="header-icon-text">알림</p>*/}
            </button>
            {isPopupVisible && (
                <NotificationPopup
                    notifications={notifications}
                    onClose={handlePopupClose}
                    setRead={markNotificationAsRead}
                    onRemove={handleNotificationRemove}
                    setUnreadCount={setUnreadCount}
                />
            )}
        </div>
    );
}

export default Notification;