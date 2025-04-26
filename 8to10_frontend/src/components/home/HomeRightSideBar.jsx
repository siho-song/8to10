import { useState } from "react";
import PropTypes from 'prop-types';

import "@/styles/home/RightSideBar.css"

import EventDetails from "./eventDetails/EventDetails.jsx";
import FixedScheduleForm from "./form/ScheduleForm/FixedScheduleForm.jsx";
import ScheduleTypePopup from "./form/ScheduleTypePopup.jsx";
import NormalScheduleForm from "@/components/home/form/ScheduleForm/NormalScheduleForm.jsx";
import VariableScheduleForm from "@/components/home/form/ScheduleForm/VariableScheduleForm.jsx";



function HomeSidebarRight({ selectedEvent, onClose, showScheduleForm, onShowForm }) {
    const [showPopup, setShowPopup] = useState(false);
    const [scheduleType, setScheduleType] = useState(null);

    const handleTogglePopup = () => {
        // 일정 생성 팝업을 열면 기존 우측 사이드바에 열려있는 창이 닫혀야함
        onClose();
        setShowPopup((prev) => !prev);
    };

    const handleSelectType = (type) => {
        setScheduleType(type);
        setShowPopup(false);
        onShowForm();
    };

    return (
        <div className="sidebar" id="right-sidebar">
            <button id="toggle-add-schedule-btn" onClick={handleTogglePopup}>
                일정 생성
            </button>

            {!showScheduleForm && selectedEvent && (
                <EventDetails
                    selectedEvent={selectedEvent}
                    onClose={onClose}
                />
            )}

            {showPopup && (
                <ScheduleTypePopup
                    onClose={handleTogglePopup}
                    onSelectType={handleSelectType}
                />
            )}

            {showScheduleForm && scheduleType && (
                scheduleType === "fixed"
                    ? <FixedScheduleForm onClose={onClose} />
                    : (scheduleType === "normal"
                        ? <NormalScheduleForm onClose={onClose} />
                        : <VariableScheduleForm onClose={onClose} />)
            )}
        </div>
    );
}

HomeSidebarRight.propTypes = {
    selectedEvent: PropTypes.shape({
        id: PropTypes.string.isRequired,
        groupId: PropTypes.string.isRequired,
        title: PropTypes.string.isRequired,
        start: PropTypes.instanceOf(Date).isRequired,
        end: PropTypes.instanceOf(Date).isRequired,
        extendedProps: PropTypes.shape({
            type: PropTypes.string.isRequired,
            commonDescription: PropTypes.string,
            detailDescription: PropTypes.string,
            bufferTime: PropTypes.string,
            completeStatus: PropTypes.bool,
            isComplete: PropTypes.bool,
            dailyAmount: PropTypes.number,
            achievedAmount: PropTypes.number,
            parentId: PropTypes.number,
            originId: PropTypes.number,
        }),
    }),
    onClose: PropTypes.func.isRequired,
    showScheduleForm: PropTypes.bool.isRequired,
    onShowForm: PropTypes.func.isRequired,
};

export default HomeSidebarRight;
