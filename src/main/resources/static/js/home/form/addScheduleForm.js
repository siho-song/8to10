function submitAddScheduleForm(timeslots, calendar) {
    // 공통 입력에 대한 처리
    const commonProps = initCommonProperties();
    const { type, title, startDate, endDate, commonDescription } = commonProps;
    //type, title, startDate, endDate, description 초기화

    let event = {
        title: title,
        commonDescription: commonDescription,
        type: type // 이벤트 타입을 추가하여 구분
    };

    if (type === 'variable') { //변동 일정
        const variableProperties = initFixAndVariableProperties(startDate, endDate);
        const { startDateTime, endDateTime } = variableProperties;
        a = 10;
        
        event.start = startDateTime;
        event.end = endDateTime;

        // 서버로 이벤트 객체 전송
        fetch('http://localhost:8080/schedule/variable/add', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(event)
        })
            .then(response => response.json())
            .then(data => {
                console.log('Success:', data);
                calendar.addEvent(data);
                addToDoItem(event); // 일정 추가 시 To-Do 리스트에 반영
            })
            .catch((error) => {
                console.error('Error:', error);
            });

        console.log(event)

    }

    else if (type === 'fixed') {

        const events = timeslots.map(slot => {
            return {
                startTime: slot.startTime,
                duration: `${slot.duration}:00`,
                frequency: slot.frequency,
                days: slot.days
            };
        });
        const fixedEvents = {
            title:title,
            commonDescription:commonDescription,
            startDate:startDate,
            endDate:endDate,
            events:events
        }
        console.log(fixedEvents);


        // 서버로 이벤트 객체 전송
        fetch('http://localhost:8080/schedule/fixed/add', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(fixedEvents)
        })
            .then(response => response.json())
            .then(data => {
                console.log('Success:', data);
                if (data.events && typeof data.events === 'object') {
                    Object.keys(data.events).forEach(key => {
                        const e = data.events[key];
                        calendar.addEvent(e);
                        console.log(e);
                    });
                } else {
                    console.error('Expected an object but got:', data);
                }
                // addToDoItem(data); // 일정 추가 시 To-Do 리스트에 반영
            })
            .catch((error) => {
                console.error('Error:', error);
            });

        // event.forEach(e => calendar.addEvent(e));
        timeslots.length = 0;
        renderTimeslots(timeslots);
        document.getElementById('add-timeslot-btn').style.display = 'none';
        document.getElementById('add-timeslot-btn').disabled = true;
    }

    //일반 일정
    else if (type === 'normal') {
        const normalProperties = initFixAndVariableProperties(startDate, endDate);
        //TODO initNormalProperties로 변경
        const { startDateTime, endDateTime } = normalProperties;

        const frequency = document.getElementById('schedule-frequency').value;
        const bufferTime = document.getElementById('schedule-buffer-time').value;
        const weekdays = Array.from(document.querySelectorAll('input[name="weekdays"]:checked')).map(checkbox => checkbox.value);

        // TODO: 서버로 요청 전송, 서버에서 생성한 랜덤시간을 받아서 랜덤시간 주입해야 함

        event.start = startDateTime; // + 랜덤 시간
        event.end = endDateTime; // + 랜덤 시간
        event.rrule = {
            freq: frequency.toUpperCase(),
            interval: 1,
            byweekday: weekdays,
            dtstart: startDateTime,
            until: endDateTime
        };
        event.duration = '01:00';
        event.bufferTime = bufferTime;
        event.type = type;

        calendar.addEvent(event);
        addToDoItem(event); // 일정 추가 시 To-Do 리스트에 반영
    }
    hideScheduleForm();
}

function hideScheduleForm() {
    document.getElementById('schedule-form-container').style.display = 'none';
    document.getElementById('schedule-form').reset();
}

function initCommonProperties() {
    const form = document.getElementById('schedule-form');
    const type = form.dataset.type; //"fixed", "variable", "normal"
    const title = document.getElementById('schedule-title').value;
    const startDate = document.getElementById('schedule-start-date').value;
    const endDate = document.getElementById('schedule-end-date').value;
    const commonDescription = document.getElementById('schedule-description').value;

    return {
        type,
        title,
        startDate,
        endDate,
        commonDescription
    };
}

function initFixAndVariableProperties(startDate, endDate) {
    const startHour = document.getElementById('schedule-start-hour').value;
    const startMinute = document.getElementById('schedule-start-minute').value;
    const endHour = document.getElementById('schedule-end-hour').value;
    const endMinute = document.getElementById('schedule-end-minute').value;

    const startDateTime = convertTo24HourFormat(startDate, startHour, startMinute);
    const endDateTime = convertTo24HourFormat(endDate, endHour, endMinute);

    return {
        startDateTime,
        endDateTime
    };
}
