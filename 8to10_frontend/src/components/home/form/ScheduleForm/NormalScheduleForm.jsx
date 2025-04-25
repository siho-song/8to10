import { InitializeTimeOptions, } from "@/components/home/form/ScheduleTimeUtils/TimeOptions.jsx";
import {
    formatDateToLocalDateTime,
    formatDuration
} from "@/helpers/TimeFormatter.js";
import {useEffect, useState} from "react";
import {useCalendar} from "@/context/fullCalendar/UseCalendar.jsx";
import PropTypes from "prop-types";
import authenticatedApi from "@/api/AuthenticatedApi.js";
import {API_ENDPOINT_NAMES} from "@/constants/ApiEndPoints.js";
import {formatNormalSchedule} from "@/helpers/ScheduleFormatter.js";
import {
    isStartDateBeforeEndDate, validateBufferPerformSum,
    validateDate, validatePerformInWeek, validatePerformTime,
    validateTitle, validateTotalAmount
} from "@/components/home/form/ScheduleForm/ValidateScheduleForm.js";
import {EVENT_CREATE_VALIDATE_MESSAGE} from "@/constants/ScheduleValidateMessage.js";
import {isSuccess} from "@/helpers/AxiosHelper.js";

function NormalScheduleForm({ onClose }) {

    const { loadCalendarEvents } = useCalendar();

    const today = new Date();

    const [formData, setFormData] = useState({
        title: '',
        commonDescription: '',
        startDate: `${formatDateToLocalDateTime(today).split('T')[0]}`,
        endDate: `${formatDateToLocalDateTime(new Date(today.getTime() + 24 * 60 * 60 * 1000)).split('T')[0]}`,
        bufferHour: "00",
        bufferMinute: "00",
        totalAmount: 0,
        performHour: "00",
        performMinute: "00",
        performInWeek: '1',
        includeSaturday: false,
        includeSunday: false
    });

    const [performInWeekOptions, setPerformInWeekOptions] = useState([1, 2, 3, 4, 5]);
    const [weekendState, setWeekendState] = useState({
        includeSaturdayDisabled: false,
        includeSundayDisabled: false
    });

    const [titleError, setTitleError] = useState("");
    const [startDateError, setStartDateError] = useState("");
    const [endDateError, setEndDateError] = useState("");
    const [dateObjectError, setDateObjectError] = useState("");
    const [performTimeError, setPerformTimeError] = useState("");
    const [bufferPerformSumError, setBufferPerformSumError] = useState("");
    const [performInWeekError, setPerformInWeekError] = useState("");

    const handleChange = (e) => {
        const { name, value, type, checked } = e.target;

        if (name === "totalAmount" && !validateTotalAmount(value)) {
            return;
        }

        validateFields(name, value);

        setFormData((prevData) => ({
            ...prevData,
            [name]: type === "checkbox" ? checked : value,
        }));
    };

    const handleSubmit = async (e) => {
        e.preventDefault();

        if (!validateSubmit()) return;

        const finalData = {
            title: formData.title,
            commonDescription: formData.commonDescription,
            startDateTime: formData.startDate + "T00:00:00",
            endDateTime: formData.endDate + "T00:00:00",
            bufferTime: formatDuration(formData.bufferHour, formData.bufferMinute),
            totalAmount: formData.totalAmount,
            performInDay: formatDuration(formData.performHour, formData.performMinute),
            performInWeek: formData.performInWeek,
            isIncludeSaturday: formData.includeSaturday,
            isIncludeSunday: formData.includeSunday
        };
        const url = '/schedule/normal';
        const response = await authenticatedApi.post(
            url,
            finalData,
            {
                apiEndPoint: API_ENDPOINT_NAMES.CREATE_N_SCHEDULE,
        });
        if (await isSuccess(response)) {
            await loadCalendarEvents();
            alert(EVENT_CREATE_VALIDATE_MESSAGE.SUBMIT_SUCCESS);
            onClose();
        } else {
            alert(EVENT_CREATE_VALIDATE_MESSAGE.SUBMIT_NORMAL);
        }
    };

    const validateSubmit = () => {
        const isTitleValid = validateTitle(formData.title, setTitleError);
        const isStartDateValid = validateDate(formData.startDate, setStartDateError);
        const isEndDateValid = validateDate(formData.endDate, setEndDateError);
        const isPerformTimeValid = validatePerformTime(formData.performHour, formData.performMinute, setPerformTimeError);
        const isBufferPerformSumValid = validateBufferPerformSum(formData.bufferHour, formData.bufferMinute, formData.performHour, formData.performMinute, setBufferPerformSumError);
        const isPerformInWeekValid = validatePerformInWeek(formData.performInWeek, setPerformInWeekError);

        return isTitleValid
            && isStartDateValid
            && isEndDateValid
            && isPerformTimeValid
            && isBufferPerformSumValid
            && isPerformInWeekValid;
    }

    const getMinDate = () => {
        const currentYear = new Date().getFullYear();
        return `${currentYear - 10}-01-01`;
    };

    const getMaxDate = () => {
        const currentYear = new Date().getFullYear();
        return `${currentYear + 10}-12-31`;
    };

    const calculateScheduleOptions = () => {
        const start = new Date(formData.startDate);
        const end = new Date(formData.endDate);


        const dayCount = Math.ceil((end - start) / (1000 * 60 * 60 * 24)) + 1;
        if (dayCount >= 7) {
            const totalDays = 5 + (formData.includeSaturday ? 1 : 0) + (formData.includeSunday ? 1 : 0);
            return {
                performInWeekOptions: Array.from({ length: totalDays }, (_, i) => i + 1),
                weekendState: {
                    includeSaturdayDisabled: false,
                    includeSundayDisabled: false,
                },
            };
        }

        let weekendCount = 0;
        const daysBetween = [];


        while (start <= end) {
            const day = start.getDay();
            daysBetween.push(day);
            if (day === 0 || day === 6) weekendCount++;
            start.setDate(start.getDate() + 1);
        }


        const hasSaturday = daysBetween.includes(6);
        const hasSunday = daysBetween.includes(0);

        const weekendState = {
            includeSaturdayDisabled: !hasSaturday,
            includeSundayDisabled: !hasSunday,
        };

        let maxDays = dayCount - weekendCount;
        if (formData.includeSaturday) maxDays += 1;
        if (formData.includeSunday) maxDays += 1;

        maxDays = Math.min(maxDays, 7);

        const performInWeekOptions = maxDays > 0
            ? Array.from({ length: maxDays }, (_, i) => i + 1)
            : [];
        validatePerformInWeek(performInWeekOptions.length, setPerformInWeekError);
        return { performInWeekOptions, weekendState };
    };


    useEffect(() => {
        const { performInWeekOptions, weekendState } = calculateScheduleOptions();

        setPerformInWeekOptions(performInWeekOptions);

        if (!performInWeekOptions.includes(Number(formData.performInWeek))) {
            setFormData((prevData) => ({
                ...prevData,
                performInWeek: performInWeekOptions.length > 0 ? '1' : '',
            }));
        }

        setWeekendState(weekendState);

        if (weekendState.includeSaturdayDisabled) {
            setFormData((prevData) => ({ ...prevData, includeSaturday: false }));
        }
        if (weekendState.includeSundayDisabled) {
            setFormData((prevData) => ({ ...prevData, includeSunday: false }));
        }
    }, [formData.startDate, formData.endDate, formData.includeSaturday, formData.includeSunday]);

    useEffect(() => {
        validateBufferPerformSum(formData.bufferHour, formData.bufferMinute, formData.performHour, formData.performMinute, setBufferPerformSumError);
    }, [formData.bufferHour, formData.bufferMinute, formData.performHour, formData.performMinute]);

    useEffect(() => {
        validatePerformInWeek(formData.performInWeek, setPerformInWeekError);
    }, [formData.performInWeek]);

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
        } else if (name === "performHour") {
            validatePerformTime(value, formData.performMinute, setPerformTimeError);
        } else if (name === "performMinute") {
            validatePerformTime(formData.performHour, value, setPerformTimeError);
        } else if (name === "performInWeek") {
            validatePerformInWeek(value, setPerformInWeekError);
        }
    }

    return (
        <div id="schedule-form-container">
            <h2 id="schedule-form-header">일반 일정 생성</h2>
            <form id="schedule-form" data-type="normal" onSubmit={handleSubmit}>
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
                        onChange={handleChange}
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
                        min={getMinDate()}
                        max={getMaxDate()}
                        value={formData.endDate}
                        onChange={handleChange}
                    />
                    {endDateError && <p className="error-message">{endDateError}</p>}
                </div>
                {dateObjectError && <p className="error-message">{dateObjectError}</p>}

                <InitializeTimeOptions labelText="희망 여유 시간" selectType="buffer" handleChange={handleChange}/>

                <div className="form-group">
                    <label htmlFor="schedule-total-amount">목표 달성 총량</label>
                    <input
                        type="text"
                        id="schedule-total-amount"
                        name="totalAmount"
                        placeholder="달성하고 싶은 양"
                        pattern="\d*"
                        title="숫자만 입력 가능합니다."
                        value={formData.totalAmount}
                        onInput={handleChange}/>
                </div>

                <InitializeTimeOptions labelText="희망 하루 수행시간" selectType="perform" handleChange={handleChange}/>
                {performTimeError && <p className="error-message">{performTimeError}</p>}
                {bufferPerformSumError && <p className="error-message">{bufferPerformSumError}</p>}

                <div className="form-group">
                    <label htmlFor="include-weekend">주말 포함 여부</label>
                    <div>
                        <label>
                            <input
                                type="checkbox"
                                id="include-saturday"
                                name="includeSaturday"
                                checked={formData.includeSaturday}
                                onChange={handleChange}
                                disabled={weekendState.includeSaturdayDisabled}
                            /> 토요일 포함
                        </label>
                        <label>
                            <input
                                type="checkbox"
                                id="include-sunday"
                                name="includeSunday"
                                checked={formData.includeSunday}
                                onChange={handleChange}
                                disabled={weekendState.includeSundayDisabled}
                            /> 일요일 포함
                        </label>
                    </div>
                </div>

                <div className="form-group">
                    <label htmlFor="schedule-perform-in-week">희망 주간 수행빈도</label>
                    <select
                        id="schedule-perform-in-week"
                        name="performInWeek"
                        value={formData.performInWeek}
                        onChange={handleChange}>
                        {performInWeekOptions.map((option) => (
                            <option key={option} value={option}>
                                {option}회
                            </option>
                        ))}
                    </select>
                </div>
                {performInWeekError && <p className="error-message">{performInWeekError}</p>}

                <button
                    type="submit"
                    id="submit-button"
                    disabled={!!titleError || !!startDateError || !!endDateError || !!dateObjectError || !!performTimeError || !!bufferPerformSumError || !!performInWeekError}>
                    저장
                </button>
                <button type="button" id="cancel-btn" onClick={onClose}>
                    취소
                </button>

            </form>
        </div>
    );
}

NormalScheduleForm.propTypes = {
    onClose: PropTypes.func.isRequired,
}

export default NormalScheduleForm;
