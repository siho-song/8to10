import PropTypes from "prop-types";
import {dateToLocalDateTime, formatDateToLocalDateTime} from "@/helpers/TimeFormatter.js";
import {useCalendar} from "@/context/fullCalendar/UseCalendar.jsx";
import {useEffect, useState} from "react";
import authenticatedApi from "@/api/AuthenticatedApi.js";
import {API_ENDPOINT_NAMES} from "@/constants/ApiEndPoints.js";
import {createLocalDateTime, extractDateInfo} from "@/helpers/TimeFormatter.js";
import TimeEditForm from "@/components/home/eventDetails/TimeEditForm.jsx";
import ScheduleDeleteModal from "@/components/modal/ScheduleDeleteModal.jsx";
import {
    isStartDateBeforeEndDate,
    validateDateInput,
    validateTitle
} from "@/components/home/eventDetails/ValidateEventDetails.js";
import {EVENT_DETAILS_VALIDATE_MESSAGE} from "@/constants/ScheduleValidateMessage.js";
import {isSuccess} from "@/helpers/AxiosHelper.js";

const FScheduleDetails = ({selectedEvent, onClose}) => {
    const {deleteEvent, deleteEventsByGroupId, deleteEventsAfterDateByGroupId, updateExtendedProps, updateProps, updatePropsByGroupId, updateExtendedPropsByGroupId, countEventsByGroupId, getEarliestStartByGroupId} = useCalendar();

    const [detailDescription, setDetailDescription] = useState("");
    const [hasDetailDescription, setHasDetailDescription] = useState(false);
    const [commonDescription, setCommonDescription] = useState("");
    const [hasCommonDescription, setHasCommonDescription] = useState(false);

    const [isDetailDescriptionEditMode, setIsDetailDescriptionEditMode] = useState(false);
    const [isCommonDescriptionEditMode, setIsCommonDescriptionEditMode] = useState(false);

    const [isModalOpen, setIsModalOpen] = useState(false);

    const [isItemEditMode, setIsItemEditMode] = useState(false);

    const startDateInfo = extractDateInfo(selectedEvent.start);
    const endDateInfo = extractDateInfo(selectedEvent.end);
    const [title, setTitle] = useState(selectedEvent.title);
    const [startDate, setStartDate] = useState(startDateInfo);
    const [endDate, setEndDate] = useState(endDateInfo);

    const [titleError, setTitleError] = useState("");
    const [dateObjectError, setDateObjectError] = useState("");
    const [startDateError, setStartDateError] = useState("");
    const [endDateError, setEndDateError] = useState("");

    useEffect(() => {
        setDetailDescription(selectedEvent.extendedProps.detailDescription);
        setHasDetailDescription(selectedEvent.extendedProps.detailDescription.length > 0);

        setCommonDescription(selectedEvent.extendedProps.commonDescription);
        setHasCommonDescription(selectedEvent.extendedProps.commonDescription.length > 0);

        setIsDetailDescriptionEditMode(false);
        setIsCommonDescriptionEditMode(false);
        setIsItemEditMode(false);

        setDateObjectError("");
        setTitleError("");
        setStartDateError("");
        setEndDateError("");

        setIsModalOpen(false);

        setTitle(selectedEvent.title);
        setStartDate(startDateInfo);
        setEndDate(endDateInfo);
    }, [selectedEvent]);


    const handleDetailDescriptionChange = (e) => {
        const inputValue = e.target.value;
        setDetailDescription(inputValue);
    }

    const handleCommonDescriptionChange = (e) => {
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
    };

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

    const handleEditDetailDescription = () => {
        setIsDetailDescriptionEditMode(true);
    }

    const handleEditCommonDescription = () => {
        setIsCommonDescriptionEditMode(true);
    }

    const handleItemEditButtonClick = () => {
        setIsItemEditMode(true);
    }

    const handleDetailEditCancel = () => {
        setDetailDescription(selectedEvent.extendedProps.detailDescription);
        setIsDetailDescriptionEditMode(false);
    }

    const handleCommonEditCancel = () => {
        setCommonDescription(selectedEvent.extendedProps.commonDescription);
        setIsCommonDescriptionEditMode(false);
    }

    const handleEditCancel = () => {
        setDetailDescription(selectedEvent.extendedProps.detailDescription);
        setCommonDescription(selectedEvent.extendedProps.commonDescription);
        const startDateInfo = extractDateInfo(selectedEvent.start);
        const endDateInfo = extractDateInfo(selectedEvent.end);

        setTitle(selectedEvent.title);
        setTitleError("")

        setStartDate({
            date: startDateInfo.date,
            period: startDateInfo.period,
            hour: startDateInfo.hour,
            minute: startDateInfo.minute,
        });

        setEndDate({
            date: endDateInfo.date,
            period: endDateInfo.period,
            hour: endDateInfo.hour,
            minute: endDateInfo.minute,
        });
        setDateObjectError("");
        setIsItemEditMode(false);
    }

    const handleItemEditSubmit = async () => {
        const startDateTime = createLocalDateTime(startDate);
        const endDateTime = createLocalDateTime(endDate);

        if (!validateTitle(title, setTitleError)
            || !validateDateInput(startDate, setStartDateError)
            || !validateDateInput(endDate, setEndDateError)) {
            return;
        }

        if (!isStartDateBeforeEndDate(startDateTime, endDateTime, setDateObjectError)) {
            return;
        }

        const urlOfItemData = "/schedule/fixed/detail";
        const urlOfTotalData = "/schedule/fixed";
        const itemData = {
            id: selectedEvent.extendedProps.originId,
            startDateTime: startDateTime,
            endDateTime: endDateTime,
            detailDescription: detailDescription,
        };
        const totalData = {
            id: selectedEvent.extendedProps.parentId,
            title: title,
            commonDescription: commonDescription,
        }
        const itemResponse = await authenticatedApi.patch(
            urlOfItemData,
            itemData,
            {apiEndPoint: API_ENDPOINT_NAMES.EDIT_F_SCHEDULE_ITEM,}
        );

        const totalResponse = await authenticatedApi.put(
            urlOfTotalData,
            totalData,
            {apiEndPoint: API_ENDPOINT_NAMES.EDIT_F_SCHEDULE},
        );

        const itemSuccess = isSuccess(itemResponse);
        const totalSuccess = isSuccess(totalResponse);

        if (itemSuccess && totalSuccess) {
            updateExtendedProps(selectedEvent.id, ['detailDescription'], [detailDescription]);
            updateProps(selectedEvent.id, ['start', 'end'], [startDateTime, endDateTime]);
            setHasDetailDescription(detailDescription.length > 0);

            updateExtendedPropsByGroupId(selectedEvent.groupId, ['commonDescription'], [commonDescription])
            setHasCommonDescription(commonDescription.length > 0);
            updatePropsByGroupId(selectedEvent.groupId, ['title'], [title]);

            alert(EVENT_DETAILS_VALIDATE_MESSAGE.MODIFICATION_SUCCESS);
            setIsItemEditMode(false);
        } else {
            if (!itemSuccess) {
                await authenticatedApi.put(
                    urlOfTotalData,
                    {
                        id: selectedEvent.extendedProps.parentId,
                        title: selectedEvent.title,
                        commonDescription: selectedEvent.extendedProps.commonDescription,
                    }
                )
            }

            if (!totalSuccess) {
                const start = dateToLocalDateTime(selectedEvent.start);
                const end = dateToLocalDateTime(selectedEvent.end);

                await authenticatedApi.patch(
                    urlOfItemData,
                    {
                        id: selectedEvent.extendedProps.originId,
                        startDateTime: start,
                        endDateTime: end,
                        detailDescription: selectedEvent.extendedProps.detailDescription,
                    },
                    {apiEndPoint: API_ENDPOINT_NAMES.EDIT_F_SCHEDULE_ITEM,}
                );
            }
            alert(EVENT_DETAILS_VALIDATE_MESSAGE.MODIFICATION_FAIL);
        }
    }

    const handleCommonDescriptionSubmit = async () => {
        const url = "/schedule/fixed";
        const data = {
            id: selectedEvent.extendedProps.parentId,
            title: selectedEvent.title,
            commonDescription: commonDescription,
        }
        const response = await authenticatedApi.put(
            url,
            data,
            {apiEndPoint: API_ENDPOINT_NAMES.EDIT_F_SCHEDULE},
        );
        if (await isSuccess(response)) {
            updateExtendedPropsByGroupId(selectedEvent.groupId, ['commonDescription'], [commonDescription]);
            setHasCommonDescription(commonDescription.length > 0);
            alert(EVENT_DETAILS_VALIDATE_MESSAGE.MEMO_SUCCESS);
            setIsCommonDescriptionEditMode(false);
        } else {
            alert(EVENT_DETAILS_VALIDATE_MESSAGE.MEMO_FAIL);
        }
    }

    const handleDetailDescriptionSubmit = async () => {
        const url = "/schedule/fixed/detail";
        const data = {
            id: selectedEvent.extendedProps.originId,
            startDateTime: formatDateToLocalDateTime(selectedEvent.start),
            endDateTime: formatDateToLocalDateTime(selectedEvent.end),
            detailDescription: detailDescription,
        };
        const response = await authenticatedApi.patch(
            url,
            data,
            {apiEndPoint: API_ENDPOINT_NAMES.EDIT_F_SCHEDULE_ITEM,},
        );
        if (await isSuccess(response)) {
            updateExtendedProps(selectedEvent.id, ['detailDescription'], [detailDescription]);
            setHasDetailDescription(detailDescription.length > 0);
            alert(EVENT_DETAILS_VALIDATE_MESSAGE.MEMO_SUCCESS);
            setIsDetailDescriptionEditMode(false);
        } else {
            alert(EVENT_DETAILS_VALIDATE_MESSAGE.MEMO_FAIL);
        }
    }

    const handleDeleteButtonClick = () => {
        openModal();
    }

    const handleTotalDelete = async() => {
        const url = `/schedule/fixed/${selectedEvent.extendedProps.parentId}`;
        const response = await authenticatedApi.delete(
            url,
            {apiEndPoint: API_ENDPOINT_NAMES.DELETE_SCHEDULE}
        );
        if (await isSuccess(response)) {
            alert(EVENT_DETAILS_VALIDATE_MESSAGE.DELETE_SUCCESS);
            deleteEventsByGroupId(selectedEvent.groupId);
            closeModal();
            onClose();
        } else {
            alert(EVENT_DETAILS_VALIDATE_MESSAGE.DELETE_FAIL);
        }
    }

    const handleTotalDeleteFromNow = async () => {
        const earliestStart = getEarliestStartByGroupId(selectedEvent.groupId);

        if (formatDateToLocalDateTime(selectedEvent.start) === earliestStart) {
            await handleTotalDelete();
            return;
        }

        const url = `/schedule/fixed/detail?parentId=${selectedEvent.extendedProps.parentId}&startDate=${formatDateToLocalDateTime(selectedEvent.start)}`;
        const response = await authenticatedApi.delete(
            url,
            {apiEndPoint: API_ENDPOINT_NAMES.DELETE_F_SCHEDULE_FROM_NOW},
        );
        if (await isSuccess(response)) {
            alert(EVENT_DETAILS_VALIDATE_MESSAGE.DELETE_SUCCESS);
            deleteEventsAfterDateByGroupId(selectedEvent.groupId, selectedEvent.start);
            closeModal();
            onClose();
        } else {
            alert(EVENT_DETAILS_VALIDATE_MESSAGE.DELETE_FAIL);
        }
    }

    const handleItemDelete = async () => {
        const currentEventCounts = countEventsByGroupId(selectedEvent.groupId);

        if (currentEventCounts === 1) {
            await handleTotalDelete();
            return;
        }

        const url = `/schedule/fixed/detail/${selectedEvent.extendedProps.originId}`;
        const response = await authenticatedApi.delete(
            url,
            {apiEndPoint: API_ENDPOINT_NAMES.DELETE_F_SCHEDULE_ITEM},
        );
        if (await isSuccess(response)) {
            alert(EVENT_DETAILS_VALIDATE_MESSAGE.DELETE_SUCCESS);
            deleteEvent(selectedEvent.id);
            closeModal();
            onClose();
        } else {
            alert(EVENT_DETAILS_VALIDATE_MESSAGE.DELETE_FAIL);
        }
    }

    const openModal = () => {
        setIsModalOpen(true);
    };
    const closeModal = () => {
        setIsModalOpen(false);
    };

    return (
        <>
            <div id="event-details-container-fixed">
                <div className="event-details-header-fixed">
                    <h2>일정 상세보기</h2>
                    <button className="close-event-details" onClick={onClose}>&times;</button>
                </div>
                <div className="event-detail-props">
                    {!isItemEditMode ? (
                        <>
                            <div className="event-detail-prop">
                                <h2>일정 제목</h2>
                                <hr className="event-detail-contour"/>
                                <p>{title}</p>
                            </div>
                            <div className="event-detail-prop">
                                <h2>
                                    <strong>시작 시간</strong>
                                </h2>
                                <hr className="event-detail-contour"/>
                                <TimeEditForm
                                    type={"start"}
                                    date={startDate}
                                    setDate={handleStartDateChange}
                                    isDisabled={true}
                                />
                            </div>
                            <div className="event-detail-prop">
                                <h2>
                                    <strong>종료 시간</strong>
                                </h2>
                                <hr className="event-detail-contour"/>
                                <TimeEditForm
                                    type={"end"}
                                    date={endDate}
                                    setDate={handleEndDateChange}
                                    isDisabled={true}
                                />
                            </div>
                        </>
                    ) : (
                        <>
                            <div className="event-detail-prop">
                                <h2>일정 제목</h2>
                                <hr className="event-detail-contour"/>
                                <input
                                    type="text"
                                    id="edit-schedule-title-fixed"
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
                                    type={"start"}
                                    date={startDate}
                                    setDate={handleStartDateChange}
                                    isDisabled={false}
                                />
                                {startDateError && <p className="error-message">{startDateError}</p>}
                            </div>
                            <div className="event-detail-prop">
                                <h2>
                                    <strong>종료 시간</strong>
                                </h2>
                                <hr className="event-detail-contour"/>
                                <TimeEditForm
                                    type={"end"}
                                    date={endDate}
                                    setDate={handleEndDateChange}
                                    isDisabled={false}
                                />
                                {endDateError && <p className="error-message">{endDateError}</p>}
                            </div>
                            {dateObjectError && <p className="error-message">{dateObjectError}</p>}
                        </>
                    )}
                    <div className="event-detail-prop">
                        <div className="description-header">
                            <h2>
                                <strong>일정 공통 메모</strong>
                            </h2>
                            {!hasCommonDescription && !isCommonDescriptionEditMode && !isItemEditMode &&
                                <div className="description-btns">
                                    <div className="description-btns">
                                        <p className="description-btn fixed"
                                           onClick={handleEditCommonDescription}>수정</p>
                                    </div>
                                </div>
                            }
                        </div>
                        <hr className="event-details-contour"/>
                        {isItemEditMode || isCommonDescriptionEditMode ? (
                            <textarea
                                className="description-textarea fixed"
                                value={commonDescription}
                                onChange={handleCommonDescriptionChange}
                                placeholder={"일정 공통 메모를 입력해주세요."}
                                cols="30"
                                rows="3"
                            />
                        ) : hasCommonDescription ? (
                            <p>{commonDescription}</p>
                        ) : (
                            <p className="no-description">등록된 일정 공통 메모가 없습니다.</p>
                        )}
                        {isCommonDescriptionEditMode &&
                            <div className="description-edit-btns">
                                <button
                                    className="description-edit-btn fixed"
                                    onClick={handleCommonDescriptionSubmit}
                                >등록
                                </button>
                                <button
                                    className="description-cancel-btn"
                                    onClick={handleCommonEditCancel}
                                >취소
                                </button>
                            </div>
                        }
                    </div>

                    <div className="event-detail-prop">
                        <div className="description-header">
                            <h2>
                                <strong>일정 개별 메모</strong>
                            </h2>
                            {!hasDetailDescription && !isDetailDescriptionEditMode && !isItemEditMode &&
                                <div className="description-btns">
                                    <div className="description-btns">
                                        <p className="description-btn fixed"
                                           onClick={handleEditDetailDescription}>수정</p>
                                    </div>
                                </div>
                            }
                        </div>
                        <hr className="event-details-contour"/>
                        {isItemEditMode || isDetailDescriptionEditMode ? (
                            <textarea
                                className="description-textarea fixed"
                                value={detailDescription}
                                onChange={handleDetailDescriptionChange}
                                placeholder={"일정 개별 메모를 입력해주세요."}
                                cols="30"
                                rows="3"
                            />
                        ) : hasDetailDescription ? (
                            <p>{detailDescription}</p>
                        ) : (
                            <p className="no-description">등록된 일정 개별 메모가 없습니다.</p>
                        )}
                        {isDetailDescriptionEditMode &&
                            <div className="description-edit-btns">
                                <button
                                    className="description-edit-btn fixed"
                                    onClick={handleDetailDescriptionSubmit}
                                >등록
                                </button>
                                <button
                                    className="description-cancel-btn"
                                    onClick={handleDetailEditCancel}
                                >취소
                                </button>
                            </div>
                        }
                    </div>
                    {(!isItemEditMode && !(isDetailDescriptionEditMode || isCommonDescriptionEditMode)) &&
                        <>
                            <button
                                className="fixed-edit-btn"
                                onClick={handleItemEditButtonClick}>
                                일정 정보 수정
                            </button>
                            <button
                                className="edit-cancel-btn"
                                onClick={handleDeleteButtonClick}>
                                삭제
                            </button>
                        </>

                    }
                    {isItemEditMode &&
                        <>
                            <button
                                className="fixed-edit-btn"
                                onClick={handleItemEditSubmit}
                                disabled={!!titleError || !!dateObjectError || !!startDateError || !!endDateError}>
                                수정
                            </button>

                            <button
                                className="edit-cancel-btn"
                                onClick={handleEditCancel}>
                                취소
                            </button>
                        </>
                    }

                </div>

                <ScheduleDeleteModal
                    onClose={closeModal}
                    isOpen={isModalOpen}
                    onDeleteSingle={handleItemDelete}
                    onDeleteAllFromNow={handleTotalDeleteFromNow}
                    onDeleteAll={handleTotalDelete}
                />
            </div>
        </>
    );
}

FScheduleDetails.propTypes = {
    selectedEvent: PropTypes.shape({
        id: PropTypes.string.isRequired,
        groupId: PropTypes.string,
        title: PropTypes.string.isRequired,
        start: PropTypes.instanceOf(Date).isRequired,
        end: PropTypes.instanceOf(Date).isRequired,
        extendedProps: PropTypes.shape({
            type: PropTypes.string.isRequired,
            commonDescription: PropTypes.string,
            detailDescription: PropTypes.string,
            originId: PropTypes.number,
            parentId: PropTypes.number,
        }),
    }),
    onClose: PropTypes.func.isRequired,
}

export default FScheduleDetails;