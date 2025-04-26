import {
    InitializeTimeOptionsWithPeriod
} from "@/components/home/form/ScheduleTimeUtils/TimeOptions.jsx";
import {useState} from "react";
import {
    formatDateToLocalDateTime,
    formatPeriodTimeToLocalTimeFormat,
} from "@/helpers/TimeFormatter.js";
import {useCalendar} from "@/context/fullCalendar/UseCalendar.jsx";
import PropTypes from "prop-types";
import authenticatedApi from "@/api/AuthenticatedApi.js";
import {API_ENDPOINT_NAMES} from "@/constants/ApiEndPoints.js";
import {formatVariableSchedule} from "@/helpers/ScheduleFormatter.js";
import {
    isStartDateTimeBeforeEndDateTime,
    validateDate,
    validateTitle
} from "@/components/home/form/ScheduleForm/ValidateScheduleForm.js";
import {EVENT_CREATE_VALIDATE_MESSAGE} from "@/constants/ScheduleValidateMessage.js";
import {isSuccess} from "@/helpers/AxiosHelper.js";

function VariableScheduleForm({ onClose }) {

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
        endTime: 'AM',
        endHour: "01",
        endMinute: "00",
    });

    const [titleError, setTitleError] = useState("");
    const [startDateError, setStartDateError] = useState("");
    const [endDateError, setEndDateError] = useState("");
    const [dateObjectError, setDateObjectError] = useState("");

    const handleChange = (e) => {
        const { name, value } = e.target;
        validateFields(name, value);

        setFormData((prevData) => ({
            ...prevData,
            [name]: value
        }));
    };

    const handleSubmit =  async (e) => {
        e.preventDefault();

        if (!validateSubmit()) return ;

        const startDateTime = formData.startDate +'T'+ formatPeriodTimeToLocalTimeFormat(
            formData.startTime,
            formData.startHour,
            formData.startMinute
        );

        const endDateTime = formData.endDate + 'T' + formatPeriodTimeToLocalTimeFormat(
            formData.endTime,
            formData.endHour,
            formData.endMinute
        );

        const finalData = {
            title: formData.title,
            commonDescription: formData.commonDescription,
            startDateTime: startDateTime,
            endDateTime: endDateTime
        };

        const url = '/schedule/variable';
        const response = await authenticatedApi.post(
            url,
            finalData,
            {
                apiEndPoint: API_ENDPOINT_NAMES.CREATE_V_SCHEDULE,
        });

        if (isSuccess(response)) {
            await loadCalendarEvents();
            alert(EVENT_CREATE_VALIDATE_MESSAGE.SUBMIT_SUCCESS);
            onClose();
        } else {
            alert(EVENT_CREATE_VALIDATE_MESSAGE.SUBMIT);
        }
    };

    const generateStartDateTimeAndEndDateTime = (name, value) => {
        const startDateTime = {startDate: name === "startDate" ? value : formData.startDate
            , startTime: name === "startTime" ? value : formData.startTime
            , startHour: name === "startHour" ? value : formData.startHour
            , startMinute: name === "startMinute" ? value : formData.startMinute};
        const endDateTime = {endDate: name === "endDate" ? value : formData.endDate
            , endTime: name === "endTime" ? value : formData.endTime
            , endHour: name === "endHour" ? value : formData.endHour
            , endMinute: name === "endMinute" ? value : formData.endMinute};
        return {startDateTime, endDateTime}
    }

    const validateFields = (name, value) => {
        if (name === "title") {
            validateTitle(value, setTitleError);
        } else if (name === "startDate" || name === "endDate") {
            if (validateDate(name === "startDate" ? value : formData.startDate, setStartDateError)
                && validateDate(name === "endDate" ? value : formData.endDate, setEndDateError)) {
                const {startDateTime, endDateTime} = generateStartDateTimeAndEndDateTime(name, value);
                isStartDateTimeBeforeEndDateTime(startDateTime, endDateTime, setDateObjectError);
            } else {
                setDateObjectError("");
            }
        } else if (name === "startTime" || name === "startHour" || name === "startMinute" || name === "endTime" || name === "endHour" || name === "endMinute") {
            const {startDateTime, endDateTime} = generateStartDateTimeAndEndDateTime(name, value);
            isStartDateTimeBeforeEndDateTime(startDateTime, endDateTime, setDateObjectError);
        }
    }

    const validateSubmit = () => {
        const isTitleValid = validateTitle(formData.title, setTitleError);
        const isStartDateValid = validateDate(formData.startDate, setStartDateError);
        const isEndDateValid = validateDate(formData.endDate, setEndDateError);
        const isInputValid = isTitleValid && isStartDateValid && isEndDateValid;

        if (!isInputValid) return;

        const startDateTime = {startDate:formData.startDate
                                    , startTime: formData.startTime
                                    , startHour: formData.startHour
                                    , startMinute: formData.startMinute};
        const endDateTime = {endDate:formData.endDate
                                    , endTime: formData.endTime
                                    , endHour: formData.endHour
                                    , endMinute: formData.endMinute};

        const isDateTimeValid = isStartDateTimeBeforeEndDateTime(startDateTime, endDateTime, setDateObjectError);

        return isInputValid && isDateTimeValid;
    }

    const getMinDate = () => {
        const currentYear = new Date().getFullYear();
        return `${currentYear - 10}-01-01`;
    };

    const getMaxDate = () => {
        const currentYear = new Date().getFullYear();
        return `${currentYear + 10}-12-31`;
    };

    return (
        <div id="schedule-form-container">
            <h2 id="schedule-form-header">변동 일정 생성</h2>
            <form id="schedule-form" data-type="variable" onSubmit={handleSubmit}>
                <div className="form-group">
                    <label htmlFor="schedule-title">제목</label>
                    <input
                        type="text"
                        id="schedule-title"
                        name="title"
                        placeholder="일정 제목"
                        maxLength="80"
                        value={formData.title}
                        onChange={handleChange} // 추가된 onChange 핸들러
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
                        min={getMinDate()}
                        max={getMaxDate()}
                        value={formData.startDate}
                        onChange={handleChange} // 추가된 onChange 핸들러
                    />
                    {startDateError && <p className="error-message">{startDateError}</p>}
                </div>

                <div className="form-group">
                    <label htmlFor="schedule-end-date">종료 날짜</label>
                    <input
                        type="date"
                        id="schedule-end-date"
                        name="endDate"
                        min={getMinDate()}
                        max={getMaxDate()}
                        value={formData.endDate}
                        onChange={handleChange} // 추가된 onChange 핸들러
                    />
                    {endDateError && <p className="error-message">{endDateError}</p>}
                </div>

                <InitializeTimeOptionsWithPeriod
                    labelText="시작 시간"
                    selectType="start"
                    handleChange={handleChange}
                />

                <InitializeTimeOptionsWithPeriod
                    labelText="종료 시간"
                    selectType="end"
                    handleChange={handleChange}
                />

                {dateObjectError && <p className="error-message">{dateObjectError}</p>}

                <button
                    type="submit"
                    id="submit-button"
                    disabled={!!titleError || !!startDateError || !!endDateError || !!dateObjectError}>
                    저장
                </button>
                <button
                    type="button"
                    id="cancel-btn"
                    onClick={ onClose }>
                취소
            </button>
            </form>
        </div>
    );
}

VariableScheduleForm.propTypes = {
    onClose: PropTypes.func.isRequired,
}

export default VariableScheduleForm;
