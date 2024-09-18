document.addEventListener('DOMContentLoaded', function() {

    const calendarEl = document.getElementById('calendar');
    const calendar = new FullCalendar.Calendar(calendarEl, {
        initialDate: new Date(),
        initialView: 'dayGridMonth',
        nowIndicator: true,
        locale: 'ko',
        headerToolbar: getHeaderToolbarOptions(),
        dayMaxEvents: false,
        allDaySlot : false,
        firstDay: 1,
        events: {},
        eventLimit: 3,
        titleRangeSeparator: ' - ',
        windowResize: () => handleWindowResize(calendar),
        dateClick: (info) => handleDateClick(calendar, info),
        eventClick: (info) => displayEventDetailsInSidebar(info.event),
        eventDidMount: function(info) {
            // displayEventDetails(info.event);
        }
    });
    calendar.render();


    // 일정 생성폼을 위한 팝업창
    document.getElementById('toggle-add-schedule-btn').addEventListener('click', function() {
        document.getElementById('schedule-type-popup').style.display = 'block';  /* 팝업 표시 */
    });

    // 일정 생성 팝업 닫기
    document.querySelector('.close-button').addEventListener('click', function() {
        document.getElementById('schedule-type-popup').style.display = 'none';  /* 팝업 숨기기 */
    });

    // 일정 생성 폼 제출
    document.getElementById('schedule-form').addEventListener('submit', function(e) {
        e.preventDefault();
        submitAddScheduleForm(calendar);
    });


    const titleInput = document.getElementById('schedule-title');
    const saveBtn = document.getElementById('submit-button');

    titleInput.addEventListener('input', function () {
        const titleLength = titleInput.value.length;

        if (titleLength > 80) {
            showTooltip(titleInput, '글자 제한은 80자 입니다.');
            titleInput.value = titleInput.value.substring(0, 80);
        }

        if(document.getElementById('schedule-form').dataset.type === 'fixed') {
            saveBtn.disabled = (titleLength === 0);
        }
    });

    // 서버에서 일정 요청
    fetch('http://localhost:8080/schedule', {
        method: 'GET',
    })
        .then(response => {
            if (!response.ok) {
                throw new Error('Network response was not ok ' + response.statusText);
            }
            return response.json(); // JSON 데이터를 파싱
        })
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
        })
        .catch((error) => {
            console.error('Error:', error);
        });


    // 날짜 네비게이션 기능
    const currentDateEl = document.getElementById('current-date');
    const updateDate = () => currentDateEl.textContent = calendar.getDate().toLocaleDateString();

    document.getElementById('prev-date-btn').addEventListener('click', () => {
        handleDateClick(calendar, info);
        updateDate();
    });
    document.getElementById('next-date-btn').addEventListener('click', () => {
        handleDateClick(calendar, info);
        updateDate();
    });
    updateDate();


    // 제출 버튼 기능
    document.getElementById('submit-todo-btn').addEventListener('click', () => {
        const events = calendar.getEvents();
        const todoItems = [];
        events.forEach(event => {
            todoItems.push({
                type: event.extendedProps.type || 'normal',
                id: event.id,
                completeStatus: event.extendedProps.completeStatus || false
            });
        });
        fetch('http://localhost:8080/schedule/todo', {
            method: 'PUT',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(todoItems)
        })
            .then(response => response.json())
            .then(data => {
                console.log('Success:', data);
            })
            .catch(error => console.error('Error:', error));
    });

    // 일정 상세보기 기능
    function displayEventDetails(event) {
        const modal = document.getElementById('event-details-modal');
        const details = document.getElementById('event-details');
        details.innerHTML = `
            <p>제목: ${event.title}</p>
            <p>시작: ${event.start.toLocaleString()}</p>
            <p>종료: ${event.end.toLocaleString()}</p>
            <p>설명: ${event.extendedProps.commonDescription || ''}</p>
            <p>타입: ${event.extendedProps.type || 'normal'}</p>
        `;
        modal.style.display = 'block';
    }

    // 팝업 닫기 기능
    document.querySelector('.close-button').addEventListener('click', () => {
        document.getElementById('event-details-modal').style.display = 'none';
    });

    document.querySelector('#cancel-btn').addEventListener('click', () => {
        hideScheduleForm();
    });

    window.addEventListener('click', (event) => {
        if (event.target === document.getElementById('event-details-modal')) {
            document.getElementById('event-details-modal').style.display = 'none';
        }
    });

});

function handleWindowResize(calendar) {
    if (window.innerWidth < 768) {
        calendar.changeView('dayGridDay');
    } else if (window.innerWidth >= 768 && window.innerWidth < 1024) {
        calendar.changeView('dayGridWeek');
    } else {
        calendar.changeView('dayGridMonth');
    }
}

function getHeaderToolbarOptions() {
    if (window.innerWidth < 480) {
        return { left: 'prev,next', center: 'title', right: 'dayGridMonth,timeGridDay' };
    } else {
        return { left: 'prev,next today', center: 'title', right: 'dayGridMonth,timeGridWeek,dayGridDay' };
    }
}

function handleDateClick(calendar, info) {
    calendar.changeView('timeGridWeek', info.dateStr);
}