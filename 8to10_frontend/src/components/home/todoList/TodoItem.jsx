import PropTypes from "prop-types";
import {formatLocalDateTimeToTime} from "@/helpers/TimeFormatter.js";
import {useState} from "react";
import {useCalendar} from "@/context/fullCalendar/UseCalendar.jsx";

const TodoItem = ({currentDate, event}) => {

    const {updateExtendedProps} = useCalendar();
    const {extendedProps} = event;

    const today = new Date();
    today.setHours(0, 0, 0, 0);

    const [achievedAmount, setAchievedAmount] = useState(extendedProps.achievedAmount || 0);
    const [isSubmmitted, setIsSubmitted] = useState(extendedProps.isComplete);
    const [showTooltip, setShowTooltip] = useState(false);
    const [errorTooltip, setErrorTooltip] = useState(false);
    const isCurrentDate = currentDate.getTime() === today.getTime();

    const handleInputFocus = () => {
        if (achievedAmount === 0) {
            setAchievedAmount("");
        }
    }

    const handleInputChange = (e) => {
        let inputValue = e.target.value;
        if (!/^\d*\.?\d*$/.test(inputValue)) {
            inputValue = inputValue.replace(/[^0-9.]/g, "");
        }

        const numericValue = parseFloat(inputValue);
        if (numericValue >= 0 && numericValue <= extendedProps.dailyAmount) {
            if (numericValue === extendedProps.dailyAmount) {
                updateExtendedProps(event.id, ["isComplete", "achievedAmount"], [true, numericValue]);
            } else {
                updateExtendedProps(event.id, ["isComplete", "achievedAmount"], [false, numericValue]);
            }
            setAchievedAmount(numericValue);
        } else {
            setErrorTooltip(true);
            setTimeout(() => {
                setErrorTooltip(false);
            }, 3000);
        }
    };

    const handleCheck = () => {
        updateExtendedProps(event.id, ["isComplete"], [!event.extendedProps.isComplete]);
    }

    const handleDisabledClick = () => {
        if (!isCurrentDate) {
            setShowTooltip(true);
            setTimeout(() => setShowTooltip(false), 3000);
        }
    }

    return (
        <>
            <div
                className={`todo-item ${extendedProps.isComplete ? "completed" : ""}`}
                onClick={handleDisabledClick}>
                <div className="todo-item-content">
                    <h3 className="todo-title">{event.title}</h3>
                    {extendedProps.dailyAmount > 0 && (
                        <p className="todo-amount">목표 달성 량: <span>{extendedProps.dailyAmount}</span></p>
                    )}
                    <p className="todo-time">{formatLocalDateTimeToTime(event.start)} ~ {formatLocalDateTimeToTime(event.end)}</p>
                </div>
                <div>
                    {extendedProps.dailyAmount > 0 ? (
                        <input
                            type="number"
                            className="achievement-input"
                            value={achievedAmount}
                            onChange={handleInputChange}
                            onFocus={handleInputFocus}
                            onClick={handleDisabledClick}
                            placeholder="달성량 입력"
                            min={"0"}
                            disabled={!isCurrentDate}
                            max={extendedProps.dailyAmount}
                        />
                    ) : (
                        <button
                            className="check-button"
                            onClick={isCurrentDate ? handleCheck : handleDisabledClick}
                            disabled={!isCurrentDate}
                            aria-label="Mark as completed"
                        >
                            {extendedProps.isComplete ? "✓" : "☐"}
                        </button>
                    )}
                </div>
            </div>
            {showTooltip && (
                <div className="tooltip">
                    오늘의 일정만 업데이트할 수 있습니다.
                </div>
            )}
            {errorTooltip && (
                <div className="tooltip">
                    0에서 목표 달성 량 사이의 값만 입력할 수 있습니다.
                </div>
            )}
        </>

    );

};

TodoItem.propTypes = {
    currentDate: PropTypes.instanceOf(Date).isRequired,
    event: PropTypes.shape({
        id: PropTypes.string.isRequired,
        groupId: PropTypes.string,
        title: PropTypes.string.isRequired,
        start: PropTypes.string.isRequired,
        end: PropTypes.string.isRequired,
        color: PropTypes.string,
        extendedProps: PropTypes.shape({
            type: PropTypes.string.isRequired,
            parentId: PropTypes.number,
            commonDescription: PropTypes.string,
            detailDescription: PropTypes.string,
            bufferTime: PropTypes.string,
            completeStatus: PropTypes.bool,
            isComplete: PropTypes.bool,
            dailyAmount: PropTypes.number,
            achievedAmount: PropTypes.number,
            originId: PropTypes.number,
        }).isRequired,
    }),
};

export default TodoItem;
