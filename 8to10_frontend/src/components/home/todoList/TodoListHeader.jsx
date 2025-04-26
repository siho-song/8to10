import {formatDate} from "@/helpers/TimeFormatter.js";
import PropTypes from "prop-types";

const TodoListHeader = ({currentDate, changeCurrentDate}) => {

    const handlePrevDateClick = () => {
        const prevDate = new Date(currentDate);
        prevDate.setDate(prevDate.getDate() - 1);
        changeCurrentDate(prevDate);
    }

    const handleNextDateClick = () => {
        const nextDate = new Date(currentDate);
        nextDate.setDate(nextDate.getDate() + 1);
        changeCurrentDate(nextDate);
    }

    return (
        <div className="todo-list-header">
            <button
                id="prev-date-btn"
                onClick={handlePrevDateClick}
            >
                &lt;
            </button>

            <span id="current-date">{formatDate(currentDate)}</span>

            <button
                id="next-date-btn"
                onClick={handleNextDateClick}
            >
                &gt;
            </button>
        </div>
    );
}

TodoListHeader.propTypes = {
    currentDate: PropTypes.instanceOf(Date).isRequired,
    changeCurrentDate: PropTypes.func.isRequired,
}

export default TodoListHeader;