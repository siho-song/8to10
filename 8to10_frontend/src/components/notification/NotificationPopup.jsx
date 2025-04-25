import CloseIcon from "@mui/icons-material/Close";
import PropTypes from "prop-types";
import NotificationItem from "@/components/notification/NotificationItem.jsx";

const NotificationPopup = ({ notifications, onClose, setRead, onRemove, setUnreadCount }) => {
    return (
        <div className="notification-popup">
            <div className="notification-popup-header">
                <h2 className="notification-popup-header-text">알림</h2>
                <button className="notification-close-button" onClick={onClose}>
                    <CloseIcon />
                </button>
            </div>
            <div className="notification-popup-content">
                {notifications.length > 0 ? (
                    notifications.map((notification, index) => (
                        <NotificationItem
                            key={index}
                            notification={notification}
                            setRead={setRead}
                            onRemove={onRemove}
                            setUnreadCount={setUnreadCount}
                        />
                    ))
                ) : (
                    <p>새로운 알림이 없습니다.</p>
                )}
            </div>
        </div>
    );
};

NotificationPopup.propTypes = {
    notifications: PropTypes.arrayOf(
        PropTypes.shape({
            entityId: PropTypes.number,
            message: PropTypes.string,
            notificationType: PropTypes.string,
            relatedEntityId: PropTypes.number,
            targetUrl: PropTypes.string,
            isRead: PropTypes.bool,
            receivedAt: PropTypes.string,
        }),
    ),
    onClose: PropTypes.func,
    setRead: PropTypes.func,
    onRemove: PropTypes.func,
    setUnreadCount: PropTypes.func,
}

export default NotificationPopup;
