import {useEffect, useState} from 'react';

import {InitializeTimeOptions, InitializeTimeOptionsWithPeriod} from "../ScheduleTimeUtils/TimeOptions.jsx";
import {formatDateToLocalDateTime, formatDuration, formatPeriodTimeToLocalTimeFormat} from "@/helpers/TimeFormatter.js"
import {useCalendar} from "@/context/fullCalendar/UseCalendar.jsx";

import "@/styles/home/ScheduleForm.css";
import PropTypes from "prop-types";

import authenticatedApi from "@/api/AuthenticatedApi.js";
import {API_ENDPOINT_NAMES} from "@/constants/ApiEndPoints.js";
import {formatFixedSchedule} from "@/helpers/ScheduleFormatter.js";
import {
    isStartDateBeforeEndDate,
    validateDayChecked,
    validateDurationTime,
    validateDate,
    validateTitle
} from "@/components/home/form/ScheduleForm/ValidateScheduleForm.js";
import {EVENT_CREATE_VALIDATE_MESSAGE} from "@/constants/ScheduleValidateMessage.js";
import {isSuccess} from "@/helpers/AxiosHelper.js";

function FixedScheduleForm({ onClose }) {

    const { loadCalendarEvents } = useCalendar();

    const today = new Date();

    const [formData, setFormData] = useState({
        title: '',
        commonDescription: '',
        startDate: `${formatDateToLocalDateTime(today).split('T')[0]}`,
        endDate: `${formatDateToLocalDateTime(new Date(today.getTime() + 24 * 60 * 60 * 1000)).split('T')[0]}`,
        startTime: 'AM',
        startHour: "01",
        startMinute: "00",
        durationHour: "00",
        durationMinute: "00",
        frequency: 'daily',
        days: [],
    });

    const [titleError, setTitleError] = useState("");
    const [startDateError, setStartDateError] = useState("");
    const [endDateError, setEndDateError] = useState("");
    const [dateObjectError, setDateObjectError] = useState("");
    const [durationTimeError, setDurationTimeError] = useState("");
    const [dayUncheckedError, setDayUncheckedError] = useState("");

    useEffect(() => {
        handleFrequencyChange({target: {value: formData.frequency}});
    }, [formData.frequency, formData.startDate, formData.endDate]);

    useEffect(() => {
        validateDayChecked(formData.days, setDayUncheckedError);
    }, [formData.days]);

    const handleFrequencyChange = (e) => {
        const { value } = e.target;
        const validDays = getValidDays(formData.startDate, formData.endDate);

        if (value === 'daily') {
            setFormData((prevData) => ({
                ...prevData,
                frequency: value,
                days: validDays,
            }));
        } else if (value === 'weekly') {
            setFormData((prevData) => ({
                ...prevData,
                frequency: value,
                days: [],
            }));
        }
    };


    const handleChange = (e) => {
        const { name, value, type, checked } = e.target;
        validateFields(name, value);

        if (type === 'checkbox') {
            const updatedDays = checked
                ? [...formData.days, value]
                : formData.days.filter((day) => day !== value);
            setFormData((prevData) => ({
                ...prevData,
                days: updatedDays,
            }));

        } else {
            setFormData({
                ...formData,
                [name]: value,
            });
        }
    };

    const validateFields = (name, value) => {
        if (name === "title") {
            validateTitle(value, setTitleError);
        } else if (name === "startDate") {
            if (validateDate(value, setStartDateError) && validateDate(formData.endDate, setEndDateError)) {
                isStartDateBeforeEndDate(value, formData.endDate, setDateObjectError);
            } else {
                setDateObjectError("");
            }
        } else if (name === "endDate") {
            if (validateDate(formData.startDate, setEndDateError) && validateDate(value, setEndDateError)) {
                isStartDateBeforeEndDate(formData.startDate, value, setDateObjectError);
            } else {
                setDateObjectError("");
            }
        } else if (name === "durationHour") {
            validateDurationTime(value, formData.durationMinute, setDurationTimeError);
        } else if (name === "durationMinute") {
            validateDurationTime(formData.durationHour, value, setDurationTimeError);
        }
    }

    const validateSubmit = () => {
        const isTitleValid = validateTitle(formData.title, setTitleError);
        const isStartDateValid = validateDate(formData.startDate, setStartDateError);
        const isEndDateValid = validateDate(formData.endDate, setEndDateError);
        const isDurationTimeValid = validateDurationTime(formData.durationHour, formData.durationMinute, setDurationTimeError);
        const areDaysChecked = validateDayChecked(formData.days, setDayUncheckedError);
        const isValidInput = isTitleValid && isStartDateValid && isEndDateValid && isDurationTimeValid && areDaysChecked;

        if (!isValidInput) {
            return false;
        }

        const isDateTimeValid = isStartDateBeforeEndDate(formData.startDate, formData.endDate, setDateObjectError);

        return isValidInput && isDateTimeValid;
    };

    const handleSubmit = async (e) => {
        e.preventDefault();

        if (!validateSubmit()) return;

        const finalData = {
            title: formData.title,
            commonDescription: formData.commonDescription,
            startDateTime: formData.startDate+"T00:00:00",
            endDateTime: formData.endDate+"T00:00:00",
            startTime: formatPeriodTimeToLocalTimeFormat(
                formData.startTime,
                formData.startHour,
                formData.startMinute
            ),
            duration: formatDuration(formData.durationHour, formData.durationMinute),
            frequency: formData.frequency,
            days: formData.days,
        };
        const url = '/schedule/fixed';
        const response = await authenticatedApi.post(
            url,
            finalData,
            {
                apiEndPoint: API_ENDPOINT_NAMES.CREATE_F_SCHEDULE,
        });
        if (await isSuccess(response)) {
            await loadCalendarEvents();
            alert(EVENT_CREATE_VALIDATE_MESSAGE.SUBMIT_SUCCESS);
            onClose();
        } else {
            alert(EVENT_CREATE_VALIDATE_MESSAGE.SUBMIT);
        }
    };

    const getMinDate = () => {
        const currentYear = new Date().getFullYear();
        return `${currentYear - 10}-01-01`;
    };

    const getMaxDate = () => {
        const currentYear = new Date().getFullYear();
        return `${currentYear + 10}-12-31`;
    };

    const getValidDays = (startDate, endDate) => {
        const start = new Date(startDate);
        const end = new Date(endDate);
        const dayMapping = ['su', 'mo', 'tu', 'we', 'th', 'fr', 'sa'];

        if (start > end) return [];

        const validDays = new Set();

        const differenceInDays = Math.ceil((end - start) / (1000 * 60 * 60 * 24)) + 1;
        if (differenceInDays >= 7) {
            return dayMapping;
        }

        for (let d = new Date(start); d <= end; d.setDate(d.getDate() + 1)) {
            validDays.add(dayMapping[d.getDay()]);
        }

        return Array.from(validDays);
    };


    return (
        <div id="schedule-form-container">
            <h2 id="schedule-form-header">고정 일정 생성</h2>
            <form id="schedule-form" data-type="fixed" onSubmit={handleSubmit}>
                <div className="form-group">
                    <label htmlFor="schedule-title">제목</label>
                    <input
                        type="text"
                        id="schedule-title"
                        name="title"
                        placeholder="일정 제목"
                        maxLength="80"
                        value={formData.title}
                        onChange={handleChange}
                    />
                    {titleError && <p className="error-message">{titleError}</p>}
                </div>

                <div className="form-group">
                    <label htmlFor="schedule-description">설명</label>
                    <textarea
                        id="schedule-description"
                        name="commonDescription"
                        placeholder="일정 설명"
                        value={formData.commonDescription}
                        onChange={handleChange} // 추가된 onChange 핸들러
                    ></textarea>
                </div>

                <div className="form-group">
                    <label htmlFor="schedule-start-date">시작 날짜</label>
                    <input
                        type="date"
                        id="schedule-start-date"
                        name="startDate"
                        value={formData.startDate}
                        min={getMinDate()}
                        max={getMaxDate()}
                        onChange={handleChange}
                    />
                    {startDateError && <p className="error-message">{startDateError}</p>}
                </div>

                <div className="form-group">
                    <label htmlFor="schedule-end-date">종료 날짜</label>
                    <input
                        type="date"
                        id="schedule-end-date"
                        name="endDate"
                        value={formData.endDate}
                        min={getMinDate()}
                        max={getMaxDate()}
                        onChange={handleChange}
                    />
                    {endDateError && <p className="error-message">{endDateError}</p>}
                </div>
                {dateObjectError && <p className="error-message">{dateObjectError}</p>}

                <InitializeTimeOptionsWithPeriod
                    labelText="시작 시간"
                    selectType="start"
                    handleChange={handleChange}
                />

                <InitializeTimeOptions
                    labelText="지속 시간"
                    selectType="duration"
                    handleChange={handleChange}
                />
                {durationTimeError && <p className="error-message">{durationTimeError}</p>}

                <div className="form-group">
                    <label htmlFor="schedule-frequency">빈도</label>
                    <select
                        id="schedule-frequency"
                        name="frequency"
                        value={formData.frequency}
                        onChange={handleFrequencyChange}
                    >
                        <option value="daily">매일</option>
                        <option value="weekly">매주</option>
                    </select>
                </div>

                <div className="form-group">
                    <label>수행 요일</label>
                    <div className="weekday-checkbox-group">
                        {['mo', 'tu', 'we', 'th', 'fr', 'sa', 'su'].map((day, index) => {
                            const validDays = getValidDays(formData.startDate, formData.endDate);
                            const isDisabled = !validDays.includes(day);
                            return (
                                <label key={day} htmlFor={`weekday-${day}`}>
                                    <input
                                        type="checkbox"
                                        id={`weekday-${day}`}
                                        name="days"
                                        value={day}
                                        checked={formData.days.includes(day)}
                                        onChange={handleChange}
                                        disabled={formData.frequency === "daily" ? true : isDisabled}
                                    />
                                    {['월', '화', '수', '목', '금', '토', '일'][index]}
                                </label>
                            );
                        })}
                    </div>
                    {dayUncheckedError && <p className="error-message">{dayUncheckedError}</p>}
                </div>
                <button
                    type="submit"
                    id="submit-button"
                    disabled={!!titleError
                        || !!startDateError
                        || !!endDateError
                        || !!dateObjectError
                        || !!durationTimeError
                        || !!dayUncheckedError}>
                    저장
                </button>
                <button
                    type="button"
                    id="cancel-btn"
                    onClick={onClose}>
                    취소
                </button>
            </form>
        </div>

    );
}

FixedScheduleForm.propTypes = {
    onClose: PropTypes.func.isRequired,
}

export default FixedScheduleForm;
