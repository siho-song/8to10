import PropTypes from "prop-types";
import {createLocalDateTime, extractDateInfo, formatDateInfo} from "@/helpers/TimeFormatter.js";
import {useEffect, useState} from "react";
import {useCalendar} from "@/context/fullCalendar/UseCalendar.jsx";
import authenticatedApi from "@/api/AuthenticatedApi.js";
import {API_ENDPOINT_NAMES} from "@/constants/ApiEndPoints.js";
import ConfirmDeleteModal from "@/components/modal/ConfirmDeleteModal.jsx";
import TimeEditForm from "@/components/home/eventDetails/TimeEditForm.jsx";
import {
    isStartDateBeforeEndDate,
    validateDateInput,
    validateTitle
} from "@/components/home/eventDetails/ValidateEventDetails.js";
import {EVENT_DETAILS_VALIDATE_MESSAGE} from "@/constants/ScheduleValidateMessage.js";
import {isSuccess} from "@/helpers/AxiosHelper.js";

const VScheduleDetails = ({selectedEvent, onClose}) => {

    const {updateExtendedProps, updateProps, deleteEvent} = useCalendar();

    const [isEditMode, setIsEditMode] = useState(false);
    const [isDescriptionEditMode, setIsDescriptionEditMode] = useState(false);

    const [title, setTitle] = useState(selectedEvent.title);
    const [commonDescription, setCommonDescription] = useState(selectedEvent.extendedProps.commonDescription);
    const [hasCommonDescription, setHasCommonDescription] = useState(selectedEvent.extendedProps.commonDescription.lenght > 0);

    const startDateInfo = extractDateInfo(selectedEvent.start);
    const endDateInfo = extractDateInfo(selectedEvent.end);
    const [startDate, setStartDate] = useState(startDateInfo);
    const [endDate, setEndDate] = useState(endDateInfo);

    const [isModalOpen, setIsModalOpen] = useState(false);

    const [titleError, setTitleError] = useState("");
    const [dateObjectError, setDateObjectError] = useState("");
    const [startDateError, setStartDateError] = useState("");
    const [endDateError, setEndDateError] = useState("");

    useEffect(() => {
        setTitle(selectedEvent.title);
        setCommonDescription(selectedEvent.extendedProps.commonDescription);
        setHasCommonDescription(selectedEvent.extendedProps.commonDescription.length > 0);
        setStartDate(startDateInfo);
        setEndDate(endDateInfo);

        setIsEditMode(false);
        setIsDescriptionEditMode(false);
        setTitleError("");
        setDateObjectError("");
        setStartDateError("");
        setEndDateError("");

        setIsModalOpen(false);
    }, [selectedEvent]);

    const handleDelete = async () => {
        const url = `/schedule/variable/${selectedEvent.extendedProps.originId}`;
        const response = await authenticatedApi.delete(
            url,
            {apiEndPoint: API_ENDPOINT_NAMES.DELETE_SCHEDULE,},
        )
        if (await isSuccess(response)) {
            alert(EVENT_DETAILS_VALIDATE_MESSAGE.DELETE_SUCCESS);
            deleteEvent(selectedEvent.id);
            onClose();
        } else {
            alert(EVENT_DETAILS_VALIDATE_MESSAGE.DELETE_FAIL);
        }
    }

    const handleEditModeChange = () => {
        setIsEditMode(true);
    }

    const handleEditDescription = () => {
        setIsDescriptionEditMode(true);
    }

    const handleDescriptionChange = (e) => {
        const inputValue = e.target.value;
        setCommonDescription(inputValue);
    }

    const handleTitleChange = (e) => {
        const inputValue = e.target.value;
        validateTitle(inputValue, setTitleError);
        setTitle(inputValue);
    }

    const handleStartDateChange = (updatedStartDate) => {
        setStartDate(updatedStartDate);

        if (validateDateInput(updatedStartDate, setStartDateError)) {
            const startDateTime = createLocalDateTime(updatedStartDate);
            const endDateTime = createLocalDateTime(endDate);

            isStartDateBeforeEndDate(startDateTime, endDateTime, setDateObjectError);
        } else {
            setDateObjectError("");
        }
    }

    const handleEndDateChange = (updatedEndDate) => {
        setEndDate(updatedEndDate);

        if (validateDateInput(updatedEndDate, setEndDateError)) {
            const startDateTime = createLocalDateTime(startDate);
            const endDateTime = createLocalDateTime(updatedEndDate);

            isStartDateBeforeEndDate(startDateTime, endDateTime, setDateObjectError);
        } else {
            setDateObjectError("");
        }
    };

    const openModal = () => {
        setIsModalOpen(true);
    };

    const closeModal = () => {
        setIsModalOpen(false);
    };

    const handleCancel = () => {
        setTitle(selectedEvent.title);
        setCommonDescription(selectedEvent.extendedProps.commonDescription);
        setStartDate(startDateInfo);
        setEndDate(endDateInfo);
        setIsEditMode(false);
        setTitleError("");
        setDateObjectError("");
        setStartDateError("");
        setEndDateError("");
    }

    const handleDescriptionEditCancel = () => {
        setCommonDescription(selectedEvent.extendedProps.commonDescription);
        setIsDescriptionEditMode(false);
    }

    const handleDescriptionEditSubmit = async () => {
        const startDateTime = createLocalDateTime(startDate);
        const endDateTime = createLocalDateTime(endDate);

        const url = "/schedule/variable";
        const response = await authenticatedApi.put(
            url,
            {
                id: selectedEvent.extendedProps.originId,
                title: title,
                commonDescription: commonDescription,
                startDateTime: startDateTime,
                endDateTime: endDateTime,
            },
            {apiEndPoint: API_ENDPOINT_NAMES.EDIT_V_SCHEDULE,},
        )
        if (await isSuccess(response)) {
            updateExtendedProps(selectedEvent.id, ['commonDescription'], [commonDescription]);
            setHasCommonDescription(commonDescription.length > 0);
            alert(EVENT_DETAILS_VALIDATE_MESSAGE.MEMO_SUCCESS);
            setIsDescriptionEditMode(false);
        } else {
            alert(EVENT_DETAILS_VALIDATE_MESSAGE.MEMO_FAIL);
        }
    }

    const handleEditSubmit = async () => {
        setDateObjectError("");

        const startDateTime = createLocalDateTime(startDate);
        const endDateTime = createLocalDateTime(endDate);

        if (!validateTitle(title, setTitleError)
            || !validateDateInput(startDate, setStartDateError)
            || !validateDateInput(endDate, setEndDateError)) {
            return;
        }

        if(!isStartDateBeforeEndDate(startDateTime, endDateTime, setDateObjectError)) {
            return;
        }

        const url = "/schedule/variable";
        const response = await authenticatedApi.put(
            url,
            {
                id: selectedEvent.extendedProps.originId,
                title: title,
                commonDescription: commonDescription,
                startDateTime: startDateTime,
                endDateTime: endDateTime,
            },
            {apiEndPoint: API_ENDPOINT_NAMES.EDIT_V_SCHEDULE,}
        );
        if (await isSuccess(response)) {
            updateExtendedProps(selectedEvent.id, ['commonDescription'], [commonDescription]);
            updateProps(selectedEvent.id, ['title', 'start', 'end'], [title, startDateTime, endDateTime]);
            setHasCommonDescription(commonDescription.length > 0);
            alert(EVENT_DETAILS_VALIDATE_MESSAGE.MODIFICATION_SUCCESS);
            setIsEditMode(false);
        } else {
            alert(EVENT_DETAILS_VALIDATE_MESSAGE.MODIFICATION_FAIL);
        }
    }

    return (
        <>
            <div id="event-details-container-variable">
                <div
                    className="event-details-header-variable">
                    <h2>일정 상세보기</h2>
                    <button className="close-event-details" onClick={onClose}>&times;</button>
                </div>
                <>
                    {!isEditMode ? (
                        <div className="event-detail-props">
                            <div className="event-detail-prop">
                                <h2>
                                    일정 제목
                                </h2>
                                <hr className="event-detail-contour"/>
                                <p>{title}</p>
                            </div>
                            <div className="event-detail-prop">
                                <h2>
                                    <strong>시작 시간</strong>
                                </h2>
                                <hr className="event-detail-contour"/>
                                <TimeEditForm
                                    date={startDate}
                                    setDate={setStartDate}
                                    type={"start"}
                                    isDisabled={true}/>
                            </div>
                            <div className="event-detail-prop">
                                <h2>
                                    <strong>종료 시간</strong>
                                </h2>
                                <hr className="event-detail-contour"/>
                                <TimeEditForm
                                    date={endDate}
                                    setDate={setEndDate}
                                    type={"end"}
                                    isDisabled={true}/>
                            </div>
                            <div className="event-detail-prop">
                                <div className="description-header">
                                    <h2>
                                        <strong>일정 메모</strong>
                                    </h2>
                                    {!hasCommonDescription && !isDescriptionEditMode &&
                                        <div className="description-btns">
                                            <div className="description-btns">
                                                <p className="description-btn variable"
                                                   onClick={handleEditDescription}>수정</p>
                                            </div>
                                        </div>
                                    }
                                </div>
                                <hr className="event-details-contour"/>
                                {hasCommonDescription ? (
                                    <p>{commonDescription}</p>
                                ) : (
                                    <>
                                        {!isDescriptionEditMode ? (
                                            <p className="no-description">등록된 일정 공통 메모가 없습니다.</p>
                                        ) : (
                                            <>
                                                <textarea
                                                    className="description-textarea variable"
                                                    value={commonDescription}
                                                    onChange={handleDescriptionChange}
                                                    placeholder={"일정 메모를 입력해주세요."}
                                                    cols="30"
                                                    rows="3"
                                                />
                                            </>
                                        )}
                                        {isDescriptionEditMode &&
                                            <div className="description-edit-btns">
                                                <button
                                                    className="description-edit-btn variable"
                                                    onClick={handleDescriptionEditSubmit}
                                                >등록
                                                </button>
                                                <button
                                                    className="description-cancel-btn"
                                                    onClick={handleDescriptionEditCancel}
                                                >취소
                                                </button>
                                            </div>
                                        }

                                    </>
                                )}
                            </div>
                            {!isDescriptionEditMode &&
                                <div className="handle-variable">
                                    <button
                                        className="edit-variable"
                                        onClick={handleEditModeChange}
                                    > 일정 정보 수정
                                    </button>
                                    <button
                                        className="delete-variable"
                                        onClick={openModal}
                                    >삭제
                                    </button>
                                </div>
                            }
                        </div>
                    ) : (
                        <div className="event-detail-props">
                            <div className="event-detail-prop">
                                <h2>
                                    일정 제목
                                </h2>
                                <hr className="event-detail-contour"/>
                                <input
                                    type="text"
                                    id="edit-schedule-title-variable"
                                    name="title"
                                    placeholder="일정 제목"
                                    maxLength="80"
                                    value={title}
                                    onChange={handleTitleChange}
                                />
                                {titleError && <p className="error-message">{titleError}</p>}
                            </div>
                            <div className="event-detail-prop">
                                <h2>
                                    <strong>시작 시간</strong>
                                </h2>
                                <hr className="event-detail-contour"/>
                                <TimeEditForm
                                    date={startDate}
                                    setDate={handleStartDateChange}
                                    type={"start"}
                                    isDisabled={false}/>
                                {startDateError && <p className="error-message">{startDateError}</p>}
                            </div>
                            <div className="event-detail-prop">
                                <h2>
                                    <strong>종료 시간</strong>
                                </h2>
                                <hr className="event-detail-contour"/>
                                <TimeEditForm
                                    date={endDate}
                                    setDate={handleEndDateChange}
                                    type={"end"}
                                    isDisabled={false}/>
                                {endDateError && <p className="error-message">{endDateError}</p>}
                            </div>
                            {dateObjectError && <p className="error-message">{dateObjectError}</p>}
                            <div className="event-detail-prop">
                                <h2>
                                    <strong>일정 메모</strong>
                                </h2>
                                <hr className="event-detail-contour"/>
                                <textarea
                                    className="description-textarea variable"
                                    value={commonDescription}
                                    onChange={handleDescriptionChange}
                                    cols="30"
                                    rows="3"
                                />
                            </div>

                            <div className="handle-variable">
                                <button
                                    className="edit-variable"
                                    onClick={handleEditSubmit}
                                >등록
                                </button>
                                <button
                                    className="delete-variable"
                                    onClick={handleCancel}
                                >취소
                                </button>
                            </div>
                        </div>
                    )}

                    <ConfirmDeleteModal
                        isOpen={isModalOpen}
                        onClose={closeModal}
                        onConfirm={handleDelete}
                        itemName={selectedEvent.title}
                    />
                </>
            </div>
        </>

    );
}

VScheduleDetails.propTypes = {
    selectedEvent: PropTypes.shape({
        id: PropTypes.string.isRequired,
        groupId: PropTypes.string.isRequired,
        title: PropTypes.string.isRequired,
        start: PropTypes.instanceOf(Date).isRequired,
        end: PropTypes.instanceOf(Date).isRequired,
        extendedProps: PropTypes.shape({
            type: PropTypes.string.isRequired,
            commonDescription: PropTypes.string,
            originId: PropTypes.number,
        }),
    }),
    onClose: PropTypes.func.isRequired,
}


export default VScheduleDetails;