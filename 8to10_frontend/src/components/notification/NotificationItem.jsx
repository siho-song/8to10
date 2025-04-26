import {useState} from "react";
import CloseIcon from "@mui/icons-material/Close";
import {useNavigate} from "react-router-dom";
import {BASE_URL, NOTIFICATION_TYPES} from "@/constants/NotificationTypes.js";
import PropTypes from "prop-types";
import authenticatedApi from "@/api/AuthenticatedApi.js";
import {API_ENDPOINT_NAMES} from "@/constants/ApiEndPoints.js";

const NotificationItem = ({notification, onRemove, setRead, setUnreadCount}) => {
    const navigate = useNavigate();
    const [isRead, setIsRead] = useState(notification.isRead);

    const handleNotificationItemClick = async () => {
        const { entityId, notificationType, targetUrl, relatedEntityId } = notification;

        let basePath = "";
        if (notificationType === NOTIFICATION_TYPES.REPLY_ADD) {
            basePath = BASE_URL.REPLY_ADD;
        } else if (notificationType === NOTIFICATION_TYPES.NESTED_REPLY_ADD) {
            basePath = BASE_URL.NESTED_REPLY_ADD;
        }

        const url = `/notification/${entityId}`
        const response = await authenticatedApi.put(
            url,
            {},
            {apiEndPoint: API_ENDPOINT_NAMES.PUT_READ_NOTIFICATION,}
        )

        setUnreadCount((prevCount) => prevCount - 1);
        setIsRead(!isRead);
        setRead(entityId);

        if (basePath) {
            const finalPath = `${basePath}${targetUrl}`;
            navigate(finalPath, {state: {relatedEntityId:relatedEntityId}});
        }
    };

    return (
        <div
            className={`notification-item ${isRead ? "read" : "unread"}`}
            onClick={handleNotificationItemClick}
        >
            <div>
                <p className="notification-content"><strong>{notification.message}</strong></p>
                <p className="notification-receivedAt">{notification.receivedAt}</p>
            </div>
            <button
                className="notification-remove-button"
                onClick={(e) => {
                    e.stopPropagation();
                    onRemove(notification.entityId);
                }}
            >
                <CloseIcon/>
            </button>
        </div>
    );
}

NotificationItem.propTypes = {
    notification: PropTypes.shape({
        entityId: PropTypes.number,
        message: PropTypes.string,
        notificationType: PropTypes.string,
        relatedEntityId: PropTypes.number,
        targetUrl: PropTypes.string,
        isRead: PropTypes.bool,
        receivedAt: PropTypes.string,
    }),
    onRemove: PropTypes.func,
    setRead: PropTypes.func,
    setUnreadCount: PropTypes.func,
}

export default NotificationItem;